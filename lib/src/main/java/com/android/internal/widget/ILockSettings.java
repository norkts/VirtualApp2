package com.android.internal.widget;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface ILockSettings extends IInterface {
   void setRecoverySecretTypes(int[] var1) throws RemoteException;

   int[] getRecoverySecretTypes() throws RemoteException;

   public abstract static class Stub extends Binder implements ILockSettings {
      private static final String DESCRIPTOR = "com.android.internal.widget.ILockSettings";
      static final int TRANSACTION_setRecoverySecretTypes = 1;
      static final int TRANSACTION_getRecoverySecretTypes = 2;

      public Stub() {
         this.attachInterface(this, "com.android.internal.widget.ILockSettings");
      }

      public static ILockSettings asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (ILockSettings)(iin != null && iin instanceof ILockSettings ? (ILockSettings)iin : new Proxy(obj));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
         String descriptor = DESCRIPTOR;
         int[] _result;
         switch (code) {
            case 1:
               data.enforceInterface(descriptor);
               _result = data.createIntArray();
               this.setRecoverySecretTypes(_result);
               reply.writeNoException();
               return true;
            case 2:
               data.enforceInterface(descriptor);
               _result = this.getRecoverySecretTypes();
               reply.writeNoException();
               reply.writeIntArray(_result);
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(ILockSettings impl) {
         if (ILockSettings.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException("setDefaultImpl() called twice");
         } else if (impl != null) {
            ILockSettings.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static ILockSettings getDefaultImpl() {
         return ILockSettings.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements ILockSettings {
         private IBinder mRemote;
         public static ILockSettings sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return "com.android.internal.widget.ILockSettings";
         }

         public void setRecoverySecretTypes(int[] secretTypes) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("com.android.internal.widget.ILockSettings");
               _data.writeIntArray(secretTypes);
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (!_status && ILockSettings.Stub.getDefaultImpl() != null) {
                  ILockSettings.Stub.getDefaultImpl().setRecoverySecretTypes(secretTypes);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public int[] getRecoverySecretTypes() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            int[] _result;
            try {
               _data.writeInterfaceToken("com.android.internal.widget.ILockSettings");
               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (!_status && ILockSettings.Stub.getDefaultImpl() != null) {
                  int[] var5 = ILockSettings.Stub.getDefaultImpl().getRecoverySecretTypes();
                  return var5;
               }

               _reply.readException();
               _result = _reply.createIntArray();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }
      }
   }

   public static class Default implements ILockSettings {
      public void setRecoverySecretTypes(int[] secretTypes) throws RemoteException {
      }

      public int[] getRecoverySecretTypes() throws RemoteException {
         return null;
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
