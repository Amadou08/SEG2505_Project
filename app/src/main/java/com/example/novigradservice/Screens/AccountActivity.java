package com.example.novigradservice.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.novigradservice.Fragments.LoginFragment;
import com.example.novigradservice.Fragments.SignUpFragment;
import com.example.novigradservice.R;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        showLoginScreen();
    }
    // function to load login fragment in activity
    public void showLoginScreen(){
        getSupportFragmentManager().beginTransaction().replace(R.id.frag,new LoginFragment()).commit();
    }
    // function to call signup fragment in activity
    public void showSignUpScreen(){
        getSupportFragmentManager().beginTransaction().replace(R.id.frag,new SignUpFragment()).commit();
    }
}