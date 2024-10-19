package com.lody.virtual.server.interfaces;

import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import com.lody.virtual.os.VUserInfo;
import java.util.ArrayList;
import java.util.List;

public interface IUserManager extends IInterface {
   VUserInfo createUser(String var1, int var2) throws RemoteException;

   boolean removeUser(int var1) throws RemoteException;

   void setUserName(int var1, String var2) throws RemoteException;

   void setUserIcon(int var1, Bitmap var2) throws RemoteException;

   Bitmap getUserIcon(int var1) throws RemoteException;

   List<VUserInfo> getUsers(boolean var1) throws RemoteException;

   VUserInfo getUserInfo(int var1) throws RemoteException;

   void setGuestEnabled(boolean var1) throws RemoteException;

   boolean isGuestEnabled() throws RemoteException;

   void wipeUser(int var1) throws RemoteException;

   int getUserSerialNumber(int var1) throws RemoteException;

   int getUserHandle(int var1) throws RemoteException;

   public abstract static class Stub extends Binder implements IUserManager {
      private static final String DESCRIPTOR = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWKAguLGU2TTVvEV0yLQcMVg=="));
      static final int TRANSACTION_createUser = 1;
      static final int TRANSACTION_removeUser = 2;
      static final int TRANSACTION_setUserName = 3;
      static final int TRANSACTION_setUserIcon = 4;
      static final int TRANSACTION_getUserIcon = 5;
      static final int TRANSACTION_getUsers = 6;
      static final int TRANSACTION_getUserInfo = 7;
      static final int TRANSACTION_setGuestEnabled = 8;
      static final int TRANSACTION_isGuestEnabled = 9;
      static final int TRANSACTION_wipeUser = 10;
      static final int TRANSACTION_getUserSerialNumber = 11;
      static final int TRANSACTION_getUserHandle = 12;

      public Stub() {
         this.attachInterface(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWKAguLGU2TTVvEV0yLQcMVg==")));
      }

      public static IUserManager asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IUserManager)(iin != null && iin instanceof IUserManager ? (IUserManager)iin : new Proxy(obj));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
         String descriptor = DESCRIPTOR;
         int _arg0;
         int _result;
         boolean b_result;
         Bitmap _arg1;
         switch (code) {
            case 1:
               data.enforceInterface(descriptor);
               String s_arg0 = data.readString();
               _result = data.readInt();
               VUserInfo v_result = this.createUser(s_arg0, _result);
               reply.writeNoException();
               if (v_result != null) {
                  reply.writeInt(1);
                  v_result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 2:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               boolean br_result = this.removeUser(_arg0);
               reply.writeNoException();
               reply.writeInt(br_result ? 1 : 0);
               return true;
            case 3:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               String s_arg1 = data.readString();
               this.setUserName(_arg0, s_arg1);
               reply.writeNoException();
               return true;
            case 4:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               if (0 != data.readInt()) {
                  _arg1 = (Bitmap)Bitmap.CREATOR.createFromParcel(data);
               } else {
                  _arg1 = null;
               }

               this.setUserIcon(_arg0, _arg1);
               reply.writeNoException();
               return true;
            case 5:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               _arg1 = this.getUserIcon(_arg0);
               reply.writeNoException();
               if (_arg1 != null) {
                  reply.writeInt(1);
                  _arg1.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 6:
               data.enforceInterface(descriptor);
               b_result = 0 != data.readInt();
               List<VUserInfo> list_result = this.getUsers(b_result);
               reply.writeNoException();
               reply.writeTypedList(list_result);
               return true;
            case 7:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               VUserInfo vu_result = this.getUserInfo(_arg0);
               reply.writeNoException();
               if (vu_result != null) {
                  reply.writeInt(1);
                  vu_result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 8:
               data.enforceInterface(descriptor);
               b_result = 0 != data.readInt();
               this.setGuestEnabled(b_result);
               reply.writeNoException();
               return true;
            case 9:
               data.enforceInterface(descriptor);
               b_result = this.isGuestEnabled();
               reply.writeNoException();
               reply.writeInt(b_result ? 1 : 0);
               return true;
            case 10:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               this.wipeUser(_arg0);
               reply.writeNoException();
               return true;
            case 11:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               _result = this.getUserSerialNumber(_arg0);
               reply.writeNoException();
               reply.writeInt(_result);
               return true;
            case 12:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               _result = this.getUserHandle(_arg0);
               reply.writeNoException();
               reply.writeInt(_result);
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(IUserManager impl) {
         if (IUserManager.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGAFNC59ATAoLBUcD2ozOyB6DTwqLRgEKGIKESNqETwiKAgAVg==")));
         } else if (impl != null) {
            IUserManager.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IUserManager getDefaultImpl() {
         return IUserManager.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IUserManager {
         private IBinder mRemote;
         public static IUserManager sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWKAguLGU2TTVvEV0yLQcMVg=="));
         }

         public VUserInfo createUser(String name, int flags) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            VUserInfo _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWKAguLGU2TTVvEV0yLQcMVg==")));
               _data.writeString(name);
               _data.writeInt(flags);
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (!_status && IUserManager.Stub.getDefaultImpl() != null) {
                  VUserInfo var7 = IUserManager.Stub.getDefaultImpl().createUser(name, flags);
                  return var7;
               }

               _reply.readException();
               if (0 != _reply.readInt()) {
                  _result = (VUserInfo)VUserInfo.CREATOR.createFromParcel(_reply);
               } else {
                  _result = null;
               }
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public boolean removeUser(int userHandle) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean var6;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWKAguLGU2TTVvEV0yLQcMVg==")));
               _data.writeInt(userHandle);
               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (_status || IUserManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  boolean _result = 0 != _reply.readInt();
                  return _result;
               }

               var6 = IUserManager.Stub.getDefaultImpl().removeUser(userHandle);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var6;
         }

         public void setUserName(int userHandle, String name) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWKAguLGU2TTVvEV0yLQcMVg==")));
               _data.writeInt(userHandle);
               _data.writeString(name);
               boolean _status = this.mRemote.transact(3, _data, _reply, 0);
               if (_status || IUserManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IUserManager.Stub.getDefaultImpl().setUserName(userHandle, name);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void setUserIcon(int userHandle, Bitmap icon) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWKAguLGU2TTVvEV0yLQcMVg==")));
               _data.writeInt(userHandle);
               if (icon != null) {
                  _data.writeInt(1);
                  icon.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(4, _data, _reply, 0);
               if (!_status && IUserManager.Stub.getDefaultImpl() != null) {
                  IUserManager.Stub.getDefaultImpl().setUserIcon(userHandle, icon);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public Bitmap getUserIcon(int userHandle) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            Bitmap var6;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWKAguLGU2TTVvEV0yLQcMVg==")));
               _data.writeInt(userHandle);
               boolean _status = this.mRemote.transact(5, _data, _reply, 0);
               if (_status || IUserManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  Bitmap _result;
                  if (0 != _reply.readInt()) {
                     _result = (Bitmap)Bitmap.CREATOR.createFromParcel(_reply);
                  } else {
                     _result = null;
                  }

                  return _result;
               }

               var6 = IUserManager.Stub.getDefaultImpl().getUserIcon(userHandle);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var6;
         }

         public List<VUserInfo> getUsers(boolean excludeDying) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            ArrayList _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWKAguLGU2TTVvEV0yLQcMVg==")));
               _data.writeInt(excludeDying ? 1 : 0);
               boolean _status = this.mRemote.transact(6, _data, _reply, 0);
               if (!_status && IUserManager.Stub.getDefaultImpl() != null) {
                  List var6 = IUserManager.Stub.getDefaultImpl().getUsers(excludeDying);
                  return var6;
               }

               _reply.readException();
               _result = _reply.createTypedArrayList(VUserInfo.CREATOR);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public VUserInfo getUserInfo(int userHandle) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            VUserInfo _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWKAguLGU2TTVvEV0yLQcMVg==")));
               _data.writeInt(userHandle);
               boolean _status = this.mRemote.transact(7, _data, _reply, 0);
               if (!_status && IUserManager.Stub.getDefaultImpl() != null) {
                  VUserInfo var6 = IUserManager.Stub.getDefaultImpl().getUserInfo(userHandle);
                  return var6;
               }

               _reply.readException();
               if (0 != _reply.readInt()) {
                  _result = (VUserInfo)VUserInfo.CREATOR.createFromParcel(_reply);
               } else {
                  _result = null;
               }
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public void setGuestEnabled(boolean enable) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWKAguLGU2TTVvEV0yLQcMVg==")));
               _data.writeInt(enable ? 1 : 0);
               boolean _status = this.mRemote.transact(8, _data, _reply, 0);
               if (_status || IUserManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IUserManager.Stub.getDefaultImpl().setGuestEnabled(enable);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public boolean isGuestEnabled() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean var5;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWKAguLGU2TTVvEV0yLQcMVg==")));
               boolean _status = this.mRemote.transact(9, _data, _reply, 0);
               if (_status || IUserManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  boolean _result = 0 != _reply.readInt();
                  return _result;
               }

               var5 = IUserManager.Stub.getDefaultImpl().isGuestEnabled();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var5;
         }

         public void wipeUser(int userHandle) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWKAguLGU2TTVvEV0yLQcMVg==")));
               _data.writeInt(userHandle);
               boolean _status = this.mRemote.transact(10, _data, _reply, 0);
               if (_status || IUserManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IUserManager.Stub.getDefaultImpl().wipeUser(userHandle);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public int getUserSerialNumber(int userHandle) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            int _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWKAguLGU2TTVvEV0yLQcMVg==")));
               _data.writeInt(userHandle);
               boolean _status = this.mRemote.transact(11, _data, _reply, 0);
               if (!_status && IUserManager.Stub.getDefaultImpl() != null) {
                  int var6 = IUserManager.Stub.getDefaultImpl().getUserSerialNumber(userHandle);
                  return var6;
               }

               _reply.readException();
               _result = _reply.readInt();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public int getUserHandle(int userSerialNumber) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            int var6;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWKAguLGU2TTVvEV0yLQcMVg==")));
               _data.writeInt(userSerialNumber);
               boolean _status = this.mRemote.transact(12, _data, _reply, 0);
               if (_status || IUserManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  int _result = _reply.readInt();
                  return _result;
               }

               var6 = IUserManager.Stub.getDefaultImpl().getUserHandle(userSerialNumber);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var6;
         }
      }
   }

   public static class Default implements IUserManager {
      public VUserInfo createUser(String name, int flags) throws RemoteException {
         return null;
      }

      public boolean removeUser(int userHandle) throws RemoteException {
         return false;
      }

      public void setUserName(int userHandle, String name) throws RemoteException {
      }

      public void setUserIcon(int userHandle, Bitmap icon) throws RemoteException {
      }

      public Bitmap getUserIcon(int userHandle) throws RemoteException {
         return null;
      }

      public List<VUserInfo> getUsers(boolean excludeDying) throws RemoteException {
         return null;
      }

      public VUserInfo getUserInfo(int userHandle) throws RemoteException {
         return null;
      }

      public void setGuestEnabled(boolean enable) throws RemoteException {
      }

      public boolean isGuestEnabled() throws RemoteException {
         return false;
      }

      public void wipeUser(int userHandle) throws RemoteException {
      }

      public int getUserSerialNumber(int userHandle) throws RemoteException {
         return 0;
      }

      public int getUserHandle(int userSerialNumber) throws RemoteException {
         return 0;
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
