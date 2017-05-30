package com.example.androidlib.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 先put完，再调用apply()，不要put一次就调用apply()一次
 */
public class SharedPreferencesHelper {

    private static SharedPreferencesHelper sPreferencesHelper;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    public static SharedPreferencesHelper getInstance(Context context, String name) {
        if (sPreferencesHelper == null && context != null) {
            sPreferencesHelper = new SharedPreferencesHelper(context, name);
        }
        return sPreferencesHelper;
    }

    /**
     *
     * @param context
     * @param name SharedPreferences的文件名
     */
    private SharedPreferencesHelper(Context context, String name) {
        mSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public boolean getBooleanValue(String key) {
        boolean value = true;
        if (key != null) {
            value = mSharedPreferences.getBoolean(key, true);
        }
        return value;
    }

    public long getLongValue(String key) {
        long result = 0;
        if (key != null) {
            result = mSharedPreferences.getLong(key, 0);
        }
        return result;
    }

    public String getStringValue(String key) {
        String value = null;
        if (key != null) {
            value = mSharedPreferences.getString(key, null);
        }
        return value;
    }

    public SharedPreferencesHelper putBooleanValue(String key, boolean value) {
        if (key != null) {
            if (mEditor == null) {
                mEditor = mSharedPreferences.edit();
            }
            mEditor.putBoolean(key, value);
        }
        return sPreferencesHelper;
    }

    public SharedPreferencesHelper putLongValue(String key, long value) {
        if (key != null) {
            if (mEditor == null) {
                mEditor = mSharedPreferences.edit();
            }
            mEditor.putLong(key, value);
        }
        return sPreferencesHelper;
    }

    public SharedPreferencesHelper putStringValue(String key, String value) {
        if (key != null) {
            if (mEditor == null) {
                mEditor = mSharedPreferences.edit();
            }
            mEditor.putString(key, value);
        }
        return sPreferencesHelper;
    }

    public SharedPreferencesHelper remove(String key) {
        if (key != null) {
            if (mEditor == null) {
                mEditor = mSharedPreferences.edit();
            }
            mEditor.remove(key);
        }
        return sPreferencesHelper;
    }

    public void apply() {
        mEditor.apply();
    }
}
