package com.wildnet.firebasechatapplication.model;

public class UserActiveModel {
    private String activeCheck;

    public UserActiveModel(String activeCheck) {
        this.activeCheck = activeCheck;
    }

    public UserActiveModel() {
    }

    public String getActiveCheck() {
        return activeCheck;
    }

    public void setActiveCheck(String activeCheck) {
        this.activeCheck = activeCheck;
    }

    @Override
    public String toString() {
        return "UserActiveModel{" +
                "activeCheck='" + activeCheck + '\'' +
                '}';
    }
}
