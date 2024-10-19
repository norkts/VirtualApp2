package com.lody.virtual.client.hook.proxies.libcore;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.annotations.Inject;
import com.lody.virtual.client.hook.base.MethodInvocationProxy;
import com.lody.virtual.client.hook.base.MethodInvocationStub;
import com.lody.virtual.client.hook.base.StaticMethodProxy;
import com.lody.virtual.helper.utils.VLog;
import java.lang.reflect.Method;
import mirror.libcore.io.ForwardingOs;
import mirror.libcore.io.Libcore;

@Inject(MethodProxies.class)
public class LibCoreStub extends MethodInvocationProxy<MethodInvocationStub<Object>> {
   public LibCoreStub() {
      super(new MethodInvocationStub(getOs()));
   }

   private static Object getOs() {
      Object os = Libcore.os.get();
      if (ForwardingOs.os != null) {
         Object posix = ForwardingOs.os.get(os);
         if (posix != null) {
            os = posix;
         }
      }

      return os;
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceUidMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li5fD2wzBlo=")), 1));
      this.addMethodProxy(new ReplaceUidMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT42CmowPCY=")), 1));
      this.addMethodProxy(new ReplaceUidMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLG8KPAVjDgpF")), 0));
      this.addMethodProxy(new ReplaceUidMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ixg2CmowPCY=")), 1));
      this.addMethodProxy(new ReplaceUidMethodProxy(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGwVAiw=")), 0));
   }

   public void inject() {
      Libcore.os.set(this.getInvocationStub().getProxyInterface());
   }

   public boolean isEnvBad() {
      return getOs() != this.getInvocationStub().getProxyInterface();
   }

   public class ReplaceUidMethodProxy extends StaticMethodProxy {
      private final int index;

      public ReplaceUidMethodProxy(String name, int index) {
         super(name);
         this.index = index;
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         return super.call(who, method, args);
      }

      public boolean beforeCall(Object who, Method method, Object... args) {
         VLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JBUhDQ==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhUMM28FHjd9JDBKKQc2UmkgBiBlJywRKS4APGcOOChuATAhKQgbPnhSIFo=")) + method.getName());
         int uid = (Integer)args[this.index];
         if (uid == getVUid() || uid == getBaseVUid()) {
            args[this.index] = getRealUid();
         }

         return super.beforeCall(who, method, args);
      }
   }
}
