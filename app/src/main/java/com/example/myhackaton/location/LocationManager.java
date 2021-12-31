package com.example.myhackaton.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;

import androidx.core.app.ActivityCompat;

import com.example.myhackaton.L;
import com.google.android.gms.maps.model.LatLng;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class LocationManager implements android.location.LocationListener {
    public static final int LOCATION_HANDLER = 254;
    private static LocationMode mLocationMode;
    private AltitudeItem mAltitudeItem;
    private Context mContext;
    private android.location.LocationManager mLocationManger;
    private boolean mIsLocation = false;
    private AltitudeListener mAltitudeListener;
    public Handler mLocationHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case LOCATION_HANDLER:
                    L.d("isLocation : " + mIsLocation);
                    if (!mIsLocation) {
                        removeLocation();
                        if (mAltitudeListener != null) {
                            mAltitudeListener.onResult(null);
                        }
                    }
                    mIsLocation = false;
                    mLocationHandler.removeMessages(LOCATION_HANDLER);
                    break;
            }
        }
    };
    private long mDelayMils = 1000 * 20;

    public LocationManager(Activity activity) {
        this.mContext = activity;
        mLocationManger = (android.location.LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
    }

    public static LocationMode getLocationMode() {
        return mLocationMode;
    }

    public void setLocationMode(LocationMode mode) {
        mLocationMode = mode;

        switch (mode) {
            case ALTITUDE:
                if (mAltitudeItem == null) {
                    mAltitudeItem = new AltitudeItem();
                }
                mDelayMils = 1000 * 20;
                break;

            default:
                break;

        }
    }

    public void registerAltitudeListener(AltitudeListener listener) {
        this.mAltitudeListener = listener;
    }

    public void unRegisterAltitudeListener() {
        this.mAltitudeListener = null;
    }

    public void startLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            L.w("not granted permission..ACCESS_FINE_LOCATION / ACCESS_COARSE_LOCATION");
            getLocationPermission((Activity)mContext);
            return;
        }

        L.i("mLocationManger = " + mLocationManger);
        if (mLocationManger != null) {
            mLocationManger.requestLocationUpdates(android.location.LocationManager.NETWORK_PROVIDER, 0, 0, this, Looper.getMainLooper());
            mLocationHandler.sendEmptyMessageDelayed(LOCATION_HANDLER, mDelayMils);
        }
    }
    private void getLocationPermission(Activity activity) {
        Dexter.withActivity(activity).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                startLocationUpdate();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

            }
        }).check();
    }
    @Override
    public void onLocationChanged(Location location) {
        if (location == null) {
            return;
        }
        removeLocation();
        if (mAltitudeListener != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mAltitudeListener.onResult(latLng);
        }
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

    public void onAltitudeLocationChanged(Location location) {

    }

    public void removeLocation() {
        if (mLocationManger != null)
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                L.w("not granted permission..ACCESS_FINE_LOCATION / ACCESS_COARSE_LOCATION");
                return;
            }

        mLocationManger.removeUpdates(this);

        mLocationHandler.removeMessages(LOCATION_HANDLER);
    }

    public enum LocationMode {NONE, ALTITUDE}

}
