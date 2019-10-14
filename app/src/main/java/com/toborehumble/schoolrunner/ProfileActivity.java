package com.toborehumble.schoolrunner;

import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.toborehumble.schoolrunner.pageradapter.ProfilePagerAdapter;
import com.toborehumble.schoolrunner.pojo.Profile;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    private TextView profile_quote;
    private TextView profile_hobby;
    private ImageView profile_image;
    private TextView profile_username;
    private TextView profile_department;

    FirebaseUser firebaseUser;
    Profile profileObject;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference profileReference;

    FirebaseStorage fireBaseStorage;
    StorageReference storageReference;
    String profilePhotoUrl;

    ViewPager profile_page_pager;
    ProfilePagerAdapter profilePagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolBar();

        profile_hobby = findViewById(R.id.profile_page_hobby);
        profile_image = findViewById(R.id.profile_page_image);
        profile_username = findViewById(R.id.profile_page_username);
        profile_quote = findViewById(R.id.profile_page_profile_quote);
        profile_department = findViewById(R.id.profile_page_department);
        profile_page_pager = findViewById(R.id.profile_page_pager);

        profilePagerAdapter = new ProfilePagerAdapter(
                getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        );

        profile_page_pager.setAdapter(profilePagerAdapter);

        fireBaseStorage = FirebaseStorage.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        String userId = firebaseUser.getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();
        storageReference = fireBaseStorage.getReference();

        profileReference = firebaseDatabase.getReference().child("users").child(userId).child(
                "profile");

        profileReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                profileObject = dataSnapshot.getValue(Profile.class);

                assert profileObject != null;
                profile_hobby.setText(profileObject.getHobby());
                profile_username.setText(profileObject.getUserName());
                profile_department.setText(profileObject.getDepartment());
                profile_quote.setText(profileObject.getProfileQuote());

                Glide.with(ProfileActivity.this).load(Uri.parse(profileObject.getProfilePicture())).into(profile_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(
                        ProfileActivity.this, "Error fetching your profile",
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void toolBar() {
        Toolbar toolbar = findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeAsUpIndicator(
                getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp)
        );
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    private void uploadImage() {
        AssetManager assetManager = getAssets();
        String[] paths = new String[0];
        try {
            paths = assetManager.list("assets");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String path : paths) {
            Toast.makeText(ProfileActivity.this, path, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_edit_profile: {
                uploadImage();
            }
            break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
