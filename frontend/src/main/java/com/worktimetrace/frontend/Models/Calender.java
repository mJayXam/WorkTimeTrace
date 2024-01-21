package com.worktimetrace.frontend.Models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class Calender {
    private int day;
    private int month;
    private int year;
    private String monthYear;

    public Calender(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public Calender(LocalDate date) {
        this.day = date.getDayOfMonth();
        this.month = date.getMonthValue();
        this.year = date.getYear();
        this.monthYear = getMonthYearFromDate(date);
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
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

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    @Override
    public String toString() {
        return day + "." + month + "." + year;
    }

    private String getMonthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM - yyyy", Locale.GERMAN);
        return date.format(formatter);
    }

    public static ArrayList<LocalDate> daysInMonthArray(LocalDate date) {
        ArrayList<LocalDate> daysInMonthArray = new ArrayList<>();
        int viewSpace = 0;

        LocalDate previousMonth = date.minusMonths(1).withDayOfMonth(1);
        LocalDate endOfPreviousMonth = previousMonth.plusMonths(1).minusDays(1);

        LocalDate dayOfMonth = date.withDayOfMonth(1);

        int dayOfWeek = dayOfMonth.getDayOfWeek().getValue();
        for(int i = 1; i < dayOfWeek; i++)
        {
            daysInMonthArray.add(endOfPreviousMonth.minusDays(dayOfWeek - i - 1));
            viewSpace += 1;
        }

        while(viewSpace < 42)
        {
            daysInMonthArray.add(dayOfMonth);
            dayOfMonth = dayOfMonth.plusDays(1);
            viewSpace += 1;
        }
        return  daysInMonthArray;
    }

    public static Calender[][] fillWeeksInMonthArray(LocalDate date) {
        Calender[][] ret = new Calender[6][7];
        int pos = 0;
        ArrayList<LocalDate> daysInMonthArray = daysInMonthArray(date);
        for(LocalDate datum : daysInMonthArray){
            if(pos < 7){
                ret[0][pos] = new Calender(datum);
            }
            if(pos >= 7 && pos < 14) {
                ret[1][pos - 7] = new Calender(datum);
            }
            if(pos >= 14 && pos < 21) {
                ret[2][pos - 14] = new Calender(datum);
            }
            if(pos >= 21 && pos < 28) {
                ret[3][pos - 21] = new Calender(datum);
            }
            if(pos >= 28 && pos < 35) {
                ret[4][pos - 28] = new Calender(datum);
            }
            if(pos >= 35 && pos < 42) {
                ret[5][pos - 35] = new Calender(datum);
            }
            pos++;
        }
        return ret;
    }
}
