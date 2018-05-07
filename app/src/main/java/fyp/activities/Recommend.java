package fyp.activities;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import fyp.model.CarPark;
import fyp.model.LoadCarParks;
import fyp.model.ParkDistance;
import fyp.model.RetrieveJsonObject;

public class Recommend extends AppCompatActivity {
    private EditText editText;
    private URL url = null;
    private String latLng;
    private ArrayList<CarPark> carParksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        Button searchLocation = findViewById(R.id.searchLocation);
        editText = findViewById(R.id.entDest);
        final TextView viewNearest = findViewById(R.id.viewNearest);
        final ParkDistance[] nearest = new ParkDistance[1];
        searchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address="
                            + String.valueOf(editText.getText()).trim().replaceAll(" +", "+")
                            + "&key=%20AIzaSyCzNHDdvhriV2eQ0I6gN1G7n_Vuu0chKdw");
                    RetrieveJsonObject retriveTask = new RetrieveJsonObject(getApplicationContext());
                    retriveTask.execute(url);
                    if (retriveTask.get() != null) {
                        JSONObject jsonObject = retriveTask.get();
                        if (!jsonObject.getString("status").equalsIgnoreCase("ZERO_RESULTS")) {
                            ParseGeoJson parseTask = new ParseGeoJson();
                            parseTask.execute(jsonObject);
                            latLng = parseTask.get();
                            String[] latLngs = latLng.split(",");
                            ArrayList<ParkDistance> distances = new ArrayList<>();
                            Location dest = new Location("Dest");
                            dest.setLatitude(Double.parseDouble(latLngs[0]));
                            dest.setLongitude(Double.parseDouble(latLngs[1]));
                            try {
                                carParksList = LoadCarParks.get(getApplicationContext());
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                            for (CarPark carPark : carParksList) {
                                Location park = new Location("Park");
                                park.setLatitude(carPark.getLatitude());
                                park.setLongitude(carPark.getLongitude());
                                distances.add(new ParkDistance(carPark.getName(), park.distanceTo(dest), carPark.isFull()));
                            }
                            nearest[0] = distances.get(0);
                            for (int i = 0; i < distances.size(); i++) {
                                if (distances.get(i).getDistance() < nearest[0].getDistance() && !(distances.get(i).isFull())) {
                                    nearest[0] = distances.get(i);
                                }
                            }
                            for (CarPark park : carParksList) {
                                if (park.getName().equalsIgnoreCase(nearest[0].getName())) {
                                    String diplayInfo = nearest[0].getName() + "\n" + Math.round(nearest[0].getDistance())
                                            + " metres\n" + park.getFreeSpaces() + " spaces";
                                    viewNearest.setText(diplayInfo);
                                }
                            }
                        } else {
                            Toast.makeText(Recommend.this, "Could not find that location!", Toast.LENGTH_SHORT).show();
                            viewNearest.setText("");
                        }
                    }
                } catch (MalformedURLException | InterruptedException | ExecutionException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void backToMenu(View view) {
        onBackPressed();
    }

    public void clearText(View view) {
        editText.setText("");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
    }
}

class ParseGeoJson extends AsyncTask<JSONObject, Void, String> {
    @Override
    protected String doInBackground(JSONObject... jsonObjects) {
        JSONObject jsonObject = jsonObjects[0];
        JSONArray jsonArray;
        try {
            jsonArray = jsonObject.getJSONArray("results");
            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
            JSONObject jsonObject2 = jsonObject1.getJSONObject("geometry");
            JSONObject jsonObject3 = jsonObject2.getJSONObject("location");
            return String.valueOf(jsonObject3.getDouble("lat")) + "," 
                + String.valueOf(jsonObject3.getDouble("lng"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
