package com.leon.estimate_new.tables;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "CalculationUserInput", indices = @Index(value = {"trackNumber"}, unique = true))

public class CalculationUserInput {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public boolean sent;
    public boolean ready;
    public String trackNumber;
    public String trackingId;
    public String radif;
    public int requestType;
    public String parNumber;
    public String licenceNumber;
    public String billId;
    public String neighbourBillId;
    public int zoneId;
    public String notificationMobile;
    public String selectedServicesString;
    public int qotrEnsheabId;
    public int qotrEnsheabS;
    public int noeVagozariId;
    public int taxfifId;
    public String phoneNumber;
    public String mobile;
    public String firstName;
    public String sureName;
    public int arse;
    public int arseNew;
    public int aianKol;
    public int aianKolNew;
    public int aianMaskooni;
    public int aianMaskooniNew;
    public int aianTejari;
    public int aianTejariNew;
    public int sifoon100;
    public int sifoon125;
    public int sifoon150;
    public int sifoon200;

    public int zarfiatQarardadi;
    public int tedadMaskooni;
    public int tedadMaskooniNew;
    public int tedadTejari;
    public int tedadTejariNew;
    public int tedadSaier;
    public int tedadSaierNew;
    public int tedadTaxfif;
    public String nationalId;
    public String identityCode;
    public String fatherName;
    public String postalCode;
    public boolean ensheabQeireDaem;
    public boolean adamTaxfifAb;
    public boolean adamTaxfifFazelab;
    public String address;
    public String description;
    public String shenasname;
    public int resultId;
    public double x1, x2, y1, y2;
    public double accuracy;

    public int arzeshMelk;
    public String block;
    public String arz;
    public int karbariId;
    public int pelak;
    public double x3, y3;
    public boolean sanad;
    public int sanadNumber;

    public String adamLicence;
    public String qaradad;
    public String qaradadNumber;
    @Ignore
    public ArrayList<RequestDictionary> selectedServicesObject;

    public CalculationUserInput() {
    }

    public void preparePersonal(CalculationUserInput calculationUserInput) {
        nationalId = calculationUserInput.nationalId;
        firstName = calculationUserInput.firstName;
        sureName = calculationUserInput.sureName;
        fatherName = calculationUserInput.fatherName;
        postalCode = calculationUserInput.postalCode;
        radif = calculationUserInput.radif;
        phoneNumber = calculationUserInput.phoneNumber;
        mobile = calculationUserInput.mobile;
        address = calculationUserInput.address;
        description = calculationUserInput.description;
        shenasname = calculationUserInput.shenasname;
        zoneId = calculationUserInput.zoneId;
    }

    public void fillCalculationUserInput(ExaminerDuties examinerDuties) {
        trackingId = examinerDuties.trackingId;
        requestType = Integer.parseInt(examinerDuties.requestType);
        licenceNumber = examinerDuties.licenceNumber;
        billId = examinerDuties.billId;
        neighbourBillId = examinerDuties.neighbourBillId;
        notificationMobile = examinerDuties.notificationMobile;
        identityCode = examinerDuties.identityCode;
        trackNumber = examinerDuties.trackNumber;
        sent = false;
    }

    public void updateCalculationUserInput(ExaminerDuties examinerDuty) {
        sifoon100 = examinerDuty.sifoon100;
        sifoon125 = examinerDuty.sifoon125;
        sifoon150 = examinerDuty.sifoon150;
        sifoon200 = examinerDuty.sifoon200;
        arse = examinerDuty.arseNew;
        aianKol = examinerDuty.aianKolNew;
        aianMaskooni = examinerDuty.aianMaskooniNew;
        aianTejari = examinerDuty.aianNonMaskooniNew;
        tedadMaskooni = examinerDuty.tedadMaskooniNew;
        tedadTejari = examinerDuty.tedadTejariNew;
        tedadSaier = examinerDuty.tedadSaierNew;
        arzeshMelk = examinerDuty.arzeshMelk;
        tedadTaxfif = examinerDuty.tedadTaxfif;
        zarfiatQarardadi = examinerDuty.zarfiatQarardadiNew;
        licenceNumber = examinerDuty.licenceNumber;
        karbariId = examinerDuty.karbariId;
        noeVagozariId = examinerDuty.noeVagozariId;
        qotrEnsheabId = examinerDuty.qotrEnsheabId;
        taxfifId = examinerDuty.taxfifId;
        ensheabQeireDaem = examinerDuty.isEnsheabQeirDaem;
        adamTaxfifAb = examinerDuty.adamTaxfifAb;
        adamTaxfifFazelab = examinerDuty.adamTaxfifFazelab;
        pelak = examinerDuty.pelak;
    }
}
