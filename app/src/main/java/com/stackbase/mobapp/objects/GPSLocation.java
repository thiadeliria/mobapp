package com.stackbase.mobapp.objects;

import android.location.Location;

public class GPSLocation extends JSONObj {
    private long time;
    private double longitude;
    private double latitude;
    private double altitude;
    private float speed;
    private float bearing;
    private float accuracy;

    public GPSLocation(Location location) {
        if (location != null) {
            this.time = location.getTime();
            this.longitude = location.getLongitude();
            this.latitude = location.getLatitude();
            this.altitude = location.getAltitude();
            this.speed = location.getSpeed();
            this.bearing = location.getBearing();
            this.accuracy = location.getAccuracy();
        }
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getBearing() {
        return bearing;
    }

    public void setBearing(float bearing) {
        this.bearing = bearing;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }
}
