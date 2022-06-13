package com.leon.estimate_new.fragments.forms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.leon.estimate_new.databinding.FragmentTakeSingleBinding;

public class TakeSingleFragment extends Fragment {
    private FragmentTakeSingleBinding binding;

    public TakeSingleFragment() {
    }

    public static TakeSingleFragment newInstance() {
        return new TakeSingleFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTakeSingleBinding.inflate(inflater, container, false);
        initialize();
        return binding.getRoot();
    }

    private void initialize() {
        setOnButtonsClickListener();
    }

    private void setOnButtonsClickListener() {
        binding.buttonPre.setOnClickListener(v -> {

        });
        binding.buttonEditCrooki.setOnClickListener(v -> {

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}