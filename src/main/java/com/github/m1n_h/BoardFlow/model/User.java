package com.github.m1n_h.BoardFlow.model;

public class User {
    private String userId;
    private String userPw;
    private String userName;
    private String userEmail;

    public User(String userId, String userPw, String userName, String userEmail) {
        this.userId = userId;
        this.userPw = userPw;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    public String getUserId() { return userId; }
    public String getUserPw() { return userPw; }
    public String getUserName() { return userName; }
    public String getUserEmail() { return userEmail; }

    @Override
    public String toString() {
        return "User{" +
                "id='" + userId + '\'' +
                ", name='" + userName + '\'' +
                ", email='" + userEmail + '\'' +
                '}';
    }
}
