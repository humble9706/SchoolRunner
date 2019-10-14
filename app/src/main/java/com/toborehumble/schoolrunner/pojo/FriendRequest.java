package com.toborehumble.schoolrunner.pojo;

public class FriendRequest {
    private User requestFrom;
    private User requestTo;
    private long timeStamp;
    private boolean accepted;

    public FriendRequest(){}

    public FriendRequest(User requestFrom, User requestTo) {
        this.requestFrom = requestFrom;
        this.requestTo = requestTo;
        this.timeStamp = System.currentTimeMillis();
        this.accepted = false;
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
