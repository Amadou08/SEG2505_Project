package com.example.novigradservice.Screens;

import static com.example.novigradservice.Utils.Constant.getAdminLoginStatus;
import static com.example.novigradservice.Utils.Constant.getCustomerLoginStatus;
import static com.example.novigradservice.Utils.Constant.getServiceLoginStatus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.novigradservice.AdminScreens.AdminMainActivity;
import com.example.novigradservice.MainActivity;
import com.example.novigradservice.R;
import com.example.novigradservice.ServiceNovigradScreens.ServiceNovigradMainActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Thread thread=new Thread(){
            @Override
            public void run() {
                try {
                    sleep(3000);
                    // if login status is true move to the main screen
                    if(getAdminLoginStatus(SplashActivity.this)){
                        startActivity(new Intent(SplashActivity.this, AdminMainActivity.class));
                        finish();
                    }else if(getServiceLoginStatus(SplashActivity.this)){
                            startActivity(new Intent(SplashActivity.this, ServiceNovigradMainActivity.class));
                            finish();

                    }else if(getCustomerLoginStatus(SplashActivity.this)){
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }

                    // if no one login move to login screen
                    else {
                        startActivity(new Intent(SplashActivity.this, AccountActivity.class));
                        finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}