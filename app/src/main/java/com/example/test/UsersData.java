package com.example.test;

public class UsersData {

    String FullName, UserEmail, UserType;

    public UsersData(){}

    public UsersData(String FullName, String UserEmail, String UserType) {
        this.FullName = FullName;
        this.UserType = UserType;
        this.UserEmail = UserEmail;

    }

    public String getFullname() {
        return FullName;
    }

    public String getUserType(){
        return UserType;
    }

    public String getUserEmail(){return UserEmail;}

    public void setFullname(String FullName) {
        this.FullName = FullName;
    }

    public void setUserType(String UserType){
        this.UserType = UserType;
    }

    public void setUserEmail(String UserEmail) { this.UserEmail = UserEmail; }
}
