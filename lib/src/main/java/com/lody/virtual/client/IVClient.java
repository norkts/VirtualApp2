package com.lody.virtual.client;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import java.util.ArrayList;
import java.util.List;

public interface IVClient extends IInterface {
   void scheduleNewIntent(String var1, IBinder var2, Intent var3) throws RemoteException;

   void finishActivity(IBinder var1) throws RemoteException;

   IBinder createProxyService(ComponentName var1, IBinder var2) throws RemoteException;

   IBinder acquireProviderClient(ProviderInfo var1) throws RemoteException;

   IBinder getAppThread() throws RemoteException;

   IBinder getToken() throws RemoteException;

   boolean isAppRunning() throws RemoteException;

   String getDebugInfo() throws RemoteException;

   boolean finishReceiver(IBinder var1) throws RemoteException;

   List<ActivityManager.RunningServiceInfo> getServices() throws RemoteException;

   public abstract static class Stub extends Binder implements IVClient {
      private static final String DESCRIPTOR = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVhoqLAgYJ2AzESlnDzgCKT42J2UwMFo="));
      static final int TRANSACTION_scheduleNewIntent = 1;
      static final int TRANSACTION_finishActivity = 2;
      static final int TRANSACTION_createProxyService = 3;
      static final int TRANSACTION_acquireProviderClient = 4;
      static final int TRANSACTION_getAppThread = 5;
      static final int TRANSACTION_getToken = 6;
      static final int TRANSACTION_isAppRunning = 7;
      static final int TRANSACTION_getDebugInfo = 8;
      static final int TRANSACTION_finishReceiver = 9;
      static final int TRANSACTION_getServices = 10;

      public Stub() {
         this.attachInterface(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVhoqLAgYJ2AzESlnDzgCKT42J2UwMFo=")));
      }

      public static IVClient asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IVClient)(iin != null && iin instanceof IVClient ? (IVClient)iin : new Proxy(obj));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
         String descriptor = DESCRIPTOR;
         IBinder _arg0;
         String _arg0;
         IBinder _result;
         switch (code) {
            case 1:
               data.enforceInterface(descriptor);
               _arg0 = data.readString();
               _result = data.readStrongBinder();
               Intent _arg2;
               if (0 != data.readInt()) {
                  _arg2 = (Intent)Intent.CREATOR.createFromParcel(data);
               } else {
                  _arg2 = null;
               }

               this.scheduleNewIntent(_arg0, _result, _arg2);
               reply.writeNoException();
               return true;
            case 2:
               data.enforceInterface(descriptor);
               _arg0 = data.readStrongBinder();
               this.finishActivity(_arg0);
               reply.writeNoException();
               return true;
            case 3:
               data.enforceInterface(descriptor);
               ComponentName _arg0;
               if (0 != data.readInt()) {
                  _arg0 = (ComponentName)ComponentName.CREATOR.createFromParcel(data);
               } else {
                  _arg0 = null;
               }

               _result = data.readStrongBinder();
               IBinder _result = this.createProxyService(_arg0, _result);
               reply.writeNoException();
               reply.writeStrongBinder(_result);
               return true;
            case 4:
               data.enforceInterface(descriptor);
               ProviderInfo _arg0;
               if (0 != data.readInt()) {
                  _arg0 = (ProviderInfo)ProviderInfo.CREATOR.createFromParcel(data);
               } else {
                  _arg0 = null;
               }

               _result = this.acquireProviderClient(_arg0);
               reply.writeNoException();
               reply.writeStrongBinder(_result);
               return true;
            case 5:
               data.enforceInterface(descriptor);
               _arg0 = this.getAppThread();
               reply.writeNoException();
               reply.writeStrongBinder(_arg0);
               return true;
            case 6:
               data.enforceInterface(descriptor);
               _arg0 = this.getToken();
               reply.writeNoException();
               reply.writeStrongBinder(_arg0);
               return true;
            case 7:
               data.enforceInterface(descriptor);
               boolean _result = this.isAppRunning();
               reply.writeNoException();
               reply.writeInt(_result ? 1 : 0);
               return true;
            case 8:
               data.enforceInterface(descriptor);
               _arg0 = this.getDebugInfo();
               reply.writeNoException();
               reply.writeString(_arg0);
               return true;
            case 9:
               data.enforceInterface(descriptor);
               _arg0 = data.readStrongBinder();
               boolean _result = this.finishReceiver(_arg0);
               reply.writeNoException();
               reply.writeInt(_result ? 1 : 0);
               return true;
            case 10:
               data.enforceInterface(descriptor);
               List<ActivityManager.RunningServiceInfo> _result = this.getServices();
               reply.writeNoException();
               reply.writeTypedList(_result);
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(IVClient impl) {
         if (IVClient.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGAFNC59ATAoLBUcD2ozOyB6DTwqLRgEKGIKESNqETwiKAgAVg==")));
         } else if (impl != null) {
            IVClient.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IVClient getDefaultImpl() {
         return IVClient.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IVClient {
         private IBinder mRemote;
         public static IVClient sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVhoqLAgYJ2AzESlnDzgCKT42J2UwMFo="));
         }

         public void scheduleNewIntent(String creator, IBinder token, Intent intent) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVhoqLAgYJ2AzESlnDzgCKT42J2UwMFo=")));
               _data.writeString(creator);
               _data.writeStrongBinder(token);
               if (intent != null) {
                  _data.writeInt(1);
                  intent.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (!_status && IVClient.Stub.getDefaultImpl() != null) {
                  IVClient.Stub.getDefaultImpl().scheduleNewIntent(creator, token, intent);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void finishActivity(IBinder token) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVhoqLAgYJ2AzESlnDzgCKT42J2UwMFo=")));
               _data.writeStrongBinder(token);
               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (_status || IVClient.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IVClient.Stub.getDefaultImpl().finishActivity(token);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public IBinder createProxyService(ComponentName component, IBinder binder) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            IBinder _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVhoqLAgYJ2AzESlnDzgCKT42J2UwMFo=")));
               if (component != null) {
                  _data.writeInt(1);
                  component.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeStrongBinder(binder);
               boolean _status = this.mRemote.transact(3, _data, _reply, 0);
               if (!_status && IVClient.Stub.getDefaultImpl() != null) {
                  IBinder var7 = IVClient.Stub.getDefaultImpl().createProxyService(component, binder);
                  return var7;
               }

               _reply.readException();
               _result = _reply.readStrongBinder();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public IBinder acquireProviderClient(ProviderInfo info) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            IBinder _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVhoqLAgYJ2AzESlnDzgCKT42J2UwMFo=")));
               if (info != null) {
                  _data.writeInt(1);
                  info.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(4, _data, _reply, 0);
               if (!_status && IVClient.Stub.getDefaultImpl() != null) {
                  IBinder var6 = IVClient.Stub.getDefaultImpl().acquireProviderClient(info);
                  return var6;
               }

               _reply.readException();
               _result = _reply.readStrongBinder();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public IBinder getAppThread() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            IBinder var5;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVhoqLAgYJ2AzESlnDzgCKT42J2UwMFo=")));
               boolean _status = this.mRemote.transact(5, _data, _reply, 0);
               if (_status || IVClient.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  IBinder _result = _reply.readStrongBinder();
                  return _result;
               }

               var5 = IVClient.Stub.getDefaultImpl().getAppThread();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var5;
         }

         public IBinder getToken() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            IBinder var5;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVhoqLAgYJ2AzESlnDzgCKT42J2UwMFo=")));
               boolean _status = this.mRemote.transact(6, _data, _reply, 0);
               if (_status || IVClient.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  IBinder _result = _reply.readStrongBinder();
                  return _result;
               }

               var5 = IVClient.Stub.getDefaultImpl().getToken();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var5;
         }

         public boolean isAppRunning() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVhoqLAgYJ2AzESlnDzgCKT42J2UwMFo=")));
               boolean _status = this.mRemote.transact(7, _data, _reply, 0);
               if (!_status && IVClient.Stub.getDefaultImpl() != null) {
                  boolean var5 = IVClient.Stub.getDefaultImpl().isAppRunning();
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

         public String getDebugInfo() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            String var5;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVhoqLAgYJ2AzESlnDzgCKT42J2UwMFo=")));
               boolean _status = this.mRemote.transact(8, _data, _reply, 0);
               if (_status || IVClient.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  String _result = _reply.readString();
                  return _result;
               }

               var5 = IVClient.Stub.getDefaultImpl().getDebugInfo();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var5;
         }

         public boolean finishReceiver(IBinder token) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVhoqLAgYJ2AzESlnDzgCKT42J2UwMFo=")));
               _data.writeStrongBinder(token);
               boolean _status = this.mRemote.transact(9, _data, _reply, 0);
               if (!_status && IVClient.Stub.getDefaultImpl() != null) {
                  boolean var6 = IVClient.Stub.getDefaultImpl().finishReceiver(token);
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

         public List<ActivityManager.RunningServiceInfo> getServices() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            ArrayList _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVhoqLAgYJ2AzESlnDzgCKT42J2UwMFo=")));
               boolean _status = this.mRemote.transact(10, _data, _reply, 0);
               if (!_status && IVClient.Stub.getDefaultImpl() != null) {
                  List var5 = IVClient.Stub.getDefaultImpl().getServices();
                  return var5;
               }

               _reply.readException();
               _result = _reply.createTypedArrayList(RunningServiceInfo.CREATOR);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }
      }
   }

   public static class Default implements IVClient {
      public void scheduleNewIntent(String creator, IBinder token, Intent intent) throws RemoteException {
      }

      public void finishActivity(IBinder token) throws RemoteException {
      }

      public IBinder createProxyService(ComponentName component, IBinder binder) throws RemoteException {
         return null;
      }

      public IBinder acquireProviderClient(ProviderInfo info) throws RemoteException {
         return null;
      }

      public IBinder getAppThread() throws RemoteException {
         return null;
      }

      public IBinder getToken() throws RemoteException {
         return null;
      }

      public boolean isAppRunning() throws RemoteException {
         return false;
      }

      public String getDebugInfo() throws RemoteException {
         return null;
      }

      public boolean finishReceiver(IBinder token) throws RemoteException {
         return false;
      }

      public List<ActivityManager.RunningServiceInfo> getServices() throws RemoteException {
         return null;
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
