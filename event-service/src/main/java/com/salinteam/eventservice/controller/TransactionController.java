package com.salinteam.eventservice.controller;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.salinteam.eventservice.exception.ResourceNotFoundException;
import com.salinteam.eventservice.model.CoTransaction;
import com.salinteam.eventservice.model.GroupLevel;
import com.salinteam.eventservice.model.User;
import com.salinteam.eventservice.payload.ApiResponse;
import com.salinteam.eventservice.payload.TransactionUsingRequest;
import com.salinteam.eventservice.repository.CoTransactionRepository;
import com.salinteam.eventservice.security.security.CompanyPrincipal;
import com.salinteam.eventservice.security.security.CurrentCompany;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/")
@Api(value = "/api/", description = " event service", produces = "application/json")
public class TransactionController {

    @Autowired
    CoTransactionRepository coTransactionRepository;

    private UserClient userClientTrans;

    public TransactionController(UserClient userClientTrans) {
        this.userClientTrans = userClientTrans;
    }

    @CrossOrigin
    @HystrixCommand(fallbackMethod = "fallback")
    public Optional<User> getuser(String phonenumber) {
        return userClientTrans.getUser(phonenumber);
    }

    @CrossOrigin
    @HystrixCommand(fallbackMethod = "fallback")
    public ResponseEntity<?> updateUser(String phone, Long score) {
        return userClientTrans.saveUser(phone, score);
    }


    @PostMapping("/transaction/use")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", example = "Bearer access_token")
    public ResponseEntity<?> useTrans(@Valid @RequestBody TransactionUsingRequest transactionUsingRequest,
                                      @CurrentCompany CompanyPrincipal currentUser) {

        String userId = transactionUsingRequest.getUserId();
        //todo update this way

        User user = getuser(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));


        GroupLevel groupLevel = user.getGroupLevel();


        String transId = transactionUsingRequest.getTransId();

        CoTransaction coTransaction = coTransactionRepository.findCoTransactionByCompanyIdAndTransactionIdAndGroupLevelId(currentUser.getId(),
                transId, groupLevel.getId())
                .orElseThrow(() -> new ResourceNotFoundException("The User Could Not Operate At This Level. ", "transId", transId));


//        GroupLevel groupLevel = groupLevelRepository.findGroupLevelById(userLevelId)
//                .orElseThrow(() -> new ResourceNotFoundException("groupLevel", "groupLevel", userLevelId));


        Long price = transactionUsingRequest.getPrice();

        long scorable = price / coTransaction.getUnitprice();


        Long beforescore = user.getScore();

        user.setScore((scorable + beforescore));

        ResponseEntity<?> responseEntity = updateUser(userId, user.getScore());

        return responseEntity;

    }


    @PostMapping("/transaction/add")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", example = "Bearer access_token")
    public ResponseEntity<?> addnewTrans(@Valid @RequestBody CoTransaction coTransaction,
                                         @CurrentCompany CompanyPrincipal currentUser) {

        try {
            coTransactionRepository.save(coTransaction.setCompanyId(currentUser.getId()));
            return ResponseEntity.ok(new ApiResponse(true,
                    "transaction added successfully"));

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "transactionId duplicated"));

        }
    }


}
