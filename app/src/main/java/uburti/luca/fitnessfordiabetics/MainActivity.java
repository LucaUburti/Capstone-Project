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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import uburti.luca.fitnessfordiabetics.ViewModel.MainActivityViewModel;
import uburti.luca.fitnessfordiabetics.database.AppDatabase;
import uburti.luca.fitnessfordiabetics.database.DiabeticDay;

public class MainActivity extends AppCompatActivity implements DayAdapter.DayClickHandler {
    public static final String DAY_ID = "DAY_ID";
    @BindView(R.id.main_rv)
    RecyclerView mainRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mainRv.setLayoutManager(layoutManager);

//        ArrayList<DiabeticDay> diabeticDays=new ArrayList<>();
//        Date date, String breakfast, int breakfastInjectionRapid, int breakfastInjectionLong, int breakfastInjectionRapidExtra, int glycemiaBeforeBreakfast, int glycemiaAfterBreakfast, String lunch, int lunchInjectionRapid, int lunchInjectionLong, int lunchInjectionRapidExtra, int glycemiaBeforeLunch, int glycemiaAfterLunch, String dinner, int dinnerInjectionRapid, int dinnerInjectionLong, int dinnerInjectionRapidExtra, int glycemiaBeforeDinner, int glycemiaAfterDinner, int bedtimeInjectionRapidExtra, int glycemiaBedtime, String workouts, String notes) {

//
//        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.HOUR_OF_DAY, 0);
//        cal.set(Calendar.MINUTE, 0);
//        cal.set(Calendar.SECOND, 0);
//        cal.set(Calendar.MILLISECOND, 0);
////        int flags= DateUtils.FORMAT_SHOW_DATE|DateUtils.FORMAT_SHOW_WEEKDAY|DateUtils.FORMAT_SHOW_YEAR;
//
//
//        for (int i = 0; i < 100; i++) {
//
//
//            diabeticDays.add(new DiabeticDay(cal.getTimeInMillis(), "Meal: 1 slice bread, 100gr red meat, 50gr broccoli", 6, 0, 0,80, 130,"Meal: 1 slice bread, 100gr red meat, 50gr broccoli",10,0,0,93,143,"Meal: 1 slice bread, 100gr red meat, 50gr broccoli",8,13,1,85,200,0,140,"Cardio: 20 min excercise bike\nWeight: 3 session 12 repetitions squats 10kg","extra snack before dinner"));
//            cal.add(Calendar.DATE, -1);
//        }


        AppDatabase appDatabase = AppDatabase.getInstance(getApplicationContext());
//        diabeticDays = appDatabase.dayDao().loadAllDays();
        MainActivityViewModel viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        viewModel.getDiabeticDays().observe(this, new Observer<List<DiabeticDay>>() {
            @Override
            public void onChanged(@Nullable List<DiabeticDay> diabeticDays) {

            }
        });



        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        int daysToRetrieve = 50; //make this a user preference
        ArrayList<DiabeticDay> diabeticDays=new ArrayList<>();
        diabeticDays.add(new DiabeticDay(cal.getTimeInMillis(), "Meal: 1 slice bread, 100gr red meat, 50gr broccoli", 6, 0, 0, 80, 130, "Meal: 1 slice bread, 100gr red meat, 50gr broccoli", 10, 0, 0, 93, 143, "Meal: 1 slice bread, 100gr red meat, 50gr broccoli", 8, 13, 1, 85, 200, 0, 140, "Cardio: 20 min excercise bike\nWeight: 3 session 12 repetitions squats 10kg", "extra snack before dinner"));
        List<Long> retrievedDates = new ArrayList<>();
        for (int i = 0; i < diabeticDays.size(); i++) {
            retrievedDates.add(diabeticDays.get(i).getDate());
        }

        for (int i = 0; i < daysToRetrieve; i++) {
            long dateToBeChecked = cal.getTimeInMillis();
            if (!retrievedDates.contains(dateToBeChecked)) { //no data for this day, add mock day to the list
                Random random = new Random();
                if (random.nextBoolean()) {
                    diabeticDays.add(new DiabeticDay(dateToBeChecked, true));
                } else {
                    diabeticDays.add(new DiabeticDay(cal.getTimeInMillis(), "Meal: 1 slice bread, 100gr red meat, 50gr broccoli", 6, 0, 0, 80, 130, "Meal: 1 slice bread, 100gr red meat, 50gr broccoli", 10, 0, 0, 93, 143, "Meal: 1 slice bread, 100gr red meat, 50gr broccoli", 8, 13, 1, 85, 200, 0, 140, "Cardio: 20 min excercise bike\nWeight: 3 session 12 repetitions squats 10kg", "extra snack before dinner"));
                }
            }
            cal.add(Calendar.DATE, -1);

        }


        DayAdapter dayAdapter = new DayAdapter(diabeticDays, this, this);
        mainRv.setAdapter(dayAdapter);
        mainRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onDayClicked(int dayId) {
        Toast.makeText(this, "clicked dayId: " + dayId, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, DayDetail.class);
        intent.putExtra(DAY_ID, dayId);
        startActivity(intent);

    }
}

