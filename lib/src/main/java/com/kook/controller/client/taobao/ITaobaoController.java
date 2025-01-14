package com.kook.controller.client.taobao;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.carlos.libcommon.StringFog;

public interface ITaobaoController extends IInterface {
   boolean checkLogin(IBinder var1) throws RemoteException;

   void doLogin(IBinder var1) throws RemoteException;

   void testFun(IBinder var1) throws RemoteException;

   public abstract static class Stub extends Binder implements ITaobaoController {
      private static final String DESCRIPTOR = "com.kook.controller.client.taobao.ITaobaoController";
      static final int TRANSACTION_checkLogin = 1;
      static final int TRANSACTION_doLogin = 2;
      static final int TRANSACTION_testFun = 3;

      public Stub() {
         this.attachInterface(this, "com.kook.controller.client.taobao.ITaobaoController");
      }

      public static ITaobaoController asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (ITaobaoController)(iin != null && iin instanceof ITaobaoController ? (ITaobaoController)iin : new Proxy(obj));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
         String descriptor = DESCRIPTOR;
         IBinder _arg0;
         switch (code) {
            case 1:
               data.enforceInterface(descriptor);
               _arg0 = data.readStrongBinder();
               boolean _result = this.checkLogin(_arg0);
               reply.writeNoException();
               reply.writeInt(_result ? 1 : 0);
               return true;
            case 2:
               data.enforceInterface(descriptor);
               _arg0 = data.readStrongBinder();
               this.doLogin(_arg0);
               reply.writeNoException();
               return true;
            case 3:
               data.enforceInterface(descriptor);
               _arg0 = data.readStrongBinder();
               this.testFun(_arg0);
               reply.writeNoException();
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(ITaobaoController impl) {
         if (ITaobaoController.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException("setDefaultImpl() called twice");
         } else if (impl != null) {
            ITaobaoController.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static ITaobaoController getDefaultImpl() {
         return ITaobaoController.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements ITaobaoController {
         private IBinder mRemote;
         public static ITaobaoController sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return "com.kook.controller.client.taobao.ITaobaoController";
         }

         public boolean checkLogin(IBinder ibinder) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean _result;
            try {
               _data.writeInterfaceToken("com.kook.controller.client.taobao.ITaobaoController");
               _data.writeStrongBinder(ibinder);
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (!_status && ITaobaoController.Stub.getDefaultImpl() != null) {
                  boolean var6 = ITaobaoController.Stub.getDefaultImpl().checkLogin(ibinder);
                  return var6;
               }

               _reply.readException();
               _result = 0 != _reply.readInt();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public void doLogin(IBinder ibinder) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("com.kook.controller.client.taobao.ITaobaoController");
               _data.writeStrongBinder(ibinder);
               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (_status || ITaobaoController.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               ITaobaoController.Stub.getDefaultImpl().doLogin(ibinder);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void testFun(IBinder ibinder) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("com.kook.controller.client.taobao.ITaobaoController");
               _data.writeStrongBinder(ibinder);
               boolean _status = this.mRemote.transact(3, _data, _reply, 0);
               if (_status || ITaobaoController.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               ITaobaoController.Stub.getDefaultImpl().testFun(ibinder);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }
      }
   }

   public static class Default implements ITaobaoController {
      public boolean checkLogin(IBinder ibinder) throws RemoteException {
         return false;
      }

      public void doLogin(IBinder ibinder) throws RemoteException {
      }

      public void testFun(IBinder ibinder) throws RemoteException {
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
