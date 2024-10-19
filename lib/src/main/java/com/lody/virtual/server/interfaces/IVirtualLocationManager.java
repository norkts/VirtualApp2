package com.lody.virtual.server.interfaces;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import com.lody.virtual.remote.vloc.VCell;
import com.lody.virtual.remote.vloc.VLocation;
import java.util.ArrayList;
import java.util.List;

public interface IVirtualLocationManager extends IInterface {
   int getMode(int var1, String var2) throws RemoteException;

   void setMode(int var1, String var2, int var3) throws RemoteException;

   void setCell(int var1, String var2, VCell var3) throws RemoteException;

   void setAllCell(int var1, String var2, List<VCell> var3) throws RemoteException;

   void setNeighboringCell(int var1, String var2, List<VCell> var3) throws RemoteException;

   void setGlobalCell(VCell var1) throws RemoteException;

   void setGlobalAllCell(List<VCell> var1) throws RemoteException;

   void setGlobalNeighboringCell(List<VCell> var1) throws RemoteException;

   VCell getCell(int var1, String var2) throws RemoteException;

   List<VCell> getAllCell(int var1, String var2) throws RemoteException;

   List<VCell> getNeighboringCell(int var1, String var2) throws RemoteException;

   void setLocation(int var1, String var2, VLocation var3) throws RemoteException;

   VLocation getLocation(int var1, String var2) throws RemoteException;

   void setGlobalLocation(VLocation var1) throws RemoteException;

   VLocation getGlobalLocation() throws RemoteException;

   public abstract static class Stub extends Binder implements IVirtualLocationManager {
      private static final String DESCRIPTOR = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWKD0AOWoVFjVvNSQ6Li4+LGUVGiZoDiA2Lwc6PWoVSFo="));
      static final int TRANSACTION_getMode = 1;
      static final int TRANSACTION_setMode = 2;
      static final int TRANSACTION_setCell = 3;
      static final int TRANSACTION_setAllCell = 4;
      static final int TRANSACTION_setNeighboringCell = 5;
      static final int TRANSACTION_setGlobalCell = 6;
      static final int TRANSACTION_setGlobalAllCell = 7;
      static final int TRANSACTION_setGlobalNeighboringCell = 8;
      static final int TRANSACTION_getCell = 9;
      static final int TRANSACTION_getAllCell = 10;
      static final int TRANSACTION_getNeighboringCell = 11;
      static final int TRANSACTION_setLocation = 12;
      static final int TRANSACTION_getLocation = 13;
      static final int TRANSACTION_setGlobalLocation = 14;
      static final int TRANSACTION_getGlobalLocation = 15;

      public Stub() {
         this.attachInterface(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWKD0AOWoVFjVvNSQ6Li4+LGUVGiZoDiA2Lwc6PWoVSFo=")));
      }

      public static IVirtualLocationManager asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IVirtualLocationManager)(iin != null && iin instanceof IVirtualLocationManager ? (IVirtualLocationManager)iin : new Proxy(obj));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
         String descriptor = DESCRIPTOR;
         VLocation _arg0;
         String _arg1;
         VLocation _arg2;
         int _arg0;
         ArrayList _arg0;
         List _result;
         VCell _arg2;
         ArrayList _arg2;
         int _arg2;
         switch (code) {
            case 1:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               _arg1 = data.readString();
               _arg2 = this.getMode(_arg0, _arg1);
               reply.writeNoException();
               reply.writeInt(_arg2);
               return true;
            case 2:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               _arg1 = data.readString();
               _arg2 = data.readInt();
               this.setMode(_arg0, _arg1, _arg2);
               reply.writeNoException();
               return true;
            case 3:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               _arg1 = data.readString();
               if (0 != data.readInt()) {
                  _arg2 = (VCell)VCell.CREATOR.createFromParcel(data);
               } else {
                  _arg2 = null;
               }

               this.setCell(_arg0, _arg1, _arg2);
               reply.writeNoException();
               return true;
            case 4:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               _arg1 = data.readString();
               _arg2 = data.createTypedArrayList(VCell.CREATOR);
               this.setAllCell(_arg0, _arg1, _arg2);
               reply.writeNoException();
               return true;
            case 5:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               _arg1 = data.readString();
               _arg2 = data.createTypedArrayList(VCell.CREATOR);
               this.setNeighboringCell(_arg0, _arg1, _arg2);
               reply.writeNoException();
               return true;
            case 6:
               data.enforceInterface(descriptor);
               VCell _arg0;
               if (0 != data.readInt()) {
                  _arg0 = (VCell)VCell.CREATOR.createFromParcel(data);
               } else {
                  _arg0 = null;
               }

               this.setGlobalCell(_arg0);
               reply.writeNoException();
               return true;
            case 7:
               data.enforceInterface(descriptor);
               _arg0 = data.createTypedArrayList(VCell.CREATOR);
               this.setGlobalAllCell(_arg0);
               reply.writeNoException();
               return true;
            case 8:
               data.enforceInterface(descriptor);
               _arg0 = data.createTypedArrayList(VCell.CREATOR);
               this.setGlobalNeighboringCell(_arg0);
               reply.writeNoException();
               return true;
            case 9:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               _arg1 = data.readString();
               _arg2 = this.getCell(_arg0, _arg1);
               reply.writeNoException();
               if (_arg2 != null) {
                  reply.writeInt(1);
                  _arg2.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 10:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               _arg1 = data.readString();
               _result = this.getAllCell(_arg0, _arg1);
               reply.writeNoException();
               reply.writeTypedList(_result);
               return true;
            case 11:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               _arg1 = data.readString();
               _result = this.getNeighboringCell(_arg0, _arg1);
               reply.writeNoException();
               reply.writeTypedList(_result);
               return true;
            case 12:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               _arg1 = data.readString();
               if (0 != data.readInt()) {
                  _arg2 = (VLocation)VLocation.CREATOR.createFromParcel(data);
               } else {
                  _arg2 = null;
               }

               this.setLocation(_arg0, _arg1, _arg2);
               reply.writeNoException();
               return true;
            case 13:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               _arg1 = data.readString();
               _arg2 = this.getLocation(_arg0, _arg1);
               reply.writeNoException();
               if (_arg2 != null) {
                  reply.writeInt(1);
                  _arg2.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 14:
               data.enforceInterface(descriptor);
               if (0 != data.readInt()) {
                  _arg0 = (VLocation)VLocation.CREATOR.createFromParcel(data);
               } else {
                  _arg0 = null;
               }

               this.setGlobalLocation(_arg0);
               reply.writeNoException();
               return true;
            case 15:
               data.enforceInterface(descriptor);
               _arg0 = this.getGlobalLocation();
               reply.writeNoException();
               if (_arg0 != null) {
                  reply.writeInt(1);
                  _arg0.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(IVirtualLocationManager impl) {
         if (IVirtualLocationManager.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGAFNC59ATAoLBUcD2ozOyB6DTwqLRgEKGIKESNqETwiKAgAVg==")));
         } else if (impl != null) {
            IVirtualLocationManager.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IVirtualLocationManager getDefaultImpl() {
         return IVirtualLocationManager.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IVirtualLocationManager {
         private IBinder mRemote;
         public static IVirtualLocationManager sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWKD0AOWoVFjVvNSQ6Li4+LGUVGiZoDiA2Lwc6PWoVSFo="));
         }

         public int getMode(int userId, String pkg) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            int _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWKD0AOWoVFjVvNSQ6Li4+LGUVGiZoDiA2Lwc6PWoVSFo=")));
               _data.writeInt(userId);
               _data.writeString(pkg);
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (!_status && IVirtualLocationManager.Stub.getDefaultImpl() != null) {
                  int var7 = IVirtualLocationManager.Stub.getDefaultImpl().getMode(userId, pkg);
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

         public void setMode(int userId, String pkg, int mode) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWKD0AOWoVFjVvNSQ6Li4+LGUVGiZoDiA2Lwc6PWoVSFo=")));
               _data.writeInt(userId);
               _data.writeString(pkg);
               _data.writeInt(mode);
               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (_status || IVirtualLocationManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IVirtualLocationManager.Stub.getDefaultImpl().setMode(userId, pkg, mode);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void setCell(int userId, String pkg, VCell cell) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWKD0AOWoVFjVvNSQ6Li4+LGUVGiZoDiA2Lwc6PWoVSFo=")));
               _data.writeInt(userId);
               _data.writeString(pkg);
               if (cell != null) {
                  _data.writeInt(1);
                  cell.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(3, _data, _reply, 0);
               if (!_status && IVirtualLocationManager.Stub.getDefaultImpl() != null) {
                  IVirtualLocationManager.Stub.getDefaultImpl().setCell(userId, pkg, cell);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void setAllCell(int userId, String pkg, List<VCell> cell) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWKD0AOWoVFjVvNSQ6Li4+LGUVGiZoDiA2Lwc6PWoVSFo=")));
               _data.writeInt(userId);
               _data.writeString(pkg);
               _data.writeTypedList(cell);
               boolean _status = this.mRemote.transact(4, _data, _reply, 0);
               if (_status || IVirtualLocationManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IVirtualLocationManager.Stub.getDefaultImpl().setAllCell(userId, pkg, cell);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void setNeighboringCell(int userId, String pkg, List<VCell> cell) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWKD0AOWoVFjVvNSQ6Li4+LGUVGiZoDiA2Lwc6PWoVSFo=")));
               _data.writeInt(userId);
               _data.writeString(pkg);
               _data.writeTypedList(cell);
               boolean _status = this.mRemote.transact(5, _data, _reply, 0);
               if (_status || IVirtualLocationManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IVirtualLocationManager.Stub.getDefaultImpl().setNeighboringCell(userId, pkg, cell);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void setGlobalCell(VCell cell) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWKD0AOWoVFjVvNSQ6Li4+LGUVGiZoDiA2Lwc6PWoVSFo=")));
               if (cell != null) {
                  _data.writeInt(1);
                  cell.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(6, _data, _reply, 0);
               if (!_status && IVirtualLocationManager.Stub.getDefaultImpl() != null) {
                  IVirtualLocationManager.Stub.getDefaultImpl().setGlobalCell(cell);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void setGlobalAllCell(List<VCell> cell) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWKD0AOWoVFjVvNSQ6Li4+LGUVGiZoDiA2Lwc6PWoVSFo=")));
               _data.writeTypedList(cell);
               boolean _status = this.mRemote.transact(7, _data, _reply, 0);
               if (!_status && IVirtualLocationManager.Stub.getDefaultImpl() != null) {
                  IVirtualLocationManager.Stub.getDefaultImpl().setGlobalAllCell(cell);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void setGlobalNeighboringCell(List<VCell> cell) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWKD0AOWoVFjVvNSQ6Li4+LGUVGiZoDiA2Lwc6PWoVSFo=")));
               _data.writeTypedList(cell);
               boolean _status = this.mRemote.transact(8, _data, _reply, 0);
               if (_status || IVirtualLocationManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IVirtualLocationManager.Stub.getDefaultImpl().setGlobalNeighboringCell(cell);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public VCell getCell(int userId, String pkg) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            VCell var7;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWKD0AOWoVFjVvNSQ6Li4+LGUVGiZoDiA2Lwc6PWoVSFo=")));
               _data.writeInt(userId);
               _data.writeString(pkg);
               boolean _status = this.mRemote.transact(9, _data, _reply, 0);
               if (_status || IVirtualLocationManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  VCell _result;
                  if (0 != _reply.readInt()) {
                     _result = (VCell)VCell.CREATOR.createFromParcel(_reply);
                  } else {
                     _result = null;
                  }

                  return _result;
               }

               var7 = IVirtualLocationManager.Stub.getDefaultImpl().getCell(userId, pkg);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var7;
         }

         public List<VCell> getAllCell(int userId, String pkg) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            ArrayList _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWKD0AOWoVFjVvNSQ6Li4+LGUVGiZoDiA2Lwc6PWoVSFo=")));
               _data.writeInt(userId);
               _data.writeString(pkg);
               boolean _status = this.mRemote.transact(10, _data, _reply, 0);
               if (!_status && IVirtualLocationManager.Stub.getDefaultImpl() != null) {
                  List var7 = IVirtualLocationManager.Stub.getDefaultImpl().getAllCell(userId, pkg);
                  return var7;
               }

               _reply.readException();
               _result = _reply.createTypedArrayList(VCell.CREATOR);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public List<VCell> getNeighboringCell(int userId, String pkg) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            ArrayList _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWKD0AOWoVFjVvNSQ6Li4+LGUVGiZoDiA2Lwc6PWoVSFo=")));
               _data.writeInt(userId);
               _data.writeString(pkg);
               boolean _status = this.mRemote.transact(11, _data, _reply, 0);
               if (!_status && IVirtualLocationManager.Stub.getDefaultImpl() != null) {
                  List var7 = IVirtualLocationManager.Stub.getDefaultImpl().getNeighboringCell(userId, pkg);
                  return var7;
               }

               _reply.readException();
               _result = _reply.createTypedArrayList(VCell.CREATOR);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public void setLocation(int userId, String pkg, VLocation loc) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWKD0AOWoVFjVvNSQ6Li4+LGUVGiZoDiA2Lwc6PWoVSFo=")));
               _data.writeInt(userId);
               _data.writeString(pkg);
               if (loc != null) {
                  _data.writeInt(1);
                  loc.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(12, _data, _reply, 0);
               if (_status || IVirtualLocationManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IVirtualLocationManager.Stub.getDefaultImpl().setLocation(userId, pkg, loc);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public VLocation getLocation(int userId, String pkg) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            VLocation var7;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWKD0AOWoVFjVvNSQ6Li4+LGUVGiZoDiA2Lwc6PWoVSFo=")));
               _data.writeInt(userId);
               _data.writeString(pkg);
               boolean _status = this.mRemote.transact(13, _data, _reply, 0);
               if (_status || IVirtualLocationManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  VLocation _result;
                  if (0 != _reply.readInt()) {
                     _result = (VLocation)VLocation.CREATOR.createFromParcel(_reply);
                  } else {
                     _result = null;
                  }

                  return _result;
               }

               var7 = IVirtualLocationManager.Stub.getDefaultImpl().getLocation(userId, pkg);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var7;
         }

         public void setGlobalLocation(VLocation loc) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWKD0AOWoVFjVvNSQ6Li4+LGUVGiZoDiA2Lwc6PWoVSFo=")));
               if (loc != null) {
                  _data.writeInt(1);
                  loc.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(14, _data, _reply, 0);
               if (!_status && IVirtualLocationManager.Stub.getDefaultImpl() != null) {
                  IVirtualLocationManager.Stub.getDefaultImpl().setGlobalLocation(loc);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public VLocation getGlobalLocation() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            VLocation var5;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWKD0AOWoVFjVvNSQ6Li4+LGUVGiZoDiA2Lwc6PWoVSFo=")));
               boolean _status = this.mRemote.transact(15, _data, _reply, 0);
               if (_status || IVirtualLocationManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  VLocation _result;
                  if (0 != _reply.readInt()) {
                     _result = (VLocation)VLocation.CREATOR.createFromParcel(_reply);
                  } else {
                     _result = null;
                  }

                  return _result;
               }

               var5 = IVirtualLocationManager.Stub.getDefaultImpl().getGlobalLocation();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var5;
         }
      }
   }

   public static class Default implements IVirtualLocationManager {
      public int getMode(int userId, String pkg) throws RemoteException {
         return 0;
      }

      public void setMode(int userId, String pkg, int mode) throws RemoteException {
      }

      public void setCell(int userId, String pkg, VCell cell) throws RemoteException {
      }

      public void setAllCell(int userId, String pkg, List<VCell> cell) throws RemoteException {
      }

      public void setNeighboringCell(int userId, String pkg, List<VCell> cell) throws RemoteException {
      }

      public void setGlobalCell(VCell cell) throws RemoteException {
      }

      public void setGlobalAllCell(List<VCell> cell) throws RemoteException {
      }

      public void setGlobalNeighboringCell(List<VCell> cell) throws RemoteException {
      }

      public VCell getCell(int userId, String pkg) throws RemoteException {
         return null;
      }

      public List<VCell> getAllCell(int userId, String pkg) throws RemoteException {
         return null;
      }

      public List<VCell> getNeighboringCell(int userId, String pkg) throws RemoteException {
         return null;
      }

      public void setLocation(int userId, String pkg, VLocation loc) throws RemoteException {
      }

      public VLocation getLocation(int userId, String pkg) throws RemoteException {
         return null;
      }

      public void setGlobalLocation(VLocation loc) throws RemoteException {
      }

      public VLocation getGlobalLocation() throws RemoteException {
         return null;
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
