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
   private static final String TAG = "AppComponentDelegate";
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

      if (packageName.equals("com.kakaogames.ares")) {
         HVLog.d("--------com.kakaogames.ares-----------");
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
      if (packageName.equals("com.alibaba.android.rimet")) {
         VLog.d("VA-", "开始 hook 丁丁打卡 9.19   " + SysUtils.getCurrentProcessName());
         classLoader = VClient.get().getClassLoader();
         DingTalk.hook(classLoader);
      }

      if (packageName.equals("com.eg.android.AlipayGphone")) {
         classLoader = VClient.get().getClassLoader();
      }

      if (packageName.equals("com.wemade.mirm")) {
         classLoader = VClient.get().getClassLoader();
         Grame.hook(classLoader);
      }

      if (packageName.equals("com.nexon.hit2") && "samsung".equals(Build.BRAND) && VERSION.SDK_INT == 29) {
         classLoader = VClient.get().getClassLoader();
      }

      if (packageName.equals("com.miniclip.eightballpool")) {
         classLoader = VClient.get().getClassLoader();
         BallPool.hook(classLoader, application);
      }

      if (packageName.equals("com.xingin.xhs")) {
         classLoader = VClient.get().getClassLoader();
         XHSHook.hook(classLoader, application);
      }

      if (packageName.equals("com.tencent.mm")) {
         classLoader = VClient.get().getClassLoader();
         WeChat.hook(classLoader, application);
      }

      if (packageName.equals("com.com2usholdings.zenonia.android.google.kr.normal")) {
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
