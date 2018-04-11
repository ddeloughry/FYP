package fyp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import fyp.model.CarPark;
import fyp.model.JSONParser;
import fyp.model.RetrieveJson;

public class Traffic extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic);

        RetrieveJson task = null;
        if (isOnline()) {
            task = new RetrieveJson(this);
            String urlStr = "http://data.corkcity.ie/api/action/datastore_search?resource_id=6cc1028e-7388-4bc5-95b7-667a59aa76dc";
            task.execute(urlStr);
        }
        JSONObject json = null;
        if (isOnline()) {
            try {
                assert task != null;
                json = task.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            SharedPreferences sharedPref = this.getSharedPreferences("backUp", Context.MODE_PRIVATE);
            try {
                json = new JSONObject(sharedPref.getString("jsonBackUp", null));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        JSONParser task2 = new JSONParser();
        task2.execute(json);
        ArrayList<CarPark> carParksList = null;
        try {
            carParksList = task2.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        ArrayList<TextView> vwCarParkNames = new ArrayList<>();
        vwCarParkNames.add((TextView) findViewById(R.id.carTraf0));
        vwCarParkNames.add((TextView) findViewById(R.id.carTraf1));
        vwCarParkNames.add((TextView) findViewById(R.id.carTraf2));
        vwCarParkNames.add((TextView) findViewById(R.id.carTraf3));
        vwCarParkNames.add((TextView) findViewById(R.id.carTraf4));
        vwCarParkNames.add((TextView) findViewById(R.id.carTraf5));
        vwCarParkNames.add((TextView) findViewById(R.id.carTraf6));
        vwCarParkNames.add((TextView) findViewById(R.id.carTraf7));

        assert carParksList != null;
        for (int i = 0; i < carParksList.size(); i++) {
            vwCarParkNames.get(i).setText(carParksList.get(i).getName());
        }
    }

    public void backToMenu(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
