package uburti.luca.fitnessfordiabetics;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import static uburti.luca.fitnessfordiabetics.MainActivity.DAY_ID;

public class DayDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_detail);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int dayId = extras.getInt(DAY_ID);
            if (dayId != 0) {
                Toast.makeText(this, dayId, Toast.LENGTH_SHORT).show();
            }
        }

    }
}
