package com.kushkumardhawan.inspections.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;


import com.google.android.gms.location.LocationListener;

import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.kushkumardhawan.inspections.Abstract.PermissionUtils;
import com.kushkumardhawan.inspections.Generic.Custom_Dialog;
import com.kushkumardhawan.inspections.Model.User;
import com.kushkumardhawan.inspections.R;

public class MapsActivity extends AppCompatActivity implements

        GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        LocationListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener

{


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private boolean mPermissionDenied = false;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    LatLng latLng;
    Marker currLocationMarker;
    private GoogleMap mMap;
    public TextView tv_latitude,tv_longitude;
    Button proceed ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parking);

        Intent getRoomDetailsIntent = getIntent();
        final User userdetails =	(User) getRoomDetailsIntent.getSerializableExtra("UserPoJO");
        Log.e("Maps",userdetails.toString());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        tv_latitude = (TextView) findViewById(R.id.latitudetv);
        tv_longitude = (TextView) findViewById(R.id.longitudetv);
        proceed = (Button) findViewById(R.id.proceed);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if( tv_latitude.getText().toString().equalsIgnoreCase("") || tv_latitude.getText().toString().isEmpty() &&
                       tv_longitude.getText().toString().equalsIgnoreCase("") || tv_longitude.getText().toString().isEmpty()){
                   Custom_Dialog CD = new Custom_Dialog();
                   CD.showDialog(MapsActivity.this,"Unable to find the Location. Please wait ...");
               }else{
                   Intent GoToMainActivity = new Intent();
                   GoToMainActivity.putExtra("UserPoJO", userdetails);
                   GoToMainActivity.putExtra("Latitude", tv_latitude.getText().toString());
                   GoToMainActivity.putExtra("Longitude", tv_longitude.getText().toString());
                   GoToMainActivity.setClass(MapsActivity.this, MainActivity.class);
                   startActivity(GoToMainActivity);
               }
            }
        });


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMyLocationButtonClickListener(this);
        //mMap.setOnInfoWindowClickListener(this);
        // mMap.setOnInfoWindowLongClickListener(this);
        try {
            mMap.setMyLocationEnabled(true);
        }catch(SecurityException s){
            // Toast.makeText(getApplicationContext(),"Not Good",Toast.LENGTH_SHORT).show();
        }

        buildGoogleApiClient();

        mGoogleApiClient.connect();

        enableMyLocation();

//        if(latlongList.size()!=0){
//            PolygonOptions polOpt =
//                    new PolygonOptions().addAll(latlongList).strokeColor(Color.RED).fillColor(Color.BLUE);
//
//            Polygon polygon = mMap.addPolygon(polOpt);
//
//
//        }


    }



    protected synchronized void buildGoogleApiClient() {
        // Toast.makeText(this,"buildGoogleApiClient",Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,  android.Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Location mLastLocation = null;
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }catch(SecurityException r){
            //
            //  Toast.makeText(getApplicationContext(),"There is a problem with the GPS device.",Toast.LENGTH_SHORT).show();
        }
        if (mLastLocation != null) {
            //place marker at current position
            //mGoogleMap.clear();
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            // markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            currLocationMarker = mMap.addMarker(markerOptions);
        }
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(150000); //5 seconds
        mLocationRequest.setFastestInterval(100000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }catch(SecurityException s){
            //  Toast.makeText(getApplicationContext(),"Something's not Good.",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this,"onConnectionSuspended",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
//place marker at current position
        //mGoogleMap.clear();
        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        //MarkerOptions markerOptions = new MarkerOptions();
        // markerOptions.position(latLng);
        if(latLng!=null){

            //   tv_latitude.setText(Double.toString((location.getLatitude())));
            //  tv_longitude.setText(Double.toString((location.getLongitude())));

            Log.e("Latitude",Double.toString(location.getLatitude()));
            Log.e("Longitude",Double.toString(location.getLongitude()));
            // markerOptions.title("Latitude: \t"+Double.toString(location.getLatitude())+"\n Longitude:-"+ Double.toString(location.getLongitude()));

            //Update TextView
            new update_View_GPS().execute(Double.toString(location.getLatitude()),Double.toString(location.getLongitude()));


        }else{
            tv_latitude.setText("");
            tv_longitude.setText("");
        }
        //
        // markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        //currLocationMarker = mMap.addMarker(markerOptions);


        Location CurrentLocation = new Location("Current Location");
        CurrentLocation.setLatitude(latLng.latitude);
        CurrentLocation.setLongitude(latLng.longitude);


        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(18).build();  //default was 14

        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,"onConnectionFailed",Toast.LENGTH_SHORT).show();
    }



    @Override
    public boolean onMyLocationButtonClick() {
        if((latLng==null)) {
            Custom_Dialog CD = new Custom_Dialog();
            CD.showDialog(MapsActivity.this,"Please go to the settings and  enable your GPS Location.");

        }
        return false;
    }

    @Override
    public void onPolygonClick(Polygon polygon) {

    }



    @Override
    public void onPolylineClick(Polyline polyline) {

    }


    public class update_View_GPS extends AsyncTask<String,String,String[]> {

        String[] GPS_ = new String[2]; // <--initialized statement



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tv_latitude.setText("");
            tv_longitude.setText("");
        }

        @Override
        protected String[] doInBackground(String... params) {


            GPS_[0] = params[0];
            GPS_[1] = params[1];

            Log.e("Latitude Async", GPS_[0] );
            Log.e("Longitude Async", GPS_[1] );

            return GPS_;
        }

        @Override
        protected void onPostExecute(String[] s) {
            // super.onPreExecute(s);
            Log.e("Latitude Async Post", s[0] );
            Log.e("Longitude Async Post", s[1] );
            tv_latitude.setText(s[0]);
            tv_longitude.setText(s[1]);

        }
    }
}
