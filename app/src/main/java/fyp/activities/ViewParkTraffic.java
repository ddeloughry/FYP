package fyp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import fyp.model.TrafficDelay;

public class ViewParkTraffic extends AppCompatActivity {
    private ArrayList<TrafficDelay> delayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_park_traffic);
        TextView refreshTime = findViewById(R.id.refreshTime);

        DateFormat formatter = new SimpleDateFormat("HH:mm:ss dd-MM-yy", Locale.getDefault());

        refreshTime.setText(String.valueOf(refreshTime.getText().toString()).concat("\n" + String.valueOf(formatter.format(new Date(System.currentTimeMillis())))));
        TextView carParkName = findViewById(R.id.carParkName);
        carParkName.setText(getIntent().getStringExtra("carParkName"));
        delayList = new ArrayList<>();
        DatabaseReference delayDb = FirebaseDatabase.getInstance().getReference("delays");
        delayDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                    TrafficDelay td = new TrafficDelay();
                    td.setCarParkName(dataSnapshot.child(String.valueOf(i)).getValue(TrafficDelay.class).getCarParkName());
                    td.setDelay(dataSnapshot.child(String.valueOf(i)).getValue(TrafficDelay.class).getDelay());
                    td.setDirection(dataSnapshot.child(String.valueOf(i)).getValue(TrafficDelay.class).getDirection());
                    td.setTime(dataSnapshot.child(String.valueOf(i)).getValue(TrafficDelay.class).getTime());
                    if (td.getDirection().equalsIgnoreCase(getIntent().getStringExtra("direction")) && td.getCarParkName().equalsIgnoreCase(getIntent().getStringExtra("carParkName"))) {
                        delayList.add(td);
                    }
                }
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
                    String secStr = String.valueOf(calendar.get(Calendar.MINUTE));
                    if (secStr.length() == 1) {
                        secStr = "0" + String.valueOf(secStr);
                    }
                    displayDelays.append(String.valueOf(((delay.getDelay() - smallestDelay) % 3600) / 60)).append(" mins").append("\t-\t").append(calendar.get(Calendar.HOUR_OF_DAY)).append(":").append(secStr).append("\n");
                }
                TextView viewDelay = findViewById(R.id.viewDelay);
                viewDelay.setText(displayDelays);
                ProgressBar progressBar = findViewById(R.id.progressBar);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

    public void refresh(View view) {
        finish();
        startActivity(getIntent());
    }
}
