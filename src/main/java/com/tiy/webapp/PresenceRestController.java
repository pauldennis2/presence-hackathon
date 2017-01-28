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

    boolean dbInitialized = false;

    int nextRequestId;

    @Autowired
    UserRepo users;

    @Autowired
    EventRepo events;

    @Autowired
    ContactRepo contacts;

    //POC Endpoints
    //tested in postman WITH DB
    @RequestMapping(path = "/user-login.json", method = RequestMethod.POST)
    public Response userLogin (@RequestBody UserLoginRequest userLoginRequest) {

        initializeDb();
        String email = userLoginRequest.getEmail();
        //User user = userMap.get(email);
        User user = users.findFirstByEmail(email);
        if (user != null && user.getPassword().equals(userLoginRequest.getPassword())) {
            return new Response(true);
        }
        return new Response(false);
    }

    //tested in postman WITH DB
    @RequestMapping(path = "/get-open-events.json", method = RequestMethod.GET)
    public Iterable<Event> getOpenEvents () {
        initializeDb();
        return events.findAll();
    }

    //tested in postman WITH DB
    @RequestMapping(path = "/get-users-event.json", method = RequestMethod.POST)
    public Event getUsersEvent (@RequestBody DumbEmailWrapper dumbWrapper) {
        initializeDb();
        User user = users.findFirstByEmail(dumbWrapper.getEmail());
        System.out.println(user);
        if (user != null) {
            Long id = user.getCheckedInEventId();
            return events.findOne(id);//Could be null!
        }
        return null;
    }

    //tested in postman WITH DB
    @RequestMapping(path = "/user-event-signup.json", method = RequestMethod.POST)
    public Response userEventSignup (@RequestBody EventSignupRequest eventSignupRequest) {
        initializeDb();
        String email = eventSignupRequest.getEmail();
        Long eventId = eventSignupRequest.getEventId();
        User user = users.findFirstByEmail(email);
        Event event = events.findOne(eventId);
        if (user != null && event != null) {
            user.setCheckedInEventId(eventId);
            users.save(user);
            //This is not creating a new duplicate user. (todo doubleckeck)
            return new Response(true);
        }
        return new Response(false);
    }

    //tested in postman WITH DB
    @RequestMapping(path = "/respond-to-request.json", method = RequestMethod.POST)
    public Response respondToRequest (@RequestBody ContactResponseRequest contactResponseRequest) {
        initializeDb();
        //String email = contactResponseRequest.getEmail();
        Long requestId = contactResponseRequest.getRequestId();
        boolean accept = contactResponseRequest.isAccept();

        UserContact contact = contacts.findOne(requestId);
        if (contact == null) {
            return new Response(false);
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

    //tested in postman WITH DB
    @RequestMapping(path = "/send-request.json", method = RequestMethod.POST)
    public Response sendRequest (@RequestBody UserContact userContact) {
        initializeDb();
        String requesterEmail = userContact.getRequesterEmail();
        String requesteeEmail = userContact.getRequesteeEmail();
        User requester = users.findFirstByEmail(requesterEmail);
        User requestee = users.findFirstByEmail(requesteeEmail);
        if (requester == null || requestee == null) {
            return new Response(false);
        }
        if (requester.equals(requestee)) {
            return new Response(false);
        }
        contacts.save(userContact);
        return new Response(true);
    }


    //tested in postman WITH DB
    @RequestMapping(path = "/get-event-attendees.json", method = RequestMethod.POST)
    public List<User> getEventAttendees (@RequestBody EventAttendeesRequest request) {
        initializeDb();
        Long eventId = request.getEventId();
        return users.findByCheckedInEventId(eventId);
    }


    //***************MVP Endpoints********************\\
    @RequestMapping(path = "/user-registration.json", method = RequestMethod.POST)
    public Response userRegistration (@RequestBody User newUser) {
        users.save(newUser);
        return new Response(true);
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

    public void initializeDb () {
        if (users == null || contacts == null || events == null) {
            throw new AssertionError("One of the repos is null.");
        }
        //public User(String email, String firstName, String lastName, String company, String position, String password)
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
        if (event1 == null) {
            event1 = new Event("Big Test Party", "TIY", "MLK");
            events.save(event1);
        }
        if (event2 == null) {
            event2 = new Event("Giant Test Luau", "Hawaii", "Oahu");
            events.save(event2);
        }
    }

    public void initializeContacts () {
        if (contacts.count() == 0) {
            System.out.println("Adding contact");
            UserContact userContact = new UserContact("paul@gmail", "adrian@gmail", ContactStatus.REQUESTED);
            contacts.save(userContact);
        }
    }
}
