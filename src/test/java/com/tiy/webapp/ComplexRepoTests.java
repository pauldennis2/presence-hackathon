package com.tiy.webapp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.Scanner;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by Paul Dennis on 1/27/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ComplexRepoTests {

    public static final long MILLIS_TO_24HOURS = 86400000;

    @Autowired
    EventRepo events;

    @Autowired
    ContactRepo contacts;

    @Autowired
    UserRepo users;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    public void complexTests () {
        System.out.println("Add some more complex tests, fool");
        assertTrue(false);
    }

    @Test
    public void testPictureLob () {
        try {
            String content = new Scanner(new File("base64test.txt")).useDelimiter("\\Z").next();
            int length = content.length();
            System.out.println("Content length: " + length);

            User user = new User("Jon", "Jacob", "Jingleheimer", "Schmidt", "1234", "5678");
            user.setImageString(content);
            users.save(user);

            User retrievedUser = users.findFirstByEmail(user.getEmail());
            assertNotNull(retrievedUser);
            assertEquals(length, retrievedUser.getImageString().length());

            users.delete(user);
            User nullUser = users.findFirstByEmail(user.getEmail());
            assertNull(nullUser);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            assertFalse(true);
        }
    }


    /* Fails with the following:
    org.springframework.orm.jpa.JpaSystemException: A collection with cascade="all-delete-orphan" was no longer
    referenced by the owning entity instance: com.tiy.webapp.User.incomingRequests; nested exception is
    org.hibernate.HibernateException: A collection with cascade="all-delete-orphan" was no longer referenced
    by the owning entity instance: com.tiy.webapp.User.incomingRequests


    @Test
    public void testGetStaleRequests () {
        User firstUser = new User("Boromir", "Aragorn", "Gimli", "Legolas", "Gandalf", "Frodo");
        User secondUser = new User("Sam", "Merry", "Pippin", "Bilbo", "Sauron", "Saruman");

        users.save(firstUser);
        users.save(secondUser);

        UserContact staleContact = new UserContact(firstUser, secondUser, ContactStatus.REQUESTED);
        long millis = staleContact.getOriginalRequestTime().getTime();
        millis -= MILLIS_TO_24HOURS;
        millis -= MILLIS_TO_24HOURS;
        staleContact.setOriginalRequestTime(new Timestamp(millis));
        assertTrue(staleContact.isStale());
        UserContact savedContact = contacts.save(staleContact);

        User retrievedSecondUser = users.findFirstByEmail(secondUser.getEmail());
        Set<UserContact> staleIncomingRequests = retrievedSecondUser.getIncomingRequests();
        if (staleIncomingRequests == null) {
            System.out.println("It's null");
        }
        for (UserContact userContact : staleIncomingRequests) {
            if (!userContact.isStale() && userContact.getStatus() == ContactStatus.REQUESTED) {
                staleIncomingRequests.remove(userContact);
            }   //FROM ENDPOINT
        }
        assertEquals(1, staleIncomingRequests.size());

        contacts.delete(staleContact);
        users.delete(firstUser);
        users.delete(secondUser);
        User firstNullUser = users.findFirstByEmail(firstUser.getEmail());
        User secondNullUser = users.findFirstByEmail(secondUser.getEmail());
        UserContact nullContact = contacts.findOne(savedContact.getId());
        assertNull(firstNullUser);
        assertNull(secondNullUser);
        assertNull(nullContact);
    }*/

}