package com.example.novigradservice.ServiceNovigradScreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.novigradservice.AdminScreens.AddLicenseServiceActivity;
import com.example.novigradservice.Model.License;
import com.example.novigradservice.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.UUID;

public class BranchWorkingActivity extends AppCompatActivity {
          TextView tv_title;
          EditText et_start_time,et_last_name;
          String title;
          Button btn_save;
          boolean serviceStatus=false;

    private Dialog loadingDialog;
    String timeId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_working);
        /////loading dialog
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        tv_title=findViewById(R.id.tv_title);
        title=getIntent().getStringExtra("title");

        tv_title.setText(title);
        btn_save=findViewById(R.id.btn_save);
        et_last_name=findViewById(R.id.et_last_name);
        et_start_time=findViewById(R.id.et_start_time);

        et_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the current time
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);

                // Create a new TimePickerDialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        BranchWorkingActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                                // Display the selected time in the EditText
                                et_start_time.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                            }
                        },
                        hour,
                        minute,
                        true // set true if you want 24-hour format, false otherwise
                );

                // Show the TimePickerDialog
                timePickerDialog.show();
            }
        });

        et_last_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the current time
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);

                // Create a new TimePickerDialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        BranchWorkingActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                                // Display the selected time in the EditText
                                et_last_name.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                            }
                        },
                        hour,
                        minute,
                        true // set true if you want 24-hour format, false otherwise
                );

                // Show the TimePickerDialog
                timePickerDialog.show();
            }
        });

        et_last_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(serviceStatus) {
                   updateWorkingHours();

                }else {
                         addWorkingHours();
                }
            }
        });
    }

    public void updateWorkingHours(){
        if(title.equals("LicenseService")){
            DatabaseReference myRef=  FirebaseDatabase.getInstance().getReference("LicenseServiceTime").child(timeId);
            myRef.child("StartTime").setValue(et_start_time.getText().toString());
            myRef.child("EndTime").setValue(et_last_name.getText().toString());
            loadingDialog.dismiss();
            Toast.makeText(BranchWorkingActivity.this,"time added",Toast.LENGTH_LONG).show();
            finish();
        }else if(title.equals("HealthCardService")){
            DatabaseReference myRef=  FirebaseDatabase.getInstance().getReference("HealthCardServiceTime").child(timeId);
            myRef.child("StartTime").setValue(et_start_time.getText().toString());

            myRef.child("EndTime").setValue(et_last_name.getText().toString());
            loadingDialog.dismiss();
            Toast.makeText(BranchWorkingActivity.this,"time added",Toast.LENGTH_LONG).show();
            finish();
        }else if(title.equals("PhotoIdService")){
            DatabaseReference myRef=  FirebaseDatabase.getInstance().getReference("PhotoIdServiceTime").child(timeId);
            myRef.child("StartTime").setValue(et_start_time.getText().toString());
            myRef.child("EndTime").setValue(et_last_name.getText().toString());
            loadingDialog.dismiss();
            Toast.makeText(BranchWorkingActivity.this,"time added",Toast.LENGTH_LONG).show();
            finish();
        }
    }
    public void addWorkingHours(){
        loadingDialog.show();
        if(title.equals("LicenseService")){
            String id = null;
            try {
                id = createFavId().substring(0, 8);
                DatabaseReference myRef=  FirebaseDatabase.getInstance().getReference("LicenseServiceTime").child(id);
                myRef.child("StartTime").setValue(et_start_time.getText().toString());
                myRef.child("Id").setValue(id);
                myRef.child("EndTime").setValue(et_last_name.getText().toString());
                loadingDialog.dismiss();
                Toast.makeText(BranchWorkingActivity.this,"time added",Toast.LENGTH_LONG).show();
                finish();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }else if(title.equals("HealthCardService")){
            String id = null;
            try {
                id = createFavId().substring(0, 8);
                DatabaseReference myRef=  FirebaseDatabase.getInstance().getReference("HealthCardServiceTime").child(id);
                myRef.child("StartTime").setValue(et_start_time.getText().toString());
                myRef.child("Id").setValue(id);
                myRef.child("EndTime").setValue(et_last_name.getText().toString());
                loadingDialog.dismiss();
                Toast.makeText(BranchWorkingActivity.this,"time added",Toast.LENGTH_LONG).show();
                finish();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }else if(title.equals("PhotoIdService")){
            String id = null;
            try {
                id = createFavId().substring(0, 8);
                DatabaseReference myRef=  FirebaseDatabase.getInstance().getReference("PhotoIdServiceTime").child(id);
                myRef.child("StartTime").setValue(et_start_time.getText().toString());
                myRef.child("Id").setValue(id);
                myRef.child("EndTime").setValue(et_last_name.getText().toString());
                loadingDialog.dismiss();
                Toast.makeText(BranchWorkingActivity.this,"time added",Toast.LENGTH_LONG).show();
                finish();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }
    public String createFavId() throws Exception{
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }
    public void getData(){
        if(title.equals("LicenseService")){
            DatabaseReference myRef=  FirebaseDatabase.getInstance().getReference("LicenseServiceTime");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue()!=null){
                      et_start_time.setText(dataSnapshot.child("StartTime").getValue(String.class));
                      et_last_name.setText(dataSnapshot.child("EndTime").getValue(String.class));
                                timeId=dataSnapshot.child("Id").getValue(String.class);
                                  serviceStatus=true;
                                  btn_save.setText("update");
                    }
                    else {
                        serviceStatus=false;
                    }


                    loadingDialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }else if(title.equals("HealthCardService")){
            DatabaseReference myRef=  FirebaseDatabase.getInstance().getReference("HealthCardServiceTime");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue()!=null){
                        et_start_time.setText(dataSnapshot.child("StartTime").getValue(String.class));
                        et_last_name.setText(dataSnapshot.child("EndTime").getValue(String.class));
                        timeId=dataSnapshot.child("Id").getValue(String.class);
                        serviceStatus=true;
                        btn_save.setText("update");
                    }
                    else {
                        serviceStatus=false;
                    }


                    loadingDialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }else if(title.equals("PhotoIdService")){
            DatabaseReference myRef=  FirebaseDatabase.getInstance().getReference("PhotoIdServiceTime");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue()!=null){
                        et_start_time.setText(dataSnapshot.child("StartTime").getValue(String.class));
                        et_last_name.setText(dataSnapshot.child("EndTime").getValue(String.class));
                        timeId=dataSnapshot.child("Id").getValue(String.class);
                        serviceStatus=true;
                        btn_save.setText("update");
                    }
                    else {
                        serviceStatus=false;
                    }


                    loadingDialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }



    }

}