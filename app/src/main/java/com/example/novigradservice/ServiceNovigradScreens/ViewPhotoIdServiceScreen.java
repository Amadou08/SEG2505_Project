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

import com.example.novigradservice.AdminScreens.ViewPhotoIdServiceActivity;
import com.example.novigradservice.Model.PhotoId;
import com.example.novigradservice.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewPhotoIdServiceScreen extends AppCompatActivity {
    RecyclerView recyclerView;
    private Dialog loadingDialog;
    public  ArrayList<PhotoId> photoIdArrayList=new ArrayList<PhotoId>();
    ArrayAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo_id_service_screen);
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
        photoIdArrayList.clear();
        DatabaseReference myRef=  FirebaseDatabase.getInstance().getReference("PhotoIdService");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    photoIdArrayList.add(new PhotoId(dataSnapshot1.child("FirstName").getValue(String.class)
                            ,dataSnapshot1.child("LastName").getValue(String.class)
                            ,dataSnapshot1.child("Address").getValue(String.class)
                            ,dataSnapshot1.child("DOB").getValue(String.class)
                            ,dataSnapshot1.child("AddressImage").getValue(String.class)
                            ,dataSnapshot1.child("CustomerImage").getValue(String.class)
                            ,dataSnapshot1.child("UserId").getValue(String.class)));

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
            View v= LayoutInflater.from(ViewPhotoIdServiceScreen.this).inflate(R.layout.item_health_card,parent,false);
            return  new ArrayAdapter.ImageViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final ArrayAdapter.ImageViewHolder holder, @SuppressLint("RecyclerView") int position) {

            if(photoIdArrayList.get(position).getDocImg().equals("empty")){
                holder.doc_image.setImageDrawable(getDrawable(R.drawable.upload_icon));
            }else {
                Picasso.with(ViewPhotoIdServiceScreen.this)
                        .load(photoIdArrayList.get(position).getDocImg())
                        .placeholder(R.drawable.progress_animation)
                        .fit()
                        .centerCrop()
                        .into(holder.doc_image);
            }
            if(photoIdArrayList.get(position).getCustomerImg().equals("empty")){
                holder.status_img.setImageDrawable(getDrawable(R.drawable.upload_icon));
            }else {
                Picasso.with(ViewPhotoIdServiceScreen.this)
                        .load(photoIdArrayList.get(position).getCustomerImg())
                        .placeholder(R.drawable.progress_animation)
                        .fit()
                        .centerCrop()
                        .into(holder.status_img);
            }
            holder.user_dob.setText(photoIdArrayList.get(position).getDob());
            holder.user_address.setText(photoIdArrayList.get(position).getAddress());
            holder.name.setText(photoIdArrayList.get(position).getFirstName()+" "+photoIdArrayList.get(position).getLastName());


        }

        @Override
        public int getItemCount() {
            return photoIdArrayList.size();
        }

        public class ImageViewHolder extends  RecyclerView.ViewHolder {
            TextView name,user_address,user_dob;
            ImageView doc_image,status_img;
            CardView cardView;
            public ImageViewHolder(@NonNull View itemView) {
                super(itemView);
                name=itemView.findViewById(R.id.user_name);
                user_dob=itemView.findViewById(R.id.user_dob);
                user_address=itemView.findViewById(R.id.user_address);
                doc_image=itemView.findViewById(R.id.doc_img);
                status_img=itemView.findViewById(R.id.status_img);
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

            startActivity(new Intent(ViewPhotoIdServiceScreen.this,
                    BranchWorkingActivity.class)
                    .putExtra("title","PhotoIdService"));

        } else {
            return super.onOptionsItemSelected(item);
        }


        return super.onOptionsItemSelected(item);
    }
}