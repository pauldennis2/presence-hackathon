package com.tiy.webapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by Paul Dennis on 1/25/2017.
 */
@Controller
public class PresenceController {

    @Autowired
    EventRepo events;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String home (Model model) {
        model.addAttribute("eventList", events.findAll());
        return "admin";
    }

    @RequestMapping(path = "/add-event", method = RequestMethod.POST)
    public String addEvent (String eventName, String location, String address, String startDate, String startTime, String endDate, String endTime) {
        System.out.println("Event name = " + eventName);
        System.out.println("Location = " + location);
        System.out.println("Address = " + address);
        System.out.println("Start date = " + startDate);
        System.out.println("Start time = " + startTime);
        System.out.println("End date = " + endDate);
        System.out.println("End time = " + endTime);

        String[] splitStartDate = startDate.split("-");
        String[] splitStartTime = startTime.split(":");
        String[] splitEndDate = endDate.split("-");
        String[] splitEndTime = endTime.split(":");
        int startYear = Integer.parseInt(splitStartDate[0]);
        int startMonth = Integer.parseInt(splitStartDate[1]);
        int startDay = Integer.parseInt(splitStartDate[2]);

        int startHour = Integer.parseInt(splitStartTime[0]);
        int startMinute = Integer.parseInt(splitStartTime[1]);

        int endYear = Integer.parseInt(splitEndDate[0]);
        int endMonth = Integer.parseInt(splitEndDate[1]);
        int endDay = Integer.parseInt(splitEndDate[2]);

        int endHour = Integer.parseInt(splitEndTime[0]);
        int endMinute = Integer.parseInt(splitEndTime[1]);

        LocalDateTime startDateTime = LocalDateTime.of(startYear, startMonth, startDay, startHour, startMinute);
        LocalDateTime endDateTime = LocalDateTime.of(endYear, endMonth, endDay, endHour, endMinute);
        System.out.println(startDateTime);
        System.out.println(endDateTime);
        Timestamp startTimestamp = Timestamp.valueOf(startDateTime);
        Timestamp endTimestamp = Timestamp.valueOf(endDateTime);
        if (startTimestamp.after(endTimestamp)) {
            throw new AssertionError("Event start cannot be after end");
        }
        Event event = new Event();
        event.setEventName(eventName);
        event.setLocation(location);
        event.setAddress(address);
        event.setStartTime(startTimestamp);
        event.setEndTime(endTimestamp);
        events.save(event);
        return "redirect:/";
    }

    @RequestMapping(path = "/delete-event", method = RequestMethod.GET)
    public String deleteEvent (Long eventId) {
        events.delete(eventId);
        return "redirect:/";
    }
}
