package com.example.membersedgeservice.model;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.Date;

public class ImageLike {
    private int id;
    private String userEmail;
    private String imageKey;
    private boolean state;
    private String likeKey;

    public ImageLike(){}

    public ImageLike(String userEmail, String imageKey, ImageLike newLike) {
        this.userEmail = userEmail;
        this.imageKey = imageKey;
        this.state = newLike.state;
        this.likeKey = DigestUtils.sha256Hex(imageKey + userEmail + new Date(System.currentTimeMillis()));
    }
    public ImageLike(boolean state, String userEmail, String imageKey) {
        this.state = state;
        this.userEmail = userEmail;
        this.imageKey = imageKey;
        this.likeKey = DigestUtils.sha256Hex(imageKey + userEmail + new Date(System.currentTimeMillis()));
    }

    public String getLikeKey() {
        return this.likeKey;
    }

    public void setLikeKey(String key) {
        this.likeKey = likeKey;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserEmail(){
        return userEmail;
    }

    public void setUserEmail(String userEmail){
        this.userEmail = userEmail;
    }

    public String getImageKey(){
        return imageKey;
    }

    public void setImageKey(String imageKey){
        this.imageKey = imageKey;
    }

    public Boolean getState(){
        return state;
    }

    public void setState(Boolean state){
        this.state = state;
    }
}
