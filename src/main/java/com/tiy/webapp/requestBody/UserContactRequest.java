package com.tiy.webapp.requestBody;

import com.tiy.webapp.ContactStatus;

/**
 * Created by Paul Dennis on 1/30/2017.
 */
public class UserContactRequest {

    String requesterEmail;
    String requesteeEmail;
    ContactStatus status;

    public UserContactRequest () {

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
}
