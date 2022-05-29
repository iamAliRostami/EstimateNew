package com.leon.estimate_new.tables.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.leon.estimate_new.tables.Calculation;

import java.util.List;

@Dao
public interface CalculationDao {

    @Query("SELECT * FROM Calculation ORDER BY trackNumber desc")
    List<Calculation> fetchCalculate();

    @Query("SELECT * FROM Calculation WHERE read != '1' ORDER BY trackNumber desc ")
    List<Calculation> unreadCalculate();

    @Query("SELECT * FROM Calculation WHERE trackNumber =:trackingNumber")
    List<Calculation> getCalculate(String trackingNumber);

    @Query("UPDATE Calculation SET read = :read WHERE trackNumber = :trackNumber")
    int updateCalculation(boolean read, String trackNumber);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertCalculation(Calculation calculation);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<Calculation> values);

    @Update
    void updateCalculation(Calculation calculation);

    @Delete
    void deleteCalculation(Calculation calculation);
}
