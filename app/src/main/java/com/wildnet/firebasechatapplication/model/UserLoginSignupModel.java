package com.wildnet.firebasechatapplication.model;

public class UserLoginSignupModel {
    private String name, email, password;
    private String userImageUrl, activeCheck;

    public UserLoginSignupModel(String name, String email, String password, String userImageUrl, String activeCheck) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userImageUrl = userImageUrl;
        this.activeCheck = activeCheck;
    }

    public UserLoginSignupModel(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserLoginSignupModel{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", userImageUrl='" + userImageUrl + '\'' +
                ", activeCheck='" + activeCheck + '\'' +
                '}';
    }
}
