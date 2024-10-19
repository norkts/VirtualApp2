package com.lody.virtual.helper.utils;

import android.os.Bundle;
import android.os.Build.VERSION;
import android.util.Log;
import com.lody.virtual.StringFog;
import java.util.Iterator;
import java.util.Set;

public class VLog {
   public static boolean OPEN_LOG = true;
   public static final String TAG_DEFAULT = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("ITw9DQ=="));

   public static void i(String tag, String msg, Object... format) {
      if (OPEN_LOG) {
         Log.i(tag, String.format(msg, format));
      }

   }

   public static void d(String tag, String msg, Object... format) {
      if (OPEN_LOG) {
         Log.d(tag, String.format(msg, format));
      }

   }

   public static void d(String msg, Object... format) {
      if (OPEN_LOG) {
         Log.d(TAG_DEFAULT, String.format(msg, format));
      }

   }

   public static void logbug(String tag, String msg) {
      d(tag, msg);
   }

   public static void w(String tag, String msg, Object... format) {
      if (OPEN_LOG) {
         Log.w(tag, String.format(msg, format));
      }

   }

   public static void e(String tag, String msg) {
      if (OPEN_LOG) {
         Log.e(tag, msg);
      }

   }

   public static void e(String tag, String msg, Object... format) {
      if (OPEN_LOG) {
         Log.e(tag, String.format(msg, format));
      }

   }

   public static void v(String tag, String msg) {
      if (OPEN_LOG) {
         Log.v(tag, msg);
      }

   }

   public static void v(String tag, String msg, Object... format) {
      if (OPEN_LOG) {
         Log.v(tag, String.format(msg, format));
      }

   }

   public static String toString(Bundle bundle) {
      if (bundle == null) {
         return null;
      } else if (Reflect.on((Object)bundle).get(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwU6P28jLCtgHlE/KBU2OWUzQVo="))) == null) {
         return bundle.toString();
      } else {
         Set<String> keys = bundle.keySet();
         StringBuilder stringBuilder = new StringBuilder(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jj0uCGgFHitvJ1RF")));
         if (keys != null) {
            Iterator var3 = keys.iterator();

            while(var3.hasNext()) {
               String key = (String)var3.next();
               stringBuilder.append(key);
               stringBuilder.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PwhSVg==")));
               stringBuilder.append(bundle.get(key));
               stringBuilder.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("MxhSVg==")));
            }
         }

         stringBuilder.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JwhSVg==")));
         return stringBuilder.toString();
      }
   }

   public static String getStackTraceString(Throwable tr) {
      return Log.getStackTraceString(tr);
   }

   public static void printStackTrace(String tag) {
      Log.e(tag, getStackTraceString(new Exception()));
   }

   public static void e(String tag, Throwable e) {
      Log.e(tag, getStackTraceString(e));
   }

   public static void printException(Exception e) {
      StackTraceElement[] stackTrace = e.getStackTrace();
      Throwable cause = e.getCause();
      d(TAG_DEFAULT, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jj0uCWoFMyZuMjAAOyscRGdSMF5jH10MOxYcXUtSQCM=")) + VERSION.SDK_INT);
      if (cause != null) {
         String stackTraceString = Log.getStackTraceString(cause);
         e(TAG_DEFAULT, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxsBHEZbRjBLHig7LAgqPXwwTQRqARo/JhdfJWIFOD9vDlkdOik6Vg==")) + stackTraceString);
      } else {
         e(TAG_DEFAULT, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxsBHEZbRjB3N1RF")) + e.toString() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsJICl9ATApKAMmMWoJTSZvAQIdPQMAVg==")) + (cause == null));

         for(int i = 0; i < stackTrace.length; ++i) {
            e(TAG_DEFAULT, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JQdfOWgaIAZjDh42PxcLIA==")) + stackTrace[i].toString());
         }
      }

   }
}
