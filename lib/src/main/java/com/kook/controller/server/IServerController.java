package com.kook.controller.server;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.carlos.libcommon.StringFog;

public interface IServerController extends IInterface {
   void setClientApplication(String var1, IBinder var2) throws RemoteException;

   void setWidth(int var1) throws RemoteException;

   void setHeight(int var1) throws RemoteException;

   void hideFloatWindow(boolean var1) throws RemoteException;

   void setNeedAttach(boolean var1) throws RemoteException;

   void show(String var1, IBinder var2) throws RemoteException;

   void hide() throws RemoteException;

   boolean virtualClick(int var1, int var2) throws RemoteException;

   boolean virtualTouch(int var1, int var2, int var3, int var4, boolean var5) throws RemoteException;

   boolean sendKeyEvent(int var1) throws RemoteException;

   public abstract static class Stub extends Binder implements IServerController {
      private static final String DESCRIPTOR = "com.kook.controller.server.IServerController";
      static final int TRANSACTION_setClientApplication = 1;
      static final int TRANSACTION_setWidth = 2;
      static final int TRANSACTION_setHeight = 3;
      static final int TRANSACTION_hideFloatWindow = 4;
      static final int TRANSACTION_setNeedAttach = 5;
      static final int TRANSACTION_show = 6;
      static final int TRANSACTION_hide = 7;
      static final int TRANSACTION_virtualClick = 8;
      static final int TRANSACTION_virtualTouch = 9;
      static final int TRANSACTION_sendKeyEvent = 10;

      public Stub() {
         this.attachInterface(this, "com.kook.controller.server.IServerController");
      }

      public static IServerController asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IServerController)(iin != null && iin instanceof IServerController ? (IServerController)iin : new Proxy(obj));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
         String descriptor = DESCRIPTOR;
         int _arg0;
         String s_arg0;
         boolean b_arg0;
         int _arg1;
         IBinder binder_arg1;
         switch (code) {
            case 1:
               data.enforceInterface(descriptor);
               s_arg0 = data.readString();
               binder_arg1 = data.readStrongBinder();
               this.setClientApplication(s_arg0, binder_arg1);
               reply.writeNoException();
               return true;
            case 2:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               this.setWidth(_arg0);
               reply.writeNoException();
               return true;
            case 3:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               this.setHeight(_arg0);
               reply.writeNoException();
               return true;
            case 4:
               data.enforceInterface(descriptor);
               b_arg0 = 0 != data.readInt();
               this.hideFloatWindow(b_arg0);
               reply.writeNoException();
               return true;
            case 5:
               data.enforceInterface(descriptor);
               b_arg0 = 0 != data.readInt();
               this.setNeedAttach(b_arg0);
               reply.writeNoException();
               return true;
            case 6:
               data.enforceInterface(descriptor);
               s_arg0 = data.readString();
               binder_arg1 = data.readStrongBinder();
               this.show(s_arg0, binder_arg1);
               reply.writeNoException();
               return true;
            case 7:
               data.enforceInterface(descriptor);
               this.hide();
               reply.writeNoException();
               return true;
            case 8:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               _arg1 = data.readInt();
               boolean _result = this.virtualClick(_arg0, _arg1);
               reply.writeNoException();
               reply.writeInt(_result ? 1 : 0);
               return true;
            case 9:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               _arg1 = data.readInt();
               int _arg2 = data.readInt();
               int _arg3 = data.readInt();
               boolean _arg4 = 0 != data.readInt();
               boolean b_result = this.virtualTouch(_arg0, _arg1, _arg2, _arg3, _arg4);
               reply.writeNoException();
               reply.writeInt(b_result ? 1 : 0);
               return true;
            case 10:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               b_result = this.sendKeyEvent(_arg0);
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

      public static boolean setDefaultImpl(IServerController impl) {
         if (IServerController.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException("setDefaultImpl() called twice");
         } else if (impl != null) {
            IServerController.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IServerController getDefaultImpl() {
         return IServerController.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IServerController {
         private IBinder mRemote;
         public static IServerController sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return "com.kook.controller.server.IServerController";
         }

         public void setClientApplication(String packageName, IBinder ibinder) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("com.kook.controller.server.IServerController");
               _data.writeString(packageName);
               _data.writeStrongBinder(ibinder);
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (_status || IServerController.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IServerController.Stub.getDefaultImpl().setClientApplication(packageName, ibinder);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void setWidth(int width) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("com.kook.controller.server.IServerController");
               _data.writeInt(width);
               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (_status || IServerController.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IServerController.Stub.getDefaultImpl().setWidth(width);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void setHeight(int height) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("com.kook.controller.server.IServerController");
               _data.writeInt(height);
               boolean _status = this.mRemote.transact(3, _data, _reply, 0);
               if (!_status && IServerController.Stub.getDefaultImpl() != null) {
                  IServerController.Stub.getDefaultImpl().setHeight(height);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void hideFloatWindow(boolean hide) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("com.kook.controller.server.IServerController");
               _data.writeInt(hide ? 1 : 0);
               boolean _status = this.mRemote.transact(4, _data, _reply, 0);
               if (!_status && IServerController.Stub.getDefaultImpl() != null) {
                  IServerController.Stub.getDefaultImpl().hideFloatWindow(hide);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void setNeedAttach(boolean needAttach) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("com.kook.controller.server.IServerController");
               _data.writeInt(needAttach ? 1 : 0);
               boolean _status = this.mRemote.transact(5, _data, _reply, 0);
               if (_status || IServerController.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IServerController.Stub.getDefaultImpl().setNeedAttach(needAttach);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void show(String pkgName, IBinder ibinder) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("com.kook.controller.server.IServerController");
               _data.writeString(pkgName);
               _data.writeStrongBinder(ibinder);
               boolean _status = this.mRemote.transact(6, _data, _reply, 0);
               if (!_status && IServerController.Stub.getDefaultImpl() != null) {
                  IServerController.Stub.getDefaultImpl().show(pkgName, ibinder);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void hide() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("com.kook.controller.server.IServerController");
               boolean _status = this.mRemote.transact(7, _data, _reply, 0);
               if (!_status && IServerController.Stub.getDefaultImpl() != null) {
                  IServerController.Stub.getDefaultImpl().hide();
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public boolean virtualClick(int centerX, int centerY) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean var7;
            try {
               _data.writeInterfaceToken("com.kook.controller.server.IServerController");
               _data.writeInt(centerX);
               _data.writeInt(centerY);
               boolean _status = this.mRemote.transact(8, _data, _reply, 0);
               if (_status || IServerController.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  boolean _result = 0 != _reply.readInt();
                  return _result;
               }

               var7 = IServerController.Stub.getDefaultImpl().virtualClick(centerX, centerY);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var7;
         }

         public boolean virtualTouch(int fromX, int fromY, int toX, int toY, boolean direction) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean _result;
            try {
               _data.writeInterfaceToken("com.kook.controller.server.IServerController");
               _data.writeInt(fromX);
               _data.writeInt(fromY);
               _data.writeInt(toX);
               _data.writeInt(toY);
               _data.writeInt(direction ? 1 : 0);
               boolean _status = this.mRemote.transact(9, _data, _reply, 0);
               if (!_status && IServerController.Stub.getDefaultImpl() != null) {
                  boolean var10 = IServerController.Stub.getDefaultImpl().virtualTouch(fromX, fromY, toX, toY, direction);
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

         public boolean sendKeyEvent(int keycode) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean var6;
            try {
               _data.writeInterfaceToken("com.kook.controller.server.IServerController");
               _data.writeInt(keycode);
               boolean _status = this.mRemote.transact(10, _data, _reply, 0);
               if (_status || IServerController.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  boolean _result = 0 != _reply.readInt();
                  return _result;
               }

               var6 = IServerController.Stub.getDefaultImpl().sendKeyEvent(keycode);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var6;
         }
      }
   }

   public static class Default implements IServerController {
      public void setClientApplication(String packageName, IBinder ibinder) throws RemoteException {
      }

      public void setWidth(int width) throws RemoteException {
      }

      public void setHeight(int height) throws RemoteException {
      }

      public void hideFloatWindow(boolean hide) throws RemoteException {
      }

      public void setNeedAttach(boolean needAttach) throws RemoteException {
      }

      public void show(String pkgName, IBinder ibinder) throws RemoteException {
      }

      public void hide() throws RemoteException {
      }

      public boolean virtualClick(int centerX, int centerY) throws RemoteException {
         return false;
      }

      public boolean virtualTouch(int fromX, int fromY, int toX, int toY, boolean direction) throws RemoteException {
         return false;
      }

      public boolean sendKeyEvent(int keycode) throws RemoteException {
         return false;
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
