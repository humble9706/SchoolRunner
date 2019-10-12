package com.toborehumble.schoolrunner;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.toborehumble.schoolrunner.pojo.FriendRequest;

import java.util.HashMap;
import java.util.Map;

public class UserDetailActivity extends AppCompatActivity {
    String username;
    String userUid;
    FirebaseUser authUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        authUser = FirebaseAuth.getInstance().getCurrentUser();

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        username = bundle.getString("username");
        userUid = bundle.getString("userUid");

        toolbar();
    }

    private void toolbar() {
        Toolbar toolbar = findViewById(R.id.user_detail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeAsUpIndicator(
                getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp)
        );
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(username);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_follow_user: {
                sendFriendRequest();
            }
            break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void sendFriendRequest() {
        FriendRequest friendRequestObject = new FriendRequest(
                authUser.getUid(), userUid, System.currentTimeMillis(), false
        );
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

        String key =
                dbRef.child("users").child(authUser.getUid()).child("friend_requests_made").push().getKey();

        Map<String, Object> requestUpdates = new HashMap<>();
        requestUpdates.put("/users/" + authUser.getUid() + "/friend_requests_made/" + key,
                friendRequestObject);

        requestUpdates.put("/users/" + userUid + "/friend_requests_received/" + key,
                friendRequestObject);

        dbRef.updateChildren(requestUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(UserDetailActivity.this, "Friend request sent", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserDetailActivity.this, "Friend request not sent",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
