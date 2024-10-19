package com.lody.virtual.client.hook.proxies.libcore;

import com.lody.virtual.StringFog;
import com.lody.virtual.client.NativeEngine;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.helper.utils.Reflect;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import mirror.libcore.io.Os;

class MethodProxies {
   static class Stat extends MethodProxy {
      private static Field st_uid;

      public Object afterCall(Object who, Method method, Object[] args, Object result) throws Throwable {
         int uid = (Integer)st_uid.get(result);
         if (uid == VirtualCore.get().myUid()) {
            st_uid.set(result, getBaseVUid());
         }

         return result;
      }

      public String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qP2wFSFo="));
      }

      static {
         try {
            Method stat = Os.TYPE.getMethod(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qP2wFSFo=")), String.class);
            Class<?> StructStat = stat.getReturnType();
            st_uid = StructStat.getDeclaredField(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qH2wVAiw=")));
            st_uid.setAccessible(true);
         } catch (Throwable var2) {
            Throwable e = var2;
            throw new IllegalStateException(e);
         }
      }
   }

   static class GetsockoptUcred extends MethodProxy {
      public String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLG8zGiljJB4sLBYMP2oVGiw="));
      }

      public Object afterCall(Object who, Method method, Object[] args, Object result) throws Throwable {
         if (result != null) {
            Reflect ucred = Reflect.on(result);
            int uid = (Integer)ucred.get(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQgYPA==")));
            if (uid == VirtualCore.get().myUid()) {
               ucred.set(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQgYPA==")), getBaseVUid());
            }
         }

         return result;
      }
   }

   static class GetUid extends MethodProxy {
      public String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLGwVAiw="));
      }

      public Object afterCall(Object who, Method method, Object[] args, Object result) throws Throwable {
         int uid = (Integer)result;
         return NativeEngine.onGetUid(uid);
      }
   }

   static class Getpwnam extends MethodProxy {
      public String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS4uLG8KPCZ9Dl1F"));
      }

      public Object afterCall(Object who, Method method, Object[] args, Object result) throws Throwable {
         if (result != null) {
            Reflect pwd = Reflect.on(result);
            int uid = (Integer)pwd.get(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhcmH2wVAiw=")));
            if (uid == VirtualCore.get().myUid()) {
               pwd.set(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhcmH2wVAiw=")), VClient.get().getVUid());
            }
         }

         return result;
      }
   }

   static class Fstat extends Stat {
      public String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT02LGsaMFo="));
      }

      public Object afterCall(Object who, Method method, Object[] args, Object result) throws Throwable {
         if (result != null) {
            Reflect pwd = Reflect.on(result);
            int uid = (Integer)pwd.get(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qH2wVAiw=")));
            if (uid == VirtualCore.get().myUid()) {
               pwd.set(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qH2wVAiw=")), VClient.get().getVUid());
            }
         }

         return result;
      }
   }

   static class Lstat extends Stat {
      public String getMethodName() {
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ixc2LGsaMFo="));
      }

      public Object afterCall(Object who, Method method, Object[] args, Object result) throws Throwable {
         if (result != null) {
            Reflect pwd = Reflect.on(result);
            int uid = (Integer)pwd.get(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qH2wVAiw=")));
            if (uid == VirtualCore.get().myUid()) {
               pwd.set(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qH2wVAiw=")), VClient.get().getVUid());
            }
         }

         return result;
      }
   }
}
