package com.example.test;

public class PassRequestData {

    String ResetName, UserEmail, PassRequest;

    public PassRequestData(){}

    public PassRequestData(String ResetName, String UserEmail, String PassRequest) {
        this.ResetName = ResetName;
        this.UserEmail = UserEmail;
        this.PassRequest = PassRequest;
    }

    public String getResetName() {
        return ResetName;
    }

    public String getUserEmail() {return UserEmail;}

    public String getPassRequest(){
        return PassRequest;
    }

    public void setResetName(String ResetName) {
        this.ResetName = ResetName;
    }

    public void setUserEmail(String UserEmail) { this.UserEmail = UserEmail;}

    public void setPassRequest(String PassRequest){ this.PassRequest = PassRequest;}

}
