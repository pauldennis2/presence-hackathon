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
    EventRepo events;

    Event testEvent;
    Event otherEvent;

    @Before
    public void setUp() throws Exception {
        testEvent = new Event("Test Test Event", "TIY", "MLK");
        otherEvent = new Event("Pining for the Fjords", "Norway", "Fjords");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testBasicEventCrud () {
        Event savedEvent = events.save(testEvent);
        Event otherSavedEvent = events.save(otherEvent);
        Long id = savedEvent.getId();
        Long id2 = otherSavedEvent.getId();
        Event retrievedEvent = events.findOne(id);
        Event otherRetrievedEvent = events.findOne(id2);
        assertNotNull(retrievedEvent);
        assertNotNull(otherRetrievedEvent);
        assertEquals(testEvent.getAddress(), retrievedEvent.getAddress());
        assertEquals(otherEvent.getStartTime(), otherRetrievedEvent.getStartTime());
        assertEquals(otherEvent.getAddress(), otherRetrievedEvent.getAddress());

        Event foundByEventName = events.findFirstByEventName("Pining for the Fjords");
        Event notFound = events.findFirstByEventName("Just Pining Thanks");
        assertNotNull(foundByEventName);
        assertNull(notFound);

        events.delete(id);
        events.delete(id2);

        Event nullEvent1 = events.findOne(id);
        Event nullEvent2 = events.findOne(id2);
        assertNull(nullEvent1);
        assertNull(nullEvent2);
    }

}