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

public class SignUp2Activity extends AppCompatActivity {

    Spinner monthSpinner;
    EditText dayED;
    EditText yearED;
    Spinner genderSpinner;
    Button next;
    NewUser newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        newUser = (NewUser) getIntent().getSerializableExtra("newUser");

        monthSpinner = (Spinner) findViewById(R.id.monthSpinner);
        dayED = (EditText) findViewById(R.id.dayED);
        yearED = (EditText) findViewById(R.id.yearED);
        genderSpinner = (Spinner) findViewById(R.id.genderSpinner);
        next = (Button) findViewById(R.id.next);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.months, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(
                this, R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(dayED.getText()) || TextUtils.isEmpty(yearED.getText()))
                    Toast.makeText(SignUp2Activity.this, R.string.fields_cannot_be_empty, Toast.LENGTH_SHORT).show();
                else {
                    newUser.birthdate = dayED.getText().toString() + "-" + monthSpinner.getSelectedItem().toString() + "-" + yearED.getText().toString();
                    newUser.gender = genderSpinner.getSelectedItem().toString();
                    Intent intent = new Intent(SignUp2Activity.this, SignUp3Activity.class);
                    intent.putExtra("newUser", newUser);
                    startActivity(intent);
                }
            }
        });
    }
}

