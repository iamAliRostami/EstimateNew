package com.leon.estimate_new.tables.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.leon.estimate_new.tables.NoeVagozariDictionary;

import java.util.List;

@Dao
public interface NoeVagozariDictionaryDao {

    @Query("SELECT * FROM NoeVagozariDictionary")
    List<NoeVagozariDictionary> getNoeVagozariDictionaries();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<NoeVagozariDictionary> values);
}
