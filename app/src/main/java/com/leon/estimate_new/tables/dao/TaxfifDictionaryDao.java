package com.leon.estimate_new.tables.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.leon.estimate_new.tables.TaxfifDictionary;

import java.util.List;

@Dao
public interface TaxfifDictionaryDao {

    @Query("SELECT * FROM TaxfifDictionary")
    List<TaxfifDictionary> getTaxfifDictionaries();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<TaxfifDictionary> values);
}
