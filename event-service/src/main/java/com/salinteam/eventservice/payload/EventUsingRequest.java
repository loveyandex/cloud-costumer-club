package com.salinteam.eventservice.payload;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * Created by Abolfazl Ghahremani(Joobin)  on 1/29/2021 , 7:05 PM.
 */


@Data
@Accessors(chain = true)
public class EventUsingRequest {

    @NotBlank
    private String eventId;

    @NotBlank
    private String userId;


}
