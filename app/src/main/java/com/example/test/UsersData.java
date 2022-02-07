package com.example.test;

public class UsersData {

    String FullName, UserEmail, Password, PhoneNumber, UserType;

    public UsersData(){}

    public UsersData(String FullName, String UserEmail, String Password, String PhoneNumber, String UserType) {
        this.FullName = FullName;
        this.Password = Password;
        this.PhoneNumber = PhoneNumber;
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

    public String getPassword(){return Password;}

    public String getPhoneNumber(){return PhoneNumber;}

    public void setFullname(String FullName) {
        this.FullName = FullName;
    }

    public void setUserType(String UserType){
        this.UserType = UserType;
    }

    public void setUserEmail(String UserEmail) { this.UserEmail = UserEmail; }

    public void setPassword(String Password) { this.Password = Password; }

    public void setPhoneNumber(String PhoneNumber) { this.PhoneNumber = PhoneNumber; }


}
