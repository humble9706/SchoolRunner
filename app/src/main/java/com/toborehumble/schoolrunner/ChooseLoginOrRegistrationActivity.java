package com.toborehumble.schoolrunner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChooseLoginOrRegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_login_or_registration);

        Toast.makeText(this, "Choose", Toast.LENGTH_SHORT).show();

        Button btn_login = findViewById(R.id.btn_login);
        Button btn_register = findViewById(R.id.btn_register);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toLoginActivity = new Intent(
                        ChooseLoginOrRegistrationActivity.this,
                        LoginActivity.class
                );
                toLoginActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(toLoginActivity);
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toRegistrationActivity = new Intent(
                        ChooseLoginOrRegistrationActivity.this,
                        RegistrationActivity.class
                );
                toRegistrationActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(toRegistrationActivity);
            }
        });
    }
}
