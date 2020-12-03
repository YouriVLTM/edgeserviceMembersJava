package com.example.demo.controller;

import com.example.demo.model.Comment;
import com.example.demo.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/comments")
    public List<Comment> getComments()
    {
        return commentRepository.findAll();
    }

    @GetMapping("/comments/{key}")
    public Comment getComment(@PathVariable String key)    {
        return commentRepository.findByKey(key);
    }

    @PostMapping("/comments")
    Comment addComment(@RequestBody Comment newComment)
    {
        // create date and update date
        newComment.setCreateDate(new Date(System.currentTimeMillis()));
        newComment.setUpdateDate(new Date(System.currentTimeMillis()));
        commentRepository.save(newComment);
        return newComment;
    }

    @PutMapping("/comments")
    public Comment updateComment(@RequestBody Comment updateComment)
    {
        Comment comment = commentRepository.findByKey(updateComment.getKey());
        if(comment != null){
            comment.setTitle(updateComment.getTitle());
            comment.setDescription(updateComment.getDescription());
            comment.setUserEmail(updateComment.getUserEmail());
            comment.setImageKey(updateComment.getImageKey());
            comment.setUpdateDate(new Date(System.currentTimeMillis()));
            commentRepository.save(comment);
            return comment;
        }else{
            return null;
        }

    }

    @DeleteMapping("/comments/{key}")
    public ResponseEntity<Object> deleteComment(@PathVariable String key)
    {
        Comment comment = commentRepository.findByKey(key);
        if(comment != null){
            commentRepository.delete(comment);
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }



}
