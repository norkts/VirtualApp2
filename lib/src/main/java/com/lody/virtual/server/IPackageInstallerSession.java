package com.lody.virtual.server;

import android.content.IntentSender;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface IPackageInstallerSession extends IInterface {
   void setClientProgress(float var1) throws RemoteException;

   void addClientProgress(float var1) throws RemoteException;

   String[] getNames() throws RemoteException;

   ParcelFileDescriptor openWrite(String var1, long var2, long var4) throws RemoteException;

   ParcelFileDescriptor openRead(String var1) throws RemoteException;

   void close() throws RemoteException;

   void commit(IntentSender var1) throws RemoteException;

   void abandon() throws RemoteException;

   public abstract static class Stub extends Binder implements IPackageInstallerSession {
      private static final String DESCRIPTOR = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylnDyAqKAg+O2sjNFBqJDAZOwdXO24FNA1pJB4cLAgACA=="));
      static final int TRANSACTION_setClientProgress = 1;
      static final int TRANSACTION_addClientProgress = 2;
      static final int TRANSACTION_getNames = 3;
      static final int TRANSACTION_openWrite = 4;
      static final int TRANSACTION_openRead = 5;
      static final int TRANSACTION_close = 6;
      static final int TRANSACTION_commit = 7;
      static final int TRANSACTION_abandon = 8;

      public Stub() {
         this.attachInterface(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylnDyAqKAg+O2sjNFBqJDAZOwdXO24FNA1pJB4cLAgACA==")));
      }

      public static IPackageInstallerSession asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IPackageInstallerSession)(iin != null && iin instanceof IPackageInstallerSession ? (IPackageInstallerSession)iin : new Proxy(obj));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
         String descriptor = DESCRIPTOR;
         String _arg0;
         float _arg0;
         switch (code) {
            case 1:
               data.enforceInterface(descriptor);
               _arg0 = data.readFloat();
               this.setClientProgress(_arg0);
               reply.writeNoException();
               return true;
            case 2:
               data.enforceInterface(descriptor);
               _arg0 = data.readFloat();
               this.addClientProgress(_arg0);
               reply.writeNoException();
               return true;
            case 3:
               data.enforceInterface(descriptor);
               String[] _result = this.getNames();
               reply.writeNoException();
               reply.writeStringArray(_result);
               return true;
            case 4:
               data.enforceInterface(descriptor);
               _arg0 = data.readString();
               long _arg1 = data.readLong();
               long _arg2 = data.readLong();
               ParcelFileDescriptor _result = this.openWrite(_arg0, _arg1, _arg2);
               reply.writeNoException();
               if (_result != null) {
                  reply.writeInt(1);
                  _result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 5:
               data.enforceInterface(descriptor);
               _arg0 = data.readString();
               ParcelFileDescriptor _result = this.openRead(_arg0);
               reply.writeNoException();
               if (_result != null) {
                  reply.writeInt(1);
                  _result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 6:
               data.enforceInterface(descriptor);
               this.close();
               reply.writeNoException();
               return true;
            case 7:
               data.enforceInterface(descriptor);
               IntentSender _arg0;
               if (0 != data.readInt()) {
                  _arg0 = (IntentSender)IntentSender.CREATOR.createFromParcel(data);
               } else {
                  _arg0 = null;
               }

               this.commit(_arg0);
               reply.writeNoException();
               return true;
            case 8:
               data.enforceInterface(descriptor);
               this.abandon();
               reply.writeNoException();
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(IPackageInstallerSession impl) {
         if (IPackageInstallerSession.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGAFNC59ATAoLBUcD2ozOyB6DTwqLRgEKGIKESNqETwiKAgAVg==")));
         } else if (impl != null) {
            IPackageInstallerSession.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IPackageInstallerSession getDefaultImpl() {
         return IPackageInstallerSession.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IPackageInstallerSession {
         private IBinder mRemote;
         public static IPackageInstallerSession sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylnDyAqKAg+O2sjNFBqJDAZOwdXO24FNA1pJB4cLAgACA=="));
         }

         public void setClientProgress(float progress) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylnDyAqKAg+O2sjNFBqJDAZOwdXO24FNA1pJB4cLAgACA==")));
               _data.writeFloat(progress);
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (!_status && IPackageInstallerSession.Stub.getDefaultImpl() != null) {
                  IPackageInstallerSession.Stub.getDefaultImpl().setClientProgress(progress);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void addClientProgress(float progress) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylnDyAqKAg+O2sjNFBqJDAZOwdXO24FNA1pJB4cLAgACA==")));
               _data.writeFloat(progress);
               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (_status || IPackageInstallerSession.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IPackageInstallerSession.Stub.getDefaultImpl().addClientProgress(progress);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public String[] getNames() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            String[] var5;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylnDyAqKAg+O2sjNFBqJDAZOwdXO24FNA1pJB4cLAgACA==")));
               boolean _status = this.mRemote.transact(3, _data, _reply, 0);
               if (_status || IPackageInstallerSession.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  String[] _result = _reply.createStringArray();
                  return _result;
               }

               var5 = IPackageInstallerSession.Stub.getDefaultImpl().getNames();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var5;
         }

         public ParcelFileDescriptor openWrite(String name, long offsetBytes, long lengthBytes) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            ParcelFileDescriptor _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylnDyAqKAg+O2sjNFBqJDAZOwdXO24FNA1pJB4cLAgACA==")));
               _data.writeString(name);
               _data.writeLong(offsetBytes);
               _data.writeLong(lengthBytes);
               boolean _status = this.mRemote.transact(4, _data, _reply, 0);
               if (!_status && IPackageInstallerSession.Stub.getDefaultImpl() != null) {
                  ParcelFileDescriptor var10 = IPackageInstallerSession.Stub.getDefaultImpl().openWrite(name, offsetBytes, lengthBytes);
                  return var10;
               }

               _reply.readException();
               if (0 != _reply.readInt()) {
                  _result = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(_reply);
               } else {
                  _result = null;
               }
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public ParcelFileDescriptor openRead(String name) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            ParcelFileDescriptor var6;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylnDyAqKAg+O2sjNFBqJDAZOwdXO24FNA1pJB4cLAgACA==")));
               _data.writeString(name);
               boolean _status = this.mRemote.transact(5, _data, _reply, 0);
               if (_status || IPackageInstallerSession.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  ParcelFileDescriptor _result;
                  if (0 != _reply.readInt()) {
                     _result = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(_reply);
                  } else {
                     _result = null;
                  }

                  return _result;
               }

               var6 = IPackageInstallerSession.Stub.getDefaultImpl().openRead(name);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var6;
         }

         public void close() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylnDyAqKAg+O2sjNFBqJDAZOwdXO24FNA1pJB4cLAgACA==")));
               boolean _status = this.mRemote.transact(6, _data, _reply, 0);
               if (!_status && IPackageInstallerSession.Stub.getDefaultImpl() != null) {
                  IPackageInstallerSession.Stub.getDefaultImpl().close();
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void commit(IntentSender statusReceiver) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylnDyAqKAg+O2sjNFBqJDAZOwdXO24FNA1pJB4cLAgACA==")));
               if (statusReceiver != null) {
                  _data.writeInt(1);
                  statusReceiver.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(7, _data, _reply, 0);
               if (!_status && IPackageInstallerSession.Stub.getDefaultImpl() != null) {
                  IPackageInstallerSession.Stub.getDefaultImpl().commit(statusReceiver);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void abandon() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylnDyAqKAg+O2sjNFBqJDAZOwdXO24FNA1pJB4cLAgACA==")));
               boolean _status = this.mRemote.transact(8, _data, _reply, 0);
               if (!_status && IPackageInstallerSession.Stub.getDefaultImpl() != null) {
                  IPackageInstallerSession.Stub.getDefaultImpl().abandon();
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

   public static class Default implements IPackageInstallerSession {
      public void setClientProgress(float progress) throws RemoteException {
      }

      public void addClientProgress(float progress) throws RemoteException {
      }

      public String[] getNames() throws RemoteException {
         return null;
      }

      public ParcelFileDescriptor openWrite(String name, long offsetBytes, long lengthBytes) throws RemoteException {
         return null;
      }

      public ParcelFileDescriptor openRead(String name) throws RemoteException {
         return null;
      }

      public void close() throws RemoteException {
      }

      public void commit(IntentSender statusReceiver) throws RemoteException {
      }

      public void abandon() throws RemoteException {
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
