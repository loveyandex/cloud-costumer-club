package com.salinteam.eventservice.model;

import com.salinteam.eventservice.model.audit.DateAudit;
import com.salinteam.eventservice.security.model.Company;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Abolfazl Ghahremani(Joobin)  on 01/27/21.
 */


@Getter
@Setter
@Accessors(chain = true)
public class User extends DateAudit {

    private Long id;

    private String firstname;

    private String lastname;

    private String username;

    private String phonenumber;

    private String email;

    private Long score;

    private Set<Company> companies = new HashSet<>();


    private GroupLevel groupLevel;


    public User() {

    }


}