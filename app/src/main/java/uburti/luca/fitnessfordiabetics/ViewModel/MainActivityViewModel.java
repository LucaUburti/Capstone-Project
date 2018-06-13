package uburti.luca.fitnessfordiabetics.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.util.List;

import uburti.luca.fitnessfordiabetics.MainActivity;
import uburti.luca.fitnessfordiabetics.database.AppDatabase;
import uburti.luca.fitnessfordiabetics.database.DiabeticDay;

public class MainActivityViewModel extends AndroidViewModel{
    private LiveData<List<DiabeticDay>> diabeticDays;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getInstance(this.getApplication());
        Toast.makeText(this.getApplication(), "Actively reading from db...", Toast.LENGTH_LONG).show();
        diabeticDays = appDatabase.dayDao().loadAllDays();
    }

    public LiveData<List<DiabeticDay>> getDiabeticDays() {
        return diabeticDays;
    }
}
