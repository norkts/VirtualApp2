package com.lody.virtual.server.pm.installer;

import android.content.Intent;
import android.content.pm.IPackageInstallObserver2;
import android.os.Bundle;

public class PackageInstallObserver {
   private final IPackageInstallObserver2.Stub mBinder = new IPackageInstallObserver2.Stub() {
      public void onUserActionRequired(Intent intent) {
         PackageInstallObserver.this.onUserActionRequired(intent);
      }

      public void onPackageInstalled(String basePackageName, int returnCode, String msg, Bundle extras) {
         PackageInstallObserver.this.onPackageInstalled(basePackageName, returnCode, msg, extras);
      }
   };

   public IPackageInstallObserver2 getBinder() {
      return this.mBinder;
   }

   public void onUserActionRequired(Intent intent) {
   }

   public void onPackageInstalled(String basePackageName, int returnCode, String msg, Bundle extras) {
   }
}
