package com.example.test;

public class AppUsersData {

    String PatientEmail, Location, Dentist, AppointmentType, Day, Month, Year, Hour, Mins;

    public String getPatientEmail() {return PatientEmail;}

    public String getAppointmentType(){
        return AppointmentType;
    }

    public String getDentist() {return Dentist;}

    public String getLocation() {return Location;}

    public String getDay() {return Day;}

    public String getMonth() {return Month;}

    public String getYear() {return Year;}

    public String getHour() {return Hour;}

    public String getMins() {return Mins;}

    public void setDentist(String Dentist) { this.Dentist = Dentist; }

}
