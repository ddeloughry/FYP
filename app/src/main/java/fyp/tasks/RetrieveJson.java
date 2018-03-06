package fyp.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicReference;


public class RetrieveJson extends AsyncTask<String, Void, JSONObject> {
    private final AtomicReference<Context> context = new AtomicReference<>();

    public RetrieveJson(Context context) {
        this.context.set(context);
    }

    protected JSONObject doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);
            URLConnection urlConnection = url.openConnection();
//            urlConnection.setConnectTimeout(1000);
            InputStream is = urlConnection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            backUpJson(jsonText);
            is.close();
            return new JSONObject(jsonText);
        } catch (IOException | JSONException e) {
            Log.d("myError", e.toString());
            e.printStackTrace();
            Toast.makeText(context.get(), e.toString(), Toast.LENGTH_SHORT).show();
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
