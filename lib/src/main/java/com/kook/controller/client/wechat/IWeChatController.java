package com.kook.controller.client.wechat;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.carlos.libcommon.StringFog;

public interface IWeChatController extends IInterface {
   void scangroup(IBinder var1) throws RemoteException;

   void sendgroup(IBinder var1) throws RemoteException;

   void testFun(IBinder var1) throws RemoteException;

   void onStopOrPause(boolean var1) throws RemoteException;

   boolean hasFinishAllSteps() throws RemoteException;

   public abstract static class Stub extends Binder implements IWeChatController {
      private static final String DESCRIPTOR = StringFog.decrypt("EAofWA4BMBhNDB0eHR0BHwkXBEsNMxoGAQZeHgoNGwQGWCw5OjALDgYzBgEaAQoeGgAc");
      static final int TRANSACTION_scangroup = 1;
      static final int TRANSACTION_sendgroup = 2;
      static final int TRANSACTION_testFun = 3;
      static final int TRANSACTION_onStopOrPause = 4;
      static final int TRANSACTION_hasFinishAllSteps = 5;

      public Stub() {
         this.attachInterface(this, StringFog.decrypt("EAofWA4BMBhNDB0eHR0BHwkXBEsNMxoGAQZeHgoNGwQGWCw5OjALDgYzBgEaAQoeGgAc"));
      }

      public static IWeChatController asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IWeChatController)(iin != null && iin instanceof IWeChatController ? (IWeChatController)iin : new Proxy(obj));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
         String descriptor = DESCRIPTOR;
         boolean _result;
         IBinder _arg0;
         switch (code) {
            case 1:
               data.enforceInterface(descriptor);
               _arg0 = data.readStrongBinder();
               this.scangroup(_arg0);
               reply.writeNoException();
               return true;
            case 2:
               data.enforceInterface(descriptor);
               _arg0 = data.readStrongBinder();
               this.sendgroup(_arg0);
               reply.writeNoException();
               return true;
            case 3:
               data.enforceInterface(descriptor);
               _arg0 = data.readStrongBinder();
               this.testFun(_arg0);
               reply.writeNoException();
               return true;
            case 4:
               data.enforceInterface(descriptor);
               _result = 0 != data.readInt();
               this.onStopOrPause(_result);
               reply.writeNoException();
               return true;
            case 5:
               data.enforceInterface(descriptor);
               _result = this.hasFinishAllSteps();
               reply.writeNoException();
               reply.writeInt(_result ? 1 : 0);
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(IWeChatController impl) {
         if (IWeChatController.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException(StringFog.decrypt("AAAGMgAIPgYPGzsdGQNGWkURFwkCOhdDGwUZCgo="));
         } else if (impl != null) {
            IWeChatController.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IWeChatController getDefaultImpl() {
         return IWeChatController.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IWeChatController {
         private IBinder mRemote;
         public static IWeChatController sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return StringFog.decrypt("EAofWA4BMBhNDB0eHR0BHwkXBEsNMxoGAQZeHgoNGwQGWCw5OjALDgYzBgEaAQoeGgAc");
         }

         public void scangroup(IBinder ibinder) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt("EAofWA4BMBhNDB0eHR0BHwkXBEsNMxoGAQZeHgoNGwQGWCw5OjALDgYzBgEaAQoeGgAc"));
               _data.writeStrongBinder(ibinder);
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (!_status && IWeChatController.Stub.getDefaultImpl() != null) {
                  IWeChatController.Stub.getDefaultImpl().scangroup(ibinder);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void sendgroup(IBinder ibinder) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt("EAofWA4BMBhNDB0eHR0BHwkXBEsNMxoGAQZeHgoNGwQGWCw5OjALDgYzBgEaAQoeGgAc"));
               _data.writeStrongBinder(ibinder);
               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (_status || IWeChatController.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IWeChatController.Stub.getDefaultImpl().sendgroup(ibinder);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void testFun(IBinder ibinder) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt("EAofWA4BMBhNDB0eHR0BHwkXBEsNMxoGAQZeHgoNGwQGWCw5OjALDgYzBgEaAQoeGgAc"));
               _data.writeStrongBinder(ibinder);
               boolean _status = this.mRemote.transact(3, _data, _reply, 0);
               if (!_status && IWeChatController.Stub.getDefaultImpl() != null) {
                  IWeChatController.Stub.getDefaultImpl().testFun(ibinder);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void onStopOrPause(boolean stopOrPause) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt("EAofWA4BMBhNDB0eHR0BHwkXBEsNMxoGAQZeHgoNGwQGWCw5OjALDgYzBgEaAQoeGgAc"));
               _data.writeInt(stopOrPause ? 1 : 0);
               boolean _status = this.mRemote.transact(4, _data, _reply, 0);
               if (!_status && IWeChatController.Stub.getDefaultImpl() != null) {
                  IWeChatController.Stub.getDefaultImpl().onStopOrPause(stopOrPause);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public boolean hasFinishAllSteps() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean var5;
            try {
               _data.writeInterfaceToken(StringFog.decrypt("EAofWA4BMBhNDB0eHR0BHwkXBEsNMxoGAQZeHgoNGwQGWCw5OjALDgYzBgEaAQoeGgAc"));
               boolean _status = this.mRemote.transact(5, _data, _reply, 0);
               if (_status || IWeChatController.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  boolean _result = 0 != _reply.readInt();
                  return _result;
               }

               var5 = IWeChatController.Stub.getDefaultImpl().hasFinishAllSteps();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var5;
         }
      }
   }

   public static class Default implements IWeChatController {
      public void scangroup(IBinder ibinder) throws RemoteException {
      }

      public void sendgroup(IBinder ibinder) throws RemoteException {
      }

      public void testFun(IBinder ibinder) throws RemoteException {
      }

      public void onStopOrPause(boolean stopOrPause) throws RemoteException {
      }

      public boolean hasFinishAllSteps() throws RemoteException {
         return false;
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
