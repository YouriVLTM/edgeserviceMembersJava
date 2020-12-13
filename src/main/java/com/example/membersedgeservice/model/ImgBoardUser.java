package com.example.membersedgeservice.model;


public class ImgBoardUser {

    private int id;
    private String firstname;
    private String lastname;
    private String email;

    private String password;

    public ImgBoardUser(){

    }

    public ImgBoardUser(String firstname,String lastname,String email, String password) {
        setFirstname(firstname);
        setLastname(lastname);
        setEmail(email);
        setPassword(password);
    }


    public void setEmail(String email) {
        this.email=email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getFirstname() {
        return firstname;
    }
    public String getLastname() {
        return lastname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
