package com.example.novigradservice.Fragments;

import static com.example.novigradservice.Utils.Constant.getServiceLoginStatus;
import static com.example.novigradservice.Utils.Constant.setAdminLoginStatus;
import static com.example.novigradservice.Utils.Constant.setCustomerLoginStatus;
import static com.example.novigradservice.Utils.Constant.setServiceLoginStatus;
import static com.example.novigradservice.Utils.Constant.setUserId;
import static com.example.novigradservice.Utils.Constant.setUsername;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.novigradservice.AdminScreens.AdminMainActivity;
import com.example.novigradservice.MainActivity;
import com.example.novigradservice.R;
import com.example.novigradservice.Screens.AccountActivity;
import com.example.novigradservice.ServiceNovigradScreens.ServiceNovigradMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginFragment extends Fragment {
    private EditText etLoginEmail, etLoginPassword;
    TextView tv_new_register;

    private Dialog loadingDialog;
    private FirebaseAuth firebaseAuth;


    DatabaseReference myRef,databaseReference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_login, container, false);
        /////loading dialog
        loadingDialog=new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        firebaseAuth = FirebaseAuth.getInstance();
        etLoginEmail =view.findViewById(R.id.et_login_email);
        etLoginPassword = view.findViewById(R.id.et_login_password);
        tv_new_register=view.findViewById(R.id.tv_new_register);
        Button btnLogin = view.findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onClick(View view) {
                String email = etLoginEmail.getText().toString();
                String password = etLoginPassword.getText().toString();
                // call the validate function and then request
                if (validate(email, password)) requestLogin(email, password);
            }
        });


        tv_new_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AccountActivity)getActivity()).showSignUpScreen();
            }
        });
        return view;
    }
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private boolean validate(String email, String password) {
        if (email.isEmpty()) etLoginEmail.setError("Required!");
        else if (password.isEmpty()) etLoginPassword.setError("Enter password!");
        else if (password.length()<6) etLoginPassword.setError("Password must be at least 6 characters!");
        else return true;
        return false;
    }
    private void requestLogin(String email, String password) {
        loadingDialog.show();
                       if(email.equals("admin")&&password.equals("123admin456")){
                           setServiceLoginStatus(getContext() ,false);
                           setCustomerLoginStatus(getContext(), false);
                           setAdminLoginStatus(getContext(),true);
                           startActivity(new Intent(getContext(), AdminMainActivity.class));
                           getActivity().finish();
                           loadingDialog.dismiss();
                       }else {
                           firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                               @Override
                               public void onComplete(@NonNull Task<AuthResult> task) {
                                   if (!task.isSuccessful()) {
                                       loadingDialog.dismiss();
                                       Toast.makeText(getContext(), "wrong mail or password" + task.getException(), Toast.LENGTH_LONG).show();
                                   } else if (task.isSuccessful()) {
                                       getCustomerData();
                                   }
                               }
                           });
                       }
    }
    public void getCustomerData(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
       DatabaseReference myRef= FirebaseDatabase.getInstance().getReference().child("Customer").child(firebaseUser.getUid());
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    setUserId(getContext(),firebaseUser.getUid());
                    setUsername(getContext(),dataSnapshot.child("Name").getValue(String.class));
                    setServiceLoginStatus(getContext() ,false);
                    setCustomerLoginStatus(getContext(), true);
                    setAdminLoginStatus(getContext(),false);
                    loadingDialog.dismiss();
                    startActivity(new Intent(getContext(), MainActivity.class));
                    getActivity().finish();

                }
                else {
                    getServiceNovigrad();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    public void getServiceNovigrad(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
      DatabaseReference  myRef= FirebaseDatabase.getInstance().getReference().child("ServiceNovigrad").child(firebaseUser.getUid());
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    setUserId(getContext(),firebaseUser.getUid());
                    setUsername(getContext(),dataSnapshot.child("Name").getValue(String.class));
                    setServiceLoginStatus(getContext() ,true);
                    setCustomerLoginStatus(getContext(), false);
                    setAdminLoginStatus(getContext(),false);
                    loadingDialog.dismiss();
                  startActivity(new Intent(getContext(), ServiceNovigradMainActivity.class));
                  getActivity().finish();

                }
                else {

                    loadingDialog.dismiss();
                    Toast.makeText(getContext(), "wrong mail or password" , Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}