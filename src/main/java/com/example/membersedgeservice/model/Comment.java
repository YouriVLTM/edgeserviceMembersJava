package com.example.membersedgeservice.model;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class Comment {

    private String id;
    private String title;
    private String description;
    private String userEmail;
    private String imageKey;
    private String key;

    @DateTimeFormat(style="yyyyMMdd'T'HHmmss.SSSZ")
    private java.util.Date createDate;

    @DateTimeFormat(style="yyyyMMdd'T'HHmmss.SSSZ")
    private java.util.Date updateDate;

    public Comment(){

    }

    public Comment( String imageKey,String userEmail,Comment newComment) {
        this.title = newComment.title;
        this.description = newComment.description;
        this.userEmail = userEmail;
        this.imageKey = imageKey;
        this.key = DigestUtils.sha256Hex(title + userEmail + new Date(System.currentTimeMillis()));
    }

    public Comment(String title, String description, String userEmail, String imageKey) {
        this.title = title;
        this.description = description;
        this.userEmail = userEmail;
        this.imageKey = imageKey;
        this.key = DigestUtils.sha256Hex(title + userEmail + new Date(System.currentTimeMillis()));
    }

    public Comment(String title, String description, String userEmail, String imageKey, String key) {
        this.title = title;
        this.description = description;
        this.userEmail = userEmail;
        this.imageKey = imageKey;
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }


    public String getImageKey() {
        return imageKey;
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
