package fyp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import fyp.model.MyDao;
import fyp.model.ParkReservation;
import fyp.model.User;

public class ReserveSpace extends AppCompatActivity {
    private EditText enterLicence;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_space);
        TextView carParkName = findViewById(R.id.carParkName);
        enterLicence = findViewById(R.id.enterLicencePlate);
        carParkName.setText(getIntent().getStringExtra("carParkName"));
        email = User.getLoggedEmail();
    }


    public void reserveSpace(View view) {
        if (!(enterLicence.getText().toString().equalsIgnoreCase(""))) {
            ParkReservation reservation = new ParkReservation(System.currentTimeMillis(),
                    System.currentTimeMillis() + 3600000,
                    getIntent().getStringExtra("carParkName"),
                    enterLicence.getText().toString());
            MyDao.putReservation(email, reservation);
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

    public void clearText(View v) {
        enterLicence.setText("");
    }
}
