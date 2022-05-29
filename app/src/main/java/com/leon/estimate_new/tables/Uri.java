package com.leon.estimate_new.tables;

public class Uri {
    public String uri;
    public String billIdOrTrackNumber;

    public Uri(String uri) {
        this.uri = uri;
    }

    public Uri(String uri, String billIdOrTrackNumber) {
        this.uri = uri;
        this.billIdOrTrackNumber = billIdOrTrackNumber;
    }
}
