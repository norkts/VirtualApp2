package android.content.pm;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface IPackageInstallerCallback extends IInterface {
   void onSessionCreated(int var1) throws RemoteException;

   void onSessionBadgingChanged(int var1) throws RemoteException;

   void onSessionActiveChanged(int var1, boolean var2) throws RemoteException;

   void onSessionProgressChanged(int var1, float var2) throws RemoteException;

   void onSessionFinished(int var1, boolean var2) throws RemoteException;

   public abstract static class Stub extends Binder implements IPackageInstallerCallback {
      private static final String DESCRIPTOR = "android.content.pm.IPackageInstallerCallback";
      static final int TRANSACTION_onSessionCreated = 1;
      static final int TRANSACTION_onSessionBadgingChanged = 2;
      static final int TRANSACTION_onSessionActiveChanged = 3;
      static final int TRANSACTION_onSessionProgressChanged = 4;
      static final int TRANSACTION_onSessionFinished = 5;

      public Stub() {
         this.attachInterface(this, "android.content.pm.IPackageInstallerCallback");
      }

      public static IPackageInstallerCallback asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IPackageInstallerCallback)(iin != null && iin instanceof IPackageInstallerCallback ? (IPackageInstallerCallback)iin : new Proxy(obj));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
         String descriptor = DESCRIPTOR;
         int _arg0;
         boolean _arg1;
         switch (code) {
            case 1:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               this.onSessionCreated(_arg0);
               reply.writeNoException();
               return true;
            case 2:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               this.onSessionBadgingChanged(_arg0);
               reply.writeNoException();
               return true;
            case 3:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               _arg1 = 0 != data.readInt();
               this.onSessionActiveChanged(_arg0, _arg1);
               reply.writeNoException();
               return true;
            case 4:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               float f_arg1 = data.readFloat();
               this.onSessionProgressChanged(_arg0, f_arg1);
               reply.writeNoException();
               return true;
            case 5:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               _arg1 = 0 != data.readInt();
               this.onSessionFinished(_arg0, _arg1);
               reply.writeNoException();
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(IPackageInstallerCallback impl) {
         if (IPackageInstallerCallback.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException("setDefaultImpl() called twice");
         } else if (impl != null) {
            IPackageInstallerCallback.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IPackageInstallerCallback getDefaultImpl() {
         return IPackageInstallerCallback.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IPackageInstallerCallback {
         private IBinder mRemote;
         public static IPackageInstallerCallback sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return "android.content.pm.IPackageInstallerCallback";
         }

         public void onSessionCreated(int sessionId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("android.content.pm.IPackageInstallerCallback");
               _data.writeInt(sessionId);
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (!_status && IPackageInstallerCallback.Stub.getDefaultImpl() != null) {
                  IPackageInstallerCallback.Stub.getDefaultImpl().onSessionCreated(sessionId);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void onSessionBadgingChanged(int sessionId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("android.content.pm.IPackageInstallerCallback");
               _data.writeInt(sessionId);
               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (!_status && IPackageInstallerCallback.Stub.getDefaultImpl() != null) {
                  IPackageInstallerCallback.Stub.getDefaultImpl().onSessionBadgingChanged(sessionId);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void onSessionActiveChanged(int sessionId, boolean active) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("android.content.pm.IPackageInstallerCallback");
               _data.writeInt(sessionId);
               _data.writeInt(active ? 1 : 0);
               boolean _status = this.mRemote.transact(3, _data, _reply, 0);
               if (!_status && IPackageInstallerCallback.Stub.getDefaultImpl() != null) {
                  IPackageInstallerCallback.Stub.getDefaultImpl().onSessionActiveChanged(sessionId, active);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void onSessionProgressChanged(int sessionId, float progress) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("android.content.pm.IPackageInstallerCallback");
               _data.writeInt(sessionId);
               _data.writeFloat(progress);
               boolean _status = this.mRemote.transact(4, _data, _reply, 0);
               if (!_status && IPackageInstallerCallback.Stub.getDefaultImpl() != null) {
                  IPackageInstallerCallback.Stub.getDefaultImpl().onSessionProgressChanged(sessionId, progress);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void onSessionFinished(int sessionId, boolean success) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("android.content.pm.IPackageInstallerCallback");
               _data.writeInt(sessionId);
               _data.writeInt(success ? 1 : 0);
               boolean _status = this.mRemote.transact(5, _data, _reply, 0);
               if (_status || IPackageInstallerCallback.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IPackageInstallerCallback.Stub.getDefaultImpl().onSessionFinished(sessionId, success);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }
      }
   }

   public static class Default implements IPackageInstallerCallback {
      public void onSessionCreated(int sessionId) throws RemoteException {
      }

      public void onSessionBadgingChanged(int sessionId) throws RemoteException {
      }

      public void onSessionActiveChanged(int sessionId, boolean active) throws RemoteException {
      }

      public void onSessionProgressChanged(int sessionId, float progress) throws RemoteException {
      }

      public void onSessionFinished(int sessionId, boolean success) throws RemoteException {
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
