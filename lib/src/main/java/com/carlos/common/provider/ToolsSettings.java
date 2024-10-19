package com.carlos.common.provider;

import android.net.Uri;
import android.provider.BaseColumns;
import com.carlos.libcommon.StringFog;
import com.kook.common.utils.HVLog;

public class ToolsSettings {
   public static final class ServerInfo extends Base implements BaseDataColumns {
      public static int ACCOUNT_TYPE_PHONE = 1;
      public static int ACCOUNT_TYPE_ID = 2;
      public static final String SERVER_INFO = "serverinfo";
      public static final String SERVER_IP = "serverIp";
      public static final String SERVER_PORT = "serverPort";

      public static final Uri getContentUri(String packageName) {
         String URI = "content://" + ScorpionProvider.getAUTHORITY(packageName) + "/%s?" + "notify" + "=true";
         return Uri.parse(String.format(URI, SERVER_INFO));
      }

      public static Uri getContentUri(String packageName, long id, boolean notify) {
         return Uri.parse("content://" + ScorpionProvider.getAUTHORITY(packageName) + "/" + SERVER_INFO + "/" + id + "?" + "notify" + "=" + notify);
      }

      public static String onCreateTable() {
         HVLog.d(" onCreateTable :");
         return "CREATE TABLE serverinfo (_id INTEGER PRIMARY KEY,serverIp TEXT,serverPort TEXT);";
      }
   }

   public interface BaseDataColumns extends BaseColumns {
   }

   public abstract static class Base {
   }
}
