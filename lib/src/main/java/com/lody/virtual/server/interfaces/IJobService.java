package com.lody.virtual.server.interfaces;

import android.app.job.JobInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import com.lody.virtual.remote.VJobWorkItem;
import java.util.ArrayList;
import java.util.List;

public interface IJobService extends IInterface {
   int schedule(int var1, JobInfo var2) throws RemoteException;

   void cancel(int var1, int var2) throws RemoteException;

   void cancelAll(int var1) throws RemoteException;

   List<JobInfo> getAllPendingJobs(int var1) throws RemoteException;

   JobInfo getPendingJob(int var1, int var2) throws RemoteException;

   int enqueue(int var1, JobInfo var2, VJobWorkItem var3) throws RemoteException;

   public abstract static class Stub extends Binder implements IJobService {
      private static final String DESCRIPTOR = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWLT0YKX0gFiRlETA2LQhSVg=="));
      static final int TRANSACTION_schedule = 1;
      static final int TRANSACTION_cancel = 2;
      static final int TRANSACTION_cancelAll = 3;
      static final int TRANSACTION_getAllPendingJobs = 4;
      static final int TRANSACTION_getPendingJob = 5;
      static final int TRANSACTION_enqueue = 6;

      public Stub() {
         this.attachInterface(this, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWLT0YKX0gFiRlETA2LQhSVg==")));
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
         int _arg0;
         JobInfo _arg1;
         int _arg1;
         switch (code) {
            case 1:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               if (0 != data.readInt()) {
                  _arg1 = (JobInfo)JobInfo.CREATOR.createFromParcel(data);
               } else {
                  _arg1 = null;
               }

               int _result = this.schedule(_arg0, _arg1);
               reply.writeNoException();
               reply.writeInt(_result);
               return true;
            case 2:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               _arg1 = data.readInt();
               this.cancel(_arg0, _arg1);
               reply.writeNoException();
               return true;
            case 3:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               this.cancelAll(_arg0);
               reply.writeNoException();
               return true;
            case 4:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               List<JobInfo> _result = this.getAllPendingJobs(_arg0);
               reply.writeNoException();
               reply.writeTypedList(_result);
               return true;
            case 5:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               _arg1 = data.readInt();
               JobInfo _result = this.getPendingJob(_arg0, _arg1);
               reply.writeNoException();
               if (_result != null) {
                  reply.writeInt(1);
                  _result.writeToParcel(reply, 1);
               } else {
                  reply.writeInt(0);
               }

               return true;
            case 6:
               data.enforceInterface(descriptor);
               _arg0 = data.readInt();
               if (0 != data.readInt()) {
                  _arg1 = (JobInfo)JobInfo.CREATOR.createFromParcel(data);
               } else {
                  _arg1 = null;
               }

               VJobWorkItem _arg2;
               if (0 != data.readInt()) {
                  _arg2 = (VJobWorkItem)VJobWorkItem.CREATOR.createFromParcel(data);
               } else {
                  _arg2 = null;
               }

               int _result = this.enqueue(_arg0, _arg1, _arg2);
               reply.writeNoException();
               reply.writeInt(_result);
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
            throw new IllegalStateException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGAFNC59ATAoLBUcD2ozOyB6DTwqLRgEKGIKESNqETwiKAgAVg==")));
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
            return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWLT0YKX0gFiRlETA2LQhSVg=="));
         }

         public int schedule(int uid, JobInfo job) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            int var7;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWLT0YKX0gFiRlETA2LQhSVg==")));
               _data.writeInt(uid);
               if (job != null) {
                  _data.writeInt(1);
                  job.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(1, _data, _reply, 0);
               if (_status || IJobService.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  int _result = _reply.readInt();
                  return _result;
               }

               var7 = IJobService.Stub.getDefaultImpl().schedule(uid, job);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var7;
         }

         public void cancel(int uid, int jobId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWLT0YKX0gFiRlETA2LQhSVg==")));
               _data.writeInt(uid);
               _data.writeInt(jobId);
               boolean _status = this.mRemote.transact(2, _data, _reply, 0);
               if (_status || IJobService.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  return;
               }

               IJobService.Stub.getDefaultImpl().cancel(uid, jobId);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public void cancelAll(int uid) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWLT0YKX0gFiRlETA2LQhSVg==")));
               _data.writeInt(uid);
               boolean _status = this.mRemote.transact(3, _data, _reply, 0);
               if (!_status && IJobService.Stub.getDefaultImpl() != null) {
                  IJobService.Stub.getDefaultImpl().cancelAll(uid);
                  return;
               }

               _reply.readException();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

         }

         public List<JobInfo> getAllPendingJobs(int uid) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            ArrayList _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWLT0YKX0gFiRlETA2LQhSVg==")));
               _data.writeInt(uid);
               boolean _status = this.mRemote.transact(4, _data, _reply, 0);
               if (!_status && IJobService.Stub.getDefaultImpl() != null) {
                  List var6 = IJobService.Stub.getDefaultImpl().getAllPendingJobs(uid);
                  return var6;
               }

               _reply.readException();
               _result = _reply.createTypedArrayList(JobInfo.CREATOR);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }

         public JobInfo getPendingJob(int uid, int jobId) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            JobInfo var7;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWLT0YKX0gFiRlETA2LQhSVg==")));
               _data.writeInt(uid);
               _data.writeInt(jobId);
               boolean _status = this.mRemote.transact(5, _data, _reply, 0);
               if (_status || IJobService.Stub.getDefaultImpl() == null) {
                  _reply.readException();
                  JobInfo _result;
                  if (0 != _reply.readInt()) {
                     _result = (JobInfo)JobInfo.CREATOR.createFromParcel(_reply);
                  } else {
                     _result = null;
                  }

                  return _result;
               }

               var7 = IJobService.Stub.getDefaultImpl().getPendingJob(uid, jobId);
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return var7;
         }

         public int enqueue(int uid, JobInfo job, VJobWorkItem workItem) throws RemoteException {
            Parcel _data = Parcel.obtain();
            Parcel _reply = Parcel.obtain();

            int _result;
            try {
               _data.writeInterfaceToken(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojHiViERk2LD0cKGUwGjdlVho6LhcMMmIFMylvDh49Ly1fImgFLD9vMxoWLT0YKX0gFiRlETA2LQhSVg==")));
               _data.writeInt(uid);
               if (job != null) {
                  _data.writeInt(1);
                  job.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               if (workItem != null) {
                  _data.writeInt(1);
                  workItem.writeToParcel(_data, 0);
               } else {
                  _data.writeInt(0);
               }

               boolean _status = this.mRemote.transact(6, _data, _reply, 0);
               if (!_status && IJobService.Stub.getDefaultImpl() != null) {
                  int var8 = IJobService.Stub.getDefaultImpl().enqueue(uid, job, workItem);
                  return var8;
               }

               _reply.readException();
               _result = _reply.readInt();
            } finally {
               _reply.recycle();
               _data.recycle();
            }

            return _result;
         }
      }
   }

   public static class Default implements IJobService {
      public int schedule(int uid, JobInfo job) throws RemoteException {
         return 0;
      }

      public void cancel(int uid, int jobId) throws RemoteException {
      }

      public void cancelAll(int uid) throws RemoteException {
      }

      public List<JobInfo> getAllPendingJobs(int uid) throws RemoteException {
         return null;
      }

      public JobInfo getPendingJob(int uid, int jobId) throws RemoteException {
         return null;
      }

      public int enqueue(int uid, JobInfo job, VJobWorkItem workItem) throws RemoteException {
         return 0;
      }

      public IBinder asBinder() {
         return null;
      }
   }
}
