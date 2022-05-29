package com.leon.estimate_new.tables;

import java.util.List;

public class CalculationInfo {
    public String trackingId;
    public String trackNumber;
    public String requestType;
    public String parNumber;
    public String billId;
    public String radif;
    public String neighbourBillId;
    public String zoneId;
    public String callerId;
    public String notificationMobile;
    public String zoneTitle;
    public String isNewEnsheab;
    public String phoneNumber;
    public String mobile;
    public String firstName;
    public String sureName;
    public String hasFazelab;
    public String fazelabInstallDate;
    public String isFinished;
    public String eshterak;
    public String arse;
    public String aianKol;
    public String aianMaskooni;
    public String aianNonMaskooni;
    public String sifoon100;
    public String sifoon125;
    public String sifoon150;
    public String sifoon200;
    public String zarfiatQarardadi;
    public String arzeshMelk;
    public String tedadMaskooni;
    public String tedadTejari;
    public String tedadSaier;
    public String tedadTaxfif;
    public String nationalId;
    public String identityCode;
    public String fatherName;
    public String postalCode;
    public String address;
    public String description;
    public boolean adamTaxfifAb;
    public boolean adamTaxfifFazelab;
    public boolean isEnsheabQeirDaem;
    public boolean hasRadif;

    public List<KarbariDictionary> karbariDictionary;
    public List<NoeVagozariDictionary> noeVagozariDictionary;
    public List<QotrEnsheabDictionary> qotrEnsheabDictionary;
    public List<TaxfifDictionary> taxfifDictionary;
    public List<ServiceDictionary> serviceDictionary;

    public CalculationInfo(String trackingId, String trackNumber, String requestType,
                           String parNumber, String billId, String radif, String neighbourBillId,
                           String zoneId, String callerId, String notificationMobile,
                           String zoneTitle, String isNewEnsheab, String phoneNumber,
                           String mobile, String firstName, String sureName, String hasFazelab,
                           String fazelabInstallDate, String isFinished, String eshterak,
                           String arse, String aianKol, String aianMaskooni, String aianNonMaskooni,
                           String sifoon100, String sifoon125, String sifoon150, String sifoon200,
                           String zarfiatQarardadi, String arzeshMelk, String tedadMaskooni,
                           String tedadTejari, String tedadSaier, String tedadTaxfif,
                           String nationalId, String identityCode, String fatherName,
                           String postalCode, String address, String description,
                           boolean adamTaxfifAb, boolean adamTaxfifFazelab, boolean isEnsheabQeirDaem,
                           boolean hasRadif, List<KarbariDictionary> karbariDictionary,
                           List<NoeVagozariDictionary> noeVagozariDictionary,
                           List<QotrEnsheabDictionary> qotrEnsheabDictionary,
                           List<TaxfifDictionary> taxfifDictionary, List<ServiceDictionary> serviceDictionary) {
        this.trackingId = trackingId;
        this.trackNumber = trackNumber;
        this.requestType = requestType;
        this.parNumber = parNumber;
        this.billId = billId;
        this.radif = radif;
        this.neighbourBillId = neighbourBillId;
        this.zoneId = zoneId;
        this.callerId = callerId;
        this.notificationMobile = notificationMobile;
        this.zoneTitle = zoneTitle;
        this.isNewEnsheab = isNewEnsheab;
        this.phoneNumber = phoneNumber;
        this.mobile = mobile;
        this.firstName = firstName;
        this.sureName = sureName;
        this.hasFazelab = hasFazelab;
        this.fazelabInstallDate = fazelabInstallDate;
        this.isFinished = isFinished;
        this.eshterak = eshterak;
        this.arse = arse;
        this.aianKol = aianKol;
        this.aianMaskooni = aianMaskooni;
        this.aianNonMaskooni = aianNonMaskooni;
        this.sifoon100 = sifoon100;
        this.sifoon125 = sifoon125;
        this.sifoon150 = sifoon150;
        this.sifoon200 = sifoon200;
        this.zarfiatQarardadi = zarfiatQarardadi;
        this.arzeshMelk = arzeshMelk;
        this.tedadMaskooni = tedadMaskooni;
        this.tedadTejari = tedadTejari;
        this.tedadSaier = tedadSaier;
        this.tedadTaxfif = tedadTaxfif;
        this.nationalId = nationalId;
        this.identityCode = identityCode;
        this.fatherName = fatherName;
        this.postalCode = postalCode;
        this.address = address;
        this.description = description;
        this.adamTaxfifAb = adamTaxfifAb;
        this.adamTaxfifFazelab = adamTaxfifFazelab;
        this.isEnsheabQeirDaem = isEnsheabQeirDaem;
        this.hasRadif = hasRadif;
        this.karbariDictionary = karbariDictionary;
        this.noeVagozariDictionary = noeVagozariDictionary;
        this.qotrEnsheabDictionary = qotrEnsheabDictionary;
        this.taxfifDictionary = taxfifDictionary;
        this.serviceDictionary = serviceDictionary;
    }
}
