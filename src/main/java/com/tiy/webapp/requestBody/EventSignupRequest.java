package com.tiy.webapp.requestBody;

/**
 * Created by Paul Dennis on 1/25/2017.
 */
public class EventSignupRequest {

    String email;
    Long eventId;

    public EventSignupRequest () {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }
}
