package fyp.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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
import fyp.tasks.LoadCarParks;

public class Map extends FragmentActivity implements OnMapReadyCallback {

    private ArrayList carParksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        carParksList = LoadCarParks.get(this);
    }


    public void backToMenu(View view) {
        onBackPressed();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        ArrayList<CarPark> carParksList = LoadCarParks.get(this);
        for (int i = 0; i < carParksList.size(); i++) {
            LatLng ll = new LatLng(carParksList.get(i).getLatitude(), carParksList.get(i).getLongitude());
            MarkerOptions marker = new MarkerOptions().position(ll).title(carParksList.get(i).getName());
            googleMap.addMarker(marker);

        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(getMidPoint()));
        googleMap.setMinZoomPreference(12.7f);
        googleMap.setMaxZoomPreference(20.0f);
    }

    private LatLng getMidPoint() {
        double lats = 0, lngs = 0;
        for (int i = 0; i < carParksList.size(); i++) {
            lats += ((CarPark) carParksList.get(i)).getLatitude();
            lngs += ((CarPark) carParksList.get(i)).getLongitude();
        }
        return new LatLng(lats / carParksList.size(), lngs / carParksList.size());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
