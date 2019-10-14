package com.toborehumble.schoolrunner.pojo;

public class Broadcast {
    private long timeStamp;
    private String broadcastMessage;
    private String broadcastPhoto;
    private String broadcaster;

    public Broadcast(){}

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Broadcast(String broadcastMessage, String broadcastPhoto, String broadcaster) {
        this.broadcastMessage = broadcastMessage;
        this.broadcastPhoto = broadcastPhoto;
        this.broadcaster = broadcaster;
        this.timeStamp = System.currentTimeMillis();
    }

    public String getBroadcastMessage() {
        return broadcastMessage;
    }

    public void setBroadcastMessage(String broadcastMessage) {
        this.broadcastMessage = broadcastMessage;
    }

    public String getBroadcastPhoto() {
        return broadcastPhoto;
    }

    public void setBroadcastPhoto(String broadcastPhoto) {
        this.broadcastPhoto = broadcastPhoto;
    }

    public String getBroadcaster() {
        return broadcaster;
    }

    public void setBroadcaster(String broadcaster) {
        this.broadcaster = broadcaster;
    }
}
