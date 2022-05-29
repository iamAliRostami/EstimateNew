package com.leon.estimate_new.fragments.dialog;

import static com.leon.estimate_new.helpers.MyApplication.getApplicationComponent;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.estimate_new.R;
import com.leon.estimate_new.adapters.SpinnerCustomAdapter;
import com.leon.estimate_new.adapters.TejariSayerAdapter;
import com.leon.estimate_new.databinding.FragmentTejarihaSayerBinding;
import com.leon.estimate_new.fragments.forms.BaseInfoFragment;
import com.leon.estimate_new.tables.ExaminerDuties;
import com.leon.estimate_new.tables.KarbariDictionary;
import com.leon.estimate_new.tables.Tejariha;
import com.leon.estimate_new.utils.CustomToast;

import java.util.ArrayList;

public class TejarihaSayerFragment extends DialogFragment {
    private final Callback baseInfoFragment;
    private final ArrayList<Tejariha> tejariha = new ArrayList<>();
    private TejariSayerAdapter adapter;
    private FragmentTejarihaSayerBinding binding;

    public TejarihaSayerFragment(final BaseInfoFragment baseInfoFragment) {
        this.baseInfoFragment = baseInfoFragment;
        tejariha.addAll(this.baseInfoFragment.getTejariha());
    }

    public static TejarihaSayerFragment newInstance(final BaseInfoFragment baseInfoFragment) {
        TejarihaSayerFragment fragment = new TejarihaSayerFragment(baseInfoFragment);
        fragment.setCancelable(false);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTejarihaSayerBinding.inflate(inflater, container, false);
        initialize();
        return binding.getRoot();
    }

    private void initialize() {
        setOnImageViewPlusClickListener();
        initializeKarbariSpinner();
        initializeRecyclerView();
        setOnSubmitButtonClickListener();
    }

    private void setOnSubmitButtonClickListener() {
        binding.buttonSubmit.setOnClickListener(v -> {
            getApplicationComponent().MyDatabase().tejarihaDao().delete();
            getApplicationComponent().MyDatabase().tejarihaDao().insertTejariha(adapter.getTejarihas());
            baseInfoFragment.setTejariha(adapter.getTejarihas());
            dismiss();
        });
    }

    private void initializeRecyclerView() {
        adapter = new TejariSayerAdapter(tejariha);
        binding.recyclerViewTejariha.setAdapter(adapter);
        binding.recyclerViewTejariha.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean requestChildRectangleOnScreen(@NonNull RecyclerView parent,
                                                         @NonNull View child,
                                                         @NonNull Rect rect, boolean immediate) {
                return false;
            }
        });
    }

    private void setOnImageViewPlusClickListener() {
        binding.imageViewPlus.setOnClickListener(v -> {
            if (checkIsNoEmpty(binding.editTextNoeShoql) && checkIsNoEmpty(binding.editTextCapacity)
                    && checkIsNoEmpty(binding.editTextVahed) && checkIsNoEmpty(binding.editTextA2)
                    && checkIsNoEmpty(binding.editTextVahedMohasebe)) {
                if (tejariha.size() == 8) {
                    new CustomToast().warning(getString(R.string.tejari_over_flow), Toast.LENGTH_LONG);
                    return;
                }
                addItem();
                emptyForm();
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addItem() {
        //TODO
        tejariha.add(new Tejariha(baseInfoFragment.getKarbariDictionary()
                .get(binding.spinner1.getSelectedItemPosition()).title,
                binding.editTextNoeShoql.getText().toString(),
                Integer.parseInt(binding.editTextVahed.getText().toString()),
                binding.editTextVahedMohasebe.getText().toString(),
                binding.editTextA2.getText().toString(),
                Integer.parseInt(binding.editTextCapacity.getText().toString()),
                baseInfoFragment.getExaminerDuty().trackNumber));
        adapter.notifyDataSetChanged();
    }

    private void emptyForm() {
        binding.editTextA2.setText("");
        binding.editTextVahed.setText("");
        binding.editTextNoeShoql.setText("");
        binding.editTextCapacity.setText("");
        binding.editTextVahedMohasebe.setText("");
    }

    private boolean checkIsNoEmpty(EditText editText) {
        if (editText.getText().toString().length() < 1) {
            editText.setError(getString(R.string.error_empty));
            editText.requestFocus();
            return false;
        }
        return true;
    }

    private void initializeKarbariSpinner() {
        final ArrayList<String> spinnerItems = new ArrayList<>();
        int selected = 0, counter = 0;
        for (KarbariDictionary karbariDictionary : baseInfoFragment.getKarbariDictionary()) {
            spinnerItems.add(karbariDictionary.title);
            if (karbariDictionary.id == baseInfoFragment.getExaminerDuty().karbariId)
                selected = counter;
            counter = counter + 1;
        }
        binding.spinner1.setAdapter(new SpinnerCustomAdapter(requireContext(), spinnerItems));
        binding.spinner1.setSelection(selected);
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
        ArrayList<KarbariDictionary> getKarbariDictionary();

        ArrayList<Tejariha> getTejariha();

        void setTejariha(ArrayList<Tejariha> tejarihas);

        ExaminerDuties getExaminerDuty();
    }
}