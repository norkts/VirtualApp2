package com.carlos.common.provider;

import android.net.Uri;
import android.provider.BaseColumns;
import com.carlos.libcommon.StringFog;
import com.kook.common.utils.HVLog;

public class ToolsSettings {
   public static final class ServerInfo extends Base implements BaseDataColumns {
      public static int ACCOUNT_TYPE_PHONE = 1;
      public static int ACCOUNT_TYPE_ID = 2;
      public static final String SERVER_INFO = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uKmwjNARjDlk+Ki5SVg=="));
      public static final String SERVER_IP = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uKmwjNARrASRF"));
      public static final String SERVER_PORT = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uKmwjNARpHh4qLBhSVg=="));

      public static final Uri getContentUri(String packageName) {
         String URI = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ACGwFNCZmVgU1Oi5SVg==")) + ScorpionProvider.getAUTHORITY(packageName) + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("MyouKX4zSFo=")) + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4ALGUVOD8=")) + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PwcqKmwVNFo="));
         return Uri.parse(String.format(URI, SERVER_INFO));
      }

      public static Uri getContentUri(String packageName, long id, boolean notify) {
         return Uri.parse(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ACGwFNCZmVgU1Oi5SVg==")) + ScorpionProvider.getAUTHORITY(packageName) + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")) + SERVER_INFO + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")) + id + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Py5SVg==")) + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4ALGUVOD8=")) + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PwhSVg==")) + notify);
      }

      public static String onCreateTable() {
         HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhgACGMwFit9AQo/JBciOG8zBSh+N1RF")));
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JisMWGMYMBVLHwoRJztbWn4wAitsNCQgKS4YKmIwDSN/H1kiLzpXBX02MFRgNShADRYiE2cITR9iHDM3JCwuGXoKLCthNzw/IzscKn42BhVmHC8dKT4uCGYwLDV9Hlk7IzpXXWMIRQp1V11F"));
      }
   }

   public interface BaseDataColumns extends BaseColumns {
   }

   public abstract static class Base {
   }
}
