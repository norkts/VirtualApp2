package com.lody.virtual.server.interfaces;

import android.accounts.Account;
import android.accounts.AuthenticatorDescription;
import android.accounts.IAccountManagerResponse;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import java.util.HashMap;
import java.util.Map;

public interface IAccountManager extends IInterface {
   AuthenticatorDescription[] getAuthenticatorTypes(int var1) throws RemoteException;

   void getAccountsByFeatures(int var1, IAccountManagerResponse var2, String var3, String[] var4) throws RemoteException;

   String getPreviousName(int var1, Account var2) throws RemoteException;

   Account[] getAccounts(int var1, String var2) throws RemoteException;

   void getAuthToken(int var1, IAccountManagerResponse var2, Account var3, String var4, boolean var5, boolean var6, Bundle var7) throws RemoteException;

   void setPassword(int var1, Account var2, String var3) throws RemoteException;

   void setAuthToken(int var1, Account var2, String var3, String var4) throws RemoteException;

   void setUserData(int var1, Account var2, String var3, String var4) throws RemoteException;

   void hasFeatures(int var1, IAccountManagerResponse var2, Account var3, String[] var4) throws RemoteException;

   void updateCredentials(int var1, IAccountManagerResponse var2, Account var3, String var4, boolean var5, Bundle var6) throws RemoteException;

   void editProperties(int var1, IAccountManagerResponse var2, String var3, boolean var4) throws RemoteException;

   void getAuthTokenLabel(int var1, IAccountManagerResponse var2, String var3, String var4) throws RemoteException;

   String getUserData(int var1, Account var2, String var3) throws RemoteException;

   String getPassword(int var1, Account var2) throws RemoteException;

   void confirmCredentials(int var1, IAccountManagerResponse var2, Account var3, Bundle var4, boolean var5) throws RemoteException;

   void addAccount(int var1, IAccountManagerResponse var2, String var3, String var4, String[] var5, boolean var6, Bundle var7) throws RemoteException;

   boolean addAccountExplicitly(int var1, Account var2, String var3, Bundle var4) throws RemoteException;

   boolean removeAccountExplicitly(int var1, Account var2) throws RemoteException;

   void renameAccount(int var1, IAccountManagerResponse var2, Account var3, String var4) throws RemoteException;

   void removeAccount(int var1, IAccountManagerResponse var2, Account var3, boolean var4) throws RemoteException;

   void clearPassword(int var1, Account var2) throws RemoteException;

   boolean accountAuthenticated(int var1, Account var2) throws RemoteException;

   void invalidateAuthToken(int var1, String var2, String var3) throws RemoteException;

   String peekAuthToken(int var1, Account var2, String var3) throws RemoteException;

   boolean setAccountVisibility(int var1, Account var2, String var3, int var4) throws RemoteException;

   int getAccountVisibility(int var1, Account var2, String var3) throws RemoteException;

   void startAddAccountSession(IAccountManagerResponse var1, String var2, String var3, String[] var4, boolean var5, Bundle var6) throws RemoteException;

   void startUpdateCredentialsSession(IAccountManagerResponse var1, Account var2, String var3, boolean var4, Bundle var5) throws RemoteException;

   void registerAccountListener(String[] var1) throws RemoteException;

   void unregisterAccountListener(String[] var1) throws RemoteException;

   Map getPackagesAndVisibilityForAccount(int var1, Account var2) throws RemoteException;

   Map getAccountsAndVisibilityForPackage(int var1, String var2, String var3) throws RemoteException;

   void finishSessionAsUser(IAccountManagerResponse var1, Bundle var2, boolean var3, Bundle var4, int var5) throws RemoteException;

   void isCredentialsUpdateSuggested(IAccountManagerResponse var1, Account var2, String var3) throws RemoteException;

   boolean addAccountExplicitlyWithVisibility(int var1, Account var2, String var3, Bundle var4, Map var5) throws RemoteException;

   public abstract static class Stub extends Binder implements IAccountManager {
      private static final String DESCRIPTOR = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuKmwjFgJlNSAoIz4+PWgaFlo="));
      static final int TRANSACTION_getAuthenticatorTypes = 1;
      static final int TRANSACTION_getAccountsByFeatures = 2;
      static final int TRANSACTION_getPreviousName = 3;
      static final int TRANSACTION_getAccounts = 4;
      static final int TRANSACTION_getAuthToken = 5;
      static final int TRANSACTION_setPassword = 6;
      static final int TRANSACTION_setAuthToken = 7;
      static final int TRANSACTION_setUserData = 8;
      static final int TRANSACTION_hasFeatures = 9;
      static final int TRANSACTION_updateCredentials = 10;
      static final int TRANSACTION_editProperties = 11;
      static final int TRANSACTION_getAuthTokenLabel = 12;
      static final int TRANSACTION_getUserData = 13;
      static final int TRANSACTION_getPassword = 14;
      static final int TRANSACTION_confirmCredentials = 15;
      static final int TRANSACTION_addAccount = 16;
      static final int TRANSACTION_addAccountExplicitly = 17;
      static final int TRANSACTION_removeAccountExplicitly = 18;
      static final int TRANSACTION_renameAccount = 19;
      static final int TRANSACTION_removeAccount = 20;
      static final int TRANSACTION_clearPassword = 21;
      static final int TRANSACTION_accountAuthenticated = 22;
      static final int TRANSACTION_invalidateAuthToken = 23;
      static final int TRANSACTION_peekAuthToken = 24;
      static final int TRANSACTION_setAccountVisibility = 25;
      static final int TRANSACTION_getAccountVisibility = 26;
      static final int TRANSACTION_startAddAccountSession = 27;
      static final int TRANSACTION_startUpdateCredentialsSession = 28;
      static final int TRANSACTION_registerAccountListener = 29;
      static final int TRANSACTION_unregisterAccountListener = 30;
      static final int TRANSACTION_getPackagesAndVisibilityForAccount = 31;
      static final int TRANSACTION_getAccountsAndVisibilityForPackage = 32;
      static final int TRANSACTION_finishSessionAsUser = 33;
      static final int TRANSACTION_isCredentialsUpdateSuggested = 34;
      static final int TRANSACTION_addAccountExplicitlyWithVisibility = 35;

      public Stub() {
         this.attachInterface(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuKmwjFgJlNSAoIz4+PWgaFlo=")));
      }

      public static IAccountManager asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IAccountManager)(iin != null && iin instanceof IAccountManager ? (IAccountManager)iin : new Proxy(obj));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
         String descriptor = DESCRIPTOR;
         int _arg0;
         Account _arg1;
         String _arg2;
         Bundle _arg3;
         IAccountManagerResponse ia_arg0;
         String[] sarr_arg0;
         boolean b_arg2;
         String s_arg1;
         Bundle bundle_arg5;
         boolean b_arg3;
         String[] sarr_arg3;
         boolean b_result;
         Account account_arg2;
         int _result;
         boolean _arg5;
         Bundle _arg6;
         String s_arg3;
         IAccountManagerResponse ia_arg1;
         switch (code) {
            case 1:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               AuthenticatorDescription[] authenticatorDescriptions_result = this.getAuthenticatorTypes(_arg0);
               reply.writeNoException();
               reply.writeTypedArray(authenticatorDescriptions_result, 1);
               return true;
            case 2:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               ia_arg1 = IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
               _arg2 = data.readString();
               sarr_arg3 = data.createStringArray();
               this.getAccountsByFeatures(_arg0, ia_arg1, _arg2, sarr_arg3);
               reply.writeNoException();
               return true;
            case 3:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               if (0 != data.readInt()) {
                  _arg1 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  _arg1 = null;
               }

               _arg2 = this.getPreviousName(_arg0, _arg1);
               reply.writeNoException();
               reply.writeString(_arg2);
               return true;
            case 4:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               s_arg1 = data.readString();
               Account[] accounts_result = this.getAccounts(_arg0, s_arg1);
               reply.writeNoException();
               reply.writeTypedArray(accounts_result, 1);
               return true;
            case 5:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               ia_arg1 = IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
               if (0 != data.readInt()) {
                  account_arg2 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  account_arg2 = null;
               }

               s_arg3 = data.readString();
               b_result = 0 != data.readInt();
               _arg5 = 0 != data.readInt();
               if (0 != data.readInt()) {
                  _arg6 = (Bundle)Bundle.CREATOR.createFromParcel(data);
               } else {
                  _arg6 = null;
               }

               this.getAuthToken(_arg0, ia_arg1, account_arg2, s_arg3, b_result, _arg5, _arg6);
               reply.writeNoException();
               return true;
            case 6:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               if (0 != data.readInt()) {
                  _arg1 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  _arg1 = null;
               }

               _arg2 = data.readString();
               this.setPassword(_arg0, _arg1, _arg2);
               reply.writeNoException();
               return true;
            case 7:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               if (0 != data.readInt()) {
                  _arg1 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  _arg1 = null;
               }

               _arg2 = data.readString();
               s_arg3 = data.readString();
               this.setAuthToken(_arg0, _arg1, _arg2, s_arg3);
               reply.writeNoException();
               return true;
            case 8:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               if (0 != data.readInt()) {
                  _arg1 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  _arg1 = null;
               }

               _arg2 = data.readString();
               s_arg3 = data.readString();
               this.setUserData(_arg0, _arg1, _arg2, s_arg3);
               reply.writeNoException();
               return true;
            case 9:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               ia_arg1 = IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
               if (0 != data.readInt()) {
                  account_arg2 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  account_arg2 = null;
               }

               sarr_arg3 = data.createStringArray();
               this.hasFeatures(_arg0, ia_arg1, account_arg2, sarr_arg3);
               reply.writeNoException();
               return true;
            case 10:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               ia_arg1 = IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
               if (0 != data.readInt()) {
                  account_arg2 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  account_arg2 = null;
               }

               s_arg3 = data.readString();
               b_result = 0 != data.readInt();
               if (0 != data.readInt()) {
                  bundle_arg5 = (Bundle)Bundle.CREATOR.createFromParcel(data);
               } else {
                  bundle_arg5 = null;
               }

               this.updateCredentials(_arg0, ia_arg1, account_arg2, s_arg3, b_result, bundle_arg5);
               reply.writeNoException();
               return true;
            case 11:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               ia_arg1 = IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
               _arg2 = data.readString();
               b_arg3 = 0 != data.readInt();
               this.editProperties(_arg0, ia_arg1, _arg2, b_arg3);
               reply.writeNoException();
               return true;
            case 12:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               ia_arg1 = IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
               _arg2 = data.readString();
               s_arg3 = data.readString();
               this.getAuthTokenLabel(_arg0, ia_arg1, _arg2, s_arg3);
               reply.writeNoException();
               return true;
            case 13:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               if (0 != data.readInt()) {
                  _arg1 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  _arg1 = null;
               }

               _arg2 = data.readString();
               s_arg3 = this.getUserData(_arg0, _arg1, _arg2);
               reply.writeNoException();
               reply.writeString(s_arg3);
               return true;
            case 14:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               if (0 != data.readInt()) {
                  _arg1 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  _arg1 = null;
               }

               _arg2 = this.getPassword(_arg0, _arg1);
               reply.writeNoException();
               reply.writeString(_arg2);
               return true;
            case 15:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               ia_arg1 = IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
               if (0 != data.readInt()) {
                  account_arg2 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  account_arg2 = null;
               }

               if (0 != data.readInt()) {
                  _arg3 = (Bundle)Bundle.CREATOR.createFromParcel(data);
               } else {
                  _arg3 = null;
               }

               b_result = 0 != data.readInt();
               this.confirmCredentials(_arg0, ia_arg1, account_arg2, _arg3, b_result);
               reply.writeNoException();
               return true;
            case 16:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               ia_arg1 = IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
               _arg2 = data.readString();
               s_arg3 = data.readString();
               String[] _arg4 = data.createStringArray();
               _arg5 = 0 != data.readInt();
               if (0 != data.readInt()) {
                  _arg6 = (Bundle)Bundle.CREATOR.createFromParcel(data);
               } else {
                  _arg6 = null;
               }

               this.addAccount(_arg0, ia_arg1, _arg2, s_arg3, _arg4, _arg5, _arg6);
               reply.writeNoException();
               return true;
            case 17:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
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

               b_result = this.addAccountExplicitly(_arg0, _arg1, _arg2, _arg3);
               reply.writeNoException();
               reply.writeInt(b_result ? 1 : 0);
               return true;
            case 18:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               if (0 != data.readInt()) {
                  _arg1 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  _arg1 = null;
               }

               b_arg2 = this.removeAccountExplicitly(_arg0, _arg1);
               reply.writeNoException();
               reply.writeInt(b_arg2 ? 1 : 0);
               return true;
            case 19:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               ia_arg1 = IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
               if (0 != data.readInt()) {
                  account_arg2 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  account_arg2 = null;
               }

               s_arg3 = data.readString();
               this.renameAccount(_arg0, ia_arg1, account_arg2, s_arg3);
               reply.writeNoException();
               return true;
            case 20:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               ia_arg1 = IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
               if (0 != data.readInt()) {
                  account_arg2 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  account_arg2 = null;
               }

               b_arg3 = 0 != data.readInt();
               this.removeAccount(_arg0, ia_arg1, account_arg2, b_arg3);
               reply.writeNoException();
               return true;
            case 21:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               if (0 != data.readInt()) {
                  _arg1 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  _arg1 = null;
               }

               this.clearPassword(_arg0, _arg1);
               reply.writeNoException();
               return true;
            case 22:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               if (0 != data.readInt()) {
                  _arg1 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  _arg1 = null;
               }

               b_arg2 = this.accountAuthenticated(_arg0, _arg1);
               reply.writeNoException();
               reply.writeInt(b_arg2 ? 1 : 0);
               return true;
            case 23:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               s_arg1 = data.readString();
               _arg2 = data.readString();
               this.invalidateAuthToken(_arg0, s_arg1, _arg2);
               reply.writeNoException();
               return true;
            case 24:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               if (0 != data.readInt()) {
                  _arg1 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  _arg1 = null;
               }

               _arg2 = data.readString();
               s_arg3 = this.peekAuthToken(_arg0, _arg1, _arg2);
               reply.writeNoException();
               reply.writeString(s_arg3);
               return true;
            case 25:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               if (0 != data.readInt()) {
                  _arg1 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  _arg1 = null;
               }

               _arg2 = data.readString();
               _result = data.readInt();
               b_result = this.setAccountVisibility(_arg0, _arg1, _arg2, _result);
               reply.writeNoException();
               reply.writeInt(b_result ? 1 : 0);
               return true;
            case 26:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               if (0 != data.readInt()) {
                  _arg1 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  _arg1 = null;
               }

               _arg2 = data.readString();
               _result = this.getAccountVisibility(_arg0, _arg1, _arg2);
               reply.writeNoException();
               reply.writeInt(_result);
               return true;
            case 27:
               data.enforceInterface(descriptor);
               ia_arg0 = IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
               s_arg1 = data.readString();
               String s_arg2 = data.readString();
               sarr_arg3 = data.createStringArray();
               b_result = 0 != data.readInt();
               if (0 != data.readInt()) {
                  bundle_arg5 = (Bundle)Bundle.CREATOR.createFromParcel(data);
               } else {
                  bundle_arg5 = null;
               }

               this.startAddAccountSession(ia_arg0, s_arg1, s_arg2, sarr_arg3, b_result, bundle_arg5);
               reply.writeNoException();
               return true;
            case 28:
               data.enforceInterface(descriptor);
               ia_arg0 = IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
               if (0 != data.readInt()) {
                  _arg1 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  _arg1 = null;
               }

               _arg2 = data.readString();
               b_arg3 = 0 != data.readInt();
               Bundle bundle_arg4;
               if (0 != data.readInt()) {
                  bundle_arg4 = (Bundle)Bundle.CREATOR.createFromParcel(data);
               } else {
                  bundle_arg4 = null;
               }

               this.startUpdateCredentialsSession(ia_arg0, _arg1, _arg2, b_arg3, bundle_arg4);
               reply.writeNoException();
               return true;
            case 29:
               data.enforceInterface(descriptor);
               sarr_arg0 = data.createStringArray();
               this.registerAccountListener(sarr_arg0);
               reply.writeNoException();
               return true;
            case 30:
               data.enforceInterface(descriptor);
               sarr_arg0 = data.createStringArray();
               this.unregisterAccountListener(sarr_arg0);
               reply.writeNoException();
               return true;
            case 31:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               if (0 != data.readInt()) {
                  _arg1 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  _arg1 = null;
               }

               Map map_result = this.getPackagesAndVisibilityForAccount(_arg0, _arg1);
               reply.writeNoException();
               reply.writeMap(map_result);
               return true;
            case 32:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               s_arg1 = data.readString();
               _arg2 = data.readString();
               map_result = this.getAccountsAndVisibilityForPackage(_arg0, s_arg1, _arg2);
               reply.writeNoException();
               reply.writeMap(map_result);
               return true;
            case 33:
               data.enforceInterface(descriptor);
               ia_arg0 = IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
               Bundle bundle_arg1;
               if (0 != data.readInt()) {
                  bundle_arg1 = (Bundle)Bundle.CREATOR.createFromParcel(data);
               } else {
                  bundle_arg1 = null;
               }

               b_arg2 = 0 != data.readInt();
               if (0 != data.readInt()) {
                  _arg3 = (Bundle)Bundle.CREATOR.createFromParcel(data);
               } else {
                  _arg3 = null;
               }

               int i_arg4 = data.readInt();
               this.finishSessionAsUser(ia_arg0, bundle_arg1, b_arg2, _arg3, i_arg4);
               reply.writeNoException();
               return true;
            case 34:
               data.enforceInterface(descriptor);
               ia_arg0 = IAccountManagerResponse.Stub.asInterface(data.readStrongBinder());
               if (0 != data.readInt()) {
                  _arg1 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  _arg1 = null;
               }

               _arg2 = data.readString();
               this.isCredentialsUpdateSuggested(ia_arg0, _arg1, _arg2);
               reply.writeNoException();
               return true;
            case 35:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
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

               ClassLoader cl = this.getClass().getClassLoader();
               Map map_arg4 = data.readHashMap(cl);
               b_result = this.addAccountExplicitlyWithVisibility(_arg0, _arg1, _arg2, _arg3, map_arg4);
               reply.writeNoException();
               reply.writeInt(b_result ? 1 : 0);
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(IAccountManager impl) {
         if (IAccountManager.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGAFNC59ATAoLBUcD2ozOyB6DTwqLRgEKGIKESNqETwiKAgAVg==")));
         } else if (impl != null) {
            IAccountManager.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IAccountManager getDefaultImpl() {
         return IAccountManager.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IAccountManager {
         private IBinder mRemote;
         public static IAccountManager sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuKmwjFgJlNSAoIz4+PWgaFlo="));
         }

         public AuthenticatorDescription[] getAuthenticatorTypes(int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            AuthenticatorDescription[] var6;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuKmwjFgJlNSAoIz4+PWgaFlo=")));
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (_status || IAccountManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  AuthenticatorDescription[] _result = (AuthenticatorDescription[])_reply.createTypedArray(AuthenticatorDescription.CREATOR);
                  return _result;
               }

               var6 = IAccountManager.Stub.getDefaultImpl().getAuthenticatorTypes(userId);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var6;
         }

         public void getAccountsByFeatures(int userId, IAccountManagerResponse response, String type, String[] features) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuKmwjFgJlNSAoIz4+PWgaFlo=")));
               _data.writeInt(userId);
               _data.writeStrongBinder(response != null ? response.asBinder() : null);
               _data.writeString(type);
               _data.writeStringArray(features);
               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (!_status && IAccountManager.Stub.getDefaultImpl() != null) {
                  IAccountManager.Stub.getDefaultImpl().getAccountsByFeatures(userId, response, type, features);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public String getPreviousName(int userId, Account account) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            String var7;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuKmwjFgJlNSAoIz4+PWgaFlo=")));
               _data.writeInt(userId);
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(3, _data, _reply, 0);
               if (_status || IAccountManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  String _result = _reply.readString();
                  return _result;
               }

               var7 = IAccountManager.Stub.getDefaultImpl().getPreviousName(userId, account);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var7;
         }

         public Account[] getAccounts(int userId, String type) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            Account[] var7;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuKmwjFgJlNSAoIz4+PWgaFlo=")));
               _data.writeInt(userId);
               _data.writeString(type);
               boolean _status = this.mRemote.transact(4, _data, _reply, 0);
               if (_status || IAccountManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  Account[] _result = (Account[])_reply.createTypedArray(Account.CREATOR);
                  return _result;
               }

               var7 = IAccountManager.Stub.getDefaultImpl().getAccounts(userId, type);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var7;
         }

         public void getAuthToken(int userId, IAccountManagerResponse response, Account account, String authTokenType, boolean notifyOnAuthFailure, boolean expectActivityLaunch, Bundle loginOptions) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuKmwjFgJlNSAoIz4+PWgaFlo=")));
               _data.writeInt(userId);
               _data.writeStrongBinder(response != null ? response.asBinder() : null);
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(authTokenType);
               _data.writeInt(notifyOnAuthFailure ? 1 : 0);
               _data.writeInt(expectActivityLaunch ? 1 : 0);
               if (loginOptions != null) {
                  _data.writeInt(1);
                  loginOptions.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(5, _data, _reply, 0);
               if (!_status && IAccountManager.Stub.getDefaultImpl() != null) {
                  IAccountManager.Stub.getDefaultImpl().getAuthToken(userId, response, account, authTokenType, notifyOnAuthFailure, expectActivityLaunch, loginOptions);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void setPassword(int userId, Account account, String password) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuKmwjFgJlNSAoIz4+PWgaFlo=")));
               _data.writeInt(userId);
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(password);
               boolean _status = this.mRemote.transact(6, _data, _reply, 0);
               if (!_status && IAccountManager.Stub.getDefaultImpl() != null) {
                  IAccountManager.Stub.getDefaultImpl().setPassword(userId, account, password);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void setAuthToken(int userId, Account account, String authTokenType, String authToken) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuKmwjFgJlNSAoIz4+PWgaFlo=")));
               _data.writeInt(userId);
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(authTokenType);
               _data.writeString(authToken);
               boolean _status = this.mRemote.transact(7, _data, _reply, 0);
               if (_status || IAccountManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IAccountManager.Stub.getDefaultImpl().setAuthToken(userId, account, authTokenType, authToken);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void setUserData(int userId, Account account, String key, String value) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuKmwjFgJlNSAoIz4+PWgaFlo=")));
               _data.writeInt(userId);
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(key);
               _data.writeString(value);
               boolean _status = this.mRemote.transact(8, _data, _reply, 0);
               if (_status || IAccountManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IAccountManager.Stub.getDefaultImpl().setUserData(userId, account, key, value);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void hasFeatures(int userId, IAccountManagerResponse response, Account account, String[] features) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuKmwjFgJlNSAoIz4+PWgaFlo=")));
               _data.writeInt(userId);
               _data.writeStrongBinder(response != null ? response.asBinder() : null);
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeStringArray(features);
               boolean _status = this.mRemote.transact(9, _data, _reply, 0);
               if (!_status && IAccountManager.Stub.getDefaultImpl() != null) {
                  IAccountManager.Stub.getDefaultImpl().hasFeatures(userId, response, account, features);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void updateCredentials(int userId, IAccountManagerResponse response, Account account, String authTokenType, boolean expectActivityLaunch, Bundle loginOptions) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuKmwjFgJlNSAoIz4+PWgaFlo=")));
               _data.writeInt(userId);
               _data.writeStrongBinder(response != null ? response.asBinder() : null);
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(authTokenType);
               _data.writeInt(expectActivityLaunch ? 1 : 0);
               if (loginOptions != null) {
                  _data.writeInt(1);
                  loginOptions.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(10, _data, _reply, 0);
               if (_status || IAccountManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IAccountManager.Stub.getDefaultImpl().updateCredentials(userId, response, account, authTokenType, expectActivityLaunch, loginOptions);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void editProperties(int userId, IAccountManagerResponse response, String accountType, boolean expectActivityLaunch) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuKmwjFgJlNSAoIz4+PWgaFlo=")));
               _data.writeInt(userId);
               _data.writeStrongBinder(response != null ? response.asBinder() : null);
               _data.writeString(accountType);
               _data.writeInt(expectActivityLaunch ? 1 : 0);
               boolean _status = this.mRemote.transact(11, _data, _reply, 0);
               if (!_status && IAccountManager.Stub.getDefaultImpl() != null) {
                  IAccountManager.Stub.getDefaultImpl().editProperties(userId, response, accountType, expectActivityLaunch);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void getAuthTokenLabel(int userId, IAccountManagerResponse response, String accountType, String authTokenType) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuKmwjFgJlNSAoIz4+PWgaFlo=")));
               _data.writeInt(userId);
               _data.writeStrongBinder(response != null ? response.asBinder() : null);
               _data.writeString(accountType);
               _data.writeString(authTokenType);
               boolean _status = this.mRemote.transact(12, _data, _reply, 0);
               if (!_status && IAccountManager.Stub.getDefaultImpl() != null) {
                  IAccountManager.Stub.getDefaultImpl().getAuthTokenLabel(userId, response, accountType, authTokenType);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public String getUserData(int userId, Account account, String key) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            String _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuKmwjFgJlNSAoIz4+PWgaFlo=")));
               _data.writeInt(userId);
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(key);
               boolean _status = this.mRemote.transact(13, _data, _reply, 0);
               if (!_status && IAccountManager.Stub.getDefaultImpl() != null) {
                  String var8 = IAccountManager.Stub.getDefaultImpl().getUserData(userId, account, key);
                  return var8;
               }

               _reply.readException();
               _result = _reply.readString();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public String getPassword(int userId, Account account) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            String var7;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuKmwjFgJlNSAoIz4+PWgaFlo=")));
               _data.writeInt(userId);
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(14, _data, _reply, 0);
               if (_status || IAccountManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  String _result = _reply.readString();
                  return _result;
               }

               var7 = IAccountManager.Stub.getDefaultImpl().getPassword(userId, account);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var7;
         }

         public void confirmCredentials(int userId, IAccountManagerResponse response, Account account, Bundle options, boolean expectActivityLaunch) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuKmwjFgJlNSAoIz4+PWgaFlo=")));
               _data.writeInt(userId);
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

               _data.writeInt(expectActivityLaunch ? 1 : 0);
               boolean _status = this.mRemote.transact(15, _data, _reply, 0);
               if (!_status && IAccountManager.Stub.getDefaultImpl() != null) {
                  IAccountManager.Stub.getDefaultImpl().confirmCredentials(userId, response, account, options, expectActivityLaunch);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void addAccount(int userId, IAccountManagerResponse response, String accountType, String authTokenType, String[] requiredFeatures, boolean expectActivityLaunch, Bundle optionsIn) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuKmwjFgJlNSAoIz4+PWgaFlo=")));
               _data.writeInt(userId);
               _data.writeStrongBinder(response != null ? response.asBinder() : null);
               _data.writeString(accountType);
               _data.writeString(authTokenType);
               _data.writeStringArray(requiredFeatures);
               _data.writeInt(expectActivityLaunch ? 1 : 0);
               if (optionsIn != null) {
                  _data.writeInt(1);
                  optionsIn.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(16, _data, _reply, 0);
               if (!_status && IAccountManager.Stub.getDefaultImpl() != null) {
                  IAccountManager.Stub.getDefaultImpl().addAccount(userId, response, accountType, authTokenType, requiredFeatures, expectActivityLaunch, optionsIn);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public boolean addAccountExplicitly(int userId, Account account, String password, Bundle extras) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean var9;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuKmwjFgJlNSAoIz4+PWgaFlo=")));
               _data.writeInt(userId);
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(password);
               if (extras != null) {
                  _data.writeInt(1);
                  extras.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(17, _data, _reply, 0);
               if (_status || IAccountManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  boolean _result = 0 != _reply.readInt();
                  return _result;
               }

               var9 = IAccountManager.Stub.getDefaultImpl().addAccountExplicitly(userId, account, password, extras);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var9;
         }

         public boolean removeAccountExplicitly(int userId, Account account) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean var7;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuKmwjFgJlNSAoIz4+PWgaFlo=")));
               _data.writeInt(userId);
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(18, _data, _reply, 0);
               if (_status || IAccountManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  boolean _result = 0 != _reply.readInt();
                  return _result;
               }

               var7 = IAccountManager.Stub.getDefaultImpl().removeAccountExplicitly(userId, account);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var7;
         }

         public void renameAccount(int userId, IAccountManagerResponse response, Account accountToRename, String newName) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuKmwjFgJlNSAoIz4+PWgaFlo=")));
               _data.writeInt(userId);
               _data.writeStrongBinder(response != null ? response.asBinder() : null);
               if (accountToRename != null) {
                  _data.writeInt(1);
                  accountToRename.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(newName);
               boolean _status = this.mRemote.transact(19, _data, _reply, 0);
               if (_status || IAccountManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IAccountManager.Stub.getDefaultImpl().renameAccount(userId, response, accountToRename, newName);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void removeAccount(int userId, IAccountManagerResponse response, Account account, boolean expectActivityLaunch) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuKmwjFgJlNSAoIz4+PWgaFlo=")));
               _data.writeInt(userId);
               _data.writeStrongBinder(response != null ? response.asBinder() : null);
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeInt(expectActivityLaunch ? 1 : 0);
               boolean _status = this.mRemote.transact(20, _data, _reply, 0);
               if (_status || IAccountManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IAccountManager.Stub.getDefaultImpl().removeAccount(userId, response, account, expectActivityLaunch);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void clearPassword(int userId, Account account) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuKmwjFgJlNSAoIz4+PWgaFlo=")));
               _data.writeInt(userId);
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(21, _data, _reply, 0);
               if (_status || IAccountManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IAccountManager.Stub.getDefaultImpl().clearPassword(userId, account);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public boolean accountAuthenticated(int userId, Account account) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuKmwjFgJlNSAoIz4+PWgaFlo=")));
               _data.writeInt(userId);
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(22, _data, _reply, 0);
               if (!_status && IAccountManager.Stub.getDefaultImpl() != null) {
                  boolean var7 = IAccountManager.Stub.getDefaultImpl().accountAuthenticated(userId, account);
                  return var7;
               }

               _reply.readException();
               _result = 0 != _reply.readInt();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public void invalidateAuthToken(int userId, String accountType, String authToken) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuKmwjFgJlNSAoIz4+PWgaFlo=")));
               _data.writeInt(userId);
               _data.writeString(accountType);
               _data.writeString(authToken);
               boolean _status = this.mRemote.transact(23, _data, _reply, 0);
               if (_status || IAccountManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IAccountManager.Stub.getDefaultImpl().invalidateAuthToken(userId, accountType, authToken);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public String peekAuthToken(int userId, Account account, String authTokenType) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            String _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuKmwjFgJlNSAoIz4+PWgaFlo=")));
               _data.writeInt(userId);
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(authTokenType);
               boolean _status = this.mRemote.transact(24, _data, _reply, 0);
               if (!_status && IAccountManager.Stub.getDefaultImpl() != null) {
                  String var8 = IAccountManager.Stub.getDefaultImpl().peekAuthToken(userId, account, authTokenType);
                  return var8;
               }

               _reply.readException();
               _result = _reply.readString();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public boolean setAccountVisibility(int userId, Account a, String packageName, int newVisibility) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuKmwjFgJlNSAoIz4+PWgaFlo=")));
               _data.writeInt(userId);
               if (a != null) {
                  _data.writeInt(1);
                  a.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(packageName);
               _data.writeInt(newVisibility);
               boolean _status = this.mRemote.transact(25, _data, _reply, 0);
               if (!_status && IAccountManager.Stub.getDefaultImpl() != null) {
                  boolean var9 = IAccountManager.Stub.getDefaultImpl().setAccountVisibility(userId, a, packageName, newVisibility);
                  return var9;
               }

               _reply.readException();
               _result = 0 != _reply.readInt();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public int getAccountVisibility(int userId, Account a, String packageName) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            int var8;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuKmwjFgJlNSAoIz4+PWgaFlo=")));
               _data.writeInt(userId);
               if (a != null) {
                  _data.writeInt(1);
                  a.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(packageName);
               boolean _status = this.mRemote.transact(26, _data, _reply, 0);
               if (_status || IAccountManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  int _result = _reply.readInt();
                  return _result;
               }

               var8 = IAccountManager.Stub.getDefaultImpl().getAccountVisibility(userId, a, packageName);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var8;
         }

         public void startAddAccountSession(IAccountManagerResponse response, String accountType, String authTokenType, String[] requiredFeatures, boolean expectActivityLaunch, Bundle options) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuKmwjFgJlNSAoIz4+PWgaFlo=")));
               _data.writeStrongBinder(response != null ? response.asBinder() : null);
               _data.writeString(accountType);
               _data.writeString(authTokenType);
               _data.writeStringArray(requiredFeatures);
               _data.writeInt(expectActivityLaunch ? 1 : 0);
               if (options != null) {
                  _data.writeInt(1);
                  options.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(27, _data, _reply, 0);
               if (!_status && IAccountManager.Stub.getDefaultImpl() != null) {
                  IAccountManager.Stub.getDefaultImpl().startAddAccountSession(response, accountType, authTokenType, requiredFeatures, expectActivityLaunch, options);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void startUpdateCredentialsSession(IAccountManagerResponse response, Account account, String authTokenType, boolean expectActivityLaunch, Bundle options) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuKmwjFgJlNSAoIz4+PWgaFlo=")));
               _data.writeStrongBinder(response != null ? response.asBinder() : null);
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(authTokenType);
               _data.writeInt(expectActivityLaunch ? 1 : 0);
               if (options != null) {
                  _data.writeInt(1);
                  options.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(28, _data, _reply, 0);
               if (!_status && IAccountManager.Stub.getDefaultImpl() != null) {
                  IAccountManager.Stub.getDefaultImpl().startUpdateCredentialsSession(response, account, authTokenType, expectActivityLaunch, options);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void registerAccountListener(String[] accountTypes) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuKmwjFgJlNSAoIz4+PWgaFlo=")));
               _data.writeStringArray(accountTypes);
               boolean _status = this.mRemote.transact(29, _data, _reply, 0);
               if (!_status && IAccountManager.Stub.getDefaultImpl() != null) {
                  IAccountManager.Stub.getDefaultImpl().registerAccountListener(accountTypes);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void unregisterAccountListener(String[] accountTypes) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuKmwjFgJlNSAoIz4+PWgaFlo=")));
               _data.writeStringArray(accountTypes);
               boolean _status = this.mRemote.transact(30, _data, _reply, 0);
               if (!_status && IAccountManager.Stub.getDefaultImpl() != null) {
                  IAccountManager.Stub.getDefaultImpl().unregisterAccountListener(accountTypes);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public Map getPackagesAndVisibilityForAccount(int userId, Account account) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            Map var7;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuKmwjFgJlNSAoIz4+PWgaFlo=")));
               _data.writeInt(userId);
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(31, _data, _reply, 0);
               if (_status || IAccountManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  ClassLoader cl = this.getClass().getClassLoader();
                  Map _result = _reply.readHashMap(cl);
                  return _result;
               }

               var7 = IAccountManager.Stub.getDefaultImpl().getPackagesAndVisibilityForAccount(userId, account);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var7;
         }

         public Map getAccountsAndVisibilityForPackage(int userId, String packageName, String accountType) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            HashMap _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuKmwjFgJlNSAoIz4+PWgaFlo=")));
               _data.writeInt(userId);
               _data.writeString(packageName);
               _data.writeString(accountType);
               boolean _status = this.mRemote.transact(32, _data, _reply, 0);
               if (!_status && IAccountManager.Stub.getDefaultImpl() != null) {
                  Map var12 = IAccountManager.Stub.getDefaultImpl().getAccountsAndVisibilityForPackage(userId, packageName, accountType);
                  return var12;
               }

               _reply.readException();
               ClassLoader cl = this.getClass().getClassLoader();
               _result = _reply.readHashMap(cl);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public void finishSessionAsUser(IAccountManagerResponse response, Bundle sessionBundle, boolean expectActivityLaunch, Bundle appInfo, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuKmwjFgJlNSAoIz4+PWgaFlo=")));
               _data.writeStrongBinder(response != null ? response.asBinder() : null);
               if (sessionBundle != null) {
                  _data.writeInt(1);
                  sessionBundle.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeInt(expectActivityLaunch ? 1 : 0);
               if (appInfo != null) {
                  _data.writeInt(1);
                  appInfo.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(33, _data, _reply, 0);
               if (!_status && IAccountManager.Stub.getDefaultImpl() != null) {
                  IAccountManager.Stub.getDefaultImpl().finishSessionAsUser(response, sessionBundle, expectActivityLaunch, appInfo, userId);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void isCredentialsUpdateSuggested(IAccountManagerResponse response, Account account, String statusToken) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuKmwjFgJlNSAoIz4+PWgaFlo=")));
               _data.writeStrongBinder(response != null ? response.asBinder() : null);
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(statusToken);
               boolean _status = this.mRemote.transact(34, _data, _reply, 0);
               if (!_status && IAccountManager.Stub.getDefaultImpl() != null) {
                  IAccountManager.Stub.getDefaultImpl().isCredentialsUpdateSuggested(response, account, statusToken);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public boolean addAccountExplicitlyWithVisibility(int userId, Account account, String password, Bundle extras, Map visibility) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwcuKmwjFgJlNSAoIz4+PWgaFlo=")));
               _data.writeInt(userId);
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(password);
               if (extras != null) {
                  _data.writeInt(1);
                  extras.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeMap(visibility);
               boolean _status = this.mRemote.transact(35, _data, _reply, 0);
               if (!_status && IAccountManager.Stub.getDefaultImpl() != null) {
                  boolean var10 = IAccountManager.Stub.getDefaultImpl().addAccountExplicitlyWithVisibility(userId, account, password, extras, visibility);
                  return var10;
               }

               _reply.readException();
               _result = 0 != _reply.readInt();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }
      }
   }

   public static class Default implements IAccountManager {
      public AuthenticatorDescription[] getAuthenticatorTypes(int userId) throws RemoteException {
         return null;
      }

      public void getAccountsByFeatures(int userId, IAccountManagerResponse response, String type, String[] features) throws RemoteException {
      }

      public String getPreviousName(int userId, Account account) throws RemoteException {
         return null;
      }

      public Account[] getAccounts(int userId, String type) throws RemoteException {
         return null;
      }

      public void getAuthToken(int userId, IAccountManagerResponse response, Account account, String authTokenType, boolean notifyOnAuthFailure, boolean expectActivityLaunch, Bundle loginOptions) throws RemoteException {
      }

      public void setPassword(int userId, Account account, String password) throws RemoteException {
      }

      public void setAuthToken(int userId, Account account, String authTokenType, String authToken) throws RemoteException {
      }

      public void setUserData(int userId, Account account, String key, String value) throws RemoteException {
      }

      public void hasFeatures(int userId, IAccountManagerResponse response, Account account, String[] features) throws RemoteException {
      }

      public void updateCredentials(int userId, IAccountManagerResponse response, Account account, String authTokenType, boolean expectActivityLaunch, Bundle loginOptions) throws RemoteException {
      }

      public void editProperties(int userId, IAccountManagerResponse response, String accountType, boolean expectActivityLaunch) throws RemoteException {
      }

      public void getAuthTokenLabel(int userId, IAccountManagerResponse response, String accountType, String authTokenType) throws RemoteException {
      }

      public String getUserData(int userId, Account account, String key) throws RemoteException {
         return null;
      }

      public String getPassword(int userId, Account account) throws RemoteException {
         return null;
      }

      public void confirmCredentials(int userId, IAccountManagerResponse response, Account account, Bundle options, boolean expectActivityLaunch) throws RemoteException {
      }

      public void addAccount(int userId, IAccountManagerResponse response, String accountType, String authTokenType, String[] requiredFeatures, boolean expectActivityLaunch, Bundle optionsIn) throws RemoteException {
      }

      public boolean addAccountExplicitly(int userId, Account account, String password, Bundle extras) throws RemoteException {
         return false;
      }

      public boolean removeAccountExplicitly(int userId, Account account) throws RemoteException {
         return false;
      }

      public void renameAccount(int userId, IAccountManagerResponse response, Account accountToRename, String newName) throws RemoteException {
      }

      public void removeAccount(int userId, IAccountManagerResponse response, Account account, boolean expectActivityLaunch) throws RemoteException {
      }

      public void clearPassword(int userId, Account account) throws RemoteException {
      }

      public boolean accountAuthenticated(int userId, Account account) throws RemoteException {
         return false;
      }

      public void invalidateAuthToken(int userId, String accountType, String authToken) throws RemoteException {
      }

      public String peekAuthToken(int userId, Account account, String authTokenType) throws RemoteException {
         return null;
      }

      public boolean setAccountVisibility(int userId, Account a, String packageName, int newVisibility) throws RemoteException {
         return false;
      }

      public int getAccountVisibility(int userId, Account a, String packageName) throws RemoteException {
         return 0;
      }

      public void startAddAccountSession(IAccountManagerResponse response, String accountType, String authTokenType, String[] requiredFeatures, boolean expectActivityLaunch, Bundle options) throws RemoteException {
      }

      public void startUpdateCredentialsSession(IAccountManagerResponse response, Account account, String authTokenType, boolean expectActivityLaunch, Bundle options) throws RemoteException {
      }

      public void registerAccountListener(String[] accountTypes) throws RemoteException {
      }

      public void unregisterAccountListener(String[] accountTypes) throws RemoteException {
      }

      public Map getPackagesAndVisibilityForAccount(int userId, Account account) throws RemoteException {
         return null;
      }

      public Map getAccountsAndVisibilityForPackage(int userId, String packageName, String accountType) throws RemoteException {
         return null;
      }

      public void finishSessionAsUser(IAccountManagerResponse response, Bundle sessionBundle, boolean expectActivityLaunch, Bundle appInfo, int userId) throws RemoteException {
      }

      public void isCredentialsUpdateSuggested(IAccountManagerResponse response, Account account, String statusToken) throws RemoteException {
      }

      public boolean addAccountExplicitlyWithVisibility(int userId, Account account, String password, Bundle extras, Map visibility) throws RemoteException {
         return false;
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
