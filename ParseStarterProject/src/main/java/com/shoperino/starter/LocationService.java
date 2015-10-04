package com.shoperino.starter;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.util.Date;

public class LocationService extends Service implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    LocationRequest mLocationRequest;
    GoogleApiClient  mGoogleApiClient;
    FusedLocationProviderApi api;
    ParseUser current;
    @Override
    public void onCreate() {
        //creating log file in mobile
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(5 * 1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //  mLocationRequest.setFastestInterval(5*1000);
        api = LocationServices.FusedLocationApi;
        current = ParseUser.getCurrentUser();
    }
    @Override
    public void onStart(Intent intent, int startId) {

        api.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // TODO Auto-generated method stub-=
        this.stopSelf();

    }

    @Override
    public void onConnected(Bundle arg0) {
        // TODO Auto-generated method stub
        Log.i("info", "Location Client is Connected");
        //checking for locaton enabled or not

        api.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }



    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        Log.i("info", "Latitude :: " + latitude);
        Log.i("info", "Longitude :: " + longitude);
        if(isOnline()){
            //sending location details
            sendLocation(latitude,longitude);
        }else{
            this.stopSelf();
            Log.i("info", "Internet not available");
        }
    }

    private void sendLocation(double lat, double lon)
    {
        ParseGeoPoint pgp = new ParseGeoPoint(lat,lon);
        current.put("location",pgp);
        current.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null)
                {
                    Log.i("info","Could not update location.");
                }
            }
        });
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

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // put something here
    }

    @Override
    public void onProviderDisabled(String provider) {
        // put something here
    }

    @Override
    public void onProviderEnabled(String provider) {
        // put something here
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}