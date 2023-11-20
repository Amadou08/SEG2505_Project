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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.novigradservice.Model.HealthCard;
import com.example.novigradservice.Model.License;
import com.example.novigradservice.R;
import com.example.novigradservice.Screens.AccountActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewHealthCardServiceActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private Dialog loadingDialog;
    public  static ArrayList<HealthCard> healthCardArrayList=new ArrayList<HealthCard>();
    ArrayAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_health_card_service);
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
        healthCardArrayList.clear();
        DatabaseReference myRef=  FirebaseDatabase.getInstance().getReference("HealthCardService");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    healthCardArrayList.add(new HealthCard(dataSnapshot1.child("FirstName").getValue(String.class)
                            ,dataSnapshot1.child("LastName").getValue(String.class)
                            ,dataSnapshot1.child("Address").getValue(String.class)
                            ,dataSnapshot1.child("DOB").getValue(String.class)
                            ,dataSnapshot1.child("AddressImage").getValue(String.class)
                            ,dataSnapshot1.child("StatusImage").getValue(String.class)
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
            View v= LayoutInflater.from(ViewHealthCardServiceActivity.this).inflate(R.layout.item_health_card,parent,false);
            return  new ArrayAdapter.ImageViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final ArrayAdapter.ImageViewHolder holder, @SuppressLint("RecyclerView") int position) {
            if(healthCardArrayList.get(position).getDocImg().equals("empty")){
                holder.doc_image.setImageDrawable(getDrawable(R.drawable.upload_icon));
            }else {
                Picasso.with(ViewHealthCardServiceActivity.this)
                        .load(healthCardArrayList.get(position).getDocImg())
                        .placeholder(R.drawable.progress_animation)
                        .fit()
                        .centerCrop()
                        .into(holder.doc_image);
            }
            if(healthCardArrayList.get(position).getStatusImg().equals("empty")){
                holder.status_img.setImageDrawable(getDrawable(R.drawable.upload_icon));
            }else {
                Picasso.with(ViewHealthCardServiceActivity.this)
                        .load(healthCardArrayList.get(position).getStatusImg())
                        .placeholder(R.drawable.progress_animation)
                        .fit()
                        .centerCrop()
                        .into(holder.status_img);
            }

            holder.user_dob.setText(healthCardArrayList.get(position).getDob());
            holder.user_address.setText(healthCardArrayList.get(position).getAddress());
            holder.name.setText(healthCardArrayList.get(position).getFirstName()+" "+healthCardArrayList.get(position).getLastName());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final CharSequence[] options = {"Delete", "Cancel"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(ViewHealthCardServiceActivity.this);
                    builder.setTitle("Select option");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (options[item].equals("Delete")) {
                                DatabaseReference databaseReference =
                                        FirebaseDatabase.getInstance().getReference("HealthCardService").
                                                child(healthCardArrayList.get(position).getUserId());
                                databaseReference.removeValue();
                                dialog.dismiss();
                                getRecord();
                            } else if (options[item].equals("Cancel")) {
                                dialog.dismiss();
                            }

                        }
                    });
                    builder.show();
                }
            });




        }

        @Override
        public int getItemCount() {
            return healthCardArrayList.size();
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
    public void addHealthCardService(View view){
        startActivity(new Intent(this,AddHealthCardServiceActivity.class));
    }

}