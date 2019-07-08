package com.k4dnikov.addpolygon.data.repository;


import com.google.android.gms.maps.model.LatLng;
import com.k4dnikov.addpolygon.data.model.PolygonEntity;

import java.util.List;

public interface RepositoryPoligons {

    List<PolygonEntity> getPolygons();

    void savePolygone(List<LatLng> points);

}
