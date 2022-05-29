package com.leon.estimate_new.tables;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Block", indices = @Index(value = {"id"}, unique = true))
public class Block {
    @PrimaryKey
    public double id;
    public int zoneId;
    public double maskooni;
    public double tejari;
    public double edariDolati;
    public double khadamati;
    public double sanati;
    public double sayer;
    public String blockId;
}
