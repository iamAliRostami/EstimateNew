package com.leon.estimate_new.fragments.forms;

import static com.leon.estimate_new.fragments.dialog.ShowFragmentDialog.ShowFragmentDialogOnce;
import static com.leon.estimate_new.helpers.Constants.SERVICES_FRAGMENT;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.leon.estimate_new.R;
import com.leon.estimate_new.adapters.SpinnerCustomAdapter;
import com.leon.estimate_new.databinding.FragmentBaseInfoBinding;
import com.leon.estimate_new.fragments.dialog.TejarihaSayerFragment;
import com.leon.estimate_new.fragments.dialog.ValueFragment;
import com.leon.estimate_new.tables.Arzeshdaraei;
import com.leon.estimate_new.tables.ExaminerDuties;
import com.leon.estimate_new.tables.KarbariDictionary;
import com.leon.estimate_new.tables.NoeVagozariDictionary;
import com.leon.estimate_new.tables.QotrEnsheabDictionary;
import com.leon.estimate_new.tables.TaxfifDictionary;
import com.leon.estimate_new.tables.Tejariha;
import com.leon.estimate_new.utils.estimating.GetArzeshdaraei;
import com.sardari.daterangepicker.customviews.DateRangeCalendarView;
import com.sardari.daterangepicker.dialog.DatePickerDialog;

import java.util.ArrayList;

public class BaseInfoFragment extends Fragment implements ValueFragment.Callback,
        TejarihaSayerFragment.Callback {
    private final View.OnClickListener onClickListener = v ->
            ShowFragmentDialogOnce(requireContext(), "TEJARI_SAYER_FRAGMENT",
                    TejarihaSayerFragment.newInstance(this));
    private FragmentBaseInfoBinding binding;
    private ExaminerDuties examinerDuty;
    private Arzeshdaraei arzeshdaraei;
    private Callback formActivity;
    private int saier, tejari;
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                if (s == binding.editTextTedadSaier.getEditableText())
                    saier = Integer.parseInt(s.toString());
                else if (s == binding.editTextTedadTejari.getEditableText())
                    tejari = Integer.parseInt(s.toString());
            } else {
                if (s == binding.editTextTedadSaier.getEditableText())
                    saier = 0;
                else if (s == binding.editTextTedadTejari.getEditableText())
                    tejari = 0;
            }
            binding.textViewTedadSaier.setEnabled(saier > 0 || tejari > 0);
            binding.textViewTedadTejari.setEnabled(saier > 0 || tejari > 0);
        }
    };
    private String block, arz;

    public static BaseInfoFragment newInstance() {
        return new BaseInfoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        formActivity.setTitle(getString(R.string.app_name).concat(" / ").concat("صفحه سوم"), false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBaseInfoBinding.inflate(inflater, container, false);
        initialize();
        return binding.getRoot();
    }

    private void initialize() {
        examinerDuty = formActivity.getExaminerDuty();
        arzeshdaraei = formActivity.getArzeshdaraei();
        block = examinerDuty.block != null ? examinerDuty.block : "-";
        arz = examinerDuty.arz != null ? examinerDuty.arz : "-";
        initializeSpinner();
        initializeField();
        setOnButtonsClickListener();
        setOnEditTextSodurDateClickListener();
        setOnTextViewArzeshDaraeiClickListener();
        setOnEditTextSayerTejariChangeListener();
        setOnTextViewSayerTejariClickListener();
    }

    private void setOnButtonsClickListener() {
        binding.buttonPre.setOnClickListener(v -> formActivity.setOnPreClickListener(SERVICES_FRAGMENT));
        binding.buttonSubmit.setOnClickListener(v -> {
            if (checkForm()) formActivity.setBaseInfo(prepareOutput());
        });
    }

    private boolean checkForm() {
        return checkIsNoEmpty(binding.editTextSifoon100)
                && checkIsNoEmpty(binding.editTextSifoon125)
                && checkIsNoEmpty(binding.editTextSifoon150)
                && checkIsNoEmpty(binding.editTextSifoon200)
                && checkIsNoEmpty(binding.editTextArse)
                && checkIsNoEmpty(binding.editTextAianKol)
                && checkIsNoEmpty(binding.editTextAianMaskooni)
                && checkIsNoEmpty(binding.editTextAianNonMaskooni)
                && checkIsNoEmpty(binding.editTextTedadMaskooni)
                && checkIsNoEmpty(binding.editTextTedadTejari)
                && checkIsNoEmpty(binding.editTextTedadSaier)
                && checkIsNoEmpty(binding.editTextTedadTakhfif)
                && checkIsNoEmpty(binding.editTextZarfiatQaradadi)
//                && checkIsNoEmpty(binding.editTextSodurDate)
//                && checkIsNoEmpty(binding.editTextLicenceNumber)
                && checkIsNoEmpty(binding.textViewArzeshMelk);
    }

    private boolean checkIsNoEmpty(EditText editText) {
        if (editText.getText().toString().length() < 1) {
            editText.setError(getString(R.string.error_empty));
            editText.requestFocus();
            return false;
        }
        return true;
    }

    private ExaminerDuties prepareOutput() {
        examinerDuty.block = block;
        examinerDuty.arz = arz;
        examinerDuty.sifoon100 = Integer.parseInt(binding.editTextSifoon100.getText().toString());
        examinerDuty.sifoon125 = Integer.parseInt(binding.editTextSifoon125.getText().toString());
        examinerDuty.sifoon150 = Integer.parseInt(binding.editTextSifoon150.getText().toString());
        examinerDuty.sifoon200 = Integer.parseInt(binding.editTextSifoon200.getText().toString());
        examinerDuty.arseNew = Integer.parseInt(binding.editTextArse.getText().toString());
        examinerDuty.aianMaskooniNew = Integer.parseInt(binding.editTextAianMaskooni.getText().toString());
        examinerDuty.aianNonMaskooniNew = Integer.parseInt(binding.editTextAianNonMaskooni.getText().toString());
        examinerDuty.aianKolNew = Integer.parseInt(binding.editTextAianKol.getText().toString());
        examinerDuty.tedadMaskooniNew = Integer.parseInt(binding.editTextTedadMaskooni.getText().toString());
        examinerDuty.tedadTejariNew = Integer.parseInt(binding.editTextTedadTejari.getText().toString());
        examinerDuty.tedadSaierNew = Integer.parseInt(binding.editTextTedadSaier.getText().toString());
        examinerDuty.arzeshMelk = Integer.parseInt(binding.textViewArzeshMelk.getText().toString());
        examinerDuty.tedadTaxfif = Integer.parseInt(binding.editTextTedadTakhfif.getText().toString());
        examinerDuty.zarfiatQarardadiNew = Integer.parseInt(binding.editTextZarfiatQaradadi.getText().toString());
        examinerDuty.licenceNumber = binding.editTextLicenceNumber.getText().toString();
        examinerDuty.karbariId = formActivity.getKarbariDictionary()
                .get(binding.spinner1.getSelectedItemPosition()).id;
        examinerDuty.karbariS = formActivity.getKarbariDictionary()
                .get(binding.spinner1.getSelectedItemPosition()).title;
        examinerDuty.noeVagozariId = formActivity.getNoeVagozariDictionaries()
                .get(binding.spinner2.getSelectedItemPosition()).id;
        examinerDuty.noeVagozariS = formActivity.getNoeVagozariDictionaries()
                .get(binding.spinner2.getSelectedItemPosition()).title;
        examinerDuty.qotrEnsheabId = formActivity.getQotrEnsheabDictionary()
                .get(binding.spinner3.getSelectedItemPosition()).id;
        examinerDuty.qotrEnsheabS = formActivity.getQotrEnsheabDictionary()
                .get(binding.spinner3.getSelectedItemPosition()).title;

        examinerDuty.qotrEnsheabFId = formActivity.getQotrEnsheabDictionary()
                .get(binding.spinner5.getSelectedItemPosition()).id;
        examinerDuty.qotrEnsheabFS = formActivity.getQotrEnsheabDictionary()
                .get(binding.spinner5.getSelectedItemPosition()).title;

        examinerDuty.taxfifId = formActivity.getTaxfifDictionary()
                .get(binding.spinner4.getSelectedItemPosition()).id;
        examinerDuty.taxfifS = formActivity.getTaxfifDictionary()
                .get(binding.spinner4.getSelectedItemPosition()).title;
        examinerDuty.isEnsheabQeirDaem = binding.checkbox1.isChecked();

        examinerDuty.motaqazi = binding.checkbox2.isChecked();
        examinerDuty.estelamShahrdari = binding.checkbox3.isChecked();
        examinerDuty.parvane = binding.checkbox4.isChecked();
        examinerDuty.sanad = binding.checkbox5.isChecked();
        examinerDuty.adamLicence = binding.checkbox6.isChecked();
        examinerDuty.qaradad = binding.checkbox7.isChecked();
        examinerDuty.qaradadNumber = binding.editTextQarardad.getText().toString();
        examinerDuty.pelak = Integer.parseInt(binding.editTextPelak.getText().toString());
        examinerDuty.codeNew = binding.editTextCodeNew.getText().toString();
        examinerDuty.codeKaf = binding.editTextCodeKaf.getText().toString();
        examinerDuty.sanadNumber = Integer.parseInt(binding.editTextSanadNumber.getText().toString());
        examinerDuty.sodurDate = binding.editTextSodurDate.getText().toString();
        return examinerDuty;
    }

    private void setOnEditTextSodurDateClickListener() {
        binding.editTextSodurDate.setOnClickListener(v -> {
            final DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext());
            datePickerDialog.setSelectionMode(DateRangeCalendarView.SelectionMode.Single);
            datePickerDialog.setDisableDaysAgo(false);
            datePickerDialog.setTextSizeTitle(10.0f);
            datePickerDialog.setTextSizeWeek(12.0f);
            datePickerDialog.setTextSizeDate(14.0f);
            datePickerDialog.setCanceledOnTouchOutside(true);
            datePickerDialog.setOnSingleDateSelectedListener(date ->
                    binding.editTextSodurDate.setText(date.getPersianShortDate()));
            datePickerDialog.showDialog();
        });
    }

    private void setOnEditTextSayerTejariChangeListener() {
        binding.editTextTedadSaier.addTextChangedListener(textWatcher);
        binding.editTextTedadTejari.addTextChangedListener(textWatcher);
    }

    private void setOnTextViewSayerTejariClickListener() {
        binding.textViewTedadTejari.setOnClickListener(onClickListener);
        binding.textViewTedadSaier.setOnClickListener(onClickListener);
    }

    private void setOnTextViewArzeshDaraeiClickListener() {
        binding.textViewArzeshMelk.setOnClickListener(v -> {
            if (arzeshdaraei != null
                    && arzeshdaraei.blocks != null
                    && arzeshdaraei.blocks.size() > 0
                    && arzeshdaraei.formulas != null
                    && arzeshdaraei.formulas.size() > 0
                    && arzeshdaraei.zaribs != null
                    && arzeshdaraei.zaribs.size() > 0) {
                ShowFragmentDialogOnce(requireContext(), "VALUE_FRAGMENT", ValueFragment.newInstance(this));
            } else {
                new GetArzeshdaraei(requireContext(), examinerDuty.zoneId, this).execute(requireActivity());
            }
        });
    }

    private void initializeField() {
        binding.editTextSifoon100.setText(String.valueOf(examinerDuty.sifoon100));
        binding.editTextSifoon125.setText(String.valueOf(examinerDuty.sifoon125));
        binding.editTextSifoon150.setText(String.valueOf(examinerDuty.sifoon150));
        binding.editTextSifoon200.setText(String.valueOf(examinerDuty.sifoon150));
        binding.editTextArse.setText(examinerDuty.arseNew != null ?
                String.valueOf(examinerDuty.arseNew) : String.valueOf(examinerDuty.arse));
        binding.editTextAianKol.setText(examinerDuty.aianKolNew != null ?
                String.valueOf(examinerDuty.aianKolNew) : String.valueOf(examinerDuty.aianKol));
        binding.editTextAianMaskooni.setText(examinerDuty.aianMaskooniNew != null ?
                String.valueOf(examinerDuty.aianMaskooniNew) : String.valueOf(examinerDuty.aianMaskooni));
        binding.editTextAianNonMaskooni.setText(examinerDuty.aianNonMaskooniNew != null ?
                String.valueOf(examinerDuty.aianNonMaskooniNew) : String.valueOf(examinerDuty.aianNonMaskooni));
        binding.editTextTedadMaskooni.setText(examinerDuty.tedadMaskooniNew != null ?
                String.valueOf(examinerDuty.tedadMaskooniNew) : String.valueOf(examinerDuty.tedadMaskooni));
        binding.editTextTedadTejari.setText(examinerDuty.tedadTejariNew != null ?
                String.valueOf(examinerDuty.tedadTejariNew) : String.valueOf(examinerDuty.tedadTejari));
        binding.editTextTedadSaier.setText(examinerDuty.tedadSaierNew != null ?
                String.valueOf(examinerDuty.tedadSaierNew) : String.valueOf(examinerDuty.tedadSaier));
        binding.editTextTedadTakhfif.setText(String.valueOf(examinerDuty.tedadTaxfif));
        binding.editTextZarfiatQaradadi.setText(examinerDuty.zarfiatQarardadiNew != null ?
                String.valueOf(examinerDuty.zarfiatQarardadiNew) : String.valueOf(examinerDuty.zarfiatQarardadi));
        binding.editTextLicenceNumber.setText(examinerDuty.licenceNumber);
        binding.editTextCodeKaf.setText(examinerDuty.codeKaf);
        binding.editTextSanadNumber.setText(String.valueOf(examinerDuty.sanadNumber));
        binding.editTextQarardad.setText(examinerDuty.qaradadNumber);
//        binding.editTextSodurDate.setText(examinerDuty.getExaminationDay());
        binding.editTextPelak.setText(String.valueOf(examinerDuty.pelak));

        binding.textViewArzeshMelk.setText(String.valueOf(examinerDuty.arzeshMelk));

        binding.checkbox1.setChecked(examinerDuty.isEnsheabQeirDaem);
        binding.checkbox2.setChecked(examinerDuty.motaqazi);
        binding.checkbox3.setChecked(examinerDuty.estelamShahrdari);
        binding.checkbox4.setChecked(examinerDuty.parvane);
        binding.checkbox5.setChecked(examinerDuty.sanad);
        binding.checkbox6.setChecked(examinerDuty.adamLicence);
        binding.checkbox7.setChecked(examinerDuty.qaradad);
    }

    private void initializeSpinner() {
        initializeNoeVagozariSpinner();
        initializeKarbariSpinner();
        initializeQotrEnsheabSpinner();
        initializeTaxfifSpinner();
    }

    private void initializeKarbariSpinner() {
        final ArrayList<String> arrayListSpinner = new ArrayList<>();
        int selected = 0, counter = 0;
        for (KarbariDictionary karbariDictionary : formActivity.getKarbariDictionary()) {
            arrayListSpinner.add(karbariDictionary.title);
            if (karbariDictionary.id == examinerDuty.karbariId) {
                selected = counter;
            }
            counter = counter + 1;
        }
        binding.spinner1.setAdapter(new SpinnerCustomAdapter(requireContext(), arrayListSpinner));
        binding.spinner1.setSelection(selected);
    }


    private void initializeNoeVagozariSpinner() {
        final ArrayList<String> arrayListSpinner = new ArrayList<>();
        for (NoeVagozariDictionary noeVagozariDictionary : formActivity.getNoeVagozariDictionaries()) {
            arrayListSpinner.add(noeVagozariDictionary.title);
        }
        binding.spinner2.setAdapter(new SpinnerCustomAdapter(requireContext(), arrayListSpinner));
        binding.spinner2.setSelection(examinerDuty.noeVagozariId);
    }

    private void initializeQotrEnsheabSpinner() {
        final ArrayList<String> arrayListSpinner = new ArrayList<>();
        int counter = 0, selectedA = 0, selectedF = 0;
        for (QotrEnsheabDictionary qotrEnsheabDictionary : formActivity.getQotrEnsheabDictionary()) {
            arrayListSpinner.add(qotrEnsheabDictionary.title);
            if (examinerDuty.qotrEnsheabId == qotrEnsheabDictionary.id) selectedA = counter;
            if (examinerDuty.qotrEnsheabFId == qotrEnsheabDictionary.id) selectedF = counter;
            counter = counter + 1;
        }
        binding.spinner3.setAdapter(new SpinnerCustomAdapter(requireContext(), arrayListSpinner));
        binding.spinner3.setSelection(selectedA);

        binding.spinner5.setAdapter(new SpinnerCustomAdapter(requireContext(), arrayListSpinner));
        binding.spinner5.setSelection(selectedF);
    }

    private void initializeTaxfifSpinner() {
        final ArrayList<String> arrayListSpinner = new ArrayList<>();
        int selected = 0, counter = 0;
        for (TaxfifDictionary taxfifDictionary : formActivity.getTaxfifDictionary()) {
            arrayListSpinner.add(taxfifDictionary.title);
            if (taxfifDictionary.id == examinerDuty.taxfifId) {
                selected = counter;
            }
            counter = counter + 1;
        }
        binding.spinner4.setAdapter(new SpinnerCustomAdapter(requireContext(), arrayListSpinner));
        binding.spinner4.setSelection(selected);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            formActivity = (Callback) context;
        }
    }

    @Override
    public Arzeshdaraei getArzeshdaraei() {
        return arzeshdaraei;
    }

    public void setArzeshdaraei(Arzeshdaraei arzeshdaraei) {
        this.arzeshdaraei = arzeshdaraei;
    }

    @Override
    public void setValue(ArrayList<Integer> values, int value, String block, String arz) {
        binding.textViewArzeshMelk.setText(String.valueOf(value));
        this.arz = arz;
        this.block = block;
        formActivity.setValues(values);
    }

    @Override
    public ArrayList<Integer> getValue() {
        return formActivity.getValues();
    }

    @Override
    public ArrayList<KarbariDictionary> getKarbariDictionary() {
        return formActivity.getKarbariDictionary();
    }

    @Override
    public ArrayList<Tejariha> getTejariha() {
        return formActivity.getTejarihas();
    }

    @Override
    public void setTejariha(ArrayList<Tejariha> tejarihas) {
        formActivity.setTejarihas(tejarihas);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public ExaminerDuties getExaminerDuty() {
        return examinerDuty;
    }

    public interface Callback {
        void setOnPreClickListener(int position);

        void setTitle(String title, boolean showMenu);

        void setBaseInfo(ExaminerDuties examinerDuties);

        ExaminerDuties getExaminerDuty();

        ArrayList<NoeVagozariDictionary> getNoeVagozariDictionaries();

        ArrayList<QotrEnsheabDictionary> getQotrEnsheabDictionary();

        ArrayList<KarbariDictionary> getKarbariDictionary();

        ArrayList<TaxfifDictionary> getTaxfifDictionary();

        ArrayList<Tejariha> getTejarihas();

        void setTejarihas(ArrayList<Tejariha> tejarihas);

        ArrayList<Integer> getValues();

        void setValues(ArrayList<Integer> values);

        Arzeshdaraei getArzeshdaraei();
    }
}