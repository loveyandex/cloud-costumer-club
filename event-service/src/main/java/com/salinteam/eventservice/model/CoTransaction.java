package com.salinteam.eventservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.salinteam.eventservice.model.audit.DateAudit;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Created by Abolfazl Ghahremani(Joobin)  on 1/30/2021 , 3:54 PM.
 */

@Entity
@Table(name = "cotransaction", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "transaction_id", "co_id", "grouplevel_id"
        })
})
@Data
@Accessors(chain = true)
public class CoTransaction extends DateAudit {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @JsonIgnore
    @Column(name = "co_id")
    private Long companyId;


    @NotBlank
    @Size(max = 15)
    @Column(name = "transaction_id")
    private String transactionId;

    @Size(max = 40)
    private String name;

    private Long unitprice;

    //todo

    @Column(name = "grouplevel_id")
    private Long groupLevelId;


    public CoTransaction() {

    }
}
