package com.tiy.webapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by Paul Dennis on 1/25/2017.
 */
@RestController
public class PresenceRestController {

    Map<Integer, Event> eventMap;
    Map<String, User> userMap;
    Map<Integer, UserContact> contactRequestMap;
    boolean initialized = false;

    int nextRequestId;

    @Autowired
    UserRepo userRepo;

    @Autowired
    EventRepo eventRepo;

    //POC Endpoints
    //tested in postman
    @RequestMapping(path = "/user-login.json", method = RequestMethod.POST)
    public Response userLogin (@RequestBody UserLoginRequest userLoginRequest) {
        initialize();
        if (userRepo == null) {
            System.out.println("User repo is null");
        } else {
            System.out.println("User repo active!");
        }
        String email = userLoginRequest.getEmail();
        User user = userMap.get(email);
        if (user != null && user.getPassword().equals(userLoginRequest.getPassword())) {
            return new Response(true);
        }
        return new Response(false);
    }

    //tested in postman
    @RequestMapping(path = "/get-open-events.json", method = RequestMethod.GET)
    public Collection<Event> getOpenEvents () {
        initialize();
        return eventMap.values();
    }

    //tested in postman
    @RequestMapping(path = "/get-users-event.json", method = RequestMethod.POST)
    public Event getUsersEvent (@RequestBody DumbEmailWrapper dumbWrapper) {
        initialize();
        User user = userMap.get(dumbWrapper.getEmail());
        System.out.println(user);
        if (user != null) {
            throw new AssertionError("Fix");
            //return user.getCurrentEvent();
        }
        return null;
    }

    //tested in postman
    @RequestMapping(path = "/user-event-signup.json", method = RequestMethod.POST)
    public Response userEventSignup (@RequestBody EventSignupRequest eventSignupRequest) {
        initialize();
        String email = eventSignupRequest.getEmail();
        int eventId = eventSignupRequest.getEventId();
        User user = userMap.get(email);
        Event event = eventMap.get(eventId);
        if (user != null && event != null) {
            throw new AssertionError("Fix");
            //user.setCurrentEvent(event);
            //return true;
        }
        return new Response(false);
    }

    //tested in postman
    @RequestMapping(path = "/respond-to-request.json", method = RequestMethod.POST)
    public Response respondToRequest (@RequestBody ContactResponseRequest contactResponseRequest) {
        initialize();
        //String email = contactResponseRequest.getEmail();
        int requestId = contactResponseRequest.getRequestId();
        boolean accept = contactResponseRequest.isAccept();

        UserContact contact = contactRequestMap.get(requestId);
        if (contact == null) {
            return new Response(false);
        }
        //Do stuff
        if (accept) {
            contact.setAccepted(true);
            return new Response(true);
        } else {
            contactRequestMap.remove(requestId);
            return new Response(true);
        }
    }

    //tested in postman
    @RequestMapping(path = "/send-request.json", method = RequestMethod.POST)
    public Response sendRequest (@RequestBody UserContact userContact) {
        initialize();
        String requesterEmail = userContact.getRequesterEmail();
        String requesteeEmail = userContact.getRequesteeEmail();
        User user1 = userMap.get(requesterEmail);
        User user2 = userMap.get(requesteeEmail);
        if (user1 == null || user2 == null) {
            return new Response(false);
        }
        if (user1.equals(user2)) {
            return new Response(false);
        }

        userContact.setRequestId(nextRequestId);
        contactRequestMap.put(nextRequestId, userContact);
        nextRequestId++;
        return new Response(true);
    }


    //tested in postman
    @RequestMapping(path = "/get-event-attendees.json", method = RequestMethod.POST)
    public List<User> getEventAttendees (@RequestBody EventAttendeesRequest request) {
        initialize();
        int eventId = request.getEventId();
        if (eventMap.get(eventId) == null) {
            return null;
        }
        List<User> attendees = new ArrayList<>();
        for (User user : userMap.values()) {
            if (user.getEventId() == eventId) {
                attendees.add(user);
            }
        }
        return attendees;
    }


    //***************MVP Endpoints********************\\
    @RequestMapping(path = "/user-registration.json", method = RequestMethod.POST)
    public boolean userRegistration (@RequestBody User newUser) {
        return false;
    }

    @RequestMapping(path = "/user-incoming-requests.json", method = RequestMethod.POST)
    public List<UserContact> userIncomingRequests (@RequestBody String email) {
        return null;
    }

    @RequestMapping(path = "/user-outgoing-requests.json", method = RequestMethod.POST)
    public List<UserContact> userOutgoingRequests (@RequestBody String email) {
        return null;
    }

    @RequestMapping(path = "/get-user-contacts.json", method = RequestMethod.POST)
    public List<User> getUserContacts (@RequestBody String email) {
        return null;
    }

    @RequestMapping(path = "/get-user-info.json", method = RequestMethod.POST)
    public User getUserInfo (@RequestBody String email) {
        return null;
    }

    //v1.0 Endpoints

    @RequestMapping(path = "/user-stale-requests-made.json", method = RequestMethod.POST)
    public List<UserContact> userStaleRequestsMade (@RequestBody String email) {
        return null;
    }

    @RequestMapping(path = "/user-stale-requests-received.json", method = RequestMethod.POST)
    public List<UserContact> userStaleRequestsReceived (@RequestBody String email) {
        return null;
    }

    @RequestMapping(path = "/refresh-stale-request.json", method = RequestMethod.POST)
    public boolean refreshStaleRequest () {
        return false;
    }

    public void initialize () {
        if (!initialized) {
            eventMap = new HashMap<>();
            Event testEvent = new Event(0, "Test Event", "Iron Yard Atlanta", "MLK");
            Event otherEvent = new Event(1, "Big Luau", "Hawaii", "Oahu");
            eventMap.put(testEvent.getEventId(), testEvent);
            eventMap.put(otherEvent.getEventId(), otherEvent);

            userMap = new HashMap<>();
            User user1 = new User("paul@gmail", "drifting3");
            User user2 = new User("adrian@gmail", "elevation9");
            userMap.put(user1.getEmail(), user1);
            userMap.put(user2.getEmail(), user2);

            contactRequestMap = new HashMap<>();
            nextRequestId = 0;

            initialized = true;
        }
    }
}
