package com.lody.virtual;

import android.app.ActivityManager;
import android.app.ActivityManager.RecentTaskInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface IExtHelperInterface extends IInterface {
   int syncPackages() throws RemoteException;

   void cleanPackageData(int[] var1, String var2) throws RemoteException;

   void forceStop(int[] var1) throws RemoteException;

   List<ActivityManager.RunningTaskInfo> getRunningTasks(int var1) throws RemoteException;

   List<ActivityManager.RecentTaskInfo> getRecentTasks(int var1, int var2) throws RemoteException;

   List<ActivityManager.RunningAppProcessInfo> getRunningAppProcesses() throws RemoteException;

   boolean isExternalStorageManager() throws RemoteException;

   void startActivity(Intent var1, Bundle var2) throws RemoteException;

   public abstract static class Stub extends Binder implements IExtHelperInterface {
      private static final String DESCRIPTOR = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVhpPJhdfCmsaLDdlHgo7Ii4qCmsKFjJrETA0"));
      static final int TRANSACTION_syncPackages = 1;
      static final int TRANSACTION_cleanPackageData = 2;
      static final int TRANSACTION_forceStop = 3;
      static final int TRANSACTION_getRunningTasks = 4;
      static final int TRANSACTION_getRecentTasks = 5;
      static final int TRANSACTION_getRunningAppProcesses = 6;
      static final int TRANSACTION_isExternalStorageManager = 7;
      static final int TRANSACTION_startActivity = 8;

      public Stub() {
         this.attachInterface(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVhpPJhdfCmsaLDdlHgo7Ii4qCmsKFjJrETA0")));
      }

      public static IExtHelperInterface asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IExtHelperInterface)(iin != null && iin instanceof IExtHelperInterface ? (IExtHelperInterface)iin : new Proxy(obj));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
         String descriptor = DESCRIPTOR;
         int _arg0;
         int[] i_arg0;
         switch (code) {
            case 1:
               data.enforceInterface(descriptor);
               _arg0 = this.syncPackages();
               reply.writeNoException();
               reply.writeInt(_arg0);
               return true;
            case 2:
               data.enforceInterface(descriptor);
               i_arg0 = data.createIntArray();
               String _arg1 = data.readString();
               this.cleanPackageData(i_arg0, _arg1);
               reply.writeNoException();
               return true;
            case 3:
               data.enforceInterface(descriptor);
               i_arg0 = data.createIntArray();
               this.forceStop(i_arg0);
               reply.writeNoException();
               return true;
            case 4:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               List<ActivityManager.RunningTaskInfo> _result = this.getRunningTasks(_arg0);
               reply.writeNoException();
               reply.writeTypedList(_result);
               return true;
            case 5:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               int i_arg1 = data.readInt();
               List<ActivityManager.RecentTaskInfo> ars_result = this.getRecentTasks(_arg0, i_arg1);
               reply.writeNoException();
               reply.writeTypedList(ars_result);
               return true;
            case 6:
               data.enforceInterface(descriptor);
               List<ActivityManager.RunningAppProcessInfo> runningAppProcesses_result = this.getRunningAppProcesses();
               reply.writeNoException();
               reply.writeTypedList(runningAppProcesses_result);
               return true;
            case 7:
               data.enforceInterface(descriptor);
               boolean b_result = this.isExternalStorageManager();
               reply.writeNoException();
               reply.writeInt(b_result ? 1 : 0);
               return true;
            case 8:
               data.enforceInterface(descriptor);
               Intent intent_arg0;
               if (0 != data.readInt()) {
                  intent_arg0 = (Intent)Intent.CREATOR.createFromParcel(data);
               } else {
                  intent_arg0 = null;
               }

               Bundle bundle_arg1;
               if (0 != data.readInt()) {
                  bundle_arg1 = (Bundle)Bundle.CREATOR.createFromParcel(data);
               } else {
                  bundle_arg1 = null;
               }

               this.startActivity(intent_arg0, bundle_arg1);
               reply.writeNoException();
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(IExtHelperInterface impl) {
         if (IExtHelperInterface.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGAFNC59ATAoLBUcD2ozOyB6DTwqLRgEKGIKESNqETwiKAgAVg==")));
         } else if (impl != null) {
            IExtHelperInterface.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IExtHelperInterface getDefaultImpl() {
         return IExtHelperInterface.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IExtHelperInterface {
         private IBinder mRemote;
         public static IExtHelperInterface sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVhpPJhdfCmsaLDdlHgo7Ii4qCmsKFjJrETA0"));
         }

         public int syncPackages() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            int var5;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVhpPJhdfCmsaLDdlHgo7Ii4qCmsKFjJrETA0")));
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (_status || IExtHelperInterface.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  int _result = _reply.readInt();
                  return _result;
               }

               var5 = IExtHelperInterface.Stub.getDefaultImpl().syncPackages();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var5;
         }

         public void cleanPackageData(int[] userIds, String packageName) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVhpPJhdfCmsaLDdlHgo7Ii4qCmsKFjJrETA0")));
               _data.writeIntArray(userIds);
               _data.writeString(packageName);
               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (_status || IExtHelperInterface.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IExtHelperInterface.Stub.getDefaultImpl().cleanPackageData(userIds, packageName);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void forceStop(int[] pids) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVhpPJhdfCmsaLDdlHgo7Ii4qCmsKFjJrETA0")));
               _data.writeIntArray(pids);
               boolean _status = this.mRemote.transact(3, _data, _reply, 0);
               if (_status || IExtHelperInterface.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IExtHelperInterface.Stub.getDefaultImpl().forceStop(pids);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public List<ActivityManager.RunningTaskInfo> getRunningTasks(int maxNum) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            ArrayList _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVhpPJhdfCmsaLDdlHgo7Ii4qCmsKFjJrETA0")));
               _data.writeInt(maxNum);
               boolean _status = this.mRemote.transact(4, _data, _reply, 0);
               if (!_status && IExtHelperInterface.Stub.getDefaultImpl() != null) {
                  List var6 = IExtHelperInterface.Stub.getDefaultImpl().getRunningTasks(maxNum);
                  return var6;
               }

               _reply.readException();
               _result = _reply.createTypedArrayList(RunningTaskInfo.CREATOR);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public List<ActivityManager.RecentTaskInfo> getRecentTasks(int maxNum, int flags) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            ArrayList _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVhpPJhdfCmsaLDdlHgo7Ii4qCmsKFjJrETA0")));
               _data.writeInt(maxNum);
               _data.writeInt(flags);
               boolean _status = this.mRemote.transact(5, _data, _reply, 0);
               if (!_status && IExtHelperInterface.Stub.getDefaultImpl() != null) {
                  List var7 = IExtHelperInterface.Stub.getDefaultImpl().getRecentTasks(maxNum, flags);
                  return var7;
               }

               _reply.readException();
               _result = _reply.createTypedArrayList(RecentTaskInfo.CREATOR);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public List<ActivityManager.RunningAppProcessInfo> getRunningAppProcesses() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            List var5;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVhpPJhdfCmsaLDdlHgo7Ii4qCmsKFjJrETA0")));
               boolean _status = this.mRemote.transact(6, _data, _reply, 0);
               if (_status || IExtHelperInterface.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  List<ActivityManager.RunningAppProcessInfo> _result = _reply.createTypedArrayList(RunningAppProcessInfo.CREATOR);
                  return _result;
               }

               var5 = IExtHelperInterface.Stub.getDefaultImpl().getRunningAppProcesses();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var5;
         }

         public boolean isExternalStorageManager() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean var5;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVhpPJhdfCmsaLDdlHgo7Ii4qCmsKFjJrETA0")));
               boolean _status = this.mRemote.transact(7, _data, _reply, 0);
               if (_status || IExtHelperInterface.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  boolean _result = 0 != _reply.readInt();
                  return _result;
               }

               var5 = IExtHelperInterface.Stub.getDefaultImpl().isExternalStorageManager();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var5;
         }

         public void startActivity(Intent intent, Bundle options) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVhpPJhdfCmsaLDdlHgo7Ii4qCmsKFjJrETA0")));
               if (intent != null) {
                  _data.writeInt(1);
                  intent.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               if (options != null) {
                  _data.writeInt(1);
                  options.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(8, _data, _reply, 0);
               if (!_status && IExtHelperInterface.Stub.getDefaultImpl() != null) {
                  IExtHelperInterface.Stub.getDefaultImpl().startActivity(intent, options);
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

   public static class Default implements IExtHelperInterface {
      public int syncPackages() throws RemoteException {
         return 0;
      }

      public void cleanPackageData(int[] userIds, String packageName) throws RemoteException {
      }

      public void forceStop(int[] pids) throws RemoteException {
      }

      public List<ActivityManager.RunningTaskInfo> getRunningTasks(int maxNum) throws RemoteException {
         return null;
      }

      public List<ActivityManager.RecentTaskInfo> getRecentTasks(int maxNum, int flags) throws RemoteException {
         return null;
      }

      public List<ActivityManager.RunningAppProcessInfo> getRunningAppProcesses() throws RemoteException {
         return null;
      }

      public boolean isExternalStorageManager() throws RemoteException {
         return false;
      }

      public void startActivity(Intent intent, Bundle options) throws RemoteException {
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
