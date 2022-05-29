package com.leon.estimate_new.tables.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.leon.estimate_new.tables.Block;

import java.util.List;

@Dao
public interface BlockDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertBlock(Block block);

    @Query("SELECT * FROM Block WHERE zoneId =:zoneId")
    List<Block> getBlockByZoneId(int zoneId);
}
