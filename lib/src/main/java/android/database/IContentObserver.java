package android.database;

import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface IContentObserver extends IInterface {
   void onChange(boolean var1, Uri var2, int var3) throws RemoteException;

   public abstract static class Stub extends Binder implements IContentObserver {
      private static final String DESCRIPTOR = "android.database.IContentObserver";
      static final int TRANSACTION_onChange = 1;

      public Stub() {
         this.attachInterface(this, "android.database.IContentObserver");
      }

      public static IContentObserver asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IContentObserver)(iin != null && iin instanceof IContentObserver ? (IContentObserver)iin : new Proxy(obj));
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
               boolean _arg0 = 0 != data.readInt();
               Uri _arg1;
               if (0 != data.readInt()) {
                  _arg1 = (Uri)Uri.CREATOR.createFromParcel(data);
               } else {
                  _arg1 = null;
               }

               int _arg2 = data.readInt();
               this.onChange(_arg0, _arg1, _arg2);
               reply.writeNoException();
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(IContentObserver impl) {
         if (IContentObserver.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException("setDefaultImpl() called twice");
         } else if (impl != null) {
            IContentObserver.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IContentObserver getDefaultImpl() {
         return IContentObserver.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IContentObserver {
         private IBinder mRemote;
         public static IContentObserver sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return "android.database.IContentObserver";
         }

         public void onChange(boolean selfUpdate, Uri uri, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("android.database.IContentObserver");
               _data.writeInt(selfUpdate ? 1 : 0);
               if (uri != null) {
                  _data.writeInt(1);
                  uri.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (_status || IContentObserver.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IContentObserver.Stub.getDefaultImpl().onChange(selfUpdate, uri, userId);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }
      }
   }

   public static class Default implements IContentObserver {
      public void onChange(boolean selfUpdate, Uri uri, int userId) throws RemoteException {
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
