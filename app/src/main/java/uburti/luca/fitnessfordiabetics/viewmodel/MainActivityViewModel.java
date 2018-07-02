package uburti.luca.fitnessfordiabetics.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import uburti.luca.fitnessfordiabetics.database.AppDatabase;
import uburti.luca.fitnessfordiabetics.database.DiabeticDay;

public class MainActivityViewModel extends ViewModel {
    //ViewModel class for MainActivity, uses the MainActivityViewModelFactory to have a startDate in the constructor

    private LiveData<List<DiabeticDay>> diabeticDays;

    MainActivityViewModel(AppDatabase appDatabase, long startDate) {
        diabeticDays = appDatabase.dayDao().loadDaysStartingFrom(startDate);
        Log.d("MainActivityViewModel", "querying DB for dates starting from: " + DateFormat.getDateTimeInstance().format(new Date(startDate)));
    }

    public LiveData<List<DiabeticDay>> getDiabeticDays() {
        return diabeticDays;
    }
}
