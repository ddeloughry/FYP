package fyp.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import fyp.model.CarPark;
import fyp.model.LoadCarParks;

public class Map extends FragmentActivity implements OnMapReadyCallback {

    private ArrayList<CarPark> carParksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            carParksList = LoadCarParks.get(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < carParksList.size(); i++) {
            LatLng ll = new LatLng(carParksList.get(i).getLatitude(), carParksList.get(i).getLongitude());
            MarkerOptions marker = new MarkerOptions().position(ll).title(carParksList.get(i).getName());
            googleMap.addMarker(marker);

        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(getMidPoint()));
        googleMap.setMinZoomPreference(12.7f);
        googleMap.setMaxZoomPreference(20.0f);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            googleMap.setMyLocationEnabled(false);
        }
        googleMap.setMyLocationEnabled(true);
    }


    private LatLng getMidPoint() {
        double lats = 0, lngs = 0;
        for (int i = 0; i < carParksList.size(); i++) {
            lats += carParksList.get(i).getLatitude();
            lngs += carParksList.get(i).getLongitude();
        }
        return new LatLng(lats / carParksList.size(), lngs / carParksList.size());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
    }

    public void backToMenu(View view) {
        onBackPressed();
    }


}
