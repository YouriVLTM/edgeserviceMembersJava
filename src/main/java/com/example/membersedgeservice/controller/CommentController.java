package com.example.membersedgeservice.controller;

import com.example.membersedgeservice.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class CommentController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${commentservice.baseurl}")
    private String commentServiceBaseUrl;

    @GetMapping("/comments/images/{imagekey}")
    public List<Comment> getRankingsByUserId(@PathVariable String imagekey){

        //DOTO CHECK imageKEY EXIST


        // GET COMMENTS FROM IMAGE LIST
        ResponseEntity<List<Comment>> responseEntityReviews =
                restTemplate.exchange("http://" + commentServiceBaseUrl + "/comments/images/{imagekey}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Comment>>() {
                        }, imagekey);

        return responseEntityReviews.getBody();
    }

}
