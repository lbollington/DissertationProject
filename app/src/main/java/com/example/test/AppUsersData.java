package com.example.test;

public class AppUsersData {

    String PatientName, PatientEmail, Location, Dentist, AppointmentType, AppointmentTime;

    public AppUsersData(){}

    public AppUsersData(String PatientName, String PatientEmail, String Location, String Dentist, String AppointmentType, String AppointmentTime) {
        this.PatientName = PatientName;
        this.AppointmentType = AppointmentType;
        this.PatientEmail = PatientEmail;
        this.Dentist = Dentist;
        this.Location = Location;
        this.AppointmentTime = AppointmentTime;

    }

    public String getPatientname() {
        return PatientName;
    }

    public String getPatientEmail() {return PatientEmail;}

    public String getAppointmentType(){
        return AppointmentType;
    }

    public String getDentist() {return Dentist;}

    public String getLocation() {return Location;}

    public String getAppointmentTime() {return AppointmentTime; }

    public void setPatientname(String PatientName) {
        this.PatientName = PatientName;
    }

    public void setPatientEmail(String PatientEmail) { this.PatientEmail = PatientEmail;}

    public void setAppointmentType(String AppointmentType){ this.AppointmentType = AppointmentType;}

    public void setDentist(String Dentist) { this.Dentist = Dentist; }

    public void setLocation(String Location){ this.Location = Location;}

    public void setAppointmentTime(String AppointmentTime){this.AppointmentTime = AppointmentTime;}

}
