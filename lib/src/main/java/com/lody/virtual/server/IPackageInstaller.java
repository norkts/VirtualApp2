package com.lody.virtual.server;

import android.content.IntentSender;
import android.content.pm.IPackageInstallerCallback;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import com.lody.virtual.remote.VParceledListSlice;
import com.lody.virtual.server.pm.installer.SessionInfo;
import com.lody.virtual.server.pm.installer.SessionParams;

public interface IPackageInstaller extends IInterface {
   int createSession(SessionParams var1, String var2, int var3) throws RemoteException;

   void updateSessionAppIcon(int var1, Bitmap var2) throws RemoteException;

   void updateSessionAppLabel(int var1, String var2) throws RemoteException;

   void abandonSession(int var1) throws RemoteException;

   android.content.pm.IPackageInstallerSession openSession(int var1) throws RemoteException;

   SessionInfo getSessionInfo(int var1) throws RemoteException;

   VParceledListSlice getAllSessions(int var1) throws RemoteException;

   VParceledListSlice getMySessions(String var1, int var2) throws RemoteException;

   void registerCallback(IPackageInstallerCallback var1, int var2) throws RemoteException;

   void unregisterCallback(IPackageInstallerCallback var1) throws RemoteException;

   void uninstall(String var1, String var2, int var3, IntentSender var4, int var5) throws RemoteException;

   void setPermissionsResult(int var1, boolean var2) throws RemoteException;

   public abstract static class Stub extends Binder implements IPackageInstaller {
      private static final String DESCRIPTOR = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylnDyAqKAg+O2sjNFBqJDAZOwdXO24FNFo="));
      static final int TRANSACTION_createSession = 1;
      static final int TRANSACTION_updateSessionAppIcon = 2;
      static final int TRANSACTION_updateSessionAppLabel = 3;
      static final int TRANSACTION_abandonSession = 4;
      static final int TRANSACTION_openSession = 5;
      static final int TRANSACTION_getSessionInfo = 6;
      static final int TRANSACTION_getAllSessions = 7;
      static final int TRANSACTION_getMySessions = 8;
      static final int TRANSACTION_registerCallback = 9;
      static final int TRANSACTION_unregisterCallback = 10;
      static final int TRANSACTION_uninstall = 11;
      static final int TRANSACTION_setPermissionsResult = 12;

      public Stub() {
         this.attachInterface(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylnDyAqKAg+O2sjNFBqJDAZOwdXO24FNFo=")));
      }

      public static IPackageInstaller asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IPackageInstaller)(iin != null && iin instanceof IPackageInstaller ? (IPackageInstaller)iin : new Proxy(obj));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
         String descriptor = DESCRIPTOR;
         int _arg0;
         int _arg2;
         String _arg0;
         IPackageInstallerCallback _arg0;
         String _arg1;
         int _arg1;
         switch (code) {
            case 1:
               data.enforceInterface(descriptor);
               SessionParams _arg0;
               if (0 != data.readInt()) {
                  _arg0 = (SessionParams)SessionParams.CREATOR.createFromParcel(data);
               } else {
                  _arg0 = null;
               }

               _arg1 = data.readString();
               _arg2 = data.readInt();
               int _result = this.createSession(_arg0, _arg1, _arg2);
               reply.writeNoException();
               reply.writeInt(_result);
               return true;
            case 2:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               Bitmap _arg1;
               if (0 != data.readInt()) {
                  _arg1 = (Bitmap)Bitmap.CREATOR.createFromParcel(data);
               } else {
                  _arg1 = null;
               }

               this.updateSessionAppIcon(_arg0, _arg1);
               reply.writeNoException();
               return true;
            case 3:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               _arg1 = data.readString();
               this.updateSessionAppLabel(_arg0, _arg1);
               reply.writeNoException();
               return true;
            case 4:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               this.abandonSession(_arg0);
               reply.writeNoException();
               return true;
            case 5:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               android.content.pm.IPackageInstallerSession _result = this.openSession(_arg0);
               reply.writeNoException();
               reply.writeStrongBinder(_result != null ? _result.asBinder() : null);
               return true;
            case 6:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               SessionInfo _result = this.getSessionInfo(_arg0);
               reply.writeNoException();
               if (_result != null) {
                  reply.writeInt(1);
                  _result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 7:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               VParceledListSlice _result = this.getAllSessions(_arg0);
               reply.writeNoException();
               if (_result != null) {
                  reply.writeInt(1);
                  _result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 8:
               data.enforceInterface(descriptor);
               _arg0 = data.readString();
               _arg1 = data.readInt();
               VParceledListSlice _result = this.getMySessions(_arg0, _arg1);
               reply.writeNoException();
               if (_result != null) {
                  reply.writeInt(1);
                  _result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 9:
               data.enforceInterface(descriptor);
               _arg0 = IPackageInstallerCallback.Stub.asInterface(data.readStrongBinder());
               _arg1 = data.readInt();
               this.registerCallback(_arg0, _arg1);
               reply.writeNoException();
               return true;
            case 10:
               data.enforceInterface(descriptor);
               _arg0 = IPackageInstallerCallback.Stub.asInterface(data.readStrongBinder());
               this.unregisterCallback(_arg0);
               reply.writeNoException();
               return true;
            case 11:
               data.enforceInterface(descriptor);
               _arg0 = data.readString();
               _arg1 = data.readString();
               _arg2 = data.readInt();
               IntentSender _arg3;
               if (0 != data.readInt()) {
                  _arg3 = (IntentSender)IntentSender.CREATOR.createFromParcel(data);
               } else {
                  _arg3 = null;
               }

               int _arg4 = data.readInt();
               this.uninstall(_arg0, _arg1, _arg2, _arg3, _arg4);
               reply.writeNoException();
               return true;
            case 12:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               boolean _arg1 = 0 != data.readInt();
               this.setPermissionsResult(_arg0, _arg1);
               reply.writeNoException();
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(IPackageInstaller impl) {
         if (IPackageInstaller.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGAFNC59ATAoLBUcD2ozOyB6DTwqLRgEKGIKESNqETwiKAgAVg==")));
         } else if (impl != null) {
            IPackageInstaller.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IPackageInstaller getDefaultImpl() {
         return IPackageInstaller.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IPackageInstaller {
         private IBinder mRemote;
         public static IPackageInstaller sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylnDyAqKAg+O2sjNFBqJDAZOwdXO24FNFo="));
         }

         public int createSession(SessionParams params, String installerPackageName, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            int _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylnDyAqKAg+O2sjNFBqJDAZOwdXO24FNFo=")));
               if (params != null) {
                  _data.writeInt(1);
                  params.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeString(installerPackageName);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (!_status && IPackageInstaller.Stub.getDefaultImpl() != null) {
                  int var8 = IPackageInstaller.Stub.getDefaultImpl().createSession(params, installerPackageName, userId);
                  return var8;
               }

               _reply.readException();
               _result = _reply.readInt();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public void updateSessionAppIcon(int sessionId, Bitmap appIcon) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylnDyAqKAg+O2sjNFBqJDAZOwdXO24FNFo=")));
               _data.writeInt(sessionId);
               if (appIcon != null) {
                  _data.writeInt(1);
                  appIcon.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (!_status && IPackageInstaller.Stub.getDefaultImpl() != null) {
                  IPackageInstaller.Stub.getDefaultImpl().updateSessionAppIcon(sessionId, appIcon);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void updateSessionAppLabel(int sessionId, String appLabel) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylnDyAqKAg+O2sjNFBqJDAZOwdXO24FNFo=")));
               _data.writeInt(sessionId);
               _data.writeString(appLabel);
               boolean _status = this.mRemote.transact(3, _data, _reply, 0);
               if (!_status && IPackageInstaller.Stub.getDefaultImpl() != null) {
                  IPackageInstaller.Stub.getDefaultImpl().updateSessionAppLabel(sessionId, appLabel);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void abandonSession(int sessionId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylnDyAqKAg+O2sjNFBqJDAZOwdXO24FNFo=")));
               _data.writeInt(sessionId);
               boolean _status = this.mRemote.transact(4, _data, _reply, 0);
               if (!_status && IPackageInstaller.Stub.getDefaultImpl() != null) {
                  IPackageInstaller.Stub.getDefaultImpl().abandonSession(sessionId);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public android.content.pm.IPackageInstallerSession openSession(int sessionId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            android.content.pm.IPackageInstallerSession _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylnDyAqKAg+O2sjNFBqJDAZOwdXO24FNFo=")));
               _data.writeInt(sessionId);
               boolean _status = this.mRemote.transact(5, _data, _reply, 0);
               if (!_status && IPackageInstaller.Stub.getDefaultImpl() != null) {
                  android.content.pm.IPackageInstallerSession var6 = IPackageInstaller.Stub.getDefaultImpl().openSession(sessionId);
                  return var6;
               }

               _reply.readException();
               _result = android.content.pm.IPackageInstallerSession.Stub.asInterface(_reply.readStrongBinder());
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public SessionInfo getSessionInfo(int sessionId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            SessionInfo _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylnDyAqKAg+O2sjNFBqJDAZOwdXO24FNFo=")));
               _data.writeInt(sessionId);
               boolean _status = this.mRemote.transact(6, _data, _reply, 0);
               if (!_status && IPackageInstaller.Stub.getDefaultImpl() != null) {
                  SessionInfo var6 = IPackageInstaller.Stub.getDefaultImpl().getSessionInfo(sessionId);
                  return var6;
               }

               _reply.readException();
               if (0 != _reply.readInt()) {
                  _result = (SessionInfo)SessionInfo.CREATOR.createFromParcel(_reply);
               } else {
                  _result = null;
               }
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public VParceledListSlice getAllSessions(int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            VParceledListSlice var6;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylnDyAqKAg+O2sjNFBqJDAZOwdXO24FNFo=")));
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(7, _data, _reply, 0);
               if (_status || IPackageInstaller.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  VParceledListSlice _result;
                  if (0 != _reply.readInt()) {
                     _result = (VParceledListSlice)VParceledListSlice.CREATOR.createFromParcel(_reply);
                  } else {
                     _result = null;
                  }

                  return _result;
               }

               var6 = IPackageInstaller.Stub.getDefaultImpl().getAllSessions(userId);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var6;
         }

         public VParceledListSlice getMySessions(String installerPackageName, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            VParceledListSlice var7;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylnDyAqKAg+O2sjNFBqJDAZOwdXO24FNFo=")));
               _data.writeString(installerPackageName);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(8, _data, _reply, 0);
               if (_status || IPackageInstaller.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  VParceledListSlice _result;
                  if (0 != _reply.readInt()) {
                     _result = (VParceledListSlice)VParceledListSlice.CREATOR.createFromParcel(_reply);
                  } else {
                     _result = null;
                  }

                  return _result;
               }

               var7 = IPackageInstaller.Stub.getDefaultImpl().getMySessions(installerPackageName, userId);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var7;
         }

         public void registerCallback(IPackageInstallerCallback callback, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylnDyAqKAg+O2sjNFBqJDAZOwdXO24FNFo=")));
               _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(9, _data, _reply, 0);
               if (!_status && IPackageInstaller.Stub.getDefaultImpl() != null) {
                  IPackageInstaller.Stub.getDefaultImpl().registerCallback(callback, userId);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void unregisterCallback(IPackageInstallerCallback callback) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylnDyAqKAg+O2sjNFBqJDAZOwdXO24FNFo=")));
               _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
               boolean _status = this.mRemote.transact(10, _data, _reply, 0);
               if (!_status && IPackageInstaller.Stub.getDefaultImpl() != null) {
                  IPackageInstaller.Stub.getDefaultImpl().unregisterCallback(callback);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void uninstall(String packageName, String callerPackageName, int flags, IntentSender statusReceiver, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylnDyAqKAg+O2sjNFBqJDAZOwdXO24FNFo=")));
               _data.writeString(packageName);
               _data.writeString(callerPackageName);
               _data.writeInt(flags);
               if (statusReceiver != null) {
                  _data.writeInt(1);
                  statusReceiver.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(11, _data, _reply, 0);
               if (_status || IPackageInstaller.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IPackageInstaller.Stub.getDefaultImpl().uninstall(packageName, callerPackageName, flags, statusReceiver, userId);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void setPermissionsResult(int sessionId, boolean accepted) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylnDyAqKAg+O2sjNFBqJDAZOwdXO24FNFo=")));
               _data.writeInt(sessionId);
               _data.writeInt(accepted ? 1 : 0);
               boolean _status = this.mRemote.transact(12, _data, _reply, 0);
               if (!_status && IPackageInstaller.Stub.getDefaultImpl() != null) {
                  IPackageInstaller.Stub.getDefaultImpl().setPermissionsResult(sessionId, accepted);
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

   public static class Default implements IPackageInstaller {
      public int createSession(SessionParams params, String installerPackageName, int userId) throws RemoteException {
         return 0;
      }

      public void updateSessionAppIcon(int sessionId, Bitmap appIcon) throws RemoteException {
      }

      public void updateSessionAppLabel(int sessionId, String appLabel) throws RemoteException {
      }

      public void abandonSession(int sessionId) throws RemoteException {
      }

      public android.content.pm.IPackageInstallerSession openSession(int sessionId) throws RemoteException {
         return null;
      }

      public SessionInfo getSessionInfo(int sessionId) throws RemoteException {
         return null;
      }

      public VParceledListSlice getAllSessions(int userId) throws RemoteException {
         return null;
      }

      public VParceledListSlice getMySessions(String installerPackageName, int userId) throws RemoteException {
         return null;
      }

      public void registerCallback(IPackageInstallerCallback callback, int userId) throws RemoteException {
      }

      public void unregisterCallback(IPackageInstallerCallback callback) throws RemoteException {
      }

      public void uninstall(String packageName, String callerPackageName, int flags, IntentSender statusReceiver, int userId) throws RemoteException {
      }

      public void setPermissionsResult(int sessionId, boolean accepted) throws RemoteException {
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
