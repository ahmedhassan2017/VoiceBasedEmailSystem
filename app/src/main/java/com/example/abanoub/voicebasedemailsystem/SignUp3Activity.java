package com.example.abanoub.voicebasedemailsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp3Activity extends AppCompatActivity {

    Spinner countrySpinner;
    EditText phoneNumberED;
    Spinner secretQuestionSpinner;
    EditText secretAnswerED;
    Button skip_btn;
    Button finish_btn;
    NewUser newUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference personalDataReference;
    DatabaseReference usersReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up3);

        newUser = (NewUser) getIntent().getSerializableExtra("newUser");

        firebaseDatabase = FirebaseDatabase.getInstance();
        personalDataReference = firebaseDatabase.getReference().child(Utilities.getModifiedCurrentEmail()).child("PersonalData");
        usersReference = firebaseDatabase.getReference().child("Users");

        countrySpinner = (Spinner) findViewById(R.id.countrySpinner);
        phoneNumberED = (EditText) findViewById(R.id.phoneNumberED);
        secretQuestionSpinner = (Spinner) findViewById(R.id.secretQuestionSpinner);
        secretAnswerED = (EditText) findViewById(R.id.secretAnswerED);
        skip_btn = (Button) findViewById(R.id.skip_btn);
        finish_btn = (Button) findViewById(R.id.finish_btn);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.country, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(
                this, R.array.secretQuestions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        secretQuestionSpinner.setAdapter(adapter);


        skip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertToDatabase();
            }
        });

        finish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(phoneNumberED.getText()) || TextUtils.isEmpty(secretAnswerED.getText()))
                    Toast.makeText(SignUp3Activity.this, R.string.fields_cannot_be_empty, Toast.LENGTH_SHORT).show();
                else {
                    newUser.country = countrySpinner.getSelectedItem().toString();
                    newUser.phoneNumber = phoneNumberED.getText().toString();
                    newUser.secretQuestion = secretQuestionSpinner.getSelectedItem().toString();
                    newUser.secretAnswer = secretAnswerED.getText().toString();

                    insertToDatabase();
                }
            }
        });
    }

    private void insertToDatabase() {
        newUser.pushID = personalDataReference.push().getKey();

        personalDataReference.child(newUser.pushID).setValue(newUser);

        UserEmail userEmail = new UserEmail(newUser.email, usersReference.push().getKey());
        usersReference.child(userEmail.pushID).setValue(userEmail);

        Toast.makeText(SignUp3Activity.this, "Account created successfully", Toast.LENGTH_LONG).show();

        startActivity(new Intent(SignUp3Activity.this, MainActivity.class));
    }
}