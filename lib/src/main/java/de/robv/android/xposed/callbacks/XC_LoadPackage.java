package de.robv.android.xposed.callbacks;

import android.content.pm.ApplicationInfo;
import com.swift.sandhook.xposedcompat.XposedCompat;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;

public abstract class XC_LoadPackage extends XCallback implements IXposedHookLoadPackage {
   public XC_LoadPackage() {
   }

   public XC_LoadPackage(int priority) {
      super(priority);
   }

   protected void call(XCallback.Param param) throws Throwable {
      if (param instanceof LoadPackageParam) {
         this.handleLoadPackage((LoadPackageParam)param);
      }

   }

   public static final class LoadPackageParam extends XCallback.Param {
      public String packageName;
      public String processName;
      public ClassLoader classLoader;
      public ApplicationInfo appInfo;
      public boolean isFirstApplication;

      public LoadPackageParam(XposedBridge.CopyOnWriteSortedSet<XC_LoadPackage> callbacks) {
         super(callbacks);
         this.packageName = XposedCompat.packageName;
         this.processName = XposedCompat.processName;
         this.classLoader = XposedCompat.classLoader;
         this.appInfo = XposedCompat.context.getApplicationInfo();
         this.isFirstApplication = XposedCompat.isFirstApplication;
      }
   }
}
