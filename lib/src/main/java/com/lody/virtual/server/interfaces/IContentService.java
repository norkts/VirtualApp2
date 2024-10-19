package com.lody.virtual.server.interfaces;

import android.accounts.Account;
import android.content.ISyncStatusObserver;
import android.content.PeriodicSync;
import android.content.SyncAdapterType;
import android.content.SyncInfo;
import android.content.SyncRequest;
import android.content.SyncStatusInfo;
import android.database.IContentObserver;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import java.util.ArrayList;
import java.util.List;

public interface IContentService extends IInterface {
   void unregisterContentObserver(IContentObserver var1) throws RemoteException;

   void registerContentObserver(Uri var1, boolean var2, IContentObserver var3, int var4) throws RemoteException;

   void notifyChange(Uri var1, IContentObserver var2, boolean var3, boolean var4, int var5) throws RemoteException;

   void requestSync(Account var1, String var2, Bundle var3) throws RemoteException;

   void sync(SyncRequest var1) throws RemoteException;

   void cancelSync(Account var1, String var2) throws RemoteException;

   boolean getSyncAutomatically(Account var1, String var2) throws RemoteException;

   void setSyncAutomatically(Account var1, String var2, boolean var3) throws RemoteException;

   List<PeriodicSync> getPeriodicSyncs(Account var1, String var2) throws RemoteException;

   void addPeriodicSync(Account var1, String var2, Bundle var3, long var4) throws RemoteException;

   void removePeriodicSync(Account var1, String var2, Bundle var3) throws RemoteException;

   int getIsSyncable(Account var1, String var2) throws RemoteException;

   void setIsSyncable(Account var1, String var2, int var3) throws RemoteException;

   void setMasterSyncAutomatically(boolean var1) throws RemoteException;

   boolean getMasterSyncAutomatically() throws RemoteException;

   boolean isSyncActive(Account var1, String var2) throws RemoteException;

   List<SyncInfo> getCurrentSyncs() throws RemoteException;

   SyncAdapterType[] getSyncAdapterTypes() throws RemoteException;

   SyncStatusInfo getSyncStatus(Account var1, String var2) throws RemoteException;

   boolean isSyncPending(Account var1, String var2) throws RemoteException;

   void addStatusChangeListener(int var1, ISyncStatusObserver var2) throws RemoteException;

   void removeStatusChangeListener(ISyncStatusObserver var1) throws RemoteException;

   public abstract static class Stub extends Binder implements IContentService {
      private static final String DESCRIPTOR = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIy0YJWoaFgJlMh40Kj0iCWszNFo="));
      static final int TRANSACTION_unregisterContentObserver = 1;
      static final int TRANSACTION_registerContentObserver = 2;
      static final int TRANSACTION_notifyChange = 3;
      static final int TRANSACTION_requestSync = 4;
      static final int TRANSACTION_sync = 5;
      static final int TRANSACTION_cancelSync = 6;
      static final int TRANSACTION_getSyncAutomatically = 7;
      static final int TRANSACTION_setSyncAutomatically = 8;
      static final int TRANSACTION_getPeriodicSyncs = 9;
      static final int TRANSACTION_addPeriodicSync = 10;
      static final int TRANSACTION_removePeriodicSync = 11;
      static final int TRANSACTION_getIsSyncable = 12;
      static final int TRANSACTION_setIsSyncable = 13;
      static final int TRANSACTION_setMasterSyncAutomatically = 14;
      static final int TRANSACTION_getMasterSyncAutomatically = 15;
      static final int TRANSACTION_isSyncActive = 16;
      static final int TRANSACTION_getCurrentSyncs = 17;
      static final int TRANSACTION_getSyncAdapterTypes = 18;
      static final int TRANSACTION_getSyncStatus = 19;
      static final int TRANSACTION_isSyncPending = 20;
      static final int TRANSACTION_addStatusChangeListener = 21;
      static final int TRANSACTION_removeStatusChangeListener = 22;

      public Stub() {
         this.attachInterface(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIy0YJWoaFgJlMh40Kj0iCWszNFo=")));
      }

      public static IContentService asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IContentService)(iin != null && iin instanceof IContentService ? (IContentService)iin : new Proxy(obj));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
         String descriptor = DESCRIPTOR;
         boolean _result;
         Account _arg0;
         String _arg1;
         int _arg2;
         Bundle _arg2;
         boolean _result;
         Uri _arg0;
         switch (code) {
            case 1:
               data.enforceInterface(descriptor);
               IContentObserver _arg0 = IContentObserver.Stub.asInterface(data.readStrongBinder());
               this.unregisterContentObserver(_arg0);
               reply.writeNoException();
               return true;
            case 2:
               data.enforceInterface(descriptor);
               if (0 != data.readInt()) {
                  _arg0 = (Uri)Uri.CREATOR.createFromParcel(data);
               } else {
                  _arg0 = null;
               }

               boolean _arg1 = 0 != data.readInt();
               IContentObserver _arg2 = IContentObserver.Stub.asInterface(data.readStrongBinder());
               int _arg3 = data.readInt();
               this.registerContentObserver(_arg0, _arg1, _arg2, _arg3);
               reply.writeNoException();
               return true;
            case 3:
               data.enforceInterface(descriptor);
               if (0 != data.readInt()) {
                  _arg0 = (Uri)Uri.CREATOR.createFromParcel(data);
               } else {
                  _arg0 = null;
               }

               IContentObserver _arg1 = IContentObserver.Stub.asInterface(data.readStrongBinder());
               _result = 0 != data.readInt();
               boolean _arg3 = 0 != data.readInt();
               int _arg4 = data.readInt();
               this.notifyChange(_arg0, _arg1, _result, _arg3, _arg4);
               reply.writeNoException();
               return true;
            case 4:
               data.enforceInterface(descriptor);
               if (0 != data.readInt()) {
                  _arg0 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  _arg0 = null;
               }

               _arg1 = data.readString();
               if (0 != data.readInt()) {
                  _arg2 = (Bundle)Bundle.CREATOR.createFromParcel(data);
               } else {
                  _arg2 = null;
               }

               this.requestSync(_arg0, _arg1, _arg2);
               reply.writeNoException();
               return true;
            case 5:
               data.enforceInterface(descriptor);
               SyncRequest _arg0;
               if (0 != data.readInt()) {
                  _arg0 = (SyncRequest)SyncRequest.CREATOR.createFromParcel(data);
               } else {
                  _arg0 = null;
               }

               this.sync(_arg0);
               reply.writeNoException();
               return true;
            case 6:
               data.enforceInterface(descriptor);
               if (0 != data.readInt()) {
                  _arg0 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  _arg0 = null;
               }

               _arg1 = data.readString();
               this.cancelSync(_arg0, _arg1);
               reply.writeNoException();
               return true;
            case 7:
               data.enforceInterface(descriptor);
               if (0 != data.readInt()) {
                  _arg0 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  _arg0 = null;
               }

               _arg1 = data.readString();
               _result = this.getSyncAutomatically(_arg0, _arg1);
               reply.writeNoException();
               reply.writeInt(_result ? 1 : 0);
               return true;
            case 8:
               data.enforceInterface(descriptor);
               if (0 != data.readInt()) {
                  _arg0 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  _arg0 = null;
               }

               _arg1 = data.readString();
               _result = 0 != data.readInt();
               this.setSyncAutomatically(_arg0, _arg1, _result);
               reply.writeNoException();
               return true;
            case 9:
               data.enforceInterface(descriptor);
               if (0 != data.readInt()) {
                  _arg0 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  _arg0 = null;
               }

               _arg1 = data.readString();
               List<PeriodicSync> _result = this.getPeriodicSyncs(_arg0, _arg1);
               reply.writeNoException();
               reply.writeTypedList(_result);
               return true;
            case 10:
               data.enforceInterface(descriptor);
               if (0 != data.readInt()) {
                  _arg0 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  _arg0 = null;
               }

               _arg1 = data.readString();
               if (0 != data.readInt()) {
                  _arg2 = (Bundle)Bundle.CREATOR.createFromParcel(data);
               } else {
                  _arg2 = null;
               }

               long _arg3 = data.readLong();
               this.addPeriodicSync(_arg0, _arg1, _arg2, _arg3);
               reply.writeNoException();
               return true;
            case 11:
               data.enforceInterface(descriptor);
               if (0 != data.readInt()) {
                  _arg0 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  _arg0 = null;
               }

               _arg1 = data.readString();
               if (0 != data.readInt()) {
                  _arg2 = (Bundle)Bundle.CREATOR.createFromParcel(data);
               } else {
                  _arg2 = null;
               }

               this.removePeriodicSync(_arg0, _arg1, _arg2);
               reply.writeNoException();
               return true;
            case 12:
               data.enforceInterface(descriptor);
               if (0 != data.readInt()) {
                  _arg0 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  _arg0 = null;
               }

               _arg1 = data.readString();
               _arg2 = this.getIsSyncable(_arg0, _arg1);
               reply.writeNoException();
               reply.writeInt(_arg2);
               return true;
            case 13:
               data.enforceInterface(descriptor);
               if (0 != data.readInt()) {
                  _arg0 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  _arg0 = null;
               }

               _arg1 = data.readString();
               _arg2 = data.readInt();
               this.setIsSyncable(_arg0, _arg1, _arg2);
               reply.writeNoException();
               return true;
            case 14:
               data.enforceInterface(descriptor);
               _result = 0 != data.readInt();
               this.setMasterSyncAutomatically(_result);
               reply.writeNoException();
               return true;
            case 15:
               data.enforceInterface(descriptor);
               _result = this.getMasterSyncAutomatically();
               reply.writeNoException();
               reply.writeInt(_result ? 1 : 0);
               return true;
            case 16:
               data.enforceInterface(descriptor);
               if (0 != data.readInt()) {
                  _arg0 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  _arg0 = null;
               }

               _arg1 = data.readString();
               _result = this.isSyncActive(_arg0, _arg1);
               reply.writeNoException();
               reply.writeInt(_result ? 1 : 0);
               return true;
            case 17:
               data.enforceInterface(descriptor);
               List<SyncInfo> _result = this.getCurrentSyncs();
               reply.writeNoException();
               reply.writeTypedList(_result);
               return true;
            case 18:
               data.enforceInterface(descriptor);
               SyncAdapterType[] _result = this.getSyncAdapterTypes();
               reply.writeNoException();
               reply.writeTypedArray(_result, 1);
               return true;
            case 19:
               data.enforceInterface(descriptor);
               if (0 != data.readInt()) {
                  _arg0 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  _arg0 = null;
               }

               _arg1 = data.readString();
               SyncStatusInfo _result = this.getSyncStatus(_arg0, _arg1);
               reply.writeNoException();
               if (_result != null) {
                  reply.writeInt(1);
                  _result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 20:
               data.enforceInterface(descriptor);
               if (0 != data.readInt()) {
                  _arg0 = (Account)Account.CREATOR.createFromParcel(data);
               } else {
                  _arg0 = null;
               }

               _arg1 = data.readString();
               _result = this.isSyncPending(_arg0, _arg1);
               reply.writeNoException();
               reply.writeInt(_result ? 1 : 0);
               return true;
            case 21:
               data.enforceInterface(descriptor);
               int _arg0 = data.readInt();
               ISyncStatusObserver _arg1 = ISyncStatusObserver.Stub.asInterface(data.readStrongBinder());
               this.addStatusChangeListener(_arg0, _arg1);
               reply.writeNoException();
               return true;
            case 22:
               data.enforceInterface(descriptor);
               ISyncStatusObserver _arg0 = ISyncStatusObserver.Stub.asInterface(data.readStrongBinder());
               this.removeStatusChangeListener(_arg0);
               reply.writeNoException();
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(IContentService impl) {
         if (IContentService.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGAFNC59ATAoLBUcD2ozOyB6DTwqLRgEKGIKESNqETwiKAgAVg==")));
         } else if (impl != null) {
            IContentService.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IContentService getDefaultImpl() {
         return IContentService.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IContentService {
         private IBinder mRemote;
         public static IContentService sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIy0YJWoaFgJlMh40Kj0iCWszNFo="));
         }

         public void unregisterContentObserver(IContentObserver observer) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIy0YJWoaFgJlMh40Kj0iCWszNFo=")));
               _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (_status || IContentService.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IContentService.Stub.getDefaultImpl().unregisterContentObserver(observer);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void registerContentObserver(Uri uri, boolean notifyForDescendants, IContentObserver observer, int userHandle) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIy0YJWoaFgJlMh40Kj0iCWszNFo=")));
               if (uri != null) {
                  _data.writeInt(1);
                  uri.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeInt(notifyForDescendants ? 1 : 0);
               _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
               _data.writeInt(userHandle);
               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (!_status && IContentService.Stub.getDefaultImpl() != null) {
                  IContentService.Stub.getDefaultImpl().registerContentObserver(uri, notifyForDescendants, observer, userHandle);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void notifyChange(Uri uri, IContentObserver observer, boolean observerWantsSelfNotifications, boolean syncToNetwork, int userHandle) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIy0YJWoaFgJlMh40Kj0iCWszNFo=")));
               if (uri != null) {
                  _data.writeInt(1);
                  uri.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
               _data.writeInt(observerWantsSelfNotifications ? 1 : 0);
               _data.writeInt(syncToNetwork ? 1 : 0);
               _data.writeInt(userHandle);
               boolean _status = this.mRemote.transact(3, _data, _reply, 0);
               if (!_status && IContentService.Stub.getDefaultImpl() != null) {
                  IContentService.Stub.getDefaultImpl().notifyChange(uri, observer, observerWantsSelfNotifications, syncToNetwork, userHandle);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void requestSync(Account account, String authority, Bundle extras) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIy0YJWoaFgJlMh40Kj0iCWszNFo=")));
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(authority);
               if (extras != null) {
                  _data.writeInt(1);
                  extras.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(4, _data, _reply, 0);
               if (!_status && IContentService.Stub.getDefaultImpl() != null) {
                  IContentService.Stub.getDefaultImpl().requestSync(account, authority, extras);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void sync(SyncRequest request) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIy0YJWoaFgJlMh40Kj0iCWszNFo=")));
               if (request != null) {
                  _data.writeInt(1);
                  request.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(5, _data, _reply, 0);
               if (_status || IContentService.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IContentService.Stub.getDefaultImpl().sync(request);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void cancelSync(Account account, String authority) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIy0YJWoaFgJlMh40Kj0iCWszNFo=")));
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(authority);
               boolean _status = this.mRemote.transact(6, _data, _reply, 0);
               if (_status || IContentService.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IContentService.Stub.getDefaultImpl().cancelSync(account, authority);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public boolean getSyncAutomatically(Account account, String providerName) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean var7;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIy0YJWoaFgJlMh40Kj0iCWszNFo=")));
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(providerName);
               boolean _status = this.mRemote.transact(7, _data, _reply, 0);
               if (_status || IContentService.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  boolean _result = 0 != _reply.readInt();
                  return _result;
               }

               var7 = IContentService.Stub.getDefaultImpl().getSyncAutomatically(account, providerName);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var7;
         }

         public void setSyncAutomatically(Account account, String providerName, boolean sync) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIy0YJWoaFgJlMh40Kj0iCWszNFo=")));
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(providerName);
               _data.writeInt(sync ? 1 : 0);
               boolean _status = this.mRemote.transact(8, _data, _reply, 0);
               if (_status || IContentService.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IContentService.Stub.getDefaultImpl().setSyncAutomatically(account, providerName, sync);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public List<PeriodicSync> getPeriodicSyncs(Account account, String providerName) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            ArrayList _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIy0YJWoaFgJlMh40Kj0iCWszNFo=")));
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(providerName);
               boolean _status = this.mRemote.transact(9, _data, _reply, 0);
               if (!_status && IContentService.Stub.getDefaultImpl() != null) {
                  List var7 = IContentService.Stub.getDefaultImpl().getPeriodicSyncs(account, providerName);
                  return var7;
               }

               _reply.readException();
               _result = _reply.createTypedArrayList(PeriodicSync.CREATOR);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public void addPeriodicSync(Account account, String providerName, Bundle extras, long pollFrequency) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIy0YJWoaFgJlMh40Kj0iCWszNFo=")));
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(providerName);
               if (extras != null) {
                  _data.writeInt(1);
                  extras.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeLong(pollFrequency);
               boolean _status = this.mRemote.transact(10, _data, _reply, 0);
               if (!_status && IContentService.Stub.getDefaultImpl() != null) {
                  IContentService.Stub.getDefaultImpl().addPeriodicSync(account, providerName, extras, pollFrequency);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void removePeriodicSync(Account account, String providerName, Bundle extras) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIy0YJWoaFgJlMh40Kj0iCWszNFo=")));
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(providerName);
               if (extras != null) {
                  _data.writeInt(1);
                  extras.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(11, _data, _reply, 0);
               if (_status || IContentService.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IContentService.Stub.getDefaultImpl().removePeriodicSync(account, providerName, extras);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public int getIsSyncable(Account account, String providerName) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            int _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIy0YJWoaFgJlMh40Kj0iCWszNFo=")));
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(providerName);
               boolean _status = this.mRemote.transact(12, _data, _reply, 0);
               if (!_status && IContentService.Stub.getDefaultImpl() != null) {
                  int var7 = IContentService.Stub.getDefaultImpl().getIsSyncable(account, providerName);
                  return var7;
               }

               _reply.readException();
               _result = _reply.readInt();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public void setIsSyncable(Account account, String providerName, int syncable) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIy0YJWoaFgJlMh40Kj0iCWszNFo=")));
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(providerName);
               _data.writeInt(syncable);
               boolean _status = this.mRemote.transact(13, _data, _reply, 0);
               if (!_status && IContentService.Stub.getDefaultImpl() != null) {
                  IContentService.Stub.getDefaultImpl().setIsSyncable(account, providerName, syncable);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void setMasterSyncAutomatically(boolean flag) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIy0YJWoaFgJlMh40Kj0iCWszNFo=")));
               _data.writeInt(flag ? 1 : 0);
               boolean _status = this.mRemote.transact(14, _data, _reply, 0);
               if (_status || IContentService.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IContentService.Stub.getDefaultImpl().setMasterSyncAutomatically(flag);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public boolean getMasterSyncAutomatically() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIy0YJWoaFgJlMh40Kj0iCWszNFo=")));
               boolean _status = this.mRemote.transact(15, _data, _reply, 0);
               if (!_status && IContentService.Stub.getDefaultImpl() != null) {
                  boolean var5 = IContentService.Stub.getDefaultImpl().getMasterSyncAutomatically();
                  return var5;
               }

               _reply.readException();
               _result = 0 != _reply.readInt();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public boolean isSyncActive(Account account, String authority) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean var7;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIy0YJWoaFgJlMh40Kj0iCWszNFo=")));
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(authority);
               boolean _status = this.mRemote.transact(16, _data, _reply, 0);
               if (_status || IContentService.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  boolean _result = 0 != _reply.readInt();
                  return _result;
               }

               var7 = IContentService.Stub.getDefaultImpl().isSyncActive(account, authority);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var7;
         }

         public List<SyncInfo> getCurrentSyncs() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            List var5;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIy0YJWoaFgJlMh40Kj0iCWszNFo=")));
               boolean _status = this.mRemote.transact(17, _data, _reply, 0);
               if (_status || IContentService.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  List<SyncInfo> _result = _reply.createTypedArrayList(SyncInfo.CREATOR);
                  return _result;
               }

               var5 = IContentService.Stub.getDefaultImpl().getCurrentSyncs();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var5;
         }

         public SyncAdapterType[] getSyncAdapterTypes() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            SyncAdapterType[] _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIy0YJWoaFgJlMh40Kj0iCWszNFo=")));
               boolean _status = this.mRemote.transact(18, _data, _reply, 0);
               if (!_status && IContentService.Stub.getDefaultImpl() != null) {
                  SyncAdapterType[] var5 = IContentService.Stub.getDefaultImpl().getSyncAdapterTypes();
                  return var5;
               }

               _reply.readException();
               _result = (SyncAdapterType[])_reply.createTypedArray(SyncAdapterType.CREATOR);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public SyncStatusInfo getSyncStatus(Account account, String authority) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            SyncStatusInfo var7;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIy0YJWoaFgJlMh40Kj0iCWszNFo=")));
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(authority);
               boolean _status = this.mRemote.transact(19, _data, _reply, 0);
               if (_status || IContentService.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  SyncStatusInfo _result;
                  if (0 != _reply.readInt()) {
                     _result = (SyncStatusInfo)SyncStatusInfo.CREATOR.createFromParcel(_reply);
                  } else {
                     _result = null;
                  }

                  return _result;
               }

               var7 = IContentService.Stub.getDefaultImpl().getSyncStatus(account, authority);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var7;
         }

         public boolean isSyncPending(Account account, String authority) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIy0YJWoaFgJlMh40Kj0iCWszNFo=")));
               if (account != null) {
                  _data.writeInt(1);
                  account.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(authority);
               boolean _status = this.mRemote.transact(20, _data, _reply, 0);
               if (!_status && IContentService.Stub.getDefaultImpl() != null) {
                  boolean var7 = IContentService.Stub.getDefaultImpl().isSyncPending(account, authority);
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

         public void addStatusChangeListener(int mask, ISyncStatusObserver callback) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIy0YJWoaFgJlMh40Kj0iCWszNFo=")));
               _data.writeInt(mask);
               _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
               boolean _status = this.mRemote.transact(21, _data, _reply, 0);
               if (!_status && IContentService.Stub.getDefaultImpl() != null) {
                  IContentService.Stub.getDefaultImpl().addStatusChangeListener(mask, callback);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void removeStatusChangeListener(ISyncStatusObserver callback) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIy0YJWoaFgJlMh40Kj0iCWszNFo=")));
               _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
               boolean _status = this.mRemote.transact(22, _data, _reply, 0);
               if (_status || IContentService.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IContentService.Stub.getDefaultImpl().removeStatusChangeListener(callback);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }
      }
   }

   public static class Default implements IContentService {
      public void unregisterContentObserver(IContentObserver observer) throws RemoteException {
      }

      public void registerContentObserver(Uri uri, boolean notifyForDescendants, IContentObserver observer, int userHandle) throws RemoteException {
      }

      public void notifyChange(Uri uri, IContentObserver observer, boolean observerWantsSelfNotifications, boolean syncToNetwork, int userHandle) throws RemoteException {
      }

      public void requestSync(Account account, String authority, Bundle extras) throws RemoteException {
      }

      public void sync(SyncRequest request) throws RemoteException {
      }

      public void cancelSync(Account account, String authority) throws RemoteException {
      }

      public boolean getSyncAutomatically(Account account, String providerName) throws RemoteException {
         return false;
      }

      public void setSyncAutomatically(Account account, String providerName, boolean sync) throws RemoteException {
      }

      public List<PeriodicSync> getPeriodicSyncs(Account account, String providerName) throws RemoteException {
         return null;
      }

      public void addPeriodicSync(Account account, String providerName, Bundle extras, long pollFrequency) throws RemoteException {
      }

      public void removePeriodicSync(Account account, String providerName, Bundle extras) throws RemoteException {
      }

      public int getIsSyncable(Account account, String providerName) throws RemoteException {
         return 0;
      }

      public void setIsSyncable(Account account, String providerName, int syncable) throws RemoteException {
      }

      public void setMasterSyncAutomatically(boolean flag) throws RemoteException {
      }

      public boolean getMasterSyncAutomatically() throws RemoteException {
         return false;
      }

      public boolean isSyncActive(Account account, String authority) throws RemoteException {
         return false;
      }

      public List<SyncInfo> getCurrentSyncs() throws RemoteException {
         return null;
      }

      public SyncAdapterType[] getSyncAdapterTypes() throws RemoteException {
         return null;
      }

      public SyncStatusInfo getSyncStatus(Account account, String authority) throws RemoteException {
         return null;
      }

      public boolean isSyncPending(Account account, String authority) throws RemoteException {
         return false;
      }

      public void addStatusChangeListener(int mask, ISyncStatusObserver callback) throws RemoteException {
      }

      public void removeStatusChangeListener(ISyncStatusObserver callback) throws RemoteException {
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
