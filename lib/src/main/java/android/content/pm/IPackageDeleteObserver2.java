package android.content.pm;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface IPackageDeleteObserver2 extends IInterface {
   void onUserActionRequired(Intent var1) throws RemoteException;

   void onPackageDeleted(String var1, int var2, String var3) throws RemoteException;

   public abstract static class Stub extends Binder implements IPackageDeleteObserver2 {
      private static final String DESCRIPTOR = "android.content.pm.IPackageDeleteObserver2";
      static final int TRANSACTION_onUserActionRequired = 1;
      static final int TRANSACTION_onPackageDeleted = 2;

      public Stub() {
         this.attachInterface(this, "android.content.pm.IPackageDeleteObserver2");
      }

      public static IPackageDeleteObserver2 asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IPackageDeleteObserver2)(iin != null && iin instanceof IPackageDeleteObserver2 ? (IPackageDeleteObserver2)iin : new Proxy(obj));
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
               this.onPackageDeleted(s_arg0, _arg1, _arg2);
               reply.writeNoException();
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(IPackageDeleteObserver2 impl) {
         if (IPackageDeleteObserver2.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException("setDefaultImpl() called twice");
         } else if (impl != null) {
            IPackageDeleteObserver2.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IPackageDeleteObserver2 getDefaultImpl() {
         return IPackageDeleteObserver2.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IPackageDeleteObserver2 {
         private IBinder mRemote;
         public static IPackageDeleteObserver2 sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return "android.content.pm.IPackageDeleteObserver2";
         }

         public void onUserActionRequired(Intent intent) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("android.content.pm.IPackageDeleteObserver2");
               if (intent != null) {
                  _data.writeInt(1);
                  intent.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (_status || IPackageDeleteObserver2.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IPackageDeleteObserver2.Stub.getDefaultImpl().onUserActionRequired(intent);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void onPackageDeleted(String packageName, int returnCode, String msg) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("android.content.pm.IPackageDeleteObserver2");
               _data.writeString(packageName);
               _data.writeInt(returnCode);
               _data.writeString(msg);
               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (!_status && IPackageDeleteObserver2.Stub.getDefaultImpl() != null) {
                  IPackageDeleteObserver2.Stub.getDefaultImpl().onPackageDeleted(packageName, returnCode, msg);
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

   public static class Default implements IPackageDeleteObserver2 {
      public void onUserActionRequired(Intent intent) throws RemoteException {
      }

      public void onPackageDeleted(String packageName, int returnCode, String msg) throws RemoteException {
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
