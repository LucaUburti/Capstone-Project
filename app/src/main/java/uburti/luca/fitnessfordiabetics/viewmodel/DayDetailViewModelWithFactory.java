package uburti.luca.fitnessfordiabetics.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import uburti.luca.fitnessfordiabetics.database.AppDatabase;
import uburti.luca.fitnessfordiabetics.database.DiabeticDay;

public class DayDetailViewModelWithFactory extends ViewModel {
    //ViewModel for DayDetail. This is used with its factory class: DayDetailViewModelWithFactory so that we can pass the dayId to the constructor

    private LiveData<DiabeticDay> diabeticDay;


    DayDetailViewModelWithFactory(AppDatabase appDatabase, long dayId) {
        diabeticDay = appDatabase.dayDao().loadDay(dayId);
        Log.d("DayDetailViewModel", "querying DB for dayId: " + dayId);
    }

    public LiveData<DiabeticDay> getDiabeticDay() {
        Log.d("DayDetailViewModel", "returning diabetic day" + diabeticDay.toString());
        return diabeticDay;
    }
}
