package com.lody.virtual.server.pm.installer;

import android.annotation.TargetApi;
import com.lody.virtual.StringFog;

@TargetApi(21)
public class PackageHelper {
   public static final int INSTALL_SUCCEEDED = 1;
   public static final int INSTALL_FAILED_ALREADY_EXISTS = -1;
   public static final int INSTALL_FAILED_INVALID_APK = -2;
   public static final int INSTALL_FAILED_INVALID_URI = -3;
   public static final int INSTALL_FAILED_INSUFFICIENT_STORAGE = -4;
   public static final int INSTALL_FAILED_DUPLICATE_PACKAGE = -5;
   public static final int INSTALL_FAILED_NO_SHARED_USER = -6;
   public static final int INSTALL_FAILED_UPDATE_INCOMPATIBLE = -7;
   public static final int INSTALL_FAILED_SHARED_USER_INCOMPATIBLE = -8;
   public static final int INSTALL_FAILED_MISSING_SHARED_LIBRARY = -9;
   public static final int INSTALL_FAILED_REPLACE_COULDNT_DELETE = -10;
   public static final int INSTALL_FAILED_DEXOPT = -11;
   public static final int INSTALL_FAILED_OLDER_SDK = -12;
   public static final int INSTALL_FAILED_CONFLICTING_PROVIDER = -13;
   public static final int INSTALL_FAILED_NEWER_SDK = -14;
   public static final int INSTALL_FAILED_TEST_ONLY = -15;
   public static final int INSTALL_FAILED_CPU_ABI_INCOMPATIBLE = -16;
   public static final int INSTALL_FAILED_MISSING_FEATURE = -17;
   public static final int INSTALL_FAILED_CONTAINER_ERROR = -18;
   public static final int INSTALL_FAILED_INVALID_INSTALL_LOCATION = -19;
   public static final int INSTALL_FAILED_MEDIA_UNAVAILABLE = -20;
   public static final int INSTALL_FAILED_VERIFICATION_TIMEOUT = -21;
   public static final int INSTALL_FAILED_VERIFICATION_FAILURE = -22;
   public static final int INSTALL_FAILED_PACKAGE_CHANGED = -23;
   public static final int INSTALL_FAILED_UID_CHANGED = -24;
   public static final int INSTALL_FAILED_VERSION_DOWNGRADE = -25;
   public static final int INSTALL_FAILED_PERMISSION_MODEL_DOWNGRADE = -26;
   public static final int INSTALL_PARSE_FAILED_NOT_APK = -100;
   public static final int INSTALL_PARSE_FAILED_BAD_MANIFEST = -101;
   public static final int INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION = -102;
   public static final int INSTALL_PARSE_FAILED_NO_CERTIFICATES = -103;
   public static final int INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES = -104;
   public static final int INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING = -105;
   public static final int INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME = -106;
   public static final int INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID = -107;
   public static final int INSTALL_PARSE_FAILED_MANIFEST_MALFORMED = -108;
   public static final int INSTALL_PARSE_FAILED_MANIFEST_EMPTY = -109;
   public static final int INSTALL_FAILED_INTERNAL_ERROR = -110;
   public static final int INSTALL_FAILED_USER_RESTRICTED = -111;
   public static final int INSTALL_FAILED_DUPLICATE_PERMISSION = -112;
   public static final int INSTALL_FAILED_NO_MATCHING_ABIS = -113;
   public static final int NO_NATIVE_LIBRARIES = -114;
   public static final int INSTALL_FAILED_ABORTED = -115;
   public static final int INSTALL_FAILED_EPHEMERAL_INVALID = -116;
   public static final int DELETE_SUCCEEDED = 1;
   public static final int DELETE_FAILED_INTERNAL_ERROR = -1;
   public static final int DELETE_FAILED_DEVICE_POLICY_MANAGER = -2;
   public static final int DELETE_FAILED_USER_RESTRICTED = -3;
   public static final int DELETE_FAILED_OWNER_BLOCKED = -4;
   public static final int DELETE_FAILED_ABORTED = -5;
   public static final int MOVE_SUCCEEDED = -100;
   public static final int MOVE_FAILED_INSUFFICIENT_STORAGE = -1;
   public static final int MOVE_FAILED_DOESNT_EXIST = -2;
   public static final int MOVE_FAILED_SYSTEM_PACKAGE = -3;
   public static final int MOVE_FAILED_FORWARD_LOCKED = -4;
   public static final int MOVE_FAILED_INVALID_LOCATION = -5;
   public static final int MOVE_FAILED_INTERNAL_ERROR = -6;
   public static final int MOVE_FAILED_OPERATION_PENDING = -7;
   public static final int MOVE_FAILED_DEVICE_ADMIN = -8;

   public static String installStatusToString(int status, String msg) {
      String str = installStatusToString(status);
      return msg != null ? str + ": " + msg : str;
   }

   public static String installStatusToString(int status) {
      switch (status) {
         case -115:
            return "INSTALL_FAILED_ABORTED";
         case -114:
         case -99:
         case -98:
         case -97:
         case -96:
         case -95:
         case -94:
         case -93:
         case -92:
         case -91:
         case -90:
         case -89:
         case -88:
         case -87:
         case -86:
         case -85:
         case -84:
         case -83:
         case -82:
         case -81:
         case -80:
         case -79:
         case -78:
         case -77:
         case -76:
         case -75:
         case -74:
         case -73:
         case -72:
         case -71:
         case -70:
         case -69:
         case -68:
         case -67:
         case -66:
         case -65:
         case -64:
         case -63:
         case -62:
         case -61:
         case -60:
         case -59:
         case -58:
         case -57:
         case -56:
         case -55:
         case -54:
         case -53:
         case -52:
         case -51:
         case -50:
         case -49:
         case -48:
         case -47:
         case -46:
         case -45:
         case -44:
         case -43:
         case -42:
         case -41:
         case -40:
         case -39:
         case -38:
         case -37:
         case -36:
         case -35:
         case -34:
         case -33:
         case -32:
         case -31:
         case -30:
         case -29:
         case -28:
         case -27:
         case -26:
         case 0:
         default:
            return Integer.toString(status);
         case -113:
            return "INSTALL_FAILED_NO_MATCHING_ABIS";
         case -112:
            return "INSTALL_FAILED_DUPLICATE_PERMISSION";
         case -111:
            return "INSTALL_FAILED_USER_RESTRICTED";
         case -110:
            return "INSTALL_FAILED_INTERNAL_ERROR";
         case -109:
            return "INSTALL_PARSE_FAILED_MANIFEST_EMPTY";
         case -108:
            return "INSTALL_PARSE_FAILED_MANIFEST_MALFORMED";
         case -107:
            return "INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID";
         case -106:
            return "INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME";
         case -105:
            return "INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING";
         case -104:
            return "INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES";
         case -103:
            return "INSTALL_PARSE_FAILED_NO_CERTIFICATES";
         case -102:
            return "INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION";
         case -101:
            return "INSTALL_PARSE_FAILED_BAD_MANIFEST";
         case -100:
            return "INSTALL_PARSE_FAILED_NOT_APK";
         case -25:
            return "INSTALL_FAILED_VERSION_DOWNGRADE";
         case -24:
            return "INSTALL_FAILED_UID_CHANGED";
         case -23:
            return "INSTALL_FAILED_PACKAGE_CHANGED";
         case -22:
            return "INSTALL_FAILED_VERIFICATION_FAILURE";
         case -21:
            return "INSTALL_FAILED_VERIFICATION_TIMEOUT";
         case -20:
            return "INSTALL_FAILED_MEDIA_UNAVAILABLE";
         case -19:
            return "INSTALL_FAILED_INVALID_INSTALL_LOCATION";
         case -18:
            return "INSTALL_FAILED_CONTAINER_ERROR";
         case -17:
            return "INSTALL_FAILED_MISSING_FEATURE";
         case -16:
            return "INSTALL_FAILED_CPU_ABI_INCOMPATIBLE";
         case -15:
            return "INSTALL_FAILED_TEST_ONLY";
         case -14:
            return "INSTALL_FAILED_NEWER_SDK";
         case -13:
            return "INSTALL_FAILED_CONFLICTING_PROVIDER";
         case -12:
            return "INSTALL_FAILED_OLDER_SDK";
         case -11:
            return "INSTALL_FAILED_DEXOPT";
         case -10:
            return "INSTALL_FAILED_REPLACE_COULDNT_DELETE";
         case -9:
            return "INSTALL_FAILED_MISSING_SHARED_LIBRARY";
         case -8:
            return "INSTALL_FAILED_SHARED_USER_INCOMPATIBLE";
         case -7:
            return "INSTALL_FAILED_UPDATE_INCOMPATIBLE";
         case -6:
            return "INSTALL_FAILED_NO_SHARED_USER";
         case -5:
            return "INSTALL_FAILED_DUPLICATE_PACKAGE";
         case -4:
            return "INSTALL_FAILED_INSUFFICIENT_STORAGE";
         case -3:
            return "INSTALL_FAILED_INVALID_URI";
         case -2:
            return "INSTALL_FAILED_INVALID_APK";
         case -1:
            return "INSTALL_FAILED_ALREADY_EXISTS";
         case 1:
            return "INSTALL_SUCCEEDED";
      }
   }

   public static int installStatusToPublicStatus(int status) {
      switch (status) {
         case -115:
            return 3;
         case -114:
         case -99:
         case -98:
         case -97:
         case -96:
         case -95:
         case -94:
         case -93:
         case -92:
         case -91:
         case -90:
         case -89:
         case -88:
         case -87:
         case -86:
         case -85:
         case -84:
         case -83:
         case -82:
         case -81:
         case -80:
         case -79:
         case -78:
         case -77:
         case -76:
         case -75:
         case -74:
         case -73:
         case -72:
         case -71:
         case -70:
         case -69:
         case -68:
         case -67:
         case -66:
         case -65:
         case -64:
         case -63:
         case -62:
         case -61:
         case -60:
         case -59:
         case -58:
         case -57:
         case -56:
         case -55:
         case -54:
         case -53:
         case -52:
         case -51:
         case -50:
         case -49:
         case -48:
         case -47:
         case -46:
         case -45:
         case -44:
         case -43:
         case -42:
         case -41:
         case -40:
         case -39:
         case -38:
         case -37:
         case -36:
         case -35:
         case -34:
         case -33:
         case -32:
         case -31:
         case -30:
         case -29:
         case -28:
         case -27:
         case 0:
         default:
            return 1;
         case -113:
            return 7;
         case -112:
            return 5;
         case -111:
            return 7;
         case -110:
            return 1;
         case -109:
            return 4;
         case -108:
            return 4;
         case -107:
            return 4;
         case -106:
            return 4;
         case -105:
            return 4;
         case -104:
            return 4;
         case -103:
            return 4;
         case -102:
            return 4;
         case -101:
            return 4;
         case -100:
            return 4;
         case -26:
            return 4;
         case -25:
            return 4;
         case -24:
            return 4;
         case -23:
            return 4;
         case -22:
            return 3;
         case -21:
            return 3;
         case -20:
            return 6;
         case -19:
            return 6;
         case -18:
            return 6;
         case -17:
            return 7;
         case -16:
            return 7;
         case -15:
            return 4;
         case -14:
            return 7;
         case -13:
            return 5;
         case -12:
            return 7;
         case -11:
            return 4;
         case -10:
            return 5;
         case -9:
            return 7;
         case -8:
            return 5;
         case -7:
            return 5;
         case -6:
            return 5;
         case -5:
            return 5;
         case -4:
            return 6;
         case -3:
            return 4;
         case -2:
            return 4;
         case -1:
            return 5;
         case 1:
            return 0;
      }
   }

   public static String deleteStatusToString(boolean status) {
      return status ? "DELETE_SUCCEEDED" : "DELETE_FAILED";
   }
}
