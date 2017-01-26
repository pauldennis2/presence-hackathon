package com.tiy.webapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul Dennis on 1/25/2017.
 */
@Controller
public class PresenceController {

    List<Event> eventList;
    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String home (Model model) {
        if (eventList == null) {
            eventList = new ArrayList<>();
            Event testEvent = new Event();
            testEvent.setEventName("Awesome Test Event");
            eventList.add(testEvent);
            Event otherEvent = new Event();
            otherEvent.setEventName("Party Event");
            eventList.add(otherEvent);
        }
        model.addAttribute("eventList", eventList);
        return "admin";
    }

    @RequestMapping(path = "/add-event", method = RequestMethod.POST)
    public String addEvent () {
        return "admin";
    }
}
