package com.lody.virtual.server.fs;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import com.lody.virtual.remote.FileInfo;

public interface IFileTransfer extends IInterface {
   FileInfo[] listFiles(String var1) throws RemoteException;

   ParcelFileDescriptor openFile(String var1) throws RemoteException;

   public abstract static class Stub extends Binder implements IFileTransfer {
      private static final String DESCRIPTOR = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMyluNy8dIiwEI2UVNApvJzg5Jy06LGUzSFo="));
      static final int TRANSACTION_listFiles = 1;
      static final int TRANSACTION_openFile = 2;

      public Stub() {
         this.attachInterface(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMyluNy8dIiwEI2UVNApvJzg5Jy06LGUzSFo=")));
      }

      public static IFileTransfer asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IFileTransfer)(iin != null && iin instanceof IFileTransfer ? (IFileTransfer)iin : new Proxy(obj));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
         String descriptor = DESCRIPTOR;
         String _arg0;
         switch (code) {
            case 1:
               data.enforceInterface(descriptor);
               _arg0 = data.readString();
               FileInfo[] _result = this.listFiles(_arg0);
               reply.writeNoException();
               reply.writeTypedArray(_result, 1);
               return true;
            case 2:
               data.enforceInterface(descriptor);
               _arg0 = data.readString();
               ParcelFileDescriptor parcelFileDescriptor_result = this.openFile(_arg0);
               reply.writeNoException();
               if (parcelFileDescriptor_result != null) {
                  reply.writeInt(1);
                  parcelFileDescriptor_result.writeToParcel(reply, 1);
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

      public static boolean setDefaultImpl(IFileTransfer impl) {
         if (IFileTransfer.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGAFNC59ATAoLBUcD2ozOyB6DTwqLRgEKGIKESNqETwiKAgAVg==")));
         } else if (impl != null) {
            IFileTransfer.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IFileTransfer getDefaultImpl() {
         return IFileTransfer.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IFileTransfer {
         private IBinder mRemote;
         public static IFileTransfer sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMyluNy8dIiwEI2UVNApvJzg5Jy06LGUzSFo="));
         }

         public FileInfo[] listFiles(String path) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            FileInfo[] _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMyluNy8dIiwEI2UVNApvJzg5Jy06LGUzSFo=")));
               _data.writeString(path);
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (!_status && IFileTransfer.Stub.getDefaultImpl() != null) {
                  FileInfo[] var6 = IFileTransfer.Stub.getDefaultImpl().listFiles(path);
                  return var6;
               }

               _reply.readException();
               _result = (FileInfo[])_reply.createTypedArray(FileInfo.CREATOR);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public ParcelFileDescriptor openFile(String path) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            ParcelFileDescriptor var6;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMyluNy8dIiwEI2UVNApvJzg5Jy06LGUzSFo=")));
               _data.writeString(path);
               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (_status || IFileTransfer.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  ParcelFileDescriptor _result;
                  if (0 != _reply.readInt()) {
                     _result = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(_reply);
                  } else {
                     _result = null;
                  }

                  return _result;
               }

               var6 = IFileTransfer.Stub.getDefaultImpl().openFile(path);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var6;
         }
      }
   }

   public static class Default implements IFileTransfer {
      public FileInfo[] listFiles(String path) throws RemoteException {
         return null;
      }

      public ParcelFileDescriptor openFile(String path) throws RemoteException {
         return null;
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
