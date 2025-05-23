package com.example.dronecontrol;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.example.dronecontrol.Structures.UserUid;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private FirebaseAuth auth;
    private Button SignIn_button;
    private Button SignUp_button;
    private TextView signup_text;
    private EditText emailInputEditText;
    private EditText editTextPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SignUp_button = findViewById(R.id.SignUp_button);
        SignIn_button = findViewById(R.id.SignIn_button);
        signup_text = findViewById(R.id.signup_text);
        emailInputEditText = findViewById(R.id.emailInputEditText);
        editTextPassword = findViewById(R.id.editTextPassword);
        auth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("signIn", MODE_PRIVATE);
        if(sharedPreferences.contains("Email"))
        {
            emailInputEditText.setText(sharedPreferences.getString("Email",""));
            editTextPassword.setText(sharedPreferences.getString("Password",""));
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // locks the screen in the horizontol state.
    }

    public void changeSavedCredentials()
    {
        SharedPreferences.Editor ed = sharedPreferences.edit();

        ed.putString("Email",emailInputEditText.getText().toString());
        ed.putString("Password",editTextPassword.getText().toString());

        ed.commit();
    }

    public void Sign_In(View view) {
        if(emailInputEditText.getText().toString().isEmpty() || editTextPassword.getText().toString().isEmpty())
        {
            AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
            adb.setTitle("Failed to SignIn");
            adb.setMessage("Please Input the email and password");
            AlertDialog failureAlert = adb.create();
            failureAlert.show();
        }
        else {
            auth.signInWithEmailAndPassword(emailInputEditText.getText().toString(), editTextPassword.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                userAuthorised();
                            } else {
                                // If sign in fails, display a message to the user.
                                task.getException().printStackTrace();
                                AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
                                adb.setTitle("Failed to SignIn");
                                //adb.setMessage("Your email or password are wrong. please recheck them and retry again later.");
                                adb.setMessage(task.getException().getLocalizedMessage());
                                AlertDialog failureAlert = adb.create();
                                failureAlert.show();
                            }
                        }
                    });
        }
    }

    public void Sign_Up(View view) {
        if(emailInputEditText.getText().toString().isEmpty() || editTextPassword.getText().toString().isEmpty())
        {
            AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
            adb.setTitle("Failed to Sign Up");
            adb.setMessage("If you wish to Sign Up please Input the email and password");
            AlertDialog failureAlert = adb.create();
            failureAlert.show();
        }
        else
        {
            auth.createUserWithEmailAndPassword(emailInputEditText.getText().toString(), editTextPassword.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                userAuthorised();
                            } else {
                                task.getException().printStackTrace();
                                AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
                                adb.setTitle("Failed to SignUp");
                                adb.setMessage(task.getException().getLocalizedMessage());
                                AlertDialog failureAlert = adb.create();
                                failureAlert.show();
                            }
                        }
                    });
        }
    }

    public void userAuthorised()
    {
        UserUid.user_uid = auth.getCurrentUser().getUid();
        changeSavedCredentials();
        StartNextActivity();
    }

    public void StartNextActivity()
    {
        Intent nextActivity = new Intent(this,Selection_Screen.class);
        startActivity(nextActivity);
    }
}