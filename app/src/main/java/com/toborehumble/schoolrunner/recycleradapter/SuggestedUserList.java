package com.toborehumble.schoolrunner.recycleradapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.toborehumble.schoolrunner.R;
import com.toborehumble.schoolrunner.UserDetailActivity;
import com.toborehumble.schoolrunner.pojo.FriendRequest;
import com.toborehumble.schoolrunner.pojo.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SuggestedUserList extends RecyclerView.Adapter<SuggestedUserList.ViewHolder> {
    FirebaseUser authUser;
    private Context context;
    private ArrayList<User> userObjects;

    DatabaseReference dbRef;

    DatabaseReference authUserRef;
    User userFrom;

    public SuggestedUserList(Context context, ArrayList<User> userObjects) {
        this.context = context;
        this.userObjects = userObjects;
        authUser = FirebaseAuth.getInstance().getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference();
        authUserRef = FirebaseDatabase.getInstance().getReference().child("users")
                .child(authUser.getUid());
        authUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userFrom = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setUserObjects(ArrayList<User> userObjects) {
        this.userObjects = userObjects;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.suggested_single_user, viewGroup, false
        );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        final TextView username = viewHolder.username;
        TextView profile_quote = viewHolder.profile_quote;
        ImageView profile_image = viewHolder.profile_photo;
        CardView user_layout = viewHolder.user_layout;
        Button send_request_btn = viewHolder.send_friend_request_btn;

        username.setText(userObjects.get(position).getProfile().getUserName());
        profile_quote.setText(userObjects.get(position).getProfile().getProfileQuote());
        profile_image.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_launcher));

        /*Picasso.get().load(
                Uri.parse(userObjects.get(position).getProfile().getProfilePicture())
        ).into(profile_image);*/
        user_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toUserDetailActivity = new Intent(
                        context, UserDetailActivity.class
                );
                Bundle toUserDetailBundle = new Bundle();
                toUserDetailBundle.putString(
                        "username",
                        userObjects.get(position).getProfile().getUserName()
                );
                toUserDetailBundle.putString(
                        "userUid",
                        userObjects.get(position).getProfile().getUid()
                );

                toUserDetailActivity.putExtras(toUserDetailBundle);
                context.startActivity(toUserDetailActivity);
            }
        });

        send_request_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFriendRequest(position);
            }
        });
    }

    private void sendFriendRequest(int position) {
        String key =
                dbRef.child("users").child(authUser.getUid()).child("friend_requests_made").push().getKey();

        FriendRequest friendRequestObject = new FriendRequest(
                userFrom, userObjects.get(position), key
        );

        Map<String, Object> requestUpdates = new HashMap<>();
        requestUpdates.put("/users/" + authUser.getUid() + "/friend_requests_made/" + key,
                friendRequestObject);

        requestUpdates.put("/users/" + userObjects.get(position).getProfile().getUid() + "/friend_requests_received/" + key,
                friendRequestObject);

        dbRef.updateChildren(requestUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Friend request sent", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Friend request not sent",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userObjects.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView username;
        private TextView profile_quote;
        private ImageView profile_photo;
        private CardView user_layout;
        private Button send_friend_request_btn;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            send_friend_request_btn = itemView.findViewById(R.id.send_friend_request_btn);
            username = itemView.findViewById(R.id.suggested_username);
            profile_quote = itemView.findViewById(R.id.suggested_profile_quote);
            profile_photo = itemView.findViewById(R.id.suggested_profile_image);
            user_layout = itemView.findViewById(R.id.suggested_single_user_card_layout);
        }
    }
}
