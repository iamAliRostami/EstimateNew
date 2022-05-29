package com.leon.estimate_new.tables;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Calculation", indices = @Index(value = {"examinationId"}, unique = true))

public class Calculation {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String address;
    public String billId;
    public String examinationDay;
    public String examinationId;
    public String moshtarakMobile;
    public String nameAndFamily;
    public String neighbourBillId;
    public String notificationMobile;
    public String radif;
    public String serviceGroup;
    public String trackNumber;
    public boolean isPeymayesh;
    public boolean read;

    public Calculation(String address, String billId, String examinationDay,
                       String examinationId, boolean isPeymayesh, String moshtarakMobile,
                       String nameAndFamily, String neighbourBillId, String notificationMobile,
                       String radif, String serviceGroup, String trackNumber) {
        this.address = address;
        this.billId = billId;
        this.examinationDay = examinationDay;
        this.examinationId = examinationId;
        this.isPeymayesh = isPeymayesh;
        this.moshtarakMobile = moshtarakMobile;
        this.nameAndFamily = nameAndFamily;
        this.neighbourBillId = neighbourBillId;
        this.notificationMobile = notificationMobile;
        this.radif = radif;
        this.serviceGroup = serviceGroup;
        this.trackNumber = trackNumber;
    }
}