package com.ingwersen.kyle.cs125_project.location;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by kyle on 3/19/2018.
 */

public class LocationManager
{
    // https://stackoverflow.com/questions/28535703/best-way-to-get-user-gps-location-in-background-in-android
    // https://stackoverflow.com/questions/9092134/broadcast-receiver-within-a-service

    private static final String TAG = "CS125-PROJECT-GPS";
    private static final int LOCATION_INTERVAL = 1000 * 60 * 5; // One request per 5 minutes
    private static final float LOCATION_DISTANCE = 0f; // No distance requirement

    private static final Location mLastLocation = new Location(android.location.LocationManager.GPS_PROVIDER);
    private static boolean mStarted = false;
    private static Activity mContext = null;
    private static LocationChangedListener mLocationChangedListener = null;

    public static Location getLocation()
    {
        return mLastLocation;
    }

    public static void start(Activity context, LocationChangedListener listener)
    {
        if (!mStarted)
        {
            mContext = context;
            mLocationChangedListener = listener;

            Log.e(TAG, "Requesting Permissions.");
            if (checkPermissions())
            {
                Log.e(TAG, "Starting Service.");
                mContext.startService(new Intent(context, LocationService.class));
                mStarted = true;
            }
            else
            {
                Log.e(TAG, "Failed to Obtain Permissions.");
            }
        }
    }
    // Stop()?

    private static boolean checkPermissions()
    {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions( mContext,
                    new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                    99 );
            return ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    public static class LocationService extends Service
    {
        private android.location.LocationManager mLocationManager = null;

        public LocationService() { super(); }

        private class LocationListener implements android.location.LocationListener
        {
            public LocationListener(String provider)
            {
                Log.e(TAG, "LocationListener " + provider);
            }

            @Override
            public void onLocationChanged(Location location)
            {
                Log.e(TAG, "onLocationChanged: " + location);
                mLastLocation.set(location);
                mLocationChangedListener.onLocationChanged(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras)
            {
                Log.e(TAG, "onStatusChanged: " + provider);
            }

            @Override
            public void onProviderDisabled(String provider)
            {
                Log.e(TAG, "onProviderDisabled: " + provider);
            }

            @Override
            public void onProviderEnabled(String provider)
            {
                Log.e(TAG, "onProviderEnabled: " + provider);
            }

        }

        LocationListener mLocationListener = new LocationListener(android.location.LocationManager.GPS_PROVIDER);

        @Override
        public IBinder onBind(Intent arg0)
        {
            return null;
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId)
        {
            Log.e(TAG, "onStartCommand");
            super.onStartCommand(intent, flags, startId);
            return START_STICKY;
        }

        @Override
        public void onCreate()
        {
            Log.e(TAG, "onCreate");
            initializeLocationManager();
            try {
                mLocationManager.requestLocationUpdates(
                        android.location.LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                        mLocationListener);
            } catch (java.lang.SecurityException ex) {
                Log.i(TAG, "fail to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                Log.d(TAG, "gps provider does not exist " + ex.getMessage());
            }

            IntentFilter filter = new IntentFilter();
            filter.addAction(android.location.LocationManager.KEY_LOCATION_CHANGED);
        }

        @Override
        public void onDestroy()
        {
            Log.e(TAG, "onDestroy");
            super.onDestroy();
            if (mLocationManager != null) {
                try {
                    mLocationManager.removeUpdates(mLocationListener);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }

        private void initializeLocationManager() {
            Log.e(TAG, "initializeLocationManager");
            if (mLocationManager == null) {
                mLocationManager = (android.location.LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            }
        }

    }

    public interface LocationChangedListener
    {
        void onLocationChanged(Location location);
    }
}
