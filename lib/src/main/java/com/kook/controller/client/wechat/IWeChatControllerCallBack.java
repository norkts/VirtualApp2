package com.kook.controller.client.wechat;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.carlos.libcommon.StringFog;
import java.util.Map;

public interface IWeChatControllerCallBack extends IInterface {
   void syncScanGroupChatData(Map var1) throws RemoteException;

   int getSendChatPosition() throws RemoteException;

   String getSendChatName(int var1) throws RemoteException;

   String getSendChatMessage() throws RemoteException;

   void setSendChatMsgSuccess(int var1, boolean var2) throws RemoteException;

   public abstract static class Stub extends Binder implements IWeChatControllerCallBack {
      private static final String DESCRIPTOR = "com.kook.controller.client.wechat.IWeChatControllerCallBack";
      static final int TRANSACTION_syncScanGroupChatData = 1;
      static final int TRANSACTION_getSendChatPosition = 2;
      static final int TRANSACTION_getSendChatName = 3;
      static final int TRANSACTION_getSendChatMessage = 4;
      static final int TRANSACTION_setSendChatMsgSuccess = 5;

      public Stub() {
         this.attachInterface(this, "com.kook.controller.client.wechat.IWeChatControllerCallBack");
      }

      public static IWeChatControllerCallBack asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IWeChatControllerCallBack)(iin != null && iin instanceof IWeChatControllerCallBack ? (IWeChatControllerCallBack)iin : new Proxy(obj));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
         String descriptor = DESCRIPTOR;
         int _arg0;
         switch (code) {
            case 1:
               data.enforceInterface(descriptor);
               ClassLoader cl = this.getClass().getClassLoader();
               Map map_arg0 = data.readHashMap(cl);
               this.syncScanGroupChatData(map_arg0);
               reply.writeNoException();
               return true;
            case 2:
               data.enforceInterface(descriptor);
               _arg0 = this.getSendChatPosition();
               reply.writeNoException();
               reply.writeInt(_arg0);
               return true;
            case 3:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               String _result = this.getSendChatName(_arg0);
               reply.writeNoException();
               reply.writeString(_result);
               return true;
            case 4:
               data.enforceInterface(descriptor);
               String s_result = this.getSendChatMessage();
               reply.writeNoException();
               reply.writeString(s_result);
               return true;
            case 5:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               boolean _arg1 = 0 != data.readInt();
               this.setSendChatMsgSuccess(_arg0, _arg1);
               reply.writeNoException();
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(IWeChatControllerCallBack impl) {
         if (IWeChatControllerCallBack.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException("setDefaultImpl() called twice");
         } else if (impl != null) {
            IWeChatControllerCallBack.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IWeChatControllerCallBack getDefaultImpl() {
         return IWeChatControllerCallBack.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IWeChatControllerCallBack {
         private IBinder mRemote;
         public static IWeChatControllerCallBack sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return "com.kook.controller.client.wechat.IWeChatControllerCallBack";
         }

         public void syncScanGroupChatData(Map groupChat) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("com.kook.controller.client.wechat.IWeChatControllerCallBack");
               _data.writeMap(groupChat);
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (!_status && IWeChatControllerCallBack.Stub.getDefaultImpl() != null) {
                  IWeChatControllerCallBack.Stub.getDefaultImpl().syncScanGroupChatData(groupChat);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public int getSendChatPosition() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            int _result;
            try {
               _data.writeInterfaceToken("com.kook.controller.client.wechat.IWeChatControllerCallBack");
               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (!_status && IWeChatControllerCallBack.Stub.getDefaultImpl() != null) {
                  int var5 = IWeChatControllerCallBack.Stub.getDefaultImpl().getSendChatPosition();
                  return var5;
               }

               _reply.readException();
               _result = _reply.readInt();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public String getSendChatName(int position) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            String var6;
            try {
               _data.writeInterfaceToken("com.kook.controller.client.wechat.IWeChatControllerCallBack");
               _data.writeInt(position);
               boolean _status = this.mRemote.transact(3, _data, _reply, 0);
               if (_status || IWeChatControllerCallBack.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  String _result = _reply.readString();
                  return _result;
               }

               var6 = IWeChatControllerCallBack.Stub.getDefaultImpl().getSendChatName(position);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var6;
         }

         public String getSendChatMessage() throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            String var5;
            try {
               _data.writeInterfaceToken("com.kook.controller.client.wechat.IWeChatControllerCallBack");
               boolean _status = this.mRemote.transact(4, _data, _reply, 0);
               if (_status || IWeChatControllerCallBack.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  String _result = _reply.readString();
                  return _result;
               }

               var5 = IWeChatControllerCallBack.Stub.getDefaultImpl().getSendChatMessage();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var5;
         }

         public void setSendChatMsgSuccess(int position, boolean success) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("com.kook.controller.client.wechat.IWeChatControllerCallBack");
               _data.writeInt(position);
               _data.writeInt(success ? 1 : 0);
               boolean _status = this.mRemote.transact(5, _data, _reply, 0);
               if (_status || IWeChatControllerCallBack.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IWeChatControllerCallBack.Stub.getDefaultImpl().setSendChatMsgSuccess(position, success);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }
      }
   }

   public static class Default implements IWeChatControllerCallBack {
      public void syncScanGroupChatData(Map groupChat) throws RemoteException {
      }

      public int getSendChatPosition() throws RemoteException {
         return 0;
      }

      public String getSendChatName(int position) throws RemoteException {
         return null;
      }

      public String getSendChatMessage() throws RemoteException {
         return null;
      }

      public void setSendChatMsgSuccess(int position, boolean success) throws RemoteException {
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
