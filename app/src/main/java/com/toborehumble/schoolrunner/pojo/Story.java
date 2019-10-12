package com.toborehumble.schoolrunner.pojo;

public class Story {
    private String subjectUsername;
    private String objectUsername;
    private String verb;
    private long timeStamp;

    public Story() {}

    public Story(
            String subjectUsername,
            String objectUsername,
            String verb
    ) {
        this.subjectUsername = subjectUsername;
        this.objectUsername = objectUsername;
        this.verb = verb;
        this.timeStamp = System.currentTimeMillis();
    }

    public String getSubjectUsername() {
        return subjectUsername;
    }

    public void setSubjectUsername(String subjectUsername) {
        this.subjectUsername = subjectUsername;
    }

    public String getObjectUsername() {
        return objectUsername;
    }

    public void setObjectUsername(String objectUsername) {
        this.objectUsername = objectUsername;
    }

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
