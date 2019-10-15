package com.toborehumble.schoolrunner;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import com.toborehumble.schoolrunner.pojo.Friend;
import com.toborehumble.schoolrunner.recycleradapter.FriendList;

import java.util.ArrayList;

public class FriendsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Friend> friendObjects;
    FriendList friendsAdapter;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference usersReference;
    FirebaseUser firebaseUser;

    ValueEventListener userListener;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        toolBar();
        friendObjects = new ArrayList<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        friendObjects = getFriendsFromFireBase();

        sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        friendsAdapter = new FriendList(FriendsActivity.this, friendObjects);
        recyclerView = findViewById(R.id.friends_activity_recycler);
        layoutManager = new LinearLayoutManager(FriendsActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(friendsAdapter);
    }

    private ArrayList<Friend> getFriendsFromFireBase() {
        usersReference = FirebaseDatabase.getInstance().getReference().child("users")
        .child(firebaseUser.getUid()).child("friends");
        userListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot userSnapShot : dataSnapshot.getChildren()) {
                    if (!userSnapShot.getValue(Friend.class).getFriend().getProfile().getEmail().equals(
                            firebaseUser.getEmail()
                    )) {
                        friendObjects.add(userSnapShot.getValue(Friend.class));
                        friendsAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FriendsActivity.this, "fail 2",
                        Toast.LENGTH_LONG).show();
            }
        };

        usersReference.addValueEventListener(userListener);
        return friendObjects;
    }

    private void toolBar() {
        Toolbar toolbar = findViewById(R.id.friends_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeAsUpIndicator(
                getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp)
        );
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.friends_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.action_friends_search: {
                Toast.makeText(
                        FriendsActivity.this, "Friends search clicked", Toast.LENGTH_LONG
                ).show();
            }
            break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
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
