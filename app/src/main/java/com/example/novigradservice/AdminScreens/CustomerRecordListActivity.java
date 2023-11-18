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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.novigradservice.Model.HealthCard;
import com.example.novigradservice.Model.User;
import com.example.novigradservice.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomerRecordListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private Dialog loadingDialog;
    public  static ArrayList<User> userArrayList=new ArrayList<User>();
   ArrayAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_record_list);
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
        userArrayList.clear();
        DatabaseReference myRef=  FirebaseDatabase.getInstance().getReference("Customer");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    userArrayList.add(new User(dataSnapshot1.child("Name").getValue(String.class)
                            ,dataSnapshot1.child("Address").getValue(String.class)
                            ,dataSnapshot1.child("PhoneNumber").getValue(String.class)
                            ,dataSnapshot1.child("Mail").getValue(String.class)
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
            View v= LayoutInflater.from(CustomerRecordListActivity.this).inflate(R.layout.item_customer,parent,false);
            return  new ArrayAdapter.ImageViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final ArrayAdapter.ImageViewHolder holder, @SuppressLint("RecyclerView") int position) {


            holder.user_address.setText("Address : "+userArrayList.get(position).getAddress());
            holder.user_number.setText("Number : "+userArrayList.get(position).getPhoneNumber());
            holder.name.setText("Name : "+userArrayList.get(position).getUserName());
            holder.user_mail.setText("Mail : "+userArrayList.get(position).getUserEmail());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final CharSequence[] options = {"Delete", "Cancel"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(CustomerRecordListActivity.this);
                    builder.setTitle("Select option");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (options[item].equals("Delete")) {
                                DatabaseReference databaseReference =
                                        FirebaseDatabase.getInstance().getReference("Customer").
                                                child(userArrayList.get(position).getUserId());
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
            return userArrayList.size();
        }

        public class ImageViewHolder extends  RecyclerView.ViewHolder {
            TextView name,user_address,user_mail,user_number;

            CardView cardView;
            public ImageViewHolder(@NonNull View itemView) {
                super(itemView);
                name=itemView.findViewById(R.id.user_name);
                user_mail=itemView.findViewById(R.id.user_mail);
                user_address=itemView.findViewById(R.id.user_address);
                cardView=itemView.findViewById(R.id.card);
                user_number=itemView.findViewById(R.id.user_number);
            }
        }
    }
}