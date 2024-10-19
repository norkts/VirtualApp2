package android.app.job;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;

public interface IJobService extends IInterface {
   void startJob(JobParameters var1) throws RemoteException;

   void stopJob(JobParameters var1) throws RemoteException;

   public abstract static class Stub extends Binder implements IJobService {
      private static final String DESCRIPTOR = "android.app.job.IJobService";
      static final int TRANSACTION_startJob = 1;
      static final int TRANSACTION_stopJob = 2;

      public Stub() {
         this.attachInterface(this, "android.app.job.IJobService");
      }

      public static IJobService asInterface(IBinder obj) {
         if (obj == null) {
            return null;
         } else {
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            return (IJobService)(iin != null && iin instanceof IJobService ? (IJobService)iin : new Proxy(obj));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
         String descriptor = DESCRIPTOR;
         JobParameters _arg0;
         switch (code) {
            case 1:
               data.enforceInterface(descriptor);
               if (0 != data.readInt()) {
                  _arg0 = (JobParameters)JobParameters.CREATOR.createFromParcel(data);
               } else {
                  _arg0 = null;
               }

               this.startJob(_arg0);
               reply.writeNoException();
               return true;
            case 2:
               data.enforceInterface(descriptor);
               if (0 != data.readInt()) {
                  _arg0 = (JobParameters)JobParameters.CREATOR.createFromParcel(data);
               } else {
                  _arg0 = null;
               }

               this.stopJob(_arg0);
               reply.writeNoException();
               return true;
            case 1598968902:
               reply.writeString(descriptor);
               return true;
            default:
               return super.onTransact(code, data, reply, flags);
         }
      }

      public static boolean setDefaultImpl(IJobService impl) {
         if (IJobService.Stub.Proxy.sDefaultImpl != null) {
            throw new IllegalStateException("setDefaultImpl() called twice");
         } else if (impl != null) {
            IJobService.Stub.Proxy.sDefaultImpl = impl;
            return true;
         } else {
            return false;
         }
      }

      public static IJobService getDefaultImpl() {
         return IJobService.Stub.Proxy.sDefaultImpl;
      }

      private static class Proxy implements IJobService {
         private IBinder mRemote;
         public static IJobService sDefaultImpl;

         Proxy(IBinder remote) {
            this.mRemote = remote;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return "android.app.job.IJobService";
         }

         public void startJob(JobParameters jobParams) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("android.app.job.IJobService");
               if (jobParams != null) {
                  _data.writeInt(1);
                  jobParams.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (!_status && IJobService.Stub.getDefaultImpl() != null) {
                  IJobService.Stub.getDefaultImpl().startJob(jobParams);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void stopJob(JobParameters jobParams) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken("android.app.job.IJobService");
               if (jobParams != null) {
                  _data.writeInt(1);
                  jobParams.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (_status || IJobService.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IJobService.Stub.getDefaultImpl().stopJob(jobParams);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }
      }
   }

   public static class Default implements IJobService {
      public void startJob(JobParameters jobParams) throws RemoteException {
      }

      public void stopJob(JobParameters jobParams) throws RemoteException {
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
