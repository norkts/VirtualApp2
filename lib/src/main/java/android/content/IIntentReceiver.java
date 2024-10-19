package android.content;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface IIntentReceiver extends IInterface {
   void performReceive(Intent var1, int var2, String var3, Bundle var4, boolean var5, boolean var6, int var7) throws RemoteException;

   public abstract static class Stub extends Binder implements IIntentReceiver {
      private static final String DESCRIPTOR = "android.content.IIntentReceiver";
      static final int TRANSACTION_performReceive = 1;

      public Stub() {
         this.attachInterface(this, "android.content.IIntentReceiver");
      }

      public static IIntentReceiver asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IIntentReceiver)(iin != null && iin instanceof IIntentReceiver ? (IIntentReceiver)iin : new Proxy(obj));
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
               Intent _arg0;
               if (0 != data.readInt()) {
                  _arg0 = (Intent)Intent.CREATOR.createFromParcel(data);
               } else {
                  _arg0 = null;
               }

               int _arg1 = data.readInt();
               String _arg2 = data.readString();
               Bundle _arg3;
               if (0 != data.readInt()) {
                  _arg3 = (Bundle)Bundle.CREATOR.createFromParcel(data);
               } else {
                  _arg3 = null;
               }

               boolean _arg4 = 0 != data.readInt();
               boolean _arg5 = 0 != data.readInt();
               int _arg6 = data.readInt();
               this.performReceive(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6);
               reply.writeNoException();
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(IIntentReceiver impl) {
         if (IIntentReceiver.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException("setDefaultImpl() called twice");
         } else if (impl != null) {
            IIntentReceiver.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IIntentReceiver getDefaultImpl() {
         return IIntentReceiver.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IIntentReceiver {
         private IBinder mRemote;
         public static IIntentReceiver sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return "android.content.IIntentReceiver";
         }

         public void performReceive(Intent intent, int resultCode, String data, Bundle extras, boolean ordered, boolean sticky, int sendingUser) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("android.content.IIntentReceiver");
               if (intent != null) {
                  _data.writeInt(1);
                  intent.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeInt(resultCode);
               _data.writeString(data);
               if (extras != null) {
                  _data.writeInt(1);
                  extras.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               _data.writeInt(ordered ? 1 : 0);
               _data.writeInt(sticky ? 1 : 0);
               _data.writeInt(sendingUser);
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (_status || IIntentReceiver.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IIntentReceiver.Stub.getDefaultImpl().performReceive(intent, resultCode, data, extras, ordered, sticky, sendingUser);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }
      }
   }

   public static class Default implements IIntentReceiver {
      public void performReceive(Intent intent, int resultCode, String data, Bundle extras, boolean ordered, boolean sticky, int sendingUser) throws RemoteException {
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
