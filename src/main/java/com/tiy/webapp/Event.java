package com.tiy.webapp;

import javax.persistence.*;

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
    String date;

    @Column(nullable = false)
    String time;

    public Event () {

    }

    public Event(int eventId, String eventName, String location, String address, String date, String time) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.location = location;
        this.address = address;
        this.date = date;
        this.time = time;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
