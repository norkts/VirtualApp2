package com.lody.virtual.server.interfaces;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface IProcessObserver extends IInterface {
   void onProcessCreated(String var1, String var2) throws RemoteException;

   void onProcessDied(String var1, String var2) throws RemoteException;

   public abstract static class Stub extends Binder implements IProcessObserver {
      private static final String DESCRIPTOR = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWLxgqJmkgFiNqDyg1Ki4uKmwjNAQ="));
      static final int TRANSACTION_onProcessCreated = 1;
      static final int TRANSACTION_onProcessDied = 2;

      public Stub() {
         this.attachInterface(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWLxgqJmkgFiNqDyg1Ki4uKmwjNAQ=")));
      }

      public static IProcessObserver asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IProcessObserver)(iin != null && iin instanceof IProcessObserver ? (IProcessObserver)iin : new Proxy(obj));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
         String descriptor = DESCRIPTOR;
         String _arg0;
         String _arg1;
         switch (code) {
            case 1:
               data.enforceInterface(descriptor);
               _arg0 = data.readString();
               _arg1 = data.readString();
               this.onProcessCreated(_arg0, _arg1);
               reply.writeNoException();
               return true;
            case 2:
               data.enforceInterface(descriptor);
               _arg0 = data.readString();
               _arg1 = data.readString();
               this.onProcessDied(_arg0, _arg1);
               reply.writeNoException();
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(IProcessObserver impl) {
         if (IProcessObserver.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGAFNC59ATAoLBUcD2ozOyB6DTwqLRgEKGIKESNqETwiKAgAVg==")));
         } else if (impl != null) {
            IProcessObserver.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IProcessObserver getDefaultImpl() {
         return IProcessObserver.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IProcessObserver {
         private IBinder mRemote;
         public static IProcessObserver sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWLxgqJmkgFiNqDyg1Ki4uKmwjNAQ="));
         }

         public void onProcessCreated(String pkg, String processName) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWLxgqJmkgFiNqDyg1Ki4uKmwjNAQ=")));
               _data.writeString(pkg);
               _data.writeString(processName);
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (_status || IProcessObserver.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IProcessObserver.Stub.getDefaultImpl().onProcessCreated(pkg, processName);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void onProcessDied(String pkg, String processName) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWLxgqJmkgFiNqDyg1Ki4uKmwjNAQ=")));
               _data.writeString(pkg);
               _data.writeString(processName);
               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (!_status && IProcessObserver.Stub.getDefaultImpl() != null) {
                  IProcessObserver.Stub.getDefaultImpl().onProcessDied(pkg, processName);
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

   public static class Default implements IProcessObserver {
      public void onProcessCreated(String pkg, String processName) throws RemoteException {
      }

      public void onProcessDied(String pkg, String processName) throws RemoteException {
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
