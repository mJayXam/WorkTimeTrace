package com.worktimetrace.frontend.Models;

public class User {

    private String firstname;
    private String lastname;
    private String username;
    private String street;
    private String housenumber;
    private String zipcode;
    private String city;
    private String password;
    
    public User() {
    }

    public User(String firstname, String lastname, String username, String street, String housenumber,
            String zipcode, String city, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.street = street;
        this.housenumber = housenumber;
        this.zipcode = zipcode;
        this.city = city;
        this.password = password;
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
    public String getHousenumber() {
        return housenumber;
    }
    public void setHousenumber(String housenumber) {
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
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User [firstname=" + firstname + ", lastname=" + lastname + ", username=" + username + ", street="
                + street + ", housenumber=" + housenumber + ", zipcode=" + zipcode + ", city=" + city + ", password="
                + password + "]";
    }
}
