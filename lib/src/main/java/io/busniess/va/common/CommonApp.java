package io.busniess.va.common;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import com.kook.librelease.R.layout;
import com.lody.virtual.client.core.SettingConfig;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.Keep;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.VLog;
import io.busniess.va.delegate.MyAppRequestListener;
import io.busniess.va.delegate.MyTaskDescDelegate;
import jonathanfinerty.once.Once;

@Keep
public class CommonApp {
   private SettingConfig mConfig = new SettingConfig() {
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
   };
   private BroadcastReceiver mGmsInitializeReceiver = new BroadcastReceiver() {
      public void onReceive(Context context, Intent intent) {
         View view = LayoutInflater.from(context).inflate(layout.content_gms_init, (ViewGroup)null);
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

   public void attachBaseContext(Application application, Context base) {
      VLog.OPEN_LOG = true;

      try {
         VirtualCore.get().startup(application, base, this.mConfig);
      } catch (Throwable var4) {
         Throwable e = var4;
         e.printStackTrace();
      }

   }

   public void onCreate(final Application application) {
      final VirtualCore virtualCore = VirtualCore.get();
      virtualCore.initialize(new VirtualCore.VirtualInitializer() {
         public void onMainProcess() {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
            Once.initialise(application);
            application.registerReceiver(CommonApp.this.mGmsInitializeReceiver, new IntentFilter("android.intent.action.GMS_INITIALIZED"));
         }

         @RequiresApi(
            api = 17
         )
         public void onVirtualProcess() {
            virtualCore.setTaskDescriptionDelegate(new MyTaskDescDelegate());
            virtualCore.setAppRequestListener(new MyAppRequestListener(application));
         }

         public void onServerProcess() {
         }
      });
   }
}
