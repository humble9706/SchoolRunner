package com.toborehumble.schoolrunner;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.toborehumble.schoolrunner.pojo.User;
import com.toborehumble.schoolrunner.recycleradapter.SuggestedUserList;

import java.util.ArrayList;

public class SuggestedUsersActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<User> userObjects;
    SuggestedUserList usersAdapter;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference usersReference;
    FirebaseUser firebaseUser;

    ValueEventListener userListener;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_users);

        toolBar();
        userObjects = new ArrayList<>();
        userObjects = getSuggestedUsersFromFireBase();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        usersAdapter = new SuggestedUserList(SuggestedUsersActivity.this, userObjects);
        recyclerView = findViewById(R.id.suggested_users_recycler);
        layoutManager = new LinearLayoutManager(SuggestedUsersActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(usersAdapter);
    }

    private ArrayList<User> getSuggestedUsersFromFireBase() {
        usersReference = FirebaseDatabase.getInstance().getReference().child("users");
        userListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapShot : dataSnapshot.getChildren()) {
                    if (!userSnapShot.getValue(User.class).getProfile().getEmail().equals(
                            firebaseUser.getEmail()
                    )) {
                        userObjects.add(userSnapShot.getValue(User.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SuggestedUsersActivity.this, "fail 2",
                        Toast.LENGTH_LONG).show();
            }
        };

        usersReference.addValueEventListener(userListener);
        return userObjects;
    }

    private void toolBar() {
        Toolbar toolbar = findViewById(R.id.suggested_users_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeAsUpIndicator(
                getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp)
        );
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    /*@Override
    protected void onPause() {
        super.onPause();
        for(int i = 0; i < 10; i++){
            followersReference.removeEventListener(followersListener);
            usersReference.removeEventListener(userListener);
        }
    }*/
}
