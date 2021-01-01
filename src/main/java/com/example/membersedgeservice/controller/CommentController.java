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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
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
    public List<Comment> getCommentsByUserEmail(@PathVariable String userEmail){
        ImgBoardUser user;
        //check if useremail exist
        try {
            user = restTemplate.getForObject("http://" + userServiceBaseUrl + "/user/" + userEmail,
                    ImgBoardUser.class);
            if(user == null){
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "");
            }
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "User Not Found");
        }

        // GET COMMENTS FROM IMAGE LIST
        ResponseEntity<List<Comment>> comments =
                restTemplate.exchange("http://" + commentServiceBaseUrl + "/comments/users/{userEmail}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Comment>>() {
                        }, userEmail);

        return comments.getBody();
    }

    //CHECK OK
    @GetMapping("/comments/images/{key}")
    public List<Comment> getCommentsByImageKey(@PathVariable String key){
        Image image;
        //check if imageKEY exist
        try{
            image = restTemplate.getForObject("http://" + imageServiceBaseUrl + "/images/" + key,
                    Image.class);
            if(image == null){
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "");
            }
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "ImageKey Not Found");
        }

        // GET COMMENTS FROM IMAGE LIST
        ResponseEntity<List<Comment>> comments =
                restTemplate.exchange("http://" + commentServiceBaseUrl + "/comments/images/{imagekey}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Comment>>() {
                        }, image.getKey());

        return comments.getBody();
    }

    //CHECK OK
    @PostMapping("/comments")
    public FilledImageUserComment addComment(@RequestParam String userEmail,
                                             @RequestParam String imageKey,
                                             @RequestParam String title,
                                             @RequestParam String description) throws Exception {

        ImgBoardUser user;
        //check if useremail exist
        try {
            user = restTemplate.getForObject("http://" + userServiceBaseUrl + "/user/" + userEmail,
                    ImgBoardUser.class);
            if(user == null){
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "");
            }
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "User Not Found");
        }

        Image image;
        //check if imageKEY exist
        try{
            image = restTemplate.getForObject("http://" + imageServiceBaseUrl + "/images/" + imageKey,
                    Image.class);
            if(image == null){
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "");
            }
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "ImageKey Not Found");
        }


        //Make a new comment
        Comment comment =
                restTemplate.postForObject("http://" + commentServiceBaseUrl + "/comments",
                        new Comment(title,
                                description,
                                userEmail,
                                imageKey
                        ),Comment.class);

        return new FilledImageUserComment(image, user,comment);
    }

    //CHECK OK
    @PutMapping("/comments")
    public FilledImageUserComment updateComment(@RequestParam String commentKey,
                                                @RequestParam String title,
                                                @RequestParam String description) {

        Comment comment;
        //check if commentKey exist
        try{
            comment = restTemplate.getForObject("http://" + commentServiceBaseUrl + "/comments/" + commentKey,
                    Comment.class);
            if(comment == null){
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "");
            }
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Comment key Not Found");
        }

        ImgBoardUser user = restTemplate.getForObject("http://" + userServiceBaseUrl + "/user/" + comment.getUserEmail(),
                    ImgBoardUser.class);

        Image image = restTemplate.getForObject("http://" + imageServiceBaseUrl + "/images/" + comment.getImageKey(),
                    Image.class);


        // Update comment
        comment.setTitle(title);
        comment.setDescription(description);

        ResponseEntity<Comment> responseEntityReview =
                restTemplate.exchange("http://" + commentServiceBaseUrl + "/comments",
                        HttpMethod.PUT, new HttpEntity<>(comment), Comment.class);

        Comment retrievedComment = responseEntityReview.getBody();




        return new FilledImageUserComment(image, user, retrievedComment);

    }

    //CHECK OK
    @DeleteMapping("/comments/{key}")
    public ResponseEntity deleteComment(@PathVariable String key){

        restTemplate.delete("http://" + commentServiceBaseUrl + "/comments/" + key);

        return ResponseEntity.ok().build();
    }

    }
