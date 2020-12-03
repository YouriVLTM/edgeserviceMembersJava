package com.example.membersedgeservice.model;

import org.apache.commons.codec.digest.DigestUtils;
import java.util.Date;

public class Image {
    private String id;
    private String source;
    private String userEmail;
    private String description;
    private String key;

    public Image(){

    }

    public Image(String source, String userEmail, String description) {
        this.source = source;
        this.description = description;
        this.userEmail = userEmail;
        this.key = DigestUtils.sha256Hex(source + userEmail + new Date(System.currentTimeMillis()));
    }


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
