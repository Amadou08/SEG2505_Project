package com.example.novigradservice.Model;

public class User {
    String userName,address,phoneNumber,userEmail,userId;

    public User(String userName, String address, String phoneNumber, String userEmail, String userId) {
        this.userName = userName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.userEmail = userEmail;
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserId() {
        return userId;
    }
}
