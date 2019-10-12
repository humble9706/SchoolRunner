package com.toborehumble.schoolrunner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    TextInputEditText password_edit;
    TextInputEditText email_edit;
    Button login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_btn = findViewById(R.id.btn_submit);
        email_edit = findViewById(R.id.email_edit);
        password_edit = findViewById(R.id.password_edit);

        firebaseAuth = FirebaseAuth.getInstance();

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logUserIn();
                login_btn.setEnabled(false);
            }
        });
    }

    private void logUserIn() {
        String email = email_edit.getText().toString();
        String password = password_edit.getText().toString();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(
                new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        toSplashActivity();
                    }
                }
        ).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void toSplashActivity() {
        Intent toSplashActivity = new Intent(
                LoginActivity.this, SplashActivity.class
        );
        toSplashActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(toSplashActivity);
    }
}
