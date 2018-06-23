package uburti.luca.fitnessfordiabetics.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import uburti.luca.fitnessfordiabetics.database.AppDatabase;
import uburti.luca.fitnessfordiabetics.database.DiabeticDay;

public class GlycemicTrendsViewModel extends ViewModel {
    private LiveData<List<DiabeticDay>> diabeticDays;

    GlycemicTrendsViewModel(AppDatabase appDatabase, long startDate) {
        diabeticDays = appDatabase.dayDao().loadDaysStartingFrom(startDate);
        Log.d("MainActivityViewModel", "querying DB for dates starting from: " + DateFormat.getDateTimeInstance().format(new Date(startDate)));
    }

    public LiveData<List<DiabeticDay>> getDiabeticDays() {
        return diabeticDays;
    }
}
