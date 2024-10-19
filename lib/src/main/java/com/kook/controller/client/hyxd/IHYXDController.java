package com.kook.controller.client.hyxd;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.carlos.libcommon.StringFog;

public interface IHYXDController extends IInterface {
   boolean memorySRWData(String var1, String var2, boolean var3) throws RemoteException;

   boolean memoryTest() throws RemoteException;

   public abstract static class Stub extends Binder implements IHYXDController {
      private static final String DESCRIPTOR = StringFog.decrypt("EAofWA4BMBhNDB0eHR0BHwkXBEsNMxoGAQZeARYWF0s7Pjw2GzAMAQYCBgMCFhc=");
      static final int TRANSACTION_memorySRWData = 1;
      static final int TRANSACTION_memoryTest = 2;

      public Stub() {
         this.attachInterface(this, StringFog.decrypt("EAofWA4BMBhNDB0eHR0BHwkXBEsNMxoGAQZeARYWF0s7Pjw2GzAMAQYCBgMCFhc="));
      }

      public static IHYXDController asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IHYXDController)(iin != null && iin instanceof IHYXDController ? (IHYXDController)iin : new Proxy(obj));
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
               String _arg1 = data.readString();
               boolean _arg2 = 0 != data.readInt();
               boolean _result = this.memorySRWData(_arg0, _arg1, _arg2);
               reply.writeNoException();
               reply.writeInt(_result ? 1 : 0);
               return true;
            case 2:
               data.enforceInterface(descriptor);
               boolean _result = this.memoryTest();
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

      public static boolean setDefaultImpl(IHYXDController impl) {
         if (IHYXDController.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException(StringFog.decrypt("AAAGMgAIPgYPGzsdGQNGWkURFwkCOhdDGwUZCgo="));
         } else if (impl != null) {
            IHYXDController.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IHYXDController getDefaultImpl() {
         return IHYXDController.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IHYXDController {
         private IBinder mRemote;
         public static IHYXDController sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return StringFog.decrypt("EAofWA4BMBhNDB0eHR0BHwkXBEsNMxoGAQZeARYWF0s7Pjw2GzAMAQYCBgMCFhc=");
         }

         public boolean memorySRWData(String searchValue, String writeValue, boolean permission) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt("EAofWA4BMBhNDB0eHR0BHwkXBEsNMxoGAQZeARYWF0s7Pjw2GzAMAQYCBgMCFhc="));
               _data.writeString(searchValue);
               _data.writeString(writeValue);
               _data.writeInt(permission ? 1 : 0);
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (!_status && IHYXDController.Stub.getDefaultImpl() != null) {
                  boolean var8 = IHYXDController.Stub.getDefaultImpl().memorySRWData(searchValue, writeValue, permission);
                  return var8;
               }

               _reply.readException();
               _result = 0 != _reply.readInt();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public boolean memoryTest() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean var5;
            try {
               _data.writeInterfaceToken(StringFog.decrypt("EAofWA4BMBhNDB0eHR0BHwkXBEsNMxoGAQZeARYWF0s7Pjw2GzAMAQYCBgMCFhc="));
               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (_status || IHYXDController.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  boolean _result = 0 != _reply.readInt();
                  return _result;
               }

               var5 = IHYXDController.Stub.getDefaultImpl().memoryTest();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var5;
         }
      }
   }

   public static class Default implements IHYXDController {
      public boolean memorySRWData(String searchValue, String writeValue, boolean permission) throws RemoteException {
         return false;
      }

      public boolean memoryTest() throws RemoteException {
         return false;
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
