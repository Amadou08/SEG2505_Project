package com.example.novigradservice.AdminScreens;

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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.novigradservice.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class AddPhotoIdServiceActivity extends AppCompatActivity {
    EditText et_dob,et_first_name,et_last_name,et_address;
    DatePickerDialog datePicker;
    final Calendar myCalendar= Calendar.getInstance();

    private Dialog loadingDialog;
    ImageView docImage,customerImage;
    // StorageReference mRef;
    private Uri docImgUri =null;
    private Uri customerImgUri =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo_id_service);
        et_dob=findViewById(R.id.et_dob);
        et_address=findViewById(R.id.et_address);
        et_first_name=findViewById(R.id.et_first_name);
        et_last_name=findViewById(R.id.et_last_name);
        docImage=findViewById(R.id.docPic);
        customerImage=findViewById(R.id.customerImage);
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
                datePicker =  new DatePickerDialog(AddPhotoIdServiceActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH));
                datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
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
        else if(docImgUri==null){
            Toast.makeText(AddPhotoIdServiceActivity.this,"upload your document",Toast.LENGTH_LONG).show();
        } else if(customerImgUri==null){
            Toast.makeText(AddPhotoIdServiceActivity.this,"upload your document",Toast.LENGTH_LONG).show();
        }
        else {
            uploadRecord();
        }

    }

    public void uploadRecord(){

    }

    public void selectImage(View view){

        Intent intent=new Intent(Intent.ACTION_PICK,android.provider. MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,1);
    }
    public void selectCustomerImage(View view){
        Intent intent=new Intent(Intent.ACTION_PICK,android.provider. MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,2);
    }
    public String createFavId() throws Exception{
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }



    public  void checkPermission(){
        Dexter.withActivity(AddPhotoIdServiceActivity.this)
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
        AlertDialog.Builder builder = new AlertDialog.Builder(AddPhotoIdServiceActivity.this);
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
        Uri uri = Uri.fromParts("package",AddPhotoIdServiceActivity.this.getPackageName(), null);
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
            customerImgUri  = data.getData();
            customerImage.setImageURI(customerImgUri);
        }

    }
    // get the extension of file
    private String getFileEx(Uri uri){
        ContentResolver cr=AddPhotoIdServiceActivity.this.getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
}