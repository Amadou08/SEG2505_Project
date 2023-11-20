package com.example.novigradservice.ServiceNovigradScreens;

import static com.example.novigradservice.Utils.Constant.setAdminLoginStatus;
import static com.example.novigradservice.Utils.Constant.setCustomerLoginStatus;
import static com.example.novigradservice.Utils.Constant.setServiceLoginStatus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.novigradservice.R;
import com.example.novigradservice.Screens.AccountActivity;

public class ServiceNovigradMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_novigrad_main);
    }
    public void viewRequestRecord(View view){
        startActivity(new Intent(this, viewRequestRecordScreen.class));
    }
    public void btnLicenseClicked(View view){

        startActivity(new Intent(ServiceNovigradMainActivity.this, ViewLicenseServiceScreen.class));


    }
    public void btnHealthCardClicked(View view){
        startActivity(new Intent(ServiceNovigradMainActivity.this, ViewHealthCardServiceScreen.class));

    }
    public void btnPhotoIdClicked(View view){
        startActivity(new Intent(ServiceNovigradMainActivity.this, ViewPhotoIdServiceScreen.class));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemId = item.getItemId();
        if (menuItemId == R.id.logout) {
            setAdminLoginStatus(ServiceNovigradMainActivity.this,false);
            setCustomerLoginStatus(ServiceNovigradMainActivity.this,false);
            setServiceLoginStatus(ServiceNovigradMainActivity.this,false);
            startActivity(new Intent(ServiceNovigradMainActivity.this, AccountActivity.class));
            finish();
        } else {
            return super.onOptionsItemSelected(item);
        }


        return super.onOptionsItemSelected(item);
    }
}