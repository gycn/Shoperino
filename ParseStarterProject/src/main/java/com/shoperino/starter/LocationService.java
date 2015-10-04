package com.shoperino.starter;

import android.app.Service;
import android.content.Intent;
import android.location.LocationListener;
import android.os.IBinder;


import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.Date;

public class LocationService extends Service implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    LocationRequest mLocationRequest;
    GoogleApiClient  mGoogleApiClient;
    @Override
    public void onCreate() {
        //creating log file in mobile
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(5*1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //  mLocationRequest.setFastestInterval(5*1000);
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }
    @Override
    public void onStart(Intent intent, int startId) {
        int start = Service.START_STICKY;
        appendLog(DateFormat.getDateTimeInstance().format(new Date()) + ": Service Started:", com.example.locationservice.Constants.LOG_FILE);

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // TODO Auto-generated method stub
        appendLog(DateFormat.getDateTimeInstance().format(new Date()) + ": Connection to client failed", com.example.locationservice.Constants.LOG_FILE);
        this.stopSelf();

    }

    @Override
    public void onConnected(Bundle arg0) {
        // TODO Auto-generated method stub
        Log.i("info", "Location Client is Connected");
        appendLog(DateFormat.getDateTimeInstance().format(new Date()) + ": Location Client Connectd:", com.example.locationservice.Constants.LOG_FILE);
        //checking for locaton enabled or not
        if(Util.isLocationEnabled(getApplicationContext())){
            //checking for internet available or not
            if(Util.isInternetOn(getApplicationContext())){
                mLocationClient.requestLocationUpdates(mLocationRequest, this);
            }else{
                Log.i("info", "Internet not available");
                appendLog(DateFormat.getDateTimeInstance().format(new Date()) + ": Internet not available", com.example.locationservice.Constants.LOG_FILE);
                this.stopSelf();
            }
        }else{
            Log.i("info", "Location Acess disabled");
            appendLog(DateFormat.getDateTimeInstance().format(new Date()) + ": Location Acess disabled", com.example.locationservice.Constants.LOG_FILE);
            this.stopSelf();
        }
        Log.i("info", "Service Connect status :: " + isServicesConnected());

    }

    @Override
    public void onDisconnected() {
        // TODO Auto-generated method stub
        appendLog(DateFormat.getDateTimeInstance().format(new Date()) + ": Location Client DisConnectd:", com.example.locationservice.Constants.LOG_FILE);
        Log.i("info", "Location Client is DisConnected");

    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        appendLog(DateFormat.getDateTimeInstance().format(new Date()) + ": Location Changed:", com.example.locationservice.Constants.LOG_FILE);
        Log.i("info", "Latitude :: " + latitude);
        Log.i("info", "Longitude :: " + longitude);
        if(Util.isInternetOn(getApplicationContext())){
            //sending location details
            sendLocation(location);
        }else{
            this.stopSelf();
            Log.i("info", "Internet not available");
            appendLog(DateFormat.getDateTimeInstance().format(new Date()) + ": Internet not available", com.example.locationservice.Constants.LOG_FILE);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        Log.i("info", "Service is destroyed");
        mLocationClient.removeLocationUpdates(this);
        appendLog(DateFormat.getDateTimeInstance().format(new Date()) + ": Service Destroyed:", com.example.locationservice.Constants.LOG_FILE);
        super.onDestroy();
    }
    private boolean isServicesConnected() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(LocationService.this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            return true;
        } else {
            return false;
        }
    }
}