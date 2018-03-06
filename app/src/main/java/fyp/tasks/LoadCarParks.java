package fyp.tasks;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import fyp.model.CarPark;

public class LoadCarParks {
    private static ArrayList<CarPark> carParksList = new ArrayList<>();


    public static ArrayList<CarPark> get(Context context) {
        RetrieveJson task;
        JSONObject json = null;
        if (isOnline(context)) {
            task = new RetrieveJson(context);
            String urlStr = "http://data.corkcity.ie/api/action/datastore_search?resource_id=6cc1028e-7388-4bc5-95b7-667a59aa76dc";
            task.execute(urlStr);
            try {
                json = task.get();
            } catch (InterruptedException | ExecutionException e) {
                Log.d("myError", e.toString());
                e.printStackTrace();
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            }
        } else {
            SharedPreferences sharedPref = context.getSharedPreferences("backUp", Context.MODE_PRIVATE);
            try {
                json = new JSONObject(sharedPref.getString("jsonBackUp", null));
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
        assert cm != null;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


}
