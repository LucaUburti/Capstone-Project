package uburti.luca.fitnessfordiabetics.database;

import java.util.Date;

public class DiabeticDay {
    private int id;
    private Date date;

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

    private String Workouts;
    private String Notes;

    public DiabeticDay(int id, Date date, String breakfast, int breakfastInjectionRapid, int breakfastInjectionLong, int breakfastInjectionRapidExtra, int glycemiaBeforeBreakfast, int glycemiaAfterBreakfast, String lunch, int lunchInjectionRapid, int lunchInjectionLong, int lunchInjectionRapidExtra, int glycemiaBeforeLunch, int glycemiaAfterLunch, String dinner, int dinnerInjectionRapid, int dinnerInjectionLong, int dinnerInjectionRapidExtra, int glycemiaBeforeDinner, int glycemiaAfterDinner, int bedtimeInjectionRapidExtra, int glycemiaBedtime, String workouts, String notes) {
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
        Workouts = workouts;
        Notes = notes;
    }

    public DiabeticDay(Date date, String breakfast, int breakfastInjectionRapid, int breakfastInjectionLong, int breakfastInjectionRapidExtra, int glycemiaBeforeBreakfast, int glycemiaAfterBreakfast, String lunch, int lunchInjectionRapid, int lunchInjectionLong, int lunchInjectionRapidExtra, int glycemiaBeforeLunch, int glycemiaAfterLunch, String dinner, int dinnerInjectionRapid, int dinnerInjectionLong, int dinnerInjectionRapidExtra, int glycemiaBeforeDinner, int glycemiaAfterDinner, int bedtimeInjectionRapidExtra, int glycemiaBedtime, String workouts, String notes) {
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
        Workouts = workouts;
        Notes = notes;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
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
        return Workouts;
    }

    public String getNotes() {
        return Notes;
    }
}
