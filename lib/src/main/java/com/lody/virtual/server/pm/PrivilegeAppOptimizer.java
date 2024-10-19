package com.lody.virtual.server.pm;

import android.content.Intent;
import android.net.Uri;
import com.lody.virtual.StringFog;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.server.am.VActivityManagerService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class PrivilegeAppOptimizer {
   private static final PrivilegeAppOptimizer sInstance = new PrivilegeAppOptimizer();
   private final List<String> privilegeApps = new ArrayList();
   private final List<String> privilegeProcessNames = new ArrayList();

   private PrivilegeAppOptimizer() {
      this.privilegeApps.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojPCVgJDgoKAMYOW8VBgRlJx4vPC4mL2EjSFo=")));
      this.privilegeApps.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojPCVgJDgoKAMYOW8VBgRlJx4vPC4mD2IzSFo=")));
      this.privilegeProcessNames.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojPCVgJDgoKAMYOW8VBgRlJx4vPC4mL2EkRTNuASg8Ki0YCmsFBiA=")));
      this.privilegeProcessNames.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojPCVgJDgoKAMYKmoVNClrDjA6PC4mO2EVODY=")));
   }

   public static PrivilegeAppOptimizer get() {
      return sInstance;
   }

   public List<String> getPrivilegeApps() {
      return Collections.unmodifiableList(this.privilegeApps);
   }

   public void addPrivilegeApp(String packageName) {
      this.privilegeApps.add(packageName);
   }

   public void removePrivilegeApp(String packageName) {
      this.privilegeApps.remove(packageName);
   }

   public boolean isPrivilegeApp(String packageName) {
      return this.privilegeApps.contains(packageName);
   }

   public boolean isPrivilegeProcess(String processName) {
      return this.privilegeProcessNames.contains(processName);
   }

   public void performOptimizeAllApps() {
      Iterator var1 = this.privilegeApps.iterator();

      while(var1.hasNext()) {
         String pkg = (String)var1.next();
         this.performOptimize(pkg, -1);
      }

   }

   public boolean performOptimize(String packageName, int userId) {
      if (!this.isPrivilegeApp(packageName)) {
         return false;
      } else {
         VActivityManagerService.get().sendBroadcastAsUser(this.specifyApp(new Intent(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kzKj42PW8aASZoATA/IxgAKk42MBRkJTAOIAYuAWQbHlRkDygJ")), (Uri)null), packageName, userId), new VUserHandle(userId));
         return true;
      }
   }

   private Intent specifyApp(Intent intent, String packageName, int userId) {
      intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh9hESwzLD0cCGkjEithJDwiLjsAVg==")), packageName);
      intent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHh9mASg/IzxfMWk2NFo=")), userId);
      return intent;
   }
}
