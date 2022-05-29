package com.leon.estimate_new.tables;

public class AddDocument {
    public String trackNumber;
    public String firstName;
    public String sureName;
    public String address;
    public String zoneId;

    public String data;
    public String error;
    public boolean success;

    public AddDocument(String trackNumber, String firstName, String sureName, String address, String zoneId) {
        this.trackNumber = trackNumber;
        this.firstName = firstName;
        this.sureName = sureName;
        this.address = address;
        this.zoneId = zoneId;
    }
}
