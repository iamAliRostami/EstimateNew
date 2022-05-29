package com.leon.estimate_new.tables.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.leon.estimate_new.tables.MapScreen;

import java.util.List;

@Dao
public interface MapScreenDao {

    @Query("SELECT * FROM MapScreen")
    List<MapScreen> getMapScreen();
}