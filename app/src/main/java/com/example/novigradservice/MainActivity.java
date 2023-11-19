package com.example.novigradservice;

import static com.example.novigradservice.Utils.Constant.setAdminLoginStatus;
import static com.example.novigradservice.Utils.Constant.setCustomerLoginStatus;
import static com.example.novigradservice.Utils.Constant.setServiceLoginStatus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.novigradservice.Model.HealthCard;
import com.example.novigradservice.Model.Services;
import com.example.novigradservice.Screens.AccountActivity;
import com.example.novigradservice.ServiceNovigradScreens.ServiceNovigradMainActivity;
import com.example.novigradservice.ServiceNovigradScreens.ViewHealthCardServiceScreen;
import com.example.novigradservice.ServiceNovigradScreens.ViewLicenseServiceScreen;
import com.example.novigradservice.ServiceNovigradScreens.ViewPhotoIdServiceScreen;
import com.example.novigradservice.ServiceNovigradScreens.viewRequestRecordScreen;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private Dialog loadingDialog;
    public ArrayList<Services> servicesArrayList=new ArrayList<Services>();
    ArrayAdapter arrayAdapter;
    EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search=findViewById(R.id.search);
        recyclerView=findViewById(R.id.recylerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        //loading dialog
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

          search.addTextChangedListener(new TextWatcher() {
              @Override
              public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

              }

              @Override
              public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

              }

              @Override
              public void afterTextChanged(Editable editable) {
                  filter(editable.toString());
              }

              private void filter(String text) {
                  ArrayList<Services> filterlist=new ArrayList<>();
                  for(Services item: servicesArrayList){
                      if(item.getName().toLowerCase().contains(text.toLowerCase())
                              ||item.getEnd_time().toLowerCase().contains(text.toLowerCase())
                              ||item.getStart_time().toLowerCase().contains(text.toLowerCase())){
                          filterlist.add(item);
                      }
                  }
                  arrayAdapter.filteredList(filterlist);
              }
          });
    }

    @Override
    protected void onStart() {
        getRecord();
        super.onStart();
    }

    public void getRecord(){
        loadingDialog.show();
        servicesArrayList.clear();
        DatabaseReference myRef=  FirebaseDatabase.getInstance().getReference("Services");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot2:dataSnapshot.getChildren()){
                    servicesArrayList.add(new Services(
                            dataSnapshot2.child("ServiceName").getValue(String.class)
                            ,dataSnapshot2.child("StartTime").getValue(String.class)
                            ,dataSnapshot2.child("EndTime").getValue(String.class)
                            ,dataSnapshot2.child("Rating").getValue(String.class)));

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
        public ArrayList<Services> servicesList=new ArrayList<Services>();
        public ArrayAdapter(){
             this.servicesList=servicesArrayList;
        }
        @NonNull
        @Override
        public ArrayAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(MainActivity.this).inflate(R.layout.item_service,parent,false);
            return  new ArrayAdapter.ImageViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final ArrayAdapter.ImageViewHolder holder, @SuppressLint("RecyclerView") int position) {


            holder.service_name.setText("Service Name : "+servicesList.get(position).getName());
            holder.service_Start_time.setText("Start Time : "+servicesList.get(position).getStart_time());
            holder.service_end_time.setText("End Time : "+servicesList.get(position).getEnd_time());
            holder.service_rating.setText("Rating : "+servicesList.get(position).getRating());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this,AddServiceRequestActivity.class)
                            .putExtra("service",servicesList.get(position)
                                    .getName()));
                }
            });
        }

        @Override
        public int getItemCount() {
            return servicesList.size();
        }
        public void filteredList(ArrayList<Services> filterlist) {
            servicesList=filterlist;
            notifyDataSetChanged();
        }
        public class ImageViewHolder extends  RecyclerView.ViewHolder {
            TextView service_end_time,service_Start_time,service_name,service_rating;

            CardView cardView;
            public ImageViewHolder(@NonNull View itemView) {
                super(itemView);
                service_Start_time=itemView.findViewById(R.id.service_Start_time);
                service_name=itemView.findViewById(R.id.service_name);
                service_end_time=itemView.findViewById(R.id.service_end_time);
                service_rating=itemView.findViewById(R.id.service_rating);
                cardView=itemView.findViewById(R.id.card);
            }
        }
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemId = item.getItemId();
        if (menuItemId == R.id.logout) {
            setAdminLoginStatus(MainActivity.this,false);
            setCustomerLoginStatus(MainActivity.this,false);
            setServiceLoginStatus(MainActivity.this,false);
            startActivity(new Intent(MainActivity.this, AccountActivity.class));
            finish();
        }if (menuItemId == R.id.requites) {
            setAdminLoginStatus(MainActivity.this,false);
            setCustomerLoginStatus(MainActivity.this,false);
            setServiceLoginStatus(MainActivity.this,false);
            startActivity(new Intent(MainActivity.this, AccountActivity.class));
            finish();
        } else {
            return super.onOptionsItemSelected(item);
        }


        return super.onOptionsItemSelected(item);
    }

}