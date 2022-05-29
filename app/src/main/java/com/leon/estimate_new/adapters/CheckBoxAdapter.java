package com.leon.estimate_new.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.leon.estimate_new.R;
import com.leon.estimate_new.adapters.holders.CheckBoxViewHolder;
import com.leon.estimate_new.tables.RequestDictionary;

import java.util.ArrayList;
import java.util.List;

public class CheckBoxAdapter extends BaseAdapter {
    private final ArrayList<RequestDictionary> requestDictionaries;
    private final LayoutInflater inflater;

    public CheckBoxAdapter(Context context, List<RequestDictionary> requestDictionaries) {
        this.requestDictionaries = new ArrayList<>(requestDictionaries);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return requestDictionaries.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("InflateParams")
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.item_check_box, null);
        }
        final CheckBoxViewHolder holder = new CheckBoxViewHolder(view);
        holder.checkBox.setText(requestDictionaries.get(position).title);
        holder.checkBox.setOnCheckedChangeListener((compoundButton, b) ->
                requestDictionaries.get(position).isSelected = b);
        holder.checkBox.setChecked(requestDictionaries.get(position).isSelected);
        return view;
    }

    public ArrayList<RequestDictionary> getRequestDictionaries() {
        return requestDictionaries;
    }

    public boolean isRequestDictionariesEmpty() {
        for (RequestDictionary requestDictionary : requestDictionaries) {
            if (requestDictionary.isSelected)
                return true;
        }
        return false;
    }
}
