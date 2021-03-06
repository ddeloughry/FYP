package fyp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import fyp.model.FirebaseController;
import fyp.model.ParkReservation;

public class ReserveSpace extends AppCompatActivity {
    private EditText enterLicence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_space);
        TextView carParkName = findViewById(R.id.carParkName);
        enterLicence = findViewById(R.id.enterLicencePlate);
        carParkName.setText(getIntent().getStringExtra("carParkName"));
        ImageButton clearText = findViewById(R.id.clearText);
        clearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterLicence.setText("");
            }
        });
    }


    public void reserveSpace(View view) {
        if (!enterLicence.getText().toString().equalsIgnoreCase("") && FirebaseController.getLoggedUser().getEmail() != null) {
            ParkReservation reservation = new ParkReservation(System.currentTimeMillis(),
                    System.currentTimeMillis() + 3600000,
                    getIntent().getStringExtra("carParkName"),
                    enterLicence.getText().toString());
            FirebaseController.reserveSpace(reservation, FirebaseController.getLoggedUser().getEmail().replace(".", "_"));
            enterLicence.setText("");
            Toast.makeText(this, getString(R.string.res_succ) + "!", Toast.LENGTH_LONG).show();
            cancelReservation(view);
        } else {
            Toast.makeText(this, getString(R.string.enter_lice_pl) + "!", Toast.LENGTH_LONG).show();
        }
    }

    public void cancelReservation(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, CarParkDetails.class);
        intent.putExtra("carParkName", getIntent().getStringExtra("carParkName"));
        startActivity(intent);
    }
}
