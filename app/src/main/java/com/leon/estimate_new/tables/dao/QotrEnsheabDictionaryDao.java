package com.leon.estimate_new.tables.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.leon.estimate_new.tables.QotrEnsheabDictionary;

import java.util.List;

@Dao
public interface QotrEnsheabDictionaryDao {
    @Query("SELECT * FROM QotrEnsheabDictionary")
    List<QotrEnsheabDictionary> getQotrEnsheabDictionaries();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<QotrEnsheabDictionary> values);
}
