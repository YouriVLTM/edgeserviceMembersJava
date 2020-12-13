package com.example.membersedgeservice.model;

public class Login {

    private String email;
    private String password;

    public String getEmail(){
        return email;
    }
    public String setEmail(String email){
        return this.email=email;
    }

    public String getPassword(){
        return password;
    }
    public String setPassword(String password){
        return this.password=password;
    }

    public Login(){

    }

    public Login(String email,String password) {
        setEmail(email);
        setPassword(password);
    }
}
