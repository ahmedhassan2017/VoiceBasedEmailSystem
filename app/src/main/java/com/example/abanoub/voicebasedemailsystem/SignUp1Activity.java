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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp1Activity extends Activity {

    EditText fullName;
    EditText email;
    EditText password;
    EditText confirmPassword;
    Button signup_btn;
    TextView Gotologin;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up1);

        firebaseAuth = FirebaseAuth.getInstance();

        fullName = (EditText) findViewById(R.id.fullName);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        signup_btn = (Button) findViewById(R.id.signup_btn);
        Gotologin = (TextView) findViewById(R.id.login_link);


        fullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0 && TextUtils.isEmpty(email.getText()) == false
                        && TextUtils.isEmpty(password.getText()) == false && TextUtils.isEmpty(confirmPassword.getText()) == false) {
                    signup_btn.setEnabled(true);

                } else {
                    signup_btn.setEnabled(false);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0 && TextUtils.isEmpty(password.getText()) == false
                        && TextUtils.isEmpty(fullName.getText()) == false && TextUtils.isEmpty(confirmPassword.getText()) == false) {
                    signup_btn.setEnabled(true);
                } else {
                    signup_btn.setEnabled(false);
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
                if (charSequence.toString().trim().length() > 0 && TextUtils.isEmpty(email.getText()) == false
                        && TextUtils.isEmpty(fullName.getText()) == false && TextUtils.isEmpty(confirmPassword.getText()) == false) {
                    signup_btn.setEnabled(true);

                } else {
                    signup_btn.setEnabled(false);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0 && TextUtils.isEmpty(email.getText()) == false
                        && TextUtils.isEmpty(fullName.getText()) == false && TextUtils.isEmpty(password.getText()) == false) {
                    signup_btn.setEnabled(true);

                } else {
                    signup_btn.setEnabled(false);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utilities.isNetworkAvailable(SignUp1Activity.this)) {
                    if (TextUtils.isEmpty(email.getText()) || TextUtils.isEmpty(password.getText())
                            || TextUtils.isEmpty(fullName.getText()) || TextUtils.isEmpty(confirmPassword.getText()))
                        Toast.makeText(SignUp1Activity.this, R.string.fields_cannot_be_empty, Toast.LENGTH_SHORT).show();
                    else {
                        if (password.getText().toString().equals(confirmPassword.getText().toString())) {
                            firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                                    .addOnCompleteListener(SignUp1Activity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                // Sign in success, update UI with the signed-in user's information
                                                Intent intent = new Intent(SignUp1Activity.this, SignUp2Activity.class);
                                                NewUser newUser = new NewUser(fullName.getText().toString(), email.getText().toString()
                                                        , password.getText().toString());

                                                intent.putExtra("newUser", newUser);
                                                startActivity(intent);
                                            } else {
                                                // If sign in fails, display a message to the user.
                                                Toast.makeText(SignUp1Activity.this, R.string.authentication_failed, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                        else
                            Toast.makeText(SignUp1Activity.this, R.string.passwords_donot_match, Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(SignUp1Activity.this, R.string.check_internet_connection, Toast.LENGTH_SHORT).show();
            }
        });


        Gotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp1Activity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }
}