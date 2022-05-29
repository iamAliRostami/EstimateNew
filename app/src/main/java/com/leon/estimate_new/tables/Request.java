package com.leon.estimate_new.tables;

import java.util.ArrayList;

public class Request {
    public String neighbourBillId;
    public String billId;
    public String address;
    public String mobile;
    public String notificationMobile;
    public String nationalId;
    public ArrayList<Integer> selectedServices;
    public String firstName;
    public String sureName;

    public Request(ArrayList<Integer> selectedServices, String neighbourBillId, String mobile,
                   String firstName, String sureName, String nationalId, String address) {
        this.neighbourBillId = neighbourBillId;
        this.selectedServices = selectedServices;
        this.firstName = firstName;
        this.sureName = sureName;
        this.mobile = mobile;
        this.nationalId = nationalId;
        this.address = address;
    }

    public Request(ArrayList<Integer> selectedServices, String billId, String notificationMobile) {
        this.selectedServices = selectedServices;
        this.billId = billId;
        this.notificationMobile = notificationMobile;
    }
}
