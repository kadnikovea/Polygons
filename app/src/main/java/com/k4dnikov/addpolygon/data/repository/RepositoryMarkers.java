package com.k4dnikov.addpolygon.data.repository;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public interface RepositoryMarkers {

    void getMarkers(OnMarkersResponce onMarkersResponce);

    interface OnMarkersResponce{

        void onResponce(List<LatLng> latLngs);

        void onFailure(String error);

    }

}
