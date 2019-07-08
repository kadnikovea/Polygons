package com.k4dnikov.addpolygon.data.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PolygonEntity extends RealmObject {

    @PrimaryKey
    private long id;

    private RealmList<PointEntity> mPointEntities;

    public PolygonEntity() {
    }

    public long getId() {
        return id;
    }

    public RealmList<PointEntity> getPointEntities() {
        return mPointEntities;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPointEntities(RealmList<PointEntity> pointEntities) {
        mPointEntities = pointEntities;
    }
}
