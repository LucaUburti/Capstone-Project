package uburti.luca.fitnessfordiabetics.viewmodel;

import android.arch.lifecycle.ViewModel;

import uburti.luca.fitnessfordiabetics.database.DiabeticDay;


public class DayDetailViewModel extends ViewModel {
    public boolean unsavedChanges;
    public DiabeticDay tempDiabeticDay; //for storing editexts contents on rotations

    public DayDetailViewModel() {
    }


}
