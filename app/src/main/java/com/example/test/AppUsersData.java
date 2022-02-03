package com.example.test;

public class AppUsersData {

    String PatientName, PatientEmail, AppointmentType;

    public AppUsersData(){}

    public AppUsersData(String PatientName, String PatientEmail, String AppointmentType) {
        this.PatientName = PatientName;
        this.AppointmentType = AppointmentType;
        this.PatientEmail = PatientEmail;

    }

    public String getPatientname() {
        return PatientName;
    }

    public String getPatientEmail() {return PatientEmail;}

    public String getAppointmentType(){
        return AppointmentType;
    }

    public void setPatientname(String PatientName) {
        this.PatientName = PatientName;
    }

    public void setPatientEmail(String PatientEmail) { this.PatientEmail = PatientEmail;}

    public void setAppointmentType(String AppointmentType){ this.AppointmentType = AppointmentType;
    }

}