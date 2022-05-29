package com.leon.estimate_new.fragments.main_items;

import static com.leon.estimate_new.helpers.MyApplication.getLocationTracker;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.leon.estimate_new.databinding.FragmentHomeBinding;
import com.leon.estimate_new.enums.MapType;
import com.leon.estimate_new.utils.gis.GoogleMapLayer;

import org.jetbrains.annotations.NotNull;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private Activity activity;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = requireActivity();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        initialize();
        return binding.getRoot();
    }

    private void initialize() {
        initializeMap();
    }

    private void initializeMap() {
        final ArcGISMap map = new ArcGISMap();
        binding.mapView.setMap(map);
//        binding.mapView.getMap().getBasemap().getBaseLayers().add(new OpenStreetMapLayer());
//        binding.mapView.getMap().getBasemap().getBaseLayers().add(new OsmMapLayer().createLayer());
        binding.mapView.getMap().getBasemap().getBaseLayers().add(new GoogleMapLayer().createLayer(MapType.VECTOR));

        binding.mapView.setMagnifierEnabled(true);
        binding.mapView.setCanMagnifierPanMap(true);

        AsyncTask.execute(() -> {
            while (getLocationTracker(activity).getLocation() == null)
                binding.progressBar.setVisibility(View.VISIBLE);
            binding.mapView.setViewpoint(new Viewpoint(getLocationTracker(activity).getLatitude()
                    , getLocationTracker(activity).getLongitude(), 3600));
            activity.runOnUiThread(() -> binding.progressBar.setVisibility(View.GONE));
        });
    }

    @Override
    public void onResume() {
        binding.mapView.resume();
        super.onResume();
    }

    @Override
    public void onPause() {
        binding.mapView.pause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.mapView.dispose();
    }
}