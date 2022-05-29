package com.leon.estimate_new.utils.estimating;

import static com.leon.estimate_new.helpers.Constants.applicationComponent;
import static com.leon.estimate_new.helpers.MyApplication.getApplicationComponent;

import android.app.Activity;
import android.content.Context;

import com.leon.estimate_new.activities.FormActivity;
import com.leon.estimate_new.base_items.BaseAsync;
import com.leon.estimate_new.tables.Arzeshdaraei;
import com.leon.estimate_new.tables.Block;
import com.leon.estimate_new.tables.Formula;
import com.leon.estimate_new.tables.KarbariDictionary;
import com.leon.estimate_new.tables.NoeVagozariDictionary;
import com.leon.estimate_new.tables.QotrEnsheabDictionary;
import com.leon.estimate_new.tables.TaxfifDictionary;
import com.leon.estimate_new.tables.Tejariha;
import com.leon.estimate_new.tables.Zarib;

import java.util.ArrayList;

public class GetDBData extends BaseAsync {
    private final String trackNumber;
    private final int zoneId;
    private final ArrayList<NoeVagozariDictionary> noeVagozariDictionaries = new ArrayList<>();
    private final ArrayList<QotrEnsheabDictionary> qotrEnsheabDictionaries = new ArrayList<>();
    private final ArrayList<KarbariDictionary> karbariDictionaries = new ArrayList<>();
    private final ArrayList<TaxfifDictionary> taxfifDictionaries = new ArrayList<>();
    private final ArrayList<Tejariha> tejariha = new ArrayList<>();
    private Arzeshdaraei arzeshdaraei;

    public GetDBData(Context context, String zoneId, String trackNumber, Object... view) {
        super(context, view);
        this.zoneId = Integer.parseInt(zoneId);
        this.trackNumber = trackNumber;
    }

    @Override
    public void postTask(Object o) {
        ((FormActivity) o).setData(arzeshdaraei, noeVagozariDictionaries, qotrEnsheabDictionaries,
                karbariDictionaries, taxfifDictionaries, tejariha);
    }

    @Override
    public void preTask(Object o) {

    }

    @Override
    public void backgroundTask(Activity activity) {
        karbariDictionaries.addAll(getApplicationComponent().MyDatabase().karbariDictionaryDao().getKarbariDictionary());
        taxfifDictionaries.addAll(applicationComponent.MyDatabase().taxfifDictionaryDao().getTaxfifDictionaries());
        noeVagozariDictionaries.addAll(applicationComponent.MyDatabase().noeVagozariDictionaryDao().getNoeVagozariDictionaries());
        qotrEnsheabDictionaries.addAll(applicationComponent.MyDatabase().qotrEnsheabDictionaryDao().getQotrEnsheabDictionaries());
        tejariha.addAll(getApplicationComponent().MyDatabase().tejarihaDao().getTejarihaByTrackNumber(trackNumber));
        final ArrayList<Formula> formulas = new ArrayList<>(getApplicationComponent().MyDatabase().formulaDao()
                .getFormulaByZoneId(zoneId));
        final ArrayList<Block> blocks = new ArrayList<>(getApplicationComponent().MyDatabase()
                .blockDao().getBlockByZoneId(zoneId));
        final ArrayList<Zarib> zaribs = new ArrayList<>(getApplicationComponent().MyDatabase()
                .zaribDao().getZaribByZoneId(zoneId));
        arzeshdaraei = new Arzeshdaraei(blocks, formulas, zaribs);
    }

    @Override
    public void backgroundTask(Context context) {

    }
}
