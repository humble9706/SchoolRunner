package com.toborehumble.schoolrunner.pojo;

public class FriendRequest {
    private User requestFrom;
    private User requestTo;
    private String key;
    private long timeStamp;
    private boolean accepted;

    public FriendRequest(){}

    public FriendRequest(User requestFrom, User requestTo, String key) {
        this.requestFrom = requestFrom;
        this.requestTo = requestTo;
        this.key = key;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public User getUserFrom() {
        return requestFrom;
    }

    public void setUserFrom(User requestFrom) {
        this.requestFrom = requestFrom;
    }

    public User getUserTo() {
        return requestTo;
    }

    public void setUserTo(User requestTo) {
        this.requestTo = requestTo;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
