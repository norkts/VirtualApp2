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
         if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2LGUVGiZONyggLwguLmQVNCo=")).equals(action)) {
            jobParams = (JobParameters)intent.getParcelableExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LD4AOmcFJAR9Dl0p")));
            this.startJob(jobParams);
         } else if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2LGUVGiZONyggKi4mXW8FRVo=")).equals(action)) {
            jobParams = (JobParameters)intent.getParcelableExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LD4AOmcFJAR9Dl0p")));
            this.stopJob(jobParams);
         }
      }

      return 2;
   }

   public static void startJob(Context context, JobParameters jobParams) {
      Intent intent = new Intent(context, ShadowJobWorkService.class);
      intent.setAction(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2LGUVGiZONyggLwguLmQVNCo=")));
      intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LD4AOmcFJAR9Dl0p")), jobParams);
      context.startService(intent);
   }

   public static void stopJob(Context context, JobParameters jobParams) {
      Intent intent = new Intent(context, ShadowJobWorkService.class);
      intent.setAction(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2LGUVGiZONyggKi4mXW8FRVo=")));
      intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LD4AOmcFJAR9Dl0p")), jobParams);
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
      this.mScheduler = (JobScheduler)this.getSystemService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LD4AOm8zLCBiDgovKhcMKA==")));
   }

   public void onDestroy() {
      VLog.i(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii5fP2gFGj1rNB46Oy0MKGUVLClrClEcLCwqJ2EjFjVsJxpF")));
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
                  VLog.i(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii5fP2gFGj1rNB46Oy0MKGUVLClrClEpIxgcUmIFMDFvDiwuPl8AD3VSID5qNwoWJF9aLGUjSFo=")), service.getComponent(), jobId);
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
            VLog.i(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qD28LTSV9MwU/KBhSVg==")), jobId);
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
         VLog.i(ShadowJobWorkService.TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii5fP2gFGj1rNB46Oy0MKGUVLClrClEsLT5bKmAjJDduDjAgLysYCmgKFiBiESgcJy0mLm4JHTFpN1RF")), this.jobId);
         this.clientCallback.acknowledgeStartMessage(this.jobId, ongoing);
      }

      public void acknowledgeStopMessage(int jobId, boolean reschedule) throws RemoteException {
         this.isWorking = false;
         VLog.i(ShadowJobWorkService.TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii5fP2gFGj1rNB46Oy0MKGUVLClrClEsLT5bKmAjJDduDjAgLysYCmUgIEhoHjAcOwc+LHs0FjI=")), this.jobId);
         this.clientCallback.acknowledgeStopMessage(this.jobId, reschedule);
      }

      public void jobFinished(int jobId, boolean reschedule) throws RemoteException {
         this.isWorking = false;
         VLog.i(ShadowJobWorkService.TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii5fP2gFGj1rNB46Oy0MKGUVLClrClEhLD4MBGMKRSxlJAYuLzk5J2sVSFo=")), this.jobId);
         this.clientCallback.jobFinished(this.jobId, reschedule);
      }

      public boolean completeWork(int jobId, int workId) throws RemoteException {
         VLog.i(ShadowJobWorkService.TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii5fP2gFGj1rNB46Oy0MKGUVLClrClEqLD4IDmAaLD9uDzweLBg9PnsFMFo=")), this.jobId);
         return this.clientCallback.completeWork(this.jobId, workId);
      }

      public JobWorkItem dequeueWork(int jobId) throws RemoteException {
         try {
            this.lastWorkItem = null;
            VLog.i(ShadowJobWorkService.TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii5fP2gFGj1rNB46Oy0MKGUVLClrClEvLhc+CWIFLCBiJFk7KgM5J2sVSFo=")), this.jobId);
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
            VLog.i(ShadowJobWorkService.TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii5fP2gFGj1rNB46Oy0MKGUVLClrClEvLhc+CWIFLCBiJFk7KgM6Vg==")) + e);
         }

         return null;
      }

      public void startJob(boolean wait) {
         if (this.isWorking) {
            VLog.w(ShadowJobWorkService.TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii5fP2gFGj1rNB46Oy0MKGUVLClrClE6Kgg+CGYYHippMwEuLzoiJm8KMzRlHjM3IC0YOW8gBgJpAVRF")), this.jobId);
         } else {
            VLog.i(ShadowJobWorkService.TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii5fP2gFGj1rNB46Oy0MKGUVLClrClE6Kgg+CGYYHippMwEuLz5SVg==")), this.jobId);
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
                  Log.e(ShadowJobWorkService.TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii5fP2gFGj1rNB46Oy0MKGUVLClrClE6Kgg+CGYYHippN1RF")), e);
               }

            }
         }
      }

      public void onServiceConnected(ComponentName name, IBinder service) {
         VLog.i(ShadowJobWorkService.TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii5fP2gFGj1rNB46Oy0MKGUVLClrClEcLCs2J2EzICxpJAoCKQgqKmsFLCBoES8tDgguVg==")), name);
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
         VLog.i(ShadowJobWorkService.TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii5fP2gFGj1rNB46Oy0MKGUVLClrClE6KggADmkgLDZlJBoeKV45J2sVSFo=")), this.jobId);
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
