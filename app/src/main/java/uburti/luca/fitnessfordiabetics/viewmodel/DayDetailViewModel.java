package uburti.luca.fitnessfordiabetics.viewmodel;

import android.arch.lifecycle.ViewModel;

import uburti.luca.fitnessfordiabetics.database.DiabeticDay;


public class DayDetailViewModel extends ViewModel {
    public boolean unsavedChanges;
    public DiabeticDay tempDiabeticDay; //working copy of the DiabeticDay object, used for storing EdiTexts contents on rotations. Also used when saving to the DB

    public DayDetailViewModel() {
    }


}
