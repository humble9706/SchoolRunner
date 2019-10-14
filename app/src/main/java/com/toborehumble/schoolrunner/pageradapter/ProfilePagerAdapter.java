package com.toborehumble.schoolrunner.pageradapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.toborehumble.schoolrunner.fragments.FriendRequests;
import com.toborehumble.schoolrunner.fragments.Notifications;

public class ProfilePagerAdapter extends FragmentStatePagerAdapter {
    public ProfilePagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: {
                return FriendRequests.instantiate();
            }
            case 1:{
                return Notifications.instantiate();
            }
            default:{
                return FriendRequests.instantiate();
            }
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
