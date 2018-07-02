package uburti.luca.fitnessfordiabetics.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.Calendar;
import java.util.Random;

import uburti.luca.fitnessfordiabetics.AppExecutors;
import uburti.luca.fitnessfordiabetics.R;
import uburti.luca.fitnessfordiabetics.database.AppDatabase;
import uburti.luca.fitnessfordiabetics.database.DiabeticDay;

public class RandomDBDataGenerator {
    // class for demo/testing/debug purposes only
    // OVERWRITES the current Database with semi-plausible gibberish
    private Context context;
    private static final int rapidInjectionMin = 2;
    private static final int rapidInjectionMax = 15;
    private static final int longInjectionMin = 18;
    private static final int longInjectionMax = 22;
    private static final int rapidExtraInjectionMin = 2;
    private static final int rapidExtraInjectionMax = 5;
    private static final int extraInjectionProbability = 15;
    private static final int glycemiaMin = 65;
    private static final int glycemiaMax = 205;

    private String[] breakfasts = {"3 Scrambled Eggs", "1 large grapefruit", "25 almonds", "2 Tbsp of peanut butter with 1 piece of toast", "1 banana", "Lean Eggs and Ham", "0% fat Greek yogurt", "Loaded Vegetable Omelet"};
    private String[] lunches = {"Chicken", "Pasta", "Cheese Burrito", "1 apple", "1 Sandwich", "Salad", "25 almonds", "Turkey Wrap", "2 Tbsp of hummus"};
    private String[] dinners = {"Salad", "Salmon", "Broccoli", "Veggie Burger and bun", "Salad with 4 Tbsp olive oil", "1 cup of brown rice", "Chicken Spinach Parm", "Chicken with Rice", "Pasta"};

    private String[] cardio = {"Bodyweight Squats 15 reps", "Reverse Lunges 15 reps", "Lateral Plank Walks 15 reps", "Inchworms 15 reps", "Step-Ups With Knee Raise 15 reps", "Plank Jacks 15 reps", "Jumping Lunges"};
    private String[] weights = {"Dead lift 5 reps", "Squat 5 reps", "Pushups 15 reps", "pull-ups 15 reps", "hand stands", "100m sprint"};
    private String[] notes = {};

    public RandomDBDataGenerator(Context context) {
        this.context = context;
    }

    public void startDBReset() {

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setTitle(R.string.only_for_testing_purposes).setMessage(R.string.loss_of_data_overwrite)
                .setPositiveButton(R.string.overwrite_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteDB();
                        populateDBWithRandomData(context);
                        Utils.updateWidget(context);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    public void startDBDelete() {

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setTitle(R.string.only_for_testing_purposes).setMessage(R.string.loss_of_data_delete)
                .setPositiveButton(R.string.delete_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteDB();
//                        populateDBWithRandomData(context);
                        Utils.updateWidget(context);
                        Intent i = ((Activity)context).getIntent();
                        ((Activity)context).finish();
                        context.startActivity(i);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }


    private void deleteDB() {
        Log.d("RandomDBDataGenerator", "deleteDB: NUKING the DB!");
        final AppDatabase appDatabase = AppDatabase.getInstance(context);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                appDatabase.dayDao().deleteAll();
            }
        });
    }


    private void populateDBWithRandomData(Context context) { //generate semi-plausible random texts, and values
        Random rand = new Random();
        final AppDatabase appDatabase = AppDatabase.getInstance(context);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        for (int i = 0; i < Utils.getDaysToRetrieve(); i++) {
            long date = cal.getTimeInMillis();
            cal.add(Calendar.DATE, -1);

            String breakfast = breakfasts[rand.nextInt(breakfasts.length)] + "\n" + breakfasts[rand.nextInt(breakfasts.length)];
            int breakfastInjectionRapid = rand.nextInt(rapidInjectionMax - rapidInjectionMin) + rapidInjectionMin;
            int breakfastInjectionLong = 0;
            int breakfastInjectionRapidExtra = rand.nextInt(100) < extraInjectionProbability ? rand.nextInt(rapidExtraInjectionMax - rapidExtraInjectionMin) + rapidExtraInjectionMin : 0;
            int glycemiaBeforeBreakfast = rand.nextInt(glycemiaMax - glycemiaMin) + glycemiaMin;
            int glycemiaAfterBreakfast = rand.nextInt(glycemiaMax - glycemiaMin) + glycemiaMin;
            String lunch = lunches[rand.nextInt(lunches.length)] + "\n" + lunches[rand.nextInt(lunches.length)];
            int lunchInjectionRapid = rand.nextInt(rapidInjectionMax - rapidInjectionMin) + rapidInjectionMin;
            int lunchInjectionLong = 0;
            int lunchInjectionRapidExtra = rand.nextInt(100) < extraInjectionProbability ? rand.nextInt(rapidExtraInjectionMax - rapidExtraInjectionMin) + rapidExtraInjectionMin : 0;
            int glycemiaBeforeLunch = rand.nextInt(glycemiaMax - glycemiaMin) + glycemiaMin;
            int glycemiaAfterLunch = rand.nextInt(glycemiaMax - glycemiaMin) + glycemiaMin;
            String dinner = dinners[rand.nextInt(dinners.length)] + "\n" + dinners[rand.nextInt(dinners.length)];
            int dinnerInjectionRapid = rand.nextInt(rapidInjectionMax - rapidInjectionMin) + rapidInjectionMin;
            int dinnerInjectionLong = rand.nextInt(longInjectionMax - longInjectionMin) + longInjectionMin;
            int dinnerInjectionRapidExtra = rand.nextInt(100) < extraInjectionProbability ? rand.nextInt(rapidExtraInjectionMax - rapidExtraInjectionMin) + rapidExtraInjectionMin : 0;
            int glycemiaBeforeDinner = rand.nextInt(glycemiaMax - glycemiaMin) + glycemiaMin;
            int glycemiaAfterDinner = rand.nextInt(glycemiaMax - glycemiaMin) + glycemiaMin;
            int bedtimeInjectionRapidExtra = rand.nextInt(100) < extraInjectionProbability ? rand.nextInt(rapidExtraInjectionMax - rapidExtraInjectionMin) + rapidExtraInjectionMin : 0;
            int glycemiaBedtime = rand.nextInt(glycemiaMax - glycemiaMin) + glycemiaMin;
            int cardioEntries = rand.nextInt(3);
            StringBuilder workoutsCardio = new StringBuilder();
            for (int j = 0; j < cardioEntries; j++) {
                workoutsCardio.append(cardio[rand.nextInt(cardio.length)]);
                if (j != cardioEntries - 1) workoutsCardio.append("\n");
            }
            int weightsEntries = rand.nextInt(3);
            StringBuilder workoutsWeights = new StringBuilder();
            for (int j = 0; j < weightsEntries; j++) {
                workoutsWeights.append(weights[rand.nextInt(weights.length)]);
                if (j != weightsEntries - 1) workoutsWeights.append("\n");
            }
            String notes = "";

            final DiabeticDay diabeticDay = new DiabeticDay(date, breakfast, breakfastInjectionRapid, breakfastInjectionLong, breakfastInjectionRapidExtra, glycemiaBeforeBreakfast, glycemiaAfterBreakfast, lunch, lunchInjectionRapid, lunchInjectionLong, lunchInjectionRapidExtra, glycemiaBeforeLunch, glycemiaAfterLunch, dinner, dinnerInjectionRapid, dinnerInjectionLong, dinnerInjectionRapidExtra, glycemiaBeforeDinner, glycemiaAfterDinner, bedtimeInjectionRapidExtra, glycemiaBedtime, workoutsCardio.toString(), workoutsWeights.toString(), notes);

            Log.d("RandomDBDataGenerator", "inserting fake data for day: " + Utils.getReadableDate(cal.getTimeInMillis()));
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    appDatabase.dayDao().insertDay(diabeticDay);
                }
            });
        }


    }

}
