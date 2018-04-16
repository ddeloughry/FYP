package fyp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import fyp.model.CarPark;
import fyp.model.LoadCarParks;

public class ChooseParkTraffic extends AppCompatActivity {
    private ArrayList<CarPark> carParksList;
    private ArrayList<TextView> vwCarParkNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_park_traffic);
        TextView viewDirection = findViewById(R.id.viewDirection);
        viewDirection.setText(String.valueOf(viewDirection.getText()).concat(": ").concat(getIntent().getStringExtra("direction")));
        try {
            carParksList = LoadCarParks.get(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assignTextViews();
        for (int i = 0; i < carParksList.size(); i++) {
            vwCarParkNames.get(i).setText(carParksList.get(i).getName());
            final int finalI = i;
            vwCarParkNames.get(i).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    goToCarPark(carParksList.get(finalI).getName());
                }
            });
        }
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
    }

    public void goToCarPark(String carParkName) {
        Intent intent = new Intent(this, ViewParkTraffic.class);
        intent.putExtra("carParkName", carParkName);
        intent.putExtra("direction", getIntent().getStringExtra("direction"));
        startActivity(intent);
    }

    public void backToMenu(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, TrafficDirection.class));
    }
}
