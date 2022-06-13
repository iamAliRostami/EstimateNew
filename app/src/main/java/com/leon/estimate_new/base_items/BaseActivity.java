package com.leon.estimate_new.base_items;

import static com.leon.estimate_new.enums.SharedReferenceKeys.DISPLAY_NAME;
import static com.leon.estimate_new.enums.SharedReferenceKeys.USER_CODE;
import static com.leon.estimate_new.helpers.Constants.GPS_CODE;
import static com.leon.estimate_new.helpers.Constants.LOCATION_PERMISSIONS;
import static com.leon.estimate_new.helpers.Constants.PHOTO_PERMISSIONS;
import static com.leon.estimate_new.helpers.Constants.REQUEST_NETWORK_CODE;
import static com.leon.estimate_new.helpers.Constants.REQUEST_WIFI_CODE;
import static com.leon.estimate_new.helpers.Constants.exit;
import static com.leon.estimate_new.helpers.MyApplication.getApplicationComponent;
import static com.leon.estimate_new.helpers.MyApplication.setActivityComponent;
import static com.leon.estimate_new.utils.PermissionManager.checkCameraPermission;
import static com.leon.estimate_new.utils.PermissionManager.checkLocationPermission;
import static com.leon.estimate_new.utils.PermissionManager.enableNetwork;
import static com.leon.estimate_new.utils.PermissionManager.gpsEnabled;
import static com.leon.estimate_new.utils.PermissionManager.isNetworkAvailable;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Debug;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.navigation.NavigationView;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.leon.estimate_new.BuildConfig;
import com.leon.estimate_new.R;
import com.leon.estimate_new.adapters.NavigationDrawerAdapter;
import com.leon.estimate_new.adapters.items.DrawerItem;
import com.leon.estimate_new.databinding.ActivityBaseBinding;
import com.leon.estimate_new.di.view_model.LocationTrackingGoogle;
import com.leon.estimate_new.di.view_model.LocationTrackingGps;
import com.leon.estimate_new.di.view_model.MyDatabaseClientModel;
import com.leon.estimate_new.helpers.MyApplication;
import com.leon.estimate_new.infrastructure.ISharedPreferenceManager;
import com.leon.estimate_new.utils.CustomToast;
import com.leon.estimate_new.utils.PermissionManager;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {
    private Activity activity;
    private Toolbar toolbar;
    private ActivityBaseBinding binding;
    private ISharedPreferenceManager sharedPreferenceManager;

    protected abstract void initialize();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferenceManager = getApplicationComponent().SharedPreferenceModel();
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        binding = ActivityBaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initializeBase();
        if (isNetworkAvailable(getApplicationContext()))
            checkPermissions();
        else enableNetwork(this);
    }

    private void checkPermissions() {
        if (gpsEnabled(this))
            if (!checkLocationPermission(getApplicationContext())) {
                askLocationPermission();
            } else if (!checkCameraPermission(getApplicationContext())) {
                askStoragePermission();
            } else {
                initialize();
            }
    }

    private void askStoragePermission() {
        final PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                new CustomToast().info(getString(R.string.access_granted));
                checkPermissions();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                PermissionManager.forceClose(activity);
            }
        };
        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage(getString(R.string.confirm_permission))
                .setRationaleConfirmText(getString(R.string.allow_permission))
                .setDeniedMessage(getString(R.string.if_reject_permission))
                .setDeniedCloseButtonText(getString(R.string.close))
                .setGotoSettingButtonText(getString(R.string.allow_permission))
                .setPermissions(PHOTO_PERMISSIONS).check();
    }

    private void askLocationPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                new CustomToast().info(getString(R.string.access_granted));
                LocationTrackingGps.setInstance(null);
                LocationTrackingGoogle.setInstance(null);
                setActivityComponent(activity);
                checkPermissions();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                PermissionManager.forceClose(activity);
            }
        };
        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage(getString(R.string.confirm_permission))
                .setRationaleConfirmText(getString(R.string.allow_permission))
                .setDeniedMessage(getString(R.string.if_reject_permission))
                .setDeniedCloseButtonText(getString(R.string.close))
                .setGotoSettingButtonText(getString(R.string.allow_permission))
                .setPermissions(LOCATION_PERMISSIONS).check();
    }


    @SuppressLint("RtlHardcoded")
    private void initializeBase() {
        activity = this;
        setActivityComponent(activity);
        MyDatabaseClientModel.migration(activity);
        final TextView textView = findViewById(R.id.text_view_version);
        textView.setText(sharedPreferenceManager.getStringData(DISPLAY_NAME.getValue())
                .concat(" (").concat(sharedPreferenceManager.getStringData(USER_CODE.getValue())).concat(")"));
        binding.textViewVersion.setText(getString(R.string.version).concat(" ").concat(BuildConfig.VERSION_NAME));
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fillDrawerListView();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle
                (this, binding.drawerLayout, toolbar, R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        binding.drawerLayout.addDrawerListener(toggle);
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        toggle.syncState();
        toolbar.setNavigationOnClickListener(view1 -> binding.drawerLayout.openDrawer(Gravity.RIGHT));
    }

    private void fillDrawerListView() {
        final List<DrawerItem> dataList = DrawerItem.createItemList(
                getResources().getStringArray(R.array.menu),
                getResources().obtainTypedArray(R.array.icons));
        final NavigationDrawerAdapter adapter = new NavigationDrawerAdapter(dataList);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(MyApplication.getContext()));
        binding.recyclerView.setNestedScrollingEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            new CustomToast().info(getString(R.string.how_to_exit));
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == GPS_CODE) {
                checkPermissions();
            }
            if (requestCode == REQUEST_NETWORK_CODE) {
                if (isNetworkAvailable(getApplicationContext()))
                    checkPermissions();
                else PermissionManager.setMobileWifiEnabled(this);
            }
            if (requestCode == REQUEST_WIFI_CODE) {
                if (isNetworkAvailable(getApplicationContext()))
                    checkPermissions();
                else enableNetwork(this);
            }
        }
    }

    @Override
    protected void onStop() {
        Debug.getNativeHeapAllocatedSize();
        System.runFinalization();
        Runtime.getRuntime().totalMemory();
        Runtime.getRuntime().freeMemory();
        Runtime.getRuntime().maxMemory();
        Runtime.getRuntime().gc();
        System.gc();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (exit)
            android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();
    }
}