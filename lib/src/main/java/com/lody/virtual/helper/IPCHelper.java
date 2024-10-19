package com.lody.virtual.helper;

import android.os.IInterface;
import android.os.RemoteException;

public abstract class IPCHelper<S extends IInterface> {
   private S mInterface;

   public abstract S getInterface();

   public <R> R call(Callable<S, R> callable) {
      int i = 0;

      while(i <= 2) {
         if (this.mInterface == null || this.mInterface.asBinder().isBinderAlive()) {
            this.mInterface = this.getInterface();
         }

         try {
            return callable.call(this.mInterface);
         } catch (RemoteException var4) {
            RemoteException e = var4;
            e.printStackTrace();
            ++i;
         }
      }

      return null;
   }

   public boolean callBoolean(Callable<S, Boolean> callable) {
      Boolean res = (Boolean)this.call(callable);
      return res == null ? false : res;
   }

   public void callVoid(final CallableVoid<S> callable) {
      Callable<S, Void> wrapper = new Callable<S, Void>() {
         public Void call(S service) throws RemoteException {
            callable.call(service);
            return null;
         }
      };
      this.call(wrapper);
   }

   public interface CallableVoid<S> {
      void call(S var1) throws RemoteException;
   }

   public interface Callable<S, R> {
      R call(S var1) throws RemoteException;
   }
}
