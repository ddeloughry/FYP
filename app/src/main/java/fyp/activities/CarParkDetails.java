package fyp.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import fyp.model.CarPark;
import fyp.model.LoadCarParks;
import fyp.model.ParkDateTime;

public class CarParkDetails extends AppCompatActivity {
    private CarPark selectedCarPark;
    private ArrayList<CarPark> carParksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_park_details);

        try {
            carParksList = LoadCarParks.get(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (CarPark carPark : carParksList) {
            if (carPark.getName().equalsIgnoreCase(getIntent().getStringExtra("carParkName"))) {
                selectedCarPark = carPark;
            }
        }
        Button reserveButton = findViewById(R.id.reserveBtn);
        if (selectedCarPark.isFull() || !(isOnline()) || !(selectedCarPark.isOpen()) || FirebaseAuth.getInstance().getCurrentUser() == null) {
            reserveButton.setEnabled(false);
        }
        TextView vwCarParkName = findViewById(R.id.viewName);
        TextView vwFreeSpaces = findViewById(R.id.viewFreeSpaces);
        TextView vwTotalSpaces = findViewById(R.id.viewTotalSpaces);
        TextView vwLastUpdated = findViewById(R.id.viewwLastUpdated);
        TextView vwVehicleHeight = findViewById(R.id.vwVehicleHeight);
        TextView vwPrice = findViewById(R.id.vwPrice);
        vwPrice.setText(selectedCarPark.getPrice());
        Button vwTimes = findViewById(R.id.viewTimes);
        vwCarParkName.setText(selectedCarPark.getName());
        if (isOnline() && selectedCarPark.isOpen()) {
            vwFreeSpaces.setText(String.valueOf(selectedCarPark.getFreeSpaces()));
        } else if (!selectedCarPark.isOpen()) {
            vwFreeSpaces.setText(getString(R.string.closed));
        } else {
            vwFreeSpaces.setText(getString(R.string.unavailable));
        }
        vwTotalSpaces.setText(String.valueOf(selectedCarPark.getTotalSpaces()));
        DateFormat formatter = null;
        if (DateUtils.isToday(selectedCarPark.getLastUpdated())) {
            formatter = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            vwLastUpdated.setText(String.valueOf("Today\n" + formatter.format(new Date(selectedCarPark.getLastUpdated()))));
        } else {
            formatter = new SimpleDateFormat("HH:mm:ss\ndd-MM-yy", Locale.getDefault());
            vwLastUpdated.setText(String.valueOf(formatter.format(new Date(selectedCarPark.getLastUpdated()))));
        }
        vwVehicleHeight.setText(String.valueOf(selectedCarPark.getVehicleHeight() + "m"));
        vwTimes.setText(getTodaysTimes(selectedCarPark.getTimes()));

        vwTimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CarParkDetails.this)
                        .setMessage(getAllTimes(selectedCarPark.getTimes()))
                        .setTitle(getString(R.string.times))
                        .show();
            }
        });

    }

    public void backToCarParks(View view) {
        onBackPressed();
    }

    public void goToReserveSpace(View view) {
        Intent intent = new Intent(this, ReserveSpace.class);
        intent.putExtra("carParkName", selectedCarPark.getName());
        startActivity(intent);
    }

    public void refresh(View view) {
        finish();
        startActivity(getIntent());
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo.isConnectedOrConnecting();
    }

    private String getAllTimes(HashMap<Integer, ParkDateTime> times) {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < times.size(); i++) {
            switch (i) {
                case 0:
                    string.append(getPrintTime(getString(R.string.mon), times.get(i)));
                    break;
                case 1:
                    string.append(getPrintTime(getString(R.string.tue), times.get(i)));
                    break;
                case 2:
                    string.append(getPrintTime(getString(R.string.wed), times.get(i)));
                    break;
                case 3:
                    string.append(getPrintTime(getString(R.string.thu), times.get(i)));
                    break;
                case 4:
                    string.append(getPrintTime(getString(R.string.fri), times.get(i)));
                    break;
                case 5:
                    string.append(getPrintTime(getString(R.string.sat), times.get(i)));
                    break;
                case 6:
                    string.append(getPrintTime(getString(R.string.sun), times.get(i)));
                    break;
            }
        }
        return string.toString();
    }

    private String getPrintTime(String day, ParkDateTime parkDateTime) {
        if (parkDateTime == null) {
            return day + ": " + getString(R.string.closed);
        } else {
            return day + ": " + parkDateTime.toString();
        }
    }

    private String getTodaysTimes(HashMap<Integer, ParkDateTime> times) {
        Calendar now = Calendar.getInstance();
        int day = now.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case 1:
                return (getPrintTime(getString(R.string.sun), times.get(day - 2 + 7))).replace(": ", ":\n");
            case 2:
                return (getPrintTime(getString(R.string.mon), times.get(day - 2))).replace(": ", ":\n");
            case 3:
                return (getPrintTime(getString(R.string.tue), times.get(day - 2))).replace(": ", ":\n");
            case 4:
                return (getPrintTime(getString(R.string.wed), times.get(day - 2))).replace(": ", ":\n");
            case 5:
                return (getPrintTime(getString(R.string.thu), times.get(day - 2))).replace(": ", ":\n");
            case 6:
                return (getPrintTime(getString(R.string.fri), times.get(day - 2))).replace(": ", ":\n");
            case 7:
                return (getPrintTime(getString(R.string.sat), times.get(day - 2))).replace(": ", ":\n");
            default:
                return null;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, CarParks.class);
        intent.putExtra("carParkName", selectedCarPark.getName());
        startActivity(intent);
    }


}
