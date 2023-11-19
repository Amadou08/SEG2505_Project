package com.example.novigradservice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.novigradservice.ServiceNovigradScreens.BranchWorkingActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RatingActivity extends AppCompatActivity {
           String ServiceName;
    private RatingBar ratingBar;
    float userRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        ServiceName=getIntent().getStringExtra("ServiceName");

        ratingBar = findViewById(R.id.ratingBar);

        // Set a listener to respond to user rating changes
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                // Perform actions based on the selected rating
                userRating= rating;

            }
        });
    }

    public void SaveRating(View view){

    }
    public void updateRating(){

        if(ServiceName.equals("LicenseService")){
            DatabaseReference myRef=  FirebaseDatabase.getInstance().
                    getReference("Services").child("PhotoIdServiceTime");
            myRef.child("Rating").setValue(userRating+"");

        }else if(ServiceName.equals("HealthCardService")){
            DatabaseReference myRef=  FirebaseDatabase.getInstance().
                    getReference("Services").child("PhotoIdServiceTime");
            myRef.child("Rating").setValue(userRating+"");

        }else if(ServiceName.equals("PhotoIdService")){
            DatabaseReference myRef=  FirebaseDatabase.getInstance().
                    getReference("Services").child("PhotoIdServiceTime");
            myRef.child("Rating").setValue(userRating+"");
        }
    }
}