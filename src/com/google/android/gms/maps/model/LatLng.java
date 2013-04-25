package com.google.android.gms.maps.model;

/**
 * Created by IntelliJ IDEA.
 * User: Lee
 * Date: 13-4-25
 * Time: 下午7:33
 */
public class LatLng {
    public  double latitude;
    public  double longitude;

    public LatLng() {
    }

    public LatLng(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
