package com.example.membersedgeservice.controller;

import com.example.membersedgeservice.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.example.membersedgeservice.config.JwtTokenUtil;
@RestController
public class UserController {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${userservice.baseurl}")
    private String userServiceBaseUrl;

    @PostMapping(value ="/user")
    public ImgBoardUser register(@RequestBody ImgBoardUser userRequest){

        //Make a new comment
        //ImgBoardUser user = restTemplate.postForObject("http://" + userServiceBaseUrl + "/login",
        //ImgBoardUser user = restTemplate.postForObject("http://localhost:8051/user",
        ImgBoardUser user = restTemplate.postForObject("http://" + userServiceBaseUrl + "/user",
                userRequest,ImgBoardUser.class);
        return user;
        //System.out.println("email user :" + user.getEmail());
        //return user;
        //
    }
    @PutMapping("/user")
    public ImgBoardUser updateUserPassword(@RequestBody ImgBoardUser updateUser,@RequestHeader(name="Authorization") String token) {
        String jwtToken = token.substring(7);
        String email = jwtTokenUtil.getUsernameFromToken(jwtToken);
        if(updateUser.getEmail().equalsIgnoreCase(email))
        {
            HttpEntity<ImgBoardUser> entity = new HttpEntity<ImgBoardUser>(updateUser);

            HttpEntity<ImgBoardUser> user =restTemplate.exchange("http://" + userServiceBaseUrl + "/user", HttpMethod.PUT,
                    entity,ImgBoardUser.class);
            return user.getBody();
        }else{
            return null;
        }

    }
    @DeleteMapping("/user/{email}")
    public ResponseEntity deleteUser(@PathVariable String email,@RequestHeader(name="Authorization") String token){
        String jwtToken = token.substring(7);
        String emailToken = jwtTokenUtil.getUsernameFromToken(jwtToken);
        if(emailToken.equalsIgnoreCase(email)){
            //TODO delete evryting from user



            ResponseEntity response =restTemplate.exchange("http://" + userServiceBaseUrl + "/user/"+email, HttpMethod.DELETE,null,
                    String.class);
            return response;
        }else{
            return ResponseEntity.status(403).build();
        }
    }
}
