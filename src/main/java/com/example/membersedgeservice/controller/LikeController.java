package com.example.membersedgeservice.controller;

import com.example.membersedgeservice.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class LikeController {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${likeservice.baseurl}")
    private String likeServiceBaseUrl;

    @GetMapping("/likes/images/{key}")
    public List<Like> getLikesByImageId(@PathVariable String key){

        // does image exist?


        // get likes by image
        ResponseEntity<List<Like>> responseEntityReviews =
                restTemplate.exchange("http://" + likeServiceBaseUrl + "/likes/images/{key}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Like>>() {
                        }, key);

        return responseEntityReviews.getBody();
    }
}
