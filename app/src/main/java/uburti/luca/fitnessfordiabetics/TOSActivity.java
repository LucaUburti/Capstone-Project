package uburti.luca.fitnessfordiabetics;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TOSActivity extends AppCompatActivity {
    public static final String TOS_ACCEPTED = "TOS_ACCEPTED";
    @BindView(R.id.tos_button)
    Button TOSButton;
    @BindView(R.id.tos_scrollview)
    ScrollView TOSScrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tos);
        ButterKnife.bind(this);


        TOSScrollView.setScrollbarFadingEnabled(false);

        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean TOSAccepted = sharedPrefs.getBoolean(TOS_ACCEPTED, false);
        if (!TOSAccepted) {
            TOSButton.setVisibility(View.VISIBLE);
            TOSButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putBoolean(TOS_ACCEPTED, true);      //write in SharedPrefs that the user accepted the TOS
                    editor.apply();
                    TOSButton.setVisibility(View.GONE);
                    startActivity(new Intent(TOSActivity.this, MainActivity.class)); //now we can go to Main Activity
                }
            });
        } else {
            TOSButton.setVisibility(View.GONE);
        }

    }


}
