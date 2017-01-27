package com.tiy.webapp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * Created by Paul Dennis on 1/27/2017.
 */
public class EventRepoTest {

    @Autowired
    EventRepo eventRepo;

    Event testEvent;
    Event otherEvent;
    @Before
    public void setUp() throws Exception {
        testEvent = new Event("Test Test Event", "TIY", "MLK");
        otherEvent = new Event("Big Testing Party", "Norway", "Fjords");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testBasicInsertRetrieveEvent () {
        eventRepo.save(testEvent);
    }

}