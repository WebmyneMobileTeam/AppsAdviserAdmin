package com.xbpsolutions.appsadviseradmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartupActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edEmail;
    private EditText edPassword;
    private Button btnCreateAccount;
    private Button btnLogin;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        edEmail = (EditText) findViewById(R.id.edEmail);
        edPassword = (EditText) findViewById(R.id.edPassword);
        btnCreateAccount = (Button) findViewById(R.id.btnCreateUser);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(this);
        btnCreateAccount.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    gotoDashboard();
                } else {
                }

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnCreateUser:

                processCreateUser();

                break;

            case R.id.btnLogin:

                processLogin();

                break;

        }
    }

    private void processLogin() {

        progressDialog = ProgressDialog.show(StartupActivity.this,"Please Wait","Checking User",false);
        mAuth.signInWithEmailAndPassword(edEmail.getText().toString(), edPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        progressDialog.dismiss();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {

                            Toast.makeText(StartupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {

                        }

                        // ...
                    }
                });

    }

    private void processCreateUser() {
        progressDialog = ProgressDialog.show(StartupActivity.this,"Please Wait","Creating User",false);
        if (checkValidation()) {
            mAuth.createUserWithEmailAndPassword(edEmail.getText().toString(), edPassword.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            progressDialog.dismiss();
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(StartupActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            } else {

                            }

                            // ...
                        }
                    });
        }


    }

    private void gotoDashboard() {

        Intent iDashboard = new Intent(StartupActivity.this, DashBoardActivity.class);
        startActivity(iDashboard);
        finish();
    }

    public boolean checkValidation() {
        boolean isPass = false;

        if (edPassword.getText().toString().isEmpty() || edEmail.getText().toString().isEmpty()) {
            isPass = false;
        } else {
            isPass = true;
        }

        return isPass;

    }
}
