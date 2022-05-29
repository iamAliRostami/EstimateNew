package com.leon.estimate_new.tables.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.leon.estimate_new.tables.CalculationInformation;

import java.util.List;

@Dao
public interface CalculateInfoDao {

    @Query("SELECT * FROM CalculationInformation ORDER BY TrackingId desc")
    LiveData<List<CalculationInformation>> fetchCalculateInfo();


    @Query("SELECT * FROM CalculationInformation WHERE TrackingId =:trackingId")
    LiveData<CalculationInformation> getCalculateInfo(int trackingId);

    @Insert
    Long insertCalculateInfo(CalculationInformation calculationInformation);

    @Update
    void updateCalculateInfo(CalculationInformation calculationInformation);

    @Delete
    void deleteCalculateInfo(CalculationInformation calculationInformation);
}
