package uburti.luca.fitnessfordiabetics.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DayDao {

    @Query("SELECT * FROM DiabeticDay ORDER BY date")
    LiveData<List<DiabeticDay>> loadAllDays();

    @Query("SELECT * FROM DiabeticDay WHERE date >= :startDate ORDER BY date DESC")
    LiveData<List<DiabeticDay>> loadDaysStartingFrom(long startDate);

    @Query("SELECT * FROM DiabeticDay WHERE" +
            " (glycemiaBeforeBreakfast > 0) OR\n" +
            " (glycemiaAfterBreakfast > 0) OR\n" +
            " (glycemiaBeforeLunch > 0) OR\n" +
            " (glycemiaAfterLunch > 0) OR\n" +
            " (glycemiaBeforeDinner > 0) OR\n" +
            " (glycemiaAfterDinner > 0) OR\n" +
            " (glycemiaBedtime > 0)" +
            " ORDER BY date DESC LIMIT 1")
    DiabeticDay loadLatestDayWithGlycemiaSet(); //used to display relevant data for the Widget

    @Query("SELECT * FROM DiabeticDay WHERE" +
            " (breakfastInjectionRapid > 0) OR\n" +
            " (breakfastInjectionLong > 0) OR\n" +
            " (breakfastInjectionRapidExtra > 0) OR\n" +
            " (lunchInjectionRapid > 0) OR\n" +
            " (lunchInjectionLong > 0) OR\n" +
            " (lunchInjectionRapidExtra > 0) OR\n" +
            " (dinnerInjectionRapid > 0) OR\n" +
            " (dinnerInjectionLong > 0) OR\n" +
            " (dinnerInjectionRapidExtra > 0) OR\n" +
            " (bedtimeInjectionRapidExtra > 0)" +
            " ORDER BY date DESC LIMIT 1")
    DiabeticDay loadLatestDayWithInjectionSet();  //used to display relevant data for the Widget

    @Query("SELECT * FROM DiabeticDay WHERE dayId = :dayId")
    LiveData<DiabeticDay> loadDay(long dayId);

    @Insert()
    long insertDay(DiabeticDay diabeticDay);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateDay(DiabeticDay diabeticDay);

    @Delete
    void deleteDay(DiabeticDay diabeticDay);

    @Query("DELETE from DiabeticDay")
    void deleteAll();   //first used by the debugging menu option which overwrites the DB with random data

}