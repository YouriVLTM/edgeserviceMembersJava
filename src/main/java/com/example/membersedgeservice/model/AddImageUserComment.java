package com.example.membersedgeservice.model;

public class AddImageUserComment {
    private String userEmail;
    private String imageKey;
    private String title;
    private String description;

    public AddImageUserComment() {
    }

    public AddImageUserComment(String userEmail, String imageKey, String title, String description) {
        this.userEmail = userEmail;
        this.imageKey = imageKey;
        this.title = title;
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
}
