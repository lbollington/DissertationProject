package com.example.test;

public class AppUsersData {

    String PatientName, PatientEmail, Location, Dentist, AppointmentType, AppointmentTime, AppointmentDate, Day, Month, Year, Hour, Mins;

    public AppUsersData(){}

    public AppUsersData(String PatientName, String PatientEmail, String Location, String Dentist, String AppointmentType, String AppointmentDate, String AppointmentTime, String Day, String Month, String Year, String Hour, String Mins) {
        this.PatientName = PatientName;
        this.AppointmentType = AppointmentType;
        this.PatientEmail = PatientEmail;
        this.Dentist = Dentist;
        this.Location = Location;
        this.AppointmentTime = AppointmentTime;
        this.AppointmentDate = AppointmentDate;
        this.Day = Day;
        this.Month = Month;
        this.Year = Year;
        this.Hour = Hour;
        this.Mins = Mins;

    }

    public String getPatientname() {
        return PatientName;
    }

    public String getPatientEmail() {return PatientEmail;}

    public String getAppointmentType(){
        return AppointmentType;
    }

    public String getDentist() {return Dentist;}
                                                                                                    //getters and setters for appointment adapter
    public String getLocation() {return Location;}

    public String getAppointmentTime() {return AppointmentTime; }

    public String getAppointmentDate() {return AppointmentDate; }

    public String getDay() {return Day;}

    public String getMonth() {return Month;}

    public String getYear() {return Year;}

    public String getHour() {return Hour;}

    public String getMins() {return Mins;}

    public void setPatientname(String PatientName) { this.PatientName = PatientName; }

    public void setPatientEmail(String PatientEmail) { this.PatientEmail = PatientEmail;}

    public void setAppointmentType(String AppointmentType){ this.AppointmentType = AppointmentType;}

    public void setDentist(String Dentist) { this.Dentist = Dentist; }

    public void setLocation(String Location){ this.Location = Location;}

    public void setAppointmentTime(String AppointmentTime){this.AppointmentTime = AppointmentTime;}

    public void setAppointmentDate(String AppointmentDate){this.AppointmentDate = AppointmentDate;}

    public void setDay(String Day){this.Day = Day;}

    public void setMonth(String Month){this.Month = Month;}

    public void setYear(String Year){this.Year = Year;}

    public void setHour(String Hour){this.Hour = Hour;}

    public void setMins(String Mins){this.Mins = Mins;}

}
