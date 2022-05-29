package com.leon.estimate_new.utils;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.leon.estimate_new.tables.Block;
import com.leon.estimate_new.tables.Calculation;
import com.leon.estimate_new.tables.CalculationInformation;
import com.leon.estimate_new.tables.CalculationUserInput;
import com.leon.estimate_new.tables.ExaminerDuties;
import com.leon.estimate_new.tables.Formula;
import com.leon.estimate_new.tables.Images;
import com.leon.estimate_new.tables.KarbariDictionary;
import com.leon.estimate_new.tables.MapScreen;
import com.leon.estimate_new.tables.NoeVagozariDictionary;
import com.leon.estimate_new.tables.QotrEnsheabDictionary;
import com.leon.estimate_new.tables.QotrSifoonDictionary;
import com.leon.estimate_new.tables.RequestDictionary;
import com.leon.estimate_new.tables.ResultDictionary;
import com.leon.estimate_new.tables.ServiceDictionary;
import com.leon.estimate_new.tables.TaxfifDictionary;
import com.leon.estimate_new.tables.Tejariha;
import com.leon.estimate_new.tables.Zarib;
import com.leon.estimate_new.tables.dao.BlockDao;
import com.leon.estimate_new.tables.dao.CalculateInfoDao;
import com.leon.estimate_new.tables.dao.CalculationDao;
import com.leon.estimate_new.tables.dao.CalculationUserInputDao;
import com.leon.estimate_new.tables.dao.ExaminerDutiesDao;
import com.leon.estimate_new.tables.dao.FormulaDao;
import com.leon.estimate_new.tables.dao.ImagesDao;
import com.leon.estimate_new.tables.dao.KarbariDictionaryDao;
import com.leon.estimate_new.tables.dao.MapScreenDao;
import com.leon.estimate_new.tables.dao.NoeVagozariDictionaryDao;
import com.leon.estimate_new.tables.dao.QotrEnsheabDictionaryDao;
import com.leon.estimate_new.tables.dao.QotrSifoonDictionaryDao;
import com.leon.estimate_new.tables.dao.RequestDictionaryDao;
import com.leon.estimate_new.tables.dao.ResultDictionaryDao;
import com.leon.estimate_new.tables.dao.ServiceDictionaryDao;
import com.leon.estimate_new.tables.dao.TaxfifDictionaryDao;
import com.leon.estimate_new.tables.dao.TejarihaDao;
import com.leon.estimate_new.tables.dao.ZaribDao;

import org.jetbrains.annotations.NotNull;

@Database(entities = {CalculationInformation.class, Calculation.class, CalculationUserInput.class,
        TaxfifDictionary.class, ServiceDictionary.class, KarbariDictionary.class, ExaminerDuties.class,
        QotrSifoonDictionary.class, QotrEnsheabDictionary.class, NoeVagozariDictionary.class,
        RequestDictionary.class, Images.class, MapScreen.class, ResultDictionary.class,
        Tejariha.class, Zarib.class, Formula.class, Block.class},
        version = 1, exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {
    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NotNull SupportSQLiteDatabase database) {
        }
    };

    public abstract CalculationDao calculationDao();

    public abstract CalculateInfoDao calculateInfoDao();

    public abstract CalculationUserInputDao calculationUserInputDao();

    public abstract ImagesDao imagesDao();

    public abstract TejarihaDao tejarihaDao();

    public abstract MapScreenDao mapScreenDao();

    public abstract ExaminerDutiesDao examinerDutiesDao();

    public abstract KarbariDictionaryDao karbariDictionaryDao();

    public abstract RequestDictionaryDao requestDictionaryDao();

    public abstract NoeVagozariDictionaryDao noeVagozariDictionaryDao();

    public abstract QotrEnsheabDictionaryDao qotrEnsheabDictionaryDao();

    public abstract QotrSifoonDictionaryDao qotrSifoonDictionaryDao();

    public abstract TaxfifDictionaryDao taxfifDictionaryDao();

    public abstract ServiceDictionaryDao serviceDictionaryDao();

    public abstract ResultDictionaryDao resultDictionaryDao();

    public abstract ZaribDao zaribDao();

    public abstract BlockDao blockDao();

    public abstract FormulaDao formulaDao();
}
