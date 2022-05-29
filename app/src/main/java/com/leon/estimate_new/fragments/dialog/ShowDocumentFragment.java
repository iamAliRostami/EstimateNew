package com.leon.estimate_new.fragments.dialog;

import static com.leon.estimate_new.enums.BundleEnum.BILL_ID;
import static com.leon.estimate_new.enums.BundleEnum.IS_NEIGHBOUR;
import static com.leon.estimate_new.enums.BundleEnum.NEW_ENSHEAB;
import static com.leon.estimate_new.enums.BundleEnum.TRACK_NUMBER;
import static com.leon.estimate_new.utils.CustomFile.loadImage;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.leon.estimate_new.adapters.ImageViewAdapter;
import com.leon.estimate_new.databinding.FragmentShowDocumentBinding;
import com.leon.estimate_new.tables.ImageData;
import com.leon.estimate_new.tables.ImageDataThumbnail;
import com.leon.estimate_new.tables.ImageDataTitle;
import com.leon.estimate_new.tables.Images;
import com.leon.estimate_new.utils.estimating.ImageThumbnail;
import com.leon.estimate_new.utils.estimating.ImageThumbnailList;
import com.leon.estimate_new.utils.estimating.ImageTitles;
import com.leon.estimate_new.utils.estimating.LoginDocument;

import java.util.ArrayList;

import okhttp3.ResponseBody;

public class ShowDocumentFragment extends DialogFragment {
    private final ArrayList<ImageData> dataThumbnail = new ArrayList<>();
    private final ArrayList<Images> images = new ArrayList<>();
    private final ArrayList<String> dataThumbnailUri = new ArrayList<>();
    private FragmentShowDocumentBinding binding;
    private String billId, trackNumber;
    private int position = 0;
    private boolean isNew, isNeighbour;
    private ImageViewAdapter imageViewAdapter;

    public ShowDocumentFragment() {
    }

    public static ShowDocumentFragment newInstance(String billId, boolean isNew, boolean isNeighbour
            , String... trackNumber) {
        final ShowDocumentFragment fragment = new ShowDocumentFragment();
        final Bundle args = new Bundle();
        if (trackNumber.length > 0)
            args.putString(TRACK_NUMBER.getValue(), trackNumber[0]);
        args.putString(BILL_ID.getValue(), billId);
        args.putBoolean(NEW_ENSHEAB.getValue(), isNew);
        args.putBoolean(IS_NEIGHBOUR.getValue(), isNeighbour);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isNew = getArguments().getBoolean(NEW_ENSHEAB.getValue());
            isNeighbour = getArguments().getBoolean(IS_NEIGHBOUR.getValue());
            trackNumber = getArguments().getString(TRACK_NUMBER.getValue());
            billId = getArguments().getString(BILL_ID.getValue());
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentShowDocumentBinding.inflate(inflater, container, false);
        initialize();
        return binding.getRoot();
    }

    private void initialize() {
        new LoginDocument(requireContext(), this).execute(requireActivity());
    }

    public void successLogin() {
        new ImageTitles(requireContext(), this).execute(requireActivity());
    }

    public void setTitles(ImageDataTitle body) {
        //    private final ArrayList<String> titles = new ArrayList<>();
        //        for (int i = 0; i < imageDataTitle.data.size(); i++)
//            titles.add(imageDataTitle.data.get(i).title);
//        imageDataTitle.data.stream().map(dataTitle -> dataTitle.title).forEach(titles::add);
        if (!isNeighbour) {
            images.addAll(loadImage(trackNumber, billId, body.data, requireContext()));
            imageViewAdapter = new ImageViewAdapter(requireContext(), images);
            binding.gridViewImage.setAdapter(imageViewAdapter);
        }
        new ImageThumbnailList(requireContext(), isNew ? trackNumber : billId, this).execute(requireActivity());
    }

    public ProgressBar getProgressBar() {
        return binding.progressBar;
    }

    public void setThumbnails(final ImageDataThumbnail thumbnails) {
        dataThumbnail.addAll(thumbnails.data);
        for (ImageData data : dataThumbnail) {
            dataThumbnailUri.add(data.img);
        }
        setImage();
    }

    public void setImage(ResponseBody... body) {
        if (body.length > 0) {
            images.add(new Images(billId, trackNumber,
                    dataThumbnail.get(position - 1).title_name,
                    dataThumbnailUri.get(position - 1),
                    BitmapFactory.decodeStream(body[0].byteStream()), false));
            //TODO
            imageViewAdapter.notifyDataSetChanged();
//            prepareImageAdapter();
        }
        if (dataThumbnail.size() > position)//TODO
            new ImageThumbnail(dataThumbnail.get(position).img, this).execute(requireActivity());
        else binding.progressBar.setVisibility(View.GONE);
        position++;
    }

    private void prepareImageAdapter() {
        final ImageViewAdapter imageViewAdapter = new ImageViewAdapter(requireContext(), images);
        binding.gridViewImage.setAdapter(imageViewAdapter);
    }

    @Override
    public void onResume() {
        if (getDialog() != null) {
            WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            getDialog().getWindow().setAttributes(params);
        }
        super.onResume();
    }
}