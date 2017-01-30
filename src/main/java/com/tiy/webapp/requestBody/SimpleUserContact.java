package com.tiy.webapp.requestBody;

import com.tiy.webapp.ContactStatus;

import java.sql.Timestamp;

/**
 * Created by Paul Dennis on 1/30/2017.
 */
public class SimpleUserContact {

    String requesterEmail;
    String requesteeEmail;
    ContactStatus status;
    Timestamp recentRequestTime;

    public SimpleUserContact () {

    }

    public SimpleUserContact(String requesterEmail, String requesteeEmail, ContactStatus status, Timestamp recentRequestTime) {
        this.requesterEmail = requesterEmail;
        this.requesteeEmail = requesteeEmail;
        this.status = status;
        this.recentRequestTime = recentRequestTime;
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

    public ContactStatus getStatus() {
        return status;
    }

    public void setStatus(ContactStatus status) {
        this.status = status;
    }

    public Timestamp getRecentRequestTime() {
        return recentRequestTime;
    }

    public void setRecentRequestTime(Timestamp recentRequestTime) {
        this.recentRequestTime = recentRequestTime;
    }
}
