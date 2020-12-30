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
@RestController
public class ImageLikeController {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${likeservice.baseurl}")
    private String likeServiceBaseUrl;

    @GetMapping("/likes/image/{imageKey}")
    public List<ImageLike> getLikesByImageKey(@PathVariable String imageKey){

        // does image exist?

        // get likes by image
        ResponseEntity<List<ImageLike>> responseEntityReviews =
                restTemplate.exchange("http://" + likeServiceBaseUrl + "/likes/image/{imageKey}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<ImageLike>>() {
                        }, imageKey);

        return responseEntityReviews.getBody();
    }
    @GetMapping("/likes/user/{userEmail}")
    public List<ImageLike> getlikesByUserEmail(@PathVariable String userEmail){

        // does image exist?

        // get likes by user
        ResponseEntity<List<ImageLike>> responseEntityReviews =
                restTemplate.exchange("http://" + likeServiceBaseUrl + "/likes/user/{userEmail}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<ImageLike>>() {
                        }, userEmail);

        return responseEntityReviews.getBody();
    }

    @GetMapping("/likes/image/{imageKey}/state/{state}")
    public List<ImageLike> getLikesByImageKeyAndState(@PathVariable String imageKey, @PathVariable boolean state){

        // does image exist?

        // get likes by image and state
        ResponseEntity<List<ImageLike>> responseEntityReviews =
                restTemplate.exchange("http://" + likeServiceBaseUrl + "/likes/image/{imageKey}/state/{state}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<ImageLike>>() {
                        }, imageKey, state);

        return responseEntityReviews.getBody();
    }

    @GetMapping("/likes/image/{imageKey}/user/{userEmail}")
    public ImageLike getLikeByImageKeyAndUserEmail(@PathVariable String imageKey, @PathVariable String userEmail){

        // does image exist?

        // get like by image and user
        ResponseEntity<ImageLike> responseEntityReviews =
                restTemplate.exchange("http://" + likeServiceBaseUrl + "/likes/image/{imageKey}/user/{userEmail}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<ImageLike>() {
                        }, imageKey, userEmail);

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
