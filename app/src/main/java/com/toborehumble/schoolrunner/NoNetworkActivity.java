package com.toborehumble.schoolrunner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class NoNetworkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_network);

        Button to_splash_btn = findViewById(R.id.to_splash_btn);
        to_splash_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSplashActivity = new Intent(
                        NoNetworkActivity.this, SplashActivity.class
                );
                startActivity(toSplashActivity);
                finish();
            }
        });
    }
}
