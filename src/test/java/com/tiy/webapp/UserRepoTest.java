package com.tiy.webapp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * Created by Paul Dennis on 1/27/2017.
 */
public class UserRepoTest {

    @Autowired
    UserRepo userRepo;

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
    public void testBasicUserInsertRetrieval () {
        User firstSavedUser = userRepo.save(firstTestUser);
        User secondSavedUser = userRepo.save(secondTestUser);

        User firstRetrievedUser = userRepo.findFirstByEmail(firstTestUser.getEmail());
        assertNotNull(firstRetrievedUser);
        User secondRetrievedUser = userRepo.findFirstByEmail(secondTestUser.getEmail());
        assertNotNull(secondRetrievedUser);
        assertEquals(firstTestUser.getPosition(), firstRetrievedUser.getPosition());
        assertEquals(secondTestUser.getPassword(), secondRetrievedUser.getPassword());

        userRepo.delete(firstRetrievedUser);
        userRepo.delete(secondRetrievedUser);

        User firstNullUser = userRepo.findFirstByEmail(firstTestUser.getEmail());
        assertNull(firstNullUser);
        User secondNullUser = userRepo.findFirstByEmail(secondTestUser.getEmail());
        assertNull(secondNullUser);
    }

}