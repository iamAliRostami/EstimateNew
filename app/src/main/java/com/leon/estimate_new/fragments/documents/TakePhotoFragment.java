package com.leon.estimate_new.fragments.documents;

import static com.leon.estimate_new.enums.BundleEnum.LICENCE_TITLE;
import static com.leon.estimate_new.enums.BundleEnum.OTHER_TITLE;
import static com.leon.estimate_new.enums.BundleEnum.TITLE;
import static com.leon.estimate_new.enums.BundleEnum.TRACK_NUMBER;
import static com.leon.estimate_new.enums.DialogType.YellowRedirect;
import static com.leon.estimate_new.fragments.dialog.ShowFragmentDialog.ShowFragmentDialogOnce;
import static com.leon.estimate_new.utils.CustomFile.compressBitmap;
import static com.leon.estimate_new.utils.CustomFile.createImageFile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.leon.estimate_new.BuildConfig;
import com.leon.estimate_new.R;
import com.leon.estimate_new.activities.FinalReportActivity;
import com.leon.estimate_new.adapters.ImageViewAdapter;
import com.leon.estimate_new.adapters.SpinnerCustomAdapter;
import com.leon.estimate_new.databinding.FragmentTakePhotoBinding;
import com.leon.estimate_new.di.view_model.CustomDialogModel;
import com.leon.estimate_new.di.view_model.HttpClientWrapper;
import com.leon.estimate_new.fragments.dialog.AddDocumentFragment;
import com.leon.estimate_new.tables.DataTitle;
import com.leon.estimate_new.tables.ImageData;
import com.leon.estimate_new.tables.ImageDataThumbnail;
import com.leon.estimate_new.tables.Images;
import com.leon.estimate_new.utils.document.ImageThumbnail;
import com.leon.estimate_new.utils.document.ImageThumbnailList;
import com.leon.estimate_new.utils.document.UploadImages;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.ResponseBody;

public class TakePhotoFragment extends Fragment {
    public Callback documentActivity;
    private final ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null &&
                        result.getData().getData() != null) {
                    try {
                        final InputStream inputStream = requireContext().getContentResolver()
                                .openInputStream(result.getData().getData());
                        final Bitmap bitmap = compressBitmap(BitmapFactory.decodeStream(inputStream));
                        documentActivity.setTakenBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
    private FragmentTakePhotoBinding binding;
    @SuppressLint("UseCompatLoadingForDrawables")
    private final View.OnClickListener onUploadClickListener = v -> {
        if (documentActivity.getBitmap() != null) {
            binding.buttonUpload.setVisibility(View.GONE);
            binding.imageView.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                    R.drawable.icon_finder_camera));
            new UploadImages(this).execute(requireActivity());
        }
    };
    private int position = 0;
    private String path;
    private final ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    documentActivity.setTakenBitmap(compressBitmap(BitmapFactory.decodeFile(path)));
                }
            });
    @SuppressLint("QueryPermissionsNeeded")
    private final View.OnClickListener onPickClickListener = v -> {
        final AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.choose_document);
        builder.setMessage(R.string.select_source);
        builder.setPositiveButton(R.string.gallery, (dialog, which) -> {
            dialog.dismiss();
            openGalleryActivityForResult();
        });
        builder.setNegativeButton(R.string.camera, (dialog, which) -> {
            dialog.dismiss();
            openCameraActivityForResult();
        });
        builder.create().show();
    };

    public static TakePhotoFragment newInstance() {
        return new TakePhotoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTakePhotoBinding.inflate(inflater, container, false);
        initialize();
        setHasOptionsMenu(true);
        return binding.getRoot();
    }

    private void initialize() {
        if (documentActivity.getBitmap() != null) {
            binding.imageView.setImageBitmap(documentActivity.getBitmap());
            binding.buttonUpload.setVisibility(View.VISIBLE);
        }
        binding.buttonPick.setOnClickListener(onPickClickListener);
        binding.buttonUpload.setOnClickListener(onUploadClickListener);
        binding.buttonAccepted.setOnClickListener(v ->
                new ShowDialogue(getString(R.string.accepted_question), getString(R.string.dear_user),
                        getString(R.string.final_accepted), getString(R.string.yes)));
        binding.spinnerTitle.setAdapter(new SpinnerCustomAdapter(requireContext(),
                documentActivity.getTitles()));
        binding.spinnerTitle.setSelection(documentActivity.getSelected());
        if (documentActivity.getDataThumbnail().size() == 0) {
            documentActivity.setImages();
            new ImageThumbnailList(requireContext(), documentActivity.getKey(), this).execute(requireActivity());
        } else binding.progressBar.setVisibility(View.GONE);
        prepareImageAdapter();
    }

    public void setThumbnails(final ImageDataThumbnail thumbnails) {
        documentActivity.setDataThumbnail(thumbnails);
        setImage();
    }

    public void setImage(ResponseBody... body) {
        if (body.length > 0) {
            documentActivity.addImage(new Images(documentActivity.getBillId(), documentActivity.getTrackNumber(),
                    documentActivity.getDataThumbnail().get(position - 1).title_name,
                    documentActivity.getDataThumbnailUri().get(position - 1),
                    BitmapFactory.decodeStream(body[0].byteStream()), false));
        }
        prepareImageAdapter();
        if (documentActivity.getDataThumbnail().size() > position)
            new ImageThumbnail(documentActivity.getDataThumbnail().get(position).img, this).execute(requireActivity());
        else binding.progressBar.setVisibility(View.GONE);
        position++;
    }

    public DataTitle getTitle() {
        return documentActivity.getDataTitle(binding.spinnerTitle.getSelectedItemPosition());
    }

    public void addImage(Images image) {
        documentActivity.addImage(image);
        prepareImageAdapter();
    }

    private void prepareImageAdapter() {
        try {
            final ImageViewAdapter imageViewAdapter = new ImageViewAdapter(requireContext(),
                    documentActivity.getImages());
            binding.gridViewImage.setAdapter(imageViewAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ProgressBar getProgressBar() {
        return binding.progressBar;
    }

    private void openCameraActivityForResult() {
        final Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(requireContext().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile(requireContext());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile != null) {
                path = photoFile.getPath();
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(requireContext(),
                        BuildConfig.APPLICATION_ID.concat(".provider"), photoFile));
                cameraActivityResultLauncher.launch(cameraIntent);
            }
        }
    }

    private void openGalleryActivityForResult() {
        final Intent galleryIntent = new Intent("android.intent.action.PICK");
        if (galleryIntent.resolveActivity(requireContext().getPackageManager()) != null) {
            galleryIntent.setType("image/*");
            galleryActivityResultLauncher.launch(galleryIntent);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if (documentActivity.isNew())
            inflater.inflate(R.menu.add_document_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (item.getItemId() == R.id.add_document_menu) {
            ShowFragmentDialogOnce(requireContext(), "ADD_DOCUMENT", AddDocumentFragment.newInstance(documentActivity.getTrackNumber()));
        }
        return super.onOptionsItemSelected(item);
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

        void setTakenBitmap(Bitmap bitmap);

        ArrayList<String> getTitles();

        int getSelected();

        String getKey();

        String getTrackNumber();

        String getBillId();

        boolean isNew();

        DataTitle getDataTitle(int position);

        ArrayList<DataTitle> getDataTitle();

        ArrayList<String> getDataThumbnailUri();

        ArrayList<ImageData> getDataThumbnail();

        void setDataThumbnail(ImageDataThumbnail thumbnails);

        void setImages();

        void addImage(Images images);

        ArrayList<Images> getImages();
    }

    class ShowDialogue implements CustomDialogModel.Inline {
        ShowDialogue(String message, String title, String top, String positiveButtonText) {
            new CustomDialogModel(YellowRedirect, requireContext(), message,
                    title, top, positiveButtonText, this);
        }

        @Override
        public void inline() {
            if (HttpClientWrapper.call != null) {
                HttpClientWrapper.call.cancel();
                HttpClientWrapper.call = null;
            }
            final Intent intent = new Intent(requireContext(), FinalReportActivity.class);
            int titleId = 0, crookiTitleId = 0, licenceTitleId = 0;
            //TODO
            for (DataTitle dataTitle : documentActivity.getDataTitle()) {
                if (dataTitle.title.equals("فرم ارزیابی"))
                    titleId = dataTitle.id;
                if (dataTitle.title.equals("کروکی"))
                    crookiTitleId = dataTitle.id;
                if (dataTitle.title.equals("مجوز حفاری"))
                    licenceTitleId = dataTitle.id;
            }
            intent.putExtra(TITLE.getValue(), titleId);
            intent.putExtra(OTHER_TITLE.getValue(), crookiTitleId);
            intent.putExtra(LICENCE_TITLE.getValue(), licenceTitleId);
            intent.putExtra(TRACK_NUMBER.getValue(), documentActivity.getTrackNumber());
            startActivity(intent);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            requireActivity().finish();
        }
    }
}