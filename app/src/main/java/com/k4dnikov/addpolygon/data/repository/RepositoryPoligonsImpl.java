package com.k4dnikov.addpolygon.data.repository;


import com.google.android.gms.maps.model.LatLng;
import com.k4dnikov.addpolygon.data.model.PointEntity;
import com.k4dnikov.addpolygon.data.model.PolygonEntity;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class RepositoryPoligonsImpl implements RepositoryPoligons {

    static Realm sRealm;


    public RepositoryPoligonsImpl() {
        sRealm = Realm.getDefaultInstance();
    }

    @Override
    public List<PolygonEntity> getPolygons() {

        RealmResults<PolygonEntity> polygonEntityRealmResults = sRealm.where(PolygonEntity.class).findAllAsync();

        List<PolygonEntity> polygonEntityList = sRealm.copyFromRealm(polygonEntityRealmResults);

        return polygonEntityList;

    }

    @Override
    public void savePolygone(List<LatLng> points) {

        sRealm.executeTransaction(realm -> {

            PolygonEntity polygonEntity = sRealm.createObject(PolygonEntity.class, getPolygonNextKey());

            for (LatLng latLng : points) {
                PointEntity pointEntity = sRealm.createObject(PointEntity.class, getPointNextKey());
                pointEntity.setLat(latLng.latitude);
                pointEntity.setLon(latLng.longitude);
                polygonEntity.getPointEntities().add(pointEntity);
            }

        });


    }

    public long getPolygonNextKey() {
        try {
            Number number = sRealm.where(PolygonEntity.class).max("id");
            if (number != null) {
                return number.longValue() + 1;
            } else {
                return 0;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }

    public long getPointNextKey() {
        try {
            Number number = sRealm.where(PointEntity.class).max("id");
            if (number != null) {
                return number.longValue() + 1;
            } else {
                return 0;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }

}
