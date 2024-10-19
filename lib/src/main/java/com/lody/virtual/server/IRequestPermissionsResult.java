package com.lody.virtual.server;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface IRequestPermissionsResult extends IInterface {
   boolean onResult(int var1, String[] var2, int[] var3) throws RemoteException;

   public abstract static class Stub extends Binder implements IRequestPermissionsResult {
      private static final String DESCRIPTOR = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylnDyguLC0AJ2wgMA5oHgo8JQguOm8KRQJqDFk0Ki0uDmwFSFo="));
      static final int TRANSACTION_onResult = 1;

      public Stub() {
         this.attachInterface(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylnDyguLC0AJ2wgMA5oHgo8JQguOm8KRQJqDFk0Ki0uDmwFSFo=")));
      }

      public static IRequestPermissionsResult asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IRequestPermissionsResult)(iin != null && iin instanceof IRequestPermissionsResult ? (IRequestPermissionsResult)iin : new Proxy(obj));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
         String descriptor = DESCRIPTOR;
         switch (code) {
            case 1:
               data.enforceInterface(descriptor);
               int _arg0 = data.readInt();
               String[] _arg1 = data.createStringArray();
               int[] _arg2 = data.createIntArray();
               boolean _result = this.onResult(_arg0, _arg1, _arg2);
               reply.writeNoException();
               reply.writeInt(_result ? 1 : 0);
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(IRequestPermissionsResult impl) {
         if (IRequestPermissionsResult.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGAFNC59ATAoLBUcD2ozOyB6DTwqLRgEKGIKESNqETwiKAgAVg==")));
         } else if (impl != null) {
            IRequestPermissionsResult.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IRequestPermissionsResult getDefaultImpl() {
         return IRequestPermissionsResult.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IRequestPermissionsResult {
         private IBinder mRemote;
         public static IRequestPermissionsResult sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylnDyguLC0AJ2wgMA5oHgo8JQguOm8KRQJqDFk0Ki0uDmwFSFo="));
         }

         public boolean onResult(int requestCode, String[] permissions, int[] grantResults) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean var8;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylnDyguLC0AJ2wgMA5oHgo8JQguOm8KRQJqDFk0Ki0uDmwFSFo=")));
               _data.writeInt(requestCode);
               _data.writeStringArray(permissions);
               _data.writeIntArray(grantResults);
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (_status || IRequestPermissionsResult.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  boolean _result = 0 != _reply.readInt();
                  return _result;
               }

               var8 = IRequestPermissionsResult.Stub.getDefaultImpl().onResult(requestCode, permissions, grantResults);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var8;
         }
      }
   }

   public static class Default implements IRequestPermissionsResult {
      public boolean onResult(int requestCode, String[] permissions, int[] grantResults) throws RemoteException {
         return false;
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
