package android.app;

import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface IServiceConnection extends IInterface {
   void connected(ComponentName var1, IBinder var2) throws RemoteException;

   public abstract static class Stub extends Binder implements IServiceConnection {
      private static final String DESCRIPTOR = "android.app.IServiceConnection";
      static final int TRANSACTION_connected = 1;

      public Stub() {
         this.attachInterface(this, "android.app.IServiceConnection");
      }

      public static IServiceConnection asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IServiceConnection)(iin != null && iin instanceof IServiceConnection ? (IServiceConnection)iin : new Proxy(obj));
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
               ComponentName _arg0;
               if (0 != data.readInt()) {
                  _arg0 = (ComponentName)ComponentName.CREATOR.createFromParcel(data);
               } else {
                  _arg0 = null;
               }

               IBinder _arg1 = data.readStrongBinder();
               this.connected(_arg0, _arg1);
               reply.writeNoException();
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(IServiceConnection impl) {
         if (IServiceConnection.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException("setDefaultImpl() called twice");
         } else if (impl != null) {
            IServiceConnection.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IServiceConnection getDefaultImpl() {
         return IServiceConnection.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IServiceConnection {
         private IBinder mRemote;
         public static IServiceConnection sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return "android.app.IServiceConnection";
         }

         public void connected(ComponentName name, IBinder service) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("android.app.IServiceConnection");
               if (name != null) {
                  _data.writeInt(1);
                  name.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeStrongBinder(service);
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (_status || IServiceConnection.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IServiceConnection.Stub.getDefaultImpl().connected(name, service);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }
      }
   }

   public static class Default implements IServiceConnection {
      public void connected(ComponentName name, IBinder service) throws RemoteException {
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
