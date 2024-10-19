package com.android.dex;

public final class DexFormat {
   public static final int API_CONST_METHOD_HANDLE = 28;
   public static final int API_METHOD_HANDLES = 26;
   public static final int API_DEFINE_INTERFACE_METHODS = 24;
   public static final int API_INVOKE_INTERFACE_METHODS = 24;
   public static final int API_INVOKE_STATIC_INTERFACE_METHODS = 21;
   public static final int API_NO_EXTENDED_OPCODES = 13;
   public static final int API_CURRENT = 28;
   public static final String VERSION_FOR_API_28 = "039";
   public static final String VERSION_FOR_API_26 = "038";
   public static final String VERSION_FOR_API_24 = "037";
   public static final String VERSION_FOR_API_13 = "035";
   public static final String VERSION_CURRENT = "039";
   public static final String DEX_IN_JAR_NAME = "classes.dex";
   public static final String MAGIC_PREFIX = "dex\n";
   public static final String MAGIC_SUFFIX = "\u0000";
   public static final int ENDIAN_TAG = 305419896;
   public static final int MAX_MEMBER_IDX = 65535;
   public static final int MAX_TYPE_IDX = 65535;

   private DexFormat() {
   }

   public static int magicToApi(byte[] magic) {
      if (magic.length != 8) {
         return -1;
      } else if (magic[0] == 100 && magic[1] == 101 && magic[2] == 120 && magic[3] == 10 && magic[7] == 0) {
         String version = "" + (char)magic[4] + (char)magic[5] + (char)magic[6];
         if (version.equals("035")) {
            return 13;
         } else if (version.equals("037")) {
            return 24;
         } else if (version.equals("038")) {
            return 26;
         } else if (version.equals("039")) {
            return 28;
         } else {
            return version.equals("039") ? 28 : -1;
         }
      } else {
         return -1;
      }
   }

   public static String apiToMagic(int targetApiLevel) {
      String version;
      if (targetApiLevel >= 28) {
         version = "039";
      } else if (targetApiLevel >= 28) {
         version = "039";
      } else if (targetApiLevel >= 26) {
         version = "038";
      } else if (targetApiLevel >= 24) {
         version = "037";
      } else {
         version = "035";
      }

      return "dex\n" + version + "\u0000";
   }

   public static boolean isSupportedDexMagic(byte[] magic) {
      int api = magicToApi(magic);
      return api > 0;
   }
}
