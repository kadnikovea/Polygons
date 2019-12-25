package com.k4dnikov.addpolygon.data.repository;

import com.google.android.gms.maps.model.LatLng;
import com.k4dnikov.addpolygon.App;
import com.k4dnikov.addpolygon.data.model.MarkerDto;
import com.k4dnikov.addpolygon.data.retrofit.MarkersService;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepositoryMarkersImpl implements RepositoryMarkers {

    private MarkersService mMarkersService;

    public RepositoryMarkersImpl() {
        mMarkersService = App.getRetrofit().create(MarkersService.class);
    }

    @Override
    public void getMarkers(OnMarkersResponce onMarkersResponce) {
        Call<List<MarkerDto>> call = mMarkersService.getMarkers();
        call.enqueue(new Callback<List<MarkerDto>>() {
            @Override
            public void onResponse(Call<List<MarkerDto>> call, Response<List<MarkerDto>> response) {
                if (response != null && response.body() != null) {
                    List<LatLng> latLngs = mapperMarker(response.body());
                    onMarkersResponce.onResponce(latLngs);
                }
            }

            @Override
            public void onFailure(Call<List<MarkerDto>> call, Throwable t) {
                onMarkersResponce.onFailure(t.getMessage());
            }
        });
        List<LatLng> markers = new ArrayList<>();
        for (int i = 0; i < 100; i += 10) {
            markers.add(new LatLng(0, i));
        }
    }

    private List<LatLng> mapperMarker(List<MarkerDto> body) {
        List<LatLng> latLngs = new ArrayList<>();
        for (MarkerDto markerDto : body) {
            latLngs.add(new LatLng(markerDto.getLatitude(), markerDto.getLongitude()));
        }
        return latLngs;
    }
}
