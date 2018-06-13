package uburti.luca.fitnessfordiabetics.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface DayDao {

    @Query("SELECT * FROM DiabeticDay ORDER BY date")
    List<DiabeticDay> loadAllDays();

    @Query("SELECT * FROM DiabeticDay WHERE date >= :startDate")
    List<DiabeticDay>  loadDaysStartingFrom(int startDate);

    @Insert()
    void insertDay(DiabeticDay diabeticDay);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateDay(DiabeticDay diabeticDay);

    @Delete
    void deleteDay(DiabeticDay diabeticDay);

}