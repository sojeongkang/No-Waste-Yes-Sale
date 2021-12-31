package com.example.myhackaton.location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by user on 2016-04-18.
 */
public interface AltitudeListener {
    void onResult(LatLng location);
}
