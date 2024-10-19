package android.content;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface ISyncContext extends IInterface {
   void sendHeartbeat() throws RemoteException;

   void onFinished(SyncResult var1) throws RemoteException;

   public abstract static class Stub extends Binder implements ISyncContext {
      private static final String DESCRIPTOR = "android.content.ISyncContext";
      static final int TRANSACTION_sendHeartbeat = 1;
      static final int TRANSACTION_onFinished = 2;

      public Stub() {
         this.attachInterface(this, "android.content.ISyncContext");
      }

      public static ISyncContext asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (ISyncContext)(iin != null && iin instanceof ISyncContext ? (ISyncContext)iin : new Proxy(obj));
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
               this.sendHeartbeat();
               reply.writeNoException();
               return true;
            case 2:
               data.enforceInterface(descriptor);
               SyncResult _arg0;
               if (0 != data.readInt()) {
                  _arg0 = (SyncResult)SyncResult.CREATOR.createFromParcel(data);
               } else {
                  _arg0 = null;
               }

               this.onFinished(_arg0);
               reply.writeNoException();
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(ISyncContext impl) {
         if (ISyncContext.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException("setDefaultImpl() called twice");
         } else if (impl != null) {
            ISyncContext.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static ISyncContext getDefaultImpl() {
         return ISyncContext.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements ISyncContext {
         private IBinder mRemote;
         public static ISyncContext sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return "android.content.ISyncContext";
         }

         public void sendHeartbeat() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("android.content.ISyncContext");
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (!_status && ISyncContext.Stub.getDefaultImpl() != null) {
                  ISyncContext.Stub.getDefaultImpl().sendHeartbeat();
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void onFinished(SyncResult result) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("android.content.ISyncContext");
               if (result != null) {
                  _data.writeInt(1);
                  result.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (_status || ISyncContext.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               ISyncContext.Stub.getDefaultImpl().onFinished(result);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }
      }
   }

   public static class Default implements ISyncContext {
      public void sendHeartbeat() throws RemoteException {
      }

      public void onFinished(SyncResult result) throws RemoteException {
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
