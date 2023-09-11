package com.salinteam.userservice.repository;

import com.salinteam.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Abolfazl Ghahremani(Joobin)  on 01/27/21.
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhonenumber(String phonenumber);

}
