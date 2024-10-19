package android.accounts;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface IAccountAuthenticator extends IInterface {
   void addAccount(IAccountAuthenticatorResponse var1, String var2, String var3, String[] var4, Bundle var5) throws RemoteException;

   void confirmCredentials(IAccountAuthenticatorResponse var1, Account var2, Bundle var3) throws RemoteException;

   void getAuthToken(IAccountAuthenticatorResponse var1, Account var2, String var3, Bundle var4) throws RemoteException;

   void getAuthTokenLabel(IAccountAuthenticatorResponse var1, String var2) throws RemoteException;

   void updateCredentials(IAccountAuthenticatorResponse var1, Account var2, String var3, Bundle var4) throws RemoteException;

   void editProperties(IAccountAuthenticatorResponse var1, String var2) throws RemoteException;

   void hasFeatures(IAccountAuthenticatorResponse var1, Account var2, String[] var3) throws RemoteException;

   void getAccountRemovalAllowed(IAccountAuthenticatorResponse var1, Account var2) throws RemoteException;

   void getAccountCredentialsForCloning(IAccountAuthenticatorResponse var1, Account var2) throws RemoteException;

   void addAccountFromCredentials(IAccountAuthenticatorResponse var1, Account var2, Bundle var3) throws RemoteException;

   public abstract static class Stub extends Binder implements IAccountAuthenticator {
      private static final String DESCRIPTOR = StringFog.decrypt("EgsWBAoHO10CDBEfHAEaAEs7NwYNMAYNGzMFHQcLHREbFQQaMAE=");
      static final int TRANSACTION_addAccount = 1;
      static final int TRANSACTION_confirmCredentials = 2;
      static final int TRANSACTION_getAuthToken = 3;
      static final int TRANSACTION_getAuthTokenLabel = 4;
      static final int TRANSACTION_updateCredentials = 5;
      static final int TRANSACTION_editProperties = 6;
      static final int TRANSACTION_hasFeatures = 7;
      static final int TRANSACTION_getAccountRemovalAllowed = 8;
      static final int TRANSACTION_getAccountCredentialsForCloning = 9;
      static final int TRANSACTION_addAccountFromCredentials = 10;

      public Stub() {
         this.attachInterface(this, StringFog.decrypt("EgsWBAoHO10CDBEfHAEaAEs7NwYNMAYNGzMFHQcLHREbFQQaMAE="));
      }

      public static IAccountAuthenticator asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IAccountAuthenticator)(iin != null && iin instanceof IAccountAuthenticator ? (IAccountAuthenticator)iin : new Proxy(obj));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
         String descriptor = DESCRIPTOR;
         IAccountAuthenticatorResponse _arg0;
         Account _arg1;
         Bundle _arg2;
         Bundle _arg3;
         String _arg2;
         String _arg1;
         switch (code) {
            case 1:
               data.enforceInterface(descriptor);
               _arg0 = IAccountAuthenticatorResponse.Stub.asInterface(data.readStrongBinder());
               _arg1 = data.readString();
               _arg2 = data.readString();
               String[] _arg3 = data.createStringArray();
               Bundle _arg4;
               if (0 != data.readInt()) {
                  _arg4 = (Bundle)Bundle.CREATOR.createFromParcel(data);
               } else {
                  _arg4 = null;
               }

               this.addAccount(_arg0, _arg1, _arg2, _arg3, _arg4);
               reply.writeNoException();
               return true;
            case 2:
               data.enforceInterface(descriptor);
               _arg0 = IAccountAuthenticatorResponse.Stub.asInterface(data.readStrongBinder());
               if (0 != data.readInt()) {
                  _arg1 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  _arg1 = null;
               }

               if (0 != data.readInt()) {
                  _arg2 = (Bundle)Bundle.CREATOR.createFromParcel(data);
               } else {
                  _arg2 = null;
               }

               this.confirmCredentials(_arg0, _arg1, _arg2);
               reply.writeNoException();
               return true;
            case 3:
               data.enforceInterface(descriptor);
               _arg0 = IAccountAuthenticatorResponse.Stub.asInterface(data.readStrongBinder());
               if (0 != data.readInt()) {
                  _arg1 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  _arg1 = null;
               }

               _arg2 = data.readString();
               if (0 != data.readInt()) {
                  _arg3 = (Bundle)Bundle.CREATOR.createFromParcel(data);
               } else {
                  _arg3 = null;
               }

               this.getAuthToken(_arg0, _arg1, _arg2, _arg3);
               reply.writeNoException();
               return true;
            case 4:
               data.enforceInterface(descriptor);
               _arg0 = IAccountAuthenticatorResponse.Stub.asInterface(data.readStrongBinder());
               _arg1 = data.readString();
               this.getAuthTokenLabel(_arg0, _arg1);
               reply.writeNoException();
               return true;
            case 5:
               data.enforceInterface(descriptor);
               _arg0 = IAccountAuthenticatorResponse.Stub.asInterface(data.readStrongBinder());
               if (0 != data.readInt()) {
                  _arg1 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  _arg1 = null;
               }

               _arg2 = data.readString();
               if (0 != data.readInt()) {
                  _arg3 = (Bundle)Bundle.CREATOR.createFromParcel(data);
               } else {
                  _arg3 = null;
               }

               this.updateCredentials(_arg0, _arg1, _arg2, _arg3);
               reply.writeNoException();
               return true;
            case 6:
               data.enforceInterface(descriptor);
               _arg0 = IAccountAuthenticatorResponse.Stub.asInterface(data.readStrongBinder());
               _arg1 = data.readString();
               this.editProperties(_arg0, _arg1);
               reply.writeNoException();
               return true;
            case 7:
               data.enforceInterface(descriptor);
               _arg0 = IAccountAuthenticatorResponse.Stub.asInterface(data.readStrongBinder());
               if (0 != data.readInt()) {
                  _arg1 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  _arg1 = null;
               }

               String[] _arg2 = data.createStringArray();
               this.hasFeatures(_arg0, _arg1, _arg2);
               reply.writeNoException();
               return true;
            case 8:
               data.enforceInterface(descriptor);
               _arg0 = IAccountAuthenticatorResponse.Stub.asInterface(data.readStrongBinder());
               if (0 != data.readInt()) {
                  _arg1 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  _arg1 = null;
               }

               this.getAccountRemovalAllowed(_arg0, _arg1);
               reply.writeNoException();
               return true;
            case 9:
               data.enforceInterface(descriptor);
               _arg0 = IAccountAuthenticatorResponse.Stub.asInterface(data.readStrongBinder());
               if (0 != data.readInt()) {
                  _arg1 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  _arg1 = null;
               }

               this.getAccountCredentialsForCloning(_arg0, _arg1);
               reply.writeNoException();
               return true;
            case 10:
               data.enforceInterface(descriptor);
               _arg0 = IAccountAuthenticatorResponse.Stub.asInterface(data.readStrongBinder());
               if (0 != data.readInt()) {
                  _arg1 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  _arg1 = null;
               }

               if (0 != data.readInt()) {
                  _arg2 = (Bundle)Bundle.CREATOR.createFromParcel(data);
               } else {
                  _arg2 = null;
               }

               this.addAccountFromCredentials(_arg0, _arg1, _arg2);
               reply.writeNoException();
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(IAccountAuthenticator impl) {
         if (IAccountAuthenticator.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException(StringFog.decrypt("AAAGMgAIPgYPGzsdGQNGWkURFwkCOhdDGwUZCgo="));
         } else if (impl != null) {
            IAccountAuthenticator.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IAccountAuthenticator getDefaultImpl() {
         return IAccountAuthenticator.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IAccountAuthenticator {
         private IBinder mRemote;
         public static IAccountAuthenticator sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return StringFog.decrypt("EgsWBAoHO10CDBEfHAEaAEs7NwYNMAYNGzMFHQcLHREbFQQaMAE=");
         }

         public void addAccount(IAccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt("EgsWBAoHO10CDBEfHAEaAEs7NwYNMAYNGzMFHQcLHREbFQQaMAE="));
               _data.writeStrongBinder(response != null ? response.asBinder() : null);
               _data.writeString(accountType);
               _data.writeString(authTokenType);
               _data.writeStringArray(requiredFeatures);
               if (options != null) {
                  _data.writeInt(1);
                  options.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (!_status && IAccountAuthenticator.Stub.getDefaultImpl() != null) {
                  IAccountAuthenticator.Stub.getDefaultImpl().addAccount(response, accountType, authTokenType, requiredFeatures, options);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void confirmCredentials(IAccountAuthenticatorResponse response, Account account, Bundle options) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt("EgsWBAoHO10CDBEfHAEaAEs7NwYNMAYNGzMFHQcLHREbFQQaMAE="));
               _data.writeStrongBinder(response != null ? response.asBinder() : null);
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               if (options != null) {
                  _data.writeInt(1);
                  options.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (!_status && IAccountAuthenticator.Stub.getDefaultImpl() != null) {
                  IAccountAuthenticator.Stub.getDefaultImpl().confirmCredentials(response, account, options);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void getAuthToken(IAccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt("EgsWBAoHO10CDBEfHAEaAEs7NwYNMAYNGzMFHQcLHREbFQQaMAE="));
               _data.writeStrongBinder(response != null ? response.asBinder() : null);
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(authTokenType);
               if (options != null) {
                  _data.writeInt(1);
                  options.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(3, _data, _reply, 0);
               if (!_status && IAccountAuthenticator.Stub.getDefaultImpl() != null) {
                  IAccountAuthenticator.Stub.getDefaultImpl().getAuthToken(response, account, authTokenType, options);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void getAuthTokenLabel(IAccountAuthenticatorResponse response, String authTokenType) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt("EgsWBAoHO10CDBEfHAEaAEs7NwYNMAYNGzMFHQcLHREbFQQaMAE="));
               _data.writeStrongBinder(response != null ? response.asBinder() : null);
               _data.writeString(authTokenType);
               boolean _status = this.mRemote.transact(4, _data, _reply, 0);
               if (_status || IAccountAuthenticator.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IAccountAuthenticator.Stub.getDefaultImpl().getAuthTokenLabel(response, authTokenType);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void updateCredentials(IAccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt("EgsWBAoHO10CDBEfHAEaAEs7NwYNMAYNGzMFHQcLHREbFQQaMAE="));
               _data.writeStrongBinder(response != null ? response.asBinder() : null);
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(authTokenType);
               if (options != null) {
                  _data.writeInt(1);
                  options.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(5, _data, _reply, 0);
               if (_status || IAccountAuthenticator.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IAccountAuthenticator.Stub.getDefaultImpl().updateCredentials(response, account, authTokenType, options);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void editProperties(IAccountAuthenticatorResponse response, String accountType) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt("EgsWBAoHO10CDBEfHAEaAEs7NwYNMAYNGzMFHQcLHREbFQQaMAE="));
               _data.writeStrongBinder(response != null ? response.asBinder() : null);
               _data.writeString(accountType);
               boolean _status = this.mRemote.transact(6, _data, _reply, 0);
               if (_status || IAccountAuthenticator.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IAccountAuthenticator.Stub.getDefaultImpl().editProperties(response, accountType);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void hasFeatures(IAccountAuthenticatorResponse response, Account account, String[] features) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt("EgsWBAoHO10CDBEfHAEaAEs7NwYNMAYNGzMFHQcLHREbFQQaMAE="));
               _data.writeStrongBinder(response != null ? response.asBinder() : null);
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeStringArray(features);
               boolean _status = this.mRemote.transact(7, _data, _reply, 0);
               if (!_status && IAccountAuthenticator.Stub.getDefaultImpl() != null) {
                  IAccountAuthenticator.Stub.getDefaultImpl().hasFeatures(response, account, features);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void getAccountRemovalAllowed(IAccountAuthenticatorResponse response, Account account) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt("EgsWBAoHO10CDBEfHAEaAEs7NwYNMAYNGzMFHQcLHREbFQQaMAE="));
               _data.writeStrongBinder(response != null ? response.asBinder() : null);
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(8, _data, _reply, 0);
               if (!_status && IAccountAuthenticator.Stub.getDefaultImpl() != null) {
                  IAccountAuthenticator.Stub.getDefaultImpl().getAccountRemovalAllowed(response, account);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void getAccountCredentialsForCloning(IAccountAuthenticatorResponse response, Account account) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt("EgsWBAoHO10CDBEfHAEaAEs7NwYNMAYNGzMFHQcLHREbFQQaMAE="));
               _data.writeStrongBinder(response != null ? response.asBinder() : null);
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(9, _data, _reply, 0);
               if (!_status && IAccountAuthenticator.Stub.getDefaultImpl() != null) {
                  IAccountAuthenticator.Stub.getDefaultImpl().getAccountCredentialsForCloning(response, account);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void addAccountFromCredentials(IAccountAuthenticatorResponse response, Account account, Bundle accountCredentials) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt("EgsWBAoHO10CDBEfHAEaAEs7NwYNMAYNGzMFHQcLHREbFQQaMAE="));
               _data.writeStrongBinder(response != null ? response.asBinder() : null);
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               if (accountCredentials != null) {
                  _data.writeInt(1);
                  accountCredentials.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(10, _data, _reply, 0);
               if (_status || IAccountAuthenticator.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IAccountAuthenticator.Stub.getDefaultImpl().addAccountFromCredentials(response, account, accountCredentials);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }
      }
   }

   public static class Default implements IAccountAuthenticator {
      public void addAccount(IAccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws RemoteException {
      }

      public void confirmCredentials(IAccountAuthenticatorResponse response, Account account, Bundle options) throws RemoteException {
      }

      public void getAuthToken(IAccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws RemoteException {
      }

      public void getAuthTokenLabel(IAccountAuthenticatorResponse response, String authTokenType) throws RemoteException {
      }

      public void updateCredentials(IAccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws RemoteException {
      }

      public void editProperties(IAccountAuthenticatorResponse response, String accountType) throws RemoteException {
      }

      public void hasFeatures(IAccountAuthenticatorResponse response, Account account, String[] features) throws RemoteException {
      }

      public void getAccountRemovalAllowed(IAccountAuthenticatorResponse response, Account account) throws RemoteException {
      }

      public void getAccountCredentialsForCloning(IAccountAuthenticatorResponse response, Account account) throws RemoteException {
      }

      public void addAccountFromCredentials(IAccountAuthenticatorResponse response, Account account, Bundle accountCredentials) throws RemoteException {
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
