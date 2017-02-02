package com.tiy.webapp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Paul Dennis on 2/1/2017.
 */

@Entity
@Table(name = "test_person")
public class TestPerson {
    @Id
    @GeneratedValue
    private Long id;

    private String firstName;
    private String lastName;
    private String profession;

    public TestPerson () {

    }

    public TestPerson(String firstName, String lastName, String profession) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.profession = profession;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }
}
