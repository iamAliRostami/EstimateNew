package com.leon.estimate_new.fragments.dialog;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.leon.estimate_new.R;
import com.leon.estimate_new.adapters.SpinnerCustomAdapter;
import com.leon.estimate_new.databinding.FragmentValueBinding;
import com.leon.estimate_new.fragments.forms.BaseInfoFragment;
import com.leon.estimate_new.tables.Arzeshdaraei;
import com.leon.estimate_new.tables.Block;
import com.leon.estimate_new.tables.Formula;
import com.leon.estimate_new.utils.CustomToast;

import java.util.ArrayList;

public class ValueFragment extends DialogFragment {
    final ArrayList<String> blockTitles = new ArrayList<>(), gozarTitles = new ArrayList<>();
    private final Arzeshdaraei arzeshdaraei;
    private final Callback baseInfoFragment;
    private final ArrayList<Integer> values = new ArrayList<>();
    private FragmentValueBinding binding;
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            counting(false);
        }
    };
    private final AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            counting(false);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    public ValueFragment(final BaseInfoFragment baseInfoFragment) {
        this.baseInfoFragment = baseInfoFragment;
        arzeshdaraei = this.baseInfoFragment.getArzeshdaraei();
        values.addAll(this.baseInfoFragment.getValue());
    }

    public static ValueFragment newInstance(final BaseInfoFragment baseInfoFragment) {
        return new ValueFragment(baseInfoFragment);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentValueBinding.inflate(inflater, container, false);
        initialize();
        return binding.getRoot();
    }

    private void initialize() {
        initializeSpinners();
        initializeEditText();
        editTextsChangedListener();
        onButtonCountingClickListener();
    }

    private void onButtonCountingClickListener() {
        binding.buttonCounting.setOnClickListener(v -> {
            if (checkIsNoEmpty(binding.editTextMaskooni) || checkIsNoEmpty(binding.editTextEdari)
                    || checkIsNoEmpty(binding.editTextHotel) || checkIsNoEmpty(binding.editTextOmumi)
                    || checkIsNoEmpty(binding.editTextSanati) || checkIsNoEmpty(binding.editTextTejari)) {
                counting(true);
            } else
                new CustomToast().warning(getString(R.string.at_least_enter_one));
        });

    }

    private boolean checkIsNoEmpty(EditText editText) {
        if (editText.getText().toString().length() < 1) {
            editText.setError(getString(R.string.error_empty));
            editText.requestFocus();
            return false;
        }
        return true;
    }

    private void editTextsChangedListener() {
        binding.editTextMaskooni.addTextChangedListener(textWatcher);
        binding.editTextTejari.addTextChangedListener(textWatcher);
        binding.editTextEdari.addTextChangedListener(textWatcher);
        binding.editTextOmumi.addTextChangedListener(textWatcher);
        binding.editTextSanati.addTextChangedListener(textWatcher);
        binding.editTextHotel.addTextChangedListener(textWatcher);
    }

    private void counting(boolean dis) {
        if (binding.editTextMaskooni.getText().toString().length() > 0)
            values.set(0, Integer.parseInt(binding.editTextMaskooni.getText().toString()));
        if (binding.editTextTejari.getText().toString().length() > 0)
            values.set(1, Integer.parseInt(binding.editTextTejari.getText().toString()));
        if (binding.editTextEdari.getText().toString().length() > 0)
            values.set(2, Integer.parseInt(binding.editTextEdari.getText().toString()));
        if (binding.editTextOmumi.getText().toString().length() > 0)
            values.set(3, Integer.parseInt(binding.editTextOmumi.getText().toString()));
        if (binding.editTextSanati.getText().toString().length() > 0)
            values.set(4, Integer.parseInt(binding.editTextSanati.getText().toString()));
        if (binding.editTextHotel.getText().toString().length() > 0)
            values.set(5, Integer.parseInt(binding.editTextHotel.getText().toString()));
        if (values.get(0) > 0 || values.get(1) > 0 || values.get(2) > 0 || values.get(3) > 0 ||
                values.get(4) > 0 || values.get(5) > 0) {
            double countMaskooni = 20000, countEdariDolati = 20000, countTejari = 20000,
                    countSanati = 20000, countKhadamati = 20000, countSayer = 20000;
            values.set(6, binding.spinner1.getSelectedItemPosition());
            values.set(7, binding.spinner2.getSelectedItemPosition());
            final Block block = arzeshdaraei.blocks.get(values.get(6));
            final Formula formula = arzeshdaraei.formulas.get(values.get(7));

            if ((block.maskooni * formula.maskooniZ) > 20000)
                countMaskooni = (block.maskooni * formula.maskooniZ);
            if ((block.tejari * formula.tejariZ) > 20000)
                countTejari = (block.tejari * formula.tejariZ);
            if ((block.edariDolati * formula.edariDolatiZ) > 20000)
                countEdariDolati = (block.edariDolati * formula.edariDolatiZ);
            if ((block.sanati * formula.sanatiZ) > 20000)
                countSanati = (block.sanati * formula.sanatiZ);
            if ((block.khadamati * formula.khadamatiZ) > 20000)
                countKhadamati = (block.khadamati * formula.khadamatiZ);
            if ((block.sayer * formula.sayerZ) > 20000)
                countSayer = (block.sayer * formula.sayerZ);

            countMaskooni = (countMaskooni * values.get(0));
            countTejari = (countTejari * values.get(1));
            countEdariDolati = (countEdariDolati * values.get(2));
            countKhadamati = (countKhadamati * values.get(3));
            countSanati = (countSanati * values.get(4));
            countSayer = (countSayer * values.get(5));
            int count = (int) ((countMaskooni + countEdariDolati + countTejari + countSanati
                    + countKhadamati + countSayer)
                    / (values.subList(0, 5).stream().mapToInt(i -> i).sum()));
            count = count / 1000;
            count = count * 1000;
            binding.textViewCount.setText(String.valueOf(count));
            if (dis) {
                baseInfoFragment.setValue(values, count,
                        blockTitles.get(binding.spinner1.getSelectedItemPosition()),
                        String.valueOf(arzeshdaraei.formulas.get(binding.spinner2.getSelectedItemPosition()).gozarFrom)
                                .concat(" - ").concat(String.valueOf(arzeshdaraei.formulas.get(binding.spinner2.getSelectedItemPosition()).gozarTo)));
                dismiss();
            }
        } else {
            binding.textViewCount.setText(getString(R.string.zero));
            if (dis) {
                new CustomToast().warning(getString(R.string.at_least_enter_one));
            }
        }
    }

    private void initializeEditText() {
        binding.editTextMaskooni.setText(String.valueOf(values.get(0)));
        binding.editTextTejari.setText(String.valueOf(values.get(1)));
        binding.editTextEdari.setText(String.valueOf(values.get(2)));
        binding.editTextOmumi.setText(String.valueOf(values.get(3)));
        binding.editTextSanati.setText(String.valueOf(values.get(4)));
        binding.editTextHotel.setText(String.valueOf(values.get(5)));
    }

    private void initializeSpinners() {
        initializeSpinnerBlock();
        initializeSpinnerGozar();
        binding.spinner1.setOnItemSelectedListener(onItemSelectedListener);
        binding.spinner2.setOnItemSelectedListener(onItemSelectedListener);
    }


    private void initializeSpinnerGozar() {
        gozarTitles.clear();
        for (Formula formula : arzeshdaraei.formulas) {
            gozarTitles.add(formula.gozarTitle);
        }
        binding.spinner2.setAdapter(new SpinnerCustomAdapter(requireContext(), gozarTitles));
        binding.spinner2.setSelection(values.get(7));
    }

    private void initializeSpinnerBlock() {
        blockTitles.clear();
        for (Block block : arzeshdaraei.blocks) {
            blockTitles.add(block.blockId);
        }
        binding.spinner1.setAdapter(new SpinnerCustomAdapter(requireContext(), blockTitles));
        binding.spinner1.setSelection(values.get(6));
    }

    @Override
    public void onResume() {
        if (getDialog() != null) {
            WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            getDialog().getWindow().setAttributes(params);
        }
        super.onResume();
    }

    public interface Callback {
        void setValue(ArrayList<Integer> values, int value, String block, String arz);

        Arzeshdaraei getArzeshdaraei();

        ArrayList<Integer> getValue();
    }
}