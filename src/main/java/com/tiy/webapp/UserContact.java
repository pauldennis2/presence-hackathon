package com.tiy.webapp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by Paul Dennis on 1/25/2017.
 */

@Entity
@Table(name = "contacts")
public class UserContact {

    public static final long MILLIS_TO_24HOURS = 86400000;

    @GeneratedValue
    @Id
    Long id;

    @ManyToOne(cascade=CascadeType.MERGE)
    @JsonBackReference
    User requester;

    @ManyToOne(cascade=CascadeType.MERGE)
    @JsonBackReference
    User requestee;

    @Column(nullable = false)
    ContactStatus status;

    @Column(nullable = false)
    Timestamp originalRequestTime;

    @Column(nullable = true)
    Timestamp refreshRequestTime;

    public UserContact() {
        originalRequestTime = Timestamp.valueOf(LocalDateTime.now());
    }

    public UserContact(User requester, User requestee, ContactStatus status) {
        this.requester = requester;
        this.requestee = requestee;
        this.status = status;
        originalRequestTime = Timestamp.valueOf(LocalDateTime.now());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContactStatus getStatus() {
        return status;
    }

    public void setStatus(ContactStatus status) {
        this.status = status;
    }

    public Timestamp getOriginalRequestTime() {
        return originalRequestTime;
    }

    public void setOriginalRequestTime(Timestamp originalRequestTime) {
        this.originalRequestTime = originalRequestTime;
    }

    public Timestamp getRefreshRequestTime() {
        return refreshRequestTime;
    }

    public void setRefreshRequestTime(Timestamp refreshRequestTime) {
        this.refreshRequestTime = refreshRequestTime;
    }

    public User getRequester() {
        return requester;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }

    public User getRequestee() {
        return requestee;
    }

    public void setRequestee(User requestee) {
        this.requestee = requestee;
    }

    public boolean isStale () {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        Timestamp requestTime = this.getRefreshRequestTime();
        if (requestTime == null) {
            requestTime = this.getOriginalRequestTime();
        }
        if (requestTime.getTime() + MILLIS_TO_24HOURS < now.getTime()) {
            return true;
        }
        return false;
    }

    @Override
    public String toString () {
        return "requester= " + requester.getFirstName() + "\trequestee= " + requestee.getFirstName() + "\tstatus=" + status.toString();
    }
}
