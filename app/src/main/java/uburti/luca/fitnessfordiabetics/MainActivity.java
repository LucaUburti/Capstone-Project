package uburti.luca.fitnessfordiabetics;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import uburti.luca.fitnessfordiabetics.database.DiabeticDay;

public class MainActivity extends AppCompatActivity implements DayAdapter.DayClickHandler {
    @BindView(R.id.main_rv)
    RecyclerView mainRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mainRv.setLayoutManager(layoutManager);

        ArrayList<DiabeticDay> diabeticDays=new ArrayList<>();
//        Date date, String breakfast, int breakfastInjectionRapid, int breakfastInjectionLong, int breakfastInjectionRapidExtra, int glycemiaBeforeBreakfast, int glycemiaAfterBreakfast, String lunch, int lunchInjectionRapid, int lunchInjectionLong, int lunchInjectionRapidExtra, int glycemiaBeforeLunch, int glycemiaAfterLunch, String dinner, int dinnerInjectionRapid, int dinnerInjectionLong, int dinnerInjectionRapidExtra, int glycemiaBeforeDinner, int glycemiaAfterDinner, int bedtimeInjectionRapidExtra, int glycemiaBedtime, String workouts, String notes) {
        for (int i = 0; i < 100; i++) {
            diabeticDays.add(new DiabeticDay(Calendar.getInstance().getTime(), "Meal: 1 slice bread, 100gr red meat, 50gr broccoli", 6, 0, 0,80, 130,"Meal: 1 slice bread, 100gr red meat, 50gr broccoli",10,0,0,93,143,"Meal: 1 slice bread, 100gr red meat, 50gr broccoli",8,13,1,85,200,0,140,"Cardio: 20 min excercise bike\nWeight: 3 session 12 repetitions squats 10kg","extra snack before dinner"));
        }

        DayAdapter dayAdapter= new DayAdapter(diabeticDays, this, this);
        mainRv.setAdapter(dayAdapter);
    }

    @Override
    public void onDayClicked(int clickedItemIndex) {
        Toast.makeText(this, "clicked item no.: "+clickedItemIndex, Toast.LENGTH_SHORT).show();
    }
}
