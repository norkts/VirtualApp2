package com.lody.virtual.server.job;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.text.TextUtils;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VJobScheduler;
import com.lody.virtual.client.stub.StubManifest;
import com.lody.virtual.helper.utils.Singleton;
import com.lody.virtual.os.VEnvironment;
import com.lody.virtual.remote.VJobWorkItem;
import com.lody.virtual.server.interfaces.IJobService;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@TargetApi(21)
public class VJobSchedulerService extends IJobService.Stub {
   private static final String TAG = VJobScheduler.class.getSimpleName();
   private static final int JOB_FILE_VERSION = 1;
   private final Map<JobId, JobConfig> mJobStore;
   private int mNextJobId;
   private final JobScheduler mScheduler;
   private final ComponentName mJobProxyComponent;
   private static final Singleton<VJobSchedulerService> gDefault = new Singleton<VJobSchedulerService>() {
      protected VJobSchedulerService create() {
         return new VJobSchedulerService();
      }
   };

   private VJobSchedulerService() {
      this.mJobStore = new HashMap();
      this.mNextJobId = 1;
      this.mScheduler = (JobScheduler)VirtualCore.get().getContext().getSystemService("jobscheduler");
      this.mJobProxyComponent = new ComponentName(VirtualCore.get().getHostPkg(), StubManifest.STUB_JOB);
      this.readJobs();
   }

   public static VJobSchedulerService get() {
      return (VJobSchedulerService)gDefault.get();
   }

   public int schedule(int uid, JobInfo job) {
      int id = job.getId();
      ComponentName service = job.getService();
      JobId jobId = new JobId(uid, service.getPackageName(), id);
      JobConfig config;
      synchronized(this.mJobStore) {
         config = (JobConfig)this.mJobStore.get(jobId);
         if (config == null) {
            int jid = this.mNextJobId++;
            config = new JobConfig(jid, service.getClassName(), job.getExtras());
            this.mJobStore.put(jobId, config);
         }
      }

      config.serviceName = service.getClassName();
      config.extras = job.getExtras();
      this.saveJobs();
      mirror.android.app.job.JobInfo.jobId.set(job, config.virtualJobId);
      mirror.android.app.job.JobInfo.service.set(job, this.mJobProxyComponent);
      return this.mScheduler.schedule(job);
   }

   private void saveJobs() {
      File jobFile = VEnvironment.getJobConfigFile();
      Parcel p = Parcel.obtain();

      try {
         p.writeInt(1);
         p.writeInt(this.mJobStore.size());
         Iterator var10 = this.mJobStore.entrySet().iterator();

         while(var10.hasNext()) {
            Map.Entry<JobId, JobConfig> entry = (Map.Entry)var10.next();
            ((JobId)entry.getKey()).writeToParcel(p, 0);
            ((JobConfig)entry.getValue()).writeToParcel(p, 0);
         }

         FileOutputStream fos = new FileOutputStream(jobFile);
         fos.write(p.marshall());
         fos.close();
      } catch (Exception var8) {
         Exception e = var8;
         e.printStackTrace();
      } finally {
         p.recycle();
      }

   }

   private void readJobs() {
      File jobFile = VEnvironment.getJobConfigFile();
      if (jobFile.exists()) {
         Parcel p = Parcel.obtain();

         try {
            FileInputStream fis = new FileInputStream(jobFile);
            byte[] bytes = new byte[(int)jobFile.length()];
            int len = fis.read(bytes);
            fis.close();
            if (len != bytes.length) {
               throw new IOException("Unable to read job config.");
            }

            p.unmarshall(bytes, 0, bytes.length);
            p.setDataPosition(0);
            int version = p.readInt();
            if (version != 1) {
               throw new IOException("Bad version of job file: " + version);
            }

            if (!this.mJobStore.isEmpty()) {
               this.mJobStore.clear();
            }

            int count = p.readInt();
            int max = 0;

            for(int i = 0; i < count; ++i) {
               JobId jobId = new JobId(p);
               JobConfig config = new JobConfig(p);
               this.mJobStore.put(jobId, config);
               max = Math.max(max, config.virtualJobId);
            }

            this.mNextJobId = max + 1;
         } catch (Exception var15) {
            Exception e = var15;
            e.printStackTrace();
         } finally {
            p.recycle();
         }

      }
   }

   public void cancel(int uid, int jobId) {
      synchronized(this.mJobStore) {
         boolean changed = false;
         Iterator<Map.Entry<JobId, JobConfig>> iterator = this.mJobStore.entrySet().iterator();

         while(iterator.hasNext()) {
            Map.Entry<JobId, JobConfig> entry = (Map.Entry)iterator.next();
            JobId job = (JobId)entry.getKey();
            JobConfig config = (JobConfig)entry.getValue();
            if ((uid == -1 || job.vuid == uid) && job.clientJobId == jobId) {
               changed = true;
               this.mScheduler.cancel(config.virtualJobId);
               iterator.remove();
               break;
            }
         }

         if (changed) {
            this.saveJobs();
         }

      }
   }

   public void cancelAll(int uid) {
      synchronized(this.mJobStore) {
         boolean changed = false;
         Iterator<Map.Entry<JobId, JobConfig>> iterator = this.mJobStore.entrySet().iterator();

         while(iterator.hasNext()) {
            Map.Entry<JobId, JobConfig> entry = (Map.Entry)iterator.next();
            JobId job = (JobId)entry.getKey();
            if (job.vuid == uid) {
               JobConfig config = (JobConfig)entry.getValue();
               this.mScheduler.cancel(config.virtualJobId);
               changed = true;
               iterator.remove();
               break;
            }
         }

         if (changed) {
            this.saveJobs();
         }

      }
   }

   public List<JobInfo> getAllPendingJobs(int uid) {
      List<JobInfo> jobs = this.mScheduler.getAllPendingJobs();
      synchronized(this.mJobStore) {
         Iterator<JobInfo> iterator = jobs.listIterator();

         while(iterator.hasNext()) {
            JobInfo job = (JobInfo)iterator.next();
            if (!StubManifest.STUB_JOB.equals(job.getService().getClassName())) {
               iterator.remove();
            } else {
               Map.Entry<JobId, JobConfig> jobEntry = this.findJobByVirtualJobId(job.getId());
               if (jobEntry == null) {
                  iterator.remove();
               } else {
                  JobId jobId = (JobId)jobEntry.getKey();
                  JobConfig config = (JobConfig)jobEntry.getValue();
                  if (jobId.vuid != uid) {
                     iterator.remove();
                  } else {
                     mirror.android.app.job.JobInfo.jobId.set(job, jobId.clientJobId);
                     mirror.android.app.job.JobInfo.service.set(job, new ComponentName(jobId.packageName, config.serviceName));
                  }
               }
            }
         }

         return jobs;
      }
   }

   public Map.Entry<JobId, JobConfig> findJobByVirtualJobId(int virtualJobId) {
      synchronized(this.mJobStore) {
         Iterator var3 = this.mJobStore.entrySet().iterator();

         Map.Entry entry;
         do {
            if (!var3.hasNext()) {
               return null;
            }

            entry = (Map.Entry)var3.next();
         } while(((JobConfig)entry.getValue()).virtualJobId != virtualJobId);

         return entry;
      }
   }

   @TargetApi(24)
   public JobInfo getPendingJob(int uid, int jobId) {
      JobInfo jobInfo = null;
      synchronized(this.mJobStore) {
         Iterator<Map.Entry<JobId, JobConfig>> iterator = this.mJobStore.entrySet().iterator();

         while(iterator.hasNext()) {
            Map.Entry<JobId, JobConfig> entry = (Map.Entry)iterator.next();
            JobId job = (JobId)entry.getKey();
            if (job.vuid == uid && job.clientJobId == jobId) {
               jobInfo = this.mScheduler.getPendingJob(job.clientJobId);
               break;
            }
         }

         return jobInfo;
      }
   }

   @TargetApi(26)
   public int enqueue(int uid, JobInfo job, VJobWorkItem workItem) {
      if (workItem.get() == null) {
         return -1;
      } else {
         int id = job.getId();
         ComponentName service = job.getService();
         JobId jobId = new JobId(uid, service.getPackageName(), id);
         JobConfig config;
         synchronized(this.mJobStore) {
            config = (JobConfig)this.mJobStore.get(jobId);
            if (config == null) {
               int jid = this.mNextJobId++;
               config = new JobConfig(jid, service.getClassName(), job.getExtras());
               this.mJobStore.put(jobId, config);
            }
         }

         config.serviceName = service.getClassName();
         config.extras = job.getExtras();
         this.saveJobs();
         mirror.android.app.job.JobInfo.jobId.set(job, config.virtualJobId);
         mirror.android.app.job.JobInfo.service.set(job, this.mJobProxyComponent);
         return this.mScheduler.enqueue(job, workItem.get());
      }
   }

   // $FF: synthetic method
   VJobSchedulerService(Object x0) {
      this();
   }

   public static final class JobConfig implements Parcelable {
      public int virtualJobId;
      public String serviceName;
      public PersistableBundle extras;
      public static final Parcelable.Creator<JobConfig> CREATOR = new Parcelable.Creator<JobConfig>() {
         public JobConfig createFromParcel(Parcel source) {
            return new JobConfig(source);
         }

         public JobConfig[] newArray(int size) {
            return new JobConfig[size];
         }
      };

      JobConfig(int virtualJobId, String serviceName, PersistableBundle extra) {
         this.virtualJobId = virtualJobId;
         this.serviceName = serviceName;
         this.extras = extra;
      }

      JobConfig(Parcel in) {
         this.virtualJobId = in.readInt();
         this.serviceName = in.readString();
         this.extras = (PersistableBundle)in.readParcelable(PersistableBundle.class.getClassLoader());
      }

      public int describeContents() {
         return 0;
      }

      public void writeToParcel(Parcel dest, int flags) {
         dest.writeInt(this.virtualJobId);
         dest.writeString(this.serviceName);
         dest.writeParcelable(this.extras, flags);
      }
   }

   public static final class JobId implements Parcelable {
      public int vuid;
      public String packageName;
      public int clientJobId;
      public static final Parcelable.Creator<JobId> CREATOR = new Parcelable.Creator<JobId>() {
         public JobId createFromParcel(Parcel source) {
            return new JobId(source);
         }

         public JobId[] newArray(int size) {
            return new JobId[size];
         }
      };

      JobId(int vuid, String packageName, int id) {
         this.vuid = vuid;
         this.packageName = packageName;
         this.clientJobId = id;
      }

      JobId(Parcel in) {
         this.vuid = in.readInt();
         this.packageName = in.readString();
         this.clientJobId = in.readInt();
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            JobId jobId = (JobId)o;
            return this.vuid == jobId.vuid && this.clientJobId == jobId.clientJobId && TextUtils.equals(this.packageName, jobId.packageName);
         } else {
            return false;
         }
      }

      public int hashCode() {
         int result = this.vuid;
         result = 31 * result + (this.packageName != null ? this.packageName.hashCode() : 0);
         result = 31 * result + this.clientJobId;
         return result;
      }

      public int describeContents() {
         return 0;
      }

      public void writeToParcel(Parcel dest, int flags) {
         dest.writeInt(this.vuid);
         dest.writeString(this.packageName);
         dest.writeInt(this.clientJobId);
      }
   }
}
