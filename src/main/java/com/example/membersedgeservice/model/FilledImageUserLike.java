package com.example.membersedgeservice.model;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.Date;

public class FilledImageUserLike {
    private User user;
    private Image image;
    private boolean state;
    private String likeKey;

    public FilledImageUserLike() {
    }

    public FilledImageUserLike(Image image,User user,ImageLike like) {
        this.user = new User(user.getUserEmail());
        this.image = new Image(image.getSource(), image.getUserEmail(), image.getDescription(), image.getKey());
        this.state = like.getState();
        this.likeKey = like.getLikeKey();
    }

    public FilledImageUserLike(Image image, User user, boolean state, String likeKey) {
        this.image = image;
        this.user = user;
        this.state = state;
        this.likeKey = likeKey;
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

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getLikeKey() {
        return likeKey;
    }

    public void setLikeKey(String likeKey) {
        this.likeKey = likeKey;
    }
}
