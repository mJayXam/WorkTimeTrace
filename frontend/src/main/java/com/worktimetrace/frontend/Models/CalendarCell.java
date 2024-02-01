package com.worktimetrace.frontend.Models;

import java.time.LocalDate;

public class CalendarCell {
    private LocalDate date;
    private boolean color;
    private Double hourCount;

    public CalendarCell(LocalDate date, boolean color) {
        this.date = date;
        this.color = color;
    }

    public CalendarCell(LocalDate date, boolean color, Double hourCount) {
        this.date = date;
        this.color = color;
        this.hourCount = hourCount;
    }

    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getDay() {
        return date.getDayOfMonth();
    }

    public int getMonth() {
        return date.getMonthValue();
    }

    public int getYear() {
        return date.getYear();
    }

    public boolean getColor() {
        return color;
    }

    public void setColor(boolean color) {
        this.color = color;
    }

    public Double getHourCount() {
        return hourCount;
    }

    public void setHourCount(Double hourCount) {
        this.hourCount = hourCount;
    }

    @Override
    public String toString() {
        return "CalendarCell [date=" + date + ", color=" + color + ", hourCount=" + hourCount + "]";
    }

}
