package com.salinteam.eventservice.controller;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.salinteam.eventservice.exception.ResourceNotFoundException;
import com.salinteam.eventservice.model.CoEvent;
import com.salinteam.eventservice.model.User;
import com.salinteam.eventservice.payload.ApiResponse;
import com.salinteam.eventservice.payload.EventUsingRequest;
import com.salinteam.eventservice.repository.CoEventRepository;
import com.salinteam.eventservice.security.security.CompanyPrincipal;
import com.salinteam.eventservice.security.security.CurrentCompany;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@EnableFeignClients
@RestController
@RequestMapping("/")
@Api(value = "/", description = " event service", produces = "application/json")
public class BusinessController {


    @Autowired
    CoEventRepository coEventRepository;

    @Autowired
    private Environment environment;


    @Autowired
    PasswordEncoder passwordEncoder;


    @PostMapping("/event/add")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", example = "Bearer access_token")
    public ResponseEntity<?> addnewevent(@Valid @RequestBody CoEvent coEvent,
                                         @CurrentCompany CompanyPrincipal currentUser) {

        try {
            coEventRepository.save(coEvent.setCompanyId(currentUser.getId()));
            System.out.println("evebnt service");
            return ResponseEntity.ok(new ApiResponse(true, "event added in port "
                    + Integer.parseInt(environment.getProperty("local.server.port"))));

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getLocalizedMessage()));

        }
    }

    private UserClient userClient;

    public BusinessController(UserClient userClient) {
        this.userClient = userClient;
    }

    @CrossOrigin
    @HystrixCommand(fallbackMethod = "fallback")
    public Optional<User> getuser(String phonenumber) {
        return userClient.getUser(phonenumber);
    }

    @CrossOrigin
    @HystrixCommand(fallbackMethod = "fallback")
    public ResponseEntity<?> updateUser(String phone, Long score) {
        return userClient.saveUser(phone, score);
    }


    @PostMapping("/event/use")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", example = "Bearer access_token")
    public ResponseEntity<?> useevent(@Valid @RequestBody EventUsingRequest eventUsingRequest,
                                      @CurrentCompany CompanyPrincipal currentUser) {


        String userId = eventUsingRequest.getUserId();

        User user = getuser(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));


        CoEvent coEvent = coEventRepository.findCoEventByCompanyIdAndAndEventIdAndGroupLevelId(
                currentUser.getId(), eventUsingRequest.getEventId(), user.getGroupLevel().getId())
                .orElseThrow(() -> new ResourceNotFoundException("event", "eventId", eventUsingRequest.getEventId()));


        int scoreValue = coEvent.getScoreValue();
        user.setScore(scoreValue + user.getScore());

        ResponseEntity<?> responseEntity = updateUser(userId, Long.valueOf(user.getScore()));


        return responseEntity;

    }

    @GetMapping("/event/{eventId}")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", example = "Bearer access_token")
    public List<CoEvent> eventsbyeventId(@PathVariable String eventId) {
        return coEventRepository.findCoEventsByEventId(eventId);
    }



    @GetMapping("/events")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", example = "Bearer access_token")
    public List<CoEvent> events() {
//        return List.of(new CoEvent());
        List<CoEvent> all = coEventRepository.findAll();
        String port = environment.getProperty("local.server.port");
        all.get(0).setPort(port);
        return all;
    }





}