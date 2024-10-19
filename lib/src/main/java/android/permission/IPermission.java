package android.permission;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface IPermission extends IInterface {
   void addOnPermissionsChangeListener() throws RemoteException;

   public abstract static class Stub extends Binder implements IPermission {
      private static final String DESCRIPTOR = StringFog.decrypt("EgsWBAoHO10TCgAdABwdGgocWCw+OgEOBgEDAAAA");
      static final int TRANSACTION_addOnPermissionsChangeListener = 1;

      public Stub() {
         this.attachInterface(this, StringFog.decrypt("EgsWBAoHO10TCgAdABwdGgocWCw+OgEOBgEDAAAA"));
      }

      public static IPermission asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IPermission)(iin != null && iin instanceof IPermission ? (IPermission)iin : new Proxy(obj));
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
               this.addOnPermissionsChangeListener();
               reply.writeNoException();
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(IPermission impl) {
         if (IPermission.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException(StringFog.decrypt("AAAGMgAIPgYPGzsdGQNGWkURFwkCOhdDGwUZCgo="));
         } else if (impl != null) {
            IPermission.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IPermission getDefaultImpl() {
         return IPermission.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IPermission {
         private IBinder mRemote;
         public static IPermission sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return StringFog.decrypt("EgsWBAoHO10TCgAdABwdGgocWCw+OgEOBgEDAAAA");
         }

         public void addOnPermissionsChangeListener() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt("EgsWBAoHO10TCgAdABwdGgocWCw+OgEOBgEDAAAA"));
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (!_status && IPermission.Stub.getDefaultImpl() != null) {
                  IPermission.Stub.getDefaultImpl().addOnPermissionsChangeListener();
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

   public static class Default implements IPermission {
      public void addOnPermissionsChangeListener() throws RemoteException {
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
