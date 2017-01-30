package com.tiy.webapp;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by Paul Dennis on 1/27/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepoTest {

    @Autowired
    UserRepo users;
    
    @Autowired
    ContactRepo contacts;

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    User firstTestUser;
    User secondTestUser;
    @Before
    public void setUp() throws Exception {
        firstTestUser = new User("tester@gmail", "Method", "Oftesting",
                "TIY", "CEO", "pass1234");
        secondTestUser = new User("blarg@gmail", "Carth", "Onasi",
                "The Republic", "War Veteran", "deathtomalak");
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testBasicUserCrud () {
        users.save(firstTestUser);
        users.save(secondTestUser);

        User firstRetrievedUser = users.findFirstByEmail(firstTestUser.getEmail());
        assertNotNull(firstRetrievedUser);
        User secondRetrievedUser = users.findFirstByEmail(secondTestUser.getEmail());
        assertNotNull(secondRetrievedUser);
        assertEquals(firstTestUser.getPosition(), firstRetrievedUser.getPosition());
        assertEquals(secondTestUser.getPassword(), secondRetrievedUser.getPassword());

        users.delete(firstRetrievedUser);
        users.delete(secondRetrievedUser);

        User firstNullUser = users.findFirstByEmail(firstTestUser.getEmail());
        assertNull(firstNullUser);
        User secondNullUser = users.findFirstByEmail(secondTestUser.getEmail());
        assertNull(secondNullUser);
    }

    @Test
    public void testEmailUniqueness () {
        secondTestUser.setEmail(firstTestUser.getEmail());
        users.save(firstTestUser);

        expectedException.expect(DataIntegrityViolationException.class);
        users.save(secondTestUser);

        users.delete(firstTestUser);
        User nullUser = users.findFirstByEmail(firstTestUser.getEmail());
        assertNull(nullUser);
    }

    @Test
    public void testFindByEventId () {
        secondTestUser.setCheckedInEventId(new Long(5));
        User thirdTestUser = new User("gobble@turkey", "1234", "GM", "CTO", "IPA", "DBA");
        thirdTestUser.setCheckedInEventId(new Long(5));
        users.save(thirdTestUser);
        users.save(firstTestUser);
        users.save(secondTestUser);

        Set<User> attendees = users.findByCheckedInEventId(new Long(5));
        assertEquals(2, attendees.size());
        String name = attendees.iterator().next().getFirstName();

        if (!name.equals("1234") || name.equals("Carth")) {
            assertTrue(false);
        }

        users.delete(firstTestUser);
        users.delete(secondTestUser);
        users.delete(thirdTestUser);

        User firstNullUser = users.findFirstByEmail(firstTestUser.getEmail());
        assertNull(firstNullUser);
        User secondNullUser = users.findFirstByEmail(secondTestUser.getEmail());
        assertNull(secondNullUser);
        User thirdNullUser = users.findFirstByEmail(thirdTestUser.getEmail());
        assertNull(thirdNullUser);
    }
    /*
    @Test
    public void testOtherthing () {
        users.save(firstTestUser);
        Set<UserContact> userContactSet = new HashSet<>();
        UserContact firstContact = new UserContact();
        firstContact.setRequester(firstTestUser);
        firstContact.setStatus(ContactStatus.FRIENDS);
        firstContact.setOriginalRequestTime(Timestamp.valueOf(LocalDateTime.now()));
        contacts.save(firstContact);
        //todo rewrite this test
        UserContact secondContact = new UserContact();
        secondContact.setRequester(firstTestUser);
        secondContact.setStatus(ContactStatus.REJECTED);
        secondContact.setOriginalRequestTime(Timestamp.valueOf(LocalDateTime.now()));
        contacts.save(secondContact);

        userContactSet.add(firstContact);
        userContactSet.add(secondContact);

        firstTestUser.setUserContactSet(userContactSet);
        users.save(firstTestUser);


        User retrievedUser = users.findFirstByEmail(firstTestUser.getEmail());
        assertNotNull(retrievedUser);
        assertEquals(2, retrievedUser.getUserContactSet().size());


        contacts.delete(firstContact);
        contacts.delete(secondContact);
        users.delete(firstTestUser);
    }*/

}