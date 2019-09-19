package com.kushkumardhawan.inspections.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.kushkumardhawan.inspections.R;
import com.kushkumardhawan.inspections.Utilities.EConstants;

import rakshan.himachal.dit.permissions.RakshamPermissionResponse;
import rakshan.himachal.dit.permissions.RakshamPermissions;

public class Permissions extends Activity implements View.OnClickListener, RakshamPermissions.OnRequestPermissionsBack{

    private static final String TAG = "MainActivity";

    private Button checkButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);
        initViews();
    }

    private void initViews() {

        checkButton = (Button) findViewById(R.id.checkButton);

        checkButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        try {

            new RakshamPermissions.Builder(this)
                    .withPermissions(Manifest.permission.CAMERA,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.SEND_SMS,
                            Manifest.permission.INTERNET,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS)
                    .requestId(1)
                    .setListener(this)
                    .check();


        } catch (Exception e) {
           // CD.showDialog(Permissions.this, "Something Went wrong while setting the permissions.");
            Log.e("Error","Somethning Went Wrong");
        }

    }



    @Override
    public void onRequestBack(RakshamPermissionResponse gotaResponse) {
        SharedPreferences settings = getSharedPreferences(EConstants.PREF_SHARED, 0); // 0 - for private mode
        SharedPreferences.Editor editor = settings.edit();
        //Set "hasLoggedIn" to true
        editor.putBoolean("PermissionsFlag", true);
        editor.commit();

        Intent i = new Intent(Permissions.this, SelectUser.class);   //Working
        // Intent i = new Intent(Permissions.this,MainActivity_Navigation_Drawer.class);
        startActivity(i);
        Permissions.this.finish();

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
