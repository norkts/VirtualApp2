package android.os;

import com.lody.virtual.StringFog;

public interface ISystemUpdateManager extends IInterface {
   Bundle retrieveSystemUpdateInfo() throws RemoteException;

   void updateSystemUpdateInfo(PersistableBundle var1) throws RemoteException;

   public abstract static class Stub extends Binder implements ISystemUpdateManager {
      private static final String DESCRIPTOR = StringFog.decrypt("EgsWBAoHO10MHFw5OhYdBwAfIxUKPgcGIhMeCAgLAQ==");
      static final int TRANSACTION_retrieveSystemUpdateInfo = 1;
      static final int TRANSACTION_updateSystemUpdateInfo = 2;

      public Stub() {
         this.attachInterface(this, StringFog.decrypt("EgsWBAoHO10MHFw5OhYdBwAfIxUKPgcGIhMeCAgLAQ=="));
      }

      public static ISystemUpdateManager asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (ISystemUpdateManager)(iin != null && iin instanceof ISystemUpdateManager ? (ISystemUpdateManager)iin : new Proxy(obj));
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
               Bundle _result = this.retrieveSystemUpdateInfo();
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
               PersistableBundle _arg0;
               if (0 != data.readInt()) {
                  _arg0 = (PersistableBundle)PersistableBundle.CREATOR.createFromParcel(data);
               } else {
                  _arg0 = null;
               }

               this.updateSystemUpdateInfo(_arg0);
               reply.writeNoException();
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(ISystemUpdateManager impl) {
         if (ISystemUpdateManager.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException(StringFog.decrypt("AAAGMgAIPgYPGzsdGQNGWkURFwkCOhdDGwUZCgo="));
         } else if (impl != null) {
            ISystemUpdateManager.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static ISystemUpdateManager getDefaultImpl() {
         return ISystemUpdateManager.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements ISystemUpdateManager {
         private IBinder mRemote;
         public static ISystemUpdateManager sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return StringFog.decrypt("EgsWBAoHO10MHFw5OhYdBwAfIxUKPgcGIhMeCAgLAQ==");
         }

         public Bundle retrieveSystemUpdateInfo() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            Bundle _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt("EgsWBAoHO10MHFw5OhYdBwAfIxUKPgcGIhMeCAgLAQ=="));
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (!_status && ISystemUpdateManager.Stub.getDefaultImpl() != null) {
                  Bundle var5 = ISystemUpdateManager.Stub.getDefaultImpl().retrieveSystemUpdateInfo();
                  return var5;
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

         public void updateSystemUpdateInfo(PersistableBundle data) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt("EgsWBAoHO10MHFw5OhYdBwAfIxUKPgcGIhMeCAgLAQ=="));
               if (data != null) {
                  _data.writeInt(1);
                  data.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (_status || ISystemUpdateManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               ISystemUpdateManager.Stub.getDefaultImpl().updateSystemUpdateInfo(data);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }
      }
   }

   public static class Default implements ISystemUpdateManager {
      public Bundle retrieveSystemUpdateInfo() throws RemoteException {
         return null;
      }

      public void updateSystemUpdateInfo(PersistableBundle data) throws RemoteException {
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
