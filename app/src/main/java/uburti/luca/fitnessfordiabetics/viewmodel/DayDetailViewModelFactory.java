package uburti.luca.fitnessfordiabetics.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import uburti.luca.fitnessfordiabetics.database.AppDatabase;

public class DayDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    //Factory ViewModel class for DayDetail: used to pass the dayID to the viewmodel constructor

    private final AppDatabase appDatabase;
    private final long dayId;

    public DayDetailViewModelFactory(AppDatabase appDatabase, long dayId) {
        this.appDatabase = appDatabase;
        this.dayId = dayId;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DayDetailViewModelWithFactory(appDatabase, dayId);
    }
}
