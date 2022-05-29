package com.leon.estimate_new.activities;

import static com.leon.estimate_new.enums.BundleEnum.BILL_ID;
import static com.leon.estimate_new.enums.BundleEnum.EXAMINER_DUTY;
import static com.leon.estimate_new.enums.BundleEnum.NEW_ENSHEAB;
import static com.leon.estimate_new.enums.BundleEnum.TRACK_NUMBER;
import static com.leon.estimate_new.fragments.dialog.ShowFragmentDialog.ShowFragmentDialogOnce;
import static com.leon.estimate_new.helpers.Constants.BASE_FRAGMENT;
import static com.leon.estimate_new.helpers.Constants.EDIT_MAP_FRAGMENT;
import static com.leon.estimate_new.helpers.Constants.MAP_DESCRIPTION_FRAGMENT;
import static com.leon.estimate_new.helpers.Constants.PERSONAL_FRAGMENT;
import static com.leon.estimate_new.helpers.Constants.SECOND_FRAGMENT;
import static com.leon.estimate_new.helpers.Constants.SERVICES_FRAGMENT;
import static com.leon.estimate_new.helpers.MyApplication.getApplicationComponent;
import static com.leon.estimate_new.helpers.MyApplication.setActivityComponent;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.esri.arcgisruntime.geometry.Point;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.leon.estimate_new.R;
import com.leon.estimate_new.databinding.ActivityFormBinding;
import com.leon.estimate_new.fragments.dialog.EnterBillFragment;
import com.leon.estimate_new.fragments.dialog.ShowDocumentFragment;
import com.leon.estimate_new.fragments.forms.BaseInfoFragment;
import com.leon.estimate_new.fragments.forms.EditMapFragment;
import com.leon.estimate_new.fragments.forms.MapDescriptionFragment;
import com.leon.estimate_new.fragments.forms.PersonalFragment;
import com.leon.estimate_new.fragments.forms.SecondFormFragment;
import com.leon.estimate_new.fragments.forms.ServicesFragment;
import com.leon.estimate_new.tables.Arzeshdaraei;
import com.leon.estimate_new.tables.CalculationUserInput;
import com.leon.estimate_new.tables.ExaminerDuties;
import com.leon.estimate_new.tables.KarbariDictionary;
import com.leon.estimate_new.tables.NoeVagozariDictionary;
import com.leon.estimate_new.tables.QotrEnsheabDictionary;
import com.leon.estimate_new.tables.RequestDictionary;
import com.leon.estimate_new.tables.TaxfifDictionary;
import com.leon.estimate_new.tables.Tejariha;
import com.leon.estimate_new.utils.estimating.GetDBData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class FormActivity extends AppCompatActivity implements PersonalFragment.Callback,
        ServicesFragment.Callback, BaseInfoFragment.Callback, SecondFormFragment.Callback,
        MapDescriptionFragment.Callback, EditMapFragment.Callback {
    private final CalculationUserInput calculationUserInput = new CalculationUserInput();
    private final ArrayList<Integer> values = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0));
    private final ArrayList<RequestDictionary> requestDictionaries = new ArrayList<>();
    private final ArrayList<NoeVagozariDictionary> noeVagozariDictionaries = new ArrayList<>();
    private final ArrayList<QotrEnsheabDictionary> qotrEnsheabDictionaries = new ArrayList<>();
    private final ArrayList<KarbariDictionary> karbariDictionaries = new ArrayList<>();
    private final ArrayList<TaxfifDictionary> taxfifDictionaries = new ArrayList<>();
    private final ArrayList<Tejariha> tejarihas = new ArrayList<>();
    private ActivityFormBinding binding;
    private Arzeshdaraei arzeshdaraei;
    private ExaminerDuties examinerDuty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setActivityComponent(this);
        initialize();
    }

    private void initialize() {
        if (getIntent().getExtras() != null) {
            final String json = getIntent().getExtras().getString(EXAMINER_DUTY.getValue());
            examinerDuty = new Gson().fromJson(json, ExaminerDuties.class);
            requestDictionaries.addAll(Arrays.asList(new GsonBuilder().create()
                    .fromJson(examinerDuty.requestDictionaryString, RequestDictionary[].class)));
        }
        new GetDBData(this, examinerDuty.zoneId, examinerDuty.trackNumber, this).execute(this);
        displayView(PERSONAL_FRAGMENT/*MAP_DESCRIPTION_FRAGMENT*/);
    }

    private void displayView(int position) {
        final String tag = Integer.toString(position);
        if (getFragmentManager().findFragmentByTag(tag) != null && getFragmentManager().findFragmentByTag(tag).isVisible()) {
            return;
        }
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.enter, R.animator.exit,
                R.animator.pop_enter, R.animator.pop_exit);
        fragmentTransaction.replace(binding.containerBody.getId(), getFragment(position), tag);
        if (position != 0) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commitAllowingStateLoss();
        getFragmentManager().executePendingTransactions();
    }

    private Fragment getFragment(int position) {
        switch (position) {
            case SERVICES_FRAGMENT:
                return ServicesFragment.newInstance();
            case BASE_FRAGMENT:
                return BaseInfoFragment.newInstance();
            case SECOND_FRAGMENT:
                return SecondFormFragment.newInstance();
            case MAP_DESCRIPTION_FRAGMENT:
                return MapDescriptionFragment.newInstance();
            case EDIT_MAP_FRAGMENT:
                return EditMapFragment.newInstance();
            case PERSONAL_FRAGMENT:
            default:
                return PersonalFragment.newInstance();
        }
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.document_menu, menu);
        if (!examinerDuty.isNewEnsheab) {
            menu.getItem(1).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (item.getItemId() == R.id.menu_document) {
            ShowFragmentDialogOnce(this, "DOCUMENT_FRAGMENT",
                    ShowDocumentFragment.newInstance(examinerDuty.billId, examinerDuty.isNewEnsheab,
                            false, examinerDuty.trackNumber));
        } else if (item.getItemId() == R.id.menu_neighbour_document) {
            ShowFragmentDialogOnce(this, "DOCUMENT_FRAGMENT",
                    ShowDocumentFragment.newInstance(examinerDuty.neighbourBillId,
                            examinerDuty.isNewEnsheab, true, examinerDuty.trackNumber));
        } else if (item.getItemId() == R.id.menu_other_document) {
            ShowFragmentDialogOnce(this, "BILL_FRAGMENT", EnterBillFragment.newInstance());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setOnPreClickListener(int position) {
        displayView(position);
    }

    @Override
    public void setTitle(String title, boolean showMenu) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(showMenu);
        // Set logo
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
    }

    @Override
    public void setSecondForm(ExaminerDuties examinerDuty) {
        this.examinerDuty = examinerDuty;
        //TODO
        displayView(MAP_DESCRIPTION_FRAGMENT);
    }

    @Override
    public void setPersonalInfo(final CalculationUserInput calculationUserInputTemp) {
        examinerDuty.preparePersonal(calculationUserInputTemp);
        calculationUserInputTemp.zoneId = Integer.parseInt(examinerDuty.zoneId);
        calculationUserInput.preparePersonal(calculationUserInputTemp);
        displayView(SERVICES_FRAGMENT);
    }

    @Override
    public void setServices(CalculationUserInput calculationUserInputTemp) {
        calculationUserInput.selectedServicesObject = new ArrayList<>(calculationUserInputTemp.selectedServicesObject);
        examinerDuty.requestDictionary = new ArrayList<>(calculationUserInputTemp.selectedServicesObject);
        calculationUserInput.selectedServicesString = new GsonBuilder().create().toJson(calculationUserInputTemp.selectedServicesObject);
        examinerDuty.requestDictionaryString = new GsonBuilder().create().toJson(calculationUserInputTemp.selectedServicesObject);
        displayView(BASE_FRAGMENT);
    }

    public void setData(Arzeshdaraei arzeshdaraei,
                        ArrayList<NoeVagozariDictionary> noeVagozariDictionaries,
                        ArrayList<QotrEnsheabDictionary> qotrEnsheabDictionaries,
                        ArrayList<KarbariDictionary> karbariDictionaries,
                        ArrayList<TaxfifDictionary> taxfifDictionaries,
                        ArrayList<Tejariha> tejariha) {
        this.noeVagozariDictionaries.addAll(noeVagozariDictionaries);
        this.qotrEnsheabDictionaries.addAll(qotrEnsheabDictionaries);
        this.karbariDictionaries.addAll(karbariDictionaries);
        this.taxfifDictionaries.addAll(taxfifDictionaries);
        this.tejarihas.addAll(tejariha);
        this.arzeshdaraei = arzeshdaraei;
    }

    @Override
    public void setBaseInfo(ExaminerDuties examinerDuty) {
        this.examinerDuty = examinerDuty;
        this.calculationUserInput.updateCalculationUserInput(examinerDuty);
        displayView(SECOND_FRAGMENT);
    }

    @Override
    public ExaminerDuties getExaminerDuty() {
        return examinerDuty;
    }

    @Override
    public void setEditMap() {
        //TODO
        final Intent intent = new Intent(getApplicationContext(), DocumentActivity.class);
//        final Intent intent = new Intent(getApplicationContext(), FinalReportActivity.class);
        intent.putExtra(TRACK_NUMBER.getValue(), examinerDuty.trackNumber);
        intent.putExtra(BILL_ID.getValue(), examinerDuty.billId != null ?
                examinerDuty.billId : examinerDuty.neighbourBillId);
        intent.putExtra(NEW_ENSHEAB.getValue(), examinerDuty.isNewEnsheab);
        prepareToSend();
        finish();
        startActivity(intent);
    }

    private void prepareToSend() {
        calculationUserInput.fillCalculationUserInput(examinerDuty);
        getApplicationComponent().MyDatabase().calculationUserInputDao().deleteByTrackNumber(examinerDuty.trackNumber);
        getApplicationComponent().MyDatabase().calculationUserInputDao().insertCalculationUserInput(calculationUserInput);
        getApplicationComponent().MyDatabase().examinerDutiesDao().insert(examinerDuty);//TODO
    }

    @Override
    public void setMapDescription(String description) {
        examinerDuty.mapDescription = description;
        displayView(EDIT_MAP_FRAGMENT);
    }

    @Override
    public void setWaterLocation(Point point) {
        examinerDuty.x1 = calculationUserInput.x3 = point.getX();
        examinerDuty.y1 = calculationUserInput.y3 = point.getY();
    }

    @Override
    public CalculationUserInput getCalculationUserInput() {
        return calculationUserInput;
    }

    @Override
    public ArrayList<NoeVagozariDictionary> getNoeVagozariDictionaries() {
        return noeVagozariDictionaries;
    }

    @Override
    public ArrayList<QotrEnsheabDictionary> getQotrEnsheabDictionary() {
        return qotrEnsheabDictionaries;
    }

    @Override
    public ArrayList<KarbariDictionary> getKarbariDictionary() {
        return karbariDictionaries;
    }

    @Override
    public ArrayList<TaxfifDictionary> getTaxfifDictionary() {
        return taxfifDictionaries;
    }

    @Override
    public ArrayList<Tejariha> getTejarihas() {
        return tejarihas;
    }

    @Override
    public void setTejarihas(final ArrayList<Tejariha> tejarihas) {
        this.tejarihas.clear();
        this.tejarihas.addAll(tejarihas);
    }

    @Override
    public Arzeshdaraei getArzeshdaraei() {
        return arzeshdaraei;
    }

    @Override
    public ArrayList<Integer> getValues() {
        return values;
    }

    @Override
    public void setValues(final ArrayList<Integer> values) {
        this.values.clear();
        this.values.addAll(values);
    }

    @Override
    public ArrayList<RequestDictionary> getServiceDictionaries() {
        return requestDictionaries;
    }
}