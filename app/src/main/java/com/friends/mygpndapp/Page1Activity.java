package com.friends.mygpndapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Page1Activity extends AppCompatActivity {

    Button princi_desk_btn,liabrary_btn,it_dept_btn,civil_dept_btn,pg_dept_btn,mech_dept_btn,about_us_btn;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page1);
        princi_desk_btn = findViewById(R.id.principaldesk_btn);
        liabrary_btn = findViewById(R.id.liabrary_btn);
        pg_dept_btn = findViewById(R.id.pg_dept_btn);
        it_dept_btn = findViewById(R.id.it_dept_btn);
        civil_dept_btn = findViewById(R.id.civil_dept_btn);
        mech_dept_btn = findViewById(R.id.mechanical_dept_btn);
        about_us_btn = findViewById(R.id.page1next_btn);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        about_us_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Page1Activity.this,Main2Activity.class);
                startActivity(intent);

            }
        });

        mech_dept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Page1Activity.this,Mechanical.class);
                startActivity(intent);

            }
        });

        pg_dept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Page1Activity.this,Production.class);
                startActivity(intent);

            }
        });

        civil_dept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Page1Activity.this,CivilDept.class);
                startActivity(intent);

            }
        });

        it_dept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Page1Activity.this,IFDept.class);
                startActivity(intent);

            }
        });

        liabrary_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Page1Activity.this,Liabrary.class);
                startActivity(intent);

            }
        });


        princi_desk_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Page1Activity.this,PrincipalDesk.class);
                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int mid = item.getItemId();

        if (mid == R.id.sign_out_menu){
            updateUI(mUser);
        }
        if (mid == R.id.settings_menu){
            Intent settingsIntent = new Intent(getApplicationContext(),SettingsActivity.class);
            startActivity(settingsIntent);
        }
        return super.onOptionsItemSelected(item);
    }
    private void updateUI(FirebaseUser user) {
        //update ui code here
        if (user != null) {
            mAuth.signOut();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
    }
//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
//    }
}
