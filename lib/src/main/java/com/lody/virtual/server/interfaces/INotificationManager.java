package com.lody.virtual.server.interfaces;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface INotificationManager extends IInterface {
   int dealNotificationId(int var1, String var2, String var3, int var4) throws RemoteException;

   String dealNotificationTag(int var1, String var2, String var3, int var4) throws RemoteException;

   boolean areNotificationsEnabledForPackage(String var1, int var2) throws RemoteException;

   void setNotificationsEnabledForPackage(String var1, boolean var2, int var3) throws RemoteException;

   void addNotification(int var1, String var2, String var3, int var4) throws RemoteException;

   void cancelAllNotification(String var1, int var2) throws RemoteException;

   public abstract static class Stub extends Binder implements INotificationManager {
      private static final String DESCRIPTOR = "com.lody.virtual.server.interfaces.INotificationManager";
      static final int TRANSACTION_dealNotificationId = 1;
      static final int TRANSACTION_dealNotificationTag = 2;
      static final int TRANSACTION_areNotificationsEnabledForPackage = 3;
      static final int TRANSACTION_setNotificationsEnabledForPackage = 4;
      static final int TRANSACTION_addNotification = 5;
      static final int TRANSACTION_cancelAllNotification = 6;

      public Stub() {
         this.attachInterface(this, "com.lody.virtual.server.interfaces.INotificationManager");
      }

      public static INotificationManager asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (INotificationManager)(iin != null && iin instanceof INotificationManager ? (INotificationManager)iin : new Proxy(obj));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
         String descriptor = DESCRIPTOR;
         String _arg0;
         int _arg1;
         String _arg2;
         int _arg3;
         int i_arg0;
         String s_arg1;
         switch (code) {
            case 1:
               data.enforceInterface(descriptor);
               i_arg0 = data.readInt();
               s_arg1 = data.readString();
               _arg2 = data.readString();
               _arg3 = data.readInt();
               int _result = this.dealNotificationId(i_arg0, s_arg1, _arg2, _arg3);
               reply.writeNoException();
               reply.writeInt(_result);
               return true;
            case 2:
               data.enforceInterface(descriptor);
               i_arg0 = data.readInt();
               s_arg1 = data.readString();
               _arg2 = data.readString();
               _arg3 = data.readInt();
               String dealNotificationTag_result = this.dealNotificationTag(i_arg0, s_arg1, _arg2, _arg3);
               reply.writeNoException();
               reply.writeString(dealNotificationTag_result);
               return true;
            case 3:
               data.enforceInterface(descriptor);
               _arg0 = data.readString();
               _arg1 = data.readInt();
               boolean b_result = this.areNotificationsEnabledForPackage(_arg0, _arg1);
               reply.writeNoException();
               reply.writeInt(b_result ? 1 : 0);
               return true;
            case 4:
               data.enforceInterface(descriptor);
               _arg0 = data.readString();
               boolean b_arg1 = 0 != data.readInt();
               int i_arg2 = data.readInt();
               this.setNotificationsEnabledForPackage(_arg0, b_arg1, i_arg2);
               reply.writeNoException();
               return true;
            case 5:
               data.enforceInterface(descriptor);
               i_arg0 = data.readInt();
               s_arg1 = data.readString();
               _arg2 = data.readString();
               _arg3 = data.readInt();
               this.addNotification(i_arg0, s_arg1, _arg2, _arg3);
               reply.writeNoException();
               return true;
            case 6:
               data.enforceInterface(descriptor);
               _arg0 = data.readString();
               _arg1 = data.readInt();
               this.cancelAllNotification(_arg0, _arg1);
               reply.writeNoException();
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(INotificationManager impl) {
         if (INotificationManager.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException("setDefaultImpl() called twice");
         } else if (impl != null) {
            INotificationManager.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static INotificationManager getDefaultImpl() {
         return INotificationManager.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements INotificationManager {
         private IBinder mRemote;
         public static INotificationManager sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return "com.lody.virtual.server.interfaces.INotificationManager";
         }

         public int dealNotificationId(int id, String packageName, String tag, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            int var9;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.INotificationManager");
               _data.writeInt(id);
               _data.writeString(packageName);
               _data.writeString(tag);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (_status || INotificationManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  int _result = _reply.readInt();
                  return _result;
               }

               var9 = INotificationManager.Stub.getDefaultImpl().dealNotificationId(id, packageName, tag, userId);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var9;
         }

         public String dealNotificationTag(int id, String packageName, String tag, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            String var9;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.INotificationManager");
               _data.writeInt(id);
               _data.writeString(packageName);
               _data.writeString(tag);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (_status || INotificationManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  String _result = _reply.readString();
                  return _result;
               }

               var9 = INotificationManager.Stub.getDefaultImpl().dealNotificationTag(id, packageName, tag, userId);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var9;
         }

         public boolean areNotificationsEnabledForPackage(String packageName, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean _result;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.INotificationManager");
               _data.writeString(packageName);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(3, _data, _reply, 0);
               if (!_status && INotificationManager.Stub.getDefaultImpl() != null) {
                  boolean var7 = INotificationManager.Stub.getDefaultImpl().areNotificationsEnabledForPackage(packageName, userId);
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

         public void setNotificationsEnabledForPackage(String packageName, boolean enable, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.INotificationManager");
               _data.writeString(packageName);
               _data.writeInt(enable ? 1 : 0);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(4, _data, _reply, 0);
               if (!_status && INotificationManager.Stub.getDefaultImpl() != null) {
                  INotificationManager.Stub.getDefaultImpl().setNotificationsEnabledForPackage(packageName, enable, userId);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void addNotification(int id, String tag, String packageName, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.INotificationManager");
               _data.writeInt(id);
               _data.writeString(tag);
               _data.writeString(packageName);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(5, _data, _reply, 0);
               if (!_status && INotificationManager.Stub.getDefaultImpl() != null) {
                  INotificationManager.Stub.getDefaultImpl().addNotification(id, tag, packageName, userId);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void cancelAllNotification(String packageName, int userId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.INotificationManager");
               _data.writeString(packageName);
               _data.writeInt(userId);
               boolean _status = this.mRemote.transact(6, _data, _reply, 0);
               if (_status || INotificationManager.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               INotificationManager.Stub.getDefaultImpl().cancelAllNotification(packageName, userId);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }
      }
   }

   public static class Default implements INotificationManager {
      public int dealNotificationId(int id, String packageName, String tag, int userId) throws RemoteException {
         return 0;
      }

      public String dealNotificationTag(int id, String packageName, String tag, int userId) throws RemoteException {
         return null;
      }

      public boolean areNotificationsEnabledForPackage(String packageName, int userId) throws RemoteException {
         return false;
      }

      public void setNotificationsEnabledForPackage(String packageName, boolean enable, int userId) throws RemoteException {
      }

      public void addNotification(int id, String tag, String packageName, int userId) throws RemoteException {
      }

      public void cancelAllNotification(String packageName, int userId) throws RemoteException {
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
