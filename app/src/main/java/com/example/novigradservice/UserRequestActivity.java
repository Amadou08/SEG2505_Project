package com.example.novigradservice;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.novigradservice.Model.ServiceRequest;
import com.example.novigradservice.Model.Services;
import com.example.novigradservice.Utils.Constant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserRequestActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private Dialog loadingDialog;
    public ArrayList<ServiceRequest> serviceRequestArrayList=new ArrayList<ServiceRequest>();
    ArrayAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_request);
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
        serviceRequestArrayList.clear();
        DatabaseReference myRef=  FirebaseDatabase.getInstance().getReference("ServiceRequests");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    if(Constant.getUserId(UserRequestActivity.this)
                            .equals(dataSnapshot1.child("UserId").getValue(String.class))){
                        serviceRequestArrayList.add(new ServiceRequest(
                                dataSnapshot1.child("ServiceName").getValue(String.class)
                                ,(dataSnapshot1.child("FirstName").getValue(String.class)+dataSnapshot1.child("LastName").getValue(String.class))
                                ,dataSnapshot1.child("Address").getValue(String.class)
                                ,dataSnapshot1.child("AddressImage").getValue(String.class)
                                ,dataSnapshot1.child("Status").getValue(String.class)
                                ,dataSnapshot1.child("ServiceId").getValue(String.class)
                                ,dataSnapshot1.child("UserId").getValue(String.class)));
                    }


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
            View v= LayoutInflater.from(UserRequestActivity.this).inflate(R.layout.item_service_request,parent,false);
            return  new ArrayAdapter.ImageViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final ArrayAdapter.ImageViewHolder holder, @SuppressLint("RecyclerView") int position) {


            holder.service_name.setText("Service Name : "+serviceRequestArrayList.get(position).getServiceName());
            holder.user_name.setText("User Name : "+serviceRequestArrayList.get(position).getUserName());
            holder.user_address.setText("Address : "+serviceRequestArrayList.get(position).getAddress());
            holder.service_status.setText("Status : "+serviceRequestArrayList.get(position).getServiceStatus());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 if(serviceRequestArrayList.get(position).getServiceStatus().equals("Approve")){
                              startActivity(new Intent(UserRequestActivity.this,RatingActivity.class)
                                      .putExtra("ServiceName",serviceRequestArrayList.get(position).getServiceName()));
                 }
                }
            });
        }

        @Override
        public int getItemCount() {
            return serviceRequestArrayList.size();
        }

        public class ImageViewHolder extends  RecyclerView.ViewHolder {
            TextView service_status,user_address,service_name,user_name;
         ImageView imageView;
            CardView cardView;
            public ImageViewHolder(@NonNull View itemView) {
                super(itemView);
                service_status=itemView.findViewById(R.id.service_status);
                service_name=itemView.findViewById(R.id.service_name);

                imageView=itemView.findViewById(R.id.imageView);
                user_address=itemView.findViewById(R.id.user_address);
                user_name=itemView.findViewById(R.id.user_name);
                cardView=itemView.findViewById(R.id.card);
            }
        }
    }
}