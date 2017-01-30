package com.tiy.webapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by Paul Dennis on 1/25/2017.
 */
@RestController
public class PresenceRestController {

    boolean dbInitialized = false;

    int nextRequestId;

    public static final long MILLIS_TO_24HOURS = 86400000;

    @Autowired
    UserRepo users;

    @Autowired
    EventRepo events;

    @Autowired
    ContactRepo contacts;

    //POC Endpoints
    @RequestMapping(path = "/user-login.json", method = RequestMethod.POST)
    public Response userLogin (@RequestBody UserLoginRequest userLoginRequest) {
        initializeDb();
        User user = users.findFirstByEmail(userLoginRequest.getEmail());
        if (user != null) {
            if (user.getPassword().equals(userLoginRequest.getPassword())) {
                return new Response(true);
            }
        }
        return new Response(false);
    }

    @RequestMapping(path = "/get-open-events.json", method = RequestMethod.GET)
    public Set<Event> getOpenEvents () {
        initializeDb();
        Iterable<Event> allEvents = events.findAll();
        Set<Event> currentEvents = new HashSet<>();
        for (Event event : allEvents) {
            if (event.isCurrent()) {
                currentEvents.add(event);
            }
        }
        return currentEvents;
    }

    @RequestMapping(path = "/get-users-event.json", method = RequestMethod.POST)
    public Event getUsersEvent (@RequestBody DumbEmailWrapper dumbWrapper) {
        initializeDb();
        User user = users.findFirstByEmail(dumbWrapper.getEmail());
        if (user != null) {
            Long id = user.getCheckedInEventId();
            return events.findOne(id);//Could be null!
        }
        return null;
    }

    @RequestMapping(path = "/user-event-signup.json", method = RequestMethod.POST)
    public Response userEventSignup (@RequestBody EventSignupRequest eventSignupRequest) throws Exception {
        initializeDb();
        User user = users.findFirstByEmail(eventSignupRequest.getEmail());
        Event event = events.findOne(eventSignupRequest.getEventId());

        if (user != null && event != null) {
            user.eventCheckIn(event);
            users.save(user);
            return new Response(true);
        }
        return new Response(false);
    }

    @RequestMapping(path = "/respond-to-request.json", method = RequestMethod.POST)
    public Response respondToRequest (@RequestBody ContactResponseRequest contactResponseRequest) {
        initializeDb();
        boolean accept = contactResponseRequest.isAccept();
        UserContact contact = contacts.findOne(contactResponseRequest.getRequestId());

        if (contact == null) {
            return new Response(false);
        }
        if (!contact.getRequestee().getEmail().equals(contactResponseRequest.getEmail())) {
            return new Response(false);
            //Only the requestee can respond to a request
        }

        //Do stuff
        if (accept) {
            contact.setStatus(ContactStatus.FRIENDS);
            contacts.save(contact);
            return new Response(true);
        } else {
            contact.setStatus(ContactStatus.REJECTED);
            contacts.save(contact);
            return new Response(true);
        }
    }

    @RequestMapping(path = "/send-request.json", method = RequestMethod.POST)
    public Response sendRequest (@RequestBody UserContact userContact) {
        initializeDb();
        User requester = userContact.getRequester();
        User requestee = userContact.getRequestee();
        if (requester == null || requestee == null) {
            return new Response(false);
        }
        if (requester.equals(requestee)) {
            return new Response(false);
        }
        contacts.save(userContact);
        return new Response(true);
    }

    @RequestMapping(path = "/get-event-attendees.json", method = RequestMethod.POST)
    public Set<User> getEventAttendees (@RequestBody EventAttendeesRequest request) {
        initializeDb();
        return users.findByCheckedInEventId(request.getEventId());
    }


    //***************MVP Endpoints********************\\

    @RequestMapping(path = "/user-registration.json", method = RequestMethod.POST)
    public Response userRegistration (@RequestBody User newUser) {
        initializeDb();
        users.save(newUser);
        return new Response(true);
    }

    @RequestMapping(path = "/user-incoming-requests.json", method = RequestMethod.POST)
    public Set<UserContact> userIncomingRequests (@RequestBody DumbEmailWrapper wrapper) {
        initializeDb();
        Set<UserContact> incomingRequests = users.findFirstByEmail(wrapper.getEmail()).getIncomingRequests();
        for (UserContact userContact : incomingRequests) {
            if (userContact.getStatus() != ContactStatus.REQUESTED || userContact.isStale()) {
                incomingRequests.remove(userContact);
            }
        }
        return incomingRequests;
    }

    @RequestMapping(path = "/user-outgoing-requests.json", method = RequestMethod.POST)
    public Set<UserContact> userOutgoingRequests (@RequestBody DumbEmailWrapper wrapper) {
        initializeDb();
        Set<UserContact> outgoingRequests = users.findFirstByEmail(wrapper.getEmail()).getOutgoingRequests();
        for (UserContact userContact : outgoingRequests) {
            if (userContact.getStatus() != ContactStatus.REQUESTED  || userContact.isStale()) {
                outgoingRequests.remove(userContact);
            }
        }
        return outgoingRequests;
    }

    //somewhat tested
    @RequestMapping(path = "/get-user-contacts.json", method = RequestMethod.POST)
    public Set<User> getUserContacts (@RequestBody DumbEmailWrapper wrapper) {
        initializeDb();
        User user = users.findFirstByEmail(wrapper.getEmail());
        Set<UserContact> incoming = user.getIncomingRequests();
        Set<UserContact> outgoing = user.getOutgoingRequests();
        Set<User> friends = new HashSet<>();
        for (UserContact userContact : incoming) {
            System.out.println("Incoming request = " + userContact);
            if (userContact.getStatus() == ContactStatus.FRIENDS) {
                friends.add(userContact.getRequester());
            }
        }
        for (UserContact userContact : outgoing) {
            System.out.println("Outgoing request = " + userContact);
            if (userContact.getStatus() == ContactStatus.FRIENDS) {
                friends.add(userContact.getRequestee());
            }
        }
        return friends;
    }

    @RequestMapping(path = "/get-user-info.json", method = RequestMethod.POST)
    public User getUserInfo (@RequestBody DumbEmailWrapper wrapper) {
        initializeDb();
        return users.findFirstByEmail(wrapper.getEmail());
    }

    //v1.0 Endpoints

    @RequestMapping(path = "/user-stale-requests-made.json", method = RequestMethod.POST)
    public Set<UserContact> userStaleRequestsMade (@RequestBody DumbEmailWrapper wrapper) {
        initializeDb();
        User user = users.findFirstByEmail(wrapper.getEmail());
        if (user == null) {
            return null;
        }
        Set<UserContact> outgoingRequests = user.getOutgoingRequests();
        for (UserContact userContact : outgoingRequests) {
            if (!userContact.isStale() && userContact.getStatus() == ContactStatus.REQUESTED) {
                outgoingRequests.remove(userContact);
            }
        }
        return outgoingRequests;
    }

    @RequestMapping(path = "/user-stale-requests-received.json", method = RequestMethod.POST)
    public Set<UserContact> userStaleRequestsReceived (@RequestBody DumbEmailWrapper wrapper) {
        initializeDb();
        User user = users.findFirstByEmail(wrapper.getEmail());
        if (user == null) {
            return null;
        }
        Set<UserContact> incomingRequests = user.getIncomingRequests();
        for (UserContact userContact : incomingRequests) {
            if (!userContact.isStale() && userContact.getStatus() == ContactStatus.REQUESTED) {
                incomingRequests.remove(userContact);
            }
        }
        return incomingRequests;
    }

    @RequestMapping(path = "/refresh-stale-request.json", method = RequestMethod.POST)
    public Response refreshStaleRequest (@RequestBody UserContactIdWrapper wrapper) {
        initializeDb();
        Long id = wrapper.getUserContactId();
        UserContact contact = contacts.findOne(id);
        if (contact == null) {
            return new Response(false);
        }
        contact.setRefreshRequestTime(Timestamp.valueOf(LocalDateTime.now()));
        return new Response(true);
    }

    public void initializeDb () {
        if (users == null || contacts == null || events == null) {
            throw new AssertionError("One of the repos is null.");
        }
        if (!dbInitialized) {
            initializeUsers();
            initializeEvents();
            initializeContacts();
            dbInitialized = true;
        }
    }

    public void initializeUsers () {
        User user1 = users.findFirstByEmail("paul@gmail");
        User user2 = users.findFirstByEmail("adrian@gmail");
        if (user1 == null) {
            user1 = new User("paul@gmail", "Paul", "Dennis", "TIY", "Student", "drifting3");
            users.save(user1);
        }
        if (user2 == null) {
            user2 = new User("adrian@gmail", "Adrian", "McDaniel", "TIY", "Student", "elevation9");
            users.save(user2);
        }
    }

    public void initializeEvents () {
        Event event1 = events.findFirstByEventName("Big Test Party");
        Event event2 = events.findFirstByEventName("Giant Test Luau");
        Timestamp tomorrow = Timestamp.valueOf(LocalDateTime.now());
        tomorrow.setTime(tomorrow.getTime() + MILLIS_TO_24HOURS);
        if (event1 == null) {
            event1 = new Event("Big Test Party", "TIY", "MLK");
            event1.setEndTime(tomorrow);
            events.save(event1);
        }
        if (event2 == null) {
            event2 = new Event("Giant Test Luau", "Hawaii", "Oahu");
            event2.setEndTime(tomorrow);
            events.save(event2);
        }
    }

    public void initializeContacts () {
        if (contacts.count() == 0) {
            User user1 = users.findFirstByEmail("paul@gmail");
            User user2 = users.findFirstByEmail("adrian@gmail");
            UserContact userContact = new UserContact(user1, user2, ContactStatus.REQUESTED);
            contacts.save(userContact);
        }
    }
}
