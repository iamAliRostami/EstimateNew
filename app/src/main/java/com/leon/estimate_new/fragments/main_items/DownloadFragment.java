package com.leon.estimate_new.fragments.main_items;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.leon.estimate_new.databinding.FragmentDownloadBinding;
import com.leon.estimate_new.utils.downloading.DownloadData;

public class DownloadFragment extends Fragment {
    private FragmentDownloadBinding binding;

    public DownloadFragment() {
    }

    public static DownloadFragment newInstance() {
        return new DownloadFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDownloadBinding.inflate(inflater, container, false);
        initialize();
        return binding.getRoot();
    }

    private void initialize() {
        initializeDownloadButton();
    }

    private void initializeDownloadButton() {
        binding.buttonDownload.setOnClickListener(v ->
                new DownloadData(requireContext(), binding.buttonDownload).execute(requireActivity()));
    }
}