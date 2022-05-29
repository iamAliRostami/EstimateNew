package com.leon.estimate_new.tables.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.leon.estimate_new.tables.ResultDictionary;

import java.util.List;

@Dao
public interface ResultDictionaryDao {
    @Query("SELECT * FROM ResultDictionary")
    List<ResultDictionary> getResults();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertResult(ResultDictionary calculation);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<ResultDictionary> values);
}
