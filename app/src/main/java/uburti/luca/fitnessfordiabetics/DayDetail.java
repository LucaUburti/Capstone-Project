package uburti.luca.fitnessfordiabetics;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import uburti.luca.fitnessfordiabetics.ViewModel.DayDetailViewModel;
import uburti.luca.fitnessfordiabetics.ViewModel.DayDetailViewModelFactory;
import uburti.luca.fitnessfordiabetics.database.AppDatabase;
import uburti.luca.fitnessfordiabetics.database.DiabeticDay;

import static uburti.luca.fitnessfordiabetics.MainActivity.DATE_EXTRA;
import static uburti.luca.fitnessfordiabetics.MainActivity.DAY_ID_EXTRA;

public class DayDetail extends AppCompatActivity {
    @BindView(R.id.detail_date_tv)
    TextView dateTv;
    @BindView(R.id.breakfast_detail)
    View breakfastLayout;
    @BindView(R.id.lunch_detail)
    View lunchLayout;
    @BindView(R.id.dinner_detail)
    View dinnerLayout;
    @BindView(R.id.detail_warning_iv)
    ImageView warningIv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_detail);
        ButterKnife.bind(this);

        setupIncludedLayouts();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int dayId = extras.getInt(DAY_ID_EXTRA);
            long date = extras.getLong(DATE_EXTRA);
            if (dayId != 0) {   //we retrieved the id, this means we are updating an existing entry
                AppDatabase appDatabase = AppDatabase.getInstance(getApplicationContext());
                DayDetailViewModelFactory factory = new DayDetailViewModelFactory(appDatabase, dayId);
                final DayDetailViewModel viewModel = ViewModelProviders.of(this, factory).get(DayDetailViewModel.class);
                viewModel.getDiabeticDay().observe(this, new Observer<DiabeticDay>() {
                    @Override
                    public void onChanged(@Nullable DiabeticDay diabeticDay) {
                        viewModel.getDiabeticDay().removeObserver(this);
                        populateUI(diabeticDay);
                    }
                });

            } else if (date != 0) { //we have the date but no an id, this means we are inserting a new entry for this date
                dateTv.setText(MainActivity.getReadableDate(date));
                warningIv.setVisibility(View.GONE);
            } else { //uh oh
                Toast.makeText(this, "Error retrieving data for this day", Toast.LENGTH_LONG).show();
                Crashlytics.log(Log.ERROR, "DayDetail", "Error retrieving day data: dayId and date are zero!");
            }


        }

    }
    private void populateUI(DiabeticDay diabeticDay) {

    }
    private void setupIncludedLayouts() {
        IncludedLayout breakfastInclude=new IncludedLayout();
        IncludedLayout lunchInclude=new IncludedLayout();
        IncludedLayout dinnerInclude=new IncludedLayout();
        ButterKnife.bind(breakfastInclude, breakfastLayout);
        ButterKnife.bind(lunchInclude, lunchLayout);
        ButterKnife.bind(dinnerInclude, dinnerLayout);

        breakfastInclude.mealName.setText(getString(R.string.breakfast));
        lunchInclude.mealName.setText(getString(R.string.lunch));
        dinnerInclude.mealName.setText(getString(R.string.dinner));
    }



    static class IncludedLayout {
        @BindView(R.id.meal_name)
        TextView mealName;
        @BindView(R.id.meal_description_et)
        EditText mealDescriptionEt;

    }

}
