package com.lody.virtual.helper.compat;

import com.lody.virtual.StringFog;

public class ContentResolverCompat {
   public static final String SYNC_EXTRAS_EXPECTED_UPLOAD = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQdfKGgVLAZiDgpALAgmCG8FQSw="));
   public static final String SYNC_EXTRAS_EXPECTED_DOWNLOAD = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LQdfKGgVLAZiDgpAKBdfI28VOCVoASxF"));
   public static final String SYNC_EXTRAS_PRIORITY = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0YCGs2GgJhNAY1Iz0cLmgjSFo="));
   public static final String SYNC_EXTRAS_DISALLOW_METERED = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggEDmowPB9gDjAgKAguPWkzSFo="));
   public static final int SYNC_OBSERVER_TYPE_STATUS = 8;
   public static final int SYNC_ERROR_SYNC_ALREADY_IN_PROGRESS = 1;
   public static final int SYNC_ERROR_AUTHENTICATION = 2;
   public static final int SYNC_ERROR_IO = 3;
   public static final int SYNC_ERROR_PARSE = 4;
   public static final int SYNC_ERROR_CONFLICT = 5;
   public static final int SYNC_ERROR_TOO_MANY_DELETIONS = 6;
   public static final int SYNC_ERROR_TOO_MANY_RETRIES = 7;
   public static final int SYNC_ERROR_INTERNAL = 8;
   private static final String[] SYNC_ERROR_NAMES = new String[]{StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggEKmgVJCxnCl0zKjlXKmoVNC1sNyg6KT5SVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcuLGUFNCZmHgY5Lwg2MW8FMyNrDgo5LD0MVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgfDWgaFgRgJyxF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Khg+Km8zNyNiASwqKi4uVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ACGgjHi99JwpF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRgAD3oVEjdgNxk3KBcMCGkgBi9lJxo6")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRgAD3oVEjdgNxk3Iz0MLmoVLCtsJ1RF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcLGgaFiZ9DlA3KAguKG8KRVo="))};

   public static String syncErrorToString(int error) {
      return error >= 1 && error <= SYNC_ERROR_NAMES.length ? SYNC_ERROR_NAMES[error - 1] : String.valueOf(error);
   }
}
