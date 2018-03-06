package fyp.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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