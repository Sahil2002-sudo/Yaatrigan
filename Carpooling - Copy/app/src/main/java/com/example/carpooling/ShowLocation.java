package com.example.carpooling;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ShowLocation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String lati,longi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Bundle b1=getIntent().getExtras();
        Toast.makeText(ShowLocation.this,"Lati: "+b1.getString("lati"),Toast.LENGTH_LONG).show();
        lati= b1.getString("lati");
        longi= b1.getString("longi");
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(Float.valueOf(lati),Float.valueOf(longi));
        //LatLng sydney = new LatLng(21.42,78.38);

        mMap.addMarker(new MarkerOptions().position(sydney).title("Pickup point"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
