package com.worktimetrace.frontend.Models;

import java.time.LocalDate;
import java.util.ArrayList;

public class Datum {
    public int day;
    public int month;
    public int year;

    public Datum(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public Datum(LocalDate date) {
        this.day = date.getDayOfMonth();
        this.month = date.getMonthValue();
        this.year = date.getYear();
    }

    @Override
    public String toString() {
        return day + "." + month + "." + year;
    }

    public static ArrayList<Datum> daysInMonthArray(LocalDate date) {
        ArrayList<Datum> daysInMonthArray = new ArrayList<>();
        int viewSpace = 0;

        LocalDate previousMonth = date.minusMonths(1).withDayOfMonth(1);
        LocalDate endOfPreviousMonth = previousMonth.plusMonths(1).minusDays(1);

        LocalDate dayOfMonth = date.withDayOfMonth(1);

        int dayOfWeek = dayOfMonth.getDayOfWeek().getValue();
        for(int i = 1; i < dayOfWeek; i++)
        {
            LocalDate datum = endOfPreviousMonth.minusDays(dayOfWeek - i - 1);
            daysInMonthArray.add(new Datum(datum.getDayOfMonth(), datum.getMonthValue(), datum.getYear()));
            viewSpace += 1;
        }

        while(viewSpace < 35)
        {
            daysInMonthArray.add(new Datum(dayOfMonth.getDayOfMonth(), dayOfMonth.getMonthValue(), dayOfMonth.getYear()));
            dayOfMonth = dayOfMonth.plusDays(1);
            viewSpace += 1;
        }
        return  daysInMonthArray;
    }

    public static ArrayList<Datum> getFirstWeekOfGivenMonth(ArrayList <Datum> daysInMonth) {
        ArrayList<Datum> ret = new ArrayList<>();
        int pos = 0;
        for(Datum date : daysInMonth) {
            pos++;
            ret.add(date);
            if (pos >= 7) {
                break;
            }
        }
        return ret;
    }

    public static ArrayList<Datum> getSeccondWeekOfGivenMonth(ArrayList <Datum> daysInMonth) {
        ArrayList<Datum> ret = new ArrayList<>();
        int pos = 0;
        for(Datum date : daysInMonth) {
            if (pos >= 7) {
                ret.add(date);
            }
            pos++;
            if (pos >= 14) {
                break;
            }
        }
        return ret;
    }

    public static ArrayList<Datum> getThirdWeekOfGivenMonth(ArrayList <Datum> daysInMonth) {
        ArrayList<Datum> ret = new ArrayList<>();
        int pos = 0;
        for(Datum date : daysInMonth) {
            if (pos >= 14) {
                ret.add(date);
            }
            pos++;
            if (pos >= 21) {
                break;
            }
        }
        return ret;
    }

    public static ArrayList<Datum> getFourthWeekOfGivenMonth(ArrayList <Datum> daysInMonth) {
        ArrayList<Datum> ret = new ArrayList<>();
        int pos = 0;
        for(Datum date : daysInMonth) {
            if (pos >= 21) {
                ret.add(date);
            }
            pos++;
            if (pos >= 28) {
                break;
            }
        }
        return ret;
    }

    public static ArrayList<Datum> getFifthWeekOfGivenMonth(ArrayList <Datum> daysInMonth) {
        ArrayList<Datum> ret = new ArrayList<>();
        int pos = 0;
        for(Datum date : daysInMonth) {
            if (pos >= 28) {
                ret.add(date);
            }
            pos++;
            if (pos >= 35) {
                break;
            }
        }
        return ret;
    }
}
