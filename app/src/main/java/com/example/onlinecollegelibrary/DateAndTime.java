package com.example.onlinecollegelibrary;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

class DateAndTime{
    int date,month,year;
    public DateAndTime(/*int date,int month,int year*/){
        Calendar calendar = Calendar.getInstance();
        this.date = calendar.get(Calendar.DATE);
        this.month = calendar.get(Calendar.MONTH);
        this.year = calendar.get(Calendar.YEAR);
    }

    public DateAndTime(Calendar calendar){
        this.date = calendar.get(Calendar.DATE);
        this.month = calendar.get(Calendar.MONTH);
        this.year = calendar.get(Calendar.YEAR);
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getStringMonth(int num){
        String monthStr = null;

        switch (num){
            case 0:
                monthStr = "Jan";
                break;
            case 1:
                monthStr = "Feb";
            break;
            case 2:
                monthStr = "Mar";
            break;
            case 3:
                monthStr = "April";
            break;
            case 4:
                monthStr = "May";
            break;
            case 5:
                monthStr = "Jun";
            break;
            case 6:
                monthStr = "Jul";
            break;
            case 7:
                monthStr = "Aug";
            break;
            case 8:
                monthStr = "Sep";
            break;
            case 9:
                monthStr = "Oct";
            break;
            case 10:
                monthStr = "Nov";
            break;
            case 11:
                monthStr = "Dec";
            break;
        }

        return monthStr;
    }

    public void setFullDate(int date,int month,int year){
        this.date = date;
        this.month = month;
        this.year = year;
    }
}

/*
public class DateAndTime {
    int Date,Year;
    int Month;
//    String Month;
    String dateString;
    String time;
    String fullTime;
    public DateAndTime(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM");

        SimpleDateFormat stringDate = new SimpleDateFormat("dd MMMM yyyy");

        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");

        Date = calendar.get(Calendar.DATE);
        Month = calendar.get(Calendar.MONTH);
//        Month = dateFormat.format(calendar.get(Calendar.MONTH));
        Year = calendar.get(Calendar.YEAR);
//        numMonth = calendar.get(Calendar.MONTH);

        dateString = stringDate.format(calendar.getTime());
        time = timeFormat.format(calendar.getTime());

        fullTime = Calendar.getInstance().getTime().toString();
    }

    public int getDate() {
        return Date;
    }

    public int getMonth() {
        return Month;
    }

    */
/*public int getNumMonth(){
        return numMonth;
    }*//*


    public int getYear() {
        return Year;
    }

    public void setDate(int date) {
        Date = date;
    }

    public void setYear(int year) {
        Year = year;
    }

    public String getDateString() {
        return dateString;
    }

    public String getTime(){
        return time;
    }

    public String getFullTime(){
        return fullTime;
    }

    public void setMonth(int month) {
        Month = month;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setFullTime(String fullTime) {
        this.fullTime = fullTime;
    }
}
*/
