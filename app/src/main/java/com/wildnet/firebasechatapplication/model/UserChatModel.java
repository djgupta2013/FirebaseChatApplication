package com.wildnet.firebasechatapplication.model;

public class UserChatModel {
    private String chat;
    private String currentTime;
    private String Email;

    public UserChatModel(String chat, String currentTime, String email) {
        this.chat = chat;
        this.currentTime = currentTime;
        Email = email;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    @Override
    public String toString() {
        return "UserChatModel{" +
                "chat='" + chat + '\'' +
                ", currentTime='" + currentTime + '\'' +
                ", Email='" + Email + '\'' +
                '}';
    }
}
