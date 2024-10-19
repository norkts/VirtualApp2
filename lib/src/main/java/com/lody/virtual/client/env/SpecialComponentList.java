package com.lody.virtual.client.env;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.ArraySet;
import androidx.annotation.RequiresApi;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.os.VEnvironment;
import com.lody.virtual.remote.BroadcastIntentData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import mirror.android.content.IntentFilterU;

public final class SpecialComponentList {
   private static final List<ComponentName> GMS_BLOCK_COMPONENT = Arrays.asList(new ComponentName("com.google.android.gms", "com.google.android.gms.update.SystemUpdateService"), new ComponentName("com.google.android.gsf", "com.google.android.gsf.update.SystemUpdateService"));
   private static final List<String> GMS_BLOCK_ACTION_LIST = Arrays.asList("com.google.android.gms.update.START_SERVICE");
   private static final List<String> ACTION_BLACK_LIST = new ArrayList(2);
   private static final Map<String, String> PROTECTED_ACTION_MAP = new HashMap(5);
   private static final HashSet<String> WHITE_PERMISSION = new HashSet(3);
   private static final HashSet<String> BROADCAST_START_WHITE_LIST = new HashSet();
   private static final HashSet<String> INSTRUMENTATION_CONFLICTING = new HashSet(2);
   private static final HashSet<String> SPEC_SYSTEM_APP_LIST = new HashSet(3);
   public static final Set<String> SYSTEM_BROADCAST_ACTION = new HashSet(7);
   private static final Set<String> PRE_INSTALL_PACKAGES = new HashSet(7);
   private static final String PROTECT_ACTION_PREFIX = "_VA_protected_";

   public static Set<String> getPreInstallPackages() {
      return PRE_INSTALL_PACKAGES;
   }

   public static void addStaticBroadCastWhiteList(String pkg) {
      BROADCAST_START_WHITE_LIST.add(pkg);
   }

   public static boolean isSpecSystemPackage(String pkg) {
      return SPEC_SYSTEM_APP_LIST.contains(pkg);
   }

   public static boolean isConflictingInstrumentation(String packageName) {
      return INSTRUMENTATION_CONFLICTING.contains(packageName);
   }

   public static boolean shouldBlockIntent(Intent intent) {
      ComponentName component = intent.getComponent();
      if (component != null && GMS_BLOCK_COMPONENT.contains(component)) {
         return true;
      } else {
         String action = intent.getAction();
         return action != null && GMS_BLOCK_ACTION_LIST.contains(action);
      }
   }

   public static boolean isActionInBlackList(String action) {
      return ACTION_BLACK_LIST.contains(action);
   }

   public static void addBlackAction(String action) {
      ACTION_BLACK_LIST.add(action);
   }

   @RequiresApi(
      api = 23
   )
   public static void protectIntentFilter(IntentFilter filter) {
      Collection<String> collection = new ArraySet();
      if (filter != null) {
         if (BuildCompat.isUpsideDownCake()) {
            try {
               collection = (Collection)IntentFilterU.mActions.get(filter);
            } catch (Exception var6) {
            }
         } else {
            collection = (Collection)mirror.android.content.IntentFilter.mActions.get(filter);
         }

         Iterator<String> iterator = ((Collection)collection).iterator();
         ArrayList<String> list = new ArrayList();

         while(iterator.hasNext()) {
            String action = (String)iterator.next();
            if (isActionInBlackList(action)) {
               iterator.remove();
            } else {
               String newAction = protectAction(action);
               if (newAction != null) {
                  iterator.remove();
                  list.add(newAction);
               }
            }
         }

         ((Collection)collection).addAll(list);
      }

   }

   public static void protectIntent(Intent intent) {
      String protectAction = protectAction(intent.getAction());
      if (protectAction != null) {
         intent.setAction(protectAction);
      }

   }

   public static Intent unprotectIntent(int userId, Intent intent) {
      BroadcastIntentData intentData = new BroadcastIntentData(intent);
      if (intentData.intent != null && (intentData.userId == -1 || intentData.userId == userId)) {
         intent = intentData.intent;
         String unprotectAction = unprotectAction(intent.getAction());
         if (unprotectAction != null) {
            intent.setAction(unprotectAction);
         }
      }

      return intent;
   }

   public static String getProtectActionPrefix() {
      return PROTECT_ACTION_PREFIX;
   }

   public static String protectAction(String originAction) {
      if (originAction == null) {
         return null;
      } else if (VirtualCore.getConfig().isUnProtectAction(originAction)) {
         return originAction;
      } else if (originAction.startsWith(getProtectActionPrefix())) {
         return originAction;
      } else {
         String newAction = (String)PROTECTED_ACTION_MAP.get(originAction);
         if (newAction == null) {
            newAction = getProtectActionPrefix() + originAction;
         }

         return newAction;
      }
   }

   public static String unprotectAction(String action) {
      if (action == null) {
         return null;
      } else if (action.startsWith(getProtectActionPrefix())) {
         return action.substring(getProtectActionPrefix().length());
      } else {
         Iterator var1 = PROTECTED_ACTION_MAP.entrySet().iterator();

         Map.Entry next;
         String modifiedAction;
         do {
            if (!var1.hasNext()) {
               return null;
            }

            next = (Map.Entry)var1.next();
            modifiedAction = (String)next.getValue();
         } while(!modifiedAction.equals(action));

         return (String)next.getKey();
      }
   }

   public static boolean isWhitePermission(String permission) {
      return WHITE_PERMISSION.contains(permission);
   }

   public static boolean allowedStartFromBroadcast(String str) {
      return BROADCAST_START_WHITE_LIST.contains(str);
   }

   static {
      SYSTEM_BROADCAST_ACTION.add("android.intent.action.SCREEN_ON");
      SYSTEM_BROADCAST_ACTION.add("android.intent.action.SCREEN_OFF");
      SYSTEM_BROADCAST_ACTION.add("android.intent.action.NEW_OUTGOING_CALL");
      SYSTEM_BROADCAST_ACTION.add("android.intent.action.TIME_TICK");
      SYSTEM_BROADCAST_ACTION.add("android.intent.action.TIME_SET");
      SYSTEM_BROADCAST_ACTION.add("android.intent.action.TIMEZONE_CHANGED");
      SYSTEM_BROADCAST_ACTION.add("android.intent.action.BATTERY_CHANGED");
      SYSTEM_BROADCAST_ACTION.add("android.intent.action.BATTERY_LOW");
      SYSTEM_BROADCAST_ACTION.add("android.intent.action.BATTERY_OKAY");
      SYSTEM_BROADCAST_ACTION.add("android.intent.action.ACTION_POWER_CONNECTED");
      SYSTEM_BROADCAST_ACTION.add("android.intent.action.ACTION_POWER_DISCONNECTED");
      SYSTEM_BROADCAST_ACTION.add("android.intent.action.USER_PRESENT");
      SYSTEM_BROADCAST_ACTION.add("android.provider.Telephony.SMS_RECEIVED");
      SYSTEM_BROADCAST_ACTION.add("android.provider.Telephony.SMS_DELIVER");
      SYSTEM_BROADCAST_ACTION.add("android.net.wifi.STATE_CHANGE");
      SYSTEM_BROADCAST_ACTION.add("android.net.wifi.SCAN_RESULTS");
      SYSTEM_BROADCAST_ACTION.add("android.net.wifi.WIFI_STATE_CHANGED");
      SYSTEM_BROADCAST_ACTION.add("android.net.conn.CONNECTIVITY_CHANGE");
      SYSTEM_BROADCAST_ACTION.add("android.intent.action.ANY_DATA_STATE");
      SYSTEM_BROADCAST_ACTION.add("android.intent.action.SIM_STATE_CHANGED");
      SYSTEM_BROADCAST_ACTION.add("android.location.PROVIDERS_CHANGED");
      SYSTEM_BROADCAST_ACTION.add("android.location.MODE_CHANGED");
      SYSTEM_BROADCAST_ACTION.add("android.intent.action.HEADSET_PLUG");
      SYSTEM_BROADCAST_ACTION.add("android.media.VOLUME_CHANGED_ACTION");
      SYSTEM_BROADCAST_ACTION.add("android.intent.action.CONFIGURATION_CHANGED");
      SYSTEM_BROADCAST_ACTION.add("android.intent.action.DYNAMIC_SENSOR_CHANGED");
      SYSTEM_BROADCAST_ACTION.add("dynamic_sensor_change");
      ACTION_BLACK_LIST.add("android.appwidget.action.APPWIDGET_UPDATE");
      ACTION_BLACK_LIST.add("android.appwidget.action.APPWIDGET_CONFIGURE");
      WHITE_PERMISSION.add("com.google.android.gms.settings.SECURITY_SETTINGS");
      WHITE_PERMISSION.add("com.google.android.apps.plus.PRIVACY_SETTINGS");
      WHITE_PERMISSION.add("android.permission.ACCOUNT_MANAGER");
      PROTECTED_ACTION_MAP.put("android.intent.action.PACKAGE_ADDED", "virtual.android.intent.action.PACKAGE_ADDED");
      PROTECTED_ACTION_MAP.put("android.intent.action.PACKAGE_REMOVED", "virtual.android.intent.action.PACKAGE_REMOVED");
      PROTECTED_ACTION_MAP.put("android.intent.action.PACKAGE_CHANGED", "virtual.android.intent.action.PACKAGE_CHANGED");
      PROTECTED_ACTION_MAP.put("android.intent.action.USER_ADDED", "virtual.android.intent.action.USER_ADDED");
      PROTECTED_ACTION_MAP.put("android.intent.action.USER_REMOVED", "virtual.android.intent.action.USER_REMOVED");
      PROTECTED_ACTION_MAP.put("android.intent.action.MEDIA_SCANNER_SCAN_FILE", "android.intent.action.MEDIA_SCANNER_SCAN_FILE");
      INSTRUMENTATION_CONFLICTING.add("com.qihoo.magic");
      INSTRUMENTATION_CONFLICTING.add("com.qihoo.magic_mutiple");
      INSTRUMENTATION_CONFLICTING.add("com.facebook.katana");
      SPEC_SYSTEM_APP_LIST.add("android");
      SPEC_SYSTEM_APP_LIST.add("com.google.android.webview");
      SPEC_SYSTEM_APP_LIST.add("com.android.providers.downloads");
      SPEC_SYSTEM_APP_LIST.add("FelipeLeite.Sober.appicon");
      PRE_INSTALL_PACKAGES.add("com.huawei.hwid");
      PRE_INSTALL_PACKAGES.add("com.vivo.sdkplugin");
      PRE_INSTALL_PACKAGES.add("com.xiaomi.gamecenter.sdk.service");
      if (VEnvironment.enableMediaRedirect()) {
      }

   }
}
