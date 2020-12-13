package com.example.membersedgeservice.service;


import java.util.ArrayList;

import com.example.membersedgeservice.model.ImgBoardUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${userservice.baseurl}")
    private String userServiceBaseUrl;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ImgBoardUser user = restTemplate.getForObject("http://"+userServiceBaseUrl+" /user/"+email, ImgBoardUser.class);
        if(user!=null){
            return new User(user.getEmail(), user.getPassword(),
                    new ArrayList<>());
        }else {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

    }
}