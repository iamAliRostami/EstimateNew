package com.leon.estimate_new.tables;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Images", indices = @Index(value = {"imageId"}, unique = true))
public class Images {
    @PrimaryKey(autoGenerate = true)
    public int imageId;
    public String address;
    public String billId;
    public String trackingNumber;
    public String docId;
    public String peygiri;
    @Ignore
    public String docTitle;
    @Ignore
    public Bitmap bitmap;
    @Ignore
    public String uri;
    @Ignore
    public boolean needSave;

    public Images(String address, String billId, String trackingNumber, String docId, String peygiri) {
        this.address = address;
        this.billId = billId;
        this.trackingNumber = trackingNumber;
        this.docId = docId;
        this.peygiri = peygiri;
    }

    public Images(String address, String billId, String trackingNumber, String docId,
                  String docTitle, Bitmap bitmap, Boolean needSave) {
        this.address = address;
        this.billId = billId;
        this.trackingNumber = trackingNumber;
        this.docId = docId;
        this.docTitle = docTitle;
        this.bitmap = bitmap;
        this.needSave = needSave;
    }

    public Images(String billId, String trackingNumber, String docTitle, String uri,
                  Bitmap bitmap, Boolean needSave) {
        this.billId = billId;
        this.trackingNumber = trackingNumber;
        this.docTitle = docTitle;
        this.uri = uri;
        this.bitmap = bitmap;
        this.needSave = needSave;
    }

    public Images(String address, String billId, String trackingNumber, int docId,
                  String docTitle, Bitmap bitmap, Boolean needSave) {
        this.address = address;
        this.billId = billId;
        this.trackingNumber = trackingNumber;
        this.docId = String.valueOf(docId);
        this.docTitle = docTitle;
        this.bitmap = bitmap;
        this.needSave = needSave;
    }
}
