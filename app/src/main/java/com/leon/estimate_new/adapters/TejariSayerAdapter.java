package com.leon.estimate_new.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.estimate_new.R;
import com.leon.estimate_new.adapters.holders.ViewHolderTejariha;
import com.leon.estimate_new.tables.Tejariha;

import java.util.ArrayList;


public class TejariSayerAdapter extends RecyclerView.Adapter<ViewHolderTejariha> {
    private final ArrayList<Tejariha> tejarihas;

    public TejariSayerAdapter(ArrayList<Tejariha> tejarihas) {
        this.tejarihas = tejarihas;
    }


    @SuppressLint("NotifyDataSetChanged")
    @NonNull
    @Override
    public ViewHolderTejariha onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        final LayoutInflater layoutInflater = LayoutInflater.from(context);
        final View view = layoutInflater.inflate(R.layout.item_tejari, parent, false);
        final ViewHolderTejariha holder = new ViewHolderTejariha(view);
        holder.imageViewMinus.setOnClickListener(v -> {
            tejarihas.remove(viewType);
            notifyDataSetChanged();
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTejariha holder, int position) {
        final Tejariha tejariha = tejarihas.get(position);
        holder.textViewKarbari.setText(tejariha.karbari);
        holder.textViewA2.setText(String.valueOf(tejariha.a));
        holder.textViewNoeShoql.setText(String.valueOf(tejariha.noeShoql));
        holder.textViewTedadVahed.setText(String.valueOf(tejariha.tedadVahed));
        holder.textViewVahedMohasebe.setText(String.valueOf(tejariha.vahedMohasebe));
    }

    @Override
    public int getItemCount() {
        return tejarihas.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public ArrayList<Tejariha> getTejarihas() {
        return tejarihas;
    }
}
