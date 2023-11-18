package com.example.novigradservice.Model;

public class HealthCard {
    public  String firstName,lastName,address,dob,docImg,userId,statusImg;

    public HealthCard(String firstName, String lastName, String address, String dob,String docImg
            ,String statusImg,String userId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.dob = dob;
        this.statusImg=statusImg;
        this.userId = userId;
        this.docImg=docImg;
    }

    public String getStatusImg() {
        return statusImg;
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
