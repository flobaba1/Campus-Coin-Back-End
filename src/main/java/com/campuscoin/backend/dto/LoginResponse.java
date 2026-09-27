package com.campuscoin.backend.dto;

import com.campuscoin.backend.entity.Admin;
import com.campuscoin.backend.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {
    private String token;
    private User user;
    private Admin admin;

    public LoginResponse(String token, User user){
        this.token = token;
        this.user = user;
    }

    public LoginResponse(String token, Admin admin){
        this.token = token;
        this.admin = admin;
    }

    public String getToken(){
        return token;
    }

    public User getUser(){
        return user;
    }

    public Admin getAdmin(){
        return admin;
    }
}
