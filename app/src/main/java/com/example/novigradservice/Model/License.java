package com.example.novigradservice.Model;

public class License {
    public  String firstName,lastName,address,dob,docImg,userId,type;

    public License(String firstName, String lastName, String address, String dob,String docImg ,String userId
    ,String type) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.dob = dob;
        this.type=type;
        this.userId = userId;
        this.docImg=docImg;
    }

    public String getType() {
        return type;
    }

    public String getDocImg() {
        return docImg;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getDob() {
        return dob;
    }

    public String getUserId() {
        return userId;
    }
}
