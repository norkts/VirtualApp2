package com.carlos.common.ui.delegate.hook.utils;

import android.text.TextUtils;
import android.util.Log;
import com.carlos.libcommon.StringFog;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LogUtil {
   private static String TAG = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS5fD28wMFo="));
   private static boolean LOG_DEBUG = true;
   private static final String LINE_SEPARATOR = System.getProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IxgYCGhSBgNiASQ7Iz0iLm8KRVo=")));
   private static final int VERBOSE = 2;
   private static final int DEBUG = 3;
   private static final int INFO = 4;
   private static final int WARN = 5;
   private static final int ERROR = 6;
   private static final int ASSERT = 7;
   private static final int JSON = 8;
   private static final int XML = 9;
   private static final int JSON_INDENT = 4;

   public static void init(boolean isDebug, String tag) {
      TAG = tag;
      LOG_DEBUG = isDebug;
   }

   public static void v(String msg) {
      log(2, (String)null, msg);
   }

   public static void v(String tag, String msg) {
      log(2, tag, msg);
   }

   public static void d(String msg) {
      log(3, (String)null, msg);
   }

   public static void d(String tag, String msg) {
      log(3, tag, msg);
   }

   public static void i(Object... msg) {
      StringBuilder sb = new StringBuilder();
      Object[] var2 = msg;
      int var3 = msg.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Object obj = var2[var4];
         sb.append(obj);
         sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("MxhSVg==")));
      }

      log(4, (String)null, String.valueOf(sb));
   }

   public static void d(Object... msg) {
      StringBuilder sb = new StringBuilder();
      Object[] var2 = msg;
      int var3 = msg.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Object obj = var2[var4];
         sb.append(obj);
         sb.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("MxhSVg==")));
      }

      log(3, (String)null, String.valueOf(sb));
   }

   public static void w(String msg) {
      log(5, (String)null, msg);
   }

   public static void w(String tag, String msg) {
      log(5, tag, msg);
   }

   public static void e(String msg) {
      log(6, (String)null, msg);
   }

   public static void e(String tagStr, String msg) {
      log(6, tagStr, msg);
   }

   public static void e(String tag, String msg, Throwable tr) {
      log(6, tag, msg + '\n' + Log.getStackTraceString(tr));
   }

   public static void a(String msg) {
      log(7, (String)null, msg);
   }

   public static void a(String tag, String msg) {
      log(7, tag, msg);
   }

   public static void json(String json) {
      log(8, (String)null, json);
   }

   public static void json(String tag, String json) {
      log(8, tag, json);
   }

   public static void xml(String xml) {
      log(9, (String)null, xml);
   }

   public static void xml(String tag, String xml) {
      log(9, tag, xml);
   }

   private static void log(int logType, String tagStr, Object objects) {
      String[] contents = wrapperContent(tagStr, objects);
      String tag = contents[0];
      String msg = contents[1];
      String headString = contents[2];
      if (LOG_DEBUG) {
         switch (logType) {
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
               printDefault(logType, tag, headString + msg);
               break;
            case 8:
               printJson(tag, msg, headString);
               break;
            case 9:
               printXml(tag, msg, headString);
         }
      }

   }

   private static void printDefault(int type, String tag, String msg) {
      if (TextUtils.isEmpty(tag)) {
         tag = TAG;
      }

      printLine(tag, true);
      int maxLength = 4000;
      int countOfSub = msg.length();
      if (countOfSub > maxLength) {
         for(int i = 0; i < countOfSub; i += maxLength) {
            if (i + maxLength < countOfSub) {
               printSub(type, tag, msg.substring(i, i + maxLength));
            } else {
               printSub(type, tag, msg.substring(i, countOfSub));
            }
         }
      } else {
         printSub(type, tag, msg);
      }

      printLine(tag, false);
   }

   private static void printSub(int type, String tag, String msg) {
      switch (type) {
         case 2:
            Log.v(tag, msg);
            break;
         case 3:
            Log.d(tag, msg);
            break;
         case 4:
            Log.i(tag, msg);
            break;
         case 5:
            Log.w(tag, msg);
            break;
         case 6:
            d(tag, msg);
            break;
         case 7:
            Log.wtf(tag, msg);
      }

   }

   private static void printJson(String tag, String json, String headString) {
      if (TextUtils.isEmpty(json)) {
         d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JQgIKGwKDSVoNzAoKl4mCmoFNCZ4ETAcLC0qJ2AzFlo=")));
      } else {
         if (TextUtils.isEmpty(tag)) {
            tag = TAG;
         }

         String message;
         try {
            if (json.startsWith(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KC5SVg==")))) {
               JSONObject jsonObject = new JSONObject(json);
               message = jsonObject.toString(4);
            } else if (json.startsWith(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IC5SVg==")))) {
               JSONArray jsonArray = new JSONArray(json);
               message = jsonArray.toString(4);
            } else {
               message = json;
            }
         } catch (JSONException var9) {
            message = json;
         }

         printLine(tag, true);
         message = headString + LINE_SEPARATOR + message;
         String[] lines = message.split(LINE_SEPARATOR);
         String[] var5 = lines;
         int var6 = lines.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            String line = var5[var7];
            printSub(3, tag, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LxhSVg==")) + line);
         }

         printLine(tag, false);
      }
   }

   private static void printXml(String tag, String xml, String headString) {
      if (TextUtils.isEmpty(tag)) {
         tag = TAG;
      }

      if (xml != null) {
         try {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcPGgVBgY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KAguKQ==")));
            transformer.setOutputProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KC5fLGwKIzJOIB4aKgdaDm4gTTdoJ1kgPC4ACGIkAgVlJF09LS42KmsVNARsDQ4oJgcYPGwzLFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Oj5SVg==")));
            transformer.transform(xmlInput, xmlOutput);
            xml = xmlOutput.getWriter().toString().replaceFirst(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pz5SVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PzhXVg==")));
         } catch (Throwable var8) {
            Throwable e = var8;
            e.printStackTrace();
         }

         xml = headString + "\n" + xml;
      } else {
         xml = headString + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OxgAPXsKPC9mHh08Kj4MCG83TSVoN1EgLT0qVg=="));
      }

      printLine(tag, true);
      String[] lines = xml.split(LINE_SEPARATOR);
      String[] var11 = lines;
      int var12 = lines.length;

      for(int var6 = 0; var6 < var12; ++var6) {
         String line = var11[var6];
         if (!TextUtils.isEmpty(line)) {
            printSub(3, tag, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LxhSVg==")) + line);
         }
      }

      printLine(tag, false);
   }

   private static String[] wrapperContent(String tag, Object... objects) {
      if (TextUtils.isEmpty(tag)) {
         tag = TAG;
      }

      StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
      StackTraceElement targetElement = stackTrace[5];
      String className = targetElement.getClassName();
      String[] classNameInfo = className.split(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("J18cVg==")));
      if (classNameInfo.length > 0) {
         className = classNameInfo[classNameInfo.length - 1] + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz5XP2wjJFo="));
      }

      String methodName = targetElement.getMethodName();
      int lineNumber = targetElement.getLineNumber();
      if (lineNumber < 0) {
         lineNumber = 0;
      }

      String methodNameShort = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
      String msg = objects == null ? StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OxgAPXsKPC9mHh08Kj4MCG83TSVoN1EgLT0qVg==")) : getObjectsString(objects);
      String headString = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("ICpfVg==")) + className + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OD5SVg==")) + lineNumber + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PAQ2Vg==")) + methodNameShort + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhUHOA=="));
      return new String[]{tag, msg, headString};
   }

   private static String getObjectsString(Object... objects) {
      if (objects.length > 1) {
         StringBuilder stringBuilder = new StringBuilder();
         stringBuilder.append("\n");

         for(int i = 0; i < objects.length; ++i) {
            Object object = objects[i];
            if (object == null) {
               stringBuilder.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Khg+KmsVElo="))).append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IC5SVg=="))).append(i).append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JwhSVg=="))).append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl4HOA=="))).append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz0uDmoFSFo="))).append("\n");
            } else {
               stringBuilder.append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Khg+KmsVElo="))).append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IC5SVg=="))).append(i).append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JwhSVg=="))).append(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl4HOA=="))).append(object.toString()).append("\n");
            }
         }

         return stringBuilder.toString();
      } else {
         Object object = objects[0];
         return object == null ? StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz0uDmoFSFo=")) : object.toString();
      }
   }

   private static void printLine(String tag, boolean isTop) {
      if (isTop) {
         Log.d(tag, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("AAkrBkEyMUxbAwsCAQoJDERNB0xGAC0RBz8rQFsHFx1HAw8fAkAdQEYEMQ5BNi1THQoNEUcHEwBESRtTAAkrAkEyMUxbAwsCAQoJDERNB0xGAC0RBz8rQFsHFx1HAw8fAkAdQEYEMQ5BNi1THQoNEUcHEwBESRtTAAkrAkEyMUxbAwsCAQoJDERNB0xGAC0RBz8rQFsHFx1HAw8fAkAdQEYEMQ5BNi1THQoNEUcHEwBESRtTAAkrAkEyMUxbAwsCAQoJDERNB0xGAC0RBz8rQFsHFx1HAw8fAkAdQEYEMQ5BNi1THQoNEUcHEwBESRtTAAkrAkEyMUxbAwsCAQoJDERNB0xGAC0RBz8rQFsHFx1HAw8fAkAdQEYEMQ5BNi1THQoNEUcHEwBESRtTAAkrAkEyMUxbAwsCAQoJDERNB0xGAC0RBz8rQFsHFx1HAw8fAkAdQEYEMQ5BNi1THQoNEQ==")));
      } else {
         Log.d(tag, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("AAkrBEEyMUxbAwsCAQoJDERNB0xGAC0RBz8rQFsHFx1HAw8fAkAdQEYEMQ5BNi1THQoNEUcHEwBESRtTAAkrAkEyMUxbAwsCAQoJDERNB0xGAC0RBz8rQFsHFx1HAw8fAkAdQEYEMQ5BNi1THQoNEUcHEwBESRtTAAkrAkEyMUxbAwsCAQoJDERNB0xGAC0RBz8rQFsHFx1HAw8fAkAdQEYEMQ5BNi1THQoNEUcHEwBESRtTAAkrAkEyMUxbAwsCAQoJDERNB0xGAC0RBz8rQFsHFx1HAw8fAkAdQEYEMQ5BNi1THQoNEUcHEwBESRtTAAkrAkEyMUxbAwsCAQoJDERNB0xGAC0RBz8rQFsHFx1HAw8fAkAdQEYEMQ5BNi1THQoNEUcHEwBESRtTAAkrAkEyMUxbAwsCAQoJDERNB0xGAC0RBz8rQFsHFx1HAw8fAkAdQEYEMQ5BNi1THQoNEQ==")));
      }

   }
}
