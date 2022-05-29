package com.leon.estimate_new.tables.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.leon.estimate_new.tables.KarbariDictionary;

import java.util.List;

@Dao
public interface KarbariDictionaryDao {

    @Query("SELECT * FROM KarbariDictionary")
    List<KarbariDictionary> getKarbariDictionary();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<KarbariDictionary> values);
}
