package com.lody.virtual.server;

import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface IBinderProxyService extends IInterface {
   ComponentName getComponent() throws RemoteException;

   IBinder getService() throws RemoteException;

   public abstract static class Stub extends Binder implements IBinderProxyService {
      private static final String DESCRIPTOR = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylnDCgiKRgcJ2w2ICZqNFkgLy02OWowBjNpJ1RF"));
      static final int TRANSACTION_getComponent = 1;
      static final int TRANSACTION_getService = 2;

      public Stub() {
         this.attachInterface(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylnDCgiKRgcJ2w2ICZqNFkgLy02OWowBjNpJ1RF")));
      }

      public static IBinderProxyService asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IBinderProxyService)(iin != null && iin instanceof IBinderProxyService ? (IBinderProxyService)iin : new Proxy(obj));
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
               ComponentName _result = this.getComponent();
               reply.writeNoException();
               if (_result != null) {
                  reply.writeInt(1);
                  _result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 2:
               data.enforceInterface(descriptor);
               IBinder _result = this.getService();
               reply.writeNoException();
               reply.writeStrongBinder(_result);
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(IBinderProxyService impl) {
         if (IBinderProxyService.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGAFNC59ATAoLBUcD2ozOyB6DTwqLRgEKGIKESNqETwiKAgAVg==")));
         } else if (impl != null) {
            IBinderProxyService.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IBinderProxyService getDefaultImpl() {
         return IBinderProxyService.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IBinderProxyService {
         private IBinder mRemote;
         public static IBinderProxyService sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylnDCgiKRgcJ2w2ICZqNFkgLy02OWowBjNpJ1RF"));
         }

         public ComponentName getComponent() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            ComponentName _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylnDCgiKRgcJ2w2ICZqNFkgLy02OWowBjNpJ1RF")));
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (!_status && IBinderProxyService.Stub.getDefaultImpl() != null) {
                  ComponentName var5 = IBinderProxyService.Stub.getDefaultImpl().getComponent();
                  return var5;
               }

               _reply.readException();
               if (0 != _reply.readInt()) {
                  _result = (ComponentName)ComponentName.CREATOR.createFromParcel(_reply);
               } else {
                  _result = null;
               }
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public IBinder getService() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            IBinder _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylnDCgiKRgcJ2w2ICZqNFkgLy02OWowBjNpJ1RF")));
               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (!_status && IBinderProxyService.Stub.getDefaultImpl() != null) {
                  IBinder var5 = IBinderProxyService.Stub.getDefaultImpl().getService();
                  return var5;
               }

               _reply.readException();
               _result = _reply.readStrongBinder();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }
      }
   }

   public static class Default implements IBinderProxyService {
      public ComponentName getComponent() throws RemoteException {
         return null;
      }

      public IBinder getService() throws RemoteException {
         return null;
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
