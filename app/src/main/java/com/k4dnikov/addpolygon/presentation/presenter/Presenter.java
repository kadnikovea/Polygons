package com.k4dnikov.addpolygon.presentation.presenter;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public interface Presenter {

    void onAddRegionClick();

    void onOkClick(List<LatLng> points);

    void loadMarkers();

    void loadPolygons();

    void onMapClick(LatLng latLng);

    void onCancelClick();

    void onCanSavePolygone();
}
