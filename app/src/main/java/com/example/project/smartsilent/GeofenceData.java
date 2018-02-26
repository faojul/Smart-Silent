package com.example.project.smartsilent;

import java.util.UUID;

/**
 * Created by Faojul Ahsan on 1/14/2017.
 */

public class GeofenceData {
    private String id;
    private String name;
    private double latitude;
    private double longitude;
    private int radius;

    public GeofenceData() {
    }

    public GeofenceData(String id, String name, double latitude, double longitude, int radius) {

        if (id == null) {
            id = UUID.randomUUID().toString();
        }

        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public String toString() {
        return "GeofenceData{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", radius=" + radius +
                '}';
    }
}
