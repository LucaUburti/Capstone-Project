package uburti.luca.fitnessfordiabetics.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class DiabeticDay {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private long date;

    private String breakfast;
    private int breakfastInjectionRapid;
    private int breakfastInjectionLong;
    private int breakfastInjectionRapidExtra;
    private int glycemiaBeforeBreakfast;
    private int glycemiaAfterBreakfast;

    private String lunch;
    private int lunchInjectionRapid;
    private int lunchInjectionLong;
    private int lunchInjectionRapidExtra;
    private int glycemiaBeforeLunch;
    private int glycemiaAfterLunch;

    private String dinner;
    private int dinnerInjectionRapid;
    private int dinnerInjectionLong;
    private int dinnerInjectionRapidExtra;
    private int glycemiaBeforeDinner;
    private int glycemiaAfterDinner;

    private int bedtimeInjectionRapidExtra;
    private int glycemiaBedtime;

    private String workouts;
    private String notes;

    public boolean isBlankDay() {
        return blankDay;
    }

    public void setBlankDay(boolean blankDay) {
        this.blankDay = blankDay;
    }

    @Ignore
    private boolean blankDay=false;

    public DiabeticDay(int id, long date, String breakfast, int breakfastInjectionRapid, int breakfastInjectionLong, int breakfastInjectionRapidExtra, int glycemiaBeforeBreakfast, int glycemiaAfterBreakfast, String lunch, int lunchInjectionRapid, int lunchInjectionLong, int lunchInjectionRapidExtra, int glycemiaBeforeLunch, int glycemiaAfterLunch, String dinner, int dinnerInjectionRapid, int dinnerInjectionLong, int dinnerInjectionRapidExtra, int glycemiaBeforeDinner, int glycemiaAfterDinner, int bedtimeInjectionRapidExtra, int glycemiaBedtime, String workouts, String notes) {
        this.id = id;
        this.date = date;
        this.breakfast = breakfast;
        this.breakfastInjectionRapid = breakfastInjectionRapid;
        this.breakfastInjectionLong = breakfastInjectionLong;
        this.breakfastInjectionRapidExtra = breakfastInjectionRapidExtra;
        this.glycemiaBeforeBreakfast = glycemiaBeforeBreakfast;
        this.glycemiaAfterBreakfast = glycemiaAfterBreakfast;
        this.lunch = lunch;
        this.lunchInjectionRapid = lunchInjectionRapid;
        this.lunchInjectionLong = lunchInjectionLong;
        this.lunchInjectionRapidExtra = lunchInjectionRapidExtra;
        this.glycemiaBeforeLunch = glycemiaBeforeLunch;
        this.glycemiaAfterLunch = glycemiaAfterLunch;
        this.dinner = dinner;
        this.dinnerInjectionRapid = dinnerInjectionRapid;
        this.dinnerInjectionLong = dinnerInjectionLong;
        this.dinnerInjectionRapidExtra = dinnerInjectionRapidExtra;
        this.glycemiaBeforeDinner = glycemiaBeforeDinner;
        this.glycemiaAfterDinner = glycemiaAfterDinner;
        this.bedtimeInjectionRapidExtra = bedtimeInjectionRapidExtra;
        this.glycemiaBedtime = glycemiaBedtime;
        this.workouts = workouts;
        this.notes = notes;
    }

    @Ignore
    public DiabeticDay(long date, boolean blankDay) {
        this.date = date;
        this.blankDay = blankDay;
    }

    @Ignore
    public DiabeticDay(long date, String breakfast, int breakfastInjectionRapid, int breakfastInjectionLong, int breakfastInjectionRapidExtra, int glycemiaBeforeBreakfast, int glycemiaAfterBreakfast, String lunch, int lunchInjectionRapid, int lunchInjectionLong, int lunchInjectionRapidExtra, int glycemiaBeforeLunch, int glycemiaAfterLunch, String dinner, int dinnerInjectionRapid, int dinnerInjectionLong, int dinnerInjectionRapidExtra, int glycemiaBeforeDinner, int glycemiaAfterDinner, int bedtimeInjectionRapidExtra, int glycemiaBedtime, String workouts, String notes) {
//        this.id = id;
        this.date = date;
        this.breakfast = breakfast;
        this.breakfastInjectionRapid = breakfastInjectionRapid;
        this.breakfastInjectionLong = breakfastInjectionLong;
        this.breakfastInjectionRapidExtra = breakfastInjectionRapidExtra;
        this.glycemiaBeforeBreakfast = glycemiaBeforeBreakfast;
        this.glycemiaAfterBreakfast = glycemiaAfterBreakfast;
        this.lunch = lunch;
        this.lunchInjectionRapid = lunchInjectionRapid;
        this.lunchInjectionLong = lunchInjectionLong;
        this.lunchInjectionRapidExtra = lunchInjectionRapidExtra;
        this.glycemiaBeforeLunch = glycemiaBeforeLunch;
        this.glycemiaAfterLunch = glycemiaAfterLunch;
        this.dinner = dinner;
        this.dinnerInjectionRapid = dinnerInjectionRapid;
        this.dinnerInjectionLong = dinnerInjectionLong;
        this.dinnerInjectionRapidExtra = dinnerInjectionRapidExtra;
        this.glycemiaBeforeDinner = glycemiaBeforeDinner;
        this.glycemiaAfterDinner = glycemiaAfterDinner;
        this.bedtimeInjectionRapidExtra = bedtimeInjectionRapidExtra;
        this.glycemiaBedtime = glycemiaBedtime;
        this.workouts = workouts;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public long getDate() {
        return date;
    }

    public String getBreakfast() {
        return breakfast;
    }

    public int getBreakfastInjectionRapid() {
        return breakfastInjectionRapid;
    }

    public int getBreakfastInjectionLong() {
        return breakfastInjectionLong;
    }

    public int getBreakfastInjectionRapidExtra() {
        return breakfastInjectionRapidExtra;
    }

    public int getGlycemiaBeforeBreakfast() {
        return glycemiaBeforeBreakfast;
    }

    public int getGlycemiaAfterBreakfast() {
        return glycemiaAfterBreakfast;
    }

    public String getLunch() {
        return lunch;
    }

    public int getLunchInjectionRapid() {
        return lunchInjectionRapid;
    }

    public int getLunchInjectionLong() {
        return lunchInjectionLong;
    }

    public int getLunchInjectionRapidExtra() {
        return lunchInjectionRapidExtra;
    }

    public int getGlycemiaBeforeLunch() {
        return glycemiaBeforeLunch;
    }

    public int getGlycemiaAfterLunch() {
        return glycemiaAfterLunch;
    }

    public String getDinner() {
        return dinner;
    }

    public int getDinnerInjectionRapid() {
        return dinnerInjectionRapid;
    }

    public int getDinnerInjectionLong() {
        return dinnerInjectionLong;
    }

    public int getDinnerInjectionRapidExtra() {
        return dinnerInjectionRapidExtra;
    }

    public int getGlycemiaBeforeDinner() {
        return glycemiaBeforeDinner;
    }

    public int getGlycemiaAfterDinner() {
        return glycemiaAfterDinner;
    }

    public int getBedtimeInjectionRapidExtra() {
        return bedtimeInjectionRapidExtra;
    }

    public int getGlycemiaBedtime() {
        return glycemiaBedtime;
    }

    public String getWorkouts() {
        return workouts;
    }

    public String getNotes() {
        return notes;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setBreakfast(String breakfast) {
        this.breakfast = breakfast;
    }

    public void setBreakfastInjectionRapid(int breakfastInjectionRapid) {
        this.breakfastInjectionRapid = breakfastInjectionRapid;
    }

    public void setBreakfastInjectionLong(int breakfastInjectionLong) {
        this.breakfastInjectionLong = breakfastInjectionLong;
    }

    public void setBreakfastInjectionRapidExtra(int breakfastInjectionRapidExtra) {
        this.breakfastInjectionRapidExtra = breakfastInjectionRapidExtra;
    }

    public void setGlycemiaBeforeBreakfast(int glycemiaBeforeBreakfast) {
        this.glycemiaBeforeBreakfast = glycemiaBeforeBreakfast;
    }

    public void setGlycemiaAfterBreakfast(int glycemiaAfterBreakfast) {
        this.glycemiaAfterBreakfast = glycemiaAfterBreakfast;
    }

    public void setLunch(String lunch) {
        this.lunch = lunch;
    }

    public void setLunchInjectionRapid(int lunchInjectionRapid) {
        this.lunchInjectionRapid = lunchInjectionRapid;
    }

    public void setLunchInjectionLong(int lunchInjectionLong) {
        this.lunchInjectionLong = lunchInjectionLong;
    }

    public void setLunchInjectionRapidExtra(int lunchInjectionRapidExtra) {
        this.lunchInjectionRapidExtra = lunchInjectionRapidExtra;
    }

    public void setGlycemiaBeforeLunch(int glycemiaBeforeLunch) {
        this.glycemiaBeforeLunch = glycemiaBeforeLunch;
    }

    public void setGlycemiaAfterLunch(int glycemiaAfterLunch) {
        this.glycemiaAfterLunch = glycemiaAfterLunch;
    }

    public void setDinner(String dinner) {
        this.dinner = dinner;
    }

    public void setDinnerInjectionRapid(int dinnerInjectionRapid) {
        this.dinnerInjectionRapid = dinnerInjectionRapid;
    }

    public void setDinnerInjectionLong(int dinnerInjectionLong) {
        this.dinnerInjectionLong = dinnerInjectionLong;
    }

    public void setDinnerInjectionRapidExtra(int dinnerInjectionRapidExtra) {
        this.dinnerInjectionRapidExtra = dinnerInjectionRapidExtra;
    }

    public void setGlycemiaBeforeDinner(int glycemiaBeforeDinner) {
        this.glycemiaBeforeDinner = glycemiaBeforeDinner;
    }

    public void setGlycemiaAfterDinner(int glycemiaAfterDinner) {
        this.glycemiaAfterDinner = glycemiaAfterDinner;
    }

    public void setBedtimeInjectionRapidExtra(int bedtimeInjectionRapidExtra) {
        this.bedtimeInjectionRapidExtra = bedtimeInjectionRapidExtra;
    }

    public void setGlycemiaBedtime(int glycemiaBedtime) {
        this.glycemiaBedtime = glycemiaBedtime;
    }

    public void setWorkouts(String workouts) {
        this.workouts = workouts;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
