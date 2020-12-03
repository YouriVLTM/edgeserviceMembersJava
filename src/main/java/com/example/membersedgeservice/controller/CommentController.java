package com.example.membersedgeservice.controller;

import com.example.membersedgeservice.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

@RestController
public class CommentController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${commentservice.baseurl}")
    private String commentServiceBaseUrl;



}
