package com.worktimetrace.frontend.Models;

public class HourDate {
    private String date;
    private double hour;

    public HourDate() {
    }

    public HourDate(String date, double hour) {
        this.date = date;
        this.hour = hour;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getHour() {
        return hour;
    }

    public void setHour(double hour) {
        this.hour = hour;
    }

    @Override
    public String toString() {
        return "Hour [date=" + date + ", hours=" + hour + "]";
    }

}
