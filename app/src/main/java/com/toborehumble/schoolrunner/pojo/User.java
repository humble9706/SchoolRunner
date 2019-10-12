package com.toborehumble.schoolrunner.pojo;

public class User {
    private Profile profile;

    public User(){}

    public User(Profile profile) {
        this.profile = profile;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
