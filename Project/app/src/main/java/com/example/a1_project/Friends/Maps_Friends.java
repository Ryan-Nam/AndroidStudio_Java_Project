package com.example.a1_project.Friends;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.example.a1_project.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class Maps_Friends extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps__friends);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent i = getIntent();
        if( i != null){
            address = i.getStringExtra("Address");
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (address.equals("")) {
            // Add a marker in syd and move camera
            LatLng position = new LatLng(-33, 151);
            mMap.addMarker(new MarkerOptions().position(position).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
            //mMap.setMyLocationEnabled(true);
        } else{
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try{
                List<Address> addresses = geocoder.getFromLocationName(address, 5);
                //first itme in the list
                double latitude = addresses.get(0).getLatitude();
                double longtitude = addresses.get(0).getLongitude();
                //variable position
                LatLng position = new LatLng(latitude,longtitude);
                mMap.addMarker(new MarkerOptions().position(position).title("Marker in " + address));
                //mMap.setMyLocationEnabled(true);
                //Move camera to the user's location and zoom in!
                //12.0f
                //higer number = more detail
                // smaller number = less detail for zoom in..
                //try up to 20
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longtitude), 15.0f));
            } catch (Exception e){
            }
        }
    }
}