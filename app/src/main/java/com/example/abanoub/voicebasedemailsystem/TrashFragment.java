package com.example.abanoub.voicebasedemailsystem;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class TrashFragment extends Fragment {
    
    ListView listView;
    MessageAdapter adapter;
    LinearLayout emptyLinear;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment=  inflater.inflate(R.layout.fragment_sent, container, false);

        getActivity().setTitle("Trash");
        emptyLinear = (LinearLayout) fragment.findViewById(R.id.emptyLinear);
        listView = (ListView) fragment.findViewById(R.id.EmailsListView);
        FloatingActionButton mic = (FloatingActionButton) fragment.findViewById(R.id.mic);
        FloatingActionButton composeEmail = (FloatingActionButton) fragment.findViewById(R.id.composeEmail);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child(Utilities.getModifiedCurrentEmail()).child("Trash");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<NewEmail> emails_list = Utilities.getAllEmails(dataSnapshot);
                fillListView(emails_list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewEmail email = (NewEmail) adapter.getItem(position);
                if (email==null)
                    return;
                Intent intent = new Intent(getActivity(), DetailedActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("child","Trash");
                startActivity(intent);
            }
        });

        mic.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(getContext(), getString(R.string.speech_not_supported),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        composeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ComposeEmailActivity.class));
            }
        });
        
        return fragment;
    }

    private void fillListView(ArrayList<NewEmail> emails_list) {
        if (emails_list.size() == 0)
            emptyLinear.setVisibility(View.VISIBLE);
        else
            emptyLinear.setVisibility(View.GONE);

        adapter = new MessageAdapter(getActivity(), emails_list);
        listView.setAdapter(adapter);
    }


    /**
     * Receiving speech input
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == getActivity().RESULT_OK && null != data) {

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    if (result.get(0).equals("compose email") || result.get(0).equals("compose an email")
                            || result.get(0).equals("compose new email") || result.get(0).equals("compose mail")
                            || result.get(0).equals("compose new mail") || result.get(0).equals("write email")
                            || result.get(0).equals("write an email") || result.get(0).equals("write new email")
                            || result.get(0).equals("write mail") || result.get(0).equals("write new mail")
                            || result.get(0).contains("compose") || result.get(0).contains("write")
                            || result.get(0).contains("new mail"))
                        startActivity(new Intent(getActivity(), ComposeEmailActivity.class));

                    else if (result.get(0).equals("sign out") || result.get(0).equals("log out")
                            || result.get(0).contains("sign out")) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getActivity(), SignInActivity.class));

                    } else if (result.get(0).equals("profile") || result.get(0).equals("open profile")
                            || result.get(0).equals("open my profile")|| result.get(0).equals("show me my profile")
                            || result.get(0).equals("show profile") || result.get(0).contains("profile"))
                        startActivity(new Intent(getActivity(),ProfileActivity.class));

                    else if (result.get(0).equals("sent") || result.get(0).equals("open sent")
                            || result.get(0).equals("open sent emails")|| result.get(0).equals("open sent page")
                            || result.get(0).equals("open sent mails") || result.get(0).equals("show me sent emails")
                            || result.get(0).equals("show me sent mails") || result.get(0).contains("sent"))
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_main,
                                new SentFragment()).commit();

                    else if (result.get(0).equals("favorites") || result.get(0).equals("open favorites")
                            || result.get(0).equals("open my favorites") || result.get(0).equals("open favorite emails")
                            || result.get(0).equals("open favorites page") || result.get(0).equals("open favorite mails")
                            || result.get(0).equals("show me favorite emails")|| result.get(0).equals("show me favorite mails")
                            || result.get(0).contains("favorite")|| result.get(0).contains("favorites")
                            || result.get(0).contains("favourite")|| result.get(0).contains("favourites"))
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_main,
                                new FavoritesFragment()).commit();

                    else if (result.get(0).equals("inbox") || result.get(0).equals("open inbox")
                            || result.get(0).equals("open my inbox") || result.get(0).equals("open received emails")
                            || result.get(0).equals("open inbox page") || result.get(0).equals("open received mails")
                            || result.get(0).contains("show me inbox")|| result.get(0).contains("show me my inbox")
                            || result.get(0).equals("show me received emails")|| result.get(0).equals("show me received mails")
                            || result.get(0).contains("inbox")|| result.get(0).contains("received"))
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_main,
                                new InboxFragment()).commit();

                    else if (result.get(0).equals("exit") || result.get(0).equals("exit application")
                            || result.get(0).equals("exit from application")|| result.get(0).equals("back")
                            || result.get(0).equals("go back"))
                        getActivity().onBackPressed();

                    else if (result.get(0).equals("trash") || result.get(0).equals("open trash")
                            || result.get(0).equals("open my trash") || result.get(0).equals("open trash emails")
                            || result.get(0).equals("open trash page") || result.get(0).equals("open trash mails")
                            || result.get(0).equals("show me trash emails")|| result.get(0).equals("show me trash mails")
                            || result.get(0).equals("open my deleted emails")|| result.get(0).contains("deleted")
                            || result.get(0).contains("trash")|| result.get(0).contains("trashed"))
                        Toast.makeText(getActivity(), "We already here", Toast.LENGTH_SHORT).show();

                    else
                        Toast.makeText(getActivity(), "Not recognized", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
}