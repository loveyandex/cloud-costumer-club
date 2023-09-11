package com.salinteam.userservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.salinteam.userservice.model.audit.DateAudit;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Abolfazl Ghahremani(Joobin)  on 01/27/21.
 */

@Entity
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "phonenumber"
        })
})
@Getter
@Setter
@Accessors(chain = true)
public class User extends DateAudit {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 40)
    private String firstname;

    @Size(max = 40)
    private String lastname;

    @Size(max = 15)
    private String username;

    @NotBlank
    @Size(max = 15)
    private String phonenumber;

    @NaturalId
    @Size(max = 40)
    @Email
    private String email;

    private Long score;


    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_companyforuser",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "company_id"))
    private Set<Companyforuser> companyforusers = new HashSet<>();


    @ManyToOne
    @JoinColumn(name = "grouplevel_id", nullable = false)
    private GroupLevel groupLevel;

}