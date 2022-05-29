package com.leon.estimate_new.tables;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CalculationInformation {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String TrackingId;
    public String TrackNumber;
    public String RequestType;
    public String ParNumber;
    public String BillId;
    public String Radif;
    public String NeighbourBillId;
    public String ZoneId;
    public String CallerId;
    public String NotificationMobile;

    public String KarbarDictionary;
    public String QotrEnsheabDictionary;
    public String NoeVagozariDictionary;
    public String TaxfifDictionary;
    public String ServiceDictionary;
    public String ZoneTitle;
    public String IsNewEnsheab;

    public String PhoneNumber;
    public String Mobile;
    public String FirstName;
    public String SureName;
    public String HasFazelab;
    public String FazelabInstallDate;
    public String IsFinished;
    public String Eshterak;

    public String Arse;
    public String AianKol;
    public String AianMaskooni;
    public String AianNonMaskooni;
    public String Sifoon100;
    public String Sifoon125;
    public String Sifoon150;
    public String Sifoon200;

    public String ZarfiatQarardadi;

    public String ArzeshMelk;
    public String TedadMaskooni;

    public String TedadTejari;
    public String TedadSaier;
    public String TedadTaxfif;
    public String NationalId;
    public String IdentityCode;
    public String FatherName;
    public String PostalCode;
    public String Address;
    public String Description;
    public String AdamTaxfifAb;
    public String AdamTaxfifFazelab;
    public String IsEnsheabQeirDaem;
    public String HasRadif;

    public boolean read;
    public boolean send;

}
