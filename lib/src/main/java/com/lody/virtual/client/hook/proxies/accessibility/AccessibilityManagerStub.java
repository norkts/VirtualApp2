package com.lody.virtual.client.hook.proxies.accessibility;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.StaticMethodProxy;
import java.lang.reflect.Method;
import mirror.android.view.accessibility.IAccessibilityManager;

public class AccessibilityManagerStub extends BinderInvocationProxy {
   public AccessibilityManagerStub() {
      super(IAccessibilityManager.Stub.TYPE, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWgaLANjDiwzKhccLmgjSFo=")));
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceLastUserIdProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggqPGMzHi9iDlkg"))));
      this.addMethodProxy(new ReplaceLastUserIdProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uCGgLJCl9JDApIy0cOGwjOC9vHh5LKi4uKmYVSFo="))));
      this.addMethodProxy(new ReplaceLastUserIdProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLH0VBgNmHiAoKhcMPmYjAilrDjA6IxgMI2AaGj9rDywuLBcEI2gjNExlHjAZ"))));
      this.addMethodProxy(new ReplaceLastUserIdProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGAVBjd9NFE/KBUiP24FGgNsJx4pIxgEI2YVGhBuASg/Ki4YJ30VAiVsAVRF"))));
      this.addMethodProxy(new ReplaceLastUserIdProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGQzAiZiHh4tJBdfCWkjMFo="))));
      this.addMethodProxy(new ReplaceLastUserIdProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcLGgaFgRmASQg"))));
      this.addMethodProxy(new ReplaceLastUserIdProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggqPGMVLCliASgpKQcuMW8zLAZuDx4bKgguCH0KND9vDlkdIAguKmUzNDVsAR46Jj5SVg=="))));
   }

   private static class ReplaceLastUserIdProxy extends StaticMethodProxy {
      public ReplaceLastUserIdProxy(String name) {
         super(name);
      }

      public boolean beforeCall(Object who, Method method, Object... args) {
         int index = args.length - 1;
         if (index >= 0 && args[index] instanceof Integer) {
            args[index] = getRealUserId();
         }

         return super.beforeCall(who, method, args);
      }
   }
}
