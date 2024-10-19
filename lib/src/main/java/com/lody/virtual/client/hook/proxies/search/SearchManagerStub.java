package com.lody.virtual.client.hook.proxies.search;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.client.hook.base.StaticMethodProxy;
import java.lang.reflect.Method;
import mirror.android.app.ISearchManager;

@TargetApi(17)
public class SearchManagerStub extends BinderInvocationProxy {
   public SearchManagerStub() {
      super(ISearchManager.Stub.asInterface, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uP28jLCA=")));
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new StaticMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ixg+I2ojLCBoHjA9LwcqIWYgAgNqDjA/"))));
      this.addMethodProxy(new GetSearchableInfo());
   }

   private static class GetSearchableInfo extends MethodProxy {
      private GetSearchableInfo() {
      }

      public String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGczNDdhNCg0LwcuCGkhLCZrNwZF"));
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         ComponentName component = (ComponentName)args[0];
         if (component != null) {
            ActivityInfo activityInfo = VirtualCore.getPM().getActivityInfo(component, 0);
            if (activityInfo != null) {
               return null;
            }
         }

         return method.invoke(who, args);
      }

      // $FF: synthetic method
      GetSearchableInfo(Object x0) {
         this();
      }
   }
}
