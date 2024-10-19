package com.lody.virtual.client.stub;

import android.annotation.TargetApi;
import android.app.Service;
import android.app.job.IJobService;
import android.app.job.JobParameters;
import android.content.Intent;
import android.os.IBinder;
import com.lody.virtual.StringFog;
import com.lody.virtual.helper.utils.VLog;

@TargetApi(21)
public class ShadowJobService extends Service {
   private final IJobService mService = new IJobService.Stub() {
      public void startJob(JobParameters jobParams) {
         ShadowJobWorkService.startJob(ShadowJobService.this, jobParams);
      }

      public void stopJob(JobParameters jobParams) {
         ShadowJobWorkService.stopJob(ShadowJobService.this, jobParams);
      }
   };

   public void onCreate() {
      super.onCreate();
      VLog.e(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii5fP2gFGj1rNB46Oy0MKGUVLClrAVRF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("MwMbOGozBhNhNDA7LBcMVg==")));
   }

   public IBinder onBind(Intent intent) {
      VLog.e(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii5fP2gFGj1rNB46Oy0MKGUVLClrAVRF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("MwMbOGozBhRjDlkwPTkmVg==")) + intent);
      return this.mService.asBinder();
   }
}
