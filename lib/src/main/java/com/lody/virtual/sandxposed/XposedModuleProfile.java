package com.lody.virtual.sandxposed;

import android.content.SharedPreferences;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;

public class XposedModuleProfile {
   private static SharedPreferences config = VirtualCore.get().getContext().getSharedPreferences(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KBc6D28zNCxsJCg1Kj0+MWkFSFo=")), 0);

   public static void enbaleXposed(boolean enbale) {
      config.edit().putBoolean(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KBc6D28zNCxsJDA2LwcuCGkjSFo=")), enbale).commit();
   }

   public static boolean isXposedEnable() {
      return config.getBoolean(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KBc6D28zNCxsJDA2LwcuCGkjSFo=")), true);
   }

   public static void enableModule(String pkg, boolean enable) {
      config.edit().putBoolean(pkg, enable).apply();
   }

   public static boolean isModuleEnable(String pkg) {
      return config.getBoolean(pkg, true);
   }
}
