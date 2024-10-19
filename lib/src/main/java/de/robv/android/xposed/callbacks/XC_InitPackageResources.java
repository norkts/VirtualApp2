package de.robv.android.xposed.callbacks;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.XposedBridge;

public abstract class XC_InitPackageResources extends XCallback implements IXposedHookInitPackageResources {
   public XC_InitPackageResources() {
   }

   public XC_InitPackageResources(int priority) {
      super(priority);
   }

   protected void call(XCallback.Param param) throws Throwable {
      if (param instanceof InitPackageResourcesParam) {
         this.handleInitPackageResources((InitPackageResourcesParam)param);
      }

   }

   public static final class InitPackageResourcesParam extends XCallback.Param {
      public String packageName;

      public InitPackageResourcesParam(XposedBridge.CopyOnWriteSortedSet<XC_InitPackageResources> callbacks) {
         super(callbacks);
      }
   }
}
