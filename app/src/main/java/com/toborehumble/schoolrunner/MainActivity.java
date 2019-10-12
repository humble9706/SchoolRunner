package com.toborehumble.schoolrunner;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.toborehumble.schoolrunner.adapter.StoryList;
import com.toborehumble.schoolrunner.pojo.Story;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Story> storyObjects;
    StoryList storyAdapter;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference storiesReference;
    FirebaseUser firebaseUser;

    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    ValueEventListener storiesListener;

    public static final int READ_EXTERNAL_STORAGE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolBar();
        tabLayout();
        requestStoragePermission();

        createAppDir();

        storyObjects = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        getStoriesFromFireBase();

        storyAdapter = new StoryList(MainActivity.this, storyObjects);
        recyclerView = findViewById(R.id.stories_recycler_view);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(storyAdapter);
    }

    private void createAppDir() {
        File folder = new File(
                Environment.getExternalStorageDirectory() + File.separator + "School Talk"
        );
        if(!folder.exists()){
            if(folder.mkdir()){
                Toast.makeText(this, "Folder created", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getStoriesFromFireBase() {
        storiesReference = FirebaseDatabase.getInstance().getReference().child("stories");
        storiesListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    storyObjects.add(snapshot.getValue(Story.class));
                }
                storyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        storiesReference.addValueEventListener(storiesListener);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(
                MainActivity.this,
                new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                READ_EXTERNAL_STORAGE
        );
    }

    private void tabLayout() {
        TabLayout tabLayout = findViewById(R.id.main_tab);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabPosition = tab.getPosition();
                switch (tabPosition){
                    case 1: {
                        Intent toFriendsActivity = new Intent(
                                MainActivity.this, FriendsActivity.class
                        );
                        startActivity(toFriendsActivity);
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void toolBar() {
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.action_settings: {
                Toast.makeText(this, "Settings clicked", Toast.LENGTH_LONG).show();
            }
            break;
            case R.id.action_search: {
                Toast.makeText(this, "Search clicked", Toast.LENGTH_LONG).show();
            }
            break;
            case R.id.action_logout: {
                logUserOut();
            }
            break;
            case R.id.action_profile: {
                gotoProfilePage();
            }
            break;
        }
        return true;
    }

    private void gotoProfilePage() {
        Intent toProfileActivity = new Intent(
                MainActivity.this, ProfileActivity.class
        );
        startActivity(toProfileActivity);
    }

    protected void logUserOut(){
        firebaseAuth.signOut();
        Intent toSplashActivity = new Intent(
                MainActivity.this, SplashActivity.class
        );
        toSplashActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(toSplashActivity);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
