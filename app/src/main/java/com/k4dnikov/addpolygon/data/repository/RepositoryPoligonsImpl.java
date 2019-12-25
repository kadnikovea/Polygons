package com.k4dnikov.addpolygon.data.repository;

import com.google.android.gms.maps.model.LatLng;
import com.k4dnikov.addpolygon.data.model.PointEntity;
import com.k4dnikov.addpolygon.data.model.PolygonEntity;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class RepositoryPoligonsImpl implements RepositoryPoligons {

    public RepositoryPoligonsImpl() {}

    @Override
    public List<PolygonEntity> getPolygons() {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.beginTransaction();
            RealmResults<PolygonEntity> polygonEntityRealmResults = realm.where(PolygonEntity.class).findAllAsync();
            realm.commitTransaction();
            realm.beginTransaction();
            List<PolygonEntity> polygonEntityList = realm.copyFromRealm(polygonEntityRealmResults);
            realm.commitTransaction();
            realm.close();
            return polygonEntityList;
        } catch(Exception e) {
            if(realm.isInTransaction()) {
                realm.cancelTransaction();
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public void savePolygone(List<LatLng> points) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realmLocal -> {
            PolygonEntity polygonEntity = realmLocal.createObject(PolygonEntity.class, getPolygonNextKey(realm));
            for (LatLng latLng : points) {
                PointEntity pointEntity = realmLocal.createObject(PointEntity.class, getPointNextKey(realm));
                pointEntity.setLat(latLng.latitude);
                pointEntity.setLon(latLng.longitude);
                polygonEntity.getPointEntities().add(pointEntity);
            }
        });
        realm.close();
    }

    private long getPolygonNextKey(Realm realm) {
        try {
            Number number = realm.where(PolygonEntity.class).max("id");
            if (number != null) {
                return number.longValue() + 1;
            } else {
                return 0;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }

    private long getPointNextKey(Realm realm) {
        try {
            Number number = realm.where(PointEntity.class).max("id");
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
