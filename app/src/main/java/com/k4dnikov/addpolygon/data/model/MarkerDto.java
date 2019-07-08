package com.k4dnikov.addpolygon.data.model;

import com.google.gson.annotations.SerializedName;

public class MarkerDto {

    private long id;

    @SerializedName("lat")
    private double latitude;

    @SerializedName("lon")
    private double longitude;

    public MarkerDto(){}

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
