package com.link.cloud.bean;

/**
 * Created by 49488 on 2018/10/6.
 */

public class Days {
    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getLauarYear() {
        return lauarYear;
    }

    public void setLauarYear(String lauarYear) {
        this.lauarYear = lauarYear;
    }

    public String getLauarMonth() {
        return lauarMonth;
    }

    public void setLauarMonth(String lauarMonth) {
        this.lauarMonth = lauarMonth;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    @Override
    public String toString() {
        return year+"年"+month+"月"+day+"日"+ "    ";

    }

    public String Lauar() {
        return "    "+lauarMonth+"月"+lauar;

    }

    public String getLauar() {
        return lauar;
    }

    public void setLauar(String lauar) {
        this.lauar = lauar;
    }

    public String day;
    public String week;
    public String lauar;
    public String lauarYear;
    public String lauarMonth;
    public String year;
    public String month;

}
