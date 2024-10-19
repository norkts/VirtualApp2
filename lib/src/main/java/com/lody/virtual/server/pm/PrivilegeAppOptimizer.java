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
      this.privilegeApps.add("com.google.android.gms");
      this.privilegeApps.add("com.google.android.gsf");
      this.privilegeProcessNames.add("com.google.android.gms.persistent");
      this.privilegeProcessNames.add("com.google.process.gapps");
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
         VActivityManagerService.get().sendBroadcastAsUser(this.specifyApp(new Intent("android.intent.action.BOOT_COMPLETED", (Uri)null), packageName, userId), new VUserHandle(userId));
         return true;
      }
   }

   private Intent specifyApp(Intent intent, String packageName, int userId) {
      intent.putExtra("_VA_|_privilege_pkg_", packageName);
      intent.putExtra("_VA_|_user_id_", userId);
      return intent;
   }
}
