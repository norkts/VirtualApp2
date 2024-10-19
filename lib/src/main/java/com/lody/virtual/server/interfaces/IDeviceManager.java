package com.lody.virtual.server.interfaces;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import com.lody.virtual.remote.VDeviceConfig;

public interface IDeviceManager extends IInterface {
   VDeviceConfig getDeviceConfig(int var1) throws RemoteException;

   void updateDeviceConfig(int var1, VDeviceConfig var2) throws RemoteException;

   boolean isEnable(int var1) throws RemoteException;

   void setEnable(int var1, boolean var2) throws RemoteException;

   public abstract static class Stub extends Binder implements IDeviceManager {
      private static final String DESCRIPTOR = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWLBc2PW8KMDFnJ105LggmM28jSFo="));
      static final int TRANSACTION_getDeviceConfig = 1;
      static final int TRANSACTION_updateDeviceConfig = 2;
      static final int TRANSACTION_isEnable = 3;
      static final int TRANSACTION_setEnable = 4;

      public Stub() {
         this.attachInterface(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWLBc2PW8KMDFnJ105LggmM28jSFo=")));
      }

      public static IDeviceManager asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IDeviceManager)(iin != null && iin instanceof IDeviceManager ? (IDeviceManager)iin : new Proxy(obj));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
         String descriptor = DESCRIPTOR;
         int _arg0;
         boolean _arg1;
         VDeviceConfig _arg1;
         switch (code) {
            case 1:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               _arg1 = this.getDeviceConfig(_arg0);
               reply.writeNoException();
               if (_arg1 != null) {
                  reply.writeInt(1);
                  _arg1.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 2:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               if (0 != data.readInt()) {
                  _arg1 = (VDeviceConfig)VDeviceConfig.CREATOR.createFromParcel(data);
               } else {
                  _arg1 = null;
               }

               this.updateDeviceConfig(_arg0, _arg1);
               reply.writeNoException();
               return true;
            case 3:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               _arg1 = this.isEnable(_arg0);
               reply.writeNoException();
               reply.writeInt(_arg1 ? 1 : 0);
               return true;
            case 4:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               _arg1 = 0 != data.readInt();
               this.setEnable(_arg0, _arg1);
               reply.writeNoException();
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(IDeviceManager impl) {
         if (IDeviceManager.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGAFNC59ATAoLBUcD2ozOyB6DTwqLRgEKGIKESNqETwiKAgAVg==")));
         } else if (impl != null) {
            IDeviceManager.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IDeviceManager getDefaultImpl() {
         return IDeviceManager.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IDeviceManager {
         private IBinder mRemote;
         public static IDeviceManager sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWLBc2PW8KMDFnJ105LggmM28jSFo="));
         }

         public VDeviceConfig getDeviceConfig(int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            VDeviceConfig var6;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWLBc2PW8KMDFnJ105LggmM28jSFo=")));
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (_status || IDeviceManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  VDeviceConfig _result;
                  if (0 != _reply.readInt()) {
                     _result = (VDeviceConfig)VDeviceConfig.CREATOR.createFromParcel(_reply);
                  } else {
                     _result = null;
                  }

                  return _result;
               }

               var6 = IDeviceManager.Stub.getDefaultImpl().getDeviceConfig(userId);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var6;
         }

         public void updateDeviceConfig(int userId, VDeviceConfig config) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWLBc2PW8KMDFnJ105LggmM28jSFo=")));
               _data.writeInt(userId);
               if (config != null) {
                  _data.writeInt(1);
                  config.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (!_status && IDeviceManager.Stub.getDefaultImpl() != null) {
                  IDeviceManager.Stub.getDefaultImpl().updateDeviceConfig(userId, config);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public boolean isEnable(int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWLBc2PW8KMDFnJ105LggmM28jSFo=")));
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(3, _data, _reply, 0);
               if (!_status && IDeviceManager.Stub.getDefaultImpl() != null) {
                  boolean var6 = IDeviceManager.Stub.getDefaultImpl().isEnable(userId);
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

         public void setEnable(int userId, boolean enable) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWLBc2PW8KMDFnJ105LggmM28jSFo=")));
               _data.writeInt(userId);
               _data.writeInt(enable ? 1 : 0);
               boolean _status = this.mRemote.transact(4, _data, _reply, 0);
               if (!_status && IDeviceManager.Stub.getDefaultImpl() != null) {
                  IDeviceManager.Stub.getDefaultImpl().setEnable(userId, enable);
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

   public static class Default implements IDeviceManager {
      public VDeviceConfig getDeviceConfig(int userId) throws RemoteException {
         return null;
      }

      public void updateDeviceConfig(int userId, VDeviceConfig config) throws RemoteException {
      }

      public boolean isEnable(int userId) throws RemoteException {
         return false;
      }

      public void setEnable(int userId, boolean enable) throws RemoteException {
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
