package uburti.luca.fitnessfordiabetics.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import uburti.luca.fitnessfordiabetics.database.AppDatabase;
import uburti.luca.fitnessfordiabetics.database.DiabeticDay;


public class DayDetailViewModel extends ViewModel {
    private LiveData<DiabeticDay> diabeticDay;

    public DayDetailViewModel(AppDatabase appDatabase, int dayId) {
        diabeticDay = appDatabase.dayDao().loadDay(dayId);
        Log.d("DayDetailViewModel", "querying DB for dayId: " + dayId);
    }

    public LiveData<DiabeticDay> getDiabeticDay() {
        return diabeticDay;
    }
}
