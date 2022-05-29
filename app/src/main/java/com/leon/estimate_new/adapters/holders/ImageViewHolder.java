package com.leon.estimate_new.adapters.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.leon.estimate_new.R;

public class ImageViewHolder {
    public final ImageView imageView;
    public final TextView textView;

    public ImageViewHolder(View view) {
        imageView = view.findViewById(R.id.image_view);
        textView = view.findViewById(R.id.text_view_title);
    }
}
