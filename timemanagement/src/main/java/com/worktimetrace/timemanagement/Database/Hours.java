package com.worktimetrace.timemanagement.Database;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "hours", schema = "public")
public class Hours {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "hourcount", nullable = false)
    private Double hourcount;

    @Column(name = "hourdate", nullable = false)
    private java.sql.Date date;

    @Column(name = "userid")
    private Long userid;


    public Hours() {
    }

    public Hours(Double stundenanzahl, java.sql.Date datum, Long nutzerid) {
        this.hourcount = stundenanzahl;
        this.date = datum;
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

    public java.sql.Date getDate() {
        return date;
    }

    public void setDate(java.sql.Date datum) {
        this.date = datum;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long nutzerid) {
        this.userid = nutzerid;
    }
}