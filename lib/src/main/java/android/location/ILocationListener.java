package android.location;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface ILocationListener extends IInterface {
   void onLocationChanged(Location var1) throws RemoteException;

   void onStatusChanged(String var1, int var2, Bundle var3) throws RemoteException;

   void onProviderEnabled(String var1) throws RemoteException;

   void onProviderDisabled(String var1) throws RemoteException;

   public abstract static class Stub extends Binder implements ILocationListener {
      private static final String DESCRIPTOR = StringFog.decrypt("EgsWBAoHO10PABERHQYBHUs7OgoNPgcKABw8ABwaFgsXBA==");
      static final int TRANSACTION_onLocationChanged = 1;
      static final int TRANSACTION_onStatusChanged = 2;
      static final int TRANSACTION_onProviderEnabled = 3;
      static final int TRANSACTION_onProviderDisabled = 4;

      public Stub() {
         this.attachInterface(this, StringFog.decrypt("EgsWBAoHO10PABERHQYBHUs7OgoNPgcKABw8ABwaFgsXBA=="));
      }

      public static ILocationListener asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (ILocationListener)(iin != null && iin instanceof ILocationListener ? (ILocationListener)iin : new Proxy(obj));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
         String descriptor = DESCRIPTOR;
         String _arg0;
         switch (code) {
            case 1:
               data.enforceInterface(descriptor);
               Location _arg0;
               if (0 != data.readInt()) {
                  _arg0 = (Location)Location.CREATOR.createFromParcel(data);
               } else {
                  _arg0 = null;
               }

               this.onLocationChanged(_arg0);
               reply.writeNoException();
               return true;
            case 2:
               data.enforceInterface(descriptor);
               _arg0 = data.readString();
               int _arg1 = data.readInt();
               Bundle _arg2;
               if (0 != data.readInt()) {
                  _arg2 = (Bundle)Bundle.CREATOR.createFromParcel(data);
               } else {
                  _arg2 = null;
               }

               this.onStatusChanged(_arg0, _arg1, _arg2);
               reply.writeNoException();
               return true;
            case 3:
               data.enforceInterface(descriptor);
               _arg0 = data.readString();
               this.onProviderEnabled(_arg0);
               reply.writeNoException();
               return true;
            case 4:
               data.enforceInterface(descriptor);
               _arg0 = data.readString();
               this.onProviderDisabled(_arg0);
               reply.writeNoException();
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(ILocationListener impl) {
         if (ILocationListener.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException(StringFog.decrypt("AAAGMgAIPgYPGzsdGQNGWkURFwkCOhdDGwUZCgo="));
         } else if (impl != null) {
            ILocationListener.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static ILocationListener getDefaultImpl() {
         return ILocationListener.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements ILocationListener {
         private IBinder mRemote;
         public static ILocationListener sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return StringFog.decrypt("EgsWBAoHO10PABERHQYBHUs7OgoNPgcKABw8ABwaFgsXBA==");
         }

         public void onLocationChanged(Location location) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt("EgsWBAoHO10PABERHQYBHUs7OgoNPgcKABw8ABwaFgsXBA=="));
               if (location != null) {
                  _data.writeInt(1);
                  location.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (!_status && ILocationListener.Stub.getDefaultImpl() != null) {
                  ILocationListener.Stub.getDefaultImpl().onLocationChanged(location);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void onStatusChanged(String provider, int status, Bundle extras) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt("EgsWBAoHO10PABERHQYBHUs7OgoNPgcKABw8ABwaFgsXBA=="));
               _data.writeString(provider);
               _data.writeInt(status);
               if (extras != null) {
                  _data.writeInt(1);
                  extras.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (!_status && ILocationListener.Stub.getDefaultImpl() != null) {
                  ILocationListener.Stub.getDefaultImpl().onStatusChanged(provider, status, extras);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void onProviderEnabled(String provider) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt("EgsWBAoHO10PABERHQYBHUs7OgoNPgcKABw8ABwaFgsXBA=="));
               _data.writeString(provider);
               boolean _status = this.mRemote.transact(3, _data, _reply, 0);
               if (_status || ILocationListener.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               ILocationListener.Stub.getDefaultImpl().onProviderEnabled(provider);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void onProviderDisabled(String provider) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt("EgsWBAoHO10PABERHQYBHUs7OgoNPgcKABw8ABwaFgsXBA=="));
               _data.writeString(provider);
               boolean _status = this.mRemote.transact(4, _data, _reply, 0);
               if (_status || ILocationListener.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               ILocationListener.Stub.getDefaultImpl().onProviderDisabled(provider);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }
      }
   }

   public static class Default implements ILocationListener {
      public void onLocationChanged(Location location) throws RemoteException {
      }

      public void onStatusChanged(String provider, int status, Bundle extras) throws RemoteException {
      }

      public void onProviderEnabled(String provider) throws RemoteException {
      }

      public void onProviderDisabled(String provider) throws RemoteException {
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
