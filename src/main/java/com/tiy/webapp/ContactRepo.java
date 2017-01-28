package com.tiy.webapp;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by Paul Dennis on 1/26/2017.
 */
public interface ContactRepo extends CrudRepository<UserContact, Long> {

}
