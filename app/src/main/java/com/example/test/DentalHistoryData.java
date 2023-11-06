package com.example.test;

public class DentalHistoryData {
    String AppName, DentistName, PatientName, Day, Month, Year, imageurl;

    public DentalHistoryData(){}

    public DentalHistoryData(String AppName, String DentistName, String Day, String Month, String Year, String PatientName, String imageurl) {
        this.AppName = AppName;
        this.DentistName = DentistName;
        this.PatientName = PatientName;
        this.Day = Day;
        this.Month = Month;
        this.Year = Year;
        this.imageurl = imageurl;
    }

    public String getAppName() {return AppName;}

    public String getDentistName() {return DentistName;}

    public String getDay() {return Day;}

    public String getMonth() {return Month;}        //getters and setters for dental history adapter

    public String getYear() {return Year;}

    public String getImageurl() {return imageurl;}

    public String getPatientName() {return PatientName;}

    public void setAppName(String AppName) {this.AppName = AppName;}

    public void setDay(String Day) {this.Day = Day;}

    public void setMonth(String Month) { this.Month = Month;}

    public void setYear(String Year) { this.Year = Year;}

    public void setImageurl(String imageurl) {this.imageurl = imageurl;}

    public void setPatientName(String PatientName) {this.PatientName = PatientName;}

    public void setDentistName(String DentistName) {this.DentistName = DentistName;}
}
