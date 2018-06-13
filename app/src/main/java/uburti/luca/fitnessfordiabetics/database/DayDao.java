package uburti.luca.fitnessfordiabetics.database;

import android.arch.lifecycle.LiveData;
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
    LiveData<List<DiabeticDay>> loadAllDays();

    @Query("SELECT * FROM DiabeticDay WHERE date >= :startDate")
    LiveData<List<DiabeticDay>>  loadDaysStartingFrom(int startDate);

    @Query("SELECT * FROM DiabeticDay WHERE id = :dayId")
    LiveData<DiabeticDay>  loadDay(int dayId);

    @Insert()
    void insertDay(DiabeticDay diabeticDay);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateDay(DiabeticDay diabeticDay);

    @Delete
    void deleteDay(DiabeticDay diabeticDay);

}