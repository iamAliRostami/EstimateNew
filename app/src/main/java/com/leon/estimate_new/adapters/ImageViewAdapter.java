package com.leon.estimate_new.adapters;

import static com.leon.estimate_new.fragments.dialog.ShowFragmentDialog.ShowFragmentDialogOnce;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.leon.estimate_new.R;
import com.leon.estimate_new.adapters.holders.ImageViewHolder;
import com.leon.estimate_new.fragments.dialog.HighQualityFragment;
import com.leon.estimate_new.tables.Images;
import com.leon.estimate_new.utils.document.ImageHighQuality;

import java.util.ArrayList;

public class ImageViewAdapter extends BaseAdapter {
    private final ArrayList<Images> images;
    private final LayoutInflater inflater;
    private final Context context;

    public ImageViewAdapter(Context c, ArrayList<Images> images) {
        this.images = new ArrayList<>(images);
        context = c;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return images.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final Images imageDataTitle = images.get(position);
        View view = convertView;
        if (view == null) view = inflater.inflate(R.layout.item_image, null);
        final ImageViewHolder holder = new ImageViewHolder(view);
        holder.textView.setText(imageDataTitle.docTitle);
        holder.imageView.setImageBitmap(imageDataTitle.bitmap);
        holder.imageView.setOnClickListener(view1 -> {
            if (imageDataTitle.uri == null)
                ShowFragmentDialogOnce(context, "HIGH_QUALITY_FRAGMENT",
                        HighQualityFragment.newInstance(imageDataTitle.bitmap));
            else
                new ImageHighQuality(context, imageDataTitle.uri.replace("thumbnail",
                        "main")).execute((Activity) context);
        });
        return view;
    }

}
