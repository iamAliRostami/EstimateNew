package com.leon.estimate_new.tables.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.leon.estimate_new.tables.Formula;

import java.util.List;

@Dao
public interface FormulaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertFormula(Formula formula);

    @Query("SELECT * FROM Formula WHERE zoneId =:zoneId")
    List<Formula> getFormulaByZoneId(int zoneId);
}
