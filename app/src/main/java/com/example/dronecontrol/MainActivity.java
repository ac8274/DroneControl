package com.example.dronecontrol;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.example.dronecontrol.Structures.UserUid;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.dronecontrol.databinding.ActivityMainBinding;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
        auth.signInWithEmailAndPassword(emailInputEditText.getText().toString() , editTextPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            onSuccess();
                        } else {
                            // If sign in fails, display a message to the user.
                            task.getException().printStackTrace();
                            Toast.makeText(MainActivity.this, "Failure",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void Sign_Up(View view) {
        auth.createUserWithEmailAndPassword(emailInputEditText.getText().toString(), editTextPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            onSuccess();
                        } else {
                            task.getException().printStackTrace();
                            Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
//added
    //cg
    public void onSuccess()
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