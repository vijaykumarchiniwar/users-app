package com.learn.spring.users.models;

public class AddressRequestModel {

    private String street;
    private String city;
    private String pinCode;
    private String type;
    //private UserRequestModel userDetails;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

/*    public UserRequestModel getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserRequestModel userDetails) {
        this.userDetails = userDetails;
    }*/
}
