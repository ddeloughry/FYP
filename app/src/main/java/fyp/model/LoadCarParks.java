package fyp.model;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class LoadCarParks {
    private static ArrayList<CarPark> carParksList;

    public static ArrayList<CarPark> get(Context context) throws MalformedURLException {
        RetrieveJsonObject task;
        JSONObject json = null;
        if (isOnline(context)) {
            task = new RetrieveJsonObject(context);
            task.execute(new URL("http://data.corkcity.ie/api/action/datastore_search?resource_id=6cc1028e-7388-4bc5-95b7-667a59aa76dc"));
            try {
                if (task.get() == null) {
                    SharedPreferences sharedPref = context.getSharedPreferences("backUp", Context.MODE_PRIVATE);
                    json = new JSONObject(sharedPref.getString("parkingBackup", null));
                } else {
                    json = task.get();
                }
            } catch (InterruptedException | ExecutionException | JSONException e) {
                e.printStackTrace();
            }
        } else {
            SharedPreferences sharedPref = context.getSharedPreferences("backUp", Context.MODE_PRIVATE);
            try {
                json = new JSONObject(sharedPref.getString("parkingBackup", null));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        JSONParser parseCarParks = new JSONParser();
        parseCarParks.execute(json);
        try {
            carParksList = parseCarParks.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return carParksList;
    }

    private static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
