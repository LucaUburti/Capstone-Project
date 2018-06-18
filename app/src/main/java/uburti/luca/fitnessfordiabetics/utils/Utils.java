package uburti.luca.fitnessfordiabetics.utils;

import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import uburti.luca.fitnessfordiabetics.DayDetail;
import uburti.luca.fitnessfordiabetics.R;
import uburti.luca.fitnessfordiabetics.database.DiabeticDay;

public class Utils {



    public static void setTempDiabeticDay(DiabeticDay tempDiabeticDay, EditText editText, long date) {
        tempDiabeticDay.setDate(date);
        int parentId = ((ViewGroup) editText.getParent()).getId();
        int editTextId = editText.getId();
        switch (parentId) {
            case R.id.breakfast_detail:
                switch (editTextId) {
                    case R.id.meal_description_et:
                        tempDiabeticDay.setBreakfast(editText.getText().toString());
                        break;
                    case R.id.meal_rapid_injection_et:
                        tempDiabeticDay.setBreakfastInjectionRapid(Integer.parseInt(editText.getText().toString()));
                        break;
                    case R.id.meal_long_injection_et:
                        tempDiabeticDay.setBreakfastInjectionLong(Integer.parseInt(editText.getText().toString()));
                        break;
                    case R.id.meal_glycemia_before_et:
                        tempDiabeticDay.setGlycemiaBeforeBreakfast(Integer.parseInt(editText.getText().toString()));
                        break;
                    case R.id.meal_extrarapid_injection_et:
                        tempDiabeticDay.setBreakfastInjectionRapidExtra(Integer.parseInt(editText.getText().toString()));
                        break;
                    case R.id.meal_glycemia_after_et:
                        tempDiabeticDay.setGlycemiaAfterBreakfast(Integer.parseInt(editText.getText().toString()));
                        break;
                }
                break;
            case R.id.lunch_detail:
                switch (editTextId) {
                    case R.id.meal_description_et:
                        tempDiabeticDay.setLunch(editText.getText().toString());
                        break;
                    case R.id.meal_rapid_injection_et:
                        tempDiabeticDay.setLunchInjectionRapid(Integer.parseInt(editText.getText().toString()));
                        break;
                    case R.id.meal_long_injection_et:
                        tempDiabeticDay.setLunchInjectionLong(Integer.parseInt(editText.getText().toString()));
                        break;
                    case R.id.meal_glycemia_before_et:
                        tempDiabeticDay.setGlycemiaBeforeLunch(Integer.parseInt(editText.getText().toString()));
                        break;
                    case R.id.meal_extrarapid_injection_et:
                        tempDiabeticDay.setLunchInjectionRapidExtra(Integer.parseInt(editText.getText().toString()));
                        break;
                    case R.id.meal_glycemia_after_et:
                        tempDiabeticDay.setGlycemiaAfterLunch(Integer.parseInt(editText.getText().toString()));
                        break;
                }
                break;
            case R.id.dinner_detail:
                switch (editTextId) {
                    case R.id.meal_description_et:
                        tempDiabeticDay.setDinner(editText.getText().toString());
                        break;
                    case R.id.meal_rapid_injection_et:
                        tempDiabeticDay.setDinnerInjectionRapid(Integer.parseInt(editText.getText().toString()));
                        break;
                    case R.id.meal_long_injection_et:
                        tempDiabeticDay.setDinnerInjectionLong(Integer.parseInt(editText.getText().toString()));
                        break;
                    case R.id.meal_glycemia_before_et:
                        tempDiabeticDay.setGlycemiaBeforeDinner(Integer.parseInt(editText.getText().toString()));
                        break;
                    case R.id.meal_extrarapid_injection_et:
                        tempDiabeticDay.setDinnerInjectionRapidExtra(Integer.parseInt(editText.getText().toString()));
                        break;
                    case R.id.meal_glycemia_after_et:
                        tempDiabeticDay.setGlycemiaAfterDinner(Integer.parseInt(editText.getText().toString()));
                        break;
                }
                break;
            case R.id.bedtime_detail:
                switch (editTextId) {
                    case R.id.detail_bedtime_extrarapid_injected_et:
                        tempDiabeticDay.setBedtimeInjectionRapidExtra(Integer.parseInt(editText.getText().toString()));
                        break;
                    case R.id.detail_bedtime_glycemia_et:
                        tempDiabeticDay.setGlycemiaBedtime(Integer.parseInt(editText.getText().toString()));
                        break;
                }
                break;

            case R.id.activity_day_detail_cl:
                switch (editTextId) {
                    case R.id.detail_workout_et:
                        tempDiabeticDay.setWorkouts(editText.getText().toString());
                        break;
                    case R.id.detail_notes_et:
                        tempDiabeticDay.setNotes(editText.getText().toString());
                        break;
                }
            default:
                Log.d("Utils", "updateTempDiabeticDay: view id to be saved not found");
                break;

        }


    }

    public static String valueOfWithoutZero(int i) { //if value is 0 returns an empty string instead
        return i == 0 ? "" : String.valueOf(i);
    }
}
