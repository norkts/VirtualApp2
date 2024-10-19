package com.lody.virtual.client.hook.secondary;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.client.core.VirtualCore;
import java.io.FileDescriptor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

abstract class StubBinder implements IBinder {
   private ClassLoader mClassLoader;
   private IBinder mBase;
   private IInterface mInterface;
   private Context context;

   StubBinder(Context appContext, ClassLoader classLoader, IBinder base) {
      this.context = appContext;
      this.mClassLoader = classLoader;
      this.mBase = base;
   }

   public String getAppPkg() {
      return this.context.getPackageName();
   }

   public String getHostPkg() {
      return VirtualCore.get().getHostPkg();
   }

   public String getInterfaceDescriptor() throws RemoteException {
      return this.mBase.getInterfaceDescriptor();
   }

   public boolean pingBinder() {
      return this.mBase.pingBinder();
   }

   public boolean isBinderAlive() {
      return this.mBase.isBinderAlive();
   }

   public IInterface queryLocalInterface(String descriptor) {
      if (this.mInterface == null) {
         StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
         if (stackTrace == null || stackTrace.length <= 1) {
            return null;
         }

         Class<?> aidlType = null;
         IInterface targetInterface = null;
         StackTraceElement[] var5 = stackTrace;
         int var6 = stackTrace.length;
         int var7 = 0;

         while(true) {
            if (var7 >= var6) {
               if (aidlType == null || targetInterface == null) {
                  return null;
               }

               InvocationHandler handler = this.createHandler(aidlType, targetInterface);
               this.mInterface = (IInterface)Proxy.newProxyInstance(this.mClassLoader, new Class[]{aidlType}, handler);
               break;
            }

            StackTraceElement element = var5[var7];
            if (!element.isNativeMethod()) {
               try {
                  Method method = this.mClassLoader.loadClass(element.getClassName()).getDeclaredMethod(element.getMethodName(), IBinder.class);
                  if ((method.getModifiers() & 8) != 0) {
                     method.setAccessible(true);
                     Class<?> returnType = method.getReturnType();
                     if (returnType.isInterface() && IInterface.class.isAssignableFrom(returnType)) {
                        aidlType = returnType;
                        targetInterface = (IInterface)method.invoke((Object)null, this.mBase);
                     }
                  }
               } catch (Exception var11) {
               }
            }

            ++var7;
         }
      }

      return this.mInterface;
   }

   public abstract InvocationHandler createHandler(Class<?> var1, IInterface var2);

   public void dump(FileDescriptor fd, String[] args) throws RemoteException {
      this.mBase.dump(fd, args);
   }

   public void dumpAsync(FileDescriptor fd, String[] args) throws RemoteException {
      this.mBase.dumpAsync(fd, args);
   }

   public boolean transact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
      return this.mBase.transact(code, data, reply, flags);
   }

   public void linkToDeath(IBinder.DeathRecipient recipient, int flags) throws RemoteException {
      this.mBase.linkToDeath(recipient, flags);
   }

   public boolean unlinkToDeath(IBinder.DeathRecipient recipient, int flags) {
      return this.mBase.unlinkToDeath(recipient, flags);
   }
}
