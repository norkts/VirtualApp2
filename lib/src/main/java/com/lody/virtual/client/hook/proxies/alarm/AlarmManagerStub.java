package com.lody.virtual.client.hook.proxies.alarm;

import android.app.AlarmManager;
import android.os.IInterface;
import android.os.WorkSource;
import android.os.Build.VERSION;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.BinderInvocationStub;
import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgAndLastUserIdMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceFirstPkgMethodProxy;
import com.lody.virtual.helper.utils.ArrayUtils;
import java.lang.reflect.Method;
import mirror.android.app.IAlarmManager;

public class AlarmManagerStub extends BinderInvocationProxy {
   public AlarmManagerStub() {
      super(IAlarmManager.Stub.asInterface, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggEP28jElo=")));
   }

   public void inject() throws Throwable {
      super.inject();
      AlarmManager alarmManager = (AlarmManager)VirtualCore.get().getContext().getSystemService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggEP28jElo=")));
      if (mirror.android.app.AlarmManager.mService != null) {
         try {
            mirror.android.app.AlarmManager.mService.set(alarmManager, (IInterface)((BinderInvocationStub)this.getInvocationStub()).getProxyInterface());
         } catch (Exception var3) {
            Exception e = var3;
            e.printStackTrace();
         }
      }

   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new Set());
      this.addMethodProxy(new SetTime());
      this.addMethodProxy(new SetTimeZone());
      this.addMethodProxy(new ReplaceFirstPkgMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4+CGczLCBiDgovKhcMWmgzQSlvHzgdLRcML2EjSFo="))));
      this.addMethodProxy(new ReplaceCallingPkgAndLastUserIdMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBg+KWczLCBiDgovKhcMWmgzQSlvHzgdLRcMLw=="))));
   }

   private static class GetNextAlarmClock extends MethodProxy {
      public String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGIjNDBmHCAoLwguD2YFOCVoJ11F"));
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         replaceLastUserId(args);
         return super.call(who, method, args);
      }
   }

   private static class Set extends MethodProxy {
      private Set() {
      }

      public String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLA=="));
      }

      public boolean beforeCall(Object who, Method method, Object... args) {
         if (VERSION.SDK_INT >= 24 && args[0] instanceof String) {
            args[0] = getHostPkg();
         }

         int index = ArrayUtils.indexOfFirst(args, WorkSource.class);
         if (index >= 0) {
            args[index] = null;
         }

         return true;
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         try {
            return super.call(who, method, args);
         } catch (Throwable var5) {
            Throwable e = var5;
            e.printStackTrace();
            return 0;
         }
      }

      // $FF: synthetic method
      Set(Object x0) {
         this();
      }
   }

   private static class SetTime extends MethodProxy {
      private SetTime() {
      }

      public String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGQFAiNiAVRF"));
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return VERSION.SDK_INT >= 21 ? false : null;
      }

      // $FF: synthetic method
      SetTime(Object x0) {
         this();
      }
   }

   private static class SetTimeZone extends MethodProxy {
      private SetTimeZone() {
      }

      public String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGQFAiNiDwI1Kj0MVg=="));
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return null;
      }

      // $FF: synthetic method
      SetTimeZone(Object x0) {
         this();
      }
   }
}
