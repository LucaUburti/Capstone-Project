package uburti.luca.fitnessfordiabetics;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import uburti.luca.fitnessfordiabetics.ViewModel.DayDetailViewModel;
import uburti.luca.fitnessfordiabetics.ViewModel.DayDetailViewModelFactory;
import uburti.luca.fitnessfordiabetics.ViewModel.DayDetailViewModelWithFactory;
import uburti.luca.fitnessfordiabetics.database.AppDatabase;
import uburti.luca.fitnessfordiabetics.database.DiabeticDay;
import uburti.luca.fitnessfordiabetics.utils.Utils;

import static uburti.luca.fitnessfordiabetics.MainActivity.DATE_EXTRA;
import static uburti.luca.fitnessfordiabetics.MainActivity.DAY_ID_EXTRA;

public class DayDetail extends AppCompatActivity {
    //    public static final String UNSAVED_CHANGES_KEY = "UNSAVED_CHANGES_KEY";
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
    @BindView(R.id.detail_workout_et)
    EditText detailWorkoutEt;
    @BindView(R.id.detail_notes_et)
    EditText detailNotesEt;
    IncludedMealLayout breakfastInclude;
    IncludedMealLayout lunchInclude;
    IncludedMealLayout dinnerInclude;
    IncludedBedtimeLayout bedtimeInclude;

    long dayIdFromBundle;
    long dateFromBundle;

    boolean hypoglycemiaWarning;
    boolean hyperglycemiaWarning;

    AppDatabase appDatabase;

    MenuItem saveChangesMenuItem;
    //    boolean unsavedChanges = false;
    DayDetailViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_detail);
        ButterKnife.bind(this);


        setupIncludedLayouts();
        viewModel = ViewModelProviders.of(this).get(DayDetailViewModel.class);

        appDatabase = AppDatabase.getInstance(getApplicationContext());

        Bundle extras = getIntent().getExtras();
        if (viewModel.unsavedChanges) {
            populateUI(viewModel.tempDiabeticDay);
        } else if (extras != null) {
            dayIdFromBundle = extras.getLong(DAY_ID_EXTRA);
            dateFromBundle = extras.getLong(DATE_EXTRA);
            if (dayIdFromBundle != 0) {   //we retrieved the dayIdFromBundle, this means we are working with an exiting row: update the UI with data retrieved from the DB
                Log.d("DayDetail", "onCreate: dayIdFromBundle retrieved: " + dayIdFromBundle);
                DayDetailViewModelFactory factory = new DayDetailViewModelFactory(appDatabase, dayIdFromBundle);
                Log.d("DayDetail", "onCreate: factory is: " + factory.toString());
                final DayDetailViewModelWithFactory viewModelWithFactory = ViewModelProviders.of(this, factory).get(DayDetailViewModelWithFactory.class);
                Log.d("DayDetail", "onCreate: viewModelWithFactory is: " + viewModelWithFactory.toString());
                viewModelWithFactory.getDiabeticDay().observe(this, new Observer<DiabeticDay>() {
                    @Override
                    public void onChanged(@Nullable DiabeticDay diabeticDayFromDb) {
                        viewModelWithFactory.getDiabeticDay().removeObserver(this);
                        viewModel.tempDiabeticDay = diabeticDayFromDb; //update the working copy of the object: used for UI editing
                        populateUI(diabeticDayFromDb);
                    }
                });

            } else if (dateFromBundle != 0) { //we have the dateFromBundle but not an id, this means we are working with a new row: update the UI with a blank day with just the dateFromBundle set
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
        detailWorkoutEt.setText(diabeticDay.getWorkouts());
        detailNotesEt.setText(diabeticDay.getNotes());

        if (hypoglycemiaWarning && !hyperglycemiaWarning) {
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
        breakfastInclude = new IncludedMealLayout();
        lunchInclude = new IncludedMealLayout();
        dinnerInclude = new IncludedMealLayout();
        bedtimeInclude = new IncludedBedtimeLayout();
        ButterKnife.bind(breakfastInclude, breakfastLayout);
        ButterKnife.bind(lunchInclude, lunchLayout);
        ButterKnife.bind(dinnerInclude, dinnerLayout);
        ButterKnife.bind(bedtimeInclude, bedtimeLayout);


        breakfastInclude.mealName.setText(getString(R.string.breakfast));   //these titles never change
        lunchInclude.mealName.setText(getString(R.string.lunch));
        dinnerInclude.mealName.setText(getString(R.string.dinner));


        ArrayList<EditText> editTextList = new ArrayList<>();
        editTextList.addAll(breakfastInclude.getEditTextList());
        editTextList.addAll(lunchInclude.getEditTextList());
        editTextList.addAll(dinnerInclude.getEditTextList());
        editTextList.addAll(bedtimeInclude.getEditTextList());
        editTextList.add(detailWorkoutEt);
        editTextList.add(detailNotesEt);

        //here editTextList contains all the EditText fields we have in this activity
        for (EditText editText : editTextList) {
            editText.addTextChangedListener(new CustomTextWatcher(editText)); //set a common TextWatcher
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

        ArrayList<EditText> getEditTextList() {
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

        ArrayList<EditText> getEditTextList() {
            ArrayList<EditText> editTextList = new ArrayList<>();
            editTextList.add(detailBedtimeGlycemiaEt);
            editTextList.add(detailBedtimeExtrarapidInjectedEt);
            return editTextList;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        saveChangesMenuItem = menu.findItem(R.id.save_changes);
        if (viewModel.unsavedChanges) { //on rotation
            saveChangesMenuItem.setVisible(true);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_changes:
                saveChangesToDb();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        if (viewModel.unsavedChanges) {
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
        } else {
            super.onBackPressed();
        }
    }


    private void saveChangesToDb() {
        if (viewModel.tempDiabeticDay.getDayId() != 0) { //updating an existing row
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
        } else if (viewModel.tempDiabeticDay.getDate() != 0) { //new insert
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
        updateWidget();
    }

    private void updateWidget() {
        appDatabase = AppDatabase.getInstance(getApplicationContext());
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                DiabeticDay latestDayWithGlycemiaSet = appDatabase.dayDao().loadLatestDayWithGlycemiaSet();
                DiabeticDay latestDayWithInjectionSet = appDatabase.dayDao().loadLatestDayWithInjectionSet();

                final String textToBeDisplayedInWidget = getTextToBeDisplayedInWidget(latestDayWithGlycemiaSet, latestDayWithInjectionSet);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DayDetail.this, textToBeDisplayedInWidget, Toast.LENGTH_LONG).show();
                    }
                });


            }
        });
    }

    private String getTextToBeDisplayedInWidget(DiabeticDay latestDayWithGlycemiaSet, DiabeticDay latestDayWithInjectionSet) {//TODO set string res
        String textToBeDisplayedInWidget;

        //first half of the widget text: get latest glycemia value, its date and the time of day when the measure was taken
        if (latestDayWithGlycemiaSet == null) {
            textToBeDisplayedInWidget = "No glycemic data found";
        } else {
            String latestGlycemiaValue = "";
            String latestGlycemiaTimeOfDay = "";
            long latestGlycemiaDate = latestDayWithGlycemiaSet.getDate();

            //check which value is the latest, starting from bedtime
            if (latestDayWithGlycemiaSet.getGlycemiaBedtime() > 0) {
                latestGlycemiaValue = Integer.toString(latestDayWithGlycemiaSet.getGlycemiaBedtime());
                latestGlycemiaTimeOfDay = getString(R.string.bedtime);
            } else if (latestDayWithGlycemiaSet.getGlycemiaAfterDinner() > 0) {
                latestGlycemiaValue = Integer.toString(latestDayWithGlycemiaSet.getGlycemiaAfterDinner());
                latestGlycemiaTimeOfDay = getString(R.string.after_dinner);
            } else if (latestDayWithGlycemiaSet.getGlycemiaBeforeDinner() > 0) {
                latestGlycemiaValue = Integer.toString(latestDayWithGlycemiaSet.getGlycemiaBeforeDinner());
                latestGlycemiaTimeOfDay = "Before dinner";
            } else if (latestDayWithGlycemiaSet.getGlycemiaAfterLunch() > 0) {
                latestGlycemiaValue = Integer.toString(latestDayWithGlycemiaSet.getGlycemiaAfterLunch());
                latestGlycemiaTimeOfDay = "After lunch";
            } else if (latestDayWithGlycemiaSet.getGlycemiaBeforeLunch() > 0) {
                latestGlycemiaValue = Integer.toString(latestDayWithGlycemiaSet.getGlycemiaBeforeLunch());
                latestGlycemiaTimeOfDay = "Before lunch";
            } else if (latestDayWithGlycemiaSet.getGlycemiaAfterBreakfast() > 0) {
                latestGlycemiaValue = Integer.toString(latestDayWithGlycemiaSet.getGlycemiaAfterBreakfast());
                latestGlycemiaTimeOfDay = "After breakfast";
            } else if (latestDayWithGlycemiaSet.getGlycemiaBeforeBreakfast() > 0) {
                latestGlycemiaValue = Integer.toString(latestDayWithGlycemiaSet.getGlycemiaBeforeBreakfast());
                latestGlycemiaTimeOfDay = "Before breakfast";
            }


            //first half done!
            textToBeDisplayedInWidget = getString(R.string.lastest_glycemia_header) + latestGlycemiaValue + " " + Utils.getReadableDate(latestGlycemiaDate) + " " + latestGlycemiaTimeOfDay;
        }

        //second half of the widget text: get latest injection units, the insulin type, its date and the time of day when the injection was made
        textToBeDisplayedInWidget += "\n";
        if (latestDayWithInjectionSet == null) {
            textToBeDisplayedInWidget += "No injections data found";
        } else {
            String latestInjectionValue = "";
            String latestInjectionType = "";
            String latestInjectionTimeOfDay = "";
            long latestInjectionDate = latestDayWithInjectionSet.getDate();

            //check which value is the latest, starting from bedtime
            if (latestDayWithInjectionSet.getBedtimeInjectionRapidExtra() > 0) {        //bedtime
                latestInjectionValue = Integer.toString(latestDayWithInjectionSet.getBedtimeInjectionRapidExtra());
                latestInjectionType = getString(R.string.rapidacting_extrainjection);
                latestInjectionTimeOfDay = getString(R.string.bedtime);

            } else if (latestDayWithInjectionSet.getDinnerInjectionRapidExtra() > 0) {  //dinner
                latestInjectionValue = Integer.toString(latestDayWithInjectionSet.getDinnerInjectionRapidExtra());
                latestInjectionType = getString(R.string.rapidacting_extrainjection);
                latestInjectionTimeOfDay = getString(R.string.dinner);
            } else if (latestDayWithInjectionSet.getDinnerInjectionLong() > 0) {
                latestInjectionValue = Integer.toString(latestDayWithInjectionSet.getDinnerInjectionLong());
                latestInjectionType = getString(R.string.long_acting);
                latestInjectionTimeOfDay = getString(R.string.dinner);
            } else if (latestDayWithInjectionSet.getDinnerInjectionRapid() > 0) {
                latestInjectionValue = Integer.toString(latestDayWithInjectionSet.getDinnerInjectionRapid());
                latestInjectionType = getString(R.string.rapid_acting);
                latestInjectionTimeOfDay = getString(R.string.dinner);

            } else if (latestDayWithInjectionSet.getLunchInjectionRapidExtra() > 0) {   //lunch
                latestInjectionValue = Integer.toString(latestDayWithInjectionSet.getLunchInjectionRapidExtra());
                latestInjectionType = getString(R.string.rapidacting_extrainjection);
                latestInjectionTimeOfDay = getString(R.string.lunch);
            } else if (latestDayWithInjectionSet.getLunchInjectionLong() > 0) {
                latestInjectionValue = Integer.toString(latestDayWithInjectionSet.getLunchInjectionLong());
                latestInjectionType = getString(R.string.long_acting);
                latestInjectionTimeOfDay = getString(R.string.lunch);
            } else if (latestDayWithInjectionSet.getLunchInjectionRapid() > 0) {
                latestInjectionValue = Integer.toString(latestDayWithInjectionSet.getLunchInjectionRapid());
                latestInjectionType = getString(R.string.rapid_acting);
                latestInjectionTimeOfDay = getString(R.string.lunch);

            } else if (latestDayWithInjectionSet.getBreakfastInjectionRapidExtra() > 0) {   //breakfast
                latestInjectionValue = Integer.toString(latestDayWithInjectionSet.getBreakfastInjectionRapidExtra());
                latestInjectionType = getString(R.string.rapidacting_extrainjection);
                latestInjectionTimeOfDay = getString(R.string.breakfast);
            } else if (latestDayWithInjectionSet.getBreakfastInjectionLong() > 0) {
                latestInjectionValue = Integer.toString(latestDayWithInjectionSet.getBreakfastInjectionLong());
                latestInjectionType = getString(R.string.long_acting);
                latestInjectionTimeOfDay = getString(R.string.breakfast);
            } else if (latestDayWithInjectionSet.getBreakfastInjectionRapid() > 0) {
                latestInjectionValue = Integer.toString(latestDayWithInjectionSet.getBreakfastInjectionRapid());
                latestInjectionType = getString(R.string.rapid_acting);
                latestInjectionTimeOfDay = getString(R.string.breakfast);
            }

            //second half done!
            textToBeDisplayedInWidget += getString(R.string.latest_injection_header) + latestInjectionValue + " " + latestInjectionType + " " + Utils.getReadableDate(latestInjectionDate) + " " + latestInjectionTimeOfDay;
        }

        return textToBeDisplayedInWidget;
    }

    public void checkForGlycemicWarnings(EditText editText, int glycemia) {
        if ((glycemia > 0) && (glycemia < getResources().getInteger(R.integer.low_glycemia_threshold))) {
            editText.setTextColor(getResources().getColor(R.color.hypoglycemia));
            editText.setTypeface(null, Typeface.BOLD);
            hypoglycemiaWarning = true;

        } else if (glycemia > getResources().getInteger(R.integer.high_glycemia_threshold)) {
            editText.setTextColor(getResources().getColor(R.color.hyperglycemia));
            editText.setTypeface(null, Typeface.BOLD);
            hyperglycemiaWarning = true;

        } else {
            editText.setTextColor(getResources().getColor(android.R.color.primary_text_light));
            editText.setTypeface(null, Typeface.NORMAL);
        }
    }


    class CustomTextWatcher implements TextWatcher { //custom TextWatcher: stores the calling EditText so that we later know which EditText changed
        private EditText editText;

        public CustomTextWatcher(EditText editText) {
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
                saveChangesMenuItem.setVisible(true);
                viewModel.unsavedChanges = true;
                try {
                    Utils.checkInputsAndSetTempDiabeticDay(viewModel.tempDiabeticDay, editText, getBaseContext()); //check for input anomalies and then save UI state in viewModel
                } catch (NumberFormatException nfe) {
                    //we constrained the input types in XML, but a funny user could insert a very huge number or do other unexpected stuff...
                    Toast.makeText(DayDetail.this, getString(R.string.please_insert_a_valid_number), Toast.LENGTH_SHORT).show();
                }
            }
//          else {} //if null do nothing: we are in a rotation event: don't set the tickmark visibility. It will be set by onCreateOptionsMenu, also don't touch unsavedChanges

        }


    }
}

