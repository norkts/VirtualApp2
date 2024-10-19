package com.kook.common.utils;

import android.content.Context;
import android.os.Build.VERSION;
import android.util.Log;
import com.kook.core.log.StringFog;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HVLog {
   private static Context mContext;
   private static String SIMPLE_NAME = StringFog.decrypt("AAAAAEENEg==");
   public static Boolean LOG_SWITCH = true;
   private static Boolean LOG_WRITE_TO_FILE = false;
   public static String LOG_PATH_SDCARD_DIR = StringFog.decrypt("RBwLCEwQEUh5DgwVDg==");
   private static int SDCARD_LOG_FILE_SAVE_DAYS = 0;
   private static String LOG_FILE_NAME = "";
   private static SimpleDateFormat myLogSdf = new SimpleDateFormat(StringFog.decrypt("EhYWEgAvOEpJAkkwI1UCBhcRBg=="));
   private static SimpleDateFormat logfile = new SimpleDateFormat(StringFog.decrypt("EhYWEgAvOEpJAg=="));
   public static String TAG = StringFog.decrypt("AAAAAEENEkp/CQQVBAwE");
   public static boolean DEBUG = true;

   public static void w(Object msg) {
      String tag = getLogTag();
      log(tag, msg.toString(), 'w');
   }

   public static void w(String tag, Object msg) {
      log(tag, msg.toString(), 'w');
   }

   public static void e(Object msg) {
      String tag = getLogTag();
      log(tag, msg.toString(), 'e');
   }

   public static void e(String tag, Object msg) {
      log(tag, msg.toString(), 'e');
   }

   public static void d(Object msg) {
      String tag = getLogTag();
      log(tag, msg.toString(), 'd');
   }

   public static void d(String tag, Object msg) {
      log(tag, msg.toString(), 'd');
   }

   public static void i(Object msg) {
      String tag = getLogTag();
      log(tag, msg.toString(), 'i');
   }

   public static void i(String tag, Object msg) {
      log(tag, msg.toString(), 'i');
   }

   public static void v(Object msg) {
      String tag = getLogTag();
      log(tag, msg.toString(), 'v');
   }

   public static void v(String tag, Object msg) {
      log(tag, msg.toString(), 'v');
   }

   public static void w(String text) {
      String tag = getLogTag();
      log(tag, text, 'w');
   }

   public static void w(String tag, String text) {
      log(tag, text, 'w');
   }

   public static void e(String text) {
      String tag = getLogTag();
      log(tag, text, 'e');
   }

   public static void e(String tag, String text) {
      log(tag, text, 'e');
   }

   public static void d(String text) {
      String tag = getLogTag();
      log(tag, text, 'd');
   }

   public static void d(String tag, String text) {
      log(tag, text, 'd');
   }

   public static void i(String text) {
      String tag = getLogTag();
      log(tag, text, 'i');
   }

   public static void i(String tag, String text) {
      log(tag, text, 'i');
   }

   public static void v(String text) {
      String tag = getLogTag();
      log(tag, text, 'v');
   }

   public static void v(String tag, String text) {
      log(tag, text, 'v');
   }

   public static String getLogTag() {
      StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
      int index = 0;
      StackTraceElement[] var2 = stackTrace;
      int var3 = stackTrace.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         StackTraceElement element = var2[var4];
         ++index;
         String className = element.getClassName();
         if (className.contains(SIMPLE_NAME)) {
            break;
         }
      }

      int stackLength = stackTrace.length;
      StackTraceElement element;
      if (stackLength > index) {
         element = stackTrace[index + 1];
      } else {
         element = stackTrace[index - 1];
      }

      String className = getSimpleName(element.getClassName());
      String methodName = element.getMethodName();
      int lineNumber = element.getLineNumber();
      String tag = TAG + StringFog.decrypt("RjQ=") + className + StringFog.decrypt("NjQ=") + methodName + StringFog.decrypt("NjQ=") + lineNumber + StringFog.decrypt("Ng==");
      return tag;
   }

   private static void log(String tag, String msg, char level) {
      if (LOG_SWITCH) {
         if (!TAG.equals(tag)) {
            boolean bit = false;
            if (bit) {
               tag = TAG + StringFog.decrypt("Rg==") + tag;
            } else {
               tag = TAG + StringFog.decrypt("Rg==") + tag;
            }
         }

         int logLevel = getSystemSettings();
         List<String> tagList = null;
         boolean TAG_SHOW = false;
         if (mContext != null) {
         }

         if ('e' != level || logLevel != 1 && logLevel != 5 && !TAG_SHOW) {
            if ('w' != level || logLevel != 2 && logLevel != 5 && !TAG_SHOW) {
               if ('d' != level || logLevel != 3 && logLevel != 5 && !TAG_SHOW) {
                  if ('i' == level && (logLevel == 4 || logLevel == 5 || TAG_SHOW)) {
                     Log.i(tag, msg);
                  }
               } else {
                  Log.d(tag, msg);
               }
            } else {
               Log.w(tag, msg);
            }
         } else {
            Log.e(tag, msg);
         }

         if (LOG_WRITE_TO_FILE) {
            writeLogtoFile(String.valueOf(level), tag, msg);
         }
      }

   }

   private static void writeLogtoFile(String mylogtype, String tag, String text) {
      Date nowtime = new Date();
      String needWriteFiel = logfile.format(nowtime);
      String needWriteMessage = myLogSdf.format(nowtime) + "    " + mylogtype + "    " + tag + "    " + text;
      File fileDir = new File(LOG_PATH_SDCARD_DIR);
      if (!fileDir.exists()) {
         fileDir.mkdirs();
      }

      File file = new File(LOG_PATH_SDCARD_DIR, needWriteFiel + LOG_FILE_NAME);

      try {
         FileWriter filerWriter = new FileWriter(file, true);
         BufferedWriter bufWriter = new BufferedWriter(filerWriter);
         bufWriter.write(needWriteMessage);
         bufWriter.newLine();
         bufWriter.close();
         filerWriter.close();
      } catch (IOException var10) {
         IOException e = var10;
         e.printStackTrace();
         Log.e(TAG, StringFog.decrypt("BwAIS8jk7IKow4zE6YrX0xc=") + e.toString());
      }

   }

   public static void delFile() {
      String needDelFiel = logfile.format(getDateBefore());
      File file = new File(LOG_PATH_SDCARD_DIR, needDelFiel + LOG_FILE_NAME);
      if (file.exists()) {
         file.delete();
      }

   }

   private static Date getDateBefore() {
      Date nowtime = new Date();
      Calendar now = Calendar.getInstance();
      now.setTime(nowtime);
      now.set(5, now.get(5) - SDCARD_LOG_FILE_SAVE_DAYS);
      return now.getTime();
   }

   public static void printException(String subtag, Exception e) {
      StackTraceElement[] stackTrace = e.getStackTrace();
      Throwable cause = e.getCause();
      d(subtag, StringFog.decrypt("KRoGB0lMIyJ/NSA3JUE8L2Y9PCl5RlRY") + VERSION.SDK_INT);
      if (cause != null) {
         String stackTraceString = Log.getStackTraceString(cause);
         e(subtag, StringFog.decrypt("jtPtjpXaVQRMExodUQ==") + stackTraceString);
      } else {
         Log.e(subtag, StringFog.decrypt("jtPtjpXaTw==") + e.toString() + StringFog.decrypt("S09PSw0BFBJeA0kRGE8BHkEOVVg=") + (cause == null));

         for(int i = 0; i < stackTrace.length; ++i) {
            e(subtag, StringFog.decrypt("LhcMDl0WHAhDRgxC") + stackTrace[i].toString());
         }
      }

   }

   public static void printException(Exception e) {
      StackTraceElement[] stackTrace = e.getStackTrace();
      Throwable cause = e.getCause();
      d(StringFog.decrypt("KRoGB0lMIyJ/NSA3JUE8L2Y9PCl5RlRY") + VERSION.SDK_INT);
      if (cause != null) {
         String stackTraceString = Log.getStackTraceString(cause);
         e(StringFog.decrypt("jtPtjpXaVQRMExodQx8dAkMWMB9OAxkMAgABQhc=") + stackTraceString);
      } else {
         e(StringFog.decrypt("jtPtjpXaTw==") + e.toString() + StringFog.decrypt("S09PSw0BFBJeA0kRGE8BHkEOVVg=") + (cause == null));

         for(int i = 0; i < stackTrace.length; ++i) {
            e(StringFog.decrypt("LhcMDl0WHAhDRgxC") + stackTrace[i].toString());
         }
      }

   }

   public static void printThrowable(Throwable throwable) {
      String stackTraceString = Log.getStackTraceString(throwable);
      d(StringFog.decrypt("KRoGB0lMIyJ/NSA3JUE8L2Y9PCl5RlRY") + VERSION.SDK_INT);
      e(TAG, StringFog.decrypt("Gx0GBVk2HRVCEQgaBwpPDhc=") + stackTraceString);
   }

   public static void printThrowable(String subtag, Throwable throwable) {
      String stackTraceString = Log.getStackTraceString(throwable);
      d(StringFog.decrypt("KRoGB0lMIyJ/NSA3JUE8L2Y9PCl5RlRY") + VERSION.SDK_INT);
      e(subtag, StringFog.decrypt("Gx0GBVk2HRVCEQgaBwpPDhc=") + stackTraceString);
   }

   public static void printInfo() {
      Throwable ex = new Throwable();
      StackTraceElement[] stackElements = ex.getStackTrace();
      if (stackElements != null) {
         for(int i = 0; i < stackElements.length; ++i) {
            StackTraceElement stackTraceElement = stackElements[i];
            String output = String.format(StringFog.decrypt("ThxHQhdHBksNQxpYQ0ocQg=="), stackTraceElement.getMethodName(), stackTraceElement.getLineNumber(), getSimpleName(stackTraceElement.getClassName()), getPackageName(stackTraceElement.getClassName()));
            Log.i(TAG, output);
         }
      }

   }

   public static void printInfo(String tag) {
      Throwable ex = new Throwable();
      StackTraceElement[] stackElements = ex.getStackTrace();
      if (stackElements != null) {
         for(int i = 0; i < stackElements.length; ++i) {
            StackTraceElement stackTraceElement = stackElements[i];
            String output = String.format(StringFog.decrypt("ThxHQhdHBksNQxpYQ0ocQg=="), stackTraceElement.getMethodName(), stackTraceElement.getLineNumber(), getSimpleName(stackTraceElement.getClassName()), getPackageName(stackTraceElement.getClassName()));
            i(tag, output);
         }
      }

   }

   private static String getSimpleName(String className) {
      return className.substring(className.lastIndexOf(46) + 1);
   }

   private static String getPackageName(String className) {
      return className.substring(0, className.lastIndexOf(46));
   }

   public static int getSystemSettings() {
      if (mContext == null) {
         mContext = getApplication();
      }

      return mContext != null ? ReceiverLog.getInt(mContext, ReceiverLog.LOG_LEVEL) : 0;
   }

   static Context getApplication() {
      try {
         Class<?> activityThreadClass = Class.forName(StringFog.decrypt("CgELGUILEUlMFhlWKgwbAlsLAR55DhsdCgs="));
         Method currentApplicationMethod = activityThreadClass.getMethod(StringFog.decrypt("CBodGUgMASZdFgURCA4bAkIM"));
         Object application = currentApplicationMethod.invoke((Object)null);
         return (Context)application;
      } catch (ClassNotFoundException var3) {
         ClassNotFoundException e = var3;
         e.printStackTrace();
      } catch (NoSuchMethodException var4) {
         NoSuchMethodException e = var4;
         e.printStackTrace();
      } catch (IllegalAccessException var5) {
         IllegalAccessException e = var5;
         e.printStackTrace();
      } catch (InvocationTargetException var6) {
         InvocationTargetException e = var6;
         e.printStackTrace();
      }

      return null;
   }
}
