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
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText et_reg_firstnm,et_reg_last_nm,et_reg_email,et_reg_fpass,et_reg_password;
    Button reg_btn;
    RadioButton radio_male,radio_female;
    String Gender="";
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    ProgressDialog p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        et_reg_firstnm = findViewById(R.id.et_reg_firstnm);
        et_reg_last_nm = findViewById(R.id.et_reg_lastnm);
        et_reg_email = findViewById(R.id.et_reg_email);
        et_reg_fpass = findViewById(R.id.et_reg_fpass);
        et_reg_password = findViewById(R.id.et_reg_password);
        reg_btn = findViewById(R.id.reg_button);
        radio_male = findViewById(R.id.radio_male);
        radio_female = findViewById(R.id.radio_female);

        databaseReference = FirebaseDatabase.getInstance().getReference("mygpnd");
        firebaseAuth = FirebaseAuth.getInstance();

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (radio_male.isChecked()){
                    Gender = "Male";
                }
                if (radio_female.isChecked()){
                    Gender = "Female";
                }

                final String fname = et_reg_firstnm.getText().toString();
                final String lname = et_reg_last_nm.getText().toString();
                final String email = et_reg_email.getText().toString();
                final String password = et_reg_password.getText().toString();

                p = new ProgressDialog(RegisterActivity.this);
                p.setTitle("Loading");
                p.setMessage("please wait...");
                p.setCancelable(false);
                p.setCanceledOnTouchOutside(false);

                if (et_reg_firstnm.getText().toString().isEmpty()
                        || et_reg_last_nm.getText().toString().isEmpty()
                        || et_reg_email.getText().toString().isEmpty()
                        || et_reg_fpass.getText().toString().isEmpty()
                        || et_reg_password.getText().toString().isEmpty()
                        || Gender == ""
                ){
                    Toast.makeText(getApplicationContext(),"Fields should not be empty",Toast.LENGTH_LONG).show();
                }else if (!(et_reg_password.getText().toString().equals(et_reg_fpass.getText().toString()))){
                    Toast.makeText(getApplicationContext(),"Password didn't match",Toast.LENGTH_LONG).show();
                }else if (et_reg_password.getText().toString().equals(et_reg_fpass.getText().toString())){


                        p.show();
                    firebaseAuth.createUserWithEmailAndPassword(et_reg_email.getText().toString(), et_reg_password.getText().toString())
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        StudentInfo informationp = new StudentInfo(
                                                fname,
                                                lname,
                                                email,
                                                password,
                                                Gender
                                        );
                                        FirebaseDatabase.getInstance().getReference("mygpnd")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                        .setValue(informationp)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {


                                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                                    updateUI(user);
                                                    p.dismiss();
                                                }
                                            }
                                        });


                                    } else {
                                        p.dismiss();
                                        Toast.makeText(getApplicationContext(), "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                    }

                                    // ...
                                }
                            });

                }
            }
        });
    }
    private void updateUI(FirebaseUser user) {
        //update ui code here
        if (user != null) {
            startActivity(new Intent(getApplicationContext(), Page1Activity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }
}
