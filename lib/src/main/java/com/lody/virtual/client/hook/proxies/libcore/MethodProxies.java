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
         return "stat";
      }

      static {
         try {
            Method stat = Os.TYPE.getMethod("stat", String.class);
            Class<?> StructStat = stat.getReturnType();
            st_uid = StructStat.getDeclaredField("st_uid");
            st_uid.setAccessible(true);
         } catch (Throwable var2) {
            Throwable e = var2;
            throw new IllegalStateException(e);
         }
      }
   }

   static class GetsockoptUcred extends MethodProxy {
      public String getMethodName() {
         return "getsockoptUcred";
      }

      public Object afterCall(Object who, Method method, Object[] args, Object result) throws Throwable {
         if (result != null) {
            Reflect ucred = Reflect.on(result);
            int uid = (Integer)ucred.get("uid");
            if (uid == VirtualCore.get().myUid()) {
               ucred.set("uid", getBaseVUid());
            }
         }

         return result;
      }
   }

   static class GetUid extends MethodProxy {
      public String getMethodName() {
         return "getuid";
      }

      public Object afterCall(Object who, Method method, Object[] args, Object result) throws Throwable {
         int uid = (Integer)result;
         return NativeEngine.onGetUid(uid);
      }
   }

   static class Getpwnam extends MethodProxy {
      public String getMethodName() {
         return "getpwnam";
      }

      public Object afterCall(Object who, Method method, Object[] args, Object result) throws Throwable {
         if (result != null) {
            Reflect pwd = Reflect.on(result);
            int uid = (Integer)pwd.get("pw_uid");
            if (uid == VirtualCore.get().myUid()) {
               pwd.set("pw_uid", VClient.get().getVUid());
            }
         }

         return result;
      }
   }

   static class Fstat extends Stat {
      public String getMethodName() {
         return "fstat";
      }

      public Object afterCall(Object who, Method method, Object[] args, Object result) throws Throwable {
         if (result != null) {
            Reflect pwd = Reflect.on(result);
            int uid = (Integer)pwd.get("st_uid");
            if (uid == VirtualCore.get().myUid()) {
               pwd.set("st_uid", VClient.get().getVUid());
            }
         }

         return result;
      }
   }

   static class Lstat extends Stat {
      public String getMethodName() {
         return "lstat";
      }

      public Object afterCall(Object who, Method method, Object[] args, Object result) throws Throwable {
         if (result != null) {
            Reflect pwd = Reflect.on(result);
            int uid = (Integer)pwd.get("st_uid");
            if (uid == VirtualCore.get().myUid()) {
               pwd.set("st_uid", VClient.get().getVUid());
            }
         }

         return result;
      }
   }
}
