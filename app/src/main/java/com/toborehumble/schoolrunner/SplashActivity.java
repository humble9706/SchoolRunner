package com.toborehumble.schoolrunner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        firebaseAuth = FirebaseAuth.getInstance();
        requestCameraPermission();
        requestStorageReadPermission();
        requestStorageWritePermission();
    }

    private void requestStorageWritePermission() {
        ActivityCompat.requestPermissions(SplashActivity.this, new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, 789);
    }

    private void requestStorageReadPermission() {
        ActivityCompat.requestPermissions(SplashActivity.this, new String[] {
                Manifest.permission.READ_EXTERNAL_STORAGE
        }, 456);
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(SplashActivity.this, new String[] {
                Manifest.permission.CAMERA
        }, 123);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null){
            Intent toChooseLoginOrRegistration = new Intent(
                    SplashActivity.this, ChooseLoginOrRegistrationActivity.class
            );
            toChooseLoginOrRegistration.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(toChooseLoginOrRegistration);
            finish();
        }else{
            Intent toMainActivity = new Intent(
                    SplashActivity.this, MainActivity.class
            );
            toMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(toMainActivity);
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 123){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode == 456){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Read permission granted", Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode == 789){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Write permission granted", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
