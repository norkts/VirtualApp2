package com.lody.virtual.client.stub;

import android.annotation.TargetApi;
import android.app.Service;
import android.app.job.IJobCallback;
import android.app.job.IJobService;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobWorkItem;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.InvocationStubManager;
import com.lody.virtual.client.hook.proxies.am.ActivityManagerStub;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.helper.collection.SparseArray;
import com.lody.virtual.helper.compat.JobWorkItemCompat;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.server.job.VJobSchedulerService;
import java.util.Map;

@TargetApi(21)
public class ShadowJobWorkService extends Service {
   private static final boolean debug = true;
   private static final String TAG = ShadowJobWorkService.class.getSimpleName();
   private final SparseArray<JobSession> mJobSessions = new SparseArray();
   private JobScheduler mScheduler;

   public IBinder onBind(Intent intent) {
      return null;
   }

   public int onStartCommand(Intent intent, int flags, int startId) {
      if (intent != null) {
         String action = intent.getAction();
         JobParameters jobParams;
         if ("action.startJob".equals(action)) {
            jobParams = (JobParameters)intent.getParcelableExtra("jobParams");
            this.startJob(jobParams);
         } else if ("action.stopJob".equals(action)) {
            jobParams = (JobParameters)intent.getParcelableExtra("jobParams");
            this.stopJob(jobParams);
         }
      }

      return 2;
   }

   public static void startJob(Context context, JobParameters jobParams) {
      Intent intent = new Intent(context, ShadowJobWorkService.class);
      intent.setAction("action.startJob");
      intent.putExtra("jobParams", jobParams);
      context.startService(intent);
   }

   public static void stopJob(Context context, JobParameters jobParams) {
      Intent intent = new Intent(context, ShadowJobWorkService.class);
      intent.setAction("action.stopJob");
      intent.putExtra("jobParams", jobParams);
      context.startService(intent);
   }

   private void emptyCallback(IJobCallback callback, int jobId) {
      try {
         callback.acknowledgeStartMessage(jobId, false);
         callback.jobFinished(jobId, false);
      } catch (RemoteException var4) {
         RemoteException e = var4;
         e.printStackTrace();
      }

   }

   public void onCreate() {
      super.onCreate();
      InvocationStubManager.getInstance().checkEnv(ActivityManagerStub.class);
      this.mScheduler = (JobScheduler)this.getSystemService("jobscheduler");
   }

   public void onDestroy() {
      VLog.i(TAG, "ShadowJobService:onDestroy");
      synchronized(this.mJobSessions) {
         int i = this.mJobSessions.size() - 1;

         while(true) {
            if (i < 0) {
               this.mJobSessions.clear();
               break;
            }

            JobSession session = (JobSession)this.mJobSessions.valueAt(i);
            session.stopSessionLocked();
            --i;
         }
      }

      super.onDestroy();
   }

   public void startJob(JobParameters jobParams) {
      int jobId = jobParams.getJobId();
      IBinder binder = (IBinder)mirror.android.app.job.JobParameters.callback.get(jobParams);
      IJobCallback callback = IJobCallback.Stub.asInterface(binder);
      Map.Entry<VJobSchedulerService.JobId, VJobSchedulerService.JobConfig> entry = VJobSchedulerService.get().findJobByVirtualJobId(jobId);
      if (entry == null) {
         this.emptyCallback(callback, jobId);
         this.mScheduler.cancel(jobId);
      } else {
         VJobSchedulerService.JobId key = (VJobSchedulerService.JobId)entry.getKey();
         VJobSchedulerService.JobConfig config = (VJobSchedulerService.JobConfig)entry.getValue();
         JobSession session;
         synchronized(this.mJobSessions) {
            session = (JobSession)this.mJobSessions.get(jobId);
         }

         if (session != null) {
            session.startJob(true);
         } else {
            boolean bound = false;
            synchronized(this.mJobSessions) {
               mirror.android.app.job.JobParameters.jobId.set(jobParams, key.clientJobId);
               session = new JobSession(jobId, callback, jobParams, key.packageName);
               mirror.android.app.job.JobParameters.callback.set(jobParams, session.asBinder());
               this.mJobSessions.put(jobId, session);
               Intent service = new Intent();
               service.setComponent(new ComponentName(key.packageName, config.serviceName));

               try {
                  VLog.i(TAG, "ShadowJobService:binService:%s, jobId=%s", service.getComponent(), jobId);
                  bound = VActivityManager.get().bindService(this, service, session, 5, VUserHandle.getUserId(key.vuid));
               } catch (Throwable var16) {
                  Throwable e = var16;
                  VLog.e(TAG, e);
               }
            }

            if (!bound) {
               synchronized(this.mJobSessions) {
                  this.mJobSessions.remove(jobId);
               }

               this.emptyCallback(callback, jobId);
               this.mScheduler.cancel(jobId);
               VJobSchedulerService.get().cancel(-1, jobId);
            }
         }
      }

   }

   public void stopJob(JobParameters jobParams) {
      int jobId = jobParams.getJobId();
      synchronized(this.mJobSessions) {
         JobSession session = (JobSession)this.mJobSessions.get(jobId);
         if (session != null) {
            VLog.i(TAG, "stopJob:%d", jobId);
            session.stopSessionLocked();
         }

      }
   }

   private final class JobSession extends IJobCallback.Stub implements ServiceConnection {
      private int jobId;
      private IJobCallback clientCallback;
      private JobParameters jobParams;
      private IJobService clientJobService;
      private boolean isWorking;
      private String packageName;
      private JobWorkItem lastWorkItem;

      JobSession(int jobId, IJobCallback clientCallback, JobParameters jobParams, String packageName) {
         this.jobId = jobId;
         this.clientCallback = clientCallback;
         this.jobParams = jobParams;
         this.packageName = packageName;
      }

      public void acknowledgeStartMessage(int jobId, boolean ongoing) throws RemoteException {
         this.isWorking = true;
         VLog.i(ShadowJobWorkService.TAG, "ShadowJobService:acknowledgeStartMessage:%d", this.jobId);
         this.clientCallback.acknowledgeStartMessage(this.jobId, ongoing);
      }

      public void acknowledgeStopMessage(int jobId, boolean reschedule) throws RemoteException {
         this.isWorking = false;
         VLog.i(ShadowJobWorkService.TAG, "ShadowJobService:acknowledgeStopMessage:%d", this.jobId);
         this.clientCallback.acknowledgeStopMessage(this.jobId, reschedule);
      }

      public void jobFinished(int jobId, boolean reschedule) throws RemoteException {
         this.isWorking = false;
         VLog.i(ShadowJobWorkService.TAG, "ShadowJobService:jobFinished:%d", this.jobId);
         this.clientCallback.jobFinished(this.jobId, reschedule);
      }

      public boolean completeWork(int jobId, int workId) throws RemoteException {
         VLog.i(ShadowJobWorkService.TAG, "ShadowJobService:completeWork:%d", this.jobId);
         return this.clientCallback.completeWork(this.jobId, workId);
      }

      public JobWorkItem dequeueWork(int jobId) throws RemoteException {
         try {
            this.lastWorkItem = null;
            VLog.i(ShadowJobWorkService.TAG, "ShadowJobService:dequeueWork:%d", this.jobId);
            JobWorkItem workItem = this.clientCallback.dequeueWork(this.jobId);
            if (workItem != null) {
               this.lastWorkItem = JobWorkItemCompat.parse(workItem);
               return this.lastWorkItem;
            }

            this.isWorking = false;
            this.stopSessionLocked();
         } catch (Exception var3) {
            Exception e = var3;
            e.printStackTrace();
            VLog.i(ShadowJobWorkService.TAG, "ShadowJobService:dequeueWork:" + e);
         }

         return null;
      }

      public void startJob(boolean wait) {
         if (this.isWorking) {
            VLog.w(ShadowJobWorkService.TAG, "ShadowJobService:startJob:%d,but is working", this.jobId);
         } else {
            VLog.i(ShadowJobWorkService.TAG, "ShadowJobService:startJob:%d", this.jobId);
            if (this.clientJobService == null) {
               if (!wait) {
                  ShadowJobWorkService.this.emptyCallback(this.clientCallback, this.jobId);
                  synchronized(ShadowJobWorkService.this.mJobSessions) {
                     this.stopSessionLocked();
                  }
               }

            } else {
               try {
                  this.clientJobService.startJob(this.jobParams);
               } catch (RemoteException var5) {
                  RemoteException e = var5;
                  this.forceFinishJob();
                  Log.e(ShadowJobWorkService.TAG, "ShadowJobService:startJob", e);
               }

            }
         }
      }

      public void onServiceConnected(ComponentName name, IBinder service) {
         VLog.i(ShadowJobWorkService.TAG, "ShadowJobService:onServiceConnected:%s", name);
         this.clientJobService = IJobService.Stub.asInterface(service);
         this.startJob(false);
      }

      public void onServiceDisconnected(ComponentName name) {
      }

      void forceFinishJob() {
         boolean var12 = false;

         label87: {
            try {
               var12 = true;
               this.clientCallback.jobFinished(this.jobId, false);
               var12 = false;
               break label87;
            } catch (RemoteException var16) {
               RemoteException e = var16;
               e.printStackTrace();
               var12 = false;
            } finally {
               if (var12) {
                  synchronized(ShadowJobWorkService.this.mJobSessions) {
                     this.stopSessionLocked();
                  }
               }
            }

            synchronized(ShadowJobWorkService.this.mJobSessions) {
               this.stopSessionLocked();
               return;
            }
         }

         synchronized(ShadowJobWorkService.this.mJobSessions) {
            this.stopSessionLocked();
         }

      }

      void stopSessionLocked() {
         VLog.i(ShadowJobWorkService.TAG, "ShadowJobService:stopSession:%d", this.jobId);
         if (this.clientJobService != null) {
            try {
               this.clientJobService.stopJob(this.jobParams);
            } catch (RemoteException var2) {
               RemoteException e = var2;
               e.printStackTrace();
            }
         }

         ShadowJobWorkService.this.mJobSessions.remove(this.jobId);
         VActivityManager.get().unbindService(ShadowJobWorkService.this, this);
      }
   }
}
