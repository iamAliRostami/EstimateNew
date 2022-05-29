package com.leon.estimate_new.tables;

import okhttp3.MultipartBody;

public class UploadImage {
    public boolean success;
    public String error;

    public String billId;
    public String trackingNumber;
    public int docId;
    public MultipartBody.Part imageFile;

    public UploadImage(String billId, String trackingNumber, int docId, MultipartBody.Part imageFile) {
        this.billId = billId;
        this.trackingNumber = trackingNumber;
        this.docId = docId;
        this.imageFile = imageFile;
    }
}
