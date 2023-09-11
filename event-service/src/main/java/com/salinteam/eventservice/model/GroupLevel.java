package com.salinteam.eventservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.salinteam.eventservice.model.audit.DateAudit;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GroupLevel extends DateAudit {

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
    private List<User> users;

    @JsonIgnore
    private List<CoEvent> coEvents;


}
