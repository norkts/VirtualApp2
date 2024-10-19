package com.lody.virtual.server.interfaces;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface IIntentFilterObserver extends IInterface {
   Intent filter(Intent var1) throws RemoteException;

   void setCallBack(IBinder var1) throws RemoteException;

   IBinder getCallBack() throws RemoteException;

   public abstract static class Stub extends Binder implements IIntentFilterObserver {
      private static final String DESCRIPTOR = "com.lody.virtual.server.interfaces.IIntentFilterObserver";
      static final int TRANSACTION_filter = 1;
      static final int TRANSACTION_setCallBack = 2;
      static final int TRANSACTION_getCallBack = 3;

      public Stub() {
         this.attachInterface(this, "com.lody.virtual.server.interfaces.IIntentFilterObserver");
      }

      public static IIntentFilterObserver asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IIntentFilterObserver)(iin != null && iin instanceof IIntentFilterObserver ? (IIntentFilterObserver)iin : new Proxy(obj));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
         String descriptor = DESCRIPTOR;
         IBinder _result;
         switch (code) {
            case 1:
               data.enforceInterface(descriptor);
               Intent _arg0;
               if (0 != data.readInt()) {
                  _arg0 = (Intent)Intent.CREATOR.createFromParcel(data);
               } else {
                  _arg0 = null;
               }

               Intent intent_result = this.filter(_arg0);
               reply.writeNoException();
               if (intent_result != null) {
                  reply.writeInt(1);
                  intent_result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 2:
               data.enforceInterface(descriptor);
               _result = data.readStrongBinder();
               this.setCallBack(_result);
               reply.writeNoException();
               return true;
            case 3:
               data.enforceInterface(descriptor);
               _result = this.getCallBack();
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

      public static boolean setDefaultImpl(IIntentFilterObserver impl) {
         if (IIntentFilterObserver.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException("setDefaultImpl() called twice");
         } else if (impl != null) {
            IIntentFilterObserver.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IIntentFilterObserver getDefaultImpl() {
         return IIntentFilterObserver.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IIntentFilterObserver {
         private IBinder mRemote;
         public static IIntentFilterObserver sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return "com.lody.virtual.server.interfaces.IIntentFilterObserver";
         }

         public Intent filter(Intent intent) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            Intent var6;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IIntentFilterObserver");
               if (intent != null) {
                  _data.writeInt(1);
                  intent.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (_status || IIntentFilterObserver.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  Intent _result;
                  if (0 != _reply.readInt()) {
                     _result = (Intent)Intent.CREATOR.createFromParcel(_reply);
                  } else {
                     _result = null;
                  }

                  return _result;
               }

               var6 = IIntentFilterObserver.Stub.getDefaultImpl().filter(intent);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var6;
         }

         public void setCallBack(IBinder callBack) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IIntentFilterObserver");
               _data.writeStrongBinder(callBack);
               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (!_status && IIntentFilterObserver.Stub.getDefaultImpl() != null) {
                  IIntentFilterObserver.Stub.getDefaultImpl().setCallBack(callBack);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public IBinder getCallBack() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            IBinder var5;
            try {
               _data.writeInterfaceToken("com.lody.virtual.server.interfaces.IIntentFilterObserver");
               boolean _status = this.mRemote.transact(3, _data, _reply, 0);
               if (_status || IIntentFilterObserver.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  IBinder _result = _reply.readStrongBinder();
                  return _result;
               }

               var5 = IIntentFilterObserver.Stub.getDefaultImpl().getCallBack();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var5;
         }
      }
   }

   public static class Default implements IIntentFilterObserver {
      public Intent filter(Intent intent) throws RemoteException {
         return null;
      }

      public void setCallBack(IBinder callBack) throws RemoteException {
      }

      public IBinder getCallBack() throws RemoteException {
         return null;
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
