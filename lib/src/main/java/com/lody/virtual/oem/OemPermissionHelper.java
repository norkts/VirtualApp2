package com.lody.virtual.oem;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import com.lody.virtual.StringFog;
import com.lody.virtual.helper.compat.BuildCompat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class OemPermissionHelper {
   private static List<ComponentName> EMUI_AUTO_START_COMPONENTS = Arrays.asList(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity"), new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.bootstart.BootStartActivity"), new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity"), new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupAwakedAppListActivity"));
   private static List<ComponentName> FLYME_AUTO_START_COMPONENTS = Arrays.asList(new ComponentName("com.meizu.safe", "com.meizu.safe.permission.SmartBGActivity"), new ComponentName("com.meizu.safe", "com.meizu.safe.security.HomeActivity"));
   private static List<ComponentName> VIVO_AUTO_START_COMPONENTS = Arrays.asList(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager"), new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.PurviewTabActivity"), new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.SoftPermissionDetailActivity"), new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.PurviewActivity"));

   public static Intent getPermissionActivityIntent(Context context) {
      BuildCompat.ROMType romType = BuildCompat.getROMType();
      Intent intent;
      ComponentName component;
      Intent intent2;
      Iterator var5;
      switch (romType) {
         case EMUI:
            var5 = EMUI_AUTO_START_COMPONENTS.iterator();

            do {
               if (!var5.hasNext()) {
                  return null;
               }

               component = (ComponentName)var5.next();
               intent = new Intent();
               intent.addFlags(268435456);
               intent.setComponent(component);
            } while(!verifyIntent(context, intent));

            return intent;
         case MIUI:
            intent = new Intent();
            intent.addFlags(268435456);
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity");
            if (verifyIntent(context, intent)) {
               return intent;
            }
            break;
         case FLYME:
            var5 = FLYME_AUTO_START_COMPONENTS.iterator();

            do {
               if (!var5.hasNext()) {
                  return null;
               }

               component = (ComponentName)var5.next();
               intent = new Intent();
               intent.addFlags(268435456);
               intent.setComponent(component);
            } while(!verifyIntent(context, intent));

            return intent;
         case COLOR_OS:
            intent = new Intent();
            intent.addFlags(268435456);
            intent.setClassName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity");
            if (verifyIntent(context, intent)) {
               return intent;
            }
            break;
         case LETV:
            intent = new Intent();
            intent.addFlags(268435456);
            intent.setClassName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity");
            if (verifyIntent(context, intent)) {
               return intent;
            }
            break;
         case VIVO:
            var5 = VIVO_AUTO_START_COMPONENTS.iterator();

            do {
               if (!var5.hasNext()) {
                  return null;
               }

               component = (ComponentName)var5.next();
               intent = new Intent();
               intent.addFlags(268435456);
               intent.setComponent(component);
            } while(!verifyIntent(context, intent));

            return intent;
         case _360:
            intent = new Intent();
            intent.addFlags(268435456);
            intent.setClassName("com.qihoo360.mobilesafe", "com.qihoo360.mobilesafe.ui.index.AppEnterActivity");
            if (verifyIntent(context, intent)) {
               return intent;
            }
      }

      return null;
   }

   private static boolean verifyIntent(Context context, Intent intent) {
      ResolveInfo info = context.getPackageManager().resolveActivity(intent, 0);
      return info != null && info.activityInfo != null && info.activityInfo.exported;
   }
}
