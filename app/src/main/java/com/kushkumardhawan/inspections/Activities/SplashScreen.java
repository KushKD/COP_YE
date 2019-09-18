package com.kushkumardhawan.inspections.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.kushkumardhawan.inspections.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


//        SharedPreferences settings = getSharedPreferences(EConstants.PREF_SHARED, 0);
//        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
//        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//
//            boolean Flag_Registration = settings.getBoolean(EConstants.LOG_IN, false);  //Registration flag is the login one
//
//
//            if (Flag_Registration) {
//                Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
//                SplashScreen.this.startActivity(mainIntent);
//                SplashScreen.this.finish();
//            } else {
//                Intent mainIntent2= new Intent(SplashScreen.this, Login.class);  //LoginType
//                SplashScreen.this.startActivity(mainIntent2);
//                SplashScreen.this.finish();
//            }
//
//
//
//        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            Intent mainIntent = new Intent(SplashScreen.this, Permissions.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
            }
        }, 5000);
    }
}
