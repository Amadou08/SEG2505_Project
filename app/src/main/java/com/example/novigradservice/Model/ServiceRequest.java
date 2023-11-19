package com.example.novigradservice.Model;

public class ServiceRequest {
   public String serviceName,userName,addressDoc,serviceRequestId,serviceStatus,userId,address;

    public ServiceRequest(String serviceName, String userName,String address, String addressDoc
            , String serviceStatus,String serviceRequestId,String userId) {
        this.serviceName = serviceName;
        this.userName = userName;
        this.address=address;
        this.addressDoc = addressDoc;
        this.serviceRequestId = serviceRequestId;
        this.serviceStatus = serviceStatus;
        this.userId=userId;
    }

    public String getAddress() {
        return address;
    }

    public String getUserId() {
        return userId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getUserName() {
        return userName;
    }

    public String getAddressDoc() {
        return addressDoc;
    }

    public String getServiceRequestId() {
        return serviceRequestId;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }
}
