package com.toborehumble.schoolrunner.recycleradapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.toborehumble.schoolrunner.R;
import com.toborehumble.schoolrunner.UserDetailActivity;
import com.toborehumble.schoolrunner.pojo.Friend;

import java.util.ArrayList;

public class FriendList extends RecyclerView.Adapter<FriendList.ViewHolder> {
    private Context context;
    private ArrayList<Friend> friendObjects;

    public FriendList(Context context, ArrayList<Friend> friendObjects) {
        this.context = context;
        this.friendObjects = friendObjects;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setUserObjects(ArrayList<Friend> friendObjects) {
        this.friendObjects = friendObjects;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.single_user, viewGroup, false
        );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        final TextView username = viewHolder.username;
        TextView profile_quote = viewHolder.profile_quote;
        ImageView profile_image = viewHolder.profile_photo;
        CardView user_layout = viewHolder.user_layout;

        username.setText(friendObjects.get(position).getFriend().getProfile().getUserName());
        profile_quote.setText(friendObjects.get(position).getFriend().getProfile().getProfileQuote());
        profile_image.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_launcher));

        /*Picasso.get().load(
                Uri.parse(friendObjects.get(position).getProfile().getProfilePicture())
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
                        friendObjects.get(position).getFriend().getProfile().getUserName()
                );
                toUserDetailBundle.putString(
                        "userUid",
                        friendObjects.get(position).getFriend().getProfile().getUid()
                );

                toUserDetailActivity.putExtras(toUserDetailBundle);
                context.startActivity(toUserDetailActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendObjects.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView username;
        private TextView profile_quote;
        private ImageView profile_photo;
        private CardView user_layout;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            profile_quote = itemView.findViewById(R.id.profile_quote);
            profile_photo = itemView.findViewById(R.id.profile_image);
            user_layout = itemView.findViewById(R.id.single_user_card_layout);
        }
    }
}

