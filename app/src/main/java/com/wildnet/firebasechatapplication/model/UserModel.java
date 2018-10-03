package com.wildnet.firebasechatapplication.model;

import java.io.Serializable;

public class UserModel implements Serializable {
    private String name, email,url,userActiveCheck;
    boolean isSelected=false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public UserModel(String email, String name) {
        this.name = name;
        this.email = email;
    }

    public UserModel(String email, String name, String url) {
        this.name = name;
        this.email = email;
        this.url = url;
    }

    public UserModel(String name, String email, String url, String userActiveCheck) {
        this.name = name;
        this.email = email;
        this.url = url;
        this.userActiveCheck = userActiveCheck;
    }

    public String getUserActiveCheck() {
        return userActiveCheck;
    }

    public void setUserActiveCheck(String userActiveCheck) {
        this.userActiveCheck = userActiveCheck;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", url='" + url + '\'' +
                ", userActiveCheck='" + userActiveCheck + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }
}
