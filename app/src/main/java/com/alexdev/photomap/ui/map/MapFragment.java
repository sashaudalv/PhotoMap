package com.alexdev.photomap.ui.map;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexdev.photomap.R;
import com.alexdev.photomap.ui.interfaces.ReselectableFragment;
import com.alexdev.photomap.utils.Utils;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MapFragment extends Fragment implements ReselectableFragment, OnMapReadyCallback {

    public static final int PERMISSION_REQUEST_ACCESS_COARSE_LOCATION = 1234;
    private final static float ZOOM_WORLD = 1;
    private final static float ZOOM_COUNTRY = 5;
    private final static float ZOOM_CITY = 10;
    private final static float ZOOM_STREET = 15;
    private final static float ZOOM_BUILDINGS = 20;

    @BindView(R.id.map)
    MapView mMapView;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    @Nullable
    private Location mLastLocation;

    public MapFragment() {

    }


    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        boolean isPermissionGranted = Utils.checkAndRequestPermissions(MapFragment.this,
                                PERMISSION_REQUEST_ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION);

                        if (!isPermissionGranted) return;

                        //TODO: deprecated call
                        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        if (savedInstanceState == null) moveMapCameraToCurrentLocation();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        if (savedInstanceState == null) moveMapCameraToCurrentLocation();
                    }
                })
                .addApi(LocationServices.API)
                .build();
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

    private void showPhoto() {
        //TODO
    }

    @Override
    public void onFragmentReselected() {
        moveMapCameraToCurrentLocation();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        boolean isPermissionGranted = Utils.checkAndRequestPermissions(this,
                PERMISSION_REQUEST_ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (!isPermissionGranted) return;

        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mLastLocation == null) mGoogleApiClient.connect();
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
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
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
            //TODO: deprecated call
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            moveMapCameraToCurrentLocation();
            mMap.setMyLocationEnabled(true);
        }
    }

}
