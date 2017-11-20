package com.alexdev.photomap.ui.map;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alexdev.photomap.App;
import com.alexdev.photomap.R;
import com.alexdev.photomap.models.Photo;
import com.alexdev.photomap.network.NetworkManager;
import com.alexdev.photomap.network.callbacks.PhotosLoadListener;
import com.alexdev.photomap.ui.interfaces.ReselectableFragment;
import com.alexdev.photomap.ui.photo.PhotoViewerActivity;
import com.alexdev.photomap.utils.Utils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MapFragment extends Fragment implements ReselectableFragment, OnMapReadyCallback, PhotosLoadListener {

    private static final int PERMISSION_REQUEST_ACCESS_COARSE_LOCATION = 111;
    private static final String PHOTOS_SAVED_STATE = "photos_saved_state";
    private static final String LAST_LOAD_POINT_SAVED_STATE = "last_load_point_saved_state";
    private static final String LAST_LOAD_ZOOM_SAVED_STATE = "last_load_zoom_saved_state";
    private static final String LAST_LOAD_RADIUS_SAVED_STATE = "last_load_radius_saved_state";
    private final static float ZOOM_WORLD = 1;
    private final static float ZOOM_COUNTRY = 5;
    private final static float ZOOM_CITY = 10;
    private final static float ZOOM_STREET = 15;
    private final static float ZOOM_BUILDINGS = 20;

    @BindView(R.id.map)
    MapView mMapView;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    @Nullable
    private Location mLastLocation;

    private final List<Photo> mActualPhotos = new ArrayList<>();
    private final Map<Marker, Photo> mActualMarkers = new HashMap<>();

    private LatLng mLastLoadPoint;
    private float mLastLoadZoomLevel;
    private int mLastLoadRadius;

    @Inject
    NetworkManager mNetworkManager;

    public MapFragment() {

    }

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get(getActivity().getApplicationContext()).getAppComponent().inject(this);

        if (savedInstanceState != null && savedInstanceState.containsKey(PHOTOS_SAVED_STATE)) {
            mActualPhotos.addAll(savedInstanceState.getParcelableArrayList(PHOTOS_SAVED_STATE));
            mLastLoadPoint = savedInstanceState.getParcelable(LAST_LOAD_POINT_SAVED_STATE);
            mLastLoadZoomLevel = savedInstanceState.getFloat(LAST_LOAD_ZOOM_SAVED_STATE);
            mLastLoadRadius = savedInstanceState.getInt(LAST_LOAD_RADIUS_SAVED_STATE);
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        boolean isPermissionGranted = Utils.checkAndRequestPermissions(MapFragment.this,
                PERMISSION_REQUEST_ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (savedInstanceState != null || !isPermissionGranted) return;

        mFusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            mLastLocation = location;
            moveMapCameraToCurrentLocation();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
        return view;
    }

    private void moveMapCamera(@NonNull Location location, float zoom) {
        LatLng coordinates = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
    }

    private void moveMapCameraToCurrentLocation() {
        if (mMap == null) return;
        if (mLastLocation == null) {
            mMap.animateCamera(CameraUpdateFactory.zoomTo(ZOOM_WORLD));
        } else {
            moveMapCamera(mLastLocation, ZOOM_CITY);
        }
    }

    private void openPhotoInPhotoViewer(Photo photo) {
        Intent intent = new Intent(getContext(), PhotoViewerActivity.class);
        intent.putExtra(PhotoViewerActivity.EXTRA_PHOTO, photo);
        startActivity(intent);
    }

    @Override
    public void onFragmentReselected() {
        moveMapCameraToCurrentLocation();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnCameraIdleListener(this::onMapMoveEnd);
        mMap.setInfoWindowAdapter(new ImageMapInfoWindowAdapter(getContext()));
        mMap.setOnInfoWindowClickListener(marker -> openPhotoInPhotoViewer((Photo) marker.getTag()));
        showMarkers(mActualPhotos);

        boolean isPermissionGranted = Utils.checkAndRequestPermissions(this,
                PERMISSION_REQUEST_ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (!isPermissionGranted) return;

        initGoogleMapForGrantedPermission();
    }

    private void onMapMoveEnd() {
        float currentMapZoom = mMap.getCameraPosition().zoom;
        if (mLastLoadPoint == null) {
            if (currentMapZoom >= ZOOM_CITY) loadPhotos();
            return;
        }
        LatLng currentCenter = mMap.getCameraPosition().target;
        int distanceBetweenCenterAndLoadPoint = (int) SphericalUtil.computeDistanceBetween(currentCenter, mLastLoadPoint);
        if (currentMapZoom >= ZOOM_CITY
                && (currentMapZoom < mLastLoadZoomLevel || distanceBetweenCenterAndLoadPoint > mLastLoadRadius)) {
            loadPhotos();
        }
    }

    private void loadPhotos() {
        if (mMap != null) {
            actualizeMarkers();
            mLastLoadRadius = getRadiusForCurrentZoomLevel();
            mLastLoadPoint = mMap.getCameraPosition().target;
            mLastLoadZoomLevel = mMap.getCameraPosition().zoom;
            mNetworkManager.loadUniquePhotosForLocation(mLastLoadPoint, mLastLoadRadius,
                    mActualPhotos, this);
        }
    }

    @Override
    public void onPhotosLoadComplete(List<Photo> photoList) {
        showMarkers(photoList);
        mActualPhotos.addAll(photoList);
    }

    @Override
    public void onPhotosLoadError() {
        Toast.makeText(getContext(), R.string.toast_network_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean isListenerVisible() {
        return isVisible();
    }

    private void showMarkers(List<Photo> photos) {
        for (Photo photo : photos) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(photo.getLatLng());
            Marker marker = mMap.addMarker(markerOptions);
            marker.setTag(photo);
            mActualMarkers.put(marker, photo);
        }
    }

    @SuppressLint("MissingPermission")
    private void initGoogleMapForGrantedPermission() {
        mMap.setMyLocationEnabled(true);
    }

    private int getRadiusForCurrentZoomLevel() {
        LatLng center = mMap.getProjection().getVisibleRegion().latLngBounds.getCenter();
        LatLng leftCorner = mMap.getProjection().getVisibleRegion().farLeft;
        return (int) SphericalUtil.computeDistanceBetween(center, leftCorner);
    }

    private void actualizeMarkers() {
        LatLngBounds visibleBounds = mMap.getProjection().getVisibleRegion().latLngBounds;
        Iterator<Map.Entry<Marker, Photo>> iterator = mActualMarkers.entrySet().iterator();
        for (Map.Entry<Marker, Photo> entry; iterator.hasNext(); ) {
            entry = iterator.next();
            if (!visibleBounds.contains(entry.getKey().getPosition())) {
                iterator.remove();
                entry.getKey().remove();
            }
        }
        mActualPhotos.clear();
        mActualPhotos.addAll(mActualMarkers.values());
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
        outState.putParcelableArrayList(PHOTOS_SAVED_STATE, (ArrayList<Photo>) mActualPhotos);
        outState.putParcelable(LAST_LOAD_POINT_SAVED_STATE, mLastLoadPoint);
        outState.putFloat(LAST_LOAD_ZOOM_SAVED_STATE, mLastLoadZoomLevel);
        outState.putInt(LAST_LOAD_RADIUS_SAVED_STATE, mLastLoadRadius);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_ACCESS_COARSE_LOCATION
                && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                mLastLocation = location;
                moveMapCameraToCurrentLocation();
            });
            initGoogleMapForGrantedPermission();
        }
    }

}
