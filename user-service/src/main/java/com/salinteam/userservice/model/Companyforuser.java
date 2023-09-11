package com.salinteam.userservice.model;

import com.salinteam.userservice.model.audit.DateAudit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by Abolfazl Ghahremani(Joobin)  on 01/27/21.
 */

@Entity
@Table(name = "companyforuser")
@Getter
@Setter
public class Companyforuser extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Companyforuser(Long id) {
        this.id = id;
    }

    public Companyforuser() {
    }
}