package com.example.membersedgeservice.controller;

import com.example.membersedgeservice.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
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

    @Value("${userservice.baseurl}")
    private String userServiceBaseUrl;

    @Value("${imageservice.baseurl}")
    private String imageServiceBaseUrl;

    //CHECK unit OK
    @GetMapping("/comments/users/{userEmail}")
    public ResponseEntity<List<Comment>> getCommentsByUserEmail(@PathVariable String userEmail){
        //check if useremail exist
        ImgBoardUser user = restTemplate.getForObject("http://" + userServiceBaseUrl + "/user/" + userEmail,
                ImgBoardUser.class);
        if(user == null){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "User not found");
        }

        // GET COMMENTS FROM IMAGE LIST
        ResponseEntity<List<Comment>> comments =
                restTemplate.exchange("http://" + commentServiceBaseUrl + "/comments/users/{userEmail}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Comment>>() {
                        }, userEmail);

        return comments;
    }

    //CHECK OK
    @GetMapping("/comments/images/{key}")
    public ResponseEntity<List<Comment>> getCommentsByImageKey(@PathVariable String key){

        //check if imageKEY exist
        Image image = restTemplate.getForObject("http://" + imageServiceBaseUrl + "/images/" + key,
                Image.class);
        if(image == null){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Image not found");
        }

        // GET COMMENTS FROM IMAGE LIST
        ResponseEntity<List<Comment>> comments =
                restTemplate.exchange("http://" + commentServiceBaseUrl + "/comments/images/{imagekey}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Comment>>() {
                        }, image.getKey());

        return comments;
    }

    //CHECK OK
    @PostMapping("/comments")
    public ResponseEntity<FilledImageUserComment> addComment(@RequestParam String userEmail,
                                             @RequestParam String imageKey,
                                             @RequestParam String title,
                                             @RequestParam String description) throws Exception {

        //check if useremail exist
        ImgBoardUser user = restTemplate.getForObject("http://" + userServiceBaseUrl + "/user/" + userEmail,
                ImgBoardUser.class);
        if(user == null){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "User not found");
        }

        // check if imageKey exist
        Image image = restTemplate.getForObject("http://" + imageServiceBaseUrl + "/images/" + imageKey,
                Image.class);
        if(image == null){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Image not found");
        }


        //Make a new comment
        Comment comment =
                restTemplate.postForObject("http://" + commentServiceBaseUrl + "/comments",
                        new Comment(title,
                                description,
                                userEmail,
                                imageKey
                        ),Comment.class);

        return new ResponseEntity<FilledImageUserComment>(new FilledImageUserComment(image, user,comment), HttpStatus.OK);
    }

    //CHECK OK
    @PutMapping("/comments")
    public ResponseEntity<FilledImageUserComment> updateComment(@RequestParam String commentKey,
                                                @RequestParam String title,
                                                @RequestParam String description) {

        //check if commentKey exist
        Comment comment = restTemplate.getForObject("http://" + commentServiceBaseUrl + "/comments/" + commentKey,
                Comment.class);

        //check if useremail exist
        ImgBoardUser user = restTemplate.getForObject("http://" + userServiceBaseUrl + "/user/" + comment.getUserEmail(),
                ImgBoardUser.class);
        if(user == null){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "User not found");
        }

        // check if imageKey exist
        Image image = restTemplate.getForObject("http://" + imageServiceBaseUrl + "/images/" + comment.getImageKey(),
                Image.class);
        if(image == null){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Image not found");
        }


        // Update comment
        comment.setTitle(title);
        comment.setDescription(description);

        ResponseEntity<Comment> responseEntityReview =
                restTemplate.exchange("http://" + commentServiceBaseUrl + "/comments",
                        HttpMethod.PUT, new HttpEntity<>(comment), Comment.class);

        Comment retrievedComment = responseEntityReview.getBody();




        return new ResponseEntity<FilledImageUserComment>(new FilledImageUserComment(image, user, retrievedComment), HttpStatus.OK);

    }

    //CHECK OK
    @DeleteMapping("/comments/{key}")
    public ResponseEntity deleteComment(@PathVariable String key){

        restTemplate.delete("http://" + commentServiceBaseUrl + "/comments/" + key);

        return ResponseEntity.ok().build();
    }

    }
