package com.example.abanoub.voicebasedemailsystem;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {

    DrawerLayout myDrawerlayout;
    ActionBarDrawerToggle myToggle;
    NavigationView navigationView;
    TextView nav_fullName, nav_email;
    CircleImageView nav_profile_image;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference personalDataReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDrawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        myToggle = new ActionBarDrawerToggle(this, myDrawerlayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        myDrawerlayout.addDrawerListener(myToggle);
        myToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.navDrawer);
        View nav_header = navigationView.getHeaderView(0);
        nav_fullName = (TextView) nav_header.findViewById(R.id.fullName);
        nav_email = (TextView) nav_header.findViewById(R.id.email);
        nav_profile_image = (CircleImageView) nav_header.findViewById(R.id.profile_image);

        String fragment=getIntent().getStringExtra("fragment");
        if (fragment==null)
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_main,  new InboxFragment()).commit();
        else if (fragment.equals("sent"))
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_main,  new SentFragment()).commit();
        else if (fragment.equals("favorites"))
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_main,  new FavoritesFragment()).commit();
        else if (fragment.equals("trash"))
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_main,  new TrashFragment()).commit();
        else
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_main,  new InboxFragment()).commit();

        firebaseDatabase = FirebaseDatabase.getInstance();
        personalDataReference = firebaseDatabase.getReference().child(Utilities.getModifiedCurrentEmail())
                .child("PersonalData");

        personalDataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                NewUser user=Utilities.getPersonalData(dataSnapshot);
                setNavHeaderFields(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                String title = item.getTitle().toString();

                switch (title) {
                    case "Inbox":
                        InboxFragment inboxFragment = new InboxFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_main, inboxFragment).commit();
                        myDrawerlayout.closeDrawers();
                        break;

                    case "Sent":
                        SentFragment sentFragment=new SentFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_main, sentFragment).commit();
                        myDrawerlayout.closeDrawers();
                        break;

                    case "Favorites":
                        FavoritesFragment favoritesFragment=new FavoritesFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_main, favoritesFragment).commit();
                        myDrawerlayout.closeDrawers();
                        break;

                    case "Trash":
                        TrashFragment trashFragment=new TrashFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout_main, trashFragment).commit();
                        myDrawerlayout.closeDrawers();
                        break;

                    case "Profile":
                        startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                        myDrawerlayout.closeDrawers();
                        break;

                    case "Sign out":
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainActivity.this, SignInActivity.class));
                        myDrawerlayout.closeDrawers();
                        break;

                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void setNavHeaderFields(NewUser user) {
//        nav_profile_image.setImageResource();
        nav_fullName.setText(user.fullname);
        nav_email.setText(user.email);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true); //exit app
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (myToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
}
