package com.tiy.webapp;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Paul Dennis on 1/26/2017.
 */
public interface ContactRepo extends CrudRepository<UserContact, Long> {
    List<UserContact> findByRequesterEmail(String requesterEmail);
    //List<UserContact> findByRequesteeEmail(String requesteeEmail);
    List<UserContact> findByRequesterEmailAndStatus (String requesterEmail, ContactStatus status);
    //List<UserContact> findByRequesteeEmailAndStatus (String requesteeEmail, ContactStatus status);


}
