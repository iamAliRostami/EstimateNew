package com.leon.estimate_new.fragments.forms;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.RED;
import static android.graphics.Color.YELLOW;
import static com.leon.estimate_new.helpers.Constants.BITMAP_SELECTED;
import static com.leon.estimate_new.helpers.Constants.MAP_DESCRIPTION_FRAGMENT;
import static com.leon.estimate_new.helpers.Constants.MAP_SELECTED;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.leon.estimate_new.R;
import com.leon.estimate_new.databinding.FragmentEditMapBinding;
import com.leon.estimate_new.tables.ExaminerDuties;


public class EditMapFragment extends Fragment {
    private FragmentEditMapBinding binding;
    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.image_view_refresh:
                    binding.signatureView.clearCanvas();
                    //TODO
                    if (BITMAP_SELECTED != null)
                        binding.signatureView.setBitmap(BITMAP_SELECTED);
                    break;
                case R.id.image_view_color_blue:
                    binding.signatureView.setPenColor(BLUE);
                    break;
                case R.id.image_view_color_red:
                    binding.signatureView.setPenColor(RED);
                    break;
                case R.id.image_view_color_yellow:
                    binding.signatureView.setPenColor(YELLOW);
                    break;
            }
        }
    };
    private Callback formActivity;

    public EditMapFragment() {
    }

    public static EditMapFragment newInstance() {
        return new EditMapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        formActivity.setTitle(getString(R.string.app_name).concat(" / ").concat("صفحه ششم"), false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditMapBinding.inflate(inflater, container, false);
        initialize();
        return binding.getRoot();
    }

    private void initialize() {
        setOnButtonClickListener();
        binding.signatureView.setPenColor(YELLOW);
        binding.imageViewRefresh.setOnClickListener(onClickListener);
        binding.imageViewColorYellow.setOnClickListener(onClickListener);
        binding.imageViewColorBlue.setOnClickListener(onClickListener);
        binding.imageViewColorRed.setOnClickListener(onClickListener);
        if (BITMAP_SELECTED != null) {
            binding.signatureView.setBitmap(BITMAP_SELECTED);
        }
    }

    private void setOnButtonClickListener() {
        binding.buttonPre.setOnClickListener(v -> formActivity.setOnPreClickListener(MAP_DESCRIPTION_FRAGMENT));
        binding.buttonSubmit.setOnClickListener(v -> {
            BITMAP_SELECTED = binding.signatureView.getSignatureBitmap();
            MAP_SELECTED = binding.signatureView.getSignatureBitmap();
            formActivity.setEditMap();
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            formActivity = (Callback) context;
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public interface Callback {
        void setOnPreClickListener(int position);

        void setTitle(String title, boolean showMenu);

        ExaminerDuties getExaminerDuty();

        void setEditMap();
    }
}