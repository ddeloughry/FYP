package fyp.model;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class JSONParser extends AsyncTask<JSONObject, Void, ArrayList<CarPark>> {
    private static HashMap<Integer, ParkDateTime> times;

    private static long getLongTime(String dateString) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd,hh:mm:ss", Locale.UK);
        dateString = dateString.replace("T", ",");
        Date date = format.parse(dateString);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getTimeInMillis();
    }

    private static void getTimes(String string) {
        string = string.replace(" ", "");
        string = mapForDays(string);
        if (string.substring(1, 2).equalsIgnoreCase("-")) {
            Integer min = Integer.parseInt(string.substring(0, 1));
            Integer max = Integer.parseInt(string.substring(2, 3));
            if (string.substring(3, 6).equalsIgnoreCase("and")) {
                Integer andNum = Integer.parseInt(string.substring(6, 7));
                ArrayList<Integer> dayInts = getRange(min, max, andNum);
                string = string.substring(7);
                for (Integer i : dayInts) {
                    if (string.substring(6).equalsIgnoreCase("00.00")) {
                        times.put(i, new ParkDateTime(string.substring(0, 5), "23:59"));
                    } else {
                        times.put(i, new ParkDateTime(string.substring(0, 5), string.substring(6, 11)));
                    }
                }
            } else {
                ArrayList<Integer> dayInts = getRange(min, max, null);
                string = string.substring(3);
                for (Integer i : dayInts) {
                    if (string.substring(6).equalsIgnoreCase("00.00")) {
                        times.put(i, new ParkDateTime(string.substring(0, 5), "23:59"));
                    } else {
                        times.put(i, new ParkDateTime(string.substring(0, 5), string.substring(6, 11)));
                    }
                }
            }
        } else {
            if (string.substring(7).equalsIgnoreCase("00.00")) {
                times.put(Integer.parseInt(string.substring(0, 1)), new ParkDateTime(string.substring(1, 6), "23:59"));
            } else {
                times.put(Integer.parseInt(string.substring(0, 1)), new ParkDateTime(string.substring(1, 6), string.substring(7, 12)));
            }
        }
    }

    private static String mapForDays(String string) {
        return string.replace("Monday", "0")
                .replace("Tuesday", "1")
                .replace("Wednesday", "2")
                .replace("Thursday", "3")
                .replace("Friday", "4")
                .replace("Saturday", "5")
                .replace("Sunday", "6");
    }

    private static ArrayList<Integer> getRange(Integer min, Integer max, Integer andNumber) {
        ArrayList<Integer> arr = new ArrayList<>(7);
        for (int i = min; i <= max; i++) {
            arr.add(i);
        }
        arr.add(andNumber);
        return arr;
    }

    @SuppressLint("UseSparseArrays")
    @Override
    protected ArrayList<CarPark> doInBackground(JSONObject... jsonObjects) {
        JSONObject json = jsonObjects[0];
        ArrayList<CarPark> cParks = new ArrayList<>();
        try {
            JSONObject items = new JSONObject(json.getString("result"));
            JSONArray itemArray = items.getJSONArray("records");
            for (int i = 0; i < itemArray.length(); i++) {
                JSONObject j = itemArray.getJSONObject(i);
                CarPark carPark = new CarPark();
                carPark.setName(j.getString("name"));
                carPark.setLatitude(j.getDouble("latitude"));
                carPark.setLongitude(j.getDouble("longitude"));
                carPark.setFreeSpaces(j.getInt("free_spaces"));
                carPark.setTotalSpaces(j.getInt("spaces"));
                carPark.setLastUpdated(getLongTime(j.getString("date")));
                if ((j.getString("price")).replaceAll("[^\\d.]", "").length() > 4) {
                    carPark.setPricePerHour(Double.parseDouble((j.getString("price")).replaceAll("[^\\d.]", "").substring(0, 4)));
                    carPark.setPricePerDay(Double.parseDouble((j.getString("price")).replaceAll("[^\\d.]", "").substring(5)));
                } else {
                    carPark.setPricePerHour(Double.parseDouble((j.getString("price")).replaceAll("[^\\d.]", "")));
                    carPark.setPricePerDay(null);
                }

                String heightString = (j.getString("height_restrictions")).replaceAll("[^\\d.]", "");
                carPark.setVehicleHeight(Double.parseDouble(heightString));
                String daysTimes = j.getString("opening_times");
                if (daysTimes.equalsIgnoreCase("24/7")) {
                    times = new HashMap<>(7);
                    times.put(0, new ParkDateTime("00:00", "23:59"));
                    times.put(1, new ParkDateTime("00:00", "23:59"));
                    times.put(2, new ParkDateTime("00:00", "23:59"));
                    times.put(3, new ParkDateTime("00:00", "23:59"));
                    times.put(4, new ParkDateTime("00:00", "23:59"));
                    times.put(5, new ParkDateTime("00:00", "23:59"));
                    times.put(6, new ParkDateTime("00:00", "23:59"));
                } else {
                    String[] st0Ar = daysTimes.split(",");
                    times = new HashMap<>(7);
                    getTimes(st0Ar[0]);

                    if (st0Ar.length == 2) {
                        getTimes(st0Ar[1]);
                    }
                    if (st0Ar.length == 3) {
                        getTimes(st0Ar[1]);
                        getTimes(st0Ar[2]);
                    }
                }
                carPark.setTimes(times);
                cParks.add(carPark);
            }
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
        return cParks;
    }
}