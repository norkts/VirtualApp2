package com.lody.virtual.server.interfaces;

import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import com.lody.virtual.remote.InstalledAppInfo;
import com.lody.virtual.remote.VAppInstallerParams;
import com.lody.virtual.remote.VAppInstallerResult;
import java.util.ArrayList;
import java.util.List;

public interface IAppManager extends IInterface {
   int[] getPackageInstalledUsers(String var1) throws RemoteException;

   void scanApps() throws RemoteException;

   int getUidForSharedUser(String var1) throws RemoteException;

   InstalledAppInfo getInstalledAppInfo(String var1, int var2) throws RemoteException;

   VAppInstallerResult installPackage(Uri var1, VAppInstallerParams var2) throws RemoteException;

   boolean isPackageLaunched(int var1, String var2) throws RemoteException;

   void setPackageHidden(int var1, String var2, boolean var3) throws RemoteException;

   boolean installPackageAsUser(int var1, String var2) throws RemoteException;

   boolean uninstallPackageAsUser(String var1, int var2) throws RemoteException;

   boolean uninstallPackage(String var1) throws RemoteException;

   List<InstalledAppInfo> getInstalledApps(int var1) throws RemoteException;

   List<InstalledAppInfo> getInstalledAppsAsUser(int var1, int var2) throws RemoteException;

   List<String> getInstalledSplitNames(String var1) throws RemoteException;

   int getInstalledAppCount() throws RemoteException;

   boolean isAppInstalled(String var1) throws RemoteException;

   boolean isAppInstalledAsUser(int var1, String var2) throws RemoteException;

   void registerObserver(IPackageObserver var1) throws RemoteException;

   void unregisterObserver(IPackageObserver var1) throws RemoteException;

   boolean isRunInExtProcess(String var1) throws RemoteException;

   boolean cleanPackageData(String var1, int var2) throws RemoteException;

   public abstract static class Stub extends Binder implements IAppManager {
      private static final String DESCRIPTOR = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwgiP2QKOAJuJw40Kj5SVg=="));
      static final int TRANSACTION_getPackageInstalledUsers = 1;
      static final int TRANSACTION_scanApps = 2;
      static final int TRANSACTION_getUidForSharedUser = 3;
      static final int TRANSACTION_getInstalledAppInfo = 4;
      static final int TRANSACTION_installPackage = 5;
      static final int TRANSACTION_isPackageLaunched = 6;
      static final int TRANSACTION_setPackageHidden = 7;
      static final int TRANSACTION_installPackageAsUser = 8;
      static final int TRANSACTION_uninstallPackageAsUser = 9;
      static final int TRANSACTION_uninstallPackage = 10;
      static final int TRANSACTION_getInstalledApps = 11;
      static final int TRANSACTION_getInstalledAppsAsUser = 12;
      static final int TRANSACTION_getInstalledSplitNames = 13;
      static final int TRANSACTION_getInstalledAppCount = 14;
      static final int TRANSACTION_isAppInstalled = 15;
      static final int TRANSACTION_isAppInstalledAsUser = 16;
      static final int TRANSACTION_registerObserver = 17;
      static final int TRANSACTION_unregisterObserver = 18;
      static final int TRANSACTION_isRunInExtProcess = 19;
      static final int TRANSACTION_cleanPackageData = 20;

      public Stub() {
         this.attachInterface(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwgiP2QKOAJuJw40Kj5SVg==")));
      }

      public static IAppManager asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IAppManager)(iin != null && iin instanceof IAppManager ? (IAppManager)iin : new Proxy(obj));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
         String descriptor = DESCRIPTOR;
         String _arg0;
         int _arg1;
         boolean _result;
         IPackageObserver _arg0;
         int _arg0;
         boolean _result;
         String _arg1;
         List _result;
         switch (code) {
            case 1:
               data.enforceInterface(descriptor);
               _arg0 = data.readString();
               int[] _result = this.getPackageInstalledUsers(_arg0);
               reply.writeNoException();
               reply.writeIntArray(_result);
               return true;
            case 2:
               data.enforceInterface(descriptor);
               this.scanApps();
               reply.writeNoException();
               return true;
            case 3:
               data.enforceInterface(descriptor);
               _arg0 = data.readString();
               _arg1 = this.getUidForSharedUser(_arg0);
               reply.writeNoException();
               reply.writeInt(_arg1);
               return true;
            case 4:
               data.enforceInterface(descriptor);
               _arg0 = data.readString();
               _arg1 = data.readInt();
               InstalledAppInfo _result = this.getInstalledAppInfo(_arg0, _arg1);
               reply.writeNoException();
               if (_result != null) {
                  reply.writeInt(1);
                  _result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 5:
               data.enforceInterface(descriptor);
               Uri _arg0;
               if (0 != data.readInt()) {
                  _arg0 = (Uri)Uri.CREATOR.createFromParcel(data);
               } else {
                  _arg0 = null;
               }

               VAppInstallerParams _arg1;
               if (0 != data.readInt()) {
                  _arg1 = (VAppInstallerParams)VAppInstallerParams.CREATOR.createFromParcel(data);
               } else {
                  _arg1 = null;
               }

               VAppInstallerResult _result = this.installPackage(_arg0, _arg1);
               reply.writeNoException();
               if (_result != null) {
                  reply.writeInt(1);
                  _result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 6:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               _arg1 = data.readString();
               _result = this.isPackageLaunched(_arg0, _arg1);
               reply.writeNoException();
               reply.writeInt(_result ? 1 : 0);
               return true;
            case 7:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               _arg1 = data.readString();
               _result = 0 != data.readInt();
               this.setPackageHidden(_arg0, _arg1, _result);
               reply.writeNoException();
               return true;
            case 8:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               _arg1 = data.readString();
               _result = this.installPackageAsUser(_arg0, _arg1);
               reply.writeNoException();
               reply.writeInt(_result ? 1 : 0);
               return true;
            case 9:
               data.enforceInterface(descriptor);
               _arg0 = data.readString();
               _arg1 = data.readInt();
               _result = this.uninstallPackageAsUser(_arg0, _arg1);
               reply.writeNoException();
               reply.writeInt(_result ? 1 : 0);
               return true;
            case 10:
               data.enforceInterface(descriptor);
               _arg0 = data.readString();
               _result = this.uninstallPackage(_arg0);
               reply.writeNoException();
               reply.writeInt(_result ? 1 : 0);
               return true;
            case 11:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               _result = this.getInstalledApps(_arg0);
               reply.writeNoException();
               reply.writeTypedList(_result);
               return true;
            case 12:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               _arg1 = data.readInt();
               List<InstalledAppInfo> _result = this.getInstalledAppsAsUser(_arg0, _arg1);
               reply.writeNoException();
               reply.writeTypedList(_result);
               return true;
            case 13:
               data.enforceInterface(descriptor);
               _arg0 = data.readString();
               _result = this.getInstalledSplitNames(_arg0);
               reply.writeNoException();
               reply.writeStringList(_result);
               return true;
            case 14:
               data.enforceInterface(descriptor);
               _arg0 = this.getInstalledAppCount();
               reply.writeNoException();
               reply.writeInt(_arg0);
               return true;
            case 15:
               data.enforceInterface(descriptor);
               _arg0 = data.readString();
               _result = this.isAppInstalled(_arg0);
               reply.writeNoException();
               reply.writeInt(_result ? 1 : 0);
               return true;
            case 16:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               _arg1 = data.readString();
               _result = this.isAppInstalledAsUser(_arg0, _arg1);
               reply.writeNoException();
               reply.writeInt(_result ? 1 : 0);
               return true;
            case 17:
               data.enforceInterface(descriptor);
               _arg0 = IPackageObserver.Stub.asInterface(data.readStrongBinder());
               this.registerObserver(_arg0);
               reply.writeNoException();
               return true;
            case 18:
               data.enforceInterface(descriptor);
               _arg0 = IPackageObserver.Stub.asInterface(data.readStrongBinder());
               this.unregisterObserver(_arg0);
               reply.writeNoException();
               return true;
            case 19:
               data.enforceInterface(descriptor);
               _arg0 = data.readString();
               _result = this.isRunInExtProcess(_arg0);
               reply.writeNoException();
               reply.writeInt(_result ? 1 : 0);
               return true;
            case 20:
               data.enforceInterface(descriptor);
               _arg0 = data.readString();
               _arg1 = data.readInt();
               _result = this.cleanPackageData(_arg0, _arg1);
               reply.writeNoException();
               reply.writeInt(_result ? 1 : 0);
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(IAppManager impl) {
         if (IAppManager.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGAFNC59ATAoLBUcD2ozOyB6DTwqLRgEKGIKESNqETwiKAgAVg==")));
         } else if (impl != null) {
            IAppManager.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IAppManager getDefaultImpl() {
         return IAppManager.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IAppManager {
         private IBinder mRemote;
         public static IAppManager sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwgiP2QKOAJuJw40Kj5SVg=="));
         }

         public int[] getPackageInstalledUsers(String packageName) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            int[] _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwgiP2QKOAJuJw40Kj5SVg==")));
               _data.writeString(packageName);
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (!_status && IAppManager.Stub.getDefaultImpl() != null) {
                  int[] var6 = IAppManager.Stub.getDefaultImpl().getPackageInstalledUsers(packageName);
                  return var6;
               }

               _reply.readException();
               _result = _reply.createIntArray();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public void scanApps() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwgiP2QKOAJuJw40Kj5SVg==")));
               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (!_status && IAppManager.Stub.getDefaultImpl() != null) {
                  IAppManager.Stub.getDefaultImpl().scanApps();
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public int getUidForSharedUser(String sharedUserName) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            int var6;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwgiP2QKOAJuJw40Kj5SVg==")));
               _data.writeString(sharedUserName);
               boolean _status = this.mRemote.transact(3, _data, _reply, 0);
               if (_status || IAppManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  int _result = _reply.readInt();
                  return _result;
               }

               var6 = IAppManager.Stub.getDefaultImpl().getUidForSharedUser(sharedUserName);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var6;
         }

         public InstalledAppInfo getInstalledAppInfo(String pkg, int flags) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            InstalledAppInfo _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwgiP2QKOAJuJw40Kj5SVg==")));
               _data.writeString(pkg);
               _data.writeInt(flags);
               boolean _status = this.mRemote.transact(4, _data, _reply, 0);
               if (!_status && IAppManager.Stub.getDefaultImpl() != null) {
                  InstalledAppInfo var7 = IAppManager.Stub.getDefaultImpl().getInstalledAppInfo(pkg, flags);
                  return var7;
               }

               _reply.readException();
               if (0 != _reply.readInt()) {
                  _result = (InstalledAppInfo)InstalledAppInfo.CREATOR.createFromParcel(_reply);
               } else {
                  _result = null;
               }
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public VAppInstallerResult installPackage(Uri uri, VAppInstallerParams params) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            VAppInstallerResult var7;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwgiP2QKOAJuJw40Kj5SVg==")));
               if (uri != null) {
                  _data.writeInt(1);
                  uri.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               if (params != null) {
                  _data.writeInt(1);
                  params.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(5, _data, _reply, 0);
               if (_status || IAppManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  VAppInstallerResult _result;
                  if (0 != _reply.readInt()) {
                     _result = (VAppInstallerResult)VAppInstallerResult.CREATOR.createFromParcel(_reply);
                  } else {
                     _result = null;
                  }

                  return _result;
               }

               var7 = IAppManager.Stub.getDefaultImpl().installPackage(uri, params);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var7;
         }

         public boolean isPackageLaunched(int userId, String packageName) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean var7;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwgiP2QKOAJuJw40Kj5SVg==")));
               _data.writeInt(userId);
               _data.writeString(packageName);
               boolean _status = this.mRemote.transact(6, _data, _reply, 0);
               if (_status || IAppManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  boolean _result = 0 != _reply.readInt();
                  return _result;
               }

               var7 = IAppManager.Stub.getDefaultImpl().isPackageLaunched(userId, packageName);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var7;
         }

         public void setPackageHidden(int userId, String packageName, boolean hidden) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwgiP2QKOAJuJw40Kj5SVg==")));
               _data.writeInt(userId);
               _data.writeString(packageName);
               _data.writeInt(hidden ? 1 : 0);
               boolean _status = this.mRemote.transact(7, _data, _reply, 0);
               if (_status || IAppManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IAppManager.Stub.getDefaultImpl().setPackageHidden(userId, packageName, hidden);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public boolean installPackageAsUser(int userId, String packageName) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwgiP2QKOAJuJw40Kj5SVg==")));
               _data.writeInt(userId);
               _data.writeString(packageName);
               boolean _status = this.mRemote.transact(8, _data, _reply, 0);
               if (!_status && IAppManager.Stub.getDefaultImpl() != null) {
                  boolean var7 = IAppManager.Stub.getDefaultImpl().installPackageAsUser(userId, packageName);
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

         public boolean uninstallPackageAsUser(String packageName, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwgiP2QKOAJuJw40Kj5SVg==")));
               _data.writeString(packageName);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(9, _data, _reply, 0);
               if (!_status && IAppManager.Stub.getDefaultImpl() != null) {
                  boolean var7 = IAppManager.Stub.getDefaultImpl().uninstallPackageAsUser(packageName, userId);
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

         public boolean uninstallPackage(String packageName) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwgiP2QKOAJuJw40Kj5SVg==")));
               _data.writeString(packageName);
               boolean _status = this.mRemote.transact(10, _data, _reply, 0);
               if (!_status && IAppManager.Stub.getDefaultImpl() != null) {
                  boolean var6 = IAppManager.Stub.getDefaultImpl().uninstallPackage(packageName);
                  return var6;
               }

               _reply.readException();
               _result = 0 != _reply.readInt();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public List<InstalledAppInfo> getInstalledApps(int flags) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            List var6;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwgiP2QKOAJuJw40Kj5SVg==")));
               _data.writeInt(flags);
               boolean _status = this.mRemote.transact(11, _data, _reply, 0);
               if (_status || IAppManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  List<InstalledAppInfo> _result = _reply.createTypedArrayList(InstalledAppInfo.CREATOR);
                  return _result;
               }

               var6 = IAppManager.Stub.getDefaultImpl().getInstalledApps(flags);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var6;
         }

         public List<InstalledAppInfo> getInstalledAppsAsUser(int userId, int flags) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            ArrayList _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwgiP2QKOAJuJw40Kj5SVg==")));
               _data.writeInt(userId);
               _data.writeInt(flags);
               boolean _status = this.mRemote.transact(12, _data, _reply, 0);
               if (!_status && IAppManager.Stub.getDefaultImpl() != null) {
                  List var7 = IAppManager.Stub.getDefaultImpl().getInstalledAppsAsUser(userId, flags);
                  return var7;
               }

               _reply.readException();
               _result = _reply.createTypedArrayList(InstalledAppInfo.CREATOR);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public List<String> getInstalledSplitNames(String pkg) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            List var6;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwgiP2QKOAJuJw40Kj5SVg==")));
               _data.writeString(pkg);
               boolean _status = this.mRemote.transact(13, _data, _reply, 0);
               if (_status || IAppManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  List<String> _result = _reply.createStringArrayList();
                  return _result;
               }

               var6 = IAppManager.Stub.getDefaultImpl().getInstalledSplitNames(pkg);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var6;
         }

         public int getInstalledAppCount() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            int _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwgiP2QKOAJuJw40Kj5SVg==")));
               boolean _status = this.mRemote.transact(14, _data, _reply, 0);
               if (!_status && IAppManager.Stub.getDefaultImpl() != null) {
                  int var5 = IAppManager.Stub.getDefaultImpl().getInstalledAppCount();
                  return var5;
               }

               _reply.readException();
               _result = _reply.readInt();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public boolean isAppInstalled(String packageName) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean var6;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwgiP2QKOAJuJw40Kj5SVg==")));
               _data.writeString(packageName);
               boolean _status = this.mRemote.transact(15, _data, _reply, 0);
               if (_status || IAppManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  boolean _result = 0 != _reply.readInt();
                  return _result;
               }

               var6 = IAppManager.Stub.getDefaultImpl().isAppInstalled(packageName);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var6;
         }

         public boolean isAppInstalledAsUser(int userId, String packageName) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwgiP2QKOAJuJw40Kj5SVg==")));
               _data.writeInt(userId);
               _data.writeString(packageName);
               boolean _status = this.mRemote.transact(16, _data, _reply, 0);
               if (!_status && IAppManager.Stub.getDefaultImpl() != null) {
                  boolean var7 = IAppManager.Stub.getDefaultImpl().isAppInstalledAsUser(userId, packageName);
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

         public void registerObserver(IPackageObserver observer) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwgiP2QKOAJuJw40Kj5SVg==")));
               _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
               boolean _status = this.mRemote.transact(17, _data, _reply, 0);
               if (_status || IAppManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IAppManager.Stub.getDefaultImpl().registerObserver(observer);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void unregisterObserver(IPackageObserver observer) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwgiP2QKOAJuJw40Kj5SVg==")));
               _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
               boolean _status = this.mRemote.transact(18, _data, _reply, 0);
               if (_status || IAppManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IAppManager.Stub.getDefaultImpl().unregisterObserver(observer);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public boolean isRunInExtProcess(String packageName) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean var6;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwgiP2QKOAJuJw40Kj5SVg==")));
               _data.writeString(packageName);
               boolean _status = this.mRemote.transact(19, _data, _reply, 0);
               if (_status || IAppManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  boolean _result = 0 != _reply.readInt();
                  return _result;
               }

               var6 = IAppManager.Stub.getDefaultImpl().isRunInExtProcess(packageName);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var6;
         }

         public boolean cleanPackageData(String pkg, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean var7;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWIwgiP2QKOAJuJw40Kj5SVg==")));
               _data.writeString(pkg);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(20, _data, _reply, 0);
               if (_status || IAppManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  boolean _result = 0 != _reply.readInt();
                  return _result;
               }

               var7 = IAppManager.Stub.getDefaultImpl().cleanPackageData(pkg, userId);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var7;
         }
      }
   }

   public static class Default implements IAppManager {
      public int[] getPackageInstalledUsers(String packageName) throws RemoteException {
         return null;
      }

      public void scanApps() throws RemoteException {
      }

      public int getUidForSharedUser(String sharedUserName) throws RemoteException {
         return 0;
      }

      public InstalledAppInfo getInstalledAppInfo(String pkg, int flags) throws RemoteException {
         return null;
      }

      public VAppInstallerResult installPackage(Uri uri, VAppInstallerParams params) throws RemoteException {
         return null;
      }

      public boolean isPackageLaunched(int userId, String packageName) throws RemoteException {
         return false;
      }

      public void setPackageHidden(int userId, String packageName, boolean hidden) throws RemoteException {
      }

      public boolean installPackageAsUser(int userId, String packageName) throws RemoteException {
         return false;
      }

      public boolean uninstallPackageAsUser(String packageName, int userId) throws RemoteException {
         return false;
      }

      public boolean uninstallPackage(String packageName) throws RemoteException {
         return false;
      }

      public List<InstalledAppInfo> getInstalledApps(int flags) throws RemoteException {
         return null;
      }

      public List<InstalledAppInfo> getInstalledAppsAsUser(int userId, int flags) throws RemoteException {
         return null;
      }

      public List<String> getInstalledSplitNames(String pkg) throws RemoteException {
         return null;
      }

      public int getInstalledAppCount() throws RemoteException {
         return 0;
      }

      public boolean isAppInstalled(String packageName) throws RemoteException {
         return false;
      }

      public boolean isAppInstalledAsUser(int userId, String packageName) throws RemoteException {
         return false;
      }

      public void registerObserver(IPackageObserver observer) throws RemoteException {
      }

      public void unregisterObserver(IPackageObserver observer) throws RemoteException {
      }

      public boolean isRunInExtProcess(String packageName) throws RemoteException {
         return false;
      }

      public boolean cleanPackageData(String pkg, int userId) throws RemoteException {
         return false;
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
