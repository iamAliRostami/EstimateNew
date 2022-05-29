package com.leon.estimate_new.utils;

import static com.leon.estimate_new.helpers.Constants.IMAGE_FILE_NAME;
import static com.leon.estimate_new.helpers.Constants.MAX_IMAGE_SIZE;
import static com.leon.estimate_new.helpers.MyApplication.getApplicationComponent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.widget.Toast;

import com.leon.estimate_new.R;
import com.leon.estimate_new.tables.DataTitle;
import com.leon.estimate_new.tables.Images;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CustomFile {
    @SuppressLint("SimpleDateFormat")
    public static MultipartBody.Part bitmapToFile(Bitmap bitmap, Context context) {
        final String fileNameToSave = "JPEG_" + new Random().nextInt() + "_" +
                (new SimpleDateFormat(context.getString(R.string.save_format_name)))
                        .format(new Date()) + ".jpg";
        final File f = new File(context.getCacheDir(), fileNameToSave);
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final byte[] bitmapData = compressBitmapToByte(bitmap);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(f);
            fos.write(bitmapData);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        final RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), f);
        return MultipartBody.Part.createFormData("imageFile", f.getName(), requestBody);
    }

    static byte[] compressBitmapToByte(Bitmap bitmap) {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    @SuppressLint({"SimpleDateFormat"})
    public static File createImageFile(Context context) throws IOException {
        final String timeStamp = (new SimpleDateFormat(context.getString(R.string.save_format_name))).format(new Date());
        final String imageFileName = "JPEG_" + timeStamp + "_";
        final File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        final File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        final StringBuilder stringBuilder = (new StringBuilder()).append("file:");
        IMAGE_FILE_NAME = stringBuilder.append(image.getAbsolutePath()).toString();
        return image;
    }

    public static Bitmap compressBitmap(Bitmap original) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            original.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            if (stream.toByteArray().length > MAX_IMAGE_SIZE) {
                final int width, height;
                if (original.getHeight() > original.getWidth() && original.getHeight() > 1200) {
                    height = 1200;
                    width = original.getWidth() / (original.getHeight() / height);
                } else if (original.getWidth() > 1200) {
                    width = 1200;
                    height = original.getHeight() / (original.getWidth() / width);
                } else {
                    height = original.getHeight();
                    width = original.getWidth();
                }
                original = Bitmap.createScaledBitmap(original, width, height, false);
                stream = new ByteArrayOutputStream();
                original.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            }
            return original;
        } catch (Exception e) {
            new CustomToast().error(e.getMessage(), Toast.LENGTH_LONG);
        }
        return null;
    }

    public static void saveTempBitmap(Bitmap bitmap, Context context, String billId,
                                      String trackNumber, int docId, String docTitle,
                                      boolean isNew) {
        if (isExternalStorageWritable()) {
            saveImage(bitmap, context, billId, trackNumber, docId, docTitle, isNew);
        } else {
            new CustomToast().error(context.getString(R.string.error_external_storage_is_not_writable), Toast.LENGTH_LONG);
        }
    }

    @SuppressLint("SimpleDateFormat")
    static void saveImage(Bitmap bitmapImage, Context context, String billId, String trackNumber,
                          int docId, String docTitle, boolean isNew) {
        final File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES) + context.getString(R.string.camera_folder));
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return;
            }
        }
        IMAGE_FILE_NAME = "JPEG_" + (new SimpleDateFormat(context.getString(R.string.save_format_name)))
                .format(new Date()) + ".jpg";
        final File file = new File(mediaStorageDir, IMAGE_FILE_NAME);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            final Images image = new Images(IMAGE_FILE_NAME, billId, trackNumber, docId, docTitle,
                    bitmapImage, true);
            if (isNew)
                image.billId = "";
            else image.trackingNumber = "";
            getApplicationComponent().MyDatabase().imagesDao().insertImage(image);
        } catch (Exception e) {
            e.printStackTrace();
            new CustomToast().error(e.getMessage());
        }
        MediaScannerConnection.scanFile(context, new String[]{file.getPath()}, new String[]{"image/jpeg"}, null);
    }

    public static ArrayList<Images> loadImage(String trackNumber, String billId,
                                              ArrayList<DataTitle> dataTitles, Context context) {
        final ArrayList<Images> images = new ArrayList<>(getApplicationComponent().MyDatabase()
                .imagesDao().getImagesByTrackingNumberOrBillId(trackNumber, billId));
        for (int i = 0; i < images.size(); i++) {
            try {
                File f = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), context.getString(R.string.camera_folder));
                f = new File(f, images.get(i).address);
                images.get(i).bitmap = BitmapFactory.decodeStream(new FileInputStream(f));
                if (dataTitles != null) {
                    for (int j = 0; j < dataTitles.size(); j++) {
                        if (images.get(i).docId.equals(String.valueOf(dataTitles.get(j).id)))
                            images.get(i).docTitle = dataTitles.get(j).title;
                    }
                }
            } catch (FileNotFoundException e) {
                new CustomToast().error(e.getMessage());
            }
        }
        return images;
    }

    public static Images getImage(Images images, Context context) {
        try {
            File f = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), context.getString(R.string.camera_folder));
            f = new File(f, images.address);
            images.bitmap = BitmapFactory.decodeStream(new FileInputStream(f));
            return images;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
