package com.leon.estimate_new.tables.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.leon.estimate_new.tables.QotrSifoonDictionary;

import java.util.List;

@Dao
public interface QotrSifoonDictionaryDao {

    @Query("SELECT * FROM QotrSifoonDictionary")
    List<QotrSifoonDictionary> getQotrSifoonDictionaries();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<QotrSifoonDictionary> values);
}
