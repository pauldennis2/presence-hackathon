package com.tiy.webapp;

import com.tiy.webapp.requestBody.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by Paul Dennis on 1/25/2017.
 */
@RestController
public class PresenceRestController {

    boolean dbInitialized = false;

    public static final long MILLIS_TO_24HOURS = 86400000;

    @Autowired
    UserRepo users;

    @Autowired
    EventRepo events;

    @Autowired
    ContactRepo contacts;

    /*@Autowired
    TestPersonRepo testPersons;

    @RequestMapping(path = "/test-person-list.json", method = RequestMethod.GET)
    public Iterable<TestPerson> testPersonList () {
        return testPersons.findAll();
    }*/

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

    @RequestMapping(path = "get-picture.json", method = RequestMethod.GET)
    public ImageStringWrapper getPicture () {
        return new ImageStringWrapper(getImageStringFromFile("narutowheader.txt"));
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
    //public Response sendRequest (@RequestBody UserContact userContact) {
    public Response sendRequest(@RequestBody UserContactRequest userContactRequest) {
        initializeDb();
        ContactStatus status = userContactRequest.getStatus();
        if (!(status == ContactStatus.BLOCKED || status == ContactStatus.REQUESTED)) {
            return new Response(false);
        }

        User requester = users.findFirstByEmail(userContactRequest.getRequesterEmail());
        User requestee = users.findFirstByEmail(userContactRequest.getRequesteeEmail());
        //todo add check to make sure users aren't friends already
        if (status == ContactStatus.REQUESTED) {
            if (usersAreFriends(requester, requestee)) {
                return new Response(false);
            }
        }

        if (requester == null || requestee == null) {
            return new Response(false);
        }
        if (requester.equals(requestee)) {
            return new Response(false);
        }
        contacts.save(new UserContact(requester, requestee, status));
        return new Response(true);
    }

    @RequestMapping(path = "/get-event-attendees.json", method = RequestMethod.POST)
    public Set<User> getEventAttendees (@RequestBody EventAttendeesRequest request) {
        initializeDb();
        return users.findByCheckedInEventId(request.getEventId());
    }

    /**
     * param: must include the user trying to see the list of attendees
     * @return the list of event attendees who haven't blocked the user
     */
/*
    @RequestMapping(path = "/get-event-attendees-wblock.json", method = RequestMethod.POST)
    public Set<User> getEventAttendeesBlock (@RequestBody EventAttendeesForUserRequest request) {
        initializeDb();
        String email = request.getEmail();
        Long eventId = request.getEventId();
        Set<User> attendees = users.findByCheckedInEventId(eventId);
        for (User attendee : attendees) {
            Set<UserContact> blocks = contacts.findByRequesterEmailAndStatus(attendee.getEmail(), ContactStatus.BLOCKED);
            for (UserContact userContact : blocks) {
                if (userContact.getRequestee().getEmail().equals(email)) {//If the person the block was about is the person
                    attendees.remove(attendee);
                }
            }
        }
        return attendees;
    }
*/


    //***************MVP Endpoints********************\\

    @RequestMapping(path = "/user-registration.json", method = RequestMethod.POST)
    public Response userRegistration (@RequestBody UserWrapper userWrapper) {
        initializeDb();
        User user = new User(userWrapper);
        users.save(user);
        return new Response(true);
    }

    @RequestMapping(path = "/user-incoming-requests.json", method = RequestMethod.POST)
    public Set<SimpleUserContact> userIncomingRequests (@RequestBody DumbEmailWrapper wrapper) {
        initializeDb();
        Set<UserContact> incomingRequests = users.findFirstByEmail(wrapper.getEmail()).getIncomingRequests();
        for (UserContact userContact : incomingRequests) {
            if (userContact.getStatus() != ContactStatus.REQUESTED || userContact.isStale()) {
                incomingRequests.remove(userContact);
            }
        }
        Set<SimpleUserContact> simpleContacts = new HashSet<>();
        for (UserContact userContact : incomingRequests) {
            simpleContacts.add(userContact.getSimpleContact());
        }
        return simpleContacts;
    }

    @RequestMapping(path = "/user-outgoing-requests.json", method = RequestMethod.POST)
    public Set<SimpleUserContact> userOutgoingRequests (@RequestBody DumbEmailWrapper wrapper) {
        initializeDb();
        Set<UserContact> outgoingRequests = users.findFirstByEmail(wrapper.getEmail()).getOutgoingRequests();
        for (UserContact userContact : outgoingRequests) {
            if (userContact.getStatus() != ContactStatus.REQUESTED  || userContact.isStale()) {
                outgoingRequests.remove(userContact);
            }
        }
        Set<SimpleUserContact> simpleContacts = new HashSet<>();
        for (UserContact userContact : outgoingRequests) {
            simpleContacts.add(userContact.getSimpleContact());
        }
        return simpleContacts;
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
    public Set<SimpleUserContact> userStaleRequestsMade (@RequestBody DumbEmailWrapper wrapper) {
        initializeDb();
        User user = users.findFirstByEmail(wrapper.getEmail());
        if (user == null) {
            return null;
        }
        //todo fix this to work in one pass through the list. this is super clumsy
        Set<UserContact> outgoingRequests = user.getOutgoingRequests();
        for (UserContact userContact : outgoingRequests) {
            if (!userContact.isStale() && userContact.getStatus() == ContactStatus.REQUESTED) {
                outgoingRequests.remove(userContact);
            }
        }
        Set<SimpleUserContact> simpleContacts = new HashSet<>();
        for (UserContact userContact : outgoingRequests) {
            simpleContacts.add(userContact.getSimpleContact());
        }
        return simpleContacts;
    }

    @RequestMapping(path = "/user-stale-requests-received.json", method = RequestMethod.POST)
    public Set<SimpleUserContact> userStaleRequestsReceived (@RequestBody DumbEmailWrapper wrapper) {
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
        Set<SimpleUserContact> simpleContacts = new HashSet<>();
        for (UserContact userContact : incomingRequests) {
            simpleContacts.add(userContact.getSimpleContact());
        }
        return simpleContacts;
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

    @RequestMapping(path = "/set-photo-visibility.json", method = RequestMethod.POST)
    public Response setPhotoVisibility (@RequestBody SetPhotoVisibleRequest wrapper) {
        initializeDb();
        User user = users.findFirstByEmail(wrapper.getEmail());
        if (user == null) {
            return new Response(false);
        }
        user.setPhotoVisible(wrapper.getPhotoVisible());
        users.save(user);
        return new Response(true);
    }

    public boolean usersAreFriends (User firstUser, User secondUser) {
        Set<UserContact> firstUserIncomingRequests = firstUser.getIncomingRequests();
        Set<UserContact> firstUserOutgoingRequests = firstUser.getOutgoingRequests();

        for (UserContact userContact : firstUserIncomingRequests) {
            if (userContact.getRequester().getEmail().equals(secondUser.getEmail())) {
                return true;
            }
        }
        for (UserContact userContact : firstUserOutgoingRequests) {
            if (userContact.getRequestee().getEmail().equals(secondUser.getEmail())) {
                return true;
            }
        }
        return false;
        //todo for sure add a unit test for this
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
        User user3 = users.findFirstByEmail("tomselleck@gmail");
        User user4 = users.findFirstByEmail("tombrady@gmail");
        User user5 = users.findFirstByEmail("tomholland@gmail");
        if (user1 == null) {
            user1 = new User("paul@gmail", "Paul", "Dennis", "TIY", "Student", "drifting3");
            user1.setPhotoVisible(false);
            user1.setImageString(getImageStringFromFile("narutowheader.txt"));
            users.save(user1);
        }
        if (user2 == null) {
            user2 = new User("adrian@gmail", "Adrian", "McDaniel", "TIY", "Student", "elevation9");
            user2.setPhotoVisible(true);
            users.save(user2);
        }
        if (user3 == null) {
            user3 = new User("tomselleck@gmail", "Tom", "Selleck", "Television", "Actor", "mustache");
            user3.setPhotoVisible(true);
            users.save(user3);
        }
        if (user4 == null) {
            user4 = new User("tombrady@gmail", "Tom", "Brady", "Patriots", "Cheater", "deflate");
            user4.setPhotoVisible(false);
            users.save(user4);
        }
        if (user5 == null) {
            user5 = new User("tomholland@gmail", "Tom", "Holland", "Hollywood", "Spiderman", "secret");
            user5.setPhotoVisible(true);
            user5.setImageString(getImageStringFromFile("muy_guapo.txt"));
            users.save(user5);
        }
    }

    public void initializeEvents () {
        Event event1 = events.findFirstByEventName("Big Test Party");
        Event event2 = events.findFirstByEventName("Giant Test Luau");
        Event event3 = events.findFirstByEventName("Likely Trump Impeachment");
        Event event4 = events.findFirstByEventName("Paul's Birthday");
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
        if (event3 == null) {
            event3 = new Event("Likely Trump Impeachment", "White House", "Pennsylvania Av");
            event3.setEndTime(tomorrow);
            events.save(event3);
        }
        if (event4 == null) {
            event4 = new Event("Paul's Birthday", "Paul's Place", "Midtown");
            event4.setStartAndEndTime(new Timestamp(1493560800000L), new Timestamp(1495560800000L));
            events.save(event4);
        }
        initializeAttendees();
    }

    public void initializeAttendees() {
        User user1 = users.findFirstByEmail("paul@gmail");
        User user2 = users.findFirstByEmail("adrian@gmail");
        User user3 = users.findFirstByEmail("tomselleck@gmail");
        User user4 = users.findFirstByEmail("tombrady@gmail");
        User user5 = users.findFirstByEmail("tomholland@gmail");

        //Event event1 = events.findFirstByEventName("Big Test Party");
        Event event2 = events.findFirstByEventName("Giant Test Luau");
        Event event3 = events.findFirstByEventName("Likely Trump Impeachment");
        Event event4 = events.findFirstByEventName("Paul's Birthday");

        user1.eventCheckIn(event4);
        user2.eventCheckIn(event4);
        user3.eventCheckIn(event2);
        //Tom Brady is not invited
        user5.eventCheckIn(event4);
        user4.eventCheckIn(event3);
    }

    public void initializeContacts () {
        User paul = users.findFirstByEmail("paul@gmail");
        if (contacts.count() == 0) {
            User user2 = users.findFirstByEmail("adrian@gmail");
            UserContact userContact = new UserContact(paul, user2, ContactStatus.REQUESTED);
            contacts.save(userContact);
        }
        User tom = users.findFirstByEmail("tomholland@gmail");
        User adrian = users.findFirstByEmail("adrian@gmail");
        Set<UserContact> userContactSet = contacts.findByRequesterAndRequestee(tom, paul);
        if (userContactSet.size() == 0) {
            UserContact bffs = new UserContact();
            bffs.setRequester(tom);
            bffs.setRequestee(paul);
            bffs.setStatus(ContactStatus.FRIENDS);
            contacts.save(bffs);
        }
        Set<UserContact> userContactSet1 = contacts.findByRequesterAndRequestee(paul, adrian);
        if (userContactSet1.size() == 0) {
            UserContact teamMates = new UserContact();
            teamMates.setRequester(paul);
            teamMates.setRequestee(adrian);
            teamMates.setStatus(ContactStatus.FRIENDS);
            contacts.save(teamMates);
        }
    }

    public String getImageStringFromFile (String fileName) {
        try {
            String content = new Scanner(new File(fileName)).useDelimiter("\\Z").next();
            System.out.println("Content length: " + content.length());
            return content;
        } catch (FileNotFoundException exception) {
            System.out.println("Unable to read file with exception: " + exception.getMessage());
            return null;
        }
    }
}
