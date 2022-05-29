package com.leon.estimate_new.tables.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.leon.estimate_new.tables.RequestDictionary;

import java.util.List;

@Dao
public interface RequestDictionaryDao {

    @Query("SELECT * FROM RequestDictionary")
    List<RequestDictionary> getRequestDictionaries();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<RequestDictionary> values);
}
