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



    /*@Column(nullable = false)
    String requesterEmail;

    @Column(nullable = false)
    String requesteeEmail;*/

    /*@OneToOne
    @JoinColumn(name = "requestee_id", nullable = false)
    User requestee;*/

    @GeneratedValue
    @Id
    Long id;

    @ManyToOne(cascade=CascadeType.MERGE)
    //@JoinColumn(name = "requester_id", nullable = false)
    User requester;

    @Column(nullable = false)
    ContactStatus status;

    @Column(nullable = false)
    Timestamp originalRequestTime;

    @Column(nullable = true)
    Timestamp refreshRequestTime;

    public UserContact() {
        originalRequestTime = Timestamp.valueOf(LocalDateTime.now());
    }

    /*public UserContact(User requester, User requestee, ContactStatus status) {
        this.requester = requester;
        this.requestee = requestee;
        this.status = status;
        originalRequestTime = Timestamp.valueOf(LocalDateTime.now());
    }*/

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
    /*
    public User getRequestee() {
        return requestee;
    }

    public void setRequestee(User requestee) {
        this.requestee = requestee;
    }*/
}
