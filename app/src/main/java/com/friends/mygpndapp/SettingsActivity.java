package com.friends.mygpndapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    private Button updateAccountSettings;
    private EditText firstName,lastName,about_status_et;
    private TextView gender_tv,about_status_tv;
    private ImageView userProfileImage;
    private ImageView imgprof;
    private String currentUserId;
    private String retriveprofile;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    private String unamedata,ustatusdata,aboutData;
    private static final int GallaryPick = 1;
    private StorageReference UserProfileImagesRef,filePath;
    private ProgressDialog p;
//    private String currentUserID;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        currentUser = mAuth.getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();

        UserProfileImagesRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        Initializefields();



        rootRef.child("mygpnd").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("about").exists())
                {
                    aboutData = dataSnapshot.child("about").getValue(String.class);
                    about_status_et.setText(aboutData);
                }
                if (dataSnapshot.child("firstname").exists()) {
                    unamedata = dataSnapshot.child("firstname").getValue(String.class);
                    firstName.setText(unamedata);
                }
                if (dataSnapshot.child("lastname").exists()) {
                    ustatusdata = dataSnapshot.child("lastname").getValue(String.class);
                    lastName.setText(ustatusdata);
                }
                if (dataSnapshot.child("gender").exists())
                {
                    gender_tv.setText("Gender = " + dataSnapshot.child("gender").getValue(String.class));
                }
                if (dataSnapshot.child("profileimage").exists()) {
                    retriveprofile = dataSnapshot.child("profileimage").getValue().toString();
                    Picasso.get().load(retriveprofile).fit().centerInside().into(userProfileImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        updateAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateSettings();
            }
        });

        userProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent GallaryIntent = new Intent();
                GallaryIntent.setAction(Intent.ACTION_GET_CONTENT);
                GallaryIntent.setType("image/*");
                startActivityForResult(GallaryIntent, GallaryPick);
            }
        });
    }

    private void updateSettings() {
        String setUserName = firstName.getText().toString();
        String setStatus = lastName.getText().toString();
        String aboutStatus = about_status_et.getText().toString();

        if (TextUtils.isEmpty(setUserName)){
            Toast.makeText(this, "Please write your FirstName", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(setStatus)){
            Toast.makeText(this, "Please write your LastName", Toast.LENGTH_SHORT).show();
        }else
        {
//            HashMap<String, String> ProfileMap = new HashMap<>();
//            ProfileMap.put("uid",currentUserId);
//            ProfileMap.put("Uname",setUserName);
//            ProfileMap.put("Status",setStatus);

            p.setTitle("Profile Update");
            p.setMessage("Please wait your profile is updating...");
            p.setCanceledOnTouchOutside(false);
            p.show();
            DatabaseReference childRef = rootRef.child("mygpnd").child(currentUserId);

            Map SettingsBody = new HashMap();
            SettingsBody.put("uid",currentUserId);
            SettingsBody.put("firstname", setUserName);
            SettingsBody.put("lastname", setStatus);
            SettingsBody.put("about", aboutStatus);
            childRef.updateChildren(SettingsBody)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                sendUserToMainActivity();
                                p.dismiss();
                                Toast.makeText(SettingsActivity.this, "Profile successfully updated", Toast.LENGTH_SHORT).show();
                            }else
                            {
                                p.dismiss();
                                String message = task.getException().toString();
                                Toast.makeText(SettingsActivity.this, "Error "+message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }

    private void Initializefields() {
        updateAccountSettings = findViewById(R.id.update_settings_button);
        about_status_tv = findViewById(R.id.about_status_tv);
        about_status_et = findViewById(R.id.about_status_et);
        firstName = findViewById(R.id.set_first_name);
        lastName = findViewById(R.id.set_last_name);
        gender_tv = findViewById(R.id.gender_tv);
        userProfileImage = (ImageView) findViewById(R.id.set_profile_image);
        p = new ProgressDialog(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GallaryPick && resultCode==RESULT_OK && data!=null)
        {
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK)
            {
                p.setTitle("Set Profile Image");
                p.setMessage("Please wait your profile is updating...");
                p.setCanceledOnTouchOutside(false);


                Uri resultUri = result.getUri();



                filePath = UserProfileImagesRef.child(currentUserId + ".jpg");

                p.show();
                filePath.putFile(resultUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                Toast.makeText(SettingsActivity.this, "Profile Image Added Successfully", Toast.LENGTH_SHORT).show();


                                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        final String downloadUrl = uri.toString();

                                        Log.d("tomato", "onSuccess: uri= "+ uri.toString());

                                        rootRef.child("mygpnd").child(currentUserId).child("profileimage")
                                                .setValue(downloadUrl)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful())
                                                        {
                                                            p.dismiss();
                                                            Toast.makeText(SettingsActivity.this, "Image saved in database,Successfully...", Toast.LENGTH_SHORT).show();
                                                        }else
                                                        {
                                                            String message = task.getException().toString();
                                                            p.dismiss();
                                                            Toast.makeText(SettingsActivity.this, "Error: "+ message, Toast.LENGTH_SHORT).show();
                                                            Log.d("db", "onComplete: "+ message);

                                                        }
                                                    }
                                                });
                                    }
                                });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                // ...
                                p.dismiss();
                                Toast.makeText(SettingsActivity.this, "error  :"+exception.toString(), Toast.LENGTH_SHORT).show();
                                Log.d("joyful",exception.toString());
                            }
                        });
            }

        }
    }



    private void sendUserToMainActivity() {
        Intent loginIntent = new Intent(SettingsActivity.this,Page1Activity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

//    private void updateUserStatus(String state)
//    {
//        String saveCurrentTime,saveCurrentDate;
//
//        Calendar calendar = Calendar.getInstance();
//
//        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
//        saveCurrentDate = currentDate.format(calendar.getTime());
//
//        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
//        saveCurrentTime = currentTime.format(calendar.getTime());
//
//        HashMap<String, Object> onlineState = new HashMap<>();
//        onlineState.put("time",saveCurrentTime);
//        onlineState.put("date", saveCurrentDate);
//        onlineState.put("state",state);
//
//        currentUserID = mAuth.getCurrentUser().getUid();
//
//        rootRef.child("Users").child(currentUserID).child("userState")
//                .updateChildren(onlineState);
//
//
//
//    }
}
