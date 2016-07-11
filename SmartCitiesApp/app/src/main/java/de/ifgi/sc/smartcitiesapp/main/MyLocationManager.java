package de.ifgi.sc.smartcitiesapp.main;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

import de.ifgi.sc.smartcitiesapp.interfaces.LocationChangedListener;

/**
 * Created by Maurin on 09.07.2016.
 */
public class MyLocationManager {

    private static MyLocationManager instance;

    private LatLng userLocation;
    private Context mContext;
    private LocationChangedListener lcl;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;

    private MyLocationManager() {
        // constructor hidden because this is a singleton
    }

    public static void initInstance(Context c) {
        if (instance == null) {
            // Create the instance
            instance = new MyLocationManager();
            instance.mContext = c;
            instance.userLocation = null;
            instance.mLocationManager = (LocationManager)
                    instance.mContext.getSystemService(Context.LOCATION_SERVICE);
        }
    }

    public synchronized void setLocationChangedListener(LocationChangedListener lcl) throws SecurityException {
        instance.lcl = lcl;
        instance.mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                instance.userLocation = new LatLng(
                        location.getLatitude(),
                        location.getLongitude()
                );
                instance.lcl.onLocationChanged(
                        new LatLng(location.getLatitude(),
                                location.getLongitude())
                );
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
        };
        instance.mLocationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                15000,          // update every 15s
                0,
                instance.mLocationListener
        );

        // Also, check for the last known location:
        LocationManager lm = (LocationManager) instance.mContext.getSystemService(Context.LOCATION_SERVICE);

        try {
            Location l = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            instance.userLocation = new LatLng(
                    l.getLatitude(),
                    l.getLongitude()
            );
            // callback to the LocationChangedListener:
            instance.lcl.onLocationChanged(instance.userLocation);
        } catch (SecurityException se) {
        }

        try {
            Location l = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            instance.userLocation = new LatLng(
                    l.getLatitude(),
                    l.getLongitude()
            );
            // callback to the LocationChangedListener:
            instance.lcl.onLocationChanged(instance.userLocation);
        } catch (SecurityException se) {
        }
    }

    public static MyLocationManager getInstance() {
        return instance;
    }

    public synchronized LatLng getUserLocation() throws NoLocationKnownException {
        if (instance.userLocation == null) {
            throw new NoLocationKnownException();
        }
        return instance.userLocation;
    }

}
