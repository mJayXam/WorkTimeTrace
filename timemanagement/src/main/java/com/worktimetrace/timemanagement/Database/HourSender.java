package com.worktimetrace.timemanagement.Database;

import java.sql.Date;

public class HourSender {
    private Double hourcount;

    private java.sql.Date date;

    private Long userid;

    public HourSender() {
    }

    public HourSender(Double hourcount, Date date, Long userid) {
        this.hourcount = hourcount;
        this.date = date;
        this.userid = userid;
    }

    public Double getHourcount() {
        return hourcount;
    }

    public void setHourcount(Double hourcount) {
        this.hourcount = hourcount;
    }

    public java.sql.Date getDate() {
        return date;
    }

    public void setDate(java.sql.Date date) {
        this.date = date;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    
}
