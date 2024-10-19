package com.carlos.common;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import androidx.annotation.RequiresApi;
import com.carlos.common.device.ChannelConfig;
import com.carlos.common.reverse.ares.Ares;
import com.carlos.common.reverse.dingding.DingTalk;
import com.carlos.common.reverse.epicgames.EpicGames;
import com.carlos.common.reverse.grame.BallPool;
import com.carlos.common.reverse.grame.Grame;
import com.carlos.common.reverse.wechat.WeChat;
import com.carlos.common.reverse.xhs.XHSHook;
import com.carlos.common.utils.SysUtils;
import com.carlos.libcommon.StringFog;
import com.kook.common.utils.HVLog;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.core.AppCallback;
import com.lody.virtual.helper.utils.VLog;
import com.swift.sandhook.xposedcompat.utils.ProcessUtils;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class AppComponentDelegate implements AppCallback {
   private static final String TAG = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jgc6KGMzGiNhHh42KAcYLmEzGiRrASAsKgguVg=="));
   public Application mApplication;
   boolean isMainProcess = false;

   @RequiresApi(
      api = 21
   )
   public AppComponentDelegate(Context context) {
   }

   private void registerDevice(Context context) {
   }

   private void getAccountPublicKey(Context context) {
   }

   public void setMainProcess(Context context, boolean mainProcess) {
      this.isMainProcess = mainProcess;
      if (this.isMainProcess) {
         this.registerDevice(context);
         ChannelConfig channelConfig = ChannelConfig.getInstance();
         channelConfig.getDevicesAction(context);
      }

   }

   public void beforeStartApplication(String packageName, String processName, Context context) {
   }

   public void beforeApplicationCreate(String packageName, String processName, Application application) {
      if (VERSION.SDK_INT >= 21) {
      }

      if (packageName.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojQTdjJCA1KC0iD2kgDSZoDgogKT5SVg==")))) {
         HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("MwQHDXpSHSNOCl05Ki1WDmwFQSFoAQYuLRgIJ2EkRSRlNAo8OSolL3UJHQF6VgE8CANbVg==")));
         ClassLoader classLoader = VClient.get().getClassLoader();
         Ares.hook(classLoader, application, packageName, processName);
      }

   }

   public void afterActivityOnCreate(Activity activity) {
   }

   public void beforeActivityOnStart(Activity activity) {
   }

   public void afterActivityOnStart(Activity activity) {
   }

   public void beforeActivityOnResume(Activity activity) {
   }

   public void afterActivityOnResume(Activity activity) {
   }

   public void beforeActivityOnStop(Activity activity) {
   }

   public void afterActivityOnStop(Activity activity) {
   }

   public void beforeActivityOnDestroy(Activity activity) {
   }

   public void afterActivityOnDestroy(Activity activity) {
   }

   public void afterApplicationCreate(String packageName, String processName, Application application) {
      ClassLoader classLoader;
      if (packageName.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojJCRjDiw7Lz0hDm4jMCxsNwYaLgQcCGMKQSBqEVRF")))) {
         VLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("ITw9DQ==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxsBEkZaOQtLHho1Ki0DOkcGExFBX1oCACAVUlgUQiR5Vh0dPCk1JHhSIFo=")) + SysUtils.getCurrentProcessName());
         classLoader = VClient.get().getClassLoader();
         DingTalk.hook(classLoader);
      }

      if (packageName.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojNC1ONCA2KBguDWwjASZgAQIaKQg+M2ojOCtsJB4u")))) {
         classLoader = VClient.get().getClassLoader();
      }

      if (packageName.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXogPCtgDiAwKAMYD2wgRSM=")))) {
         classLoader = VClient.get().getClassLoader();
         Grame.hook(classLoader);
      }

      if (packageName.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojBitnHh42Oj0AMWU0RVo="))) && StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4+DW8wNCZiJ1RF")).equals(Build.BRAND) && VERSION.SDK_INT == 29) {
         classLoader = VClient.get().getClassLoader();
      }

      if (packageName.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojEi9gNAY5KhccKn8VGi9rJ1k/LS4+KGAVOCpsJF1F")))) {
         classLoader = VClient.get().getClassLoader();
         BallPool.hook(classLoader, application);
      }

      if (packageName.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXogRS9gNDgzKjkYImwwAlo=")))) {
         classLoader = VClient.get().getClassLoader();
         XHSHook.hook(classLoader, application);
      }

      if (packageName.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXogMCtgNCg/Kj41Dm8jPFo=")))) {
         classLoader = VClient.get().getClassLoader();
         WeChat.hook(classLoader, application);
      }

      if (packageName.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojLCVgDSwvIy0ADW8zBi9lNyA6PC1XJ2AwAilvDicdKC4qIGwzGgVoDRoyJi0YLmwaEQJsDlg5Iz4AKmoVJCQ=")))) {
         classLoader = VClient.get().getClassLoader();
         EpicGames.hook(classLoader, application);
      }

   }

   public void beforeActivityOnCreate(Activity activity) {
   }

   private XC_LoadPackage.LoadPackageParam getLoadPackageParam(Application application) {
      XC_LoadPackage.LoadPackageParam packageParam = new XC_LoadPackage.LoadPackageParam(XposedBridge.sLoadedPackageCallbacks);
      if (application != null) {
         if (packageParam.packageName == null) {
            packageParam.packageName = application.getPackageName();
         }

         if (packageParam.processName == null) {
            packageParam.processName = ProcessUtils.getProcessName(application);
         }

         if (packageParam.classLoader == null) {
            packageParam.classLoader = application.getClassLoader();
         }

         if (packageParam.appInfo == null) {
            packageParam.appInfo = application.getApplicationInfo();
         }
      }

      return packageParam;
   }
}
