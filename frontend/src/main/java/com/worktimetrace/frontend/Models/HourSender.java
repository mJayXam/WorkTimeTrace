package com.worktimetrace.frontend.Models;

public class HourSender {
    private Long id;

    private Double hourcount;

    private String date;

    private Long userid;

    public HourSender() {
    }

    public HourSender(Double hourcount, String date, String userid){
        this.hourcount = hourcount;
        this.date = date;
        this.userid = Long.parseLong(userid);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getHourcount() {
        return hourcount;
    }

    public void setHourcount(Double hourcount) {
        this.hourcount = hourcount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public void setUserid(String userid) {
        this.userid = Long.parseLong(userid);
    }

    @Override
    public String toString() {
        return "HourSender [hourcount=" + hourcount + ", date=" + date + ", userid=" + userid + ", id=" + id + "]";
    }
}
