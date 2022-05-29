package com.leon.estimate_new.tables.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.leon.estimate_new.tables.Zarib;

import java.util.List;

@Dao
public interface ZaribDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertZarib(Zarib zarib);

    @Query("SELECT * FROM Zarib WHERE zoneId =:zoneId")
    List<Zarib> getZaribByZoneId(int zoneId);
}
