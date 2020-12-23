package com.example.membersedgeservice.model;

public class Like {
    private int id;
    private String userId;
    private String imageId;
    private Boolean state;

    public Like(){}

    public Like(String userId, String imageId, Boolean state) {
        this.userId = userId;
        this.imageId = imageId;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId(){
        return userId;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getImageId(){
        return imageId;
    }

    public void setImageId(String imageId){
        this.imageId = imageId;
    }

    public Boolean getState(){
        return state;
    }

    public void setState(Boolean state){
        this.state = state;
    }
}
