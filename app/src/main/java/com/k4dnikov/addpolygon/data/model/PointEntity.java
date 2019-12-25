package com.k4dnikov.addpolygon.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PointEntity extends RealmObject {

    @PrimaryKey
    private long id;

    private double lat;

    private double lon;

    public PointEntity() {}

    public void setId(long id) {
        this.id = id;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public long getId() {
        return id;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
