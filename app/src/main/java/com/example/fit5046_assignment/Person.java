package com.example.fit5046_assignment;

import android.icu.text.SimpleDateFormat;
import com.example.fit5046_assignment.Credential;

import java.util.Date;
import java.util.Locale;

public class Person {
    private Integer personId;
    private String fname;
    private String surname;
    private String gender;
    private String dob;
    private String address;
    private String postcode;
    private String state;
    private Credential credentialId;


    public Person(){

    }

    public Person(Integer personId, String fname, String surname, String gender, String dob, String address, String state, String postcode) {
        this.personId = personId;
        this.fname = fname;
        this.surname = surname;
        this.gender = gender;
        this.dob = dob;
        this.address = address;
        this.state = state;
        this.postcode = postcode;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public void setCredentialId(Credential credentialId) {
        this.credentialId = credentialId;
    }

    public Credential getCredentialId() {
        return credentialId;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }
}
