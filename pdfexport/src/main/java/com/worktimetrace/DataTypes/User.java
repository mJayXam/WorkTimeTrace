package com.worktimetrace.DataTypes;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    @JsonProperty("firstname")
    private String firstname;
    @JsonProperty("lastname")
    private String lastname;
    @JsonProperty("username")
    private String username;
    @JsonProperty("street")
    private String street;
    @JsonProperty("housenumber")
    private int housenumber;
    @JsonProperty("zipcode")
    private String zipcode;
    @JsonProperty("city")
    private String city;

    public User() {
    }

    public User(String firstname, String lastname, String username, String street, int housenumber, String zipcode,
            String city) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.street = street;
        this.housenumber = housenumber;
        this.zipcode = zipcode;
        this.city = city;
    }

    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }
    public int getHousenumber() {
        return housenumber;
    }
    public void setHousenumber(int housenumber) {
        this.housenumber = housenumber;
    }
    public String getZipcode() {
        return zipcode;
    }
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    
}
