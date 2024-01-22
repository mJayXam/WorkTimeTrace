package com.worktimetrace.DataTypes;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Hours {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("hourcount")
    private Double hourcount;

    @JsonProperty("date")
    private java.sql.Date hourdate;

    @JsonProperty("userid")
    private Long userid;


    public Hours() {
    }

    public Hours(Double stundenanzahl, java.sql.Date datum, Long nutzerid) {
        this.hourcount = stundenanzahl;
        this.hourdate = datum;
        this.userid = nutzerid;
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

    public void setHourcount(Double stundenanzahl) {
        this.hourcount = stundenanzahl;
    }

    public java.sql.Date getHourdate() {
        return hourdate;
    }

    public void setHourdate(java.sql.Date datum) {
        this.hourdate = datum;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long nutzerid) {
        this.userid = nutzerid;
    }
}