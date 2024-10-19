package com.lody.virtual.client.hook.proxies.window.session;

import android.annotation.SuppressLint;
import android.os.Build.VERSION;
import android.view.WindowManager;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.base.StaticMethodProxy;
import com.lody.virtual.helper.utils.ArrayUtils;
import java.lang.reflect.Method;

class BaseMethodProxy extends StaticMethodProxy {
   private boolean mDrawOverlays = false;

   public BaseMethodProxy(String name) {
      super(name);
   }

   protected boolean isDrawOverlays() {
      return this.mDrawOverlays;
   }

   @SuppressLint({"SwitchIntDef"})
   public boolean beforeCall(Object who, Method method, Object... args) {
      this.mDrawOverlays = false;
      int index = ArrayUtils.indexOfFirst(args, WindowManager.LayoutParams.class);
      if (index != -1) {
         WindowManager.LayoutParams attrs = (WindowManager.LayoutParams)args[index];
         if (attrs != null) {
            attrs.packageName = getHostPkg();
            switch (attrs.type) {
               case 2002:
               case 2003:
               case 2006:
               case 2007:
               case 2010:
               case 2038:
                  this.mDrawOverlays = true;
               default:
                  if (VERSION.SDK_INT >= 26 && VirtualCore.get().getTargetSdkVersion() >= 26 && this.mDrawOverlays) {
                     attrs.type = 2038;
                  }
            }
         }
      }

      return true;
   }
}
