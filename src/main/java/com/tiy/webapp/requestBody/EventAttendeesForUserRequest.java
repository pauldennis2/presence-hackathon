package com.tiy.webapp.requestBody;

/**
 * Created by Paul Dennis on 1/30/2017.
 */
public class EventAttendeesForUserRequest {

    Long eventId;
    String email;

    public EventAttendeesForUserRequest () {

    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
