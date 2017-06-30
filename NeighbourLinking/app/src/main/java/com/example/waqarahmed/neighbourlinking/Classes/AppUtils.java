package com.example.waqarahmed.neighbourlinking.Classes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.view.WindowManager;

import com.example.waqarahmed.neighbourlinking.R;

/**
 * Created by Waqar ahmed on 5/23/2017.
 */

public class AppUtils {
  public static String   MyPREFERENCES = "BADGECOUNTER";

   public static int getPreferenceInt(String PrefVavlue,Context cxt){

       SharedPreferences appPrefs = cxt.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
       int count = appPrefs.getInt(PrefVavlue,0);
       return count;

   }
   public static void savePreferenceLong(String PrefVavlue,int value,Context cxt){

       SharedPreferences appPrefs = cxt.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
       SharedPreferences.Editor notificationPrefsEditor = appPrefs.edit();
       notificationPrefsEditor.putInt(PrefVavlue,value);
       notificationPrefsEditor.commit();
   }

    public static ProgressDialog createProgressDialog(Context mContext) {
        ProgressDialog dialog = new ProgressDialog(mContext);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {

        }
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.progressdialog);
        // dialog.setMessage(Message);
        return dialog;
    }
}
