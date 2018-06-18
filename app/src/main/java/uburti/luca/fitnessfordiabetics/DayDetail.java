package uburti.luca.fitnessfordiabetics;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
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
import uburti.luca.fitnessfordiabetics.database.AppDatabase;
import uburti.luca.fitnessfordiabetics.database.DiabeticDay;
import uburti.luca.fitnessfordiabetics.utils.Utils;

import static uburti.luca.fitnessfordiabetics.MainActivity.DATE_EXTRA;
import static uburti.luca.fitnessfordiabetics.MainActivity.DAY_ID_EXTRA;

public class DayDetail extends AppCompatActivity {
    public static final String UNSAVED_CHANGES_KEY = "UNSAVED_CHANGES_KEY";
    @BindView(R.id.detail_date_tv)
    TextView dateTv;
    @BindView(R.id.breakfast_detail)
    View breakfastLayout;
    @BindView(R.id.lunch_detail)
    View lunchLayout;
    @BindView(R.id.dinner_detail)
    View dinnerLayout;
    @BindView(R.id.bedtime_detail)
    View bedtimeLayout;
    @BindView(R.id.detail_warning_iv)
    ImageView warningIv;
    @BindView(R.id.detail_workout_et)
    EditText detailWorkoutEt;
    @BindView(R.id.detail_notes_et)
    EditText detailNotesEt;
    IncludedMealLayout breakfastInclude;
    IncludedMealLayout lunchInclude;
    IncludedMealLayout dinnerInclude;
    IncludedBedtimeLayout bedtimeInclude;

    int dayId;
    long date;

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


        Bundle extras = getIntent().getExtras();
        if (viewModel.unsavedChanges) {
            populateUI(viewModel.tempDiabeticDay);
        } else if (extras != null) {
            dayId = extras.getInt(DAY_ID_EXTRA);
            date = extras.getLong(DATE_EXTRA);
            if (dayId != 0) {   //we retrieved the id, this means we are updating an existing entry
                AppDatabase appDatabase = AppDatabase.getInstance(getApplicationContext());
                DayDetailViewModelFactory factory = new DayDetailViewModelFactory(appDatabase, dayId);
                final DayDetailViewModel viewModelWithFactory = ViewModelProviders.of(this, factory).get(DayDetailViewModel.class);
                viewModelWithFactory.getDiabeticDay().observe(this, new Observer<DiabeticDay>() {
                    @Override
                    public void onChanged(@Nullable DiabeticDay diabeticDayFromDb) {
                        viewModelWithFactory.getDiabeticDay().removeObserver(this);
                        populateUI(diabeticDayFromDb);
                    }
                });

            } else if (date != 0) { //we have the date but not an id, this means we are inserting a new entry for this date
                viewModel.tempDiabeticDay = new DiabeticDay(date, true);
                dateTv.setText(MainActivity.getReadableDate(date));
                warningIv.setVisibility(View.GONE);
                populateUI(viewModel.tempDiabeticDay);
            } else { //uh oh
                Toast.makeText(this, "Error retrieving data for this day", Toast.LENGTH_LONG).show();
                Crashlytics.log(Log.ERROR, "DayDetail", "Error retrieving day data: dayId and date are zero!");
            }


        }

    }



    private void populateUI(DiabeticDay diabeticDay) {
        //breakfast
        breakfastInclude.mealDescriptionEt.setText(diabeticDay.getBreakfast());
        breakfastInclude.mealRapidInjectionEt.setText(Utils.valueOfWithoutZero(diabeticDay.getBreakfastInjectionRapid()));
        breakfastInclude.mealLongInjectionEt.setText(Utils.valueOfWithoutZero(diabeticDay.getBreakfastInjectionLong()));
        breakfastInclude.mealExtrarapidInjectionEt.setText(Utils.valueOfWithoutZero(diabeticDay.getBreakfastInjectionRapidExtra()));
        breakfastInclude.mealGlycemiaBeforeEt.setText(Utils.valueOfWithoutZero(diabeticDay.getGlycemiaBeforeBreakfast()));
        breakfastInclude.mealGlycemiaAfterEt.setText(Utils.valueOfWithoutZero(diabeticDay.getGlycemiaAfterBreakfast()));
        //lunch
        lunchInclude.mealDescriptionEt.setText(diabeticDay.getLunch());
        lunchInclude.mealRapidInjectionEt.setText(Utils.valueOfWithoutZero(diabeticDay.getLunchInjectionRapid()));
        lunchInclude.mealLongInjectionEt.setText(Utils.valueOfWithoutZero(diabeticDay.getLunchInjectionLong()));
        lunchInclude.mealExtrarapidInjectionEt.setText(Utils.valueOfWithoutZero(diabeticDay.getLunchInjectionRapidExtra()));
        lunchInclude.mealGlycemiaBeforeEt.setText(Utils.valueOfWithoutZero(diabeticDay.getGlycemiaBeforeLunch()));
        lunchInclude.mealGlycemiaAfterEt.setText(Utils.valueOfWithoutZero(diabeticDay.getGlycemiaAfterLunch()));
        //dinner
        dinnerInclude.mealDescriptionEt.setText(diabeticDay.getDinner());
        dinnerInclude.mealRapidInjectionEt.setText(Utils.valueOfWithoutZero(diabeticDay.getDinnerInjectionRapid()));
        dinnerInclude.mealLongInjectionEt.setText(Utils.valueOfWithoutZero(diabeticDay.getDinnerInjectionLong()));
        dinnerInclude.mealExtrarapidInjectionEt.setText(Utils.valueOfWithoutZero(diabeticDay.getDinnerInjectionRapidExtra()));
        dinnerInclude.mealGlycemiaBeforeEt.setText(Utils.valueOfWithoutZero(diabeticDay.getGlycemiaBeforeDinner()));
        dinnerInclude.mealGlycemiaAfterEt.setText(Utils.valueOfWithoutZero(diabeticDay.getGlycemiaAfterDinner()));
        //bedtime
        bedtimeInclude.detailBedtimeExtrarapidInjectedEt.setText(Utils.valueOfWithoutZero(diabeticDay.getBedtimeInjectionRapidExtra()));
        bedtimeInclude.detailBedtimeGlycemiaEt.setText(Utils.valueOfWithoutZero(diabeticDay.getGlycemiaBedtime()));
        //bottom edittexts
        detailWorkoutEt.setText(diabeticDay.getWorkouts());
        detailNotesEt.setText(diabeticDay.getNotes());
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

    static class IncludedBedtimeLayout {
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
                //TODO save changes to DB
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (viewModel.unsavedChanges) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(DayDetail.this);
            alertBuilder.setTitle("Unsaved changes").setMessage("You have unsaved changes on this page. Do you want to save changes before going back?")
                    .setPositiveButton("Save changes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //TODO save changes to DB
                            finish();
                        }
                    })
                    .setNeutralButton("Stay on this page", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("Discard changes", new DialogInterface.OnClickListener() {
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


    class CustomTextWatcher implements TextWatcher {
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
                Utils.setTempDiabeticDay(viewModel.tempDiabeticDay, editText, date); //save UI state in viewModel
            } else {
                //if null do nothing: we are in a rotation event: don't set the tickmark visibility. It  will be set by onCreateOptionsMenu, also don't touch unsavedChanges
            }
        }

        public class ViewInfo {
            int parentId;
            int editTextId;
            String editTextContent;

            public ViewInfo(int parentId, int editTextId, String editTextContent) {
                this.parentId = parentId;
                this.editTextId = editTextId;
                this.editTextContent = editTextContent;
            }
        }
    }
}

