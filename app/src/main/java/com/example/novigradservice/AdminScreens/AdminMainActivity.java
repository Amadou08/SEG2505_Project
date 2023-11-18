package com.example.novigradservice.AdminScreens;

import static com.example.novigradservice.Utils.Constant.setAdminLoginStatus;
import static com.example.novigradservice.Utils.Constant.setCustomerLoginStatus;
import static com.example.novigradservice.Utils.Constant.setServiceLoginStatus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.novigradservice.R;
import com.example.novigradservice.Screens.AccountActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
    }
    public void viewCustomerRecord(View view){
             startActivity(new Intent(this,CustomerRecordListActivity.class));
    }
    public void btnLicenseClicked(View view){

        final CharSequence[] options = {"Delete Record","View Record", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminMainActivity.this);
        builder.setTitle("Select option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Delete Record")) {
                    DatabaseReference databaseReference =
                            FirebaseDatabase.getInstance().getReference("LicenseService");
                    databaseReference.removeValue();
                    dialog.dismiss();
                    Toast.makeText(AdminMainActivity.this,"all record deleted",Toast.LENGTH_LONG).show();

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
                else if (options[item].equals("View Record")) {
              startActivity(new Intent(AdminMainActivity.this,ViewLicenseServiceActivity.class));
                }
            }
        });
        builder.show();

    }
    public void btnHealthCardClicked(View view){
        final CharSequence[] options = {"Delete Record","View Record", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminMainActivity.this);
        builder.setTitle("Select option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Delete Record")) {
                    DatabaseReference databaseReference =
                            FirebaseDatabase.getInstance().getReference("HealthCardService");
                    databaseReference.removeValue();
                    dialog.dismiss();
                    Toast.makeText(AdminMainActivity.this,"all record deleted",Toast.LENGTH_LONG).show();

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
                else if (options[item].equals("View Record")) {
                    startActivity(new Intent(AdminMainActivity.this,ViewHealthCardServiceActivity.class));
                }
            }
        });
        builder.show();
    }
    public void btnPhotoIdClicked(View view){
        final CharSequence[] options = {"Delete Record","View Record", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminMainActivity.this);
        builder.setTitle("Select option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Delete Record")) {
                    DatabaseReference databaseReference =
                            FirebaseDatabase.getInstance().getReference("PhotoIdService");
                    databaseReference.removeValue();
                    dialog.dismiss();
                    Toast.makeText(AdminMainActivity.this,"all record deleted",Toast.LENGTH_LONG).show();

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
                else if (options[item].equals("View Record")) {
                    startActivity(new Intent(AdminMainActivity.this,ViewPhotoIdServiceActivity.class));
                }
            }
        });
        builder.show();
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
            setAdminLoginStatus(AdminMainActivity.this,false);
            setCustomerLoginStatus(AdminMainActivity.this,false);
            setServiceLoginStatus(AdminMainActivity.this,false);
            startActivity(new Intent(AdminMainActivity.this, AccountActivity.class));
            finish();
        } else {
            return super.onOptionsItemSelected(item);
        }


        return super.onOptionsItemSelected(item);
    }



}