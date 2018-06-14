package uburti.luca.fitnessfordiabetics.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import uburti.luca.fitnessfordiabetics.database.AppDatabase;

public class MainActivityViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase appDatabase;
    private final long startDate;

    public MainActivityViewModelFactory (AppDatabase appDatabase, long startDate) { // constructor-setter
        this.appDatabase = appDatabase;
        this.startDate = startDate;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new MainActivityViewModel(appDatabase, startDate);
    }

}
