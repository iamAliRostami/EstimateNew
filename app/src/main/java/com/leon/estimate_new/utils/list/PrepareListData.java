package com.leon.estimate_new.utils.list;

import static com.leon.estimate_new.helpers.MyApplication.getApplicationComponent;

import android.app.Activity;
import android.content.Context;

import com.leon.estimate_new.base_items.BaseAsync;
import com.leon.estimate_new.fragments.main_items.DutiesListFragment;
import com.leon.estimate_new.tables.ExaminerDuties;

import java.util.ArrayList;

public class PrepareListData extends BaseAsync {
    private final Object object;

    public PrepareListData(Context context, Object o) {
        super(context, o);
        object = o;
    }

    @Override
    public void postTask(Object o) {
    }

    @Override
    public void preTask(Object o) {

    }

    @Override
    public void backgroundTask(Activity activity) {
        ((DutiesListFragment) object).initializeData((ArrayList<ExaminerDuties>)
                getApplicationComponent().MyDatabase().examinerDutiesDao().ExaminerDuties());
    }

    @Override
    public void backgroundTask(Context context) {

    }
}
