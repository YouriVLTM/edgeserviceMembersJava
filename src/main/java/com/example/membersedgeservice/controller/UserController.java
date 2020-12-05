package com.example.membersedgeservice.controller;

import com.example.membersedgeservice.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class UserController {
    @Autowired
    private RestTemplate restTemplate;

    //@Value("${userservice.baseurl}")
    //private String userServiceBaseUrl;

    @PostMapping(value ="/login")
    public ImgBoardUser login(@RequestBody Login login){

        //Make a new comment
        //ImgBoardUser user = restTemplate.postForObject("http://" + userServiceBaseUrl + "/login",
        ImgBoardUser user = restTemplate.postForObject("http://localhost:8051/login",
                        login,ImgBoardUser.class);
        System.out.println("email user :" + user.getEmail());
        return user;
    }
}
