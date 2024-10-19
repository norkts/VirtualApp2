package com.kook.controller.client.normal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.carlos.libcommon.StringFog;

public interface ILearnController extends IInterface {
   void debugLearn() throws RemoteException;

   public abstract static class Stub extends Binder implements ILearnController {
      private static final String DESCRIPTOR = "com.kook.controller.client.normal.ILearnController";
      static final int TRANSACTION_debugLearn = 1;

      public Stub() {
         this.attachInterface(this, "com.kook.controller.client.normal.ILearnController");
      }

      public static ILearnController asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (ILearnController)(iin != null && iin instanceof ILearnController ? (ILearnController)iin : new Proxy(obj));
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
               this.debugLearn();
               reply.writeNoException();
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(ILearnController impl) {
         if (ILearnController.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException("setDefaultImpl() called twice");
         } else if (impl != null) {
            ILearnController.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static ILearnController getDefaultImpl() {
         return ILearnController.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements ILearnController {
         private IBinder mRemote;
         public static ILearnController sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return "com.kook.controller.client.normal.ILearnController";
         }

         public void debugLearn() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("com.kook.controller.client.normal.ILearnController");
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (!_status && ILearnController.Stub.getDefaultImpl() != null) {
                  ILearnController.Stub.getDefaultImpl().debugLearn();
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }
      }
   }

   public static class Default implements ILearnController {
      public void debugLearn() throws RemoteException {
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
