package com.example.meutalento.Login;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.meutalento.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

        //firebase
        private FirebaseAuth mAuth;
        private FirebaseAuth.AuthStateListener mAuthListener;

        private Context mContext;
        private ProgressBar mProgressBar;
        private EditText mEmail, mPassword;
        private TextView mPleaseWait;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
            mPleaseWait = findViewById(R.id.pleaseWait);
            mEmail = findViewById(R.id.input_email);
            mPassword = findViewById(R.id.input_password);
            mContext = LoginActivity.this;

            mPleaseWait.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);

            setupFirebaseAuth();
            init();
        }

        private boolean isStringNull(String string){

            if(string.equals("")){
                return true;
            }
            else{
                return false;
            }
        }

     /*
    ------------------------------------ Firebase ---------------------------------------------
     */

        private void init(){

            //initialize the button for logging in
            Button btnLogin = findViewById(R.id.btn_login);
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String email = mEmail.getText().toString();
                    String password = mPassword.getText().toString();

                    if(isStringNull(email) && isStringNull(password)){
                        Toast.makeText(mContext, "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                    }else{
                        mProgressBar.setVisibility(View.VISIBLE);
                        mPleaseWait.setVisibility(View.VISIBLE);

                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(LoginActivity.this, getString(R.string.auth_failed),
                                                    Toast.LENGTH_SHORT).show();
                                            mProgressBar.setVisibility(View.GONE);
                                            mPleaseWait.setVisibility(View.GONE);
                                        }
                                        else{
                                            Toast.makeText(LoginActivity.this, getString(R.string.auth_success),
                                                    Toast.LENGTH_SHORT).show();
                                            mProgressBar.setVisibility(View.GONE);
                                            mPleaseWait.setVisibility(View.GONE);
                                        }

                                        // ...
                                    }
                                });
                    }

                }
            });
        }

        /**
         * Setup the firebase auth object
         */
        private void setupFirebaseAuth(){

            mAuth = FirebaseAuth.getInstance();

            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();

                    if (user != null) {
                        // User is signed in
                    } else {
                        // User is signed out
                    }
                    // ...
                }
            };
        }

        @Override
        public void onStart() {
            super.onStart();
            mAuth.addAuthStateListener(mAuthListener);
        }

        @Override
        public void onStop() {
            super.onStop();
            if (mAuthListener != null) {
                mAuth.removeAuthStateListener(mAuthListener);
            }
        }
    }
