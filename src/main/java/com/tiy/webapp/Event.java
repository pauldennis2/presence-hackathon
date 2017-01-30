package com.tiy.webapp;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by Paul Dennis on 1/25/2017.
 */
@Entity
@Table(name = "events")
public class Event {

    public static long MILLIS_TO_HOURS = 3600000;
    public static final long MILLIS_TO_24HOURS = 86400000;

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
    Timestamp startTime;

    @Column(nullable = false)
    Timestamp endTime;

    public Event () {

    }

    public Event(String eventName, String location, String address) {
        this.eventName = eventName;
        this.location = location;
        this.address = address;
        startTime = Timestamp.valueOf(LocalDateTime.now());
        endTime = new Timestamp(startTime.getTime() + MILLIS_TO_24HOURS);
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

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        if (endTime != null) {
            if (startTime.after(endTime)) {
                throw new AssertionError("Start time cannot be after end time.");
            }
        }
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        if (startTime != null) {
            if (endTime.before(startTime)) {
                throw new AssertionError("End time cannot be before start time.");
                //what do you think this is, the land before time?
            }
        }
        this.endTime = endTime;
    }

    /**
     *
     * @return true if the current time is within an hour of the event's start time or end time.
     */
    public boolean isActive () {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        long endTimePlusOneHour = this.endTime.getTime() + MILLIS_TO_HOURS;
        if (now.getTime() > endTimePlusOneHour) {
            return false;
        }
        long startTimeMinusOneHour = this.startTime.getTime() - MILLIS_TO_HOURS;
        if (now.getTime() < startTimeMinusOneHour) {
            return false;
        }
        return true;
    }

    /**
     *
     * @return true if the event's end time is after the current time (the event is not over yet.
     * Does not need to have started - the event can be starting a week from now and it is still "current").
     */
    public boolean isCurrent () {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        if (now.after(this.endTime)) {
            return false;
        }
        return true;
    }
}
