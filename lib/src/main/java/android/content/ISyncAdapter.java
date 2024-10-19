package android.content;

import android.accounts.Account;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface ISyncAdapter extends IInterface {
   void startSync(ISyncContext var1, String var2, Account var3, Bundle var4) throws RemoteException;

   void cancelSync(ISyncContext var1) throws RemoteException;

   void initialize(Account var1, String var2) throws RemoteException;

   public abstract static class Stub extends Binder implements ISyncAdapter {
      private static final String DESCRIPTOR = StringFog.decrypt("EgsWBAoHO10AABwEDAEaXSwhDwsNHhcCHwYVGw==");
      static final int TRANSACTION_startSync = 1;
      static final int TRANSACTION_cancelSync = 2;
      static final int TRANSACTION_initialize = 3;

      public Stub() {
         this.attachInterface(this, StringFog.decrypt("EgsWBAoHO10AABwEDAEaXSwhDwsNHhcCHwYVGw=="));
      }

      public static ISyncAdapter asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (ISyncAdapter)(iin != null && iin instanceof ISyncAdapter ? (ISyncAdapter)iin : new Proxy(obj));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
         String descriptor = DESCRIPTOR;
         String _arg1;
         ISyncContext _arg0;
         switch (code) {
            case 1:
               data.enforceInterface(descriptor);
               _arg0 = ISyncContext.Stub.asInterface(data.readStrongBinder());
               _arg1 = data.readString();
               Account _arg2;
               if (0 != data.readInt()) {
                  _arg2 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  _arg2 = null;
               }

               Bundle _arg3;
               if (0 != data.readInt()) {
                  _arg3 = (Bundle)Bundle.CREATOR.createFromParcel(data);
               } else {
                  _arg3 = null;
               }

               this.startSync(_arg0, _arg1, _arg2, _arg3);
               reply.writeNoException();
               return true;
            case 2:
               data.enforceInterface(descriptor);
               _arg0 = ISyncContext.Stub.asInterface(data.readStrongBinder());
               this.cancelSync(_arg0);
               reply.writeNoException();
               return true;
            case 3:
               data.enforceInterface(descriptor);
               Account acc_arg0;
               if (0 != data.readInt()) {
                  acc_arg0 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  acc_arg0 = null;
               }

               _arg1 = data.readString();
               this.initialize(acc_arg0, _arg1);
               reply.writeNoException();
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(ISyncAdapter impl) {
         if (ISyncAdapter.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException(StringFog.decrypt("AAAGMgAIPgYPGzsdGQNGWkURFwkCOhdDGwUZCgo="));
         } else if (impl != null) {
            ISyncAdapter.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static ISyncAdapter getDefaultImpl() {
         return ISyncAdapter.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements ISyncAdapter {
         private IBinder mRemote;
         public static ISyncAdapter sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return StringFog.decrypt("EgsWBAoHO10AABwEDAEaXSwhDwsNHhcCHwYVGw==");
         }

         public void startSync(ISyncContext syncContext, String authority, Account account, Bundle extras) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt("EgsWBAoHO10AABwEDAEaXSwhDwsNHhcCHwYVGw=="));
               _data.writeStrongBinder(syncContext != null ? syncContext.asBinder() : null);
               _data.writeString(authority);
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               if (extras != null) {
                  _data.writeInt(1);
                  extras.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (!_status && ISyncAdapter.Stub.getDefaultImpl() != null) {
                  ISyncAdapter.Stub.getDefaultImpl().startSync(syncContext, authority, account, extras);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void cancelSync(ISyncContext syncContext) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt("EgsWBAoHO10AABwEDAEaXSwhDwsNHhcCHwYVGw=="));
               _data.writeStrongBinder(syncContext != null ? syncContext.asBinder() : null);
               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (_status || ISyncAdapter.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               ISyncAdapter.Stub.getDefaultImpl().cancelSync(syncContext);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void initialize(Account account, String authority) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt("EgsWBAoHO10AABwEDAEaXSwhDwsNHhcCHwYVGw=="));
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(authority);
               boolean _status = this.mRemote.transact(3, _data, _reply, 0);
               if (!_status && ISyncAdapter.Stub.getDefaultImpl() != null) {
                  ISyncAdapter.Stub.getDefaultImpl().initialize(account, authority);
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

   public static class Default implements ISyncAdapter {
      public void startSync(ISyncContext syncContext, String authority, Account account, Bundle extras) throws RemoteException {
      }

      public void cancelSync(ISyncContext syncContext) throws RemoteException {
      }

      public void initialize(Account account, String authority) throws RemoteException {
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
