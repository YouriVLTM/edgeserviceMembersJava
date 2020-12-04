package com.example.membersedgeservice.model;

import static org.springframework.beans.BeanUtils.copyProperties;

public class FilledImageUserComment {
    private User user;
    private Image image;
    private String title;
    private String description;
    private String commentKey;

    public FilledImageUserComment() {
    }

    public FilledImageUserComment(User user,Image image,Comment comment) {
        this.user = new User(user.getUserEmail());
        this.image = new Image(image.getSource(), image.getUserEmail(), image.getDescription(), image.getKey());
        this.title = comment.getTitle();
        this.description = comment.getDescription();
    }

    public FilledImageUserComment(Image image, User user, String title, String description, String commentKey) {
        this.image = image;
        this.user = user;
        this.title = title;
        this.description = description;
        this.commentKey = commentKey;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCommentKey() {
        return commentKey;
    }

    public void setCommentKey(String commentKey) {
        this.commentKey = commentKey;
    }
}
