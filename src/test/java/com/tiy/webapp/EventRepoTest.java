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
    public void testBasicEventCrud () {
        Event savedEvent = eventRepo.save(testEvent);
        Event otherSavedEvent = eventRepo.save(otherEvent);
        Long id = savedEvent.getId();
        Long id2 = otherSavedEvent.getId();
        Event retrievedEvent = eventRepo.findOne(id);
        Event otherRetrievedEvent = eventRepo.findOne(id2);
        assertNotNull(retrievedEvent);
        assertNotNull(otherRetrievedEvent);
        assertEquals(testEvent.getAddress(), retrievedEvent.getAddress());
        assertEquals(otherEvent.getDateTime(), otherRetrievedEvent.getDateTime());
        assertEquals(otherEvent.getAddress(), otherRetrievedEvent.getAddress());

        eventRepo.delete(id);
        eventRepo.delete(id2);

        Event nullEvent1 = eventRepo.findOne(id);
        Event nullEvent2 = eventRepo.findOne(id2);
        assertNull(nullEvent1);
        assertNull(nullEvent2);
    }

}