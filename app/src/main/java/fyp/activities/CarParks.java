package fyp.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import fyp.model.CarPark;
import fyp.model.LoadCarParks;

public class CarParks extends AppCompatActivity {
    private ArrayList<CarPark> carParksList;
    private ArrayList<TextView> vwCarParkNames;
    private ArrayList<TextView> vwCarParkSpaces;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_parks);
        try {
            carParksList = LoadCarParks.get(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assignTextViews();
        for (int i = 0; i < carParksList.size(); i++) {
            vwCarParkNames.get(i).setText(carParksList.get(i).getName());
            if (isOnline()) {
                if (carParksList.get(i).isOpen()) {
                    if (!carParksList.get(i).isFull()) {
                        vwCarParkSpaces.get(i).setText(String.valueOf(carParksList.get(i).getFreeSpaces()));
                    } else {
                        vwCarParkSpaces.get(i).setText(getString(R.string.full));
                    }
                } else {
                    vwCarParkSpaces.get(i).setText(getString(R.string.closed));
                }
            } else {
                vwCarParkSpaces.get(i).setText(getString(R.string.unavailable));
            }
            final int finalI = i;
            vwCarParkNames.get(i).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    goToCarPark(carParksList.get(finalI).getName());
                }
            });
            vwCarParkSpaces.get(i).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    goToCarPark(carParksList.get(finalI).getName());
                }
            });
        }
    }


    public void refresh(View view) {
        finish();
        startActivity(getIntent());
    }

    public void goToCarPark(String carParkName) {
        Intent intent = new Intent(this, CarParkDetails.class);
        intent.putExtra("carParkName", carParkName);
        startActivity(intent);
    }

    public void backToMenu(View view) {
        onBackPressed();
    }

    private void assignTextViews() {
        vwCarParkNames = new ArrayList<>();
        vwCarParkNames.add((TextView) findViewById(R.id.btn0));
        vwCarParkNames.add((TextView) findViewById(R.id.btn1));
        vwCarParkNames.add((TextView) findViewById(R.id.btn2));
        vwCarParkNames.add((TextView) findViewById(R.id.btn3));
        vwCarParkNames.add((TextView) findViewById(R.id.btn4));
        vwCarParkNames.add((TextView) findViewById(R.id.btn5));
        vwCarParkNames.add((TextView) findViewById(R.id.btn6));
        vwCarParkNames.add((TextView) findViewById(R.id.btn7));
        vwCarParkSpaces = new ArrayList<>();
        vwCarParkSpaces.add((TextView) findViewById(R.id.txtvw0));
        vwCarParkSpaces.add((TextView) findViewById(R.id.txtvw1));
        vwCarParkSpaces.add((TextView) findViewById(R.id.txtvw2));
        vwCarParkSpaces.add((TextView) findViewById(R.id.txtvw3));
        vwCarParkSpaces.add((TextView) findViewById(R.id.txtvw4));
        vwCarParkSpaces.add((TextView) findViewById(R.id.txtvw5));
        vwCarParkSpaces.add((TextView) findViewById(R.id.txtvw6));
        vwCarParkSpaces.add((TextView) findViewById(R.id.txtvw7));
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
    }
}
