package com.leon.estimate_new.adapters.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.estimate_new.R;

public class ViewHolderTejariha extends RecyclerView.ViewHolder {
    public final TextView textViewKarbari;
    public final TextView textViewTedadVahed;
    public final TextView textViewVahedMohasebe;
    public final TextView textViewNoeShoql;
    public final TextView textViewA2;
    public final ImageView imageViewMinus;

    public ViewHolderTejariha(@NonNull View itemView) {
        super(itemView);
        textViewKarbari = itemView.findViewById(R.id.text_view_karbari);
        textViewNoeShoql = itemView.findViewById(R.id.text_view_noe_shoql);
        textViewTedadVahed = itemView.findViewById(R.id.text_view_tedad_vahed);
        textViewVahedMohasebe = itemView.findViewById(R.id.text_view_vahed_mohasebe);
        textViewA2 = itemView.findViewById(R.id.text_view_a2);
        imageViewMinus = itemView.findViewById(R.id.image_view_minus);
    }
}
