package com.leon.estimate_new.activities;

import static com.leon.estimate_new.enums.BundleEnum.LICENCE_TITLE;
import static com.leon.estimate_new.enums.BundleEnum.OTHER_TITLE;
import static com.leon.estimate_new.enums.BundleEnum.TITLE;
import static com.leon.estimate_new.enums.BundleEnum.TRACK_NUMBER;
import static com.leon.estimate_new.helpers.MyApplication.getApplicationComponent;
import static com.leon.estimate_new.helpers.MyApplication.setActivityComponent;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.GsonBuilder;
import com.leon.estimate_new.R;
import com.leon.estimate_new.adapters.SpinnerCustomAdapter;
import com.leon.estimate_new.databinding.ActivityFinalReportBinding;
import com.leon.estimate_new.tables.ExaminerDuties;
import com.leon.estimate_new.tables.RequestDictionary;
import com.leon.estimate_new.tables.ResultDictionary;
import com.leon.estimate_new.utils.CustomToast;
import com.leon.estimate_new.utils.document.PrepareOutputImage;
import com.leon.estimate_new.utils.estimating.UploadImages;
import com.leon.estimate_new.utils.estimating.UploadNavigated;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FinalReportActivity extends AppCompatActivity {
    private final ArrayList<ResultDictionary> resultDictionaries = new ArrayList<>();
    private final ArrayList<Bitmap> bitmaps = new ArrayList<>();
    private ActivityFinalReportBinding binding;
    private ExaminerDuties examinerDuty;
    private int pageNumber = 0, maxNumber = 2, imageNumber = 0;
    private boolean licence = false, finalSubmit = false, sent = true;
    private int licenceTitle, estimateTitle, crookiTitle;
    private List<String[]> licenceRows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFinalReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setActivityComponent(this);
        if (getIntent().getExtras() != null) {
            examinerDuty = getApplicationComponent().MyDatabase().examinerDutiesDao()
                    .examinerDutiesByTrackNumber(getIntent().getExtras().getString(TRACK_NUMBER.getValue()));
            crookiTitle = getIntent().getExtras().getInt(OTHER_TITLE.getValue());
            licenceTitle = getIntent().getExtras().getInt(LICENCE_TITLE.getValue());
            estimateTitle = getIntent().getExtras().getInt(TITLE.getValue());
        }
        initialize();
    }

    private void initialize() {
        examinerDuty.requestDictionary = new ArrayList<>(Arrays.asList(new GsonBuilder().create()
                .fromJson(examinerDuty.requestDictionaryString, RequestDictionary[].class)));
        for (int i = 0; i < examinerDuty.requestDictionary.size() && !licence; i++) {
            if ((examinerDuty.requestDictionary.get(i).title.trim().equals("انشعاب آب") ||
                    examinerDuty.requestDictionary.get(i).title.trim().equals("انشعاب فاضلاب")) &
                    examinerDuty.requestDictionary.get(i).isSelected) {
                examinerDuty.operation = examinerDuty.requestDictionary.get(i).title;
                licence = true;
                maxNumber++;
            }
        }
        createOutputImage();
        initializeSpinner();
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        binding.imageViewRefresh1.setOnClickListener(v -> binding.signatureView1.clearCanvas());
        binding.imageViewRefresh2.setOnClickListener(v -> binding.signatureView2.clearCanvas());
        binding.buttonDenial.setOnClickListener(v -> finish());
        setOnAcceptClickListener();
        initializeArrowButton();
    }

    private void initializeSpinner() {
        final ArrayList<String> items = new ArrayList<>();
        resultDictionaries.addAll(getApplicationComponent().MyDatabase().resultDictionaryDao().getResults());
        for (ResultDictionary resultDictionary : resultDictionaries)
            items.add(resultDictionary.title);
        binding.spinner.setAdapter(new SpinnerCustomAdapter(getApplicationContext(), items));
    }

    private void initializeArrowButton() {
        binding.imageButtonPrevious.setVisibility(View.GONE);
        binding.imageButtonNext.setOnClickListener(v -> {
            binding.imageButtonPrevious.setVisibility(View.VISIBLE);
            pageNumber++;
            if (pageNumber + 1 == maxNumber) binding.imageButtonNext.setVisibility(View.GONE);
            binding.imageViewOutput.setImageBitmap(bitmaps.get(pageNumber));
        });
        binding.imageButtonPrevious.setOnClickListener(v -> {
            binding.imageButtonNext.setVisibility(View.VISIBLE);
            pageNumber--;
            if (pageNumber == 0) binding.imageButtonPrevious.setVisibility(View.GONE);
            binding.imageViewOutput.setImageBitmap(bitmaps.get(pageNumber));
        });
    }

    private void setOnAcceptClickListener() {
        binding.buttonAccepted.setOnClickListener(v -> {
            if (binding.signatureView1.isBitmapEmpty() || binding.signatureView2.isBitmapEmpty())
                new CustomToast().warning(getString(R.string.request_sign), Toast.LENGTH_LONG);
            else addImageSign();
        });
    }

    private void addImageSign() {
        final Bitmap bitmap1 = binding.signatureView1.getSignatureBitmap();
        final Bitmap bitmap2 = binding.signatureView2.getSignatureBitmap();
        binding.signatureView1.setVisibility(View.GONE);
        binding.signatureView2.setVisibility(View.GONE);
        finalSubmit = true;
        createOutputImage(bitmap1, bitmap2);
    }

    private void createOutputImage(Bitmap... tempBitmaps) {
        bitmaps.clear();
        try {
            if (tempBitmaps != null && tempBitmaps.length > 0) {
                new PrepareOutputImage(this, examinerDuty, licence, licenceRows, this,
                        tempBitmaps[0], tempBitmaps[1]).execute(this);
            } else {
                new PrepareOutputImage(this, examinerDuty, licence, this).execute(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFormImageView(Bitmap[] bitmap, Object... objects) {
        bitmaps.addAll(Arrays.asList(bitmap));
        pageNumber = 0;
        runOnUiThread(() -> {
            binding.imageViewOutput.setImageBitmap(bitmap[0]);
            binding.imageButtonPrevious.setVisibility(View.GONE);
            binding.imageButtonNext.setVisibility(View.VISIBLE);
        });
        if (licence) {
            bitmaps.add(2, (Bitmap) objects[0]);
            licenceRows = (List<String[]>) objects[1];
        }
//        if (finalSubmit) sendImages();
    }

    private void finalSubmit() {
        if (sent)
            new UploadNavigated(examinerDuty,
                    resultDictionaries.get(binding.spinner.getSelectedItemPosition()).id, this).execute(this);
        else
            getApplicationComponent().MyDatabase().examinerDutiesDao().updateExamination(true, examinerDuty.trackNumber);
        finish();
    }

    public void sendImages() {
        if (imageNumber == bitmaps.size())
            finalSubmit();
        else {
            final int id;
            switch (imageNumber) {
                case 2:
                    id = crookiTitle;
                    break;
                case 3:
                    id = licenceTitle;
                    break;
                case 1:
                default:
                    id = estimateTitle;
                    break;
            }
            new UploadImages(id, examinerDuty.trackNumber, examinerDuty.billId != null ?
                    examinerDuty.billId : examinerDuty.neighbourBillId, examinerDuty.isNewEnsheab,
                    this).execute(this);
        }
        setImageNumber();
    }

    private void setImageNumber() {
        imageNumber++;
    }

    public Bitmap getBitmap() {
        return bitmaps.get(imageNumber - 1);
    }

    public void setSent(boolean b) {
        sent = b;
    }
}