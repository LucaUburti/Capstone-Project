package uburti.luca.fitnessfordiabetics;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyTherapyActivity extends AppCompatActivity {
    //a simple Activity class with various EditTexts where users can save their therapy details

    private static final String THERAPY_RAPID_INSULIN = "THERAPY_RAPID_INSULIN";
    private static final String THERAPY_LONG_INSULIN = "THERAPY_LONG_INSULIN";
    private static final String THERAPY_BREAKFAST_RAPID = "THERAPY_BREAKFAST_RAPID";
    private static final String THERAPY_LUNCH_RAPID = "THERAPY_LUNCH_RAPID";
    private static final String THERAPY_DINNER_RAPID = "THERAPY_DINNER_RAPID";
    private static final String THERAPY_BREAKFAST_LONG = "THERAPY_BREAKFAST_LONG";
    private static final String THERAPY_LUNCH_LONG = "THERAPY_LUNCH_LONG";
    private static final String THERAPY_DINNER_LONG = "THERAPY_DINNER_LONG";
    private static final String THERAPY_NOTES = "THERAPY_NOTES";
    @BindView(R.id.my_therapy_layout)
    ScrollView myTherapyLayout;
    @BindView(R.id.my_therapy_rapid_acting_et)
    EditText rapidActingEt;
    @BindView(R.id.my_therapy_long_acting_et)
    EditText longActingEt;

    @BindView(R.id.my_therapy_breakfast_rapid_et)
    EditText breakfastRapidEt;
    @BindView(R.id.my_therapy_lunch_rapid_et)
    EditText lunchRapidEt;
    @BindView(R.id.my_therapy_dinner_rapid_et)
    EditText dinnerRapidEt;

    @BindView(R.id.my_therapy_breakfast_long_et)
    EditText breakfastLongEt;
    @BindView(R.id.my_therapy_lunch_long_et)
    EditText lunchLongEt;
    @BindView(R.id.my_therapy_dinner_long_et)
    EditText dinnerLongEt;

    @BindView(R.id.my_therapy_notes_title_et)
    EditText notesEt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_therapy);
        ButterKnife.bind(this);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); //https://stackoverflow.com/questions/9732761/prevent-the-keyboard-from-displaying-on-activity-start

        if (savedInstanceState == null) {  //don't bother repopulating the EditTexts on rotations
            populateUIFromSharedPrefs();   //(EditTexts would be restored by the Android Framework anyway...)
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.therapy_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_therapy:
                saveChangesToSharedPrefs();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {   // pressing back should first warn the user if there are unsaved changes
        if (unsavedChanges()) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MyTherapyActivity.this);
            alertBuilder.setTitle(R.string.unsaved_changes).setMessage(R.string.alert_dialog_title_unsaved_changes)
                    .setPositiveButton(R.string.save_changes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            saveChangesToSharedPrefs();
                            finish();
                        }
                    })
                    .setNeutralButton(R.string.stay_on_this_page, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton(R.string.discard_changes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        } else {
            super.onBackPressed();
        }
    }

    private void saveChangesToSharedPrefs() {
        //store the content of each EditTexts in a shared preference

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(THERAPY_RAPID_INSULIN, rapidActingEt.getText().toString());
        editor.putString(THERAPY_LONG_INSULIN, longActingEt.getText().toString());
        editor.putString(THERAPY_BREAKFAST_RAPID, breakfastRapidEt.getText().toString());
        editor.putString(THERAPY_LUNCH_RAPID, lunchRapidEt.getText().toString());
        editor.putString(THERAPY_DINNER_RAPID, dinnerRapidEt.getText().toString());
        editor.putString(THERAPY_BREAKFAST_LONG, breakfastLongEt.getText().toString());
        editor.putString(THERAPY_LUNCH_LONG, lunchLongEt.getText().toString());
        editor.putString(THERAPY_DINNER_LONG, dinnerLongEt.getText().toString());
        editor.putString(THERAPY_NOTES, notesEt.getText().toString());
        editor.apply();
        Toast.makeText(this, R.string.changes_saved, Toast.LENGTH_LONG).show();
    }

    private void populateUIFromSharedPrefs() {
        //read SharedPrefs and populate all EditTexts

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        rapidActingEt.setText(sharedPrefs.getString(THERAPY_RAPID_INSULIN, ""));
        longActingEt.setText(sharedPrefs.getString(THERAPY_LONG_INSULIN, ""));
        breakfastRapidEt.setText(sharedPrefs.getString(THERAPY_BREAKFAST_RAPID, ""));
        lunchRapidEt.setText(sharedPrefs.getString(THERAPY_LUNCH_RAPID, ""));
        dinnerRapidEt.setText(sharedPrefs.getString(THERAPY_DINNER_RAPID, ""));
        breakfastLongEt.setText(sharedPrefs.getString(THERAPY_BREAKFAST_LONG, ""));
        lunchLongEt.setText(sharedPrefs.getString(THERAPY_LUNCH_LONG, ""));
        dinnerLongEt.setText(sharedPrefs.getString(THERAPY_DINNER_LONG, ""));
        notesEt.setText(sharedPrefs.getString(THERAPY_NOTES, ""));
    }

    private boolean unsavedChanges() {
        //check each SharedPref String against the content of its matching EditText

        boolean unsavedChanges = false;
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (!sharedPrefs.getString(THERAPY_RAPID_INSULIN, "").equals(rapidActingEt.getText().toString()))
            unsavedChanges = true;
        if (!sharedPrefs.getString(THERAPY_LONG_INSULIN, "").equals(longActingEt.getText().toString()))
            unsavedChanges = true;
        if (!sharedPrefs.getString(THERAPY_BREAKFAST_RAPID, "").equals(breakfastRapidEt.getText().toString()))
            unsavedChanges = true;
        if (!sharedPrefs.getString(THERAPY_LUNCH_RAPID, "").equals(lunchRapidEt.getText().toString()))
            unsavedChanges = true;
        if (!sharedPrefs.getString(THERAPY_DINNER_RAPID, "").equals(dinnerRapidEt.getText().toString()))
            unsavedChanges = true;
        if (!sharedPrefs.getString(THERAPY_BREAKFAST_LONG, "").equals(breakfastLongEt.getText().toString()))
            unsavedChanges = true;
        if (!sharedPrefs.getString(THERAPY_LUNCH_LONG, "").equals(lunchLongEt.getText().toString()))
            unsavedChanges = true;
        if (!sharedPrefs.getString(THERAPY_DINNER_LONG, "").equals(dinnerLongEt.getText().toString()))
            unsavedChanges = true;
        if (!sharedPrefs.getString(THERAPY_NOTES, "").equals(notesEt.getText().toString()))
            unsavedChanges = true;
        return unsavedChanges;
    }
}
