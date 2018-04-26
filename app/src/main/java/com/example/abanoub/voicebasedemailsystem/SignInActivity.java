package com.example.abanoub.voicebasedemailsystem;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abanoub.voicebasedemailsystem.Shaking.MyService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends Activity {

    EditText email;
    EditText password;
    Button signin_btn;
    TextView GotoSignUp;
    FirebaseAuth firebaseAuth;

    public static boolean isServiceRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        startService(new Intent(this, MyService.class));

        firebaseAuth = FirebaseAuth.getInstance();

        email= (EditText) findViewById(R.id.email);
        password= (EditText) findViewById(R.id.password);
        signin_btn= (Button) findViewById(R.id.login_btn);
        GotoSignUp = (TextView) findViewById(R.id.signup_link);

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0 && TextUtils.isEmpty(password.getText())==false) {
                    signin_btn.setEnabled(true);
                } else {
                    signin_btn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0 && TextUtils.isEmpty(email.getText())==false) {
                    signin_btn.setEnabled(true);

                } else {
                    signin_btn.setEnabled(false);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utilities.isNetworkAvailable(SignInActivity.this)) {
                    if (TextUtils.isEmpty(email.getText()) || TextUtils.isEmpty(password.getText()))
                        Toast.makeText(SignInActivity.this, R.string.wrong_email_password, Toast.LENGTH_SHORT).show();
                    else {
                        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                                .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(SignInActivity.this, R.string.authentication_failed, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }else
                    Toast.makeText(SignInActivity.this, R.string.check_internet_connection, Toast.LENGTH_SHORT).show();
            }
        });


        GotoSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUp1Activity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent=new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true); //exit app
    }
}