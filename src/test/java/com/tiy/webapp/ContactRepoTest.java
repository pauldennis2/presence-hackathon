package com.tiy.webapp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 * Created by Paul Dennis on 1/27/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ContactRepoTest {

    UserContact firstContact;

    User user1;
    User user2;

    @Autowired
    ContactRepo contacts;

    @Autowired
    UserRepo users;

    @Before
    public void setUp() throws Exception {
        user1 = new User("test1@gmail", "123", "456", "789", "CEO", "secret");
        user2 = new User("test2@gmail", "abc", "def", "jhi", "brd", "ubrs");
        //firstContact = new UserContact(user1, user2, ContactStatus.REQUESTED);
        firstContact = new UserContact();
        firstContact.setRequester(user1);
        firstContact.setStatus(ContactStatus.FRIENDS);
        firstContact.setOriginalRequestTime(Timestamp.valueOf(LocalDateTime.now()));
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testBasicContactCrud () {
        UserContact savedContact = contacts.save(firstContact);
        Long id = savedContact.getId();
        UserContact retrievedContact = contacts.findOne(id);
        assertNotNull(retrievedContact);
        //assertEquals(firstContact.getRequester().getEmail(), retrievedContact.getRequester().getEmail());
        assertEquals(firstContact.getStatus(), retrievedContact.getStatus());
        assertEquals(firstContact.getOriginalRequestTime(), retrievedContact.getOriginalRequestTime());

        contacts.delete(id);

        UserContact nullContact = contacts.findOne(id);
        assertNull(nullContact);
    }

    @Test
    public void testSomething () {
        users.save(user1);
        UserContact savedContact = contacts.save(firstContact);
        Long id = savedContact.getId();

        UserContact retrievedContact = contacts.findOne(id);
        assertNotNull(retrievedContact);
        assertEquals(user1.getEmail(), retrievedContact.getRequester().getEmail());

        contacts.delete(id);
        retrievedContact = contacts.findOne(id);
        assertNull(retrievedContact);
    }
}