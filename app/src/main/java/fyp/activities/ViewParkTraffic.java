package fyp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import fyp.model.RetrieveJsonArray;
import fyp.model.TrafficDelay;

public class ViewParkTraffic extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_park_traffic);
        TextView refreshTime = findViewById(R.id.refreshTime);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss dd-MM-yy", Locale.getDefault());
        refreshTime.setText(String.valueOf(refreshTime.getText().toString()).concat("\n" + String.valueOf(formatter.format(new Date(System.currentTimeMillis())))));
        TextView carParkName = findViewById(R.id.carParkName);
        carParkName.setText(getIntent().getStringExtra("carParkName"));
        ArrayList<TrafficDelay> delayList = new ArrayList<>();
        RetrieveJsonArray retriveDelayTask = new RetrieveJsonArray(this);
        try {
            retriveDelayTask.execute(new URL("http://192.168.1.68:8080/traffic"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        JSONArray delayArray = null;
        try {
            delayArray = retriveDelayTask.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if (delayArray != null) {
            for (int i = 0; i < delayArray.length(); i++) {
                JSONObject eachDelay = null;
                try {
                    eachDelay = delayArray.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                TrafficDelay trafficDelay = null;
                try {
                    if (eachDelay != null) {
                        trafficDelay = new TrafficDelay(
                                eachDelay.getString("carParkName"),
                                eachDelay.getString("direction"),
                                eachDelay.getLong("time"),
                                (int) eachDelay.getDouble("delay")
                        );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (trafficDelay != null
                        && trafficDelay.getCarParkName().equalsIgnoreCase(getIntent().getStringExtra("carParkName"))
                        && trafficDelay.getDirection().equalsIgnoreCase(getIntent().getStringExtra("direction"))) {
                    delayList.add(trafficDelay);
                }
            }
            StringBuilder displayDelays = new StringBuilder();
            int smallestDelay = delayList.get(0).getDelay();
            for (TrafficDelay delay : delayList) {
                delay.toString();
                if (smallestDelay > delay.getDelay() && smallestDelay > 0 && delay.getDelay() > 0) {
                    smallestDelay = delay.getDelay();
                }
            }
            for (TrafficDelay delay : delayList) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(delay.getTime());
                String secStr = String.valueOf(calendar.get(Calendar.MINUTE));
                if (secStr.length() == 1) {
                    secStr = "0" + String.valueOf(secStr);
                }
                int finalDelay = delay.getDelay() - smallestDelay;
                if (finalDelay < 0) {
                    finalDelay = 0;
                }
                displayDelays.append(String.valueOf(((finalDelay) % 3600) / 60))
                        .append(" mins")
                        .append("\t-\t")
                        .append(calendar.get(Calendar.HOUR_OF_DAY))
                        .append(":")
                        .append(secStr)
                        .append("\n");
            }
            TextView viewDelay = findViewById(R.id.viewDelay);
            viewDelay.setText(displayDelays);
            ProgressBar progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);
        }
    }

    public void backToChoosePark(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ChooseParkTraffic.class);
        intent.putExtra("direction", getIntent().getStringExtra("direction"));
        startActivity(intent);
    }

    public void refresh(View view) {
        finish();
        startActivity(getIntent());
    }
}
