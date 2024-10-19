package android.net.wifi;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Messenger;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface IWifiScanner extends IInterface {
   Messenger getMessenger() throws RemoteException;

   Bundle getAvailableChannels(int var1) throws RemoteException;

   public abstract static class Stub extends Binder implements IWifiScanner {
      private static final String DESCRIPTOR = StringFog.decrypt("EgsWBAoHO10NCgZeHgYIGks7IQwINiAADhweDB0=");
      static final int TRANSACTION_getMessenger = 1;
      static final int TRANSACTION_getAvailableChannels = 2;

      public Stub() {
         this.attachInterface(this, StringFog.decrypt("EgsWBAoHO10NCgZeHgYIGks7IQwINiAADhweDB0="));
      }

      public static IWifiScanner asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IWifiScanner)(iin != null && iin instanceof IWifiScanner ? (IWifiScanner)iin : new Proxy(obj));
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
               Messenger _result = this.getMessenger();
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
               int _arg0 = data.readInt();
               Bundle _result = this.getAvailableChannels(_arg0);
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

      public static boolean setDefaultImpl(IWifiScanner impl) {
         if (IWifiScanner.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException(StringFog.decrypt("AAAGMgAIPgYPGzsdGQNGWkURFwkCOhdDGwUZCgo="));
         } else if (impl != null) {
            IWifiScanner.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IWifiScanner getDefaultImpl() {
         return IWifiScanner.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IWifiScanner {
         private IBinder mRemote;
         public static IWifiScanner sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return StringFog.decrypt("EgsWBAoHO10NCgZeHgYIGks7IQwINiAADhweDB0=");
         }

         public Messenger getMessenger() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            Messenger _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt("EgsWBAoHO10NCgZeHgYIGks7IQwINiAADhweDB0="));
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (!_status && IWifiScanner.Stub.getDefaultImpl() != null) {
                  Messenger var5 = IWifiScanner.Stub.getDefaultImpl().getMessenger();
                  return var5;
               }

               _reply.readException();
               if (0 != _reply.readInt()) {
                  _result = (Messenger)Messenger.CREATOR.createFromParcel(_reply);
               } else {
                  _result = null;
               }
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public Bundle getAvailableChannels(int band) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            Bundle _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt("EgsWBAoHO10NCgZeHgYIGks7IQwINiAADhweDB0="));
               _data.writeInt(band);
               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (!_status && IWifiScanner.Stub.getDefaultImpl() != null) {
                  Bundle var6 = IWifiScanner.Stub.getDefaultImpl().getAvailableChannels(band);
                  return var6;
               }

               _reply.readException();
               if (0 != _reply.readInt()) {
                  _result = (Bundle)Bundle.CREATOR.createFromParcel(_reply);
               } else {
                  _result = null;
               }
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }
      }
   }

   public static class Default implements IWifiScanner {
      public Messenger getMessenger() throws RemoteException {
         return null;
      }

      public Bundle getAvailableChannels(int band) throws RemoteException {
         return null;
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
