package com.lody.virtual.server.notification;

import android.app.Notification;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build.VERSION;
import android.widget.RemoteViews;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import mirror.com.android.internal.R_Hide;

public abstract class NotificationCompat {
   public static final String EXTRA_TITLE = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kgKQg2CGkjSFo="));
   public static final String EXTRA_TITLE_BIG = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kgKQg2CGknMCpqASBF"));
   public static final String EXTRA_TEXT = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kgKAgALg=="));
   public static final String EXTRA_SUB_TEXT = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kpLAcuAGkgFgY="));
   public static final String EXTRA_INFO_TEXT = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj0+DX0zGjBvEVRF"));
   public static final String EXTRA_SUMMARY_TEXT = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kpLAdXD24gRT9nESgzKghSVg=="));
   public static final String EXTRA_BIG_TEXT = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k6KQc6AGkgFgY="));
   public static final String EXTRA_PROGRESS = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1ksIz1fM2oVGgNsJ1RF"));
   public static final String EXTRA_PROGRESS_MAX = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1ksIz1fM2oVGgNsJQ4sLwhSVg=="));
   public static final String EXTRA_BUILDER_APPLICATION_INFO = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k7IxgmXm8VHiU="));
   static final String TAG = NotificationCompat.class.getSimpleName();
   static final String SYSTEM_UI_PKG = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojJCZiESw1KQc1DmoKLANvESgeKhgYVg=="));
   private final List<Integer> sSystemLayoutResIds = new ArrayList(10);
   private NotificationFixer mNotificationFixer;

   NotificationCompat() {
      this.loadSystemLayoutRes();
      this.mNotificationFixer = new NotificationFixer(this);
   }

   public static NotificationCompat create() {
      return (NotificationCompat)(VERSION.SDK_INT >= 21 ? new NotificationCompatCompatV21() : new NotificationCompatCompatV14());
   }

   private void loadSystemLayoutRes() {
      Field[] fields = R_Hide.layout.TYPE.getFields();
      Field[] var2 = fields;
      int var3 = fields.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Field field = var2[var4];
         if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())) {
            try {
               int id = field.getInt((Object)null);
               this.sSystemLayoutResIds.add(id);
            } catch (Throwable var7) {
            }
         }
      }

   }

   NotificationFixer getNotificationFixer() {
      return this.mNotificationFixer;
   }

   boolean isSystemLayout(RemoteViews remoteViews) {
      return remoteViews != null && this.sSystemLayoutResIds.contains(remoteViews.getLayoutId());
   }

   public Context getHostContext() {
      return VirtualCore.get().getContext();
   }

   PackageInfo getPackageInfo(String packageName) {
      try {
         return VirtualCore.get().getHostPackageManager().getPackageInfo(packageName, 0L);
      } catch (PackageManager.NameNotFoundException var3) {
         return null;
      }
   }

   public abstract boolean dealNotification(int var1, Notification var2, String var3);
}
