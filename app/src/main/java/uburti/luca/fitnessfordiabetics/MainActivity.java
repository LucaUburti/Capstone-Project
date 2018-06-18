package uburti.luca.fitnessfordiabetics;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uburti.luca.fitnessfordiabetics.ViewModel.MainActivityViewModel;
import uburti.luca.fitnessfordiabetics.ViewModel.MainActivityViewModelFactory;
import uburti.luca.fitnessfordiabetics.database.AppDatabase;
import uburti.luca.fitnessfordiabetics.database.DiabeticDay;
import uburti.luca.fitnessfordiabetics.utils.Utils;

import static uburti.luca.fitnessfordiabetics.utils.Utils.getReadableDate;

public class MainActivity extends AppCompatActivity implements DayAdapter.DayClickHandler {
    public static final String DAY_ID_EXTRA = "DAY_ID";
    public static final String DATE_EXTRA = "DATE";
    @BindView(R.id.main_rv)
    RecyclerView mainRv;

    int daysToRetrieve = 50; //make this a user preference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mainRv.setLayoutManager(layoutManager);

        long startDate = getStartDate();

        AppDatabase appDatabase = AppDatabase.getInstance(this);
        MainActivityViewModelFactory factory = new MainActivityViewModelFactory(appDatabase, startDate);
        MainActivityViewModel viewModel = ViewModelProviders.of(this, factory).get(MainActivityViewModel.class);
        viewModel.getDiabeticDays().observe(this, new Observer<List<DiabeticDay>>() {
            @Override
            public void onChanged(@Nullable List<DiabeticDay> diabeticDays) {
                Log.d("MainActivity", "days retrieved: " + (diabeticDays == null ? 0 : diabeticDays.size()));
                populateUI(diabeticDays);
            }
        });
    }

    private long getStartDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DATE, -daysToRetrieve);
        return cal.getTimeInMillis();
    }

    private void populateUI(List<DiabeticDay> diabeticDays) {
        for (DiabeticDay dd : diabeticDays) {
            Log.d("MainActivity", "populateUI: dateFromBundle " + getReadableDate(dd.getDate()) + " id " + dd.getDayId());
        }
        //diabeticDays.add(new DiabeticDay(cal.getTimeInMillis(), "Meal: 1 slice bread, 100gr red meat, 50gr broccoli", 6, 0, 0, 80, 130, "Meal: 1 slice bread, 100gr red meat, 50gr broccoli", 10, 0, 0, 93, 143, "Meal: 1 slice bread, 100gr red meat, 50gr broccoli", 8, 13, 1, 85, 200, 0, 140, "Cardio: 20 min excercise bike\nWeight: 3 session 12 repetitions squats 10kg", "extra snack before dinner"));
        List<Long> retrievedDates = new ArrayList<>();
        for (int i = 0; i < diabeticDays.size(); i++) {
            retrievedDates.add(diabeticDays.get(i).getDate());
        }

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        for (int i = 0; i < daysToRetrieve; i++) {
            long dateToBeChecked = cal.getTimeInMillis();
            if (!retrievedDates.contains(dateToBeChecked)) { //no data for this day in the Db
                diabeticDays.add(i, new DiabeticDay(dateToBeChecked, true)); //add mock day to the list
                Log.d("MainActivity", "Adding mock day at position: " + i + ", no info found for day " + getReadableDate(dateToBeChecked));
            } else {
                Log.d("MainActivity", "Day at position: " + i + " unchanged. List already has info for day " + getReadableDate(dateToBeChecked));
            }
            cal.add(Calendar.DATE, -1);
        }
        DayAdapter dayAdapter = new DayAdapter(diabeticDays, this, this);
        mainRv.setAdapter(dayAdapter);
        mainRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }


    @Override
    public void onDayClicked(long dayId, long date) {
        Log.d("MainActivity", "onDayClicked dayIdFromBundle: " + dayId + " dateFromBundle: " + getReadableDate(date));
        Intent intent = new Intent(MainActivity.this, DayDetail.class);
        intent.putExtra(DAY_ID_EXTRA, dayId);
        intent.putExtra(DATE_EXTRA, date);
        startActivity(intent);

    }

}

