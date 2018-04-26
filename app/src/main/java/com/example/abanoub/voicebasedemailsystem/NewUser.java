package com.example.abanoub.voicebasedemailsystem;

import java.io.Serializable;

/**
 * Created by Abanoub on 2017-12-04.
 */

public class NewUser implements Serializable {
    public String fullname;
    public String email;
    public String password;
    public String birthdate;
    public String gender;
    public String phoneNumber;
    public String secretQuestion;
    public String secretAnswer;
    public String country;
    public String pushID;

    public NewUser() {}

    public NewUser(String fullname, String email, String password){
        this.fullname = fullname;
        this.email = email;
        this.password = password;
    }

    public NewUser(String fullname, String email, String password, String birthdate, String gender, String phoneNumber, String secretQuestion, String secretAnswer, String pushID) {
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.birthdate = birthdate;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.secretQuestion = secretQuestion;
        this.secretAnswer = secretAnswer;
        this.pushID = pushID;
    }
}
