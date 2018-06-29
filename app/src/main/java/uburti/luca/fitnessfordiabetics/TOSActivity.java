package uburti.luca.fitnessfordiabetics;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
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
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        TOSScrollView.setScrollbarFadingEnabled(false);

        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean TOSAccepted = sharedPrefs.getBoolean(TOS_ACCEPTED, false);
        if (!TOSAccepted) {
            if (ab != null) {   //hide the back arrow in the actionbar
                ab.setDisplayHomeAsUpEnabled(false);
                ab.setHomeButtonEnabled(false);
                ab.setDisplayShowHomeEnabled(false);
            }
            TOSButton.setVisibility(View.VISIBLE);  //show the Accept button
            TOSButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putBoolean(TOS_ACCEPTED, true);      //write in SharedPrefs that the user accepted the TOS
                    editor.apply();
                    TOSButton.setVisibility(View.GONE);
                    if (ab != null) {
                        ab.setDisplayHomeAsUpEnabled(true);
                        ab.setHomeButtonEnabled(true);
                        ab.setDisplayShowHomeEnabled(true);
                    }
                    Intent intent = new Intent(TOSActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent); //clear backstack and start over with the Main Activity
                }
            });
        } else {
            TOSButton.setVisibility(View.GONE);
        }

    }


}
