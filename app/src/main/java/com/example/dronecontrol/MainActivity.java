package com.example.dronecontrol;

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
    }

    public void Sign_In(View view) {
        auth.signInWithEmailAndPassword(emailInputEditText.getText().toString() , editTextPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Success",Toast.LENGTH_SHORT).show();
                            UserUid.user_uid = auth.getCurrentUser().getUid();
                            // add intent to next activity.
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
                            Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            UserUid.user_uid = auth.getCurrentUser().getUid();
                            // add intent to next activity.
                        } else {
                            task.getException().printStackTrace();
                            Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}