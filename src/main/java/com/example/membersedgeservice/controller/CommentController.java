package com.example.membersedgeservice.controller;

import com.example.membersedgeservice.model.Comment;
import com.example.membersedgeservice.model.FilledImageUserComment;
import com.example.membersedgeservice.model.Image;
import com.example.membersedgeservice.model.User;
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

        //DOTO check if imageKEY exist


        // GET COMMENTS FROM IMAGE LIST
        ResponseEntity<List<Comment>> responseEntityReviews =
                restTemplate.exchange("http://" + commentServiceBaseUrl + "/comments/images/{imagekey}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Comment>>() {
                        }, imagekey);

        return responseEntityReviews.getBody();
    }

    @PostMapping("/comments")
    public FilledImageUserComment addComment(@RequestParam String userEmail,
                                             @RequestParam String imageKey,
                                             @RequestParam String title,
                                             @RequestParam String description){

        //TODO check if Userkey exist
        User user = new User(userEmail);


        //TODO check if imageKey exist
        Image image = new Image();
        image.setKey(imageKey);


        //Make a new comment
        Comment comment =
                restTemplate.postForObject("http://" + commentServiceBaseUrl + "/comments",
                        new Comment(title,description,userEmail,imageKey),Comment.class);

        return new FilledImageUserComment(user,image,comment);
    }

}
