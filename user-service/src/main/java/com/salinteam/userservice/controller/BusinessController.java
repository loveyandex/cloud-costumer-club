package com.salinteam.userservice.controller;

import com.salinteam.userservice.exception.ResourceNotFoundException;
import com.salinteam.userservice.model.Companyforuser;
import com.salinteam.userservice.model.GroupLevel;
import com.salinteam.userservice.model.User;
import com.salinteam.userservice.payload.AddNewUserRequest;
import com.salinteam.userservice.payload.ApiResponse;
import com.salinteam.userservice.repository.CompanyforuserRepository;
import com.salinteam.userservice.repository.GroupLevelRepository;
import com.salinteam.userservice.repository.UserRepository;
import com.salinteam.userservice.security.security.CompanyPrincipal;
import com.salinteam.userservice.security.security.CurrentCompany;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;


@RestController
@RequestMapping("/")
@Api(value = "/", description = "client(company) functionalities such as adding user , event , transactions and use them to score users", produces = "application/json")
public class BusinessController {


    @Autowired
    UserRepository userRepository;

    @Autowired
    CompanyforuserRepository companyforuserRepository;

    @Autowired
    GroupLevelRepository groupLevelRepository;

    @PostMapping("/user/add")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", example = "Bearer access_token")
    public ResponseEntity<?> addnewuser(@Valid @RequestBody AddNewUserRequest addNewUserRequest,
                                        @CurrentCompany CompanyPrincipal companyPrincipal) {


        Optional<Companyforuser> companyforuser = companyforuserRepository.findById(companyPrincipal.getId());
        if (!companyforuser.isPresent()) {
            Companyforuser s = new Companyforuser(companyPrincipal.getId());
            companyforuserRepository.save(s);
            companyforuser = Optional.of(s);
        }


        Optional<User> useropt = userRepository.findByPhonenumber(addNewUserRequest.getPhonenumber());


        long id = 1L;
        GroupLevel groupLevel = groupLevelRepository
                .findGroupLevelById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GroupLevel", "grouplevel_id", id));


        Long companyPrincipalId = companyPrincipal.getId();
        if (useropt.isPresent()) {
            Set<Companyforuser> companies = useropt.get().getCompanyforusers();
            for (Companyforuser companyforuser0 : companies) {
                if (companyforuser0.getId() == companyPrincipalId) {
                    return new ResponseEntity(new ApiResponse(true, "Phone number is already taken!"),
                            HttpStatus.OK);
                }
            }

            companies.add(new Companyforuser(companyPrincipalId));
            userRepository.save(useropt.get());
            return new ResponseEntity(new ApiResponse(true, "User registered successfully"),
                    HttpStatus.OK);
        }
        User user = new User();
        user.setPhonenumber(addNewUserRequest.getPhonenumber())
                .setGroupLevel(groupLevel).
                setScore(groupLevel.getMinscore()).
                setCompanyforusers(Collections.singleton(companyforuser.get()));


        User result = userRepository.save(user);

        return ResponseEntity.ok()
                .body(new ApiResponse(true, "User registered successfully"));
    }

    @GetMapping("/user/{phonenumber}")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", example = "Bearer access_token")
    public ResponseEntity<?> getUserByPhonenumber(@PathVariable String phonenumber,
                                                  @CurrentCompany CompanyPrincipal companyPrincipal) {
        Long companyPrincipalId = companyPrincipal.getId();
        User user = userRepository.findByPhonenumber(phonenumber)
                .orElseThrow(() -> new ResourceNotFoundException("User", "phonenumber", phonenumber));
        return ResponseEntity.ok(user);

    }


    @PostMapping("/user/save")
    public ResponseEntity<?> saveUser(@PathVariable User user0,
                                      @CurrentCompany CompanyPrincipal companyPrincipal) {

        String phonenumber = user0.getPhonenumber();
        User user = userRepository.findByPhonenumber(phonenumber)
                .orElseThrow(() -> new ResourceNotFoundException("User", "phonenumber", phonenumber));

        User save = userRepository.save(user);
        return ResponseEntity.ok(save);
    }

    @GetMapping("/user/{phonenumber}/{score}")
    public ResponseEntity<?> saveUser(@PathVariable String phonenumber,
                                      @PathVariable Long score,
                                      @CurrentCompany CompanyPrincipal companyPrincipal) {

        User user = userRepository.findByPhonenumber(phonenumber)
                .orElseThrow(() -> new ResourceNotFoundException("User", "phonenumber", phonenumber));

        user.setScore(score);

        GroupLevel groupLevel = groupLevelRepository.findByScore(user.getScore())
                .orElseThrow(() -> new ResourceNotFoundException("GroupLevel",
                        "score", score));
        user.setGroupLevel(groupLevel);

        User save = userRepository.save(user);
        return ResponseEntity.ok(save);
    }

}