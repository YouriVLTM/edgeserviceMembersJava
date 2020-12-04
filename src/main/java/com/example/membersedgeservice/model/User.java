package com.example.membersedgeservice.model;

public class User {
    private Integer id;
    private String userEmail;

    public User() {
    }

    public User(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
