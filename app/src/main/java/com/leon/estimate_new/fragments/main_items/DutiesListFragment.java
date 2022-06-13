package com.leon.estimate_new.fragments.main_items;

import static com.leon.estimate_new.enums.SharedReferenceKeys.TRACK_NUMBER;
import static com.leon.estimate_new.fragments.dialog.ShowFragmentDialog.ShowFragmentDialogOnce;
import static com.leon.estimate_new.helpers.MyApplication.getPreferenceManager;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.estimate_new.R;
import com.leon.estimate_new.adapters.CustomAdapterList;
import com.leon.estimate_new.databinding.FragmentDutiesListBinding;
import com.leon.estimate_new.fragments.dialog.SearchFragment;
import com.leon.estimate_new.tables.ExaminerDuties;
import com.leon.estimate_new.utils.list.PrepareListData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DutiesListFragment extends Fragment implements SearchFragment.Callback {
    private final ArrayList<ExaminerDuties> examinerDuties = new ArrayList<>();
    private final ArrayList<ExaminerDuties> examinerDutiesTemp = new ArrayList<>();
    private FragmentDutiesListBinding binding;
    private CustomAdapterList adapter;
    private boolean resume = false;

    public DutiesListFragment() {
    }

    public static DutiesListFragment newInstance() {
        return new DutiesListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDutiesListBinding.inflate(inflater, container, false);
        initialize();
        setHasOptionsMenu(false);
        return binding.getRoot();
    }

    private void initialize() {
        setHasOptionsMenu(false);
        new PrepareListData(requireContext(), this).execute(requireActivity());
    }

    public void initializeData(final ArrayList<ExaminerDuties> examinerDuties) {
        if (examinerDuties.isEmpty()) {
            requireActivity().runOnUiThread(() -> {
                binding.recyclerView.setVisibility(View.GONE);
                binding.textViewEmpty.setVisibility(View.VISIBLE);
            });
        } else {
            setHasOptionsMenu(true);
            initializeRecyclerView(examinerDuties);
        }
    }

    private void initializeRecyclerView(final ArrayList<ExaminerDuties> examinerDuties) {
        this.examinerDuties.clear();
        this.examinerDuties.addAll(examinerDuties);
        this.examinerDutiesTemp.clear();
        this.examinerDutiesTemp.addAll(examinerDuties);
        adapter = new CustomAdapterList(requireContext(), this.examinerDutiesTemp);
        requireActivity().runOnUiThread(() -> {
            binding.textViewEmpty.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.recyclerView.setAdapter(adapter);
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()) {
                @Override
                public boolean requestChildRectangleOnScreen(@NonNull RecyclerView parent,
                                                             @NonNull View child,
                                                             @NonNull Rect rect, boolean immediate) {
                    return false;
                }
            });
        });
        resume = true;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_search) {
            ShowFragmentDialogOnce(requireContext(), "SEARCH_DIALOG", SearchFragment.newInstance(this));
        } else if (id == R.id.menu_clear) {
            filter("", "", "", "", "", "", "");
        } else if (id == R.id.menu_last) {
            filter("", getPreferenceManager().getStringData(TRACK_NUMBER.getValue()),
                    "", "", "", "", "");
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void filter(String... s) {
        examinerDutiesTemp.clear();
        examinerDutiesTemp.addAll(examinerDuties);
        if (s[0].length() > 0) filterByBillId(s[0]);
        if (s[1].length() > 0) filterByTrackNumber(s[1]);
        if (s[2].length() > 0) filterByName(s[2]);
        if (s[3].length() > 0) filterByFamily(s[3]);
        if (s[4].length() > 0) filterByNationId(s[4]);
        if (s[5].length() > 0) filterByMobile(s[5]);
        if (s[6].length() > 0) filterByDate(s[6]);
        requireActivity().runOnUiThread(() -> {
            adapter = new CustomAdapterList(requireContext(), this.examinerDutiesTemp);
            binding.recyclerView.setAdapter(adapter);
        });
    }

    private void filterByBillId(String s) {
        final ArrayList<ExaminerDuties> examinerDutiesSearch = new ArrayList<>();
        for (ExaminerDuties examinerDuty : examinerDutiesTemp) {
            if ((examinerDuty.billId != null && examinerDuty.billId.contains(s))
                    || examinerDuty.neighbourBillId != null && examinerDuty.neighbourBillId.contains(s))
                examinerDutiesSearch.add(examinerDuty);
        }
        examinerDutiesTemp.clear();
        examinerDutiesTemp.addAll(examinerDutiesSearch);
    }

    private void filterByTrackNumber(String s) {
        final ArrayList<ExaminerDuties> examinerDutiesSearch = new ArrayList<>();
        for (ExaminerDuties examinerDuty : examinerDutiesTemp) {
            if (examinerDuty.trackNumber != null && examinerDuty.trackNumber.contains(s))
                examinerDutiesSearch.add(examinerDuty);
        }
        examinerDutiesTemp.clear();
        examinerDutiesTemp.addAll(examinerDutiesSearch);
    }

    private void filterByName(String s) {
        final ArrayList<ExaminerDuties> examinerDutiesSearch = new ArrayList<>();
        for (ExaminerDuties examinerDuty : examinerDutiesTemp) {
            if ((examinerDuty.firstName != null && examinerDuty.firstName.contains(s)) ||
                    (examinerDuty.nameAndFamily != null && examinerDuty.nameAndFamily.contains(s)))
                examinerDutiesSearch.add(examinerDuty);
        }
        examinerDutiesTemp.clear();
        examinerDutiesTemp.addAll(examinerDutiesSearch);
    }

    private void filterByFamily(String s) {
        final ArrayList<ExaminerDuties> examinerDutiesSearch = new ArrayList<>();
        for (ExaminerDuties examinerDuty : examinerDutiesTemp) {
            if ((examinerDuty.sureName != null && examinerDuty.sureName.contains(s)) ||
                    (examinerDuty.nameAndFamily != null && examinerDuty.nameAndFamily.contains(s)))
                examinerDutiesSearch.add(examinerDuty);
        }
        examinerDutiesTemp.clear();
        examinerDutiesTemp.addAll(examinerDutiesSearch);
    }

    private void filterByNationId(String s) {
        final ArrayList<ExaminerDuties> examinerDutiesSearch = new ArrayList<>();
        for (ExaminerDuties examinerDuty : examinerDutiesTemp) {
            if ((examinerDuty.nationalId != null && examinerDuty.nationalId.contains(s)))
                examinerDutiesSearch.add(examinerDuty);
        }
        examinerDutiesTemp.clear();
        examinerDutiesTemp.addAll(examinerDutiesSearch);
    }

    private void filterByMobile(String s) {
        final ArrayList<ExaminerDuties> examinerDutiesSearch = new ArrayList<>();
        for (ExaminerDuties examinerDuty : examinerDutiesTemp) {
            if ((examinerDuty.mobile != null && examinerDuty.mobile.contains(s)) ||
                    (examinerDuty.moshtarakMobile != null && examinerDuty.moshtarakMobile.contains(s)) ||
                    (examinerDuty.notificationMobile != null && examinerDuty.notificationMobile.contains(s)))
                examinerDutiesSearch.add(examinerDuty);
        }
        examinerDutiesTemp.clear();
        examinerDutiesTemp.addAll(examinerDutiesSearch);
    }

    private void filterByDate(String s) {
        final ArrayList<ExaminerDuties> examinerDutiesSearch = new ArrayList<>();
        s = s.substring(2);
        for (ExaminerDuties examinerDuty : examinerDutiesTemp) {
            if ((examinerDuty.examinationDay != null && examinerDuty.examinationDay.contains(s)))
                examinerDutiesSearch.add(examinerDuty);
        }
        examinerDutiesTemp.clear();
        examinerDutiesTemp.addAll(examinerDutiesSearch);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (resume) new PrepareListData(requireContext(), this).execute(requireActivity());
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}