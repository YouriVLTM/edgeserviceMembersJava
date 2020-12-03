package com.example.demo.model;

import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.HashIndexed;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;
import org.apache.commons.codec.digest.DigestUtils;
import java.util.Date;

@Document(collection = "comment")
public class Comment {

    @Id
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

    public Comment(String title, String description, String userEmail, String imageKey) {
        this.title = title;
        this.description = description;
        this.userEmail = userEmail;
        this.imageKey = imageKey;
        this.key = DigestUtils.sha256Hex(title + userEmail + new Date(System.currentTimeMillis()));
    }

/* public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }*/

   /* public generateKey(){

    }*/

    public String getKey() {
        return this.key;
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
