package com.salinteam.userservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.salinteam.userservice.model.audit.DateAudit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "group_level")
public class GroupLevel extends DateAudit {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @JsonIgnore
    private String levelName;


    @JsonIgnore
    private Long minscore;

    @JsonIgnore
    private Long maxscore;

    public GroupLevel(Long id, String levelName, Long minscore, Long maxscore) {
        this.id = id;
        this.levelName = levelName;
        this.minscore = minscore;
        this.maxscore = maxscore;
    }

    public GroupLevel(Long grouplevel_id) {
        this.id = grouplevel_id;
    }

    public GroupLevel() {

    }

    @JsonIgnore
    @OneToMany(
            mappedBy = "groupLevel",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    private List<User> users;


}
