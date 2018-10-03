package com.wildnet.firebasechatapplication.model;

public class GroupChatModel {
    private String chat;
    private String currentTime;
    private String Email,name;

    public GroupChatModel(String chat, String currentTime, String email,String name) {
        this.chat = chat;
        this.currentTime = currentTime;
        Email = email;
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    @Override
    public String toString() {
        return "GroupChatModel{" +
                "chat='" + chat + '\'' +
                ", currentTime='" + currentTime + '\'' +
                ", Email='" + Email + '\'' +
                '}';
    }
}
