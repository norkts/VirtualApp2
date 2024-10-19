package com.carlos.common.imagepicker.utils;

import com.carlos.libcommon.StringFog;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
   public static String getImageTime(long time) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(new Date());
      Calendar imageTime = Calendar.getInstance();
      imageTime.setTimeInMillis(time);
      if (sameDay(calendar, imageTime)) {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("B1ZYX0ZaPS8="));
      } else if (sameWeek(calendar, imageTime)) {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BwkBDkZNISA="));
      } else if (sameMonth(calendar, imageTime)) {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BwkBDkYyGwo="));
      } else {
         Date date = new Date(time);
         SimpleDateFormat sdf = new SimpleDateFormat(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KAcYJ2lSEg1oAVRF")), Locale.CANADA);
         return sdf.format(date);
      }
   }

   public static boolean sameDay(Calendar calendar1, Calendar calendar2) {
      return calendar1.get(1) == calendar2.get(1) && calendar1.get(6) == calendar2.get(6);
   }

   public static boolean sameWeek(Calendar calendar1, Calendar calendar2) {
      return calendar1.get(1) == calendar2.get(1) && calendar1.get(3) == calendar2.get(3);
   }

   public static boolean sameMonth(Calendar calendar1, Calendar calendar2) {
      return calendar1.get(1) == calendar2.get(1) && calendar1.get(2) == calendar2.get(2);
   }
}
