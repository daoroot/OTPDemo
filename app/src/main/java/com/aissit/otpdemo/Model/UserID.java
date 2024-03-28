package com.aissit.otpdemo.Model;

public class UserID {

    private String userId;
    private String userSubId;

    public UserID(String userId, String userSubId) {
        this.userId = userId;
        this.userSubId = userSubId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserSubId() {
        return userSubId;
    }

    public void setUserSubId(String userSubId) {
        this.userSubId = userSubId;
    }


}
