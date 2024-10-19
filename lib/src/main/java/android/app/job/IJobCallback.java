package android.app.job;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface IJobCallback extends IInterface {
   void acknowledgeStartMessage(int var1, boolean var2) throws RemoteException;

   void acknowledgeStopMessage(int var1, boolean var2) throws RemoteException;

   JobWorkItem dequeueWork(int var1) throws RemoteException;

   boolean completeWork(int var1, int var2) throws RemoteException;

   void jobFinished(int var1, boolean var2) throws RemoteException;

   public abstract static class Stub extends Binder implements IJobCallback {
      private static final String DESCRIPTOR = "android.app.job.IJobCallback";
      static final int TRANSACTION_acknowledgeStartMessage = 1;
      static final int TRANSACTION_acknowledgeStopMessage = 2;
      static final int TRANSACTION_dequeueWork = 3;
      static final int TRANSACTION_completeWork = 4;
      static final int TRANSACTION_jobFinished = 5;

      public Stub() {
         this.attachInterface(this, "android.app.job.IJobCallback");
      }

      public static IJobCallback asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IJobCallback)(iin != null && iin instanceof IJobCallback ? (IJobCallback)iin : new Proxy(obj));
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
               boolean _arg1 = 0 != data.readInt();
               this.acknowledgeStartMessage(_arg0, _arg1);
               reply.writeNoException();
               return true;
            case 2:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               _arg1 = 0 != data.readInt();
               this.acknowledgeStopMessage(_arg0, _arg1);
               reply.writeNoException();
               return true;
            case 3:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               JobWorkItem _result = this.dequeueWork(_arg0);
               reply.writeNoException();
               if (_result != null) {
                  reply.writeInt(1);
                  _result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 4:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               int _arg2 = data.readInt();
               boolean _result2 = this.completeWork(_arg0, _arg2);
               reply.writeNoException();
               reply.writeInt(_result2 ? 1 : 0);
               return true;
            case 5:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               _arg1 = 0 != data.readInt();
               this.jobFinished(_arg0, _arg1);
               reply.writeNoException();
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(IJobCallback impl) {
         if (IJobCallback.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException("setDefaultImpl() called twice");
         } else if (impl != null) {
            IJobCallback.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IJobCallback getDefaultImpl() {
         return IJobCallback.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IJobCallback {
         private IBinder mRemote;
         public static IJobCallback sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return "android.app.job.IJobCallback";
         }

         public void acknowledgeStartMessage(int jobId, boolean ongoing) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("android.app.job.IJobCallback");
               _data.writeInt(jobId);
               _data.writeInt(ongoing ? 1 : 0);
               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (!_status && IJobCallback.Stub.getDefaultImpl() != null) {
                  IJobCallback.Stub.getDefaultImpl().acknowledgeStartMessage(jobId, ongoing);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void acknowledgeStopMessage(int jobId, boolean reschedule) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("android.app.job.IJobCallback");
               _data.writeInt(jobId);
               _data.writeInt(reschedule ? 1 : 0);
               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (_status || IJobCallback.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IJobCallback.Stub.getDefaultImpl().acknowledgeStopMessage(jobId, reschedule);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public JobWorkItem dequeueWork(int jobId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            JobWorkItem var6;
            try {
               _data.writeInterfaceToken("android.app.job.IJobCallback");
               _data.writeInt(jobId);
               boolean _status = this.mRemote.transact(3, _data, _reply, 0);
               if (_status || IJobCallback.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  JobWorkItem _result;
                  if (0 != _reply.readInt()) {
                     _result = (JobWorkItem)JobWorkItem.CREATOR.createFromParcel(_reply);
                  } else {
                     _result = null;
                  }

                  return _result;
               }

               var6 = IJobCallback.Stub.getDefaultImpl().dequeueWork(jobId);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var6;
         }

         public boolean completeWork(int jobId, int workId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            boolean var7;
            try {
               _data.writeInterfaceToken("android.app.job.IJobCallback");
               _data.writeInt(jobId);
               _data.writeInt(workId);
               boolean _status = this.mRemote.transact(4, _data, _reply, 0);
               if (_status || IJobCallback.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  boolean _result = 0 != _reply.readInt();
                  return _result;
               }

               var7 = IJobCallback.Stub.getDefaultImpl().completeWork(jobId, workId);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var7;
         }

         public void jobFinished(int jobId, boolean reschedule) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("android.app.job.IJobCallback");
               _data.writeInt(jobId);
               _data.writeInt(reschedule ? 1 : 0);
               boolean _status = this.mRemote.transact(5, _data, _reply, 0);
               if (!_status && IJobCallback.Stub.getDefaultImpl() != null) {
                  IJobCallback.Stub.getDefaultImpl().jobFinished(jobId, reschedule);
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

   public static class Default implements IJobCallback {
      public void acknowledgeStartMessage(int jobId, boolean ongoing) throws RemoteException {
      }

      public void acknowledgeStopMessage(int jobId, boolean reschedule) throws RemoteException {
      }

      public JobWorkItem dequeueWork(int jobId) throws RemoteException {
         return null;
      }

      public boolean completeWork(int jobId, int workId) throws RemoteException {
         return false;
      }

      public void jobFinished(int jobId, boolean reschedule) throws RemoteException {
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
