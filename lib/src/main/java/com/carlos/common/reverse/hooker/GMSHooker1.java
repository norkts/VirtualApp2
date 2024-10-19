package com.carlos.common.reverse.hooker;

import android.content.Context;
import android.util.Log;
import com.carlos.libcommon.StringFog;
import com.swift.sandhook.annotation.HookMethod;
import com.swift.sandhook.annotation.HookReflectClass;
import com.swift.sandhook.annotation.MethodParams;

@HookReflectClass("com.google.android.gms.common.GooglePlayServicesUtilLight")
public class GMSHooker1 {
   @HookMethod("isGooglePlayServicesAvailable")
   @MethodParams({Context.class})
   public static boolean isGooglePlayServicesAvailable(Context v1) throws Throwable {
      Log.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+LGgaLAY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2WmozGi1gHjACKhciIWIFGgRvNx4qLhc2HWYwPCxsHiQrKT4fDQ==")));
      return true;
   }

   @HookMethod("isGooglePlayServicesAvailable")
   @MethodParams({Context.class, int.class})
   public static boolean isGooglePlayServicesAvailable2(Context v1, int v2) throws Throwable {
      Log.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+LGgaLAY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2WmozGi1gHjACKhciIWIFGgRvNx4qLhc2HWYwPCxsHiQrKT4fCA==")));
      return true;
   }

   @HookMethod("isPlayServicesPossiblyUpdating")
   @MethodParams({Context.class, int.class})
   public static boolean isGooglePlayServicesUid(Context v1, int v2) throws Throwable {
      Log.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+LGgaLAY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2AmoFJD9pJDAqLD0cP2kgAkxlJDA6IxgMKGcLLDNuHiQ9Ki4qIQ==")));
      return true;
   }

   @HookMethod("isPlayStorePossiblyUpdating")
   @MethodParams({Context.class, int.class})
   public static boolean isPlayStorePossiblyUpdating(Context v1, int v2) throws Throwable {
      Log.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+LGgaLAY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAc2AmoFJD9pJwo1Iz0MDG8KAgNqAQodLxUuDmIaPD9vDh4g")));
      return true;
   }
}
