package fyp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import fyp.model.CarPark;
import fyp.model.JSONParser;
import fyp.model.RetrieveJson;

public class Map extends FragmentActivity implements OnMapReadyCallback {

    private ArrayList carParksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    public void backToMenu(View view) {
        onBackPressed();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        RetrieveJson retrieveJson = null;
        if (isOnline()) {
            retrieveJson = new RetrieveJson(this);
            String urlStr = "http://data.corkcity.ie/api/action/datastore_search?resource_id=6cc1028e-7388-4bc5-95b7-667a59aa76dc";
            retrieveJson.execute(urlStr);
        }
        JSONObject carParksJson = null;
        if (isOnline()) {
            try {
                assert retrieveJson != null;
                carParksJson = retrieveJson.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            SharedPreferences sharedPref = this.getSharedPreferences("backUp", Context.MODE_PRIVATE);
            try {
                carParksJson = new JSONObject(sharedPref.getString("jsonBackUp", null));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        JSONParser jsonParser = new JSONParser();
        jsonParser.execute(carParksJson);
        try {
            carParksList = jsonParser.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < carParksList.size(); i++) {
            LatLng ll = new LatLng(((CarPark) carParksList.get(i)).getLatitude(), ((CarPark) carParksList.get(i)).getLongitude());
            MarkerOptions marker = new MarkerOptions().position(ll).title(((CarPark) carParksList.get(i)).getName());
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
