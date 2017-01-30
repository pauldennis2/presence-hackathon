package com.tiy.webapp;

import org.springframework.data.repository.CrudRepository;

import java.util.Set;

/**
 * Created by Paul Dennis on 1/26/2017.
 */
public interface UserRepo extends CrudRepository<User, Long> {
    User findFirstByEmail(String email);
    Set<User> findByCheckedInEventId(Long checkedInEventId);

    //List<User> findByIncomingRequestsOrOutgoingRequestsAndStatus (User user, ContactStatus status);
}
