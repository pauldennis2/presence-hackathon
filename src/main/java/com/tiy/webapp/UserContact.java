package com.tiy.webapp;

/**
 * Created by Paul Dennis on 1/25/2017.
 */
public class UserContact {

    int requestId;
    String requesterEmail;
    String requesteeEmail;

    boolean accepted;
    boolean blocked;

    //LocalDateTime dateTime;

    public UserContact() {

    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getRequesterEmail() {
        return requesterEmail;
    }

    public void setRequesterEmail(String requesterEmail) {
        this.requesterEmail = requesterEmail;
    }

    public String getRequesteeEmail() {
        return requesteeEmail;
    }

    public void setRequesteeEmail(String requesteeEmail) {
        this.requesteeEmail = requesteeEmail;
    }
}
