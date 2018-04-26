package com.example.abanoub.voicebasedemailsystem;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ComposeEmailActivity extends AppCompatActivity {

    EditText senderED;
    EditText receiverED;
    EditText titleED;
    EditText bodyED;
    boolean receiverFound = false;
    FloatingActionButton speak_btn;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    NewEmail clicked_email;
    String isReplay;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_email);

        isReplay=getIntent().getStringExtra("replay");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child(Utilities.getModifiedCurrentEmail()).child("Sent");

        senderED = (EditText) findViewById(R.id.senderED);
        receiverED = (EditText) findViewById(R.id.receiverED);
        titleED = (EditText) findViewById(R.id.titleED);
        bodyED = (EditText) findViewById(R.id.bodyED);
        speak_btn = (FloatingActionButton) findViewById(R.id.fab);

        clicked_email = getIntent().getParcelableExtra("email");
        if (clicked_email != null) {
            titleED.setText(clicked_email.title);
            bodyED.setText(clicked_email.body);
            if (isReplay!=null)
                receiverED.setText(clicked_email.sender);
        }

        senderED.setText(Utilities.getCurrentUser().getEmail());

        speak_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Showing google speech input dialog
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                        getString(R.string.speech_prompt));
                try {
                    startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(), getString(R.string.speech_not_supported),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    bodyED.append("\n" + result.get(0));
                }
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.compose_email_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.send_menu:

                if (TextUtils.isEmpty(senderED.getText()) || TextUtils.isEmpty(receiverED.getText())
                        || TextUtils.isEmpty(bodyED.getText()))
                    Toast.makeText(ComposeEmailActivity.this, R.string.fields_cannot_be_empty, Toast.LENGTH_SHORT).show();
                else {
                    DatabaseReference usersReference = firebaseDatabase.getReference().child("Users");
                    usersReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            ArrayList<UserEmail> allUsersEmails = Utilities.getAllUsersEmails(dataSnapshot);
                            for (int i = 0; i < allUsersEmails.size(); i++) {
                                Log.e("onDataChange: ", allUsersEmails.get(i).email);
                                if (receiverED.getText().toString().equals(allUsersEmails.get(i).email)) {
                                    receiverFound = true;
                                    break;
                                }
                            }
                            sendEmail();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendEmail() {
        if (receiverFound) {
            if (TextUtils.isEmpty(titleED.getText()))
                titleED.setText("(No Title)");

            String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

            NewEmail newEmail = new NewEmail(senderED.getText().toString(), receiverED.getText().toString()
                    , titleED.getText().toString(), bodyED.getText().toString(), date, "no", databaseReference.push().getKey());

            databaseReference.child(newEmail.pushID).setValue(newEmail);
            databaseReference = firebaseDatabase.getReference().child(receiverED.getText().toString().replace(".", "_")).child("Inbox");
            databaseReference.child(newEmail.pushID).setValue(newEmail);

            Toast.makeText(ComposeEmailActivity.this, "Successfully sending email", Toast.LENGTH_LONG).show();

            startActivity(new Intent(ComposeEmailActivity.this, MainActivity.class));

        } else {
            Toast.makeText(ComposeEmailActivity.this, "Wrong email address", Toast.LENGTH_LONG).show();
        }
    }

}
