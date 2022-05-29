package com.leon.estimate_new.tables.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.leon.estimate_new.tables.ServiceDictionary;

import java.util.List;

@Dao
public interface ServiceDictionaryDao {

    @Query("SELECT * FROM ServiceDictionary Order By id")
    List<ServiceDictionary> getServiceDictionaries();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<ServiceDictionary> values);
}
