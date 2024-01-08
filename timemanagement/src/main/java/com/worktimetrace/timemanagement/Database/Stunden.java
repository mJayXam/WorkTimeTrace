package com.worktimetrace.timemanagement.Database;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;


@Entity
@Table(name = "stunden", schema = "public")
public class Stunden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "stundenanzahl", nullable = false)
    private Double stundenanzahl;

    @Column(name = "datum", nullable = false)
    private java.sql.Date datum;

    @Column(name = "nutzerid")
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