package com.friends.mygpndapp;

public class StudentInfo {
    String firstname,lastname,email,password,gender;

    public StudentInfo(){

    }

    public StudentInfo(String firstname, String lastname, String email, String password, String gender) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.gender = gender;
    }
}
