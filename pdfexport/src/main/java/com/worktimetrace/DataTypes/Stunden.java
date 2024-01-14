package com.worktimetrace.DataTypes;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Stunden {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("stundenanzahl")
    private Double stundenanzahl;

    @JsonProperty("datum")
    private java.sql.Date datum;

    @JsonProperty("nutzerid")
    private Long nutzerid;


    public Stunden() {
    }

    public Stunden(Double stundenanzahl, java.sql.Date datum, Long nutzerid) {
        this.stundenanzahl = stundenanzahl;
        this.datum = datum;
        this.nutzerid = nutzerid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getStundenanzahl() {
        return stundenanzahl;
    }

    public void setStundenanzahl(Double stundenanzahl) {
        this.stundenanzahl = stundenanzahl;
    }

    public java.sql.Date getDatum() {
        return datum;
    }

    public void setDatum(java.sql.Date datum) {
        this.datum = datum;
    }

    public Long getNutzerid() {
        return nutzerid;
    }

    public void setNutzerid(Long nutzerid) {
        this.nutzerid = nutzerid;
    }
}