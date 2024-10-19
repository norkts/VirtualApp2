package android.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface IStopUserCallback extends IInterface {
   void userStopped(int var1) throws RemoteException;

   void userStopAborted(int var1) throws RemoteException;

   public abstract static class Stub extends Binder implements IStopUserCallback {
      private static final String DESCRIPTOR = "android.app.IStopUserCallback";
      static final int TRANSACTION_userStopped = 1;
      static final int TRANSACTION_userStopAborted = 2;

      public Stub() {
         this.attachInterface(this, "android.app.IStopUserCallback");
      }

      public static IStopUserCallback asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IStopUserCallback)(iin != null && iin instanceof IStopUserCallback ? (IStopUserCallback)iin : new Proxy(obj));
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
               this.userStopped(_arg0);
               reply.writeNoException();
               return true;
            case 2:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               this.userStopAborted(_arg0);
               reply.writeNoException();
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(IStopUserCallback impl) {
         if (IStopUserCallback.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException("setDefaultImpl() called twice");
         } else if (impl != null) {
            IStopUserCallback.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IStopUserCallback getDefaultImpl() {
         return IStopUserCallback.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IStopUserCallback {
         private IBinder mRemote;
         public static IStopUserCallback sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return "android.app.IStopUserCallback";
         }

         public void userStopped(int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("android.app.IStopUserCallback");
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (!_status && IStopUserCallback.Stub.getDefaultImpl() != null) {
                  IStopUserCallback.Stub.getDefaultImpl().userStopped(userId);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void userStopAborted(int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("android.app.IStopUserCallback");
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (!_status && IStopUserCallback.Stub.getDefaultImpl() != null) {
                  IStopUserCallback.Stub.getDefaultImpl().userStopAborted(userId);
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

   public static class Default implements IStopUserCallback {
      public void userStopped(int userId) throws RemoteException {
      }

      public void userStopAborted(int userId) throws RemoteException {
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
