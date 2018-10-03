package com.wildnet.firebasechatapplication.model;

import java.io.Serializable;

public class GroupUserModel implements Serializable {

    private String email, name, userImageUrl;
    private String adminTxt;

    public GroupUserModel() {
    }

    public GroupUserModel(String email, String adminTxt, String name, String userImageUrl) {
        this.email = email;
        this.adminTxt = adminTxt;
        this.name = name;
        this.userImageUrl = userImageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public String getAdminTxt() {
        return adminTxt;
    }

    public void setAdminTxt(String adminTxt) {
        this.adminTxt = adminTxt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "GroupUserModel{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", userImageUrl='" + userImageUrl + '\'' +
                ", adminTxt='" + adminTxt + '\'' +
                '}';
    }
}
