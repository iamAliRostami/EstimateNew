package com.leon.estimate_new.fragments.dialog;

import static com.leon.estimate_new.helpers.MyApplication.getApplicationComponent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.leon.estimate_new.databinding.FragmentAddDocumentBinding;
import com.leon.estimate_new.tables.ExaminerDuties;
import com.leon.estimate_new.utils.document.AddDocumentRadif;


public class AddDocumentFragment extends DialogFragment {
    private FragmentAddDocumentBinding binding;
    private ExaminerDuties examinerDuty;

    public AddDocumentFragment() {
    }

    public AddDocumentFragment(String trackNumber) {
        this.examinerDuty = getApplicationComponent().MyDatabase().examinerDutiesDao()
                .examinerDutiesByTrackNumber(trackNumber);
    }

    public static AddDocumentFragment newInstance(String trackNumber) {
        return new AddDocumentFragment(trackNumber);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAddDocumentBinding.inflate(inflater, container, false);
        initialize();
        return binding.getRoot();
    }

    private void initialize() {
        binding.editTextAddress.setText(examinerDuty.address);
        binding.editTextFamily.setText(examinerDuty.sureName);
        binding.editTextName.setText(examinerDuty.firstName);
        binding.editTextTrackNumber.setText(examinerDuty.trackNumber);
        setButtonSendClickListener();
    }

    void setButtonSendClickListener() {
        binding.buttonSend.setOnClickListener(v -> {
            new AddDocumentRadif(requireContext(), examinerDuty, binding.buttonSend).execute(requireActivity());
            dismiss();
        });
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
}