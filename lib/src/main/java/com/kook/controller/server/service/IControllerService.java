package com.kook.controller.server.service;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.carlos.libcommon.StringFog;

public interface IControllerService extends IInterface {
   boolean picCompare(String var1, String var2) throws RemoteException;

   void startCheck(String var1, int var2) throws RemoteException;

   void registerCallBack(IServiceCallBack var1) throws RemoteException;

   void unregisterCallBack(IServiceCallBack var1) throws RemoteException;

   void startFullWakeLock() throws RemoteException;

   void eventDialogTips() throws RemoteException;

   void setOptionAction(String var1) throws RemoteException;

   String getOptionAction() throws RemoteException;

   boolean killApp(String var1, String var2) throws RemoteException;

   boolean isAppOnForeground() throws RemoteException;

   boolean virtualClick(int var1, int var2) throws RemoteException;

   boolean virtualTouch(int var1, int var2, int var3, int var4, boolean var5) throws RemoteException;

   boolean sendKeyEvent(int var1) throws RemoteException;

   public abstract static class Stub extends Binder implements IControllerService {
      private static final String DESCRIPTOR = "com.kook.controller.server.service.IControllerService";
      static final int TRANSACTION_picCompare = 1;
      static final int TRANSACTION_startCheck = 2;
      static final int TRANSACTION_registerCallBack = 3;
      static final int TRANSACTION_unregisterCallBack = 4;
      static final int TRANSACTION_startFullWakeLock = 5;
      static final int TRANSACTION_eventDialogTips = 6;
      static final int TRANSACTION_setOptionAction = 7;
      static final int TRANSACTION_getOptionAction = 8;
      static final int TRANSACTION_killApp = 9;
      static final int TRANSACTION_isAppOnForeground = 10;
      static final int TRANSACTION_virtualClick = 11;
      static final int TRANSACTION_virtualTouch = 12;
      static final int TRANSACTION_sendKeyEvent = 13;

      public Stub() {
         this.attachInterface(this, "com.kook.controller.server.service.IControllerService");
      }

      public static IControllerService asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IControllerService)(iin != null && iin instanceof IControllerService ? (IControllerService)iin : new Proxy(obj));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
         String descriptor = DESCRIPTOR;
         int _arg0;
         String s_arg0;
         int _arg1;
         String s_arg1;
         IServiceCallBack isc_arg0;
         boolean _result;
         switch (code) {
            case 1:
               data.enforceInterface(descriptor);
               s_arg0 = data.readString();
               s_arg1 = data.readString();
               _result = this.picCompare(s_arg0, s_arg1);
               reply.writeNoException();
               reply.writeInt(_result ? 1 : 0);
               return true;
            case 2:
               data.enforceInterface(descriptor);
               s_arg0 = data.readString();
               _arg1 = data.readInt();
               this.startCheck(s_arg0, _arg1);
               reply.writeNoException();
               return true;
            case 3:
               data.enforceInterface(descriptor);
               isc_arg0 = IServiceCallBack.Stub.asInterface(data.readStrongBinder());
               this.registerCallBack(isc_arg0);
               reply.writeNoException();
               return true;
            case 4:
               data.enforceInterface(descriptor);
               isc_arg0 = IServiceCallBack.Stub.asInterface(data.readStrongBinder());
               this.unregisterCallBack(isc_arg0);
               reply.writeNoException();
               return true;
            case 5:
               data.enforceInterface(descriptor);
               this.startFullWakeLock();
               reply.writeNoException();
               return true;
            case 6:
               data.enforceInterface(descriptor);
               this.eventDialogTips();
               reply.writeNoException();
               return true;
            case 7:
               data.enforceInterface(descriptor);
               s_arg0 = data.readString();
               this.setOptionAction(s_arg0);
               reply.writeNoException();
               return true;
            case 8:
               data.enforceInterface(descriptor);
               s_arg0 = this.getOptionAction();
               reply.writeNoException();
               reply.writeString(s_arg0);
               return true;
            case 9:
               data.enforceInterface(descriptor);
               s_arg0 = data.readString();
               s_arg1 = data.readString();
               _result = this.killApp(s_arg0, s_arg1);
               reply.writeNoException();
               reply.writeInt(_result ? 1 : 0);
               return true;
            case 10:
               data.enforceInterface(descriptor);
               boolean b_result = this.isAppOnForeground();
               reply.writeNoException();
               reply.writeInt(b_result ? 1 : 0);
               return true;
            case 11:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               _arg1 = data.readInt();
               _result = this.virtualClick(_arg0, _arg1);
               reply.writeNoException();
               reply.writeInt(_result ? 1 : 0);
               return true;
            case 12:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               _arg1 = data.readInt();
               int _arg2 = data.readInt();
               int _arg3 = data.readInt();
               boolean _arg4 = 0 != data.readInt();
               b_result = this.virtualTouch(_arg0, _arg1, _arg2, _arg3, _arg4);
               reply.writeNoException();
               reply.writeInt(b_result ? 1 : 0);
               return true;
            case 13:
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

      public static boolean setDefaultImpl(IControllerService impl) {
         if (IControllerService.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException("setDefaultImpl() called twice");
         } else if (impl != null) {
            IControllerService.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IControllerService getDefaultImpl() {
         return IControllerService.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IControllerService {
         private IBinder mRemote;
         public static IControllerService sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return "com.kook.controller.server.service.IControllerService";
         }

         public boolean picCompare(String picPathSource, String picPathTarget) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean _result;
            try {
               _data.writeInterfaceToken("com.kook.controller.server.service.IControllerService");
               _data.writeString(picPathSource);
               _data.writeString(picPathTarget);
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (!_status && IControllerService.Stub.getDefaultImpl() != null) {
                  boolean var7 = IControllerService.Stub.getDefaultImpl().picCompare(picPathSource, picPathTarget);
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

         public void startCheck(String productPicPathSource, int targetProductIndex) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("com.kook.controller.server.service.IControllerService");
               _data.writeString(productPicPathSource);
               _data.writeInt(targetProductIndex);
               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (_status || IControllerService.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IControllerService.Stub.getDefaultImpl().startCheck(productPicPathSource, targetProductIndex);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void registerCallBack(IServiceCallBack serviceCallBack) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("com.kook.controller.server.service.IControllerService");
               _data.writeStrongBinder(serviceCallBack != null ? serviceCallBack.asBinder() : null);
               boolean _status = this.mRemote.transact(3, _data, _reply, 0);
               if (!_status && IControllerService.Stub.getDefaultImpl() != null) {
                  IControllerService.Stub.getDefaultImpl().registerCallBack(serviceCallBack);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void unregisterCallBack(IServiceCallBack serviceCallBack) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("com.kook.controller.server.service.IControllerService");
               _data.writeStrongBinder(serviceCallBack != null ? serviceCallBack.asBinder() : null);
               boolean _status = this.mRemote.transact(4, _data, _reply, 0);
               if (!_status && IControllerService.Stub.getDefaultImpl() != null) {
                  IControllerService.Stub.getDefaultImpl().unregisterCallBack(serviceCallBack);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void startFullWakeLock() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("com.kook.controller.server.service.IControllerService");
               boolean _status = this.mRemote.transact(5, _data, _reply, 0);
               if (!_status && IControllerService.Stub.getDefaultImpl() != null) {
                  IControllerService.Stub.getDefaultImpl().startFullWakeLock();
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void eventDialogTips() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("com.kook.controller.server.service.IControllerService");
               boolean _status = this.mRemote.transact(6, _data, _reply, 0);
               if (!_status && IControllerService.Stub.getDefaultImpl() != null) {
                  IControllerService.Stub.getDefaultImpl().eventDialogTips();
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void setOptionAction(String action) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("com.kook.controller.server.service.IControllerService");
               _data.writeString(action);
               boolean _status = this.mRemote.transact(7, _data, _reply, 0);
               if (!_status && IControllerService.Stub.getDefaultImpl() != null) {
                  IControllerService.Stub.getDefaultImpl().setOptionAction(action);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public String getOptionAction() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            String var5;
            try {
               _data.writeInterfaceToken("com.kook.controller.server.service.IControllerService");
               boolean _status = this.mRemote.transact(8, _data, _reply, 0);
               if (_status || IControllerService.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  String _result = _reply.readString();
                  return _result;
               }

               var5 = IControllerService.Stub.getDefaultImpl().getOptionAction();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var5;
         }

         public boolean killApp(String pkg, String processName) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean _result;
            try {
               _data.writeInterfaceToken("com.kook.controller.server.service.IControllerService");
               _data.writeString(pkg);
               _data.writeString(processName);
               boolean _status = this.mRemote.transact(9, _data, _reply, 0);
               if (!_status && IControllerService.Stub.getDefaultImpl() != null) {
                  boolean var7 = IControllerService.Stub.getDefaultImpl().killApp(pkg, processName);
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

         public boolean isAppOnForeground() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean var5;
            try {
               _data.writeInterfaceToken("com.kook.controller.server.service.IControllerService");
               boolean _status = this.mRemote.transact(10, _data, _reply, 0);
               if (_status || IControllerService.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  boolean _result = 0 != _reply.readInt();
                  return _result;
               }

               var5 = IControllerService.Stub.getDefaultImpl().isAppOnForeground();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var5;
         }

         public boolean virtualClick(int centerX, int centerY) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean var7;
            try {
               _data.writeInterfaceToken("com.kook.controller.server.service.IControllerService");
               _data.writeInt(centerX);
               _data.writeInt(centerY);
               boolean _status = this.mRemote.transact(11, _data, _reply, 0);
               if (_status || IControllerService.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  boolean _result = 0 != _reply.readInt();
                  return _result;
               }

               var7 = IControllerService.Stub.getDefaultImpl().virtualClick(centerX, centerY);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var7;
         }

         public boolean virtualTouch(int fromX, int fromY, int toX, int toY, boolean direction) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean var10;
            try {
               _data.writeInterfaceToken("com.kook.controller.server.service.IControllerService");
               _data.writeInt(fromX);
               _data.writeInt(fromY);
               _data.writeInt(toX);
               _data.writeInt(toY);
               _data.writeInt(direction ? 1 : 0);
               boolean _status = this.mRemote.transact(12, _data, _reply, 0);
               if (_status || IControllerService.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  boolean _result = 0 != _reply.readInt();
                  return _result;
               }

               var10 = IControllerService.Stub.getDefaultImpl().virtualTouch(fromX, fromY, toX, toY, direction);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var10;
         }

         public boolean sendKeyEvent(int keycode) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean var6;
            try {
               _data.writeInterfaceToken("com.kook.controller.server.service.IControllerService");
               _data.writeInt(keycode);
               boolean _status = this.mRemote.transact(13, _data, _reply, 0);
               if (_status || IControllerService.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  boolean _result = 0 != _reply.readInt();
                  return _result;
               }

               var6 = IControllerService.Stub.getDefaultImpl().sendKeyEvent(keycode);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var6;
         }
      }
   }

   public static class Default implements IControllerService {
      public boolean picCompare(String picPathSource, String picPathTarget) throws RemoteException {
         return false;
      }

      public void startCheck(String productPicPathSource, int targetProductIndex) throws RemoteException {
      }

      public void registerCallBack(IServiceCallBack serviceCallBack) throws RemoteException {
      }

      public void unregisterCallBack(IServiceCallBack serviceCallBack) throws RemoteException {
      }

      public void startFullWakeLock() throws RemoteException {
      }

      public void eventDialogTips() throws RemoteException {
      }

      public void setOptionAction(String action) throws RemoteException {
      }

      public String getOptionAction() throws RemoteException {
         return null;
      }

      public boolean killApp(String pkg, String processName) throws RemoteException {
         return false;
      }

      public boolean isAppOnForeground() throws RemoteException {
         return false;
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
