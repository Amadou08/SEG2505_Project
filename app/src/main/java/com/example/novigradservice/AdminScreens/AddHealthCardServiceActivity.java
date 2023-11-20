package com.example.novigradservice.AdminScreens;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.novigradservice.R;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class AddHealthCardServiceActivity extends AppCompatActivity {
    EditText et_dob,et_first_name,et_last_name,et_address;
    DatePickerDialog datePicker;
    final Calendar myCalendar= Calendar.getInstance();

    private Dialog loadingDialog;
    ImageView docImage,statusImage;
    // StorageReference mRef;
    private Uri docImgUri =null;
    private Uri statusImgUri =null;
    StorageReference mRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_health_card_service);
        et_dob=findViewById(R.id.et_dob);
        et_address=findViewById(R.id.et_address);
        et_first_name=findViewById(R.id.et_first_name);
        et_last_name=findViewById(R.id.et_last_name);
        docImage=findViewById(R.id.docPic);
        statusImage=findViewById(R.id.statusImage);
        mRef= FirebaseStorage.getInstance().getReference("document_images");
        /////loading dialog
        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                String myFormat="MM/dd/yyyy";
                SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
                et_dob.setText(dateFormat.format(myCalendar.getTime()));
            }
        };

        et_dob.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View view) {
                datePicker =  new DatePickerDialog(AddHealthCardServiceActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH));
                datePicker.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                datePicker.show();
            }
        });
        checkPermission();
    }
    public void saveRecord(View view){
        if(et_first_name.getText().toString().isEmpty()){
            et_first_name.setError("required");
        }else if(et_last_name.getText().toString().isEmpty()){
            et_last_name.setError("required");
        }else if(et_dob.getText().toString().isEmpty()){
            et_dob.setError("required");
        }
        else if(docImgUri==null&&statusImgUri==null){
            String id = null;
            try {
                id = createFavId().substring(0, 8);
                DatabaseReference myRef=  FirebaseDatabase.getInstance().getReference("HealthCardService").child(id);
                myRef.child("FirstName").setValue(et_first_name.getText().toString());
                myRef.child("UserId").setValue(id);
                myRef.child("LastName").setValue(et_last_name.getText().toString());
                myRef.child("Address").setValue(et_address.getText().toString());
                myRef.child("DOB").setValue(et_dob.getText().toString());
                myRef.child("AddressImage").setValue("empty");
                myRef.child("StatusImage").setValue("empty");
                loadingDialog.dismiss();
                Toast.makeText(AddHealthCardServiceActivity.this,"health card service added",Toast.LENGTH_LONG).show();
                finish();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        else if(docImgUri==null&&statusImgUri!=null){
            uploadRecord("empty");
        }
        else {
            getAddressDocImageUrl();
        }

    }

    public void getAddressDocImageUrl(){
        loadingDialog.show();
        StorageReference storageReference = mRef.child(System.currentTimeMillis() + "." + getFileEx(docImgUri));
        storageReference.putFile(docImgUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!urlTask.isSuccessful()) ;
                        Uri downloadUrl = urlTask.getResult();
                        if(statusImgUri==null){
                            String id = null;
                            try {
                                id = createFavId().substring(0, 8);
                                DatabaseReference myRef=  FirebaseDatabase.getInstance().getReference("HealthCardService").child(id);
                                myRef.child("FirstName").setValue(et_first_name.getText().toString());
                                myRef.child("UserId").setValue(id);
                                myRef.child("LastName").setValue(et_last_name.getText().toString());
                                myRef.child("Address").setValue(et_address.getText().toString());
                                myRef.child("DOB").setValue(et_dob.getText().toString());
                                myRef.child("AddressImage").setValue(downloadUrl.toString());
                                myRef.child("StatusImage").setValue("empty");
                                loadingDialog.dismiss();
                                Toast.makeText(AddHealthCardServiceActivity.this,"health card service added",Toast.LENGTH_LONG).show();
                                finish();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }else {
                            uploadRecord(downloadUrl.toString());

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingDialog.dismiss();
                        Toast.makeText(AddHealthCardServiceActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                });









    }
    public void uploadRecord(String addressDocUrl){
        loadingDialog.show();
        StorageReference storageReference = mRef.child(System.currentTimeMillis() + "." + getFileEx(statusImgUri));
        storageReference.putFile(statusImgUri)
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
                            DatabaseReference myRef=  FirebaseDatabase.getInstance().getReference("HealthCardService").child(id);
                            myRef.child("FirstName").setValue(et_first_name.getText().toString());
                            myRef.child("UserId").setValue(id);
                            myRef.child("LastName").setValue(et_last_name.getText().toString());
                            myRef.child("Address").setValue(et_address.getText().toString());
                            myRef.child("DOB").setValue(et_dob.getText().toString());
                            myRef.child("AddressImage").setValue(addressDocUrl);
                            myRef.child("StatusImage").setValue(downloadUrl.toString());
                            loadingDialog.dismiss();
                            Toast.makeText(AddHealthCardServiceActivity.this,"health card service added",Toast.LENGTH_LONG).show();
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
                        Toast.makeText(AddHealthCardServiceActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                });
    }

    public void selectImage(View view){

        selectImageFromGallery();
    }
    public void selectStatusImage(View view){
        Intent intent=new Intent(Intent.ACTION_PICK,android.provider. MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,2);
    }
    public String createFavId() throws Exception{
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

    public void selectImageFromGallery(){
        Intent intent=new Intent(Intent.ACTION_PICK,android.provider. MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,1);
    }

    public  void checkPermission(){
        Dexter.withActivity(AddHealthCardServiceActivity.this)
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {

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
        AlertDialog.Builder builder = new AlertDialog.Builder(AddHealthCardServiceActivity.this);
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
        Uri uri = Uri.fromParts("package",AddHealthCardServiceActivity.this.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            docImgUri  = data.getData();
            docImage.setImageURI(docImgUri);
        } if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
            statusImgUri  = data.getData();
            statusImage.setImageURI(statusImgUri);
        }

    }
    // get the extension of file
    private String getFileEx(Uri uri){
        ContentResolver cr=AddHealthCardServiceActivity.this.getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
}