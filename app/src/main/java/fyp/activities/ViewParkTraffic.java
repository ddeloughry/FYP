package fyp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import fyp.model.RetrieveJsonArray;
import fyp.model.TrafficDelay;

public class ViewParkTraffic extends AppCompatActivity {
    private ArrayList<TrafficDelay> delayList;
    private TextView viewDelay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_park_traffic);

        delayList = new ArrayList<>();
        StringBuilder urlStr = new StringBuilder();
        urlStr.append("http://192.168.1.66:8080/traffic?direction=");
        int dirInt = getIntent().getIntExtra("direction", 0);
        if (dirInt == 0) {
            urlStr.append("north");
        } else if (dirInt == 1) {
            urlStr.append("west");
        } else if (dirInt == 2) {
            urlStr.append("east");
        } else {
            urlStr.append("south");
        }
        URL url = null;
        try {
            url = new URL(String.valueOf(urlStr));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        RetrieveJsonArray task = new RetrieveJsonArray(this);
        task.execute(url);
        JSONArray jsonDelay = null;
        try {
            jsonDelay = task.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if (jsonDelay != null) {
            for (int i = 0; i < jsonDelay.length(); i++) {
                JSONObject eachDelay = null;
                try {
                    eachDelay = jsonDelay.getJSONObject(i);
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
                if (trafficDelay != null && trafficDelay.getCarParkName().equalsIgnoreCase(getIntent().getStringExtra("carParkName"))) {
                    delayList.add(trafficDelay);
                }
            }
        }
        TextView carParkName = findViewById(R.id.carParkName);
        carParkName.setText(getIntent().getStringExtra("carParkName"));
        StringBuilder displayDelays = new StringBuilder();

        int smallestDelay = delayList.get(0).getDelay();
        for (TrafficDelay delay : delayList) {
            if (smallestDelay > delay.getDelay()) {
                smallestDelay = delay.getDelay();
            }
        }

        for (TrafficDelay delay : delayList) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(delay.getTime());
            displayDelays.append(String.valueOf(((delay.getDelay() - smallestDelay) % 3600) / 60)).append(" mins").append("\t-\t").append(calendar.get(Calendar.HOUR_OF_DAY)).append(":").append(calendar.get(Calendar.MINUTE)).append("\n");
        }
        viewDelay = findViewById(R.id.viewDelay);
        viewDelay.setText(displayDelays);
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
}
