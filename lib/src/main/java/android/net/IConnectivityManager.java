package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface IConnectivityManager extends IInterface {
   NetworkInfo getActiveNetworkInfo() throws RemoteException;

   NetworkInfo getActiveNetworkInfoForUid(int var1, boolean var2) throws RemoteException;

   NetworkInfo getNetworkInfo(int var1) throws RemoteException;

   NetworkInfo[] getAllNetworkInfo() throws RemoteException;

   boolean isActiveNetworkMetered() throws RemoteException;

   boolean requestRouteToHostAddress(int var1, int var2) throws RemoteException;

   LinkProperties getActiveLinkProperties() throws RemoteException;

   LinkProperties getLinkProperties(int var1) throws RemoteException;

   public abstract static class Stub extends Binder implements IConnectivityManager {
      private static final String DESCRIPTOR = StringFog.decrypt("EgsWBAoHO10NCgZeICwBHQsXFREHKRoXFj8RBw4JFhc=");
      static final int TRANSACTION_getActiveNetworkInfo = 1;
      static final int TRANSACTION_getActiveNetworkInfoForUid = 2;
      static final int TRANSACTION_getNetworkInfo = 3;
      static final int TRANSACTION_getAllNetworkInfo = 4;
      static final int TRANSACTION_isActiveNetworkMetered = 5;
      static final int TRANSACTION_requestRouteToHostAddress = 6;
      static final int TRANSACTION_getActiveLinkProperties = 7;
      static final int TRANSACTION_getLinkProperties = 8;

      public Stub() {
         this.attachInterface(this, StringFog.decrypt("EgsWBAoHO10NCgZeICwBHQsXFREHKRoXFj8RBw4JFhc="));
      }

      public static IConnectivityManager asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IConnectivityManager)(iin != null && iin instanceof IConnectivityManager ? (IConnectivityManager)iin : new Proxy(obj));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
         String descriptor = DESCRIPTOR;
         int _arg0;
         switch (code) {
            case 1:
               data.enforceInterface(descriptor);
               NetworkInfo _result = this.getActiveNetworkInfo();
               reply.writeNoException();
               if (_result != null) {
                  reply.writeInt(1);
                  _result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 2:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               boolean _arg1 = 0 != data.readInt();
               NetworkInfo _result = this.getActiveNetworkInfoForUid(_arg0, _arg1);
               reply.writeNoException();
               if (_result != null) {
                  reply.writeInt(1);
                  _result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 3:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               NetworkInfo _result = this.getNetworkInfo(_arg0);
               reply.writeNoException();
               if (_result != null) {
                  reply.writeInt(1);
                  _result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 4:
               data.enforceInterface(descriptor);
               NetworkInfo[] _result = this.getAllNetworkInfo();
               reply.writeNoException();
               reply.writeTypedArray(_result, 1);
               return true;
            case 5:
               data.enforceInterface(descriptor);
               boolean _result = this.isActiveNetworkMetered();
               reply.writeNoException();
               reply.writeInt(_result ? 1 : 0);
               return true;
            case 6:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               int _arg1 = data.readInt();
               boolean _result = this.requestRouteToHostAddress(_arg0, _arg1);
               reply.writeNoException();
               reply.writeInt(_result ? 1 : 0);
               return true;
            case 7:
               data.enforceInterface(descriptor);
               LinkProperties _result = this.getActiveLinkProperties();
               reply.writeNoException();
               if (_result != null) {
                  reply.writeInt(1);
                  _result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 8:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               LinkProperties _result = this.getLinkProperties(_arg0);
               reply.writeNoException();
               if (_result != null) {
                  reply.writeInt(1);
                  _result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(IConnectivityManager impl) {
         if (IConnectivityManager.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException(StringFog.decrypt("AAAGMgAIPgYPGzsdGQNGWkURFwkCOhdDGwUZCgo="));
         } else if (impl != null) {
            IConnectivityManager.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IConnectivityManager getDefaultImpl() {
         return IConnectivityManager.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IConnectivityManager {
         private IBinder mRemote;
         public static IConnectivityManager sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return StringFog.decrypt("EgsWBAoHO10NCgZeICwBHQsXFREHKRoXFj8RBw4JFhc=");
         }

         public NetworkInfo getActiveNetworkInfo() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            NetworkInfo _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt("EgsWBAoHO10NCgZeICwBHQsXFREHKRoXFj8RBw4JFhc="));
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (!_status && IConnectivityManager.Stub.getDefaultImpl() != null) {
                  NetworkInfo var5 = IConnectivityManager.Stub.getDefaultImpl().getActiveNetworkInfo();
                  return var5;
               }

               _reply.readException();
               if (0 != _reply.readInt()) {
                  _result = (NetworkInfo)NetworkInfo.CREATOR.createFromParcel(_reply);
               } else {
                  _result = null;
               }
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public NetworkInfo getActiveNetworkInfoForUid(int uid, boolean ignoreBlocked) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            NetworkInfo _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt("EgsWBAoHO10NCgZeICwBHQsXFREHKRoXFj8RBw4JFhc="));
               _data.writeInt(uid);
               _data.writeInt(ignoreBlocked ? 1 : 0);
               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (!_status && IConnectivityManager.Stub.getDefaultImpl() != null) {
                  NetworkInfo var7 = IConnectivityManager.Stub.getDefaultImpl().getActiveNetworkInfoForUid(uid, ignoreBlocked);
                  return var7;
               }

               _reply.readException();
               if (0 != _reply.readInt()) {
                  _result = (NetworkInfo)NetworkInfo.CREATOR.createFromParcel(_reply);
               } else {
                  _result = null;
               }
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public NetworkInfo getNetworkInfo(int networkType) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            NetworkInfo var6;
            try {
               _data.writeInterfaceToken(StringFog.decrypt("EgsWBAoHO10NCgZeICwBHQsXFREHKRoXFj8RBw4JFhc="));
               _data.writeInt(networkType);
               boolean _status = this.mRemote.transact(3, _data, _reply, 0);
               if (_status || IConnectivityManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  NetworkInfo _result;
                  if (0 != _reply.readInt()) {
                     _result = (NetworkInfo)NetworkInfo.CREATOR.createFromParcel(_reply);
                  } else {
                     _result = null;
                  }

                  return _result;
               }

               var6 = IConnectivityManager.Stub.getDefaultImpl().getNetworkInfo(networkType);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var6;
         }

         public NetworkInfo[] getAllNetworkInfo() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            NetworkInfo[] _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt("EgsWBAoHO10NCgZeICwBHQsXFREHKRoXFj8RBw4JFhc="));
               boolean _status = this.mRemote.transact(4, _data, _reply, 0);
               if (!_status && IConnectivityManager.Stub.getDefaultImpl() != null) {
                  NetworkInfo[] var5 = IConnectivityManager.Stub.getDefaultImpl().getAllNetworkInfo();
                  return var5;
               }

               _reply.readException();
               _result = (NetworkInfo[])_reply.createTypedArray(NetworkInfo.CREATOR);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public boolean isActiveNetworkMetered() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt("EgsWBAoHO10NCgZeICwBHQsXFREHKRoXFj8RBw4JFhc="));
               boolean _status = this.mRemote.transact(5, _data, _reply, 0);
               if (!_status && IConnectivityManager.Stub.getDefaultImpl() != null) {
                  boolean var5 = IConnectivityManager.Stub.getDefaultImpl().isActiveNetworkMetered();
                  return var5;
               }

               _reply.readException();
               _result = 0 != _reply.readInt();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public boolean requestRouteToHostAddress(int networkType, int address) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean var7;
            try {
               _data.writeInterfaceToken(StringFog.decrypt("EgsWBAoHO10NCgZeICwBHQsXFREHKRoXFj8RBw4JFhc="));
               _data.writeInt(networkType);
               _data.writeInt(address);
               boolean _status = this.mRemote.transact(6, _data, _reply, 0);
               if (_status || IConnectivityManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  boolean _result = 0 != _reply.readInt();
                  return _result;
               }

               var7 = IConnectivityManager.Stub.getDefaultImpl().requestRouteToHostAddress(networkType, address);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var7;
         }

         public LinkProperties getActiveLinkProperties() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            LinkProperties var5;
            try {
               _data.writeInterfaceToken(StringFog.decrypt("EgsWBAoHO10NCgZeICwBHQsXFREHKRoXFj8RBw4JFhc="));
               boolean _status = this.mRemote.transact(7, _data, _reply, 0);
               if (_status || IConnectivityManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  LinkProperties _result;
                  if (0 != _reply.readInt()) {
                     _result = (LinkProperties)LinkProperties.CREATOR.createFromParcel(_reply);
                  } else {
                     _result = null;
                  }

                  return _result;
               }

               var5 = IConnectivityManager.Stub.getDefaultImpl().getActiveLinkProperties();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var5;
         }

         public LinkProperties getLinkProperties(int networkType) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            LinkProperties var6;
            try {
               _data.writeInterfaceToken(StringFog.decrypt("EgsWBAoHO10NCgZeICwBHQsXFREHKRoXFj8RBw4JFhc="));
               _data.writeInt(networkType);
               boolean _status = this.mRemote.transact(8, _data, _reply, 0);
               if (_status || IConnectivityManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  LinkProperties _result;
                  if (0 != _reply.readInt()) {
                     _result = (LinkProperties)LinkProperties.CREATOR.createFromParcel(_reply);
                  } else {
                     _result = null;
                  }

                  return _result;
               }

               var6 = IConnectivityManager.Stub.getDefaultImpl().getLinkProperties(networkType);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var6;
         }
      }
   }

   public static class Default implements IConnectivityManager {
      public NetworkInfo getActiveNetworkInfo() throws RemoteException {
         return null;
      }

      public NetworkInfo getActiveNetworkInfoForUid(int uid, boolean ignoreBlocked) throws RemoteException {
         return null;
      }

      public NetworkInfo getNetworkInfo(int networkType) throws RemoteException {
         return null;
      }

      public NetworkInfo[] getAllNetworkInfo() throws RemoteException {
         return null;
      }

      public boolean isActiveNetworkMetered() throws RemoteException {
         return false;
      }

      public boolean requestRouteToHostAddress(int networkType, int address) throws RemoteException {
         return false;
      }

      public LinkProperties getActiveLinkProperties() throws RemoteException {
         return null;
      }

      public LinkProperties getLinkProperties(int networkType) throws RemoteException {
         return null;
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
