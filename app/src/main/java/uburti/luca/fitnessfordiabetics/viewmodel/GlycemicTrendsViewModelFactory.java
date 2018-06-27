package uburti.luca.fitnessfordiabetics.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import uburti.luca.fitnessfordiabetics.database.AppDatabase;

public class GlycemicTrendsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase appDatabase;
    private final long startDate;

    public GlycemicTrendsViewModelFactory(AppDatabase appDatabase, long startDate) {
        this.appDatabase = appDatabase;
        this.startDate = startDate;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new GlycemicTrendsViewModel(appDatabase, startDate);
    }


}
