package com.leon.estimate_new.tables;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "QotrSifoonDictionary", indices = @Index(value = {"id"}, unique = true))
public class QotrSifoonDictionary {
    @PrimaryKey
    public int id;
    public String title;
    public boolean isSelected;
    public boolean isDisabled;
    public boolean hasSms;

    public QotrSifoonDictionary(int id, String title, boolean isSelected, boolean isDisabled, boolean hasSms) {
        this.id = id;
        this.title = title;
        this.isSelected = isSelected;
        this.isDisabled = isDisabled;
        this.hasSms = hasSms;
    }

}
