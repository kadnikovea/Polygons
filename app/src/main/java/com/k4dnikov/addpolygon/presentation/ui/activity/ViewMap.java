package com.k4dnikov.addpolygon.presentation.ui.activity;

import com.google.android.gms.maps.model.LatLng;
import com.k4dnikov.addpolygon.data.model.PolygonEntity;

import java.util.List;

public interface ViewMap {

    void hideOkButton();

    void showOkButton();

    void hideAddRegionButton();

    void showAddRegionButton();

    void showMarkers(List<LatLng> markers);

    void showPolygons(List<PolygonEntity> polygons);

    void hideCancelButton();

    void showCancelButton();

    void addPoint(LatLng latLng);

    void removeTempData();

    void drawPolygone(List<LatLng> points);

    void showMessage(String error);
}
