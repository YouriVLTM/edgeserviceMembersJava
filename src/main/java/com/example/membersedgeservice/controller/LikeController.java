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
    public List<ImageLike> getLikesByImageId(@PathVariable String key){

        // does image exist?

        // get likes by image
        ResponseEntity<List<ImageLike>> responseEntityReviews =
                restTemplate.exchange("http://" + likeServiceBaseUrl + "/likes/images/{key}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<ImageLike>>() {
                        }, key);

        return responseEntityReviews.getBody();
    }

    @PostMapping("/likes")
    public FilledImageUserLike addLike(@RequestParam String userEmail,
                                             @RequestParam String imageKey,
                                             @RequestParam boolean state){

        //TODO check if Userkey exist
        User user = new User(userEmail);


        //TODO check if imageKey exist
        Image image = new Image();
        image.setKey(imageKey);


        //Make a new like
        ImageLike like =
                restTemplate.postForObject("http://" + likeServiceBaseUrl + "/likes",
                        new ImageLike(state,userEmail,imageKey),ImageLike.class);

        return new FilledImageUserLike(image, user, like);
    }

    @PutMapping("/likes")
    public FilledImageUserLike updateLike(@RequestParam String likeKey,
                                                @RequestParam boolean state) {

        //check if likeKey exist
        ImageLike like = restTemplate.getForObject("http://" + likeServiceBaseUrl + "/likes/" + likeKey,
                ImageLike.class);

        // Update like
        like.setState(state);
        ResponseEntity<ImageLike> responseEntityReview =
                restTemplate.exchange("http://" + likeServiceBaseUrl + "/likes",
                        HttpMethod.PUT, new HttpEntity<>(like), ImageLike.class);

        ImageLike retrievedLike = responseEntityReview.getBody();

        //TODO get user
        User user = new User();

        //TODO get Images
        Image image = new Image();


        return new FilledImageUserLike(image, user, retrievedLike);

    }

    @DeleteMapping("/likes/{key}")
    public ResponseEntity deleteLike(@PathVariable String key){

        restTemplate.delete("http://" + likeServiceBaseUrl + "/likes/" + key);

        return ResponseEntity.ok().build();
    }
}
