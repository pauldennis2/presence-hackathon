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
    Long id;

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

    public Event(Long id, String eventName, String location, String address) {
        this.id = id;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
