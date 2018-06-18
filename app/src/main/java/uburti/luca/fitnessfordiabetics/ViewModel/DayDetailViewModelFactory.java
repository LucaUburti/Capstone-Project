package uburti.luca.fitnessfordiabetics.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import uburti.luca.fitnessfordiabetics.database.AppDatabase;

public class DayDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase appDatabase;
    private final long dayId;

    public DayDetailViewModelFactory(AppDatabase appDatabase, long dayId) {
        this.appDatabase = appDatabase;
        this.dayId = dayId;
    }


    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DayDetailViewModelWithFactory(appDatabase, dayId);
    }
}
