package com.salinteam.userservice.security.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Abolfazl Ghahremani(Joobin)  on 01/27/21.
 */


@Getter
@Setter
public class Company {

    private Long id;

    @Size(max = 40)
    private String name;


    @NotBlank
    @Size(max = 15)
    private String username;

    @NotBlank
    @Size(max = 100)
    private String password;

    @ManyToMany
    @JoinTable(
            name = "company_role",
            joinColumns = {@JoinColumn(name = "company_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    @BatchSize(size = 20)
    private Set<Role> roles = new HashSet<>();


    public Company(Long id) {
        this.id = id;
    }

    public Company() {

    }

    public Company(@Size(max = 40) String name, @NotBlank @Size(max = 15) String username, @NotBlank @Size(max = 100) String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }
}