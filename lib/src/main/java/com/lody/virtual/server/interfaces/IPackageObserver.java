package com.lody.virtual.server.interfaces;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface IPackageObserver extends IInterface {
   void onPackageInstalled(String var1) throws RemoteException;

   void onPackageUninstalled(String var1) throws RemoteException;

   void onPackageInstalledAsUser(int var1, String var2) throws RemoteException;

   void onPackageUninstalledAsUser(int var1, String var2) throws RemoteException;

   public abstract static class Stub extends Binder implements IPackageObserver {
      private static final String DESCRIPTOR = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWLxcmKm8gOD9pJSg1Ki4uKmwjNAQ="));
      static final int TRANSACTION_onPackageInstalled = 1;
      static final int TRANSACTION_onPackageUninstalled = 2;
      static final int TRANSACTION_onPackageInstalledAsUser = 3;
      static final int TRANSACTION_onPackageUninstalledAsUser = 4;

      public Stub() {
         this.attachInterface(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWLxcmKm8gOD9pJSg1Ki4uKmwjNAQ=")));
      }

      public static IPackageObserver asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IPackageObserver)(iin != null && iin instanceof IPackageObserver ? (IPackageObserver)iin : new Proxy(obj));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
         String descriptor = DESCRIPTOR;
         int _arg0;
         String _arg1;
         String _arg0s;
         switch (code) {
            case 1:
               data.enforceInterface(descriptor);
               _arg0s = data.readString();
               this.onPackageInstalled(_arg0s);
               reply.writeNoException();
               return true;
            case 2:
               data.enforceInterface(descriptor);
               _arg0s = data.readString();
               this.onPackageUninstalled(_arg0s);
               reply.writeNoException();
               return true;
            case 3:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               _arg1 = data.readString();
               this.onPackageInstalledAsUser(_arg0, _arg1);
               reply.writeNoException();
               return true;
            case 4:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               _arg1 = data.readString();
               this.onPackageUninstalledAsUser(_arg0, _arg1);
               reply.writeNoException();
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(IPackageObserver impl) {
         if (IPackageObserver.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGAFNC59ATAoLBUcD2ozOyB6DTwqLRgEKGIKESNqETwiKAgAVg==")));
         } else if (impl != null) {
            IPackageObserver.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IPackageObserver getDefaultImpl() {
         return IPackageObserver.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IPackageObserver {
         private IBinder mRemote;
         public static IPackageObserver sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWLxcmKm8gOD9pJSg1Ki4uKmwjNAQ="));
         }

         public void onPackageInstalled(String packageName) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWLxcmKm8gOD9pJSg1Ki4uKmwjNAQ=")));
               _data.writeString(packageName);
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (!_status && IPackageObserver.Stub.getDefaultImpl() != null) {
                  IPackageObserver.Stub.getDefaultImpl().onPackageInstalled(packageName);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void onPackageUninstalled(String packageName) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWLxcmKm8gOD9pJSg1Ki4uKmwjNAQ=")));
               _data.writeString(packageName);
               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (_status || IPackageObserver.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IPackageObserver.Stub.getDefaultImpl().onPackageUninstalled(packageName);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void onPackageInstalledAsUser(int userId, String packageName) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWLxcmKm8gOD9pJSg1Ki4uKmwjNAQ=")));
               _data.writeInt(userId);
               _data.writeString(packageName);
               boolean _status = this.mRemote.transact(3, _data, _reply, 0);
               if (_status || IPackageObserver.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IPackageObserver.Stub.getDefaultImpl().onPackageInstalledAsUser(userId, packageName);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void onPackageUninstalledAsUser(int userId, String packageName) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWLxcmKm8gOD9pJSg1Ki4uKmwjNAQ=")));
               _data.writeInt(userId);
               _data.writeString(packageName);
               boolean _status = this.mRemote.transact(4, _data, _reply, 0);
               if (!_status && IPackageObserver.Stub.getDefaultImpl() != null) {
                  IPackageObserver.Stub.getDefaultImpl().onPackageUninstalledAsUser(userId, packageName);
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

   public static class Default implements IPackageObserver {
      public void onPackageInstalled(String packageName) throws RemoteException {
      }

      public void onPackageUninstalled(String packageName) throws RemoteException {
      }

      public void onPackageInstalledAsUser(int userId, String packageName) throws RemoteException {
      }

      public void onPackageUninstalledAsUser(int userId, String packageName) throws RemoteException {
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
