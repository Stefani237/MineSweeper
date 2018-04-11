package com.example.dell.minesweeper;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private final int RELAX = 0;
    private final int CHALLENGE = 1;
    private final int MASTER = 2;

    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;
    DatabaseHelper mDatabaseHelper;
    private Cursor mCursor;
    private int mDifficulty = 0;
    private RadioGroup radioGroup;
    private RadioButton radioButton;


    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = (MapView) mView.findViewById(R.id.mapView);
        mDatabaseHelper = new DatabaseHelper(getContext());
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(this);
        // selectRecordMap();
        return mView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) mView.findViewById(R.id.mapView);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
         googleMap.addMarker(new MarkerOptions().position(new LatLng(37.4220, 122.0840)).title("MyLocation"));
         CameraPosition cameraPosition = CameraPosition.builder().target(new LatLng(37.4220, 122.0840)).zoom(16).bearing(0).tilt(45).build();
         googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    public void selectRecordMap() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // int selectedButton = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) group.findViewById(checkedId);

                switch (radioButton.getId()) {
                    case R.id.relax_table:
                        mDifficulty = RELAX;
                        break;

                    case R.id.challenge_table:
                        mDifficulty = CHALLENGE;
                        break;

                    case R.id.master_table:
                        mDifficulty = MASTER;
                        break;

                    default:
                        mDifficulty = RELAX;
                }

                mCursor = mDatabaseHelper.getAllData(mDifficulty);


            }

        });
    }

}
