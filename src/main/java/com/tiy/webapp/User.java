package com.tiy.webapp;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tiy.webapp.requestBody.UserWrapper;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Paul Dennis on 1/25/2017.
 */
@Entity
@Table(name = "users")
public class User {

    @GeneratedValue
    @Id
    Long id;

    @Column(nullable = false, unique = true)
    String email;

    @Column(nullable = false)
    String firstName;

    @Column(nullable = false)
    String lastName;

    @Column(nullable = false)
    String company;

    @Column(nullable = false)
    String position;

    @Column(nullable = false)
    String password;

    @Column(nullable = true)
    Long checkedInEventId;

    @OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.MERGE, orphanRemoval = true, mappedBy="requester")
    @JsonManagedReference
    Set<UserContact> outgoingRequests;

    @OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.MERGE, orphanRemoval = true, mappedBy="requestee")
    @JsonManagedReference
    Set<UserContact> incomingRequests;

    @ManyToMany
    @JoinTable (
        name = "user_events",
        joinColumns = @JoinColumn(name="user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id")
    )
    Set<Event> events;

    @Column(nullable = true)
    @Type(type = "text")
    String imageString;

    @Column(nullable = true)
    Boolean photoVisible;

    public User () {

    }

    public User (String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email, String firstName, String lastName, String company, String position, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.company = company;
        this.position = position;
        this.password = password;
    }

    public User (UserWrapper userWrapper) {
        this.email = userWrapper.getEmail();
        this.firstName = userWrapper.getFirstName();
        this.lastName = userWrapper.getLastName();
        this.company = userWrapper.getCompany();
        this.position = userWrapper.getPosition();
        this.password = userWrapper.getPassword();
        this.photoVisible = userWrapper.isPhotoVisible();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCheckedInEventId() {
        return checkedInEventId;
    }

    public void setCheckedInEventId(Long checkedInEventId) {
        this.checkedInEventId = checkedInEventId;
    }

    public void eventCheckIn (Event event) {
        setCheckedInEventId(event.getId());
        events.add(event);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    public Set<UserContact> getOutgoingRequests() {
        /*if (outgoingRequests == null) {
            return new HashSet<UserContact>();
        }*/
        return outgoingRequests;
    }

    public void setOutgoingRequests(Set<UserContact> outgoingRequests) {
        this.outgoingRequests = outgoingRequests;
    }

    public Set<UserContact> getIncomingRequests() {
        return incomingRequests;
    }

    public void setIncomingRequests(Set<UserContact> incomingRequests) {
        this.incomingRequests = incomingRequests;
    }

    public Boolean getPhotoVisible() {
        return photoVisible;
    }

    public void setPhotoVisible(Boolean photoVisible) {
        this.photoVisible = photoVisible;
    }
}
