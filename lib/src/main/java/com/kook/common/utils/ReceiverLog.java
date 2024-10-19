package com.kook.common.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings.System;
import android.text.TextUtils;
import android.util.Log;
import com.kook.core.log.StringFog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReceiverLog extends BroadcastReceiver {
   public static String LOG_LEVEL = "logLevel";
   public static String LOG_TAG = "logTag";
   private static String SYSTEM_SETTINGS_KEY = "tag_list_key";
   Context mContext;
   private static SharedPreferences mSharedPreferences;
   private static final String FILE_NAME = "sharedPreferences";

   public void onReceive(Context context, Intent intent) {
      this.mContext = context;
      Log.d("kooklog", "ReceiverLog onReceive");
      if (intent.hasExtra(LOG_LEVEL)) {
         int logLevel = intent.getIntExtra(LOG_LEVEL, 0);
         Log.d("kooklog", "logLevel:" + logLevel);
         putInt(context, LOG_LEVEL, logLevel);
      }

   }

   public void setLogTag(Context context, String logTag) {
      List<String> logTagList = getLogTag(context);
      if (logTagList == null) {
         logTagList = new ArrayList();
      }

      ((List)logTagList).add(logTag);
      String listAsString = TextUtils.join(",", (Iterable)logTagList);
      System.putString(context.getContentResolver(), SYSTEM_SETTINGS_KEY, listAsString);
   }

   public static List<String> getLogTag(Context context) {
      String listAsString = System.getString(context.getContentResolver(), SYSTEM_SETTINGS_KEY);
      if (listAsString != null) {
         List<String> myList = Arrays.asList(listAsString.split(","));
         return myList;
      } else {
         return null;
      }
   }

   public static int getInt(Context context, String keyname) {
      return getInt(context, keyname, -1);
   }

   public static int getInt(Context context, String keyname, int def) {
      SharedPreferences shared = getSharedPreferences(context);
      int v = shared.getInt(keyname, def);
      return v == def ? def : v;
   }

   public static void putInt(Context context, String keyname, int values) {
      SharedPreferences shared = getSharedPreferences(context);
      SharedPreferences.Editor e = shared.edit();
      e.putInt(keyname, values);
      boolean b = e.commit();
      if (b) {
      }

   }

   private static SharedPreferences getSharedPreferences(Context context) {
      if (mSharedPreferences == null) {
         mSharedPreferences = context.getSharedPreferences(FILE_NAME, 4);
      }

      return mSharedPreferences;
   }
}
