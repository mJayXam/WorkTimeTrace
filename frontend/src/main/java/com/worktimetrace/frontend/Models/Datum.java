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

        while(viewSpace < 35)
        {
            daysInMonthArray.add(dayOfMonth);
            dayOfMonth = dayOfMonth.plusDays(1);
            viewSpace += 1;
        }
        return  daysInMonthArray;
    }

    public static Datum[][] fillWeeksInMonthArray(LocalDate date) {
        Datum[][] ret = new Datum[5][7];
        int pos = 0;
        ArrayList<LocalDate> daysInMonthArray = daysInMonthArray(date);
        for(LocalDate datum : daysInMonthArray){
            if(pos < 7){
                ret[0][pos] = new Datum(datum);
            }
            if(pos >= 7 && pos < 14) {
                ret[1][pos - 7] = new Datum(datum);
            }
            if(pos >= 14 && pos < 21) {
                ret[2][pos - 14] = new Datum(datum);
            }
            if(pos >= 21 && pos < 28) {
                ret[3][pos - 21] = new Datum(datum);
            }
            if(pos >= 28 && pos < 35) {
                ret[4][pos - 28] = new Datum(datum);
            }
            pos++;
        }
        return ret;
    }

}
