package com.lody.virtual.client.hook.proxies.job;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobWorkItem;
import android.os.Build.VERSION;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.MethodProxy;
import com.lody.virtual.client.hook.utils.MethodParameterUtils;
import com.lody.virtual.client.ipc.VJobScheduler;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.compat.JobWorkItemCompat;
import com.lody.virtual.os.VUserHandle;
import java.lang.reflect.Method;
import java.util.List;
import mirror.android.app.job.IJobScheduler;
import mirror.android.content.pm.ParceledListSlice;

@TargetApi(21)
public class JobServiceStub extends BinderInvocationProxy {
   public JobServiceStub() {
      super(IJobScheduler.Stub.asInterface, "jobscheduler");
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new schedule());
      this.addMethodProxy(new getAllPendingJobs());
      this.addMethodProxy(new cancelAll());
      this.addMethodProxy(new cancel());
      if (VERSION.SDK_INT >= 24) {
         this.addMethodProxy(new getPendingJob());
      }

      if (VERSION.SDK_INT >= 26) {
         this.addMethodProxy(new enqueue());
      }

   }

   @TargetApi(26)
   private class enqueue extends MethodProxy {
      private enqueue() {
      }

      public String getMethodName() {
         return "enqueue";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         int jobInfoIndex = 0;
         int jobWorkIntemIndex = 1;
         if (args.length > 2) {
            jobInfoIndex = 1;
            jobWorkIntemIndex = 2;
         }

         JobInfo jobInfo = (JobInfo)args[jobInfoIndex];
         JobWorkItem workItem = JobWorkItemCompat.redirect(VUserHandle.myUserId(), (JobWorkItem)args[jobWorkIntemIndex], getAppPkg());
         return VJobScheduler.get().enqueue(jobInfo, workItem);
      }

      // $FF: synthetic method
      enqueue(Object x1) {
         this();
      }
   }

   private class getPendingJob extends MethodProxy {
      private getPendingJob() {
      }

      public String getMethodName() {
         return "getPendingJob";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         int jobId = (Integer)args[0];
         return VJobScheduler.get().getPendingJob(jobId);
      }

      // $FF: synthetic method
      getPendingJob(Object x1) {
         this();
      }
   }

   private class cancel extends MethodProxy {
      private cancel() {
      }

      public String getMethodName() {
         return "cancel";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         int jobId = (Integer)args[0];
         VJobScheduler.get().cancel(jobId);
         return 0;
      }

      // $FF: synthetic method
      cancel(Object x1) {
         this();
      }
   }

   private class cancelAll extends MethodProxy {
      private cancelAll() {
      }

      public String getMethodName() {
         return "cancelAll";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         VJobScheduler.get().cancelAll();
         return 0;
      }

      // $FF: synthetic method
      cancelAll(Object x1) {
         this();
      }
   }

   private class getAllPendingJobs extends MethodProxy {
      private getAllPendingJobs() {
      }

      public String getMethodName() {
         return "getAllPendingJobs";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         List res = VJobScheduler.get().getAllPendingJobs();
         if (res == null) {
            return null;
         } else {
            return BuildCompat.isQ() ? ParceledListSlice.ctorQ.newInstance(res) : res;
         }
      }

      // $FF: synthetic method
      getAllPendingJobs(Object x1) {
         this();
      }
   }

   private class schedule extends MethodProxy {
      private schedule() {
      }

      public String getMethodName() {
         return "schedule";
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         JobInfo jobInfo = (JobInfo)MethodParameterUtils.getFirstParam(args, JobInfo.class);
         return VJobScheduler.get().schedule(jobInfo);
      }

      // $FF: synthetic method
      schedule(Object x1) {
         this();
      }
   }
}
