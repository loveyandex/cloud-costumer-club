package com.salinteam.eventservice.controller;

import com.salinteam.eventservice.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@FeignClient("user-service")
interface UserClient {

    @GetMapping("/user/{phonenumber}")
    @CrossOrigin
    Optional<User> getUser(@PathVariable String phonenumber);

    @PostMapping("/user/save")
    @CrossOrigin
    User saveUser(@RequestBody User user);

    @GetMapping("/user/{phonenumber}/{score}")
    public ResponseEntity<?> saveUser(@PathVariable String phonenumber,
                                      @PathVariable Long score);

}
