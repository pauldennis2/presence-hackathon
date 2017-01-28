package com.tiy.webapp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Paul Dennis on 1/27/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
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
    public void testBasicUserCrud () {
        userRepo.save(firstTestUser);
        userRepo.save(secondTestUser);

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

    @Test
    public void testFindByEventId () {
        secondTestUser.setCheckedInEventId(new Long(5));
        User thirdTestUser = new User("gobble@turkey", "1234", "GM", "CTO", "IPA", "DBA");
        thirdTestUser.setCheckedInEventId(new Long(5));
        userRepo.save(thirdTestUser);
        userRepo.save(firstTestUser);
        userRepo.save(secondTestUser);

        List<User> attendees = userRepo.findByCheckedInEventId(new Long(5));
        assertEquals(2, attendees.size());
        String name = attendees.get(0).getFirstName();

        if (!name.equals("1234") || name.equals("Carth")) {
            assertTrue(false);
        }

        userRepo.delete(firstTestUser);
        userRepo.delete(secondTestUser);
        userRepo.delete(thirdTestUser);

        User firstNullUser = userRepo.findFirstByEmail(firstTestUser.getEmail());
        assertNull(firstNullUser);
        User secondNullUser = userRepo.findFirstByEmail(secondTestUser.getEmail());
        assertNull(secondNullUser);
        User thirdNullUser = userRepo.findFirstByEmail(thirdTestUser.getEmail());
        assertNull(thirdNullUser);
    }

}