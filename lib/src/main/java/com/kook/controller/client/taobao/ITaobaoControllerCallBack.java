package com.kook.controller.client.taobao;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.carlos.libcommon.StringFog;

public interface ITaobaoControllerCallBack extends IInterface {
   void loginNotify(boolean var1) throws RemoteException;

   public abstract static class Stub extends Binder implements ITaobaoControllerCallBack {
      private static final String DESCRIPTOR = "com.kook.controller.client.taobao.ITaobaoControllerCallBack";
      static final int TRANSACTION_loginNotify = 1;

      public Stub() {
         this.attachInterface(this, "com.kook.controller.client.taobao.ITaobaoControllerCallBack");
      }

      public static ITaobaoControllerCallBack asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (ITaobaoControllerCallBack)(iin != null && iin instanceof ITaobaoControllerCallBack ? (ITaobaoControllerCallBack)iin : new Proxy(obj));
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
               this.loginNotify(_arg0);
               reply.writeNoException();
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(ITaobaoControllerCallBack impl) {
         if (ITaobaoControllerCallBack.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException("setDefaultImpl() called twice");
         } else if (impl != null) {
            ITaobaoControllerCallBack.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static ITaobaoControllerCallBack getDefaultImpl() {
         return ITaobaoControllerCallBack.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements ITaobaoControllerCallBack {
         private IBinder mRemote;
         public static ITaobaoControllerCallBack sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return "com.kook.controller.client.taobao.ITaobaoControllerCallBack";
         }

         public void loginNotify(boolean loginResult) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("com.kook.controller.client.taobao.ITaobaoControllerCallBack");
               _data.writeInt(loginResult ? 1 : 0);
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (_status || ITaobaoControllerCallBack.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               ITaobaoControllerCallBack.Stub.getDefaultImpl().loginNotify(loginResult);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }
      }
   }

   public static class Default implements ITaobaoControllerCallBack {
      public void loginNotify(boolean loginResult) throws RemoteException {
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
