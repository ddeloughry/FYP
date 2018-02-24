package fyp.model;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class AddTrafficStat extends Service {
    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        ArrayList<CarPark> carParksList = LoadCarParks.get(this);

        assert carParksList != null;
        final ArrayList<TrafficStat> trafficStats = new ArrayList<>();
        final String[] carParkNames = new String[carParksList.size()];
        final StringBuilder urlString = new StringBuilder("https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=cobh&destinations=");
        int i = 0;
        while (i < carParksList.size()) {
            if (i == carParksList.size() - 1) {
                urlString.append(String.valueOf(carParksList.get(i).getLatitude())).append(",").append(String.valueOf(carParksList.get(i).getLongitude()));
            } else {
                urlString.append(String.valueOf(carParksList.get(i).getLatitude())).append(",").append(String.valueOf(carParksList.get(i).getLongitude())).append("|");
            }
            carParkNames[i] = carParksList.get(i).getName();
            i++;
        }
        urlString.append("&traffic_model=pessimistic&departure_time=now&key=%20AIzaSyCzNHDdvhriV2eQ0I6gN1G7n_Vuu0chKdw");
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                Toast.makeText(context, "service running", Toast.LENGTH_SHORT).show();

                JSONObject jsonTraffic = null;
                RetrieveJson retrieveTraffic = new RetrieveJson(context);
                retrieveTraffic.execute(urlString.toString());

                JSONObject jsonWeather = null;
                RetrieveJson retrieveWeather = new RetrieveJson(context);
                retrieveWeather.execute("http://api.openweathermap.org/data/2.5/weather?q=cork&appid=bd6ab1b7b59f866b3e68f34173c5c570");
                try {
                    jsonTraffic = retrieveTraffic.get();
                    jsonWeather = retrieveWeather.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                }
                Calendar now = Calendar.getInstance();
                try {
                    assert jsonTraffic != null;
                    JSONArray jsonTrafficElements = (jsonTraffic.getJSONArray("rows")).getJSONObject(0).getJSONArray("elements");
                    assert jsonWeather != null;
                    JSONArray jsonWeatherArray = jsonWeather.getJSONArray("weather");
                    JSONObject weather = jsonWeatherArray.getJSONObject(0);
                    for (int i = 0; i < jsonTrafficElements.length(); i++) {
                        JSONObject j = jsonTrafficElements.getJSONObject(i);
                        JSONObject times = j.getJSONObject("duration_in_traffic");
                        String time = times.getString("text");

//                        if (now.get(Calendar.MINUTE) == 0 || now.get(Calendar.MINUTE) == 15 || now.get(Calendar.MINUTE) == 30 || now.get(Calendar.MINUTE) == 45) {
                        trafficStats.add(new TrafficStat(now.get(Calendar.HOUR) + "." + now.get(Calendar.MINUTE) + "." + now.get(Calendar.SECOND), Double.parseDouble(time.replaceAll("[^\\d.]", "")), now.get(Calendar.DAY_OF_WEEK), weather.getString("description"), carParkNames[i]));
                    }

                } catch (JSONException e) {
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                trafficStats.add(new TrafficStat(now.get(Calendar.HOUR) + "." + now.get(Calendar.MINUTE) + "." + now.get(Calendar.SECOND), 55, now.get(Calendar.DAY_OF_WEEK), "raining blood", "north main street"));
                DatabaseReference reservationDb = (FirebaseDatabase.getInstance()).getReference("traffic").child(String.valueOf(new Date(System.currentTimeMillis())));
                reservationDb.setValue(trafficStats);

                handler.postDelayed(runnable, 900000);
            }
        }

        ;
        handler.postDelayed(runnable, 1);
    }

    @Override
    public void onDestroy() {
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        //handler.removeCallbacks(runnable);
        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
    }

//        @Override
//    public void onStart(Intent intent, int startid) {
//        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
//    }
//    private boolean isOnline() {
//        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
//        assert cm != null;
//        NetworkInfo netInfo = cm.getActiveNetworkInfo();
//        return netInfo != null && netInfo.isConnectedOrConnecting();
//    }
}