package com.example.myhackaton.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myhackaton.helper.json.JsonSerializer;
import com.example.myhackaton.model.Account;
import com.google.gson.reflect.TypeToken;

public class PreferenceHelper {
    private static final String PREF_NAME_APP_SETTINGS = "perf_name_health_app_settings";
    private Context context;
    private SharedPreferences mPref;


    public PreferenceHelper(Context context) {
        this.context = context;
        this.mPref = context.getSharedPreferences(PREF_NAME_APP_SETTINGS, Context.MODE_PRIVATE);
    }

    public synchronized boolean getFirstAppLaunch() {
        String firstKey = "first_launch_app";
        return mPref.getBoolean(firstKey, false);
    }

    public synchronized void setFirstAppLaunch(boolean isFirst) {
        String firstKey = "first_launch_app";
        SharedPreferences.Editor editor = mPref.edit();

        editor.putBoolean(firstKey, isFirst);
        editor.apply();
    }

    public synchronized boolean getMaterPass() {
        String firstKey = "master_launch_app";
        return mPref.getBoolean(firstKey, false);
    }

    public synchronized void setMaterPass(boolean isFirst) {
        String firstKey = "master_launch_app";
        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(firstKey, isFirst);
        editor.apply();
    }

    public synchronized void removeCurrentAccount() {
        SharedPreferences.Editor editor = mPref.edit();
        editor.remove("pref_key_current_account");
        editor.commit();
    }

    public synchronized Account getCurrentAccount() {
        JsonSerializer<Account> serializer =
                new JsonSerializer<>(new TypeToken<Account>() {
                });
        String prefValue = mPref.getString("pref_key_current_account", null);
        if (prefValue == null) {
            return null;
        }
        return serializer.convert(prefValue);
    }

    public synchronized void setCurrentAccount(Account account) {

        JsonSerializer<Account> serializer =
                new JsonSerializer<>(new TypeToken<Account>() {
                });
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString("pref_key_current_account", serializer.convert(account));
        editor.commit();
    }

    public synchronized boolean getIsHost() {
        String firstKey = "is.host";
        return mPref.getBoolean(firstKey, false);
    }

    public synchronized void setIsHost(boolean isHost) {
        String firstKey = "is.host";
        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(firstKey, isHost);
        editor.commit();
    }

}
