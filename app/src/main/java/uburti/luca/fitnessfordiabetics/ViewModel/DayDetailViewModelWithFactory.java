package uburti.luca.fitnessfordiabetics.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import uburti.luca.fitnessfordiabetics.database.AppDatabase;
import uburti.luca.fitnessfordiabetics.database.DiabeticDay;

public class DayDetailViewModelWithFactory extends ViewModel {
    private LiveData<DiabeticDay> diabeticDay;


    public DayDetailViewModelWithFactory(AppDatabase appDatabase, long dayId) {
        diabeticDay = appDatabase.dayDao().loadDay(dayId);
        Log.d("DayDetailViewModel", "querying DB for dayId: " + dayId);
    }

    public LiveData<DiabeticDay> getDiabeticDay() {
        Log.d("DayDetailViewModel", "returning diabetic day" + diabeticDay.toString());
        return diabeticDay;
    }
}
