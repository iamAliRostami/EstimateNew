package com.leon.estimate_new.fragments.documents;

import static com.leon.estimate_new.utils.ImageUtils.brightnessController;
import static com.leon.estimate_new.utils.ImageUtils.contrastController;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.leon.estimate_new.R;
import com.leon.estimate_new.databinding.FragmentBrightnessContrastBinding;

public class BrightnessContrastFragment extends Fragment {
    private FragmentBrightnessContrastBinding binding;
    private Callback documentActivity;
    private final View.OnClickListener onClickListenerClose = v -> documentActivity.cancelEditing();
    private Bitmap bitmapTemp;
    private final View.OnClickListener onClickListenerAccepted = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            documentActivity.setFinalBitmap(bitmapTemp);
        }
    };
    private final SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            switch (seekBar.getId()) {
                case R.id.seekBar_contrast:
                    final float contrast = (float) (progress) / 10;
                    bitmapTemp = contrastController(bitmapTemp, contrast,
                            binding.seekBarBrightness.getProgress() - 250);
                    binding.imageView.setImageBitmap(bitmapTemp);
                    binding.textViewContrast.setText(getString(R.string.contrast).concat(String.valueOf(contrast)));
                    break;
                case R.id.seekBar_brightness:
                    final int brightness = progress - 150;
                    bitmapTemp = brightnessController(bitmapTemp, brightness);
                    binding.imageView.setImageBitmap(bitmapTemp);
                    binding.textViewBrightness.setText(getString(R.string.brightness).concat(String.valueOf(brightness)));
                    break;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

    public static BrightnessContrastFragment newInstance() {
        return new BrightnessContrastFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBrightnessContrastBinding.inflate(inflater, container, false);
        initialize();
        return binding.getRoot();
    }

    private void initialize() {
        binding.seekBarBrightness.setMax(300);
        binding.seekBarBrightness.setProgress(150);
        binding.seekBarBrightness.setOnSeekBarChangeListener(onSeekBarChangeListener);
        binding.seekBarContrast.setMax(100);
        binding.seekBarContrast.setProgress(50);
        binding.seekBarContrast.setOnSeekBarChangeListener(onSeekBarChangeListener);
        binding.buttonAccepted.setOnClickListener(onClickListenerAccepted);
        binding.buttonClose.setOnClickListener(onClickListenerClose);
        bitmapTemp = documentActivity.getBitmap();
        binding.imageView.setImageBitmap(bitmapTemp);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            documentActivity = (Callback) context;
        }
    }

    public interface Callback {
        Bitmap getBitmap();

        void setTempBitmap(Bitmap tempBitmap);

        void setFinalBitmap(Bitmap finalBitmap);

        void cancelEditing();
    }
}