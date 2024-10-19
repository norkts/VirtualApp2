package com.kook.controller.server.service;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.carlos.libcommon.StringFog;

public interface IServiceCallBack extends IInterface {
   void checkBack(boolean var1, int var2) throws RemoteException;

   void listenerOptionEvent(int var1) throws RemoteException;

   void adbDebugFunction() throws RemoteException;

   public abstract static class Stub extends Binder implements IServiceCallBack {
      private static final String DESCRIPTOR = StringFog.decrypt("EAofWA4BMBhNDB0eHR0BHwkXBEsdOgEVCgBeGgocBQwRE0snDBYRGRsTDCwPHwkwFwYF");
      static final int TRANSACTION_checkBack = 1;
      static final int TRANSACTION_listenerOptionEvent = 2;
      static final int TRANSACTION_adbDebugFunction = 3;

      public Stub() {
         this.attachInterface(this, StringFog.decrypt("EAofWA4BMBhNDB0eHR0BHwkXBEsdOgEVCgBeGgocBQwRE0snDBYRGRsTDCwPHwkwFwYF"));
      }

      public static IServiceCallBack asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IServiceCallBack)(iin != null && iin instanceof IServiceCallBack ? (IServiceCallBack)iin : new Proxy(obj));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
         String descriptor = DESCRIPTOR;
         switch (code) {
            case 1:
               data.enforceInterface(descriptor);
               boolean _arg0 = 0 != data.readInt();
               int _arg1 = data.readInt();
               this.checkBack(_arg0, _arg1);
               reply.writeNoException();
               return true;
            case 2:
               data.enforceInterface(descriptor);
               int i_arg0 = data.readInt();
               this.listenerOptionEvent(i_arg0);
               reply.writeNoException();
               return true;
            case 3:
               data.enforceInterface(descriptor);
               this.adbDebugFunction();
               reply.writeNoException();
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(IServiceCallBack impl) {
         if (IServiceCallBack.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException(StringFog.decrypt("AAAGMgAIPgYPGzsdGQNGWkURFwkCOhdDGwUZCgo="));
         } else if (impl != null) {
            IServiceCallBack.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IServiceCallBack getDefaultImpl() {
         return IServiceCallBack.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IServiceCallBack {
         private IBinder mRemote;
         public static IServiceCallBack sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return StringFog.decrypt("EAofWA4BMBhNDB0eHR0BHwkXBEsdOgEVCgBeGgocBQwRE0snDBYRGRsTDCwPHwkwFwYF");
         }

         public void checkBack(boolean check, int targetProductIndex) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt("EAofWA4BMBhNDB0eHR0BHwkXBEsdOgEVCgBeGgocBQwRE0snDBYRGRsTDCwPHwkwFwYF"));
               _data.writeInt(check ? 1 : 0);
               _data.writeInt(targetProductIndex);
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (!_status && IServiceCallBack.Stub.getDefaultImpl() != null) {
                  IServiceCallBack.Stub.getDefaultImpl().checkBack(check, targetProductIndex);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void listenerOptionEvent(int optionEvt) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt("EAofWA4BMBhNDB0eHR0BHwkXBEsdOgEVCgBeGgocBQwRE0snDBYRGRsTDCwPHwkwFwYF"));
               _data.writeInt(optionEvt);
               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (_status || IServiceCallBack.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IServiceCallBack.Stub.getDefaultImpl().listenerOptionEvent(optionEvt);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void adbDebugFunction() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt("EAofWA4BMBhNDB0eHR0BHwkXBEsdOgEVCgBeGgocBQwRE0snDBYRGRsTDCwPHwkwFwYF"));
               boolean _status = this.mRemote.transact(3, _data, _reply, 0);
               if (_status || IServiceCallBack.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IServiceCallBack.Stub.getDefaultImpl().adbDebugFunction();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }
      }
   }

   public static class Default implements IServiceCallBack {
      public void checkBack(boolean check, int targetProductIndex) throws RemoteException {
      }

      public void listenerOptionEvent(int optionEvt) throws RemoteException {
      }

      public void adbDebugFunction() throws RemoteException {
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
