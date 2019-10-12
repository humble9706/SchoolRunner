package com.toborehumble.schoolrunner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RegistrationActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    Button register_btn;
    Button to_login_btn;
    TextInputEditText email_edit;
    TextInputEditText username_edit;
    TextInputEditText password_edit;
    StorageReference storageReference;

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        email_edit = findViewById(R.id.email_edit_register);
        register_btn = findViewById(R.id.btn_submit_register);
        to_login_btn = findViewById(R.id.btn_to_login);
        username_edit = findViewById(R.id.username_edit_register);
        password_edit = findViewById(R.id.password_edit_register);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser();
                to_login_btn.setEnabled(false);
                register_btn.setEnabled(false);
            }
        });

        to_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toLoginActivity();
            }
        });
    }

    private void toLoginActivity() {
        Intent toLoginActivity = new Intent(
                RegistrationActivity.this, LoginActivity.class
        );
        toLoginActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(toLoginActivity);
    }

    private void signInUser() {
        username = username_edit.getText().toString();
        String userEmail = email_edit.getText().toString();
        String userPassword = password_edit.getText().toString();
        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(
                this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseUser = firebaseAuth.getCurrentUser();
                            toCreateProfileActivity();
                        } else {
                            Toast.makeText(
                                    RegistrationActivity.this, "Sign up failed", Toast.LENGTH_LONG
                            ).show();
                            register_btn.setEnabled(true);
                        }
                    }
                });
    }

    private void toCreateProfileActivity() {
        Intent toCreateProfileActivity = new Intent(
                RegistrationActivity.this, CreateProfileActivity.class
        );
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        toCreateProfileActivity.putExtras(bundle);
        toCreateProfileActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(toCreateProfileActivity);
    }
}
