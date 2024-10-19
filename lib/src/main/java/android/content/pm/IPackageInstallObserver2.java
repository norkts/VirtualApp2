package android.content.pm;

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface IPackageInstallObserver2 extends IInterface {
   void onUserActionRequired(Intent var1) throws RemoteException;

   void onPackageInstalled(String var1, int var2, String var3, Bundle var4) throws RemoteException;

   public abstract static class Stub extends Binder implements IPackageInstallObserver2 {
      private static final String DESCRIPTOR = StringFog.decrypt("EgsWBAoHO10AABwEDAEaXRUfWCw+PhAIDhUVIAEdBwQeGioMLBYRGRcCWw==");
      static final int TRANSACTION_onUserActionRequired = 1;
      static final int TRANSACTION_onPackageInstalled = 2;

      public Stub() {
         this.attachInterface(this, StringFog.decrypt("EgsWBAoHO10AABwEDAEaXRUfWCw+PhAIDhUVIAEdBwQeGioMLBYRGRcCWw=="));
      }

      public static IPackageInstallObserver2 asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IPackageInstallObserver2)(iin != null && iin instanceof IPackageInstallObserver2 ? (IPackageInstallObserver2)iin : new Proxy(obj));
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
               Intent _arg0;
               if (0 != data.readInt()) {
                  _arg0 = (Intent)Intent.CREATOR.createFromParcel(data);
               } else {
                  _arg0 = null;
               }

               this.onUserActionRequired(_arg0);
               reply.writeNoException();
               return true;
            case 2:
               data.enforceInterface(descriptor);
               String s_arg0 = data.readString();
               int _arg1 = data.readInt();
               String _arg2 = data.readString();
               Bundle _arg3;
               if (0 != data.readInt()) {
                  _arg3 = (Bundle)Bundle.CREATOR.createFromParcel(data);
               } else {
                  _arg3 = null;
               }

               this.onPackageInstalled(s_arg0, _arg1, _arg2, _arg3);
               reply.writeNoException();
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(IPackageInstallObserver2 impl) {
         if (IPackageInstallObserver2.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException(StringFog.decrypt("AAAGMgAIPgYPGzsdGQNGWkURFwkCOhdDGwUZCgo="));
         } else if (impl != null) {
            IPackageInstallObserver2.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IPackageInstallObserver2 getDefaultImpl() {
         return IPackageInstallObserver2.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IPackageInstallObserver2 {
         private IBinder mRemote;
         public static IPackageInstallObserver2 sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return StringFog.decrypt("EgsWBAoHO10AABwEDAEaXRUfWCw+PhAIDhUVIAEdBwQeGioMLBYRGRcCWw==");
         }

         public void onUserActionRequired(Intent intent) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt("EgsWBAoHO10AABwEDAEaXRUfWCw+PhAIDhUVIAEdBwQeGioMLBYRGRcCWw=="));
               if (intent != null) {
                  _data.writeInt(1);
                  intent.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (!_status && IPackageInstallObserver2.Stub.getDefaultImpl() != null) {
                  IPackageInstallObserver2.Stub.getDefaultImpl().onUserActionRequired(intent);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void onPackageInstalled(String basePackageName, int returnCode, String msg, Bundle extras) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt("EgsWBAoHO10AABwEDAEaXRUfWCw+PhAIDhUVIAEdBwQeGioMLBYRGRcCWw=="));
               _data.writeString(basePackageName);
               _data.writeInt(returnCode);
               _data.writeString(msg);
               if (extras != null) {
                  _data.writeInt(1);
                  extras.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (!_status && IPackageInstallObserver2.Stub.getDefaultImpl() != null) {
                  IPackageInstallObserver2.Stub.getDefaultImpl().onPackageInstalled(basePackageName, returnCode, msg, extras);
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

   public static class Default implements IPackageInstallObserver2 {
      public void onUserActionRequired(Intent intent) throws RemoteException {
      }

      public void onPackageInstalled(String basePackageName, int returnCode, String msg, Bundle extras) throws RemoteException {
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
