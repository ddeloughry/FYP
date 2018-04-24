package fyp.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class TrafficDirection extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic);

        Button estimateDirection = findViewById(R.id.estimateDirection);
        Location current = getLastKnownLocation();
        double eastDistance = getShortestDistance(51.953303, -7.847526, 51.898727, -8.471800, current.getLatitude(), current.getLongitude());
        double westDistance = getShortestDistance(51.904757, -8.958214, 51.898727, -8.471800, current.getLatitude(), current.getLongitude());
        double northDistance = getShortestDistance(52.137328, -8.645663, 51.898727, -8.471800, current.getLatitude(), current.getLongitude());
        double southDistance = getShortestDistance(51.620450, -8.905504, 51.898727, -8.471800, current.getLatitude(), current.getLongitude());
        double[] distanceToRoutes = {northDistance,
                westDistance,
                eastDistance,
                southDistance};
        if (isSmallest(distanceToRoutes, eastDistance)) {
            estimateDirection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToEast(null);
                }
            });
        } else if (isSmallest(distanceToRoutes, westDistance)) {
            estimateDirection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToWest(null);
                }
            });
        } else if (isSmallest(distanceToRoutes, northDistance)) {
            estimateDirection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToNorth(null);
                }
            });
        } else if (isSmallest(distanceToRoutes, southDistance)) {
            estimateDirection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToSouth(null);
                }
            });
        }
    }

    public void goToNorth(View view) {
        Intent intent = new Intent(this, ChooseParkTraffic.class);
        intent.putExtra("direction", getString(R.string.north));
        intent.putExtra("int", 0);
        startActivity(intent);
    }

    public void goToWest(View view) {
        Intent intent = new Intent(this, ChooseParkTraffic.class);
        intent.putExtra("direction", getString(R.string.west));
        intent.putExtra("int", 1);
        startActivity(intent);
    }

    public void goToEast(View view) {
        Intent intent = new Intent(this, ChooseParkTraffic.class);
        intent.putExtra("direction", getString(R.string.east));
        intent.putExtra("int", 2);
        startActivity(intent);
    }

    public void goToSouth(View view) {
        Intent intent = new Intent(this, ChooseParkTraffic.class);
        intent.putExtra("direction", getString(R.string.south));
        intent.putExtra("int", 3);
        startActivity(intent);
    }


    private boolean isSmallest(double[] doubleArray, double distance) {
        boolean isSmallest = false;
        for (double current : doubleArray) {
            isSmallest = !(current < distance);
        }
        return isSmallest;
    }

    /**
     * @param x1: origin latitude
     * @param y1: origin longitude
     * @param x2: destination latitude
     * @param y2: destination longitude
     * @param x3: current latitude
     * @param y3: current longitude
     * @return Shortest distance from point to line
     */
    private double getShortestDistance(double x1, double y1, double x2, double y2, double x3, double y3) {
        double px = x2 - x1;
        double py = y2 - y1;
        double temp = (px * px) + (py * py);
        double u = ((x3 - x1) * px + (y3 - y1) * py) / (temp);
        if (u > 1) {
            u = 1;
        } else if (u < 0) {
            u = 0;
        }
        double x = x1 + u * px;
        double y = y1 + u * py;

        double dx = x - x3;
        double dy = y - y3;
        return Math.sqrt((dx * dx) + (dy * dy));

    }

    private Location getLastKnownLocation() {
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = null;
        if (locationManager != null) {
            providers = locationManager.getProviders(true);
        }
        Location location = null;
        if (providers != null) {
            for (String provider : providers) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return location;
                }
                Location l = locationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (location == null || l.getAccuracy() < location.getAccuracy()) {
                    location = l;
                }
            }
        }
        return location;
    }

    public void backToMenu(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, Main.class));
    }
}
