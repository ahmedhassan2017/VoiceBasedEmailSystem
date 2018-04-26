package com.example.abanoub.voicebasedemailsystem;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Abanoub on 2017-12-03.
 */

public class Utilities {

    private static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public static FirebaseUser getCurrentUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null)
            return null;

        return user;
    }

    public static String getModifiedCurrentEmail() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null)
            return null;
        String currentUserEmail = user.getEmail().replace(".", "_");

        return currentUserEmail;
    }


    public static ArrayList<NewEmail> getAllEmails(DataSnapshot dataSnapshot) {

        ArrayList<NewEmail> list = new ArrayList<>();

        //iterate through each Email, ignoring their UID
        if (dataSnapshot.getValue() != null) {
            for (DataSnapshot child : dataSnapshot.getChildren()) {

                NewEmail emailObj = new NewEmail();
                emailObj.sender = child.getValue(NewEmail.class).sender;
                emailObj.receiver = child.getValue(NewEmail.class).receiver;
                emailObj.title = child.getValue(NewEmail.class).title;
                emailObj.body = child.getValue(NewEmail.class).body;
                emailObj.date = child.getValue(NewEmail.class).date;
                emailObj.isFavorite = child.getValue(NewEmail.class).isFavorite;
                emailObj.pushID = child.getValue(NewEmail.class).pushID;
                list.add(emailObj);
            }
        }
        return list;
    }

    public static ArrayList<NewEmail> getFavoriteEmails(DataSnapshot dataSnapshot) {

        ArrayList<NewEmail> list = new ArrayList<>();

        //iterate through each Email, ignoring their UID
        if (dataSnapshot.getValue() != null) {
            for (DataSnapshot child : dataSnapshot.getChildren()) {

                if ((child.getValue(NewEmail.class).isFavorite).equals("yes")) {
                    NewEmail emailObj = new NewEmail();
                    emailObj.sender = child.getValue(NewEmail.class).sender;
                    emailObj.receiver = child.getValue(NewEmail.class).receiver;
                    emailObj.title = child.getValue(NewEmail.class).title;
                    emailObj.body = child.getValue(NewEmail.class).body;
                    emailObj.date = child.getValue(NewEmail.class).date;
                    emailObj.isFavorite = child.getValue(NewEmail.class).isFavorite;
                    emailObj.pushID = child.getValue(NewEmail.class).pushID;
                    list.add(emailObj);
                }
            }
        }
        return list;
    }

    public static ArrayList<UserEmail> getAllUsersEmails(DataSnapshot dataSnapshot) {

        ArrayList<UserEmail> list = new ArrayList<>();

        //iterate through each Email, ignoring their UID
        if (dataSnapshot.getValue() != null) {
            for (DataSnapshot child : dataSnapshot.getChildren()) {

                UserEmail userEmailObj = new UserEmail();
                userEmailObj.email = child.getValue(UserEmail.class).email;
                userEmailObj.pushID = child.getValue(UserEmail.class).pushID;
                list.add(userEmailObj);
            }
        }
        return list;
    }

    public static NewUser getPersonalData(DataSnapshot dataSnapshot) {

        ArrayList<NewUser> list = new ArrayList<>();
        NewUser userObj = new NewUser();

        //iterate through each Email, ignoring their UID
        if (dataSnapshot.getValue() != null) {
            for (DataSnapshot child : dataSnapshot.getChildren()) {

                userObj.fullname = child.getValue(NewUser.class).fullname;
                userObj.email = child.getValue(NewUser.class).email;
                userObj.password = child.getValue(NewUser.class).password;
                userObj.birthdate = child.getValue(NewUser.class).birthdate;
                userObj.gender = child.getValue(NewUser.class).gender;
                userObj.phoneNumber = child.getValue(NewUser.class).phoneNumber;
                userObj.secretQuestion = child.getValue(NewUser.class).secretQuestion;
                userObj.secretAnswer = child.getValue(NewUser.class).secretAnswer;
                userObj.country = child.getValue(NewUser.class).country;
                userObj.pushID = child.getValue(NewUser.class).pushID;
            }
        }
        return userObj;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
