package com.lody.virtual.server.interfaces;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface IVirtualStorageService extends IInterface {
   void setVirtualStorage(String var1, int var2, String var3) throws RemoteException;

   String getVirtualStorage(String var1, int var2) throws RemoteException;

   void setVirtualStorageState(String var1, int var2, boolean var3) throws RemoteException;

   boolean isVirtualStorageEnable(String var1, int var2) throws RemoteException;

   public abstract static class Stub extends Binder implements IVirtualStorageService {
      private static final String DESCRIPTOR = "com.lody.virtual.server.interfaces.IVirtualStorageService";
      static final int TRANSACTION_setVirtualStorage = 1;
      static final int TRANSACTION_getVirtualStorage = 2;
      static final int TRANSACTION_setVirtualStorageState = 3;
      static final int TRANSACTION_isVirtualStorageEnable = 4;

      public Stub() {
         this.attachInterface(this, "com.lody.virtual.server.interfaces.IVirtualStorageService");
      }

      public static IVirtualStorageService asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IVirtualStorageService)(iin != null && iin instanceof IVirtualStorageService ? (IVirtualStorageService)iin : new Proxy(obj));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
         String descriptor = DESCRIPTOR;
         String _arg0;
         int _arg1;
         boolean _result;
         String s_result;
         switch (code) {
            case 1:
               data.enforceInterface(descriptor);
               _arg0 = data.readString();
               _arg1 = data.readInt();
               s_result = data.readString();
               this.setVirtualStorage(_arg0, _arg1, s_result);
               reply.writeNoException();
               return true;
            case 2:
               data.enforceInterface(descriptor);
               _arg0 = data.readString();
               _arg1 = data.readInt();
               s_result = this.getVirtualStorage(_arg0, _arg1);
               reply.writeNoException();
               reply.writeString(s_result);
               return true;
            case 3:
               data.enforceInterface(descriptor);
               _arg0 = data.readString();
               _arg1 = data.readInt();
               _result = 0 != data.readInt();
               this.setVirtualStorageState(_arg0, _arg1, _result);
               reply.writeNoException();
               return true;
            case 4:
               data.enforceInterface(descriptor);
               _arg0 = data.readString();
               _arg1 = data.readInt();
               _result = this.isVirtualStorageEnable(_arg0, _arg1);
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

      public static boolean setDefaultImpl(IVirtualStorageService impl) {
         if (IVirtualStorageService.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException("setDefaultImpl() called twice");
         } else if (impl != null) {
            IVirtualStorageService.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IVirtualStorageService getDefaultImpl() {
         return IVirtualStorageService.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IVirtualStorageService {
         private IBinder mRemote;
         public static IVirtualStorageService sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return "com.lody.virtual.server.interfaces.IVirtualStorageService";
         }

         public void setVirtualStorage(String packageName, int userId, String vsPath) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IVirtualStorageService");
               _data.writeString(packageName);
               _data.writeInt(userId);
               _data.writeString(vsPath);
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (!_status && IVirtualStorageService.Stub.getDefaultImpl() != null) {
                  IVirtualStorageService.Stub.getDefaultImpl().setVirtualStorage(packageName, userId, vsPath);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public String getVirtualStorage(String packageName, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            String var7;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IVirtualStorageService");
               _data.writeString(packageName);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (_status || IVirtualStorageService.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  String _result = _reply.readString();
                  return _result;
               }

               var7 = IVirtualStorageService.Stub.getDefaultImpl().getVirtualStorage(packageName, userId);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var7;
         }

         public void setVirtualStorageState(String packageName, int userId, boolean enable) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IVirtualStorageService");
               _data.writeString(packageName);
               _data.writeInt(userId);
               _data.writeInt(enable ? 1 : 0);
               boolean _status = this.mRemote.transact(3, _data, _reply, 0);
               if (!_status && IVirtualStorageService.Stub.getDefaultImpl() != null) {
                  IVirtualStorageService.Stub.getDefaultImpl().setVirtualStorageState(packageName, userId, enable);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public boolean isVirtualStorageEnable(String packageName, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean var7;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IVirtualStorageService");
               _data.writeString(packageName);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(4, _data, _reply, 0);
               if (_status || IVirtualStorageService.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  boolean _result = 0 != _reply.readInt();
                  return _result;
               }

               var7 = IVirtualStorageService.Stub.getDefaultImpl().isVirtualStorageEnable(packageName, userId);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var7;
         }
      }
   }

   public static class Default implements IVirtualStorageService {
      public void setVirtualStorage(String packageName, int userId, String vsPath) throws RemoteException {
      }

      public String getVirtualStorage(String packageName, int userId) throws RemoteException {
         return null;
      }

      public void setVirtualStorageState(String packageName, int userId, boolean enable) throws RemoteException {
      }

      public boolean isVirtualStorageEnable(String packageName, int userId) throws RemoteException {
         return false;
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
