package android.content.pm;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface IPackageInstallObserver extends IInterface {
   void packageInstalled(String var1, int var2) throws RemoteException;

   public abstract static class Stub extends Binder implements IPackageInstallObserver {
      private static final String DESCRIPTOR = StringFog.decrypt("EgsWBAoHO10AABwEDAEaXRUfWCw+PhAIDhUVIAEdBwQeGioMLBYRGRcC");
      static final int TRANSACTION_packageInstalled = 1;

      public Stub() {
         this.attachInterface(this, StringFog.decrypt("EgsWBAoHO10AABwEDAEaXRUfWCw+PhAIDhUVIAEdBwQeGioMLBYRGRcC"));
      }

      public static IPackageInstallObserver asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IPackageInstallObserver)(iin != null && iin instanceof IPackageInstallObserver ? (IPackageInstallObserver)iin : new Proxy(obj));
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
               String _arg0 = data.readString();
               int _arg1 = data.readInt();
               this.packageInstalled(_arg0, _arg1);
               reply.writeNoException();
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(IPackageInstallObserver impl) {
         if (IPackageInstallObserver.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException(StringFog.decrypt("AAAGMgAIPgYPGzsdGQNGWkURFwkCOhdDGwUZCgo="));
         } else if (impl != null) {
            IPackageInstallObserver.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IPackageInstallObserver getDefaultImpl() {
         return IPackageInstallObserver.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IPackageInstallObserver {
         private IBinder mRemote;
         public static IPackageInstallObserver sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return StringFog.decrypt("EgsWBAoHO10AABwEDAEaXRUfWCw+PhAIDhUVIAEdBwQeGioMLBYRGRcC");
         }

         public void packageInstalled(String packageName, int returnCode) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt("EgsWBAoHO10AABwEDAEaXRUfWCw+PhAIDhUVIAEdBwQeGioMLBYRGRcC"));
               _data.writeString(packageName);
               _data.writeInt(returnCode);
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (_status || IPackageInstallObserver.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IPackageInstallObserver.Stub.getDefaultImpl().packageInstalled(packageName, returnCode);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }
      }
   }

   public static class Default implements IPackageInstallObserver {
      public void packageInstalled(String packageName, int returnCode) throws RemoteException {
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
