package com.lody.virtual.server.app;

import android.content.IntentSender;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface IAppInstallerSession extends IInterface {
   void addPackage(Uri var1) throws RemoteException;

   void addSplit(Uri var1) throws RemoteException;

   void commit(IntentSender var1) throws RemoteException;

   void cancel() throws RemoteException;

   public abstract static class Stub extends Binder implements IAppInstallerSession {
      private static final String DESCRIPTOR = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylpASA5ORY2HWwaIFBqJDAZOwdXO24FNA1pJB4cLAgACA=="));
      static final int TRANSACTION_addPackage = 1;
      static final int TRANSACTION_addSplit = 2;
      static final int TRANSACTION_commit = 3;
      static final int TRANSACTION_cancel = 4;

      public Stub() {
         this.attachInterface(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylpASA5ORY2HWwaIFBqJDAZOwdXO24FNA1pJB4cLAgACA==")));
      }

      public static IAppInstallerSession asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IAppInstallerSession)(iin != null && iin instanceof IAppInstallerSession ? (IAppInstallerSession)iin : new Proxy(obj));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
         String descriptor = DESCRIPTOR;
         Uri _arg0;
         switch (code) {
            case 1:
               data.enforceInterface(descriptor);
               if (0 != data.readInt()) {
                  _arg0 = (Uri)Uri.CREATOR.createFromParcel(data);
               } else {
                  _arg0 = null;
               }

               this.addPackage(_arg0);
               reply.writeNoException();
               return true;
            case 2:
               data.enforceInterface(descriptor);
               if (0 != data.readInt()) {
                  _arg0 = (Uri)Uri.CREATOR.createFromParcel(data);
               } else {
                  _arg0 = null;
               }

               this.addSplit(_arg0);
               reply.writeNoException();
               return true;
            case 3:
               data.enforceInterface(descriptor);
               IntentSender intentSender_arg0;
               if (0 != data.readInt()) {
                  intentSender_arg0 = (IntentSender)IntentSender.CREATOR.createFromParcel(data);
               } else {
                  intentSender_arg0 = null;
               }

               this.commit(intentSender_arg0);
               reply.writeNoException();
               return true;
            case 4:
               data.enforceInterface(descriptor);
               this.cancel();
               reply.writeNoException();
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(IAppInstallerSession impl) {
         if (IAppInstallerSession.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGAFNC59ATAoLBUcD2ozOyB6DTwqLRgEKGIKESNqETwiKAgAVg==")));
         } else if (impl != null) {
            IAppInstallerSession.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IAppInstallerSession getDefaultImpl() {
         return IAppInstallerSession.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IAppInstallerSession {
         private IBinder mRemote;
         public static IAppInstallerSession sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylpASA5ORY2HWwaIFBqJDAZOwdXO24FNA1pJB4cLAgACA=="));
         }

         public void addPackage(Uri uri) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylpASA5ORY2HWwaIFBqJDAZOwdXO24FNA1pJB4cLAgACA==")));
               if (uri != null) {
                  _data.writeInt(1);
                  uri.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (!_status && IAppInstallerSession.Stub.getDefaultImpl() != null) {
                  IAppInstallerSession.Stub.getDefaultImpl().addPackage(uri);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void addSplit(Uri uri) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylpASA5ORY2HWwaIFBqJDAZOwdXO24FNA1pJB4cLAgACA==")));
               if (uri != null) {
                  _data.writeInt(1);
                  uri.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (_status || IAppInstallerSession.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IAppInstallerSession.Stub.getDefaultImpl().addSplit(uri);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void commit(IntentSender statusReceiver) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylpASA5ORY2HWwaIFBqJDAZOwdXO24FNA1pJB4cLAgACA==")));
               if (statusReceiver != null) {
                  _data.writeInt(1);
                  statusReceiver.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(3, _data, _reply, 0);
               if (_status || IAppInstallerSession.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IAppInstallerSession.Stub.getDefaultImpl().commit(statusReceiver);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void cancel() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylpASA5ORY2HWwaIFBqJDAZOwdXO24FNA1pJB4cLAgACA==")));
               boolean _status = this.mRemote.transact(4, _data, _reply, 0);
               if (_status || IAppInstallerSession.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IAppInstallerSession.Stub.getDefaultImpl().cancel();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }
      }
   }

   public static class Default implements IAppInstallerSession {
      public void addPackage(Uri uri) throws RemoteException {
      }

      public void addSplit(Uri uri) throws RemoteException {
      }

      public void commit(IntentSender statusReceiver) throws RemoteException {
      }

      public void cancel() throws RemoteException {
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
