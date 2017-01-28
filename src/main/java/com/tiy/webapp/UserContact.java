package com.tiy.webapp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by Paul Dennis on 1/25/2017.
 */

@Entity
@Table(name = "contacts")
public class UserContact {

    @GeneratedValue
    @Id
    Long id;

    @Column(nullable = false)
    String requesterEmail;

    @Column(nullable = false)
    String requesteeEmail;

    @Column(nullable = false)
    ContactStatus status;

    @Column(nullable = false)
    Timestamp originalRequestTime;

    @Column(nullable = true)
    Timestamp refreshRequestTime;

    public UserContact() {
        originalRequestTime = Timestamp.valueOf(LocalDateTime.now());
    }

    public UserContact(String requesterEmail, String requesteeEmail, ContactStatus status) {
        this.requesterEmail = requesterEmail;
        this.requesteeEmail = requesteeEmail;
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

    public String getRequesterEmail() {
        return requesterEmail;
    }

    public void setRequesterEmail(String requesterEmail) {
        this.requesterEmail = requesterEmail;
    }

    public String getRequesteeEmail() {
        return requesteeEmail;
    }

    public void setRequesteeEmail(String requesteeEmail) {
        this.requesteeEmail = requesteeEmail;
    }
}
