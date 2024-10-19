package com.lody.virtual.client.ipc;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobWorkItem;
import android.os.RemoteException;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.env.VirtualRuntime;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.IInterfaceUtils;
import com.lody.virtual.remote.VJobWorkItem;
import com.lody.virtual.server.interfaces.IJobService;
import java.util.List;

public class VJobScheduler {
   private static final VJobScheduler sInstance = new VJobScheduler();
   private IJobService mService;

   public static VJobScheduler get() {
      return sInstance;
   }

   public IJobService getService() {
      if (this.mService == null || !IInterfaceUtils.isAlive(this.mService)) {
         synchronized(this) {
            Object binder = this.getRemoteInterface();
            this.mService = (IJobService)LocalProxyUtils.genProxy(IJobService.class, binder);
         }
      }

      return this.mService;
   }

   private Object getRemoteInterface() {
      return IJobService.Stub.asInterface(ServiceManagerNative.getService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LD4AOg=="))));
   }

   public int schedule(JobInfo job) {
      try {
         return this.getService().schedule(VClient.get().getVUid(), job);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return (Integer)VirtualRuntime.crash(e);
      }
   }

   public List<JobInfo> getAllPendingJobs() {
      try {
         return this.getService().getAllPendingJobs(VClient.get().getVUid());
      } catch (RemoteException var2) {
         RemoteException e = var2;
         return (List)VirtualRuntime.crash(e);
      }
   }

   public void cancelAll() {
      try {
         this.getService().cancelAll(VClient.get().getVUid());
      } catch (RemoteException var2) {
         RemoteException e = var2;
         e.printStackTrace();
      }

   }

   public void cancel(int jobId) {
      try {
         this.getService().cancel(VClient.get().getVUid(), jobId);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         e.printStackTrace();
      }

   }

   public JobInfo getPendingJob(int jobId) {
      try {
         return this.getService().getPendingJob(VClient.get().getVUid(), jobId);
      } catch (RemoteException var3) {
         RemoteException e = var3;
         return (JobInfo)VirtualRuntime.crash(e);
      }
   }

   @TargetApi(26)
   public int enqueue(JobInfo job, JobWorkItem workItem) {
      if (workItem == null) {
         return -1;
      } else if (BuildCompat.isOreo()) {
         try {
            return this.getService().enqueue(VClient.get().getVUid(), job, new VJobWorkItem(workItem));
         } catch (RemoteException var4) {
            RemoteException e = var4;
            return (Integer)VirtualRuntime.crash(e);
         }
      } else {
         return -1;
      }
   }
}
