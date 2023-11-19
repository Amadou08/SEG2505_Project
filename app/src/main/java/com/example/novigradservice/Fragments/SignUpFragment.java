package com.example.novigradservice.Fragments;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.novigradservice.R;
import com.example.novigradservice.Screens.AccountActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignUpFragment extends Fragment {
    private EditText etRegisterEmail,et_user_name,
            etRegisterPassword, etRegisterConfirmPassword,et_user_number,et_user_address;

    TextView tv_login;
    Button btnRegister;
    private Dialog loadingDialog;
  RadioButton customerChecked,serviceChecked;
    private FirebaseAuth firebaseAuth;
    DatabaseReference myRef;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_sign_up, container, false);
        /////loading dialog
        loadingDialog=new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        firebaseAuth = FirebaseAuth.getInstance();
        customerChecked=view.findViewById(R.id.customerChecked);
        serviceChecked=view.findViewById(R.id.serviceChecked);
        et_user_number=view.findViewById(R.id.et_user_number);

        tv_login=view.findViewById(R.id.tv_login);
        et_user_address=view.findViewById(R.id.et_user_address);
        etRegisterEmail = view.findViewById(R.id.et_register_email);
        etRegisterPassword = view.findViewById(R.id.et_register_password);
        etRegisterConfirmPassword = view.findViewById(R.id.et_register_confirm_password);
        et_user_name = view.findViewById(R.id.et_user_name);
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AccountActivity)getActivity()).showLoginScreen();
            }
        });

        btnRegister = view.findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onClick(View view) {
                String email = etRegisterEmail.getText().toString();
                String name = et_user_name.getText().toString();
                String password = etRegisterPassword.getText().toString();
                String confirm_password = etRegisterConfirmPassword.getText().toString();
                String user_number =et_user_number.getText().toString();
                String address=et_user_address.getText().toString();
                if (validate(email,name, password, confirm_password,user_number ,address)) requestRegister(email, confirm_password);
            }
        });
        return view;
    }
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private boolean validate(String email, String name, String password, String confirm_password, String user_number,String address) {
        if (email.isEmpty()) etRegisterEmail.setError("Enter email!");
        else if (user_number.isEmpty()) et_user_name.setError("Enter phone number!");
        else if (name.isEmpty()) et_user_name.setError("Enter name!");
        else if (address.isEmpty()) et_user_address.setError("Enter address!");
        else if (!email.contains("@")||!email.contains(".")) etRegisterEmail.setError("Enter valid email!");
        else if (password.isEmpty()) etRegisterPassword.setError("Enter password!");
        else if (password.length()<6) etRegisterPassword.setError("Password must be at least 6 characters!");
        else if (confirm_password.isEmpty()) etRegisterConfirmPassword.setError("Enter password!");
        else if (!password.equals(confirm_password)) etRegisterConfirmPassword.setError("Password not matched!");
        else return true;
        return false;
    }
    private void requestRegister(String email, String password) {
        loadingDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getCreateUserWithEmailOnClickListener(email));
    }
    private OnCompleteListener<AuthResult> getCreateUserWithEmailOnClickListener(String email) {
        return task -> {
            if (task.isSuccessful()) {
                add();
            } else {
                loadingDialog.dismiss();
                Toast.makeText(getContext(),"Registration failed!",Toast.LENGTH_LONG).show();

            }
        };
    }
    public void  add(){
        String id = firebaseAuth.getCurrentUser().getUid();
        if(customerChecked.isChecked()){
            myRef=  FirebaseDatabase.getInstance().getReference("Customer").child(id);
            myRef.child("Name").setValue(et_user_name.getText().toString());
            myRef.child("UserId").setValue(id);
            myRef.child("Mail").setValue(etRegisterEmail.getText().toString());
            myRef.child("Address").setValue(et_user_address.getText().toString());
            myRef.child("PhoneNumber").setValue(et_user_number.getText().toString());
            loadingDialog.dismiss();
            Toast.makeText(getContext(),"Registration successful",Toast.LENGTH_LONG).show();
            ((AccountActivity)getActivity()).showLoginScreen();
        }
        else if(serviceChecked.isChecked()){
            myRef=  FirebaseDatabase.getInstance().getReference("ServiceNovigrad").child(id);
            myRef.child("Name").setValue(et_user_name.getText().toString());
            myRef.child("UserId").setValue(id);
            myRef.child("Mail").setValue(etRegisterEmail.getText().toString());
            myRef.child("Address").setValue(et_user_address.getText().toString());
            myRef.child("PhoneNumber").setValue(et_user_number.getText().toString());
            loadingDialog.dismiss();
            Toast.makeText(getContext(),"Registration successful",Toast.LENGTH_LONG).show();
            ((AccountActivity)getActivity()).showLoginScreen();
        }


    }
}