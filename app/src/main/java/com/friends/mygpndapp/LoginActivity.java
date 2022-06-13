package com.friends.mygpndapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail,etPass;
    private Button loginbtn,regbtn;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();


        etEmail = findViewById(R.id.et_email);
        etPass = findViewById(R.id.et_password);
        loginbtn = findViewById(R.id.login_button);
        regbtn = findViewById(R.id.regbutton);



        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(registerIntent);
                finish();
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                p = new ProgressDialog(LoginActivity.this);
                p.setTitle("Loading");
                p.setMessage("please wait...");
                p.setCancelable(false);
                p.setCanceledOnTouchOutside(false);
            logintoapp();

            }
        });



    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);


     }
    private void updateUI(FirebaseUser user) {
        //update ui code here
        if (user != null) {
            startActivity(new Intent(LoginActivity.this, Page1Activity.class));
            finish();
        }
    }
    public void logintoapp() {
        if (etEmail.getText().toString().isEmpty() && etPass.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Email or Password should not be empty", Toast.LENGTH_SHORT).show();
        } else {

            p.show();
            firebaseAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPass.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("Login fire", "signInWithEmail:success");

                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                updateUI(user);
                                p.dismiss();
                            } else {
                                // If sign in fails, display a message to the user.

                                p.dismiss();
                                Log.w("Login fire", "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }

                            // ...
                        }
                    });


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(in);
        finish();
    }
}
