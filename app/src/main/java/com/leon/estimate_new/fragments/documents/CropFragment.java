package com.leon.estimate_new.fragments.documents;

import static com.leon.estimate_new.utils.ImageUtils.getOutlinePoints;
import static com.leon.estimate_new.utils.ImageUtils.scaledBitmap;
import static team.clevel.documentscanner.helpers.ImageUtils.rotateBitmap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.leon.estimate_new.R;
import com.leon.estimate_new.databinding.FragmentCropBinding;
import com.leon.estimate_new.utils.CustomToast;

import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import team.clevel.documentscanner.libraries.NativeClass;

public class CropFragment extends Fragment {
    private FragmentCropBinding binding;
    private Bitmap bitmapSelectedImage, bitmapTempOriginal;
    private NativeClass nativeClass;
    private boolean isInverted = false;
    private Callback documentActivity;

    public static CropFragment newInstance() {
        return new CropFragment();
    }    @SuppressLint("NonConstantResourceId")
    private final View.OnClickListener onClickListener = view -> {
        switch (view.getId()) {
            case R.id.button_close:
                documentActivity.cancelEditing();
                break;
            case R.id.button_crop:
                setProgressBar(true);
                Observable.fromCallable(() -> {
                            bitmapSelectedImage = getCroppedImage();
                            if (bitmapSelectedImage == null)
                                return false;
                            return false;
                        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe((result) -> {
                            setProgressBar(false);
                            if (bitmapSelectedImage != null) {
                                documentActivity.setCroppedBitmap(bitmapSelectedImage);
                            }
                        });
                break;
            case R.id.image_view_rotate:
                setProgressBar(true);
                Observable.fromCallable(() -> {
                            if (isInverted)
                                invertColor();
                            documentActivity.setTempBitmap(rotateBitmap(bitmapSelectedImage, 90));
                            return false;
                        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe((result) -> {
                            setProgressBar(false);
                            initialize();
                        });
                break;
            case R.id.image_view_rebase:
                bitmapSelectedImage = bitmapTempOriginal.copy(bitmapTempOriginal
                        .getConfig(), true);
                isInverted = false;
                initialize();
                break;
            case R.id.image_view_invert:
                setProgressBar(true);
                Observable.fromCallable(() -> {
                            invertColor();
                            return false;
                        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe((result) -> {
                            setProgressBar(false);
                            binding.imageView.setImageBitmap(scaledBitmap(bitmapSelectedImage,
                                    binding.holderImageCrop.getWidth(),
                                    binding.holderImageCrop.getHeight()));
                        });
                break;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCropBinding.inflate(inflater, container, false);
        initialize();
        return binding.getRoot();
    }

    @SuppressLint("CheckResult")
    private void initialize() {
        nativeClass = new NativeClass();
        setProgressBar(true);
        Observable.fromCallable(() -> {
                    setImageRotation();
                    return false;
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe((result) -> {
                    setProgressBar(false);
                    binding.holderImageCrop.post(this::initializeCropping);
                    binding.buttonCrop.setOnClickListener(onClickListener);
                    binding.buttonClose.setOnClickListener(onClickListener);
                    binding.imageViewRebase.setOnClickListener(onClickListener);
                    binding.imageViewInvert.setOnClickListener(onClickListener);
                    binding.imageViewRotate.setOnClickListener(onClickListener);
                });
    }

    private void initializeCropping() {
        bitmapSelectedImage = documentActivity.getBitmap();
        bitmapTempOriginal = bitmapSelectedImage.copy(bitmapSelectedImage.getConfig(), true);

        binding.imageView.setImageBitmap(scaledBitmap(bitmapSelectedImage,
                binding.holderImageCrop.getWidth(), binding.holderImageCrop.getHeight()));
        final Bitmap tempBitmap = ((BitmapDrawable) binding.imageView.getDrawable()).getBitmap();
        try {
            final Map<Integer, PointF> pointFs = getEdgePoints(tempBitmap);
            binding.polygonView.setPoints(pointFs);
            binding.polygonView.setVisibility(View.VISIBLE);
            int padding = (int) getResources().getDimension(R.dimen.scanPadding);
            final FrameLayout.LayoutParams layoutParams =
                    new FrameLayout.LayoutParams(tempBitmap.getWidth() + 2 * padding,
                            tempBitmap.getHeight() + 2 * padding);
            layoutParams.gravity = Gravity.CENTER;
            binding.polygonView.setLayoutParams(layoutParams);
            binding.polygonView.setPointColor(ContextCompat.getColor(requireContext(), R.color.blue));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setImageRotation() {
        Bitmap tempBitmap = documentActivity.getBitmap().copy(documentActivity.getBitmap().getConfig(), true);
        for (int i = 1; i <= 4; i++) {
            MatOfPoint2f point2f = nativeClass.getPoint(tempBitmap);
            if (point2f == null) {
                tempBitmap = rotateBitmap(tempBitmap, 90 * i);
            } else {
                documentActivity.setTempBitmap(tempBitmap.copy(documentActivity.getBitmap()
                        .getConfig(), true));
                break;
            }
        }
    }

    private void setProgressBar(boolean isShow) {
        setViewInteract(binding.relativeLayoutContainer, !isShow);
        if (isShow)
            binding.progressBar.setVisibility(View.VISIBLE);
        else
            binding.progressBar.setVisibility(View.GONE);
    }

    private void setViewInteract(View view, boolean canDo) {
        view.setEnabled(canDo);
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                setViewInteract(((ViewGroup) view).getChildAt(i), canDo);
            }
        }
    }

    private void invertColor() {
        if (!isInverted) {
            final Bitmap bmpMonochrome = Bitmap.createBitmap(bitmapSelectedImage.getWidth(),
                    bitmapSelectedImage.getHeight(), Bitmap.Config.ARGB_8888);
            final Canvas canvas = new Canvas(bmpMonochrome);
            final ColorMatrix ma = new ColorMatrix();
            ma.setSaturation(0);
            final Paint paint = new Paint();
            paint.setColorFilter(new ColorMatrixColorFilter(ma));
            canvas.drawBitmap(bitmapSelectedImage, 0, 0, paint);
            bitmapSelectedImage = bmpMonochrome.copy(bmpMonochrome.getConfig(), true);
        } else {
            bitmapSelectedImage = bitmapTempOriginal.copy(bitmapTempOriginal.getConfig(),
                    true);
        }
        isInverted = !isInverted;
    }

    protected Bitmap getCroppedImage() {
        try {
            final Map<Integer, PointF> points = binding.polygonView.getPoints();
            final float xRatio = (float) bitmapSelectedImage.getWidth() / binding.imageView.getWidth();
            final float yRatio = (float) bitmapSelectedImage.getHeight() / binding.imageView.getHeight();
            final float x1 = (Objects.requireNonNull(points.get(0)).x) * xRatio;
            final float x2 = (Objects.requireNonNull(points.get(1)).x) * xRatio;
            final float x3 = (Objects.requireNonNull(points.get(2)).x) * xRatio;
            final float x4 = (Objects.requireNonNull(points.get(3)).x) * xRatio;
            final float y1 = (Objects.requireNonNull(points.get(0)).y) * yRatio;
            final float y2 = (Objects.requireNonNull(points.get(1)).y) * yRatio;
            final float y3 = (Objects.requireNonNull(points.get(2)).y) * yRatio;
            final float y4 = (Objects.requireNonNull(points.get(3)).y) * yRatio;
            return nativeClass.getScannedBitmap(bitmapSelectedImage, x1, y1, x2, y2, x3, y3, x4, y4);
        } catch (Exception e) {
            requireActivity().runOnUiThread(() ->
                    new CustomToast().error(getString(R.string.error_incorrect_selection),
                            Toast.LENGTH_LONG));
            return null;
        }
    }

    private Map<Integer, PointF> getEdgePoints(Bitmap tempBitmap) {
        return orderedValidEdgePoints(tempBitmap, getContourEdgePoints(tempBitmap));
    }

    private List<PointF> getContourEdgePoints(Bitmap tempBitmap) {
        MatOfPoint2f point2f = nativeClass.getPoint(tempBitmap);
        if (point2f == null)
            point2f = new MatOfPoint2f();
        final List<Point> points = Arrays.asList(point2f.toArray());
        final List<PointF> result = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            result.add(new PointF(((float) points.get(i).x), ((float) points.get(i).y)));
        }
        return result;
    }

    private Map<Integer, PointF> orderedValidEdgePoints(Bitmap tempBitmap, List<PointF> pointFs) {
        Map<Integer, PointF> orderedPoints = binding.polygonView.getOrderedPoints(pointFs);
        if (!binding.polygonView.isValidShape(orderedPoints)) {
            orderedPoints = getOutlinePoints(tempBitmap);
        }
        return orderedPoints;
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

        void setCroppedBitmap(Bitmap finalBitmap);

        void cancelEditing();
    }


}