package com.example.novigradservice.ServiceNovigradScreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.novigradservice.Model.License;
import com.example.novigradservice.R;
import com.example.novigradservice.UserRequestActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewLicenseServiceScreen extends AppCompatActivity {
    RecyclerView recyclerView;
    private Dialog loadingDialog;
    public   ArrayList<License> licenseArrayList=new ArrayList<License>();
    ArrayAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_license_service_screen);
        recyclerView=findViewById(R.id.recylerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        //loading dialog
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void onStart() {
        getRecord();
        super.onStart();
    }

    public void getRecord(){
        loadingDialog.show();
        licenseArrayList.clear();
        DatabaseReference myRef=  FirebaseDatabase.getInstance().getReference("LicenseService");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    licenseArrayList.add(new License(dataSnapshot1.child("FirstName").getValue(String.class)
                            ,dataSnapshot1.child("LastName").getValue(String.class)
                            ,dataSnapshot1.child("Address").getValue(String.class)
                            ,dataSnapshot1.child("DOB").getValue(String.class)
                            ,dataSnapshot1.child("AddressImage").getValue(String.class)
                            ,dataSnapshot1.child("UserId").getValue(String.class)
                            ,dataSnapshot1.child("Type").getValue(String.class)));

                }
                arrayAdapter =new ArrayAdapter();
                recyclerView.setAdapter(arrayAdapter);
                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public class ArrayAdapter extends RecyclerView.Adapter<ArrayAdapter.ImageViewHolder> {

        public ArrayAdapter(){

        }
        @NonNull
        @Override
        public ArrayAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(ViewLicenseServiceScreen.this).inflate(R.layout.item_license,parent,false);
            return  new ArrayAdapter.ImageViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final ArrayAdapter.ImageViewHolder holder, @SuppressLint("RecyclerView") int position) {


            if(licenseArrayList.get(position).getDocImg().equals("empty")){
                holder.doc_image.setImageDrawable(getDrawable(R.drawable.upload_icon));
            }else {
                Picasso.with(ViewLicenseServiceScreen.this)
                        .load(licenseArrayList.get(position).getDocImg())
                        .placeholder(R.drawable.progress_animation)
                        .fit()
                        .centerCrop()
                        .into(holder.doc_image);
            }
            holder.user_dob.setText(licenseArrayList.get(position).getDob());
            holder.license_type.setText("License Type : "+licenseArrayList.get(position).getType());
            holder.user_address.setText(licenseArrayList.get(position).getAddress());
            holder.name.setText(licenseArrayList.get(position).getFirstName()+" "+licenseArrayList.get(position).getLastName());

        }

        @Override
        public int getItemCount() {
            return licenseArrayList.size();
        }

        public class ImageViewHolder extends  RecyclerView.ViewHolder {
            TextView name,user_address,user_dob,license_type;
            ImageView doc_image;
            CardView cardView;
            public ImageViewHolder(@NonNull View itemView) {
                super(itemView);
                name=itemView.findViewById(R.id.user_name);
                license_type=itemView.findViewById(R.id.license_type);
                user_dob=itemView.findViewById(R.id.user_dob);
                user_address=itemView.findViewById(R.id.user_address);
                doc_image=itemView.findViewById(R.id.imageView);
                cardView=itemView.findViewById(R.id.card);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main1, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemId = item.getItemId();
        if (menuItemId == R.id.branch_working) {

            startActivity(new Intent(ViewLicenseServiceScreen.this,
                    BranchWorkingActivity.class)
                    .putExtra("title","LicenseService"));

        } else {
            return super.onOptionsItemSelected(item);
        }


        return super.onOptionsItemSelected(item);
    }
}