package com.toborehumble.schoolrunner.recycleradapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.toborehumble.schoolrunner.R;
import com.toborehumble.schoolrunner.pojo.Friend;
import com.toborehumble.schoolrunner.pojo.FriendRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FriendRequestsList extends RecyclerView.Adapter<FriendRequestsList.ViewHolder> {
    private FirebaseUser authUser;
    private Context context;
    private ArrayList<FriendRequest> friendRequests;
    DatabaseReference authUserRef;

    public FriendRequestsList() {
    }

    public FriendRequestsList(Context context, ArrayList<FriendRequest> friendRequests) {
        authUser = FirebaseAuth.getInstance().getCurrentUser();
        this.context = context;
        this.friendRequests = friendRequests;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<FriendRequest> getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(ArrayList<FriendRequest> friendRequests) {
        this.friendRequests = friendRequests;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.single_friend_request, parent, false
        );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        TextView username = holder.username;
        Button accept_request_btn = holder.accept_request_btn;
        ImageView request_image = holder.request_image;

        username.setText(friendRequests.get(position).getUserFrom().getProfile().getUserName());

        accept_request_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptFriendRequest(position);
            }
        });

        request_image.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_launcher));
    }

    private void acceptFriendRequest(final int position) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        Friend friend = new Friend(friendRequests.get(position).getUserFrom());
        Friend friendTo = new Friend(friendRequests.get(position).getUserTo());

        String key =
                dbRef.child("users").child(authUser.getUid()).child("friends").push().getKey();

        Map<String, Object> requestUpdates = new HashMap<>();
        requestUpdates.put("/users/" + authUser.getUid() + "/friends/" + key,
                friend);

        requestUpdates.put("/users/" + friendRequests.get(position).getUserFrom()
                        .getProfile().getUid() + "/friends/" + key,
                friendTo);

        dbRef.updateChildren(requestUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Friend request accepted", Toast.LENGTH_LONG).show();
                DatabaseReference reqRef = FirebaseDatabase.getInstance().getReference()
                        .child("users").child(authUser.getUid()).child("friend_requests_received")
                        .child(friendRequests.get(position).getKey());
                reqRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "request deleted", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "request removal failed", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Friend request not accepted",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendRequests.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView username;
        private Button accept_request_btn;
        private ImageView request_image;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.friend_request_username);
            accept_request_btn = itemView.findViewById(R.id.accept_request_btn);
            request_image = itemView.findViewById(R.id.friend_request_image);
        }
    }
}
