package com.wildnet.firebasechatapplication.model;

import java.io.Serializable;

public class GroupNameModel implements Serializable {
    private String groupName,iamgeUrl;

    public GroupNameModel(String groupName, String iamgeUrl) {
        this.groupName = groupName;
        this.iamgeUrl = iamgeUrl;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getIamgeUrl() {
        return iamgeUrl;
    }

    public void setIamgeUrl(String iamgeUrl) {
        this.iamgeUrl = iamgeUrl;
    }

    @Override
    public String toString() {
        return "GroupNameModel{" +
                "groupName='" + groupName + '\'' +
                ", iamgeUrl='" + iamgeUrl + '\'' +
                '}';
    }
}
