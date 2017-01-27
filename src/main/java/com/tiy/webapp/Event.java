package com.tiy.webapp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by Paul Dennis on 1/25/2017.
 */
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue
    int eventId;

    @Column(nullable = false)
    String eventName;

    @Column(nullable = false)
    String location;

    @Column(nullable = false)
    String address;

    @Column(nullable = false)
    Timestamp dateTime;

    public Event () {

    }

    public Event(int eventId, String eventName, String location, String address) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.location = location;
        this.address = address;
        dateTime = Timestamp.valueOf(LocalDateTime.now());
    }

    public Event(String eventName, String location, String address) {
        this.eventName = eventName;
        this.location = location;
        this.address = address;
        dateTime = Timestamp.valueOf(LocalDateTime.now());
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }
}
