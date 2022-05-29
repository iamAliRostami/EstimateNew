package com.leon.estimate_new.tables;

public class GISInfo {
    public String api;
    public String token;
    public String billId;
    public double lat;
    public double lng;

    public GISInfo(String api, String token, String billId, double lat, double lng) {
        this.api = api;
        this.token = token;
        this.billId = billId;
        this.lat = lat;
        this.lng = lng;
    }
}
