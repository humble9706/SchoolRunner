package com.toborehumble.schoolrunner.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.toborehumble.schoolrunner.R;
import com.toborehumble.schoolrunner.pojo.FriendRequest;
import com.toborehumble.schoolrunner.recycleradapter.FriendRequestsList;

import java.util.ArrayList;

public class FriendRequests extends Fragment {
    private DatabaseReference dbRef;
    private FirebaseUser firebaseUser;
    ValueEventListener friendRequestsListener;

    RecyclerView friend_requests_recycler;
    RecyclerView.LayoutManager layoutManager;
    FriendRequestsList friendRequestsList;
    private ArrayList<FriendRequest> friendRequests;

    public static FriendRequests instantiate(){
        return new FriendRequests();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        friendRequests = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getContext());
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid())
                .child("friend_requests_received");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friend_requests, container, false);
        friendRequests = getFriendRequests();
        friend_requests_recycler = view.findViewById(R.id.friend_requests_recycler);
        friendRequestsList = new FriendRequestsList(getContext(), friendRequests);
        friend_requests_recycler.setLayoutManager(layoutManager);
        friend_requests_recycler.setHasFixedSize(true);
        friend_requests_recycler.setNestedScrollingEnabled(false);
        friend_requests_recycler.setAdapter(friendRequestsList);
        return view;
    }

    private ArrayList<FriendRequest> getFriendRequests() {
        friendRequestsListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                friendRequests.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    friendRequests.add(snapshot.getValue(FriendRequest.class));
                    friendRequestsList.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        };
        dbRef.addValueEventListener(friendRequestsListener);
        return friendRequests;
    }
}
