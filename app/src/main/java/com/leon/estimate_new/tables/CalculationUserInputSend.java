package com.leon.estimate_new.tables;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CalculationUserInputSend {
    public String trackingId;
    public String trackNumber;
    public String parNumber;
    public String billId;
    public String radif;
    public String neighbourBillId;
    public String notificationMobile;
    public String phoneNumber;
    public String mobile;
    public String firstName;
    public String sureName;
    public String address;
    public String description;
    public String nationalId;
    public String identityCode;
    public String fatherName;
    public String postalCode;
    public int requestType;
    public int zoneId;
    public int qotrEnsheabId;
    public int noeVagozariId;
    public int taxfifId;
    public int arse;
    public int aianKol;
    public int aianMaskooni;
    public int aianTejari;
    public int sifoon100;
    public int sifoon125;
    public int sifoon150;
    public int sifoon200;
    public int zarfiatQarardadi;
    public int tedadMaskooni;
    public int tedadTejari;

    public int tedadSaier;
    public int tedadTaxfif;
    public int resultId;
    public boolean ensheabQeireDaem;
    public boolean adamTaxfifAb;
    public boolean adamTaxfifFazelab;
    public String x1, x2, y1, y2;
    public ArrayList<Integer> selectedServices;

    public int karbariId;
    public int arzeshMelk;
    public double accuracy;
    public String eshterak;
    public int omqeZirzamin;
    public boolean chahAbBaran;
    public boolean hasMap;
    public int faseleKhakiA;
    public int faseleKhakiF;
    public int faseleAsphaultA;
    public int faseleAsphaultF;
    public int faseleSangA;
    public int faseleSangF;
    public int faseleOtherA;
    public int faseleOtherF;

    public CalculationUserInputSend(CalculationUserInput calculationUserInput, ExaminerDuties examinerDuties) {
        this.eshterak = examinerDuties.eshterak;
        this.omqeZirzamin = examinerDuties.omqeZirzamin;
        this.chahAbBaran = examinerDuties.chahAbBaran;
        this.faseleOtherF = examinerDuties.faseleOtherF;
        this.faseleOtherA = examinerDuties.faseleOtherA;
        this.faseleSangF = examinerDuties.faseleSangF;
        this.faseleSangA = examinerDuties.faseleSangA;
        this.faseleAsphaultF = examinerDuties.faseleAsphaltF;
        this.faseleAsphaultA = examinerDuties.faseleAsphaltA;
        this.faseleKhakiF = examinerDuties.faseleKhakiF;
        this.faseleKhakiA = examinerDuties.faseleKhakiA;

        this.postalCode = calculationUserInput.postalCode;
        this.fatherName = calculationUserInput.fatherName;
        this.identityCode = calculationUserInput.identityCode;
        this.trackingId = calculationUserInput.trackingId;
        this.trackNumber = calculationUserInput.trackNumber;
        this.requestType = calculationUserInput.requestType;
        this.parNumber = calculationUserInput.licenceNumber;
        this.billId = calculationUserInput.billId;
        this.radif = calculationUserInput.radif;
        this.zoneId = calculationUserInput.zoneId;
        this.notificationMobile = calculationUserInput.notificationMobile;
        this.karbariId = calculationUserInput.karbariId;
        this.qotrEnsheabId = calculationUserInput.qotrEnsheabId;
        this.noeVagozariId = calculationUserInput.noeVagozariId;
        this.taxfifId = calculationUserInput.taxfifId;
        this.mobile = calculationUserInput.mobile;
        this.firstName = calculationUserInput.firstName;
        this.sureName = calculationUserInput.sureName;
        this.arse = calculationUserInput.arse;
        this.aianKol = calculationUserInput.aianKol;
        this.aianMaskooni = calculationUserInput.aianMaskooni;
        this.aianTejari = calculationUserInput.aianTejari;
        this.sifoon100 = calculationUserInput.sifoon100;
        this.sifoon125 = calculationUserInput.sifoon125;
        this.sifoon150 = calculationUserInput.sifoon150;
        this.sifoon200 = calculationUserInput.sifoon200;
        this.zarfiatQarardadi = calculationUserInput.zarfiatQarardadi;
        this.arzeshMelk = calculationUserInput.arzeshMelk;
        this.tedadMaskooni = calculationUserInput.tedadMaskooni;
        this.tedadTejari = calculationUserInput.tedadTejari;
        this.tedadSaier = calculationUserInput.tedadSaier;
        this.tedadTaxfif = calculationUserInput.tedadTaxfif;
        this.nationalId = calculationUserInput.nationalId;
        this.ensheabQeireDaem = calculationUserInput.ensheabQeireDaem;
        this.adamTaxfifAb = calculationUserInput.adamTaxfifAb;
        this.adamTaxfifFazelab = calculationUserInput.adamTaxfifFazelab;
        this.address = calculationUserInput.address;
        this.resultId = calculationUserInput.resultId;
        this.x1 = String.valueOf(calculationUserInput.x1);
        this.x2 = String.valueOf(calculationUserInput.x2);
        this.y1 = String.valueOf(calculationUserInput.y1);
        this.y2 = String.valueOf(calculationUserInput.y2);
        this.accuracy = calculationUserInput.accuracy;
        hasMap = true;
        setSelectedServices(calculationUserInput);
    }

    private void setSelectedServices(CalculationUserInput calculationUserInput) {
        String json = calculationUserInput.selectedServicesString;
        Gson gson = new GsonBuilder().create();
        Type userListType = new TypeToken<ArrayList<RequestDictionary>>() {
        }.getType();
        ArrayList<RequestDictionary> requestDictionaryArrayList = gson.fromJson(json, userListType);
        selectedServices = new ArrayList<>();
        if (requestDictionaryArrayList != null && requestDictionaryArrayList.size() > 0) {
            for (RequestDictionary requestDictionary : requestDictionaryArrayList) {
                if (requestDictionary.isSelected) {
                    selectedServices.add(requestDictionary.id);
                }
            }
        }
    }
}
