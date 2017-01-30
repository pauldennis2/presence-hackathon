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
        firstContact = new UserContact();
        firstContact.setRequester(user1);
        firstContact.setStatus(ContactStatus.FRIENDS);
        firstContact.setOriginalRequestTime(Timestamp.valueOf(LocalDateTime.now()));
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testFindByStatus () {
        users.save(user1);
        users.save(user2);
        UserContact savedContact = contacts.save(firstContact);
        UserContact block1Contact = new UserContact(user1, user2, ContactStatus.BLOCKED);
        UserContact block2Contact = new UserContact(user2, user1, ContactStatus.BLOCKED);
        UserContact firstSavedBlockContact = contacts.save(block1Contact);
        UserContact secondSavedBlockContact = contacts.save(block2Contact);
        //Setup ^^^

//        int size = contacts.findByRequesterEmailAndStatus(user1.getEmail(), ContactStatus.BLOCKED).size();
//        assertEquals (1, size);
        assertFalse(true);


        //Cleanup vvvvv
        contacts.delete(savedContact.getId());
        UserContact nullContact = contacts.findOne(savedContact.getId());
        assertNull(nullContact);

        contacts.delete(firstSavedBlockContact.getId());
        contacts.delete(secondSavedBlockContact.getId());
        nullContact = contacts.findOne(firstSavedBlockContact.getId());
        assertNull(nullContact);
        nullContact = contacts.findOne(secondSavedBlockContact.getId());
        assertNull(nullContact);

        users.delete(user1);
        users.delete(user2);
        User firstNullUser = users.findFirstByEmail(user1.getEmail());
        User secondNullUser = users.findFirstByEmail(user2.getEmail());
        assertNull(firstNullUser);
        assertNull(secondNullUser);
    }

    /*@Test
    public void testTransientProperties () {
        users.save(user1);
        users.save(user2);
        //firstContact.setRequesteeEmail("not a real email");
        UserContact savedContact = contacts.save(firstContact);

        UserContact retrievedContact = contacts.findOne(savedContact.getId());
        assertNotNull(retrievedContact);
        assertNull(retrievedContact.getRequesteeEmail());

        contacts.delete(firstContact);
        UserContact nullContact = contacts.findOne(savedContact.getId());
        assertNull(nullContact);
        users.delete(user1);
        users.delete(user2);
        User firstNullUser = users.findFirstByEmail(user1.getEmail());
        User secondNullUser = users.findFirstByEmail(user2.getEmail());
        assertNull(firstNullUser);
        assertNull(secondNullUser);
    }*/

    @Test
    public void testBasicContactCrud () {
        users.save(user1);
        users.save(user2);
        UserContact savedContact = contacts.save(firstContact);
        Long id = savedContact.getId();
        UserContact retrievedContact = contacts.findOne(id);
        assertNotNull(retrievedContact);
        assertEquals(firstContact.getStatus(), retrievedContact.getStatus());
        assertEquals(firstContact.getOriginalRequestTime(), retrievedContact.getOriginalRequestTime());

        contacts.delete(id);
        UserContact nullContact = contacts.findOne(id);
        assertNull(nullContact);

        users.delete(user1);
        users.delete(user2);
        User firstNullUser = users.findFirstByEmail(user1.getEmail());
        User secondNullUser = users.findFirstByEmail(user2.getEmail());
        assertNull(firstNullUser);
        assertNull(secondNullUser);
    }
}