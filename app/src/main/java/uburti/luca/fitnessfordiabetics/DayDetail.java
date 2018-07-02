package uburti.luca.fitnessfordiabetics;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import uburti.luca.fitnessfordiabetics.database.AppDatabase;
import uburti.luca.fitnessfordiabetics.database.DiabeticDay;
import uburti.luca.fitnessfordiabetics.utils.Utils;
import uburti.luca.fitnessfordiabetics.viewmodel.DayDetailViewModel;
import uburti.luca.fitnessfordiabetics.viewmodel.DayDetailViewModelFactory;
import uburti.luca.fitnessfordiabetics.viewmodel.DayDetailViewModelWithFactory;

import static uburti.luca.fitnessfordiabetics.MainActivity.DATE_EXTRA;
import static uburti.luca.fitnessfordiabetics.MainActivity.DAY_ID_EXTRA;

public class DayDetail extends AppCompatActivity {
    //Activity where the users can write down their blood sugars, injections, meals and workouts, for a specific day

    @BindView(R.id.detail_date_tv)
    TextView dateTv;
    @BindView(R.id.detail_warning_iv)
    ImageView warningIv;
    @BindView(R.id.detail_warning_tv)
    TextView warningTv;
    @BindView(R.id.breakfast_detail)
    View breakfastLayout;
    @BindView(R.id.lunch_detail)
    View lunchLayout;
    @BindView(R.id.dinner_detail)
    View dinnerLayout;
    @BindView(R.id.bedtime_detail)
    View bedtimeLayout;
    @BindView(R.id.detail_workout_cardio_et)
    EditText detailWorkoutCardioEt;
    @BindView(R.id.detail_workout_weights_et)
    EditText detailWorkoutWeightsEt;
    @BindView(R.id.detail_notes_et)
    EditText detailNotesEt;
    private IncludedMealLayout breakfastInclude;    //for breakfast, lunch and dinner we use a single layout include
    private IncludedMealLayout lunchInclude;
    private IncludedMealLayout dinnerInclude;
    private IncludedBedtimeLayout bedtimeInclude;   //and another simpler include specific for bedtime

    private long dayIdFromBundle;
    private long dateFromBundle;

    private boolean hypoglycemiaWarning;
    private boolean hyperglycemiaWarning;

    private AppDatabase appDatabase;
    private MenuItem saveChangesMenuItem;
    private DayDetailViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_detail);
        ButterKnife.bind(this);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); //https://stackoverflow.com/questions/9732761/prevent-the-keyboard-from-displaying-on-activity-start

        setupIncludedLayouts();
        viewModel = ViewModelProviders.of(this).get(DayDetailViewModel.class);

        appDatabase = AppDatabase.getInstance(getApplicationContext());

        Bundle extras = getIntent().getExtras();
        if (viewModel.unsavedChanges) {
            populateUI(viewModel.tempDiabeticDay);  //redraw the UI with the working DiabeticDay
        } else if (extras != null) {

            dayIdFromBundle = extras.getLong(DAY_ID_EXTRA); // on mock days this will be 0
            dateFromBundle = extras.getLong(DATE_EXTRA);

            if (dayIdFromBundle != 0) {   //we retrieved the dayIdFromBundle, this means we are working with an exiting row: update the UI with data retrieved from the DB
                Log.d("DayDetail", "onCreate: dayIdFromBundle retrieved: " + dayIdFromBundle);
                DayDetailViewModelFactory factory = new DayDetailViewModelFactory(appDatabase, dayIdFromBundle);
                final DayDetailViewModelWithFactory viewModelWithFactory = ViewModelProviders.of(this, factory).get(DayDetailViewModelWithFactory.class);
                viewModelWithFactory.getDiabeticDay().observe(this, new Observer<DiabeticDay>() {
                    @Override
                    public void onChanged(@Nullable DiabeticDay diabeticDayFromDb) {
                        viewModelWithFactory.getDiabeticDay().removeObserver(this);
                        viewModel.tempDiabeticDay = diabeticDayFromDb; //update the working copy of the object: used later for UI editing and saving
                        populateUI(diabeticDayFromDb);
                    }
                });

            } else if (dateFromBundle != 0) { //we have the dateFromBundle but not an id, this means we are working with a new row: update the UI with a blank day with just the date set
                Log.d("DayDetail", "onCreate: dayIdFromBundle not set, dateFromBundle: " + Utils.getReadableDate(dateFromBundle));
                viewModel.tempDiabeticDay = new DiabeticDay(dateFromBundle, true);
                warningIv.setVisibility(View.GONE);
                populateUI(viewModel.tempDiabeticDay);

            } else { //uh oh
                Toast.makeText(this, "Error retrieving data for this day", Toast.LENGTH_LONG).show();
                Crashlytics.log(Log.ERROR, "DayDetail", "Error retrieving day data: dayIdFromBundle and dateFromBundle are zero!");
            }


        }

    }


    private void populateUI(DiabeticDay diabeticDay) {
        hypoglycemiaWarning = false;  //reset warnings on refresh
        hyperglycemiaWarning = false; //will recheck them after redrawing

        dateTv.setText(Utils.getReadableDate(diabeticDay.getDate()));
        //breakfast
        breakfastInclude.mealDescriptionEt.setText(diabeticDay.getBreakfast());
        breakfastInclude.mealRapidInjectionEt.setText(Utils.valueOfIntWithoutZero(diabeticDay.getBreakfastInjectionRapid()));
        breakfastInclude.mealLongInjectionEt.setText(Utils.valueOfIntWithoutZero(diabeticDay.getBreakfastInjectionLong()));
        breakfastInclude.mealExtrarapidInjectionEt.setText(Utils.valueOfIntWithoutZero(diabeticDay.getBreakfastInjectionRapidExtra()));

        breakfastInclude.mealGlycemiaBeforeEt.setText(Utils.valueOfIntWithoutZero(diabeticDay.getGlycemiaBeforeBreakfast()));
        breakfastInclude.mealGlycemiaAfterEt.setText(Utils.valueOfIntWithoutZero(diabeticDay.getGlycemiaAfterBreakfast()));
        checkForGlycemicWarnings(breakfastInclude.mealGlycemiaBeforeEt, diabeticDay.getGlycemiaBeforeBreakfast());
        checkForGlycemicWarnings(breakfastInclude.mealGlycemiaAfterEt, diabeticDay.getGlycemiaAfterBreakfast());
        //lunch
        lunchInclude.mealDescriptionEt.setText(diabeticDay.getLunch());
        lunchInclude.mealRapidInjectionEt.setText(Utils.valueOfIntWithoutZero(diabeticDay.getLunchInjectionRapid()));
        lunchInclude.mealLongInjectionEt.setText(Utils.valueOfIntWithoutZero(diabeticDay.getLunchInjectionLong()));
        lunchInclude.mealExtrarapidInjectionEt.setText(Utils.valueOfIntWithoutZero(diabeticDay.getLunchInjectionRapidExtra()));
        lunchInclude.mealGlycemiaBeforeEt.setText(Utils.valueOfIntWithoutZero(diabeticDay.getGlycemiaBeforeLunch()));
        lunchInclude.mealGlycemiaAfterEt.setText(Utils.valueOfIntWithoutZero(diabeticDay.getGlycemiaAfterLunch()));
        checkForGlycemicWarnings(lunchInclude.mealGlycemiaBeforeEt, diabeticDay.getGlycemiaBeforeLunch());
        checkForGlycemicWarnings(lunchInclude.mealGlycemiaAfterEt, diabeticDay.getGlycemiaAfterLunch());
        //dinner
        dinnerInclude.mealDescriptionEt.setText(diabeticDay.getDinner());
        dinnerInclude.mealRapidInjectionEt.setText(Utils.valueOfIntWithoutZero(diabeticDay.getDinnerInjectionRapid()));
        dinnerInclude.mealLongInjectionEt.setText(Utils.valueOfIntWithoutZero(diabeticDay.getDinnerInjectionLong()));
        dinnerInclude.mealExtrarapidInjectionEt.setText(Utils.valueOfIntWithoutZero(diabeticDay.getDinnerInjectionRapidExtra()));
        dinnerInclude.mealGlycemiaBeforeEt.setText(Utils.valueOfIntWithoutZero(diabeticDay.getGlycemiaBeforeDinner()));
        dinnerInclude.mealGlycemiaAfterEt.setText(Utils.valueOfIntWithoutZero(diabeticDay.getGlycemiaAfterDinner()));
        checkForGlycemicWarnings(dinnerInclude.mealGlycemiaBeforeEt, diabeticDay.getGlycemiaBeforeDinner());
        checkForGlycemicWarnings(dinnerInclude.mealGlycemiaAfterEt, diabeticDay.getGlycemiaAfterDinner());
        //bedtime
        bedtimeInclude.detailBedtimeExtrarapidInjectedEt.setText(Utils.valueOfIntWithoutZero(diabeticDay.getBedtimeInjectionRapidExtra()));
        bedtimeInclude.detailBedtimeGlycemiaEt.setText(Utils.valueOfIntWithoutZero(diabeticDay.getGlycemiaBedtime()));
        checkForGlycemicWarnings(bedtimeInclude.detailBedtimeGlycemiaEt, diabeticDay.getGlycemiaBedtime());
        //bottom edittexts
        detailWorkoutCardioEt.setText(diabeticDay.getWorkoutsCardio());
        detailWorkoutWeightsEt.setText(diabeticDay.getWorkoutsWeights());
        detailNotesEt.setText(diabeticDay.getNotes());

        if (hypoglycemiaWarning && !hyperglycemiaWarning) { //display warning message and warning icon if necessary
            warningTv.setText(getResources().getString(R.string.hypoglycemia));
            warningTv.setVisibility(View.VISIBLE);
            warningIv.setVisibility(View.VISIBLE);

        } else if (!hypoglycemiaWarning && hyperglycemiaWarning) {
            warningTv.setText(getResources().getString(R.string.hyperglycemia));
            warningTv.setVisibility(View.VISIBLE);
            warningIv.setVisibility(View.VISIBLE);
        } else if (hypoglycemiaWarning && hyperglycemiaWarning) {
            warningTv.setText(getResources().getString(R.string.hypohyperglycemia));
            warningTv.setVisibility(View.VISIBLE);
            warningIv.setVisibility(View.VISIBLE);
        } else {
            warningTv.setText("");
            warningTv.setVisibility(View.GONE);
            warningIv.setVisibility(View.GONE);
        }

    }

    private void setupIncludedLayouts() {
        //each layout include is a subclass we will need to instantiate

        breakfastInclude = new IncludedMealLayout();
        lunchInclude = new IncludedMealLayout();
        dinnerInclude = new IncludedMealLayout();
        bedtimeInclude = new IncludedBedtimeLayout();
        ButterKnife.bind(breakfastInclude, breakfastLayout);
        ButterKnife.bind(lunchInclude, lunchLayout);
        ButterKnife.bind(dinnerInclude, dinnerLayout);
        ButterKnife.bind(bedtimeInclude, bedtimeLayout);

        //setup background colors for the meals and bedtime
        breakfastLayout.setBackgroundColor(getResources().getColor(R.color.breakfast_bg_color));
        breakfastInclude.mealName.setText(getString(R.string.breakfast));
        lunchLayout.setBackgroundColor(getResources().getColor(R.color.lunch_bg_color));
        lunchInclude.mealName.setText(getString(R.string.lunch));
        dinnerLayout.setBackgroundColor(getResources().getColor(R.color.dinner_bg_color));
        dinnerInclude.mealName.setText(getString(R.string.dinner));

        bedtimeLayout.setBackgroundColor(getResources().getColor(R.color.bedtime_bg_color));

        //create a single big List containing all EditTexts fields
        ArrayList<EditText> editTextList = new ArrayList<>();
        editTextList.addAll(breakfastInclude.getEditTextList());
        editTextList.addAll(lunchInclude.getEditTextList());
        editTextList.addAll(dinnerInclude.getEditTextList());
        editTextList.addAll(bedtimeInclude.getEditTextList());
        editTextList.add(detailWorkoutCardioEt);
        editTextList.add(detailWorkoutWeightsEt);
        editTextList.add(detailNotesEt);

        for (EditText editText : editTextList) { //for each EditText set a common custom TextWatcher
            editText.addTextChangedListener(new CustomTextWatcher(editText));
        }


    }


    class IncludedMealLayout {
        @BindView(R.id.meal_name)
        TextView mealName;
        @BindView(R.id.meal_description_et)
        EditText mealDescriptionEt;
        @BindView(R.id.meal_rapid_injection_et)
        EditText mealRapidInjectionEt;
        @BindView(R.id.meal_long_injection_et)
        EditText mealLongInjectionEt;
        @BindView(R.id.meal_glycemia_before_et)
        EditText mealGlycemiaBeforeEt;
        @BindView(R.id.meal_extrarapid_injection_et)
        EditText mealExtrarapidInjectionEt;
        @BindView(R.id.meal_glycemia_after_et)
        EditText mealGlycemiaAfterEt;


        ArrayList<EditText> getEditTextList() { //utility method to return a list of the EditTexts in this included layout
            ArrayList<EditText> editTextList = new ArrayList<>();
            editTextList.add(mealDescriptionEt);
            editTextList.add(mealRapidInjectionEt);
            editTextList.add(mealLongInjectionEt);
            editTextList.add(mealGlycemiaBeforeEt);
            editTextList.add(mealExtrarapidInjectionEt);
            editTextList.add(mealGlycemiaAfterEt);
            return editTextList;
        }
    }

    class IncludedBedtimeLayout {
        @BindView(R.id.detail_bedtime_glycemia_et)
        EditText detailBedtimeGlycemiaEt;
        @BindView(R.id.detail_bedtime_extrarapid_injected_et)
        EditText detailBedtimeExtrarapidInjectedEt;

        ArrayList<EditText> getEditTextList() { //utility method to return a list of the EditTexts in this included layout
            ArrayList<EditText> editTextList = new ArrayList<>();
            editTextList.add(detailBedtimeGlycemiaEt);
            editTextList.add(detailBedtimeExtrarapidInjectedEt);
            return editTextList;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        saveChangesMenuItem = menu.findItem(R.id.save_changes);
        if (viewModel.unsavedChanges) { //used after a screen rotation: shows the Save tickmark if there are unsaved changes
            saveChangesMenuItem.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_changes: //the Save tickmark
                saveChangesToDb();
                return true;
            case android.R.id.home:
                if (viewModel.unsavedChanges) { //pressing up should first warn the user if there are unsaved changes
                    displayUnsavedChangesAlertDialog();
                } else {
                    NavUtils.navigateUpFromSameTask(this);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        if (viewModel.unsavedChanges) {//pressing back should first warn the user if there are unsaved changes
            displayUnsavedChangesAlertDialog();
        } else {
            super.onBackPressed();
        }
    }

    private void displayUnsavedChangesAlertDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(DayDetail.this);
        alertBuilder.setTitle(R.string.unsaved_changes).setMessage(R.string.alert_dialog_title_unsaved_changes)
                .setPositiveButton(R.string.save_changes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveChangesToDb();
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
    }


    private void saveChangesToDb() {
        if (viewModel.tempDiabeticDay.getDayId() != 0) { //we got an ID from the database: we are updating an existing row
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    appDatabase.dayDao().updateDay(viewModel.tempDiabeticDay);
                    Log.d("DayDetail", "saveChangesToDb: updating existing day with dayId: " + viewModel.tempDiabeticDay.getDayId() + " date: " + Utils.getReadableDate(viewModel.tempDiabeticDay.getDate()));
                }
            });
            populateUI(viewModel.tempDiabeticDay);//not strictly necessary but useful to refresh Hypo-Hyperglicemic warnings in the detail UI
            viewModel.unsavedChanges = false;
            saveChangesMenuItem.setVisible(false);
        } else if (viewModel.tempDiabeticDay.getDate() != 0) { //no ID, just the date: this is a new insert
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    long newDayId = appDatabase.dayDao().insertDay(viewModel.tempDiabeticDay);
                    viewModel.tempDiabeticDay.setDayId(newDayId); //save the newly created dayId, so any further save to the DB will be an update instead
                    Log.d("DayDetail", "saveChangesToDb: inserting new day with id: " + newDayId + " date: " + Utils.getReadableDate(viewModel.tempDiabeticDay.getDate()));
                }
            });
            populateUI(viewModel.tempDiabeticDay);//not strictly necessary but useful to refresh Hypo-Hyperglicemic warnings in the detail UI
            viewModel.unsavedChanges = false;
            saveChangesMenuItem.setVisible(false);
        } else {
            Toast.makeText(this, R.string.error_saving_to_the_db, Toast.LENGTH_SHORT).show();
            Crashlytics.log(Log.ERROR, "DayDetail", "Error saving to the DB: dayId and date are zero!");
            return;
        }
        Toast.makeText(this, R.string.changes_saved, Toast.LENGTH_LONG).show();
        Utils.updateWidget(this);
    }

    private void checkForGlycemicWarnings(EditText editText, int glycemia) {
        if ((glycemia > 0) && (glycemia < getResources().getInteger(R.integer.low_glycemia_threshold))) {
            editText.setTextColor(getResources().getColor(R.color.hypoglycemia));
            editText.setTypeface(null, Typeface.BOLD);
            hypoglycemiaWarning = true;

        } else if (glycemia > getResources().getInteger(R.integer.high_glycemia_threshold)) {
            editText.setTextColor(getResources().getColor(R.color.hyperglycemia));
            editText.setTypeface(null, Typeface.BOLD);
            hyperglycemiaWarning = true;

        } else { //no warnings triggered, display the EditText normally
            editText.setTextColor(getResources().getColor(android.R.color.primary_text_light));
            editText.setTypeface(null, Typeface.NORMAL);
        }
    }


    class CustomTextWatcher implements TextWatcher {
        //custom TextWatcher: stores the calling EditText so that we know later which EditText changed

        private EditText editText;

        CustomTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (saveChangesMenuItem != null) {
                saveChangesMenuItem.setVisible(true); //when making changes in an EditText show the Save tickmark
                viewModel.unsavedChanges = true;
                try {
                    Utils.checkInputsAndSetTempDiabeticDay(viewModel.tempDiabeticDay, editText, getBaseContext()); //check for input anomalies and then save UI state in viewModel
                } catch (NumberFormatException nfe) {
                    //we constrained the input types in XML, but a funny user could insert a very huge number or do other unexpected stuff...
                    Toast.makeText(DayDetail.this, getString(R.string.please_insert_a_valid_number), Toast.LENGTH_SHORT).show();
                }
            }
//          else {} //if saveChangesMenuItem is null do nothing: this means we are still initializing during a rotation event
//                  // don't set the tickmark visibility. It will be set by onCreateOptionsMenu, also don't touch unsavedChanges

        }


    }
}

