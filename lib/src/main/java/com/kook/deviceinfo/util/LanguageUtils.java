package com.kook.deviceinfo.util;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build.VERSION;
import java.util.Locale;
import java.util.TimeZone;

public class LanguageUtils {
   public static Locale getSystemLanguage() {
      return getLocal(Resources.getSystem().getConfiguration());
   }

   public static Locale getLocal(Configuration configuration) {
      return VERSION.SDK_INT >= 24 ? configuration.getLocales().get(0) : configuration.locale;
   }

   public static String getCurrentTimeZone() {
      TimeZone tz = TimeZone.getDefault();
      return createGmtOffsetString(true, true, tz.getRawOffset());
   }

   public static String createGmtOffsetString(boolean includeGmt, boolean includeMinuteSeparator, int offsetMillis) {
      int offsetMinutes = offsetMillis / '\uea60';
      char sign = '+';
      if (offsetMinutes < 0) {
         sign = '-';
         offsetMinutes = -offsetMinutes;
      }

      StringBuilder builder = new StringBuilder(9);
      if (includeGmt) {
         builder.append("GMT");
      }

      builder.append(sign);
      appendNumber(builder, 2, offsetMinutes / 60);
      if (includeMinuteSeparator) {
         builder.append(':');
      }

      appendNumber(builder, 2, offsetMinutes % 60);
      return builder.toString();
   }

   public static void appendNumber(StringBuilder builder, int count, int value) {
      String string = Integer.toString(value);

      for(int i = 0; i < count - string.length(); ++i) {
         builder.append('0');
      }

      builder.append(string);
   }
}
