package fyp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void goToMaps(View view) {
        startActivity(new Intent(this, Map.class));
    }

    public void goToOptions(View view) {
        startActivity(new Intent(this, Options.class));
    }

    public void goToCarParksList(View view) {
        startActivity(new Intent(this, CarParks.class));
    }

    public void goToTrafficEstimations(View view) {
        startActivity(new Intent(this, TrafficDirection.class));
    }

    public void goToReservation(View view) {
        startActivity(new Intent(this, Reservation.class));
    }

    public void goToRecommendPark(View view) {
        startActivity(new Intent(this, Recommend.class));
    }

    public void goToLogin(View view) {
        startActivity(new Intent(this, Login.class));
    }

    @Override
    public void onBackPressed() {

    }
}