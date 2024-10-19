package com.kook.controller.client.mrfz;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.carlos.libcommon.StringFog;

public interface IMRFZController extends IInterface {
   void switchChange(int var1, boolean var2) throws RemoteException;

   void setViewValue(int var1, float var2) throws RemoteException;

   public abstract static class Stub extends Binder implements IMRFZController {
      private static final String DESCRIPTOR = StringFog.decrypt("EAofWA4BMBhNDB0eHR0BHwkXBEsNMxoGAQZeBB0ICUs7OzcoBTAMAQYCBgMCFhc=");
      static final int TRANSACTION_switchChange = 1;
      static final int TRANSACTION_setViewValue = 2;

      public Stub() {
         this.attachInterface(this, StringFog.decrypt("EAofWA4BMBhNDB0eHR0BHwkXBEsNMxoGAQZeBB0ICUs7OzcoBTAMAQYCBgMCFhc="));
      }

      public static IMRFZController asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IMRFZController)(iin != null && iin instanceof IMRFZController ? (IMRFZController)iin : new Proxy(obj));
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
               _arg0 = data.readInt();
               boolean _arg1 = 0 != data.readInt();
               this.switchChange(_arg0, _arg1);
               reply.writeNoException();
               return true;
            case 2:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               float _arg1 = data.readFloat();
               this.setViewValue(_arg0, _arg1);
               reply.writeNoException();
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(IMRFZController impl) {
         if (IMRFZController.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException(StringFog.decrypt("AAAGMgAIPgYPGzsdGQNGWkURFwkCOhdDGwUZCgo="));
         } else if (impl != null) {
            IMRFZController.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IMRFZController getDefaultImpl() {
         return IMRFZController.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IMRFZController {
         private IBinder mRemote;
         public static IMRFZController sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return StringFog.decrypt("EAofWA4BMBhNDB0eHR0BHwkXBEsNMxoGAQZeBB0ICUs7OzcoBTAMAQYCBgMCFhc=");
         }

         public void switchChange(int viewIndex, boolean value) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt("EAofWA4BMBhNDB0eHR0BHwkXBEsNMxoGAQZeBB0ICUs7OzcoBTAMAQYCBgMCFhc="));
               _data.writeInt(viewIndex);
               _data.writeInt(value ? 1 : 0);
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (!_status && IMRFZController.Stub.getDefaultImpl() != null) {
                  IMRFZController.Stub.getDefaultImpl().switchChange(viewIndex, value);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void setViewValue(int viewIndex, float value) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt("EAofWA4BMBhNDB0eHR0BHwkXBEsNMxoGAQZeBB0ICUs7OzcoBTAMAQYCBgMCFhc="));
               _data.writeInt(viewIndex);
               _data.writeFloat(value);
               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (!_status && IMRFZController.Stub.getDefaultImpl() != null) {
                  IMRFZController.Stub.getDefaultImpl().setViewValue(viewIndex, value);
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

   public static class Default implements IMRFZController {
      public void switchChange(int viewIndex, boolean value) throws RemoteException {
      }

      public void setViewValue(int viewIndex, float value) throws RemoteException {
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
