package mirror.android.app.job;

import android.annotation.TargetApi;
import android.content.ComponentName;
import mirror.RefClass;
import mirror.RefInt;
import mirror.RefObject;

@TargetApi(21)
public class JobInfo {
   public static Class<?> TYPE = RefClass.load(JobInfo.class, android.app.job.JobInfo.class);
   public static RefInt jobId;
   public static RefObject<ComponentName> service;
}
