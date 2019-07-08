package com.k4dnikov.addpolygon.presentation.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.k4dnikov.addpolygon.R;
import com.k4dnikov.addpolygon.data.model.PointEntity;
import com.k4dnikov.addpolygon.data.model.PolygonEntity;
import com.k4dnikov.addpolygon.data.repository.RepositoryMarkersImpl;
import com.k4dnikov.addpolygon.data.repository.RepositoryPoligonsImpl;
import com.k4dnikov.addpolygon.presentation.presenter.MapPresenter;
import com.k4dnikov.addpolygon.presentation.presenter.Presenter;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ViewMap {

    private GoogleMap mMap;

    private Presenter mPresenter;

    private Button mButtonAddRegion;

    private Button mButtonOk;

    private Button mButtonCancel;

    private Polyline mPolyline;

    private List<Marker> mMarkerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mButtonAddRegion = findViewById(R.id.btn_add_region);

        mButtonOk = findViewById(R.id.btn_ok);

        mButtonCancel = findViewById(R.id.btn_cancel);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Realm.getDefaultInstance().close();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mPresenter = new MapPresenter(this, new RepositoryMarkersImpl(), new RepositoryPoligonsImpl());

        mButtonAddRegion.setOnClickListener(v -> {
            mPresenter.onAddRegionClick();
        });

        mButtonOk.setOnClickListener(v -> {
            mPresenter.onOkClick(mPolyline.getPoints());
        });

        mButtonCancel.setOnClickListener(v -> {
            mPresenter.onCancelClick();
        });

        mPresenter.loadMarkers();

        mPresenter.loadPolygons();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mPresenter.onMapClick(latLng);
            }
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

                List<LatLng> points = mPolyline.getPoints();

                LatLng changingPoint = marker.getPosition();

                if(points.size() == 0){

                }else if(points.size() == 1){
                    points.remove(0);
                }else {

                    int index = -1;

                    for (LatLng latLng: points){
                        index++;
                        System.out.println("TEST point changing lat " + String.valueOf(changingPoint.latitude) + " " + String.valueOf(changingPoint.longitude) + " list " + String.valueOf(latLng.latitude) + " " + String.valueOf(latLng.longitude));
                        if(latLng.latitude == changingPoint.latitude
                                && latLng.longitude == changingPoint.longitude)
                            break;

                    }

                    points.remove(index);

                    System.out.println("TEST point index " + index);

                }


                mPolyline.remove();

                mPolyline = null;

                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.color(getResources().getColor(R.color.colorPolylineStroke));
                polylineOptions.width(10f);

                for(LatLng point : points){
                    polylineOptions.add(point);
                }

                System.out.println("TEST points size" + points.size());

                mPolyline = mMap.addPolyline(polylineOptions);

            }

            @Override
            public void onMarkerDrag(Marker marker) {
                System.out.println("TEST point onMarkerDrag lat " + " list " + String.valueOf(marker.getPosition().latitude) + " " + String.valueOf(marker.getPosition().longitude));

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                System.out.println("TEST point onMarkerDragEnd lat " + " list " + String.valueOf(marker.getPosition().latitude) + " " + String.valueOf(marker.getPosition().longitude));
                mPresenter.onMapClick(marker.getPosition());
            }
        });

    }

    @Override
    public void hideOkButton() {
        mButtonOk.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showOkButton() {
        mButtonOk.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideAddRegionButton() {
        mButtonAddRegion.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showAddRegionButton() {
        mButtonAddRegion.setVisibility(View.VISIBLE);
    }


    @Override
    public void hideCancelButton() {
        mButtonCancel.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showCancelButton() {
        mButtonCancel.setVisibility(View.VISIBLE);
    }

    @Override
    public void addPoint(LatLng latLng) {
        drawLine(latLng);
    }

    private void drawLine(LatLng latLng) {

        List<LatLng> latLngsTemp;

        if (mPolyline == null) {
            latLngsTemp = new ArrayList<>();
        } else {
            latLngsTemp = mPolyline.getPoints();
            mPolyline.remove();
            mPolyline = null;
        }

        latLngsTemp.add(latLng);

        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(getResources().getColor(R.color.colorPolylineStroke));
        polylineOptions.width(10f);

        polylineOptions.jointType(JointType.ROUND);

        for (LatLng ll : latLngsTemp) {
            polylineOptions.add(ll);
        }

        mPolyline = mMap.addPolyline(polylineOptions);

        drawLineVertices(mPolyline);

    }

    private void drawLineVertices(Polyline polyline) {

        if (polyline == null || polyline.getPoints() == null)
            return;

        if (mMarkerList == null)
            mMarkerList = new ArrayList<>();

        clearMarkers(mMarkerList);

        for (LatLng latLng : polyline.getPoints()) {

            Marker marker = mMap.addMarker(new MarkerOptions().position(latLng));
            marker.setDraggable(true);

            mMarkerList.add(marker);
        }

        if (polyline.getPoints().size() > 2)
            mPresenter.onCanSavePolygone();

    }

    private void clearMarkers(List<Marker> markerList) {
        if (markerList == null || markerList.size() == 0)
            return;

        for (Marker marker : markerList) {
            marker.remove();
        }

    }

    @Override
    public void removeTempData() {
        if (mPolyline != null)
            mPolyline.remove();

        clearMarkers(mMarkerList);

        mPolyline = null;
        mMarkerList = null;
    }

    @Override
    public void drawPolygone(List<LatLng> points) {

        if (points == null)
            return;

        removeTempData();

        PolygonOptions polygonOptions = new PolygonOptions();

        polygonOptions.strokeColor(getResources().getColor(R.color.colorPoligonStroke));

        polygonOptions.fillColor(getResources().getColor(R.color.colorPoligonFill));

        polygonOptions.strokeWidth(10f);

        for (LatLng latLng : points) {
            polygonOptions.add(latLng);
        }

        mMap.addPolygon(polygonOptions);

    }

    @Override
    public void showMessage(String error) {
        if(error != null && !error.isEmpty())
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMarkers(List<LatLng> markers) {

        for (LatLng marker : markers) {
            mMap.addMarker(new MarkerOptions().position(marker).draggable(false));
        }

    }

    @Override
    public void showPolygons(List<PolygonEntity> polygons) {

        for (PolygonEntity polygonEntity : polygons) {
            showSinglePolygon(polygonEntity);
        }

    }

    private void showSinglePolygon(PolygonEntity polygonEntity) {

        PolygonOptions polygonOptions = new PolygonOptions();

        polygonOptions.fillColor(getResources().getColor(R.color.colorPoligonFill));

        polygonOptions.strokeColor(getResources().getColor(R.color.colorPoligonFill));

        for (PointEntity pointEntity : polygonEntity.getPointEntities()) {
            polygonOptions.add(new LatLng(pointEntity.getLat(), pointEntity.getLon()));
        }

        mMap.addPolygon(polygonOptions);

    }

}
