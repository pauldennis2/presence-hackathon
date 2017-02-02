package com.tiy.webapp;

import org.springframework.data.repository.CrudRepository;

import java.util.Set;

/**
 * Created by Paul Dennis on 1/26/2017.
 */
public interface ContactRepo extends CrudRepository<UserContact, Long> {
    //Set<UserContact> findByRequesterEmail(String requesterEmail);
//    Set<UserContact> findByRequesterEmailAndStatus (String requesterEmail, ContactStatus status);

    Set<UserContact> findByRequesterAndRequestee(User requester, User requestee);
}
