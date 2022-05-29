package com.leon.estimate_new.adapters.holders;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.leon.estimate_new.R;

public class ViewHolderList extends RecyclerView.ViewHolder {
    public TextView textViewName;
    public TextView textViewPeymayesh;
    public TextView textViewExaminationDay;
    public TextView textViewServiceGroup;
    public TextView textViewAddress;
    public TextView textViewTrackNumber;
    public TextView textViewNotificationMobile;
    public TextView textViewMoshtarakMobile;
    public TextView textViewBillId;
    public TextView textViewBillIdTitle;
    public TextView textViewRadif;

    public ViewHolderList(View itemView) {
        super(itemView);
        textViewName = itemView.findViewById(R.id.text_view_name);
        textViewPeymayesh = itemView.findViewById(R.id.text_view_peymayesh);
        textViewExaminationDay = itemView.findViewById(R.id.text_view_examination_day);
        textViewServiceGroup = itemView.findViewById(R.id.text_view_service_group);
        textViewAddress = itemView.findViewById(R.id.text_view_address);
        textViewTrackNumber = itemView.findViewById(R.id.text_view_track_number);
        textViewNotificationMobile = itemView.findViewById(R.id.text_view_notification_mobile);
        textViewMoshtarakMobile = itemView.findViewById(R.id.text_view_moshtarak_mobile);
        textViewBillId = itemView.findViewById(R.id.text_view_bill_id);
        textViewBillIdTitle = itemView.findViewById(R.id.text_view_bill_id_title);
        textViewRadif = itemView.findViewById(R.id.text_view_radif);
    }
}
