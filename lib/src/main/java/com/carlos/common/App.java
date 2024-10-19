package com.carlos.common;

import android.app.MultiApplication;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import com.carlos.common.ui.delegate.MyAppRequestListener;
import com.carlos.common.ui.delegate.MyTaskDescDelegate;
import com.carlos.common.utils.SPTools;
import com.carlos.libcommon.StringFog;
import com.kook.librelease.R.layout;
import com.lody.virtual.client.core.AppCallback;
import com.lody.virtual.client.core.SettingConfig;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.VLog;

public class App extends MultiApplication {
   private static App gApp;
   AppComponentDelegate mAppComponentDelegate;
   public final SettingConfig mConfig = new SettingConfig() {
      public SettingConfig.FakeWifiStatus fakeWifiStatus = new SettingConfig.FakeWifiStatus();

      public String getMainPackageName() {
         return "com.carlos.multiapp";
      }

      public String getExtPackageName() {
         return "com.carlos.multiapp.ext";
      }

      public boolean isEnableIORedirect() {
         return true;
      }

      public Intent onHandleLauncherIntent(Intent originIntent) {
         Intent intent = new Intent();
         ComponentName component = new ComponentName(this.getMainPackageName(), "io.busniess.va.home.BackHomeActivity");
         intent.setComponent(component);
         intent.addFlags(268435456);
         return intent;
      }

      public boolean isUseRealDataDir(String packageName) {
         return false;
      }

      public boolean isOutsidePackage(String packageName) {
         return false;
      }

      public boolean isAllowCreateShortcut() {
         return false;
      }

      public boolean isHostIntent(Intent intent) {
         return intent.getData() != null && "market".equals(intent.getData().getScheme());
      }

      public boolean isUseRealApkPath(String packageName) {
         return packageName.equals("com.seeyon.cmp");
      }

      public boolean isEnableVirtualSdcardAndroidData() {
         return BuildCompat.isQ();
      }

      public boolean resumeInstrumentationInMakeApplication(String packageName) {
         return packageName.equals("com.ss.android.ugc.aweme.lite") ? true : super.resumeInstrumentationInMakeApplication(packageName);
      }

      public boolean isUnProtectAction(String action) {
         return action.startsWith("VA_BroadcastTest_") ? true : super.isUnProtectAction(action);
      }

      public SettingConfig.FakeWifiStatus getFakeWifiStatus(String packageName, int userId) {
         String SSID_KEY = "ssid_key" + packageName + "_" + userId;
         String MAC_KEY = "mac_key" + packageName + "_" + userId;
         String ssid = SPTools.getString(VirtualCore.get().getContext(), SSID_KEY);
         String mac = SPTools.getString(VirtualCore.get().getContext(), MAC_KEY);
         if (!TextUtils.isEmpty(ssid) && !TextUtils.isEmpty(mac)) {
            this.fakeWifiStatus.setSSID(ssid);
            this.fakeWifiStatus.setDefaultMac(mac);
            return this.fakeWifiStatus;
         } else {
            return null;
         }
      }
   };
   private BroadcastReceiver mGmsInitializeReceiver = new BroadcastReceiver() {
      public void onReceive(Context context, Intent intent) {
         View view = LayoutInflater.from(context).inflate(layout.content_gms_init_layout, (ViewGroup)null);
         Toast toast = new Toast(context);
         toast.setGravity(81, 0, 0);
         toast.setDuration(0);
         toast.setView(view);

         try {
            toast.show();
         } catch (Throwable var6) {
            Throwable e = var6;
            e.printStackTrace();
         }

      }
   };

   public static App getApp() {
      return gApp;
   }

   protected void attachBaseContext(Context base) {
      super.attachBaseContext(base);
      gApp = this;
      VLog.OPEN_LOG = true;

      try {
         VirtualCore.get().startup(this, base, this.mConfig);
      } catch (Throwable var3) {
         Throwable e = var3;
         e.printStackTrace();
      }

   }

   public void en(byte en) {
      boolean abc = en == 1;
   }

   public AppCallback getAppCallback() {
      return this.mAppComponentDelegate;
   }

   @RequiresApi(
      api = 21
   )
   public void onCreate() {
      super.onCreate();
      this.lazyInjectInit();
      if (this.mAppComponentDelegate == null) {
         this.mAppComponentDelegate = new AppComponentDelegate(this);
      }

      final VirtualCore virtualCore = VirtualCore.get();
      virtualCore.setAppCallback(this.mAppComponentDelegate);
      virtualCore.initialize(new VirtualCore.VirtualInitializer() {
         public void onMainProcess() {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
            App.this.mAppComponentDelegate.setMainProcess(App.gApp, true);
            App.this.registerReceiver(App.this.mGmsInitializeReceiver, new IntentFilter("android.intent.action.GMS_INITIALIZED"));
         }

         @RequiresApi(
            api = 17
         )
         public void onVirtualProcess() {
            App.this.mAppComponentDelegate.setMainProcess((Context)null, false);
            virtualCore.setTaskDescriptionDelegate(new MyTaskDescDelegate());
            virtualCore.setTaskDescriptionDelegate(new MyTaskDescDelegate());
            virtualCore.setAppRequestListener(new MyAppRequestListener(App.this));
         }

         public void onServerProcess() {
         }
      });
   }

   private void lazyInjectInit() {
   }
}
