package com.example.garbagecleaner.activity;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.garbagecleaner.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationListener locationListener;
    private LocationManager locationManager;

    DatabaseReference databaseReference;

    private final long MIN_TIME = 500; // 1 second
    private final long MIN_DIST = 2; //2 meters
    private LatLng latLng;
    Switch aSwitch;
    Marker marker;
    LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        aSwitch = findViewById(R.id.switch1);
        animationView = findViewById(R.id.load);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Location");
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        CameraUpdate point = CameraUpdateFactory.newLatLngZoom(new LatLng(20.5937, 78.9629), 4.5f);
        // moves camera to coordinates
        mMap.moveCamera(point);
        // animates camera to coordinates
        mMap.animateCamera(point);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Toast.makeText(MapsActivity.this, "Tracking Enabled", Toast.LENGTH_SHORT).show();
                    databaseReference.child(DrawerActivity.WardNo).child("Name").setValue(DrawerActivity.fullName);
                    databaseReference.child(DrawerActivity.WardNo).child("Mobile").setValue(DrawerActivity.mobileNO);
                    databaseReference.child(DrawerActivity.WardNo).child("ID").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    locationListener = new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            if (marker != null) {
                                marker.remove();
                            }
                            try {
                                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                animationView.setVisibility(View.INVISIBLE);
                                marker = mMap.addMarker(new MarkerOptions().position(latLng).title("My Current Position"));

                                // mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17), 2000, null);
                                databaseReference.child(DrawerActivity.WardNo).child("Latitude").setValue(location.getLatitude());

                                databaseReference.child(DrawerActivity.WardNo).child("Longitude").setValue(location.getLongitude());
                            } catch (SecurityException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }

                    };

                    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                    try {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, (android.location.LocationListener) locationListener);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    animationView.setVisibility(View.VISIBLE);
                    locationManager.removeUpdates(locationListener);
                    locationManager = null;
                    databaseReference.child(DrawerActivity.WardNo).removeValue();
                    if(marker!=null)
                        marker.remove();
                    Toast.makeText(MapsActivity.this, "Tracking Disabled", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private static class UiSettings {
        public static void setZoomControlsEnabled(boolean b) {
        }
    }

}
