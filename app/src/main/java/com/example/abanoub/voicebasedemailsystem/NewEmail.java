package com.example.abanoub.voicebasedemailsystem;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Abanoub on 2017-12-03.
 */

public class NewEmail implements Parcelable {
    public String sender;
    public String receiver;
    public String title;
    public String body;
    public String date;
    public String isFavorite;
    public String pushID;


    public NewEmail(){}

    public NewEmail(String sender, String receiver, String title, String body, String date,String isFavorite, String pushID) {
        this.sender = sender;
        this.receiver = receiver;
        this.title = title;
        this.body = body;
        this.date = date;
        this.isFavorite=isFavorite;
        this.pushID=pushID;
    }

    protected NewEmail(Parcel in) {
        sender = in.readString();
        receiver = in.readString();
        title = in.readString();
        body = in.readString();
        date = in.readString();
        isFavorite=in.readString();
        pushID = in.readString();
    }

    public static final Creator<NewEmail> CREATOR = new Creator<NewEmail>() {
        @Override
        public NewEmail createFromParcel(Parcel in) {
            return new NewEmail(in);
        }

        @Override
        public NewEmail[] newArray(int size) {
            return new NewEmail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sender);
        dest.writeString(receiver);
        dest.writeString(title);
        dest.writeString(body);
        dest.writeString(date);
        dest.writeString(isFavorite);
        dest.writeString(pushID);
    }
}
