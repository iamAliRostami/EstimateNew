package com.leon.estimate_new.activities;

import static com.leon.estimate_new.enums.BundleEnum.BILL_ID;
import static com.leon.estimate_new.enums.BundleEnum.NEW_ENSHEAB;
import static com.leon.estimate_new.enums.BundleEnum.TRACK_NUMBER;
import static com.leon.estimate_new.helpers.Constants.BITMAP_SELECTED;
import static com.leon.estimate_new.helpers.Constants.MAP_SELECTED;
import static com.leon.estimate_new.helpers.MyApplication.setActivityComponent;
import static com.leon.estimate_new.utils.CustomFile.loadImage;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.leon.estimate_new.R;
import com.leon.estimate_new.databinding.ActivityDocumentBinding;
import com.leon.estimate_new.di.view_model.HttpClientWrapper;
import com.leon.estimate_new.fragments.documents.BrightnessContrastFragment;
import com.leon.estimate_new.fragments.documents.CropFragment;
import com.leon.estimate_new.fragments.documents.TakePhotoFragment;
import com.leon.estimate_new.tables.DataTitle;
import com.leon.estimate_new.tables.ImageData;
import com.leon.estimate_new.tables.ImageDataThumbnail;
import com.leon.estimate_new.tables.ImageDataTitle;
import com.leon.estimate_new.tables.Images;
import com.leon.estimate_new.utils.CustomToast;
import com.leon.estimate_new.utils.document.ImageTitles;
import com.leon.estimate_new.utils.document.LoginDocument;

import java.util.ArrayList;

public class DocumentActivity extends AppCompatActivity implements TakePhotoFragment.Callback,
        CropFragment.Callback, BrightnessContrastFragment.Callback {
    private final ArrayList<ImageData> dataThumbnail = new ArrayList<>();
    private final ArrayList<String> dataThumbnailUri = new ArrayList<>();
    private final ArrayList<String> titles = new ArrayList<>();
    private final ArrayList<Images> images = new ArrayList<>();
    private final int TAKE_PHOTO_FRAGMENT = 0;
    private final int CROP_FRAGMENT = 1;
    private final int BRIGHTNESS_CONTRAST_FRAGMENT = 2;
    private ActivityDocumentBinding binding;
    private ImageDataTitle imageDataTitle;
    private String trackNumber, billId;
    private boolean isNew, close;
    private Bitmap bitmap;
    private int selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDocumentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            askPermission();
        } else {
            initialize();
        }
    }

    private void getExtra() {
        if (getIntent().getExtras() != null) {
            billId = getIntent().getExtras().getString(BILL_ID.getValue());
            trackNumber = getIntent().getExtras().getString(TRACK_NUMBER.getValue());
            isNew = getIntent().getExtras().getBoolean(NEW_ENSHEAB.getValue());
        }
    }

    private void initialize() {
        setActivityComponent(this);
        getExtra();
        new LoginDocument(this, this).execute(this);
        if (BITMAP_SELECTED != null) {
            bitmap = BITMAP_SELECTED.copy(Bitmap.Config.ARGB_8888, true);
            BITMAP_SELECTED = null;
            MAP_SELECTED = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        }
    }

    public void successLogin() {
        new ImageTitles(this, this).execute(this);
    }

    private void displayView(int position) {
        final String tag = Integer.toString(position);
        if (getFragmentManager().findFragmentByTag(tag) != null && getFragmentManager().findFragmentByTag(tag).isVisible()) {
            return;
        }
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.enter, R.animator.exit,
                R.animator.pop_enter, R.animator.pop_exit);
        fragmentTransaction.replace(binding.containerBody.getId(), getFragment(position), tag);
        if (position != 0) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commitAllowingStateLoss();
        getFragmentManager().executePendingTransactions();
    }

    private Fragment getFragment(int position) {
        close = false;
        switch (position) {
            case BRIGHTNESS_CONTRAST_FRAGMENT:
                return BrightnessContrastFragment.newInstance();
            case CROP_FRAGMENT:
                return CropFragment.newInstance();
            case TAKE_PHOTO_FRAGMENT:
            default:
                close = true;
                return TakePhotoFragment.newInstance();
        }
    }

    private void askPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                new CustomToast().success("مجوز ها داده شده", Toast.LENGTH_LONG);
                initialize();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                new CustomToast().error("مجوز رد شد \n" +
                        deniedPermissions.toString(), Toast.LENGTH_LONG);
                finish();
            }
        };

        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("جهت استفاده از برنامه مجوزهای پیشنهادی را قبول فرمایید")
                .setDeniedMessage("در صورت رد این مجوز قادر به استفاده از این دستگاه نخواهید بود" + "\n" +
                        "لطفا با فشار دادن دکمه اعطای دسترسی و سپس در بخش دسترسی ها با این مجوز ها موافقت نمایید")
                .setPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE).check();
    }

    @Override
    public void setTakenBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        displayView(CROP_FRAGMENT);
    }

    @Override
    public ArrayList<String> getTitles() {
        return titles;
    }

    public void setTitles(ImageDataTitle body) {
        imageDataTitle = body;
        for (int i = 0; i < imageDataTitle.data.size(); i++) {
            if (imageDataTitle.data.get(i).title.equals("کروکی"))
                selected = i;
            titles.add(imageDataTitle.data.get(i).title);
        }
        displayView(TAKE_PHOTO_FRAGMENT);
    }

    @Override
    public int getSelected() {
        return selected;
    }

    @Override
    public String getKey() {
        return isNew ? trackNumber : billId;
    }

    @Override
    public String getTrackNumber() {
        return trackNumber;
    }

    @Override
    public String getBillId() {
        return billId;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @Override
    public DataTitle getDataTitle(int position) {
        return imageDataTitle.data.get(position);
    }

    @Override
    public ArrayList<DataTitle> getDataTitle() {
        return imageDataTitle.data;
    }

    @Override
    public ArrayList<String> getDataThumbnailUri() {
        return dataThumbnailUri;
    }

    @Override
    public ArrayList<ImageData> getDataThumbnail() {
        return dataThumbnail;
    }

    @Override
    public void setDataThumbnail(final ImageDataThumbnail thumbnails) {
        dataThumbnail.addAll(thumbnails.data);
        for (ImageData data : dataThumbnail) {
            dataThumbnailUri.add(data.img);
        }
    }

    @Override
    public void setImages() {
        images.addAll(loadImage(trackNumber, billId, imageDataTitle.data, getApplicationContext()));
    }

    @Override
    public void addImage(Images image) {
        images.add(image);
    }

    @Override
    public ArrayList<Images> getImages() {
        return images;
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public void setFinalBitmap(Bitmap finalBitmap) {
        //TODO
        bitmap = finalBitmap;
        displayView(TAKE_PHOTO_FRAGMENT);
    }

    @Override
    public void setTempBitmap(Bitmap tempBitmap) {
        bitmap = tempBitmap;
    }

    @Override
    public void setCroppedBitmap(Bitmap finalBitmap) {
        bitmap = finalBitmap;
        displayView(BRIGHTNESS_CONTRAST_FRAGMENT);
    }

    @Override
    public void cancelEditing() {
        bitmap = null;
        displayView(TAKE_PHOTO_FRAGMENT);
    }

    @Override
    public void onBackPressed() {
        if (HttpClientWrapper.call != null) {
            HttpClientWrapper.call.cancel();
            HttpClientWrapper.call = null;
        }
        if (close)
            finish();
    }
}