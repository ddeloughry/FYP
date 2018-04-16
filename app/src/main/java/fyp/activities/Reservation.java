package fyp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import fyp.model.ParkReservation;

public class Reservation extends AppCompatActivity {
    private ParkReservation selectedReservation;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        selectedReservation = new ParkReservation();
        mAuth = FirebaseAuth.getInstance();
        final GridLayout reserveGrid = findViewById(R.id.reserveGrid);
        final TextView vwCarParkName = findViewById(R.id.resParkName);
        final TextView viewStartTime = findViewById(R.id.txtvw0);
        final TextView viewEndTime = findViewById(R.id.txtvw1);
        final TextView viewLicensePlate = findViewById(R.id.txtvw2);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reservationDb;
        if (mAuth.getCurrentUser() != null) {
            reservationDb = database.getReference("reservation").child(mAuth.getCurrentUser().getEmail().replace(".", "_"));
            reservationDb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists() && mAuth.getCurrentUser() != null) {
                        selectedReservation.setCarParkName((dataSnapshot.getValue(ParkReservation.class)).getCarParkName());
                        selectedReservation.setStartTime((dataSnapshot.getValue(ParkReservation.class)).getStartTime());
                        selectedReservation.setEndTime((dataSnapshot.getValue(ParkReservation.class)).getEndTime());
                        selectedReservation.setLicencePlate((dataSnapshot.getValue(ParkReservation.class)).getLicencePlate());
                        vwCarParkName.setText(selectedReservation.getCarParkName());
                        DateFormat formatter = new SimpleDateFormat("HH:mm:ss\nE dd-MMM-yy", Locale.UK);
                        viewStartTime.setText(String.valueOf(formatter.format(new Date(selectedReservation.getStartTime()))));
                        viewEndTime.setText(String.valueOf(formatter.format(new Date(selectedReservation.getEndTime()))));
                        viewLicensePlate.setText(String.valueOf(selectedReservation.getLicencePlate()));
                        reserveGrid.setVisibility(View.VISIBLE);
                    } else {
                        vwCarParkName.setText(getString(R.string.you_do_not_currently_have_any_reservations));
                        viewStartTime.setText("");
                        viewEndTime.setText("");
                        viewLicensePlate.setText("");
                        reserveGrid.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });
        } else {
            vwCarParkName.setText(getString(R.string.please_log_in_to_reserve_a_space));
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
}