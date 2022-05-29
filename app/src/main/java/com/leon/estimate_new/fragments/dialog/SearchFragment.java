package com.leon.estimate_new.fragments.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;

import com.leon.estimate_new.databinding.FragmentSearchBinding;
import com.leon.estimate_new.fragments.main_items.DutiesListFragment;
import com.sardari.daterangepicker.customviews.DateRangeCalendarView;
import com.sardari.daterangepicker.dialog.DatePickerDialog;

import org.jetbrains.annotations.NotNull;

public class SearchFragment extends DialogFragment {
    private final Callback dutiesListFragment;
    private FragmentSearchBinding binding;

    public SearchFragment(DutiesListFragment dutiesListFragment) {
        this.dutiesListFragment = dutiesListFragment;
    }

    public static SearchFragment newInstance(DutiesListFragment dutiesListFragment) {
        return new SearchFragment(dutiesListFragment);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        initialize();
        return binding.getRoot();
    }

    private void initialize() {
        setOnButtonSearchClickListener();
        setOnTextViewStartDateClickListener();
    }

    private void setOnButtonSearchClickListener() {
        binding.buttonSearch.setOnClickListener(v -> {
            dutiesListFragment.filter(binding.editTextBillId.getText().toString(),
                    binding.editTextTrackNumber.getText().toString(),
                    binding.editTextName.getText().toString(),
                    binding.editTextFamily.getText().toString(),
                    binding.editTextNationNumber.getText().toString(),
                    binding.editTextMobile.getText().toString(),
                    binding.textViewStartDate.getText().toString());
            dismiss();
        });
    }

    private void setOnTextViewStartDateClickListener() {
        binding.textViewStartDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity());
            datePickerDialog.setSelectionMode(DateRangeCalendarView.SelectionMode.Single);
            datePickerDialog.setDisableDaysAgo(false);
            datePickerDialog.setTextSizeTitle(10.0f);
            datePickerDialog.setTextSizeWeek(12.0f);
            datePickerDialog.setTextSizeDate(14.0f);
            datePickerDialog.setCanceledOnTouchOutside(true);
            datePickerDialog.setOnSingleDateSelectedListener(date ->
                    binding.textViewStartDate.setText(date.getPersianShortDate()));
            datePickerDialog.showDialog();
        });
    }

    @Override
    public void onResume() {
        if (getDialog() != null) {
            WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            getDialog().getWindow().setAttributes(params);
        }
        super.onResume();
    }

    public interface Callback {
        void filter(String... s);
    }
}