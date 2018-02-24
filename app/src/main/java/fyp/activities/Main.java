package fyp.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;

import fyp.model.AddTrafficStat;
import fyp.model.CarPark;
import fyp.model.LoadCarParks;

public class Main extends AppCompatActivity {
    private ArrayList<CarPark> carParksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        carParksList = LoadCarParks.get(this);

        Intent intent = new Intent(this, AddTrafficStat.class);
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < carParksList.size(); i++) {
            names.add(carParksList.get(i).getName());
        }
        intent.putStringArrayListExtra("names", names);
        startService(intent);
    }


    public void goToMaps(View view) {
        Intent intent = new Intent(this, Map.class);
        startActivity(intent);
    }

    public void goToOptions(View view) {
        Intent intent = new Intent(this, Options.class);
        startActivity(intent);
    }

    public void goToCarParksList(View view) {
        Intent intent = new Intent(this, CarParks.class);
        startActivity(intent);
    }

    public void goToTrafficEstimations(View view) {
        Intent intent = new Intent(this, Traffic.class);
        startActivity(intent);
    }

    public void goToReservation(View view) {
        Intent intent = new Intent(this, Reservation.class);
        startActivity(intent);
    }

    public void goToRecommendPark(View view) {
        Intent intent = new Intent(this, Recommend.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }

    public void goToLogin(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}