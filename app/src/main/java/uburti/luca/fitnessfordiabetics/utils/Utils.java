package uburti.luca.fitnessfordiabetics.utils;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import uburti.luca.fitnessfordiabetics.R;
import uburti.luca.fitnessfordiabetics.database.DiabeticDay;

public class Utils {

    private static int daysToRetrieve = 30;

    public static int getDaysToRetrieve() {//TODO set as user preference

//        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//        boolean TOSAccepted = sharedPrefs.getBoolean(TOS_ACCEPTED, false);

        return daysToRetrieve;
    }

    private static final int EDITTEXT_MAX_LINES = 6;

    public static void checkInputsAndSetTempDiabeticDay(DiabeticDay tempDiabeticDay, EditText editText, Context context) {
        int parentId = ((ViewGroup) editText.getParent()).getId();
        int editTextId = editText.getId();
        if (editText.getLayout().getLineCount() > EDITTEXT_MAX_LINES) {
            editText.getText().delete(editText.getText().length() - 1, editText.getText().length());
        }
        String inputText = editText.getText().toString();
        switch (parentId) {
            case R.id.breakfast_detail:
                Log.d("Utils", "parent is breakfast_detail");
                switch (editTextId) {
                    case R.id.meal_description_et:
                        tempDiabeticDay.setBreakfast(inputText);
                        break;
                    case R.id.meal_rapid_injection_et:
                        tempDiabeticDay.setBreakfastInjectionRapid(valueOfStringWithInputCheck(inputText));
                        break;
                    case R.id.meal_long_injection_et:
                        tempDiabeticDay.setBreakfastInjectionLong(valueOfStringWithInputCheck(inputText));
                        break;
                    case R.id.meal_glycemia_before_et:
                        tempDiabeticDay.setGlycemiaBeforeBreakfast(valueOfStringWithInputCheck(inputText));
                        break;
                    case R.id.meal_extrarapid_injection_et:
                        tempDiabeticDay.setBreakfastInjectionRapidExtra(valueOfStringWithInputCheck(inputText));
                        break;
                    case R.id.meal_glycemia_after_et:
                        tempDiabeticDay.setGlycemiaAfterBreakfast(valueOfStringWithInputCheck(inputText));
                        break;
                }
                break;
            case R.id.lunch_detail:
                Log.d("Utils", "parent is lunch_detail");
                switch (editTextId) {
                    case R.id.meal_description_et:
                        tempDiabeticDay.setLunch(inputText);
                        break;
                    case R.id.meal_rapid_injection_et:
                        tempDiabeticDay.setLunchInjectionRapid(valueOfStringWithInputCheck(inputText));
                        break;
                    case R.id.meal_long_injection_et:
                        tempDiabeticDay.setLunchInjectionLong(valueOfStringWithInputCheck(inputText));
                        break;
                    case R.id.meal_glycemia_before_et:
                        tempDiabeticDay.setGlycemiaBeforeLunch(valueOfStringWithInputCheck(inputText));
                        break;
                    case R.id.meal_extrarapid_injection_et:
                        tempDiabeticDay.setLunchInjectionRapidExtra(valueOfStringWithInputCheck(inputText));
                        break;
                    case R.id.meal_glycemia_after_et:
                        tempDiabeticDay.setGlycemiaAfterLunch(valueOfStringWithInputCheck(inputText));
                        break;
                }
                break;
            case R.id.dinner_detail:
                Log.d("Utils", "parent is dinner_detail");
                switch (editTextId) {
                    case R.id.meal_description_et:
                        tempDiabeticDay.setDinner(inputText);
                        break;
                    case R.id.meal_rapid_injection_et:
                        tempDiabeticDay.setDinnerInjectionRapid(valueOfStringWithInputCheck(inputText));
                        break;
                    case R.id.meal_long_injection_et:
                        tempDiabeticDay.setDinnerInjectionLong(valueOfStringWithInputCheck(inputText));
                        break;
                    case R.id.meal_glycemia_before_et:
                        tempDiabeticDay.setGlycemiaBeforeDinner(valueOfStringWithInputCheck(inputText));
                        break;
                    case R.id.meal_extrarapid_injection_et:
                        tempDiabeticDay.setDinnerInjectionRapidExtra(valueOfStringWithInputCheck(inputText));
                        break;
                    case R.id.meal_glycemia_after_et:
                        tempDiabeticDay.setGlycemiaAfterDinner(valueOfStringWithInputCheck(inputText));
                        break;
                }
                break;
            case R.id.bedtime_detail:
                Log.d("Utils", "parent is bedtime_detail");
                switch (editTextId) {
                    case R.id.detail_bedtime_extrarapid_injected_et:
                        tempDiabeticDay.setBedtimeInjectionRapidExtra(valueOfStringWithInputCheck(inputText));
                        break;
                    case R.id.detail_bedtime_glycemia_et:
                        tempDiabeticDay.setGlycemiaBedtime(valueOfStringWithInputCheck(inputText));
                        break;
                }
                break;

            case R.id.activity_day_detail_cl:
                Log.d("Utils", "parent is activity_day_detail_cl");
                switch (editTextId) {
                    case R.id.detail_workout_et:
                        tempDiabeticDay.setWorkouts(inputText);
                        break;
                    case R.id.detail_notes_et:
                        tempDiabeticDay.setNotes(inputText);
                        break;
                }
                break;
            default:
                Log.d("Utils", "updateTempDiabeticDay: view id to be saved not found. Parent: " + context.getResources().getResourceEntryName(parentId) + " item: " + context.getResources().getResourceEntryName(editTextId));
                break;

        }


    }

    public static String valueOfIntWithoutZero(int i) { //if value is 0 returns an empty string instead, used in the detail activity
        return i == 0 ? "" : String.valueOf(i);
    }

    public static String valueOfIntWithoutZeroSetDash(int i) { //if value is 0 returns a dash sign: "-", used in the main recyclerview
        return i == 0 ? "-" : String.valueOf(i);
    }

    private static int valueOfStringWithInputCheck(String s) { //if value is 0 returns an empty string instead
        int i;

        if (s.equals("")) {
            Log.d("Utils", "valueOfStringWithInputCheck: handling empty String as 0");
            i = 0;
        } else {
            i = Integer.parseInt(s);
            if (i > 999) {
                Log.d("Utils", "valueOfStringWithInputCheck: number too big");
                throw new NumberFormatException();
            }
        }

        return i;
    }

    public static String getReadableDate(long dateToBeChecked) {
        return DateFormat.getDateInstance(DateFormat.LONG).format(new Date(dateToBeChecked));
    }

    public static String getReadableDateNoYears(long dateToBeChecked, Context context) {
        int flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NO_YEAR;
        return DateUtils.formatDateTime(context, dateToBeChecked, flags);
    }

    public static String getNumericDate(long dateToBeChecked, Context context) {
        int flags = DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_NO_YEAR;
        return DateUtils.formatDateTime(context, dateToBeChecked, flags);
    }

    public static List<DiabeticDay> insertMockDays(List<DiabeticDay> diabeticDays) {
        List<Long> retrievedDates = new ArrayList<>();
        for (int i = 0; i < diabeticDays.size(); i++) { //build a list of the dates where we have actual data in the DB
            retrievedDates.add(diabeticDays.get(i).getDate());
        }

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        for (int i = 0; i < daysToRetrieve; i++) {  //check if all possible dates are present in the retrieved list
            long dateToBeChecked = cal.getTimeInMillis();
            if (!retrievedDates.contains(dateToBeChecked)) { //no data for this day in the Db
                diabeticDays.add(i, new DiabeticDay(dateToBeChecked, true)); //add an empty mock day to the list
                Log.d("MainActivity", "Adding mock day at position: " + i + ", no info found for day " + getReadableDate(dateToBeChecked));
            } else {
                Log.d("MainActivity", "Day at position: " + i + " unchanged. List already has info for day " + getReadableDate(dateToBeChecked));
            }
            cal.add(Calendar.DATE, -1);
        }
        return diabeticDays;
    }

    public static long getStartDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DATE, -getDaysToRetrieve());
        return cal.getTimeInMillis();
    }
}
