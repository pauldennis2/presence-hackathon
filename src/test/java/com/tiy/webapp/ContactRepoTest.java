package com.tiy.webapp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by Paul Dennis on 1/27/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ContactRepoTest {

    UserContact firstContact;

    @Autowired
    ContactRepo contacts;

    @Before
    public void setUp() throws Exception {
        firstContact = new UserContact("test1@gmail", "test2@gmail", ContactStatus.REQUESTED);
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
        assertEquals(firstContact.getRequesterEmail(), retrievedContact.getRequesterEmail());
        assertEquals(firstContact.getStatus(), retrievedContact.getStatus());
        assertEquals(firstContact.getOriginalRequestTime(), retrievedContact.getOriginalRequestTime());

        contacts.delete(id);

        UserContact nullContact = contacts.findOne(id);
        assertNull(nullContact);
    }
}