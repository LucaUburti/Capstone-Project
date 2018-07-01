package uburti.luca.fitnessfordiabetics.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import uburti.luca.fitnessfordiabetics.AppExecutors;
import uburti.luca.fitnessfordiabetics.DayDetail;
import uburti.luca.fitnessfordiabetics.R;
import uburti.luca.fitnessfordiabetics.appwidget.AppWidgetService;
import uburti.luca.fitnessfordiabetics.database.AppDatabase;
import uburti.luca.fitnessfordiabetics.database.DiabeticDay;

public class Utils {

    public static final String WIDGET_TEXT = "WIDGET_TEXT";
    private static int daysToRetrieve = 30;

    public static int getDaysToRetrieve() {
        return daysToRetrieve;
    }

    private static final int EDITTEXT_MAX_LINES = 6;

    public static void checkInputsAndSetTempDiabeticDay(DiabeticDay tempDiabeticDay, EditText editText, Context context) {
        int parentId = ((ViewGroup) editText.getParent()).getId();
        if (parentId == R.id.meal_detail_cl || parentId == R.id.bedtime_detail_cl) {   //EditTexts inside the meal/bedtime includes are 2 views deep
            parentId = ((ViewGroup) editText.getParent().getParent()).getId();
        }
        int editTextId = editText.getId();
        if (editText.getLayout().getLineCount() > EDITTEXT_MAX_LINES) {
            editText.getText().delete(editText.getText().length() - 1, editText.getText().length()); //disallow typing too many lines
        }
        String inputText = editText.getText().toString();
        switch (parentId) {
            case R.id.breakfast_detail:    //grandparent layout is breakfast_detail include
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
            case R.id.lunch_detail: //grandparent layout is lunch_detail include
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
            case R.id.dinner_detail:    //grandparent layout is dinner_detail include
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
            case R.id.bedtime_detail:   //grandparent layout is bedtime_detail include
                switch (editTextId) {
                    case R.id.detail_bedtime_extrarapid_injected_et:
                        tempDiabeticDay.setBedtimeInjectionRapidExtra(valueOfStringWithInputCheck(inputText));
                        break;
                    case R.id.detail_bedtime_glycemia_et:
                        tempDiabeticDay.setGlycemiaBedtime(valueOfStringWithInputCheck(inputText));
                        break;
                }
                break;

            case R.id.workouts_cl:   //parent layout is workout_cl
                switch (editTextId) {
                    case R.id.detail_workout_cardio_et:
                        tempDiabeticDay.setWorkoutsCardio(inputText);
                        break;
                    case R.id.detail_workout_weights_et:
                        tempDiabeticDay.setWorkoutsWeights(inputText);
                        break;
                }
                break;
            case R.id.notes_cl: //parent layout is notes_cl
                switch (editTextId) {
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
        List<DiabeticDay> diabeticDaysNoGaps = new ArrayList<>(diabeticDays);  //work on a copy, we don't want to make changes to the original List

        List<Long> retrievedDates = new ArrayList<>();
        for (int i = 0; i < diabeticDaysNoGaps.size(); i++) { //build a list of the dates where we have actual data in the DB
            retrievedDates.add(diabeticDaysNoGaps.get(i).getDate());
        }

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        for (int i = 0; i < getDaysToRetrieve(); i++) {  //check if all possible dates are present in the retrieved list
            long dateToBeChecked = cal.getTimeInMillis();
            if (!retrievedDates.contains(dateToBeChecked)) { //no data for this day in the Db
                diabeticDaysNoGaps.add(i, new DiabeticDay(dateToBeChecked, true)); //add an empty mock day to the list
                Log.d("MainActivity", "Adding mock day at position: " + i + ", no info found for day " + getReadableDate(dateToBeChecked));
            } else {
                Log.d("MainActivity", "Day at position: " + i + " unchanged. List already has info for day " + getReadableDate(dateToBeChecked));
            }
            cal.add(Calendar.DATE, -1);
        }
        return diabeticDaysNoGaps;
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



    public static void updateWidget(final Context context) {
        final AppDatabase appDatabase = AppDatabase.getInstance(context);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                DiabeticDay latestDayWithGlycemiaSet = appDatabase.dayDao().loadLatestDayWithGlycemiaSet();
                DiabeticDay latestDayWithInjectionSet = appDatabase.dayDao().loadLatestDayWithInjectionSet();

                String textToBeDisplayedInWidget = getTextToBeDisplayedInWidget(latestDayWithGlycemiaSet, latestDayWithInjectionSet, context);

                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(WIDGET_TEXT, textToBeDisplayedInWidget);
                editor.apply();         //save widget text in the Shared prefs so that it can be retrieved on reboot

                AppWidgetService.startActionSetMsg(context, textToBeDisplayedInWidget);


            }
        });
    }

    private static String getTextToBeDisplayedInWidget(DiabeticDay latestDayWithGlycemiaSet, DiabeticDay latestDayWithInjectionSet, Context context) {
        String textToBeDisplayedInWidget;

        //first half of the widget text: get latest glycemia value, its date and the time of day when the measure was taken
        if (latestDayWithGlycemiaSet == null) {
            textToBeDisplayedInWidget = context.getString(R.string.no_glycemic_data);
        } else {
            String latestGlycemiaValue = "";
            String latestGlycemiaTimeOfDay = "";
            long latestGlycemiaDate = latestDayWithGlycemiaSet.getDate();

            //check which value is the latest, starting from bedtime
            if (latestDayWithGlycemiaSet.getGlycemiaBedtime() > 0) {
                latestGlycemiaValue = Integer.toString(latestDayWithGlycemiaSet.getGlycemiaBedtime());
                latestGlycemiaTimeOfDay = context.getString(R.string.bedtime);
            } else if (latestDayWithGlycemiaSet.getGlycemiaAfterDinner() > 0) {
                latestGlycemiaValue = Integer.toString(latestDayWithGlycemiaSet.getGlycemiaAfterDinner());
                latestGlycemiaTimeOfDay = context.getString(R.string.after_dinner);
            } else if (latestDayWithGlycemiaSet.getGlycemiaBeforeDinner() > 0) {
                latestGlycemiaValue = Integer.toString(latestDayWithGlycemiaSet.getGlycemiaBeforeDinner());
                latestGlycemiaTimeOfDay = context.getString(R.string.before_dinner);
            } else if (latestDayWithGlycemiaSet.getGlycemiaAfterLunch() > 0) {
                latestGlycemiaValue = Integer.toString(latestDayWithGlycemiaSet.getGlycemiaAfterLunch());
                latestGlycemiaTimeOfDay = context.getString(R.string.after_lunch);
            } else if (latestDayWithGlycemiaSet.getGlycemiaBeforeLunch() > 0) {
                latestGlycemiaValue = Integer.toString(latestDayWithGlycemiaSet.getGlycemiaBeforeLunch());
                latestGlycemiaTimeOfDay = context.getString(R.string.before_lunch);
            } else if (latestDayWithGlycemiaSet.getGlycemiaAfterBreakfast() > 0) {
                latestGlycemiaValue = Integer.toString(latestDayWithGlycemiaSet.getGlycemiaAfterBreakfast());
                latestGlycemiaTimeOfDay = context.getString(R.string.after_breakfast);
            } else if (latestDayWithGlycemiaSet.getGlycemiaBeforeBreakfast() > 0) {
                latestGlycemiaValue = Integer.toString(latestDayWithGlycemiaSet.getGlycemiaBeforeBreakfast());
                latestGlycemiaTimeOfDay = context.getString(R.string.before_breakfast);
            }


            //first half done!
            textToBeDisplayedInWidget = context.getString(R.string.lastest_glycemia_header) +
                    latestGlycemiaValue + "\n" +
                    Utils.getNumericDate(latestGlycemiaDate, context) + " - " +
                    latestGlycemiaTimeOfDay.toLowerCase();
        }

        //second half of the widget text: get latest injection units, the insulin type, its date and the time of day when the injection was made
        textToBeDisplayedInWidget += "\n\n";

        if (latestDayWithInjectionSet == null) {
            textToBeDisplayedInWidget += context.getString(R.string.no_injections_found);
        } else {
            String latestInjectionValue = "";
            String latestInjectionType = "";
            String latestInjectionTimeOfDay = "";
            long latestInjectionDate = latestDayWithInjectionSet.getDate();

            //check which value is the latest, starting from bedtime
            if (latestDayWithInjectionSet.getBedtimeInjectionRapidExtra() > 0) {        //bedtime
                latestInjectionValue = Integer.toString(latestDayWithInjectionSet.getBedtimeInjectionRapidExtra());
                latestInjectionType = context.getString(R.string.rapidacting_extrainjection);
                latestInjectionTimeOfDay = context.getString(R.string.bedtime);

            } else if (latestDayWithInjectionSet.getDinnerInjectionRapidExtra() > 0) {  //dinner
                latestInjectionValue = Integer.toString(latestDayWithInjectionSet.getDinnerInjectionRapidExtra());
                latestInjectionType = context.getString(R.string.rapidacting_extrainjection);
                latestInjectionTimeOfDay = context.getString(R.string.dinner);
            } else if (latestDayWithInjectionSet.getDinnerInjectionLong() > 0) {
                latestInjectionValue = Integer.toString(latestDayWithInjectionSet.getDinnerInjectionLong());
                latestInjectionType = context.getString(R.string.long_acting);
                latestInjectionTimeOfDay = context.getString(R.string.dinner);
            } else if (latestDayWithInjectionSet.getDinnerInjectionRapid() > 0) {
                latestInjectionValue = Integer.toString(latestDayWithInjectionSet.getDinnerInjectionRapid());
                latestInjectionType = context.getString(R.string.rapid_acting);
                latestInjectionTimeOfDay = context.getString(R.string.dinner);

            } else if (latestDayWithInjectionSet.getLunchInjectionRapidExtra() > 0) {   //lunch
                latestInjectionValue = Integer.toString(latestDayWithInjectionSet.getLunchInjectionRapidExtra());
                latestInjectionType = context.getString(R.string.rapidacting_extrainjection);
                latestInjectionTimeOfDay = context.getString(R.string.lunch);
            } else if (latestDayWithInjectionSet.getLunchInjectionLong() > 0) {
                latestInjectionValue = Integer.toString(latestDayWithInjectionSet.getLunchInjectionLong());
                latestInjectionType = context.getString(R.string.long_acting);
                latestInjectionTimeOfDay = context.getString(R.string.lunch);
            } else if (latestDayWithInjectionSet.getLunchInjectionRapid() > 0) {
                latestInjectionValue = Integer.toString(latestDayWithInjectionSet.getLunchInjectionRapid());
                latestInjectionType = context.getString(R.string.rapid_acting);
                latestInjectionTimeOfDay = context.getString(R.string.lunch);

            } else if (latestDayWithInjectionSet.getBreakfastInjectionRapidExtra() > 0) {   //breakfast
                latestInjectionValue = Integer.toString(latestDayWithInjectionSet.getBreakfastInjectionRapidExtra());
                latestInjectionType = context.getString(R.string.rapidacting_extrainjection);
                latestInjectionTimeOfDay = context.getString(R.string.breakfast);
            } else if (latestDayWithInjectionSet.getBreakfastInjectionLong() > 0) {
                latestInjectionValue = Integer.toString(latestDayWithInjectionSet.getBreakfastInjectionLong());
                latestInjectionType = context.getString(R.string.long_acting);
                latestInjectionTimeOfDay = context.getString(R.string.breakfast);
            } else if (latestDayWithInjectionSet.getBreakfastInjectionRapid() > 0) {
                latestInjectionValue = Integer.toString(latestDayWithInjectionSet.getBreakfastInjectionRapid());
                latestInjectionType = context.getString(R.string.rapid_acting);
                latestInjectionTimeOfDay = context.getString(R.string.breakfast);
            }

            //second half done!
            textToBeDisplayedInWidget += context.getString(R.string.latest_injection_header) +
                    latestInjectionValue + " " +
                    context.getString(R.string.units) + "\n" +
                    latestInjectionType + "\n" +
                    Utils.getNumericDate(latestInjectionDate, context) + " - " +
                    latestInjectionTimeOfDay.toLowerCase();
        }

        return textToBeDisplayedInWidget;
    }

}
