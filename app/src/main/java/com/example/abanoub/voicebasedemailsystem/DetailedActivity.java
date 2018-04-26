package com.example.abanoub.voicebasedemailsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailedActivity extends AppCompatActivity {

    TextView title, sender, date, body;
    ImageView star_btn;
    CircleImageView profile_image;
    LinearLayout replay, forward;
    FloatingActionButton fab;
    TextToSpeech textToSpeech;
    NewEmail clicked_email;
    String child;
    boolean found=false;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
//    DatabaseReference favoritesReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        clicked_email = getIntent().getParcelableExtra("email");
        child = getIntent().getStringExtra("child");

        star_btn = (ImageView) findViewById(R.id.star);
        if (child.equals("Trash")) {
            star_btn.setVisibility(View.GONE);
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child(Utilities.getModifiedCurrentEmail())
                .child(child);

//        //another method to save favorites (by making favorite tab for each user)
//
//        favoritesReference = firebaseDatabase.getReference().child(Utilities.getModifiedCurrentEmail())
//                .child("Favorites");
//        favoritesReference.orderByKey().equalTo(clicked_email.pushID).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.getValue() != null) {
//                    found = true;
//                    star_btn.setImageResource(R.drawable.ic_star_24dp);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        if (clicked_email.isFavorite.equals("yes")) {
            found = true;
            star_btn.setImageResource(R.drawable.ic_star_24dp);
        }

        title = (TextView) findViewById(R.id.title);
        sender = (TextView) findViewById(R.id.sender);
        date = (TextView) findViewById(R.id.date);
        body = (TextView) findViewById(R.id.body);
        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        replay = (LinearLayout) findViewById(R.id.replayLinear);
        forward = (LinearLayout) findViewById(R.id.forwardLinear);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        title.setText(clicked_email.title);
        sender.setText(clicked_email.sender);
        date.setText(clicked_email.date);
        body.setText(clicked_email.body);
//        profile_image.setImageResource();

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });

        star_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (found) {
//                    favoritesReference.child(clicked_email.pushID).setValue(null);
                    clicked_email.isFavorite="no";
                    databaseReference.child(clicked_email.pushID).setValue(clicked_email);
                    star_btn.setImageResource(R.drawable.ic_star_border_24dp);
                    Toast.makeText(DetailedActivity.this, "Deleted from favorites", Toast.LENGTH_SHORT).show();
                    found = false;

                } else {
//                    favoritesReference.child(clicked_email.pushID).setValue(clicked_email);
                    clicked_email.isFavorite="yes";
                    databaseReference.child(clicked_email.pushID).setValue(clicked_email);
                    star_btn.setImageResource(R.drawable.ic_star_24dp);
                    Toast.makeText(DetailedActivity.this, "Marked as favorite", Toast.LENGTH_SHORT).show();
                    found = true;
                }

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String toSpeak = body.getText().toString();
                Toast.makeText(getApplicationContext(), toSpeak, Toast.LENGTH_SHORT).show();
                textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

            }
        });

        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DetailedActivity.this, ComposeEmailActivity.class);
                intent.putExtra("email", clicked_email);
                intent.putExtra("replay","replay");
                startActivity(intent);
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DetailedActivity.this, ComposeEmailActivity.class);
                intent.putExtra("email", clicked_email);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onPause() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            //t1.shutdown();
        }
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detailed_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.delete_menu:

                //delete email
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Are you sure that you want to delete this email ?");
                alertDialogBuilder.setPositiveButton("Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                databaseReference.child(clicked_email.pushID).setValue(null);

                                if (child.equals("Inbox") || child.equals("Favorites") || child.equals("Sent")) {

                                    databaseReference = firebaseDatabase.getReference().child(Utilities.getModifiedCurrentEmail())
                                            .child("Trash");
                                    databaseReference.child(clicked_email.pushID).setValue(clicked_email);
                                }
                                Toast.makeText(DetailedActivity.this, "Deleted successfully", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }
                        });
                alertDialogBuilder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                alertDialogBuilder.create().show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
