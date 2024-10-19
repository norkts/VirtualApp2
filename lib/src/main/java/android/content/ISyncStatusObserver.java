package android.content;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface ISyncStatusObserver extends IInterface {
   void onStatusChanged(int var1) throws RemoteException;

   public abstract static class Stub extends Binder implements ISyncStatusObserver {
      private static final String DESCRIPTOR = StringFog.decrypt("EgsWBAoHO10AABwEDAEaXSwhDwsNDAcCGwcDJg0dFhcEExc=");
      static final int TRANSACTION_onStatusChanged = 1;

      public Stub() {
         this.attachInterface(this, StringFog.decrypt("EgsWBAoHO10AABwEDAEaXSwhDwsNDAcCGwcDJg0dFhcEExc="));
      }

      public static ISyncStatusObserver asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (ISyncStatusObserver)(iin != null && iin instanceof ISyncStatusObserver ? (ISyncStatusObserver)iin : new Proxy(obj));
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
               int _arg0 = data.readInt();
               this.onStatusChanged(_arg0);
               reply.writeNoException();
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(ISyncStatusObserver impl) {
         if (ISyncStatusObserver.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException(StringFog.decrypt("AAAGMgAIPgYPGzsdGQNGWkURFwkCOhdDGwUZCgo="));
         } else if (impl != null) {
            ISyncStatusObserver.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static ISyncStatusObserver getDefaultImpl() {
         return ISyncStatusObserver.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements ISyncStatusObserver {
         private IBinder mRemote;
         public static ISyncStatusObserver sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return StringFog.decrypt("EgsWBAoHO10AABwEDAEaXSwhDwsNDAcCGwcDJg0dFhcEExc=");
         }

         public void onStatusChanged(int which) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt("EgsWBAoHO10AABwEDAEaXSwhDwsNDAcCGwcDJg0dFhcEExc="));
               _data.writeInt(which);
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (!_status && ISyncStatusObserver.Stub.getDefaultImpl() != null) {
                  ISyncStatusObserver.Stub.getDefaultImpl().onStatusChanged(which);
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

   public static class Default implements ISyncStatusObserver {
      public void onStatusChanged(int which) throws RemoteException {
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
