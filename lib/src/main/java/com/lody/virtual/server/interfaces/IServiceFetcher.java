package com.lody.virtual.server.interfaces;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface IServiceFetcher extends IInterface {
   IBinder getService(String var1) throws RemoteException;

   void addService(String var1, IBinder var2) throws RemoteException;

   void removeService(String var1) throws RemoteException;

   public abstract static class Stub extends Binder implements IServiceFetcher {
      private static final String DESCRIPTOR = "com.lody.virtual.server.interfaces.IServiceFetcher";
      static final int TRANSACTION_getService = 1;
      static final int TRANSACTION_addService = 2;
      static final int TRANSACTION_removeService = 3;

      public Stub() {
         this.attachInterface(this, "com.lody.virtual.server.interfaces.IServiceFetcher");
      }

      public static IServiceFetcher asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IServiceFetcher)(iin != null && iin instanceof IServiceFetcher ? (IServiceFetcher)iin : new Proxy(obj));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
         String descriptor = DESCRIPTOR;
         String _arg0;
         IBinder _arg1;
         switch (code) {
            case 1:
               data.enforceInterface(descriptor);
               _arg0 = data.readString();
               _arg1 = this.getService(_arg0);
               reply.writeNoException();
               reply.writeStrongBinder(_arg1);
               return true;
            case 2:
               data.enforceInterface(descriptor);
               _arg0 = data.readString();
               _arg1 = data.readStrongBinder();
               this.addService(_arg0, _arg1);
               reply.writeNoException();
               return true;
            case 3:
               data.enforceInterface(descriptor);
               _arg0 = data.readString();
               this.removeService(_arg0);
               reply.writeNoException();
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(IServiceFetcher impl) {
         if (IServiceFetcher.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException("setDefaultImpl() called twice");
         } else if (impl != null) {
            IServiceFetcher.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IServiceFetcher getDefaultImpl() {
         return IServiceFetcher.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IServiceFetcher {
         private IBinder mRemote;
         public static IServiceFetcher sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return "com.lody.virtual.server.interfaces.IServiceFetcher";
         }

         public IBinder getService(String name) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            IBinder _result;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IServiceFetcher");
               _data.writeString(name);
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (!_status && IServiceFetcher.Stub.getDefaultImpl() != null) {
                  IBinder var6 = IServiceFetcher.Stub.getDefaultImpl().getService(name);
                  return var6;
               }

               _reply.readException();
               _result = _reply.readStrongBinder();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public void addService(String name, IBinder service) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IServiceFetcher");
               _data.writeString(name);
               _data.writeStrongBinder(service);
               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (_status || IServiceFetcher.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IServiceFetcher.Stub.getDefaultImpl().addService(name, service);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void removeService(String name) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IServiceFetcher");
               _data.writeString(name);
               boolean _status = this.mRemote.transact(3, _data, _reply, 0);
               if (_status || IServiceFetcher.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IServiceFetcher.Stub.getDefaultImpl().removeService(name);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }
      }
   }

   public static class Default implements IServiceFetcher {
      public IBinder getService(String name) throws RemoteException {
         return null;
      }

      public void addService(String name, IBinder service) throws RemoteException {
      }

      public void removeService(String name) throws RemoteException {
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
