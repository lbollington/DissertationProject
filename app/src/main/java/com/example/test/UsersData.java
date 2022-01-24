package com.example.test;

public class UsersData {

    String FullName, UserEmail;

    public UsersData(){}

    public UsersData(String FullName, String UserEmail) {
        this.FullName = FullName;
        this.UserEmail = UserEmail;

    }

    public String getFullname() {
        return FullName;
    }

    public String getUserEmail(){
        return UserEmail;
    }

    public void setFullname(String FullName) {
        this.FullName = FullName;
    }

    public void setUserEmail(String UserEmail){
        this.UserEmail = UserEmail;
    }

}
