package com.toborehumble.schoolrunner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.toborehumble.schoolrunner.pojo.Profile;

public class CreateProfileActivity extends AppCompatActivity {

    DatabaseReference dbRef;
    FirebaseUser auth_user;
    DatabaseReference profile_ref;
    String username;

    Button create_profile_btn;
    TextInputEditText profile_quote;
    TextInputEditText profile_hobby;
    TextInputEditText profile_tribe;
    TextInputEditText profile_department;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        create_profile_btn = findViewById(R.id.create_profile_btn);
        profile_quote = findViewById(R.id.profile_quote);
        profile_hobby = findViewById(R.id.profile_hobby);
        profile_tribe = findViewById(R.id.profile_tribe);
        profile_department = findViewById(R.id.profile_department);

        dbRef = FirebaseDatabase.getInstance().getReference();
        auth_user = FirebaseAuth.getInstance().getCurrentUser();
        profile_ref = dbRef.child("users").child(auth_user.getUid());

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        username = bundle.getString("username");

        create_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUserProfile();
            }
        });
    }

    private void createUserProfile() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        String department = profile_department.getText().toString();
        String hobby = profile_hobby.getText().toString();
        String tribe = profile_tribe.getText().toString();
        String quote = profile_quote.getText().toString();

        Profile profileObject = new Profile();
        profileObject.setUserName(username);
        profileObject.setDepartment(department);
        profileObject.setHobby(hobby);
        profileObject.setBirthDay("june 12, 1672");
        profileObject.setTribe(tribe);
        profileObject.setUid(auth_user.getUid());
        profileObject.setFollowersCount(0);
        profileObject.setFollowingCount(0);
        profileObject.setProfileQuote(quote);
        profileObject.setProfilePicture(null);

        reference.child("users").child(auth_user.getUid()).child("profile").setValue(profileObject, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError,
                                   @NonNull DatabaseReference databaseReference) {
                Intent toUploadProfilePicture = new Intent(
                        CreateProfileActivity.this, UploadPictureActivity.class
                );
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                toUploadProfilePicture.putExtras(bundle);
                toUploadProfilePicture.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(toUploadProfilePicture);
            }
        });
    }
}
