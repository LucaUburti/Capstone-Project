package uburti.luca.fitnessfordiabetics.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import uburti.luca.fitnessfordiabetics.MainActivity;
import uburti.luca.fitnessfordiabetics.database.AppDatabase;
import uburti.luca.fitnessfordiabetics.database.DiabeticDay;

public class MainActivityViewModel extends ViewModel {
    private LiveData<List<DiabeticDay>> diabeticDays;

    public MainActivityViewModel(AppDatabase appDatabase, long startDate) {
        diabeticDays = appDatabase.dayDao().loadDaysStartingFrom(startDate);
        Log.d("MainActivityViewModel", "querying DB for dates starting from: "+ DateFormat.getDateTimeInstance().format(new Date(startDate)));
    }

    public LiveData<List<DiabeticDay>> getDiabeticDays() {
        return diabeticDays;
    }
}
