package com.tiy.webapp;

/**
 * Created by Paul Dennis on 1/25/2017.
 */
public class EventSignupRequest {

    String email;
    int eventId;

    public EventSignupRequest () {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
}
