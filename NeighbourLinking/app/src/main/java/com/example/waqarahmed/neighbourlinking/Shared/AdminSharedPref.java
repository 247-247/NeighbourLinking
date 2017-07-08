package com.example.waqarahmed.neighbourlinking.Shared;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.waqarahmed.neighbourlinking.Classes.ServiceMan;
import com.google.gson.Gson;

/**
 * Created by waqar on 6/28/2017.
 */

public class AdminSharedPref {
    public static final String MyPREFERENCES = "ADMIN_LOGIN" ;
   // public static final String Name = "SERVICEMANID";
   // static SharedPref instance = null;

    private static SharedPreferences mSharedPref;
  //  public static final String NAME = "NAME";

    public static final String IS_ADMIN = "IS_ADMIN";
    public static final String IMAGE = "IMAGE";
    public static final String EMAIL = "EMAIL";

    private AdminSharedPref()
    {

    }

    public static void init(Context context)
    {
        if(mSharedPref == null)
            mSharedPref = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    public static String read(String key, String defValue) {
        return mSharedPref.getString(key, defValue);
    }

    public static void write(String key, String value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public static boolean read(String key, boolean defValue) {
        return mSharedPref.getBoolean(key, defValue);
    }

    public static void write(String key, boolean value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.commit();
    }


    public static int read(String key, int defValue) {
        return mSharedPref.getInt(key, defValue);
    }

    public static void write(String key, int value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putInt(key, value).commit();
    }

}
