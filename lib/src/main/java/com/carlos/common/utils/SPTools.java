package com.carlos.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.carlos.libcommon.StringFog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SPTools {
   private static final String TAG = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JC4AD2U2LCB9ASw/KBYmKGkjHitsNygbLT4uDw=="));
   private static final String FILE_NAME = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki5fP28jNCxpESw/KD0MKGkjMClrDjBF"));
   private static SharedPreferences mSharedPreferences;
   private static SharedPreferences.OnSharedPreferenceChangeListener mOnSharedPreferenceChangeListener;

   public static void setOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
      mOnSharedPreferenceChangeListener = onSharedPreferenceChangeListener;
      if (mSharedPreferences != null) {
         mSharedPreferences.registerOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);
      }

   }

   private static SharedPreferences getSharedPreferences(Context context) {
      if (mSharedPreferences == null) {
         mSharedPreferences = context.getSharedPreferences(FILE_NAME, 4);
      }

      return mSharedPreferences;
   }

   public static int getInt(Context context, String keyname, int def) {
      SharedPreferences shared = getSharedPreferences(context);
      int v = shared.getInt(keyname, def);
      return v == def ? def : v;
   }

   public static int getInt(Context context, String keyname) {
      return getInt(context, keyname, -1);
   }

   public static String getString(Context context, String keyname, String defValues) {
      SharedPreferences shared = getSharedPreferences(context);
      String str = shared.getString(keyname, (String)null);
      return str == null ? defValues : str;
   }

   public static String getString(Context context, String keyname) {
      SharedPreferences shared = getSharedPreferences(context);
      String str = shared.getString(keyname, (String)null);
      return str == null ? null : str;
   }

   public static long getLong(Context context, String keyname) {
      SharedPreferences shared = getSharedPreferences(context);
      long str = shared.getLong(keyname, -1L);
      return str;
   }

   public static boolean getBoolean(Context context, String keyname) {
      SharedPreferences shared = getSharedPreferences(context);
      boolean str = shared.getBoolean(keyname, false);
      return str;
   }

   public static boolean getBoolean(Context context, String keyname, boolean defaultBoolean) {
      SharedPreferences shared = getSharedPreferences(context);
      boolean str = shared.getBoolean(keyname, defaultBoolean);
      return str;
   }

   public static void putString(Context context, String keyname, String values) {
      SharedPreferences shared = getSharedPreferences(context);
      SharedPreferences.Editor e = shared.edit();
      e.putString(keyname, values);
      boolean b = e.commit();
      if (b) {
      }

   }

   public static void putIntList(Context context, String keyname, List<Integer> values) {
      SharedPreferences shared = getSharedPreferences(context);
      SharedPreferences.Editor editor = shared.edit();
      editor.putInt(keyname, values.size());

      for(int i = 0; i < values.size(); ++i) {
         editor.putInt(keyname + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jy5SVg==")) + i, (Integer)values.get(i));
      }

      boolean b = editor.commit();
      if (b) {
      }

   }

   public static void putMap(Context context, String key, Map<String, String> datas) {
      SharedPreferences shared = getSharedPreferences(context);
      SharedPreferences.Editor editor = shared.edit();
      JSONArray mJsonArray = new JSONArray();
      Iterator<Map.Entry<String, String>> iterator = datas.entrySet().iterator();
      JSONObject object = new JSONObject();

      while(iterator.hasNext()) {
         Map.Entry<String, String> entry = (Map.Entry)iterator.next();

         try {
            object.put((String)entry.getKey(), entry.getValue());
         } catch (JSONException var10) {
         }
      }

      mJsonArray.put(object);
      editor.putString(key, mJsonArray.toString());
      editor.commit();
   }

   public static Map<String, String> getMap(Context context, String key) {
      SharedPreferences shared = getSharedPreferences(context);
      LinkedHashMap<String, String> datas = new LinkedHashMap();
      String result = shared.getString(key, "");

      try {
         JSONArray array = new JSONArray(result);

         for(int i = 0; i < array.length(); ++i) {
            JSONObject itemObject = array.getJSONObject(i);
            JSONArray names = itemObject.names();
            if (names != null) {
               for(int j = 0; j < names.length(); ++j) {
                  String name = names.getString(j);
                  String value = itemObject.getString(name);
                  datas.put(name, value);
               }
            }
         }
      } catch (JSONException var12) {
         JSONException e = var12;
         e.printStackTrace();
      }

      return datas;
   }

   public static List<Integer> getIntList(Context context, String keyname) {
      List<Integer> environmentList = new ArrayList();
      SharedPreferences shared = getSharedPreferences(context);
      int environNums = shared.getInt(keyname, 0);

      for(int i = 0; i < environNums; ++i) {
         String key = keyname + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jy5SVg==")) + i;
         if (shared.contains(key)) {
            int environItem = shared.getInt(keyname + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jy5SVg==")) + i, 0);
            environmentList.add(environItem);
         }
      }

      return environmentList;
   }

   public static void putStringList(Context context, String keyname, List<String> values) {
      SharedPreferences shared = getSharedPreferences(context);
      SharedPreferences.Editor editor = shared.edit();
      editor.putInt(keyname, values.size());

      for(int i = 0; i < values.size(); ++i) {
         editor.putString(keyname + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jy5SVg==")) + i, (String)values.get(i));
      }

      boolean b = editor.commit();
      if (b) {
      }

   }

   public static List<String> getStringList(Context context, String keyname) {
      List<String> environmentList = new ArrayList();
      SharedPreferences shared = getSharedPreferences(context);
      int environNums = shared.getInt(keyname, 0);

      for(int i = 0; i < environNums; ++i) {
         String key = keyname + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jy5SVg==")) + i;
         if (shared.contains(key)) {
            String environItem = shared.getString(keyname + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jy5SVg==")) + i, (String)null);
            environmentList.add(environItem);
         }
      }

      return environmentList;
   }

   public static void removeListAll(Context context, String keyname) {
      SharedPreferences shared = getSharedPreferences(context);
      SharedPreferences.Editor editor = shared.edit();
      int environNums = shared.getInt(keyname, 0);

      for(int i = 0; i < environNums; ++i) {
         editor.remove(keyname + "" + i);
      }

      boolean b = editor.commit();
   }

   public static void removeListItem(Context context, String keyname, Object value) {
      if (value instanceof String || value instanceof Integer) {
         try {
            throw new Exception(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+DmwVNyhYKyUhAhoVXEcsMRVAFT0yAD9cKUsaGilqVyIvXwkCLBVJNzRnNCwbJQdfLg==")));
         } catch (Exception var8) {
            Exception e = var8;
            e.printStackTrace();
         }
      }

      SharedPreferences shared = getSharedPreferences(context);
      SharedPreferences.Editor editor = shared.edit();
      int environNums = shared.getInt(keyname, 0);

      for(int i = 0; i < environNums; ++i) {
         if (value instanceof Integer) {
            int environItem = shared.getInt(keyname + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jy5SVg==")) + i, 0);
            if (environItem == (Integer)value) {
               editor.remove(keyname + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jy5SVg==")) + i);
               editor.commit();
            }
         } else if (value instanceof String) {
            String environItem = shared.getString(keyname + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jy5SVg==")) + i, (String)null);
            if (environItem.equals((String)value)) {
               editor.remove(keyname + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jy5SVg==")) + i);
               editor.commit();
            }
         }
      }

   }

   public static void putInt(Context context, String keyname, int values) {
      SharedPreferences shared = getSharedPreferences(context);
      SharedPreferences.Editor e = shared.edit();
      e.putInt(keyname, values);
      boolean b = e.commit();
      if (b) {
      }

   }

   public static void putLong(Context context, String keyname, long values) {
      SharedPreferences shared = getSharedPreferences(context);
      SharedPreferences.Editor e = shared.edit();
      e.putLong(keyname, values);
      boolean b = e.commit();
      if (b) {
      }

   }

   public static void putBoolean(Context context, String keyname, boolean values) {
      SharedPreferences shared = getSharedPreferences(context);
      SharedPreferences.Editor e = shared.edit();
      e.putBoolean(keyname, values);
      boolean b = e.commit();
      if (b) {
      }

   }
}
