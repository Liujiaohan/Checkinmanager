package com.liujiaohan.checkinmanager.location;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.List;
import android.Manifest;

/**
 * Created by Liu jiaohan on 2017-07-04.
 */
public class LocationUtil {
    private static LocationManager locationManager;
    private static String locationProvider;

    public static void init(Context context){
        locationManager= (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        List<String> list=locationManager.getProviders(true);

        if (list.contains(LocationManager.GPS_PROVIDER)){
            locationProvider=LocationManager.GPS_PROVIDER;
        }
        else if (list.contains(LocationManager.NETWORK_PROVIDER)){
            locationProvider=LocationManager.NETWORK_PROVIDER;
        }
        else{
            Toast.makeText(context,"请授予GPS权限",Toast.LENGTH_SHORT).show();
        }
    }
    public static Location getLocation(Context context){

        if (locationProvider==null) LocationUtil.init(context);

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        Location location=locationManager.getLastKnownLocation(locationProvider);
        if (location==null){
            locationProvider=LocationManager.NETWORK_PROVIDER;
            location=locationManager.getLastKnownLocation(locationProvider);
        }
        return location;
    }

    public static void requestLocationUpdata(Context context){

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return ;
        }

        if (locationProvider==null) return;
        locationManager.requestLocationUpdates(locationProvider, 3000, 1, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        });
    }
}
