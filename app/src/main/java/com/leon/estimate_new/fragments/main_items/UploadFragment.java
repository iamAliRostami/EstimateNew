package com.leon.estimate_new.fragments.main_items;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.leon.estimate_new.databinding.FragmentUploadBinding;
import com.leon.estimate_new.utils.uploading.UploadNavigated;

public class UploadFragment extends Fragment {
    private FragmentUploadBinding binding;

    public UploadFragment() {
    }

    public static UploadFragment newInstance() {
        return new UploadFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUploadBinding.inflate(inflater, container, false);
        initialize();
        return binding.getRoot();
    }

    private void initialize() {
        binding.buttonUpload.setOnClickListener(v ->
                new UploadNavigated(requireContext(), binding.buttonUpload).execute(requireActivity()));
    }

    public Button getButton() {
        return binding.buttonUpload;
    }

    public Activity getActivities() {
        return requireActivity();
    }
}