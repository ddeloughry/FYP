package fyp.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicReference;


public class RetrieveJsonArray extends AsyncTask<URL, Void, JSONArray> {
    private final AtomicReference<Context> context = new AtomicReference<>();

    public RetrieveJsonArray(Context context) {
        this.context.set(context);
    }

    protected JSONArray doInBackground(URL... urls) {
        try {
            URLConnection urlConnection;
            InputStream is;
            try {
                urlConnection = (urls[0]).openConnection();
                is = urlConnection.getInputStream();
                urlConnection.setConnectTimeout(10);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            backUpJson(jsonText);
            is.close();
            return new JSONArray(jsonText);
        } catch (IOException | JSONException e1) {
            e1.printStackTrace();
            return null;
        }
    }

//    protected void onPostExecute(Boolean result) {
//
//    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private void backUpJson(String jsonText) {
        SharedPreferences sharedPref = context.get().getSharedPreferences("backUp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("jsonBackUp", jsonText);
        editor.apply();
    }


}
