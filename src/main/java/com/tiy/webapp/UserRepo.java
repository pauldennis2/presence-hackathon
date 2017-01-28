package com.tiy.webapp;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Paul Dennis on 1/26/2017.
 */
public interface UserRepo extends CrudRepository<User, Long> {
    User findFirstByEmail(String email);
    List<User> findByCheckedInEventId(Long checkedInEventId);
}
