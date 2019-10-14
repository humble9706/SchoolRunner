package com.toborehumble.schoolrunner.pojo;

public class Friend {
    private long timeStamp;
    private User friend;

    public Friend(User friend) {
        this.timeStamp = System.currentTimeMillis();
        this.friend = friend;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }
}
