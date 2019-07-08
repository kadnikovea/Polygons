package com.k4dnikov.addpolygon.data.retrofit;

import com.k4dnikov.addpolygon.data.model.MarkerDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MarkersService {

    @GET(".")
    Call<List<MarkerDto>> getMarkers();

}
