package com.carlos.common.utils;

import android.content.Context;
import com.carlos.libcommon.StringFog;
import com.kook.librelease.R.array;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class StringUtils {
   private static boolean isTencentMap = true;

   public static String replaceChar2JsonString(String baseString) {
      return baseString.replace(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JxUEHnsjSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pj5SVg=="))).replace(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JxUEHnsjSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pj5SVg=="))).replace(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("J18MVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pj5SVg=="))).replace(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pj1bVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KC5SVg=="))).replace(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PjtbVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IC5SVg=="))).replace(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JxUECA==")), "\n").replace(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JxgcVg==")), "").replace(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JxUELA==")), "").replace(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JwQMVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JwhSVg=="))).replace(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LwQMVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LwhSVg=="))).replace(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JxUELA==")), "").replace(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JxcqVg==")), "").replace(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PT4+DW8OQVo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PT5SVg==")));
   }

   public static boolean isString(String tag) {
      return tag != null && !"".equals(tag);
   }

   public static double doubleFor6(double num) {
      return isTencentMap ? num : Double.parseDouble(doubleFor6String(num));
   }

   public static double doubleFor8(double num) {
      return isTencentMap ? num : Double.parseDouble(doubleFor8String(num));
   }

   public static String doubleFor8String(double num) {
      if (isTencentMap) {
         return String.valueOf(num);
      } else {
         DecimalFormat formater = new DecimalFormat();
         formater.setMaximumFractionDigits(8);
         return formater.format(num);
      }
   }

   public static String doubleFor6String(double num) {
      if (isTencentMap) {
         return String.valueOf(num);
      } else {
         DecimalFormat formater = new DecimalFormat();
         formater.setMaximumFractionDigits(6);
         return formater.format(num);
      }
   }

   public static String getTimeNoHour(long time) {
      Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JSwIBnU0IFo="))));
      calendar.setTimeInMillis(time);
      int h = calendar.get(11);
      int m = calendar.get(12);
      int s = calendar.get(13);
      return String.format(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PQM5KmgOTCtPViww")), h * 60 + m, s);
   }

   public static String getTime(long time) {
      Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JSwIBnU0IFo="))));
      calendar.setTimeInMillis(time);
      int h = calendar.get(11);
      int m = calendar.get(12);
      int s = calendar.get(13);
      return String.format(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PQM5KmgOTCtPViwwPTkLKnoVBlo=")), h, m, s);
   }

   public static String timeForString(Context context, long tag) {
      Calendar calendar = Calendar.getInstance(Locale.getDefault());
      calendar.setTimeInMillis(tag);
      int week = calendar.get(7);
      String[] strs = context.getResources().getStringArray(array.weeks);
      String str = strs[week - 1];
      SimpleDateFormat dateFm = new SimpleDateFormat(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KAcYJ2lSEg1oCl0wKF4mGQ==")) + str + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JwQ6CmUOTSNgAVRF")));
      return dateFm.format(tag);
   }
}
