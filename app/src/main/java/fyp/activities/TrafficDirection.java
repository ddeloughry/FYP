package fyp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class TrafficDirection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic);
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


    public void backToMenu(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, Main.class));
    }
}
