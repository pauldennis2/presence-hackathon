package com.tiy.webapp;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Paul Dennis on 1/26/2017.
 */
public interface EventRepo extends CrudRepository<Event, Long> {
    Event findFirstByEventName(String eventName); //unit tested
    // Only used for DB initialization of seed values (for testing)
}
