package com.tiy.webapp;


/**
 * Created by Paul Dennis on 1/25/2017.
 */
public class ContactResponseRequest {

    String email;
    int requestId;
    boolean accept;

    public ContactResponseRequest () {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public boolean isAccept() {
        return accept;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }
}
