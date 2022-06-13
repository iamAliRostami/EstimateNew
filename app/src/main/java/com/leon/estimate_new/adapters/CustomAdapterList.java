package com.leon.estimate_new.adapters;

import static com.leon.estimate_new.enums.BundleEnum.EXAMINER_DUTY;
import static com.leon.estimate_new.enums.BundleEnum.LICENCE_TITLE;
import static com.leon.estimate_new.enums.BundleEnum.OTHER_TITLE;
import static com.leon.estimate_new.enums.BundleEnum.TITLE;
import static com.leon.estimate_new.enums.SharedReferenceKeys.TRACK_NUMBER;
import static com.leon.estimate_new.helpers.MyApplication.getPreferenceManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.leon.estimate_new.R;
import com.leon.estimate_new.activities.FinalReportActivity;
import com.leon.estimate_new.activities.FormActivity;
import com.leon.estimate_new.adapters.holders.ViewHolderList;
import com.leon.estimate_new.tables.ExaminerDuties;
import com.leon.estimate_new.utils.CustomToast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CustomAdapterList extends RecyclerView.Adapter<ViewHolderList> {
    private final ArrayList<ExaminerDuties> examinerDutiesTemp = new ArrayList<>();
    private Context context;

    public CustomAdapterList(Context context, ArrayList<ExaminerDuties> examinerDuties) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            examinerDuties.sort(Comparator.comparing(ExaminerDuties::isPeymayesh).thenComparing(ExaminerDuties::getExaminationDay));
        } else {
            Collections.sort(examinerDuties, (obj1, obj2) ->
                    obj1.getExaminationDay().compareTo(obj2.getExaminationDay()));
        }
        this.context = context;
        this.examinerDutiesTemp.addAll(examinerDuties);
    }

    @SuppressLint("InflateParams")
    @NotNull
    public ViewHolderList onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        final LayoutInflater layoutInflater = LayoutInflater.from(context);
        final View view = layoutInflater.inflate(i % 2 == 0 ? R.layout.item_address_1 :
                R.layout.item_address_2, viewGroup, false);
        final ViewHolderList holder = new ViewHolderList(view);
        holder.itemView.setOnClickListener(view1 -> {
            if (examinerDutiesTemp.get(i).isPeymayesh()) {
                new CustomToast().success(context.getString(R.string.is_peymayesh), Toast.LENGTH_LONG);
            } else {
                final String json = new Gson().toJson(examinerDutiesTemp.get(i));
                getPreferenceManager().putData(TRACK_NUMBER.getValue(), examinerDutiesTemp.get(i).trackNumber);
                final Intent intent = new Intent(context, FormActivity.class);
                intent.putExtra(EXAMINER_DUTY.getValue(), json);
                context.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderList viewHolder, int i) {
        final ExaminerDuties examinerDuties = getItem(i);

        viewHolder.textViewName.setText(examinerDuties.nameAndFamily != null &&
                examinerDuties.nameAndFamily.trim().length() > 0 ?
                examinerDuties.nameAndFamily.trim() : "-");
        if (examinerDuties.isPeymayesh()) {
            viewHolder.textViewPeymayesh.setText("پیمایش شده");
            viewHolder.textViewPeymayesh.setBackground(ContextCompat.getDrawable(context, R.drawable.border_green_2));
        } else {
            viewHolder.textViewPeymayesh.setText("پیمایش نشده");
            viewHolder.textViewPeymayesh.setBackground(ContextCompat.getDrawable(context, R.drawable.border_red_2));
        }
        viewHolder.textViewExaminationDay.setText(examinerDuties.getExaminationDay());
        viewHolder.textViewServiceGroup.setText(examinerDuties.serviceGroup);
        viewHolder.textViewAddress.setText(examinerDuties.address != null && !examinerDuties.address.isEmpty() ?
                examinerDuties.address.trim() : "-");
        viewHolder.textViewRadif.setText(examinerDuties.radif != null && examinerDuties.radif.trim().length() > 0 ?
                examinerDuties.radif : "-");
        viewHolder.textViewTrackNumber.setText(examinerDuties.trackNumber);
        viewHolder.textViewNotificationMobile.setText(examinerDuties.notificationMobile);
        viewHolder.textViewMoshtarakMobile.setText(examinerDuties.moshtarakMobile);
        if (examinerDuties.billId != null)
            viewHolder.textViewBillId.setText(examinerDuties.billId);
        else {
            viewHolder.textViewBillId.setText(examinerDuties.neighbourBillId);
            viewHolder.textViewBillIdTitle.setText(context.getString(R.string.neighbour_bill_id));
        }
        viewHolder.textViewName.setGravity(1);
        viewHolder.textViewPeymayesh.setGravity(1);
        viewHolder.textViewExaminationDay.setGravity(1);
        viewHolder.textViewServiceGroup.setGravity(1);
        viewHolder.textViewAddress.setGravity(1);
        viewHolder.textViewRadif.setGravity(1);
        viewHolder.textViewTrackNumber.setGravity(1);
        viewHolder.textViewNotificationMobile.setGravity(1);
        viewHolder.textViewMoshtarakMobile.setGravity(1);
        viewHolder.textViewBillId.setGravity(1);
    }

    @Override
    public int getItemCount() {
        return examinerDutiesTemp.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public ExaminerDuties getItem(int position) {
        return examinerDutiesTemp.get(position);
    }

}

