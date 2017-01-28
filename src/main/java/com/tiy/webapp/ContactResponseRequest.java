package com.tiy.webapp;


/**
 * Created by Paul Dennis on 1/25/2017.
 */
public class ContactResponseRequest {

    String email;
    Long requestId;
    boolean accept;

    public ContactResponseRequest () {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public boolean isAccept() {
        return accept;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }
}
