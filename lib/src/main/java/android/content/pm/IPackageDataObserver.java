package android.content.pm;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface IPackageDataObserver extends IInterface {
   void onRemoveCompleted(String var1, boolean var2) throws RemoteException;

   public abstract static class Stub extends Binder implements IPackageDataObserver {
      private static final String DESCRIPTOR = "android.content.pm.IPackageDataObserver";
      static final int TRANSACTION_onRemoveCompleted = 1;

      public Stub() {
         this.attachInterface(this, "android.content.pm.IPackageDataObserver");
      }

      public static IPackageDataObserver asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IPackageDataObserver)(iin != null && iin instanceof IPackageDataObserver ? (IPackageDataObserver)iin : new Proxy(obj));
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
               boolean _arg1 = 0 != data.readInt();
               this.onRemoveCompleted(_arg0, _arg1);
               reply.writeNoException();
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(IPackageDataObserver impl) {
         if (IPackageDataObserver.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException("setDefaultImpl() called twice");
         } else if (impl != null) {
            IPackageDataObserver.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IPackageDataObserver getDefaultImpl() {
         return IPackageDataObserver.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IPackageDataObserver {
         private IBinder mRemote;
         public static IPackageDataObserver sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return "android.content.pm.IPackageDataObserver";
         }

         public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("android.content.pm.IPackageDataObserver");
               _data.writeString(packageName);
               _data.writeInt(succeeded ? 1 : 0);
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (!_status && IPackageDataObserver.Stub.getDefaultImpl() != null) {
                  IPackageDataObserver.Stub.getDefaultImpl().onRemoveCompleted(packageName, succeeded);
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

   public static class Default implements IPackageDataObserver {
      public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
