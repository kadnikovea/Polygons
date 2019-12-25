package com.k4dnikov.addpolygon.presentation.presenter;

import com.google.android.gms.maps.model.LatLng;
import com.k4dnikov.addpolygon.data.model.PolygonEntity;
import com.k4dnikov.addpolygon.data.repository.RepositoryMarkers;
import com.k4dnikov.addpolygon.data.repository.RepositoryPoligons;
import com.k4dnikov.addpolygon.presentation.ui.activity.ViewMap;

import java.util.List;

public class MapPresenter implements Presenter {

    private ViewMap mViewMap;

    private RepositoryMarkers mRepositoryMarkers;

    private RepositoryPoligons mRepositoryPolygons;

    private Mode mMode;

    public MapPresenter(ViewMap viewMap, RepositoryMarkers repositoryMarkers, RepositoryPoligons repositoryPolygons) {
        mRepositoryMarkers = repositoryMarkers;
        mRepositoryPolygons = repositoryPolygons;
        this.mMode = Mode.STANDART_MODE;
        this.mViewMap = viewMap;
        initView();
    }

    private void initView() {
        mViewMap.showAddRegionButton();
        mViewMap.hideOkButton();
        mViewMap.hideCancelButton();
    }


    @Override
    public void onAddRegionClick() {
        mMode = Mode.ADD_REGION_MODE;
        mViewMap.hideAddRegionButton();
        mViewMap.showCancelButton();
    }

    @Override
    public void onOkClick(List<LatLng> points) {
        mMode = Mode.STANDART_MODE;
        mViewMap.hideCancelButton();
        mViewMap.hideOkButton();
        mViewMap.showAddRegionButton();
        mViewMap.drawPolygone(points);
        mRepositoryPolygons.savePolygone(points);
    }

    @Override
    public void onCancelClick() {
        mMode = Mode.STANDART_MODE;
        mViewMap.hideOkButton();
        mViewMap.hideCancelButton();
        mViewMap.showAddRegionButton();
        mViewMap.removeTempData();
    }

    @Override
    public void onCanSavePolygone() {
        mViewMap.showOkButton();
    }

    @Override
    public void loadMarkers() {

        mRepositoryMarkers.getMarkers(new RepositoryMarkers.OnMarkersResponce() {
            @Override
            public void onResponce(List<LatLng> markers) {
                if (markers == null){
                    return;
                }
                mViewMap.showMarkers(markers);
            }

            @Override
            public void onFailure(String error) {
                mViewMap.showMessage(error);
            }
        });




    }

    @Override
    public void loadPolygons() {
        List<PolygonEntity> polygons = mRepositoryPolygons.getPolygons();
        if (polygons == null){
            return;
        }
        mViewMap.showPolygons(polygons);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (mMode == Mode.ADD_REGION_MODE) {
            mViewMap.addPoint(latLng);
        }
    }

    enum Mode {
        STANDART_MODE,
        ADD_REGION_MODE;
    }
}
