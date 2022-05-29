package com.leon.estimate_new.tables;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "MapScreen", indices = @Index(value = {"id"}, unique = true))
public class MapScreen {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String billId;
    public byte[] bitmap;
}
