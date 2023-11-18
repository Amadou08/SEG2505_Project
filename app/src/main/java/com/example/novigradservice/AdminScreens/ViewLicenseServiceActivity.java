package com.example.novigradservice.AdminScreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.novigradservice.Model.License;
import com.example.novigradservice.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewLicenseServiceActivity extends AppCompatActivity {
       RecyclerView recyclerView;
    private Dialog loadingDialog;
     public  static   ArrayList<License> licenseArrayList=new ArrayList<License>();
    ArrayAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_license_service);
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
            View v= LayoutInflater.from(ViewLicenseServiceActivity.this).inflate(R.layout.item_license,parent,false);
            return  new ImageViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final ArrayAdapter.ImageViewHolder holder, @SuppressLint("RecyclerView") int position) {

            Picasso.with(ViewLicenseServiceActivity.this)
                    .load(licenseArrayList.get(position).getDocImg())
                    .placeholder(R.drawable.progress_animation)
                    .fit()
                    .centerCrop()
                    .into(holder.doc_image);
            holder.user_dob.setText(licenseArrayList.get(position).getDob());
            holder.license_type.setText("License Type : "+licenseArrayList.get(position).getType());
            holder.user_address.setText(licenseArrayList.get(position).getAddress());
            holder.name.setText(licenseArrayList.get(position).getFirstName()+" "+licenseArrayList.get(position).getLastName());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final CharSequence[] options = {"Delete","Update", "Cancel"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(ViewLicenseServiceActivity.this);
                    builder.setTitle("Select option");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (options[item].equals("Delete")) {
                                DatabaseReference databaseReference =
                                        FirebaseDatabase.getInstance().getReference("LicenseService").
                                                child(licenseArrayList.get(position).getUserId());
                                databaseReference.removeValue();
                                dialog.dismiss();
                                getRecord();
                            } else if (options[item].equals("Cancel")) {
                                dialog.dismiss();
                            }
                            else if (options[item].equals("Update")) {

                            }
                        }
                    });
                    builder.show();
                }
            });




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
    public void addLicenseService(View view){
        startActivity(new Intent(this,AddLicenseServiceActivity.class));
    }
}