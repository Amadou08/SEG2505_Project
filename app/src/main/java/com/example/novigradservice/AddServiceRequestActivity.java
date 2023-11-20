package com.example.novigradservice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.novigradservice.AdminScreens.AddLicenseServiceActivity;
import com.example.novigradservice.Utils.Constant;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddServiceRequestActivity extends AppCompatActivity {
      String serviceName;
    EditText et_first_name,et_last_name,et_address;
    private Dialog loadingDialog;
    private Uri imgUri =null;
    StorageReference mRef;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service_request);
        serviceName=getIntent().getStringExtra("service");
        mRef= FirebaseStorage.getInstance().getReference("document_images");

        et_address=findViewById(R.id.et_address);
        et_first_name=findViewById(R.id.et_first_name);
        et_last_name=findViewById(R.id.et_last_name);
        imageView=findViewById(R.id.docPic);
        /////loading dialog
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

    }
    public void saveRecord(View view){
        if(et_first_name.getText().toString().isEmpty()){
            et_first_name.setError("required");
        }else if(et_last_name.getText().toString().isEmpty()){
            et_last_name.setError("required");
        }else if(et_address.getText().toString().isEmpty()){
            et_address.setError("required");
        }
        else if(imgUri==null){
            String id = null;
            try {
                id = createFavId().substring(0, 8);
                DatabaseReference myRef=  FirebaseDatabase.getInstance().getReference("ServiceRequests").child(id);
                myRef.child("FirstName").setValue(et_first_name.getText().toString());
                myRef.child("ServiceId").setValue(id);
                myRef.child("LastName").setValue(et_last_name.getText().toString());
                myRef.child("Address").setValue(et_address.getText().toString());
                myRef.child("AddressImage").setValue("empty");
                myRef.child("Status").setValue("Pending");
                myRef.child("UserId").setValue(Constant.getUserId(AddServiceRequestActivity.this));
                loadingDialog.dismiss();
                Toast.makeText(AddServiceRequestActivity.this,"request send",Toast.LENGTH_LONG).show();
                finish();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }        }
        else {
            uploadRecord();
        }

    }

    public void uploadRecord(){
        loadingDialog.show();
        StorageReference storageReference = mRef.child(System.currentTimeMillis() + "." + getFileEx(imgUri));
        storageReference.putFile(imgUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!urlTask.isSuccessful()) ;
                        Uri downloadUrl = urlTask.getResult();
                        String id = null;
                        try {
                            id = createFavId().substring(0, 8);
                            DatabaseReference myRef=  FirebaseDatabase.getInstance().getReference("ServiceRequests").child(id);
                            myRef.child("FirstName").setValue(et_first_name.getText().toString());
                            myRef.child("ServiceId").setValue(id);
                            myRef.child("LastName").setValue(et_last_name.getText().toString());
                            myRef.child("Address").setValue(et_address.getText().toString());
                            myRef.child("AddressImage").setValue(downloadUrl.toString());
                            myRef.child("Status").setValue("Pending");
                            myRef.child("UserId").setValue(Constant.getUserId(AddServiceRequestActivity.this));
                            loadingDialog.dismiss();
                            Toast.makeText(AddServiceRequestActivity.this,"request send",Toast.LENGTH_LONG).show();
                            finish();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingDialog.dismiss();
                        Toast.makeText(AddServiceRequestActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                });




    }
    public void selectImage(View view){
        addImage();
    }
    public String createFavId() throws Exception{
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

    public void selectImageFromGallery(){
        Intent intent=new Intent(Intent.ACTION_PICK,android.provider. MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,1);
    }

    public  void addImage(){
        Dexter.withActivity(AddServiceRequestActivity.this)
                .withPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            selectImageFromGallery();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddServiceRequestActivity.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
//
    }
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",AddServiceRequestActivity.this.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            imgUri  = data.getData();
            imageView.setImageURI(imgUri);
        }

    }
    // get the extension of file
    private String getFileEx(Uri uri){
        ContentResolver cr=AddServiceRequestActivity.this.getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
}