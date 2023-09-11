package com.salinteam.apigateway.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Abolfazl Ghahremani(Joobin)  on 01/27/21.
 */

@Entity
@Table(name = "company", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        })
})
@Getter
@Setter
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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