package com.lody.virtual.helper.utils;

import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import com.lody.virtual.StringFog;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

public class OSUtils {
   private static final String KEY_EMUI_VERSION_CODE = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4fCGsgNC9gHg02LD0MKGoFLCVlMxogLBcuIw=="));
   private static final String KEY_MIUI_VERSION_CODE = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4fCGoVAgVjClkvKQMYLGkgRQNqAQYbPC42KWIaLFo="));
   private static final String KEY_MIUI_VERSION_NAME = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4fCGoVAgVjClkvKQMYLGkgRQNqAQYbPC4cO2AKLFo="));
   private static final String KEY_MIUI_INTERNAL_STORAGE = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4fCGoVAgVjClkzKj42PWoVMDdlVho6KggACH0KJCA="));
   private static final OSUtils sOSUtils = new OSUtils();
   private boolean emui;
   private boolean miui;
   private boolean flyme;
   private boolean androidQ = false;
   private String miuiVersion;

   private OSUtils() {
      Properties properties;
      try {
         properties = new Properties();
         properties.load(new FileInputStream(new File(Environment.getRootDirectory(), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj0uCWoFMyZhESw1IxhSVg==")))));
      } catch (IOException var4) {
         properties = null;
      }

      if (properties != null) {
         this.emui = !TextUtils.isEmpty(properties.getProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4fCGsgNC9gHg02LD0MKGoFLCVlMxogLBcuIw=="))));
         this.miuiVersion = properties.getProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4fCGoVAgVjClkvKQMYLGkgRQNqAQYbPC42KWIaLFo=")));
         this.miui = !TextUtils.isEmpty(this.miuiVersion) || !TextUtils.isEmpty(properties.getProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4fCGoVAgVjClkvKQMYLGkgRQNqAQYbPC4cO2AKLFo=")))) || !TextUtils.isEmpty(properties.getProperty(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4fCGoVAgVjClkzKj42PWoVMDdlVho6KggACH0KJCA="))));
      }

      this.flyme = this.hasFlyme();

      try {
         Class.forName(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k7IxglDmQhQSlvER49IxcqM24aPDZvIlEqKRhbIWsKFlo=")));
         this.androidQ = true;
      } catch (ClassNotFoundException var3) {
      }

   }

   public static OSUtils getInstance() {
      return sOSUtils;
   }

   public String getMiuiVersion() {
      return this.miuiVersion;
   }

   public boolean isEmui() {
      return this.emui;
   }

   public boolean isMiui() {
      return this.miui;
   }

   public boolean isFlyme() {
      return this.flyme;
   }

   public boolean isAndroidQ() {
      return this.androidQ;
   }

   private boolean hasFlyme() {
      try {
         Method method = Build.class.getMethod(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBg+KWczEjdhNwoQLwguVg==")));
         return method != null;
      } catch (Exception var2) {
         return false;
      }
   }
}
