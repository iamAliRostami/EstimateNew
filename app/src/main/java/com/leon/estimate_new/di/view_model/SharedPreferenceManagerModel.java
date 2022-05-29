package com.leon.estimate_new.di.view_model;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.leon.estimate_new.infrastructure.ISharedPreferenceManager;

import javax.inject.Inject;

public class SharedPreferenceManagerModel implements ISharedPreferenceManager {
    public static SharedPreferenceManagerModel instance;
    private final SharedPreferences sharedPreferences;

    @Inject
    public SharedPreferenceManagerModel(Context context, String xml) {
        sharedPreferences = context.getSharedPreferences(xml, MODE_PRIVATE);
    }

    public static SharedPreferenceManagerModel getInstance(Context context, String xml) {
        if (instance == null)
            instance = new SharedPreferenceManagerModel(context, xml);
        return instance;
    }

    @Override
    public boolean checkIsNotEmpty(String key) {
        if (sharedPreferences == null) {
            return false;
        } else if (sharedPreferences.getString(key, "").length() > 0) {
            return true;
        } else return !sharedPreferences.getString(key, "").isEmpty();
    }

    @Override
    public void putData(String key, int value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putInt(key, value);
        prefsEditor.apply();

    }

    @Override
    public void putData(String key, String data) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, data);
        prefsEditor.apply();
    }

    @Override
    public void putData(String key, boolean value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.apply();
    }

    @Override
    public String getStringData(String key) {
        return sharedPreferences.getString(key, "");
    }

    @Override
    public int getIntData(String key) {
        return sharedPreferences.getInt(key, 1);
    }

    @Override
    public boolean getBoolData(String key) {
        return sharedPreferences.getBoolean(key, false);
    }
}
