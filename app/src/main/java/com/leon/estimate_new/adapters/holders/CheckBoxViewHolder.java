package com.leon.estimate_new.adapters.holders;

import android.view.View;
import android.widget.CheckBox;

import com.leon.estimate_new.R;

public class CheckBoxViewHolder {
    public final CheckBox checkBox;

    public CheckBoxViewHolder(View view) {
        this.checkBox = (CheckBox) view.findViewById(R.id.checkbox);
    }
}
