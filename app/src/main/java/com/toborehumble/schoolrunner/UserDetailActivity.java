package com.toborehumble.schoolrunner;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
        return true;
    }
}
