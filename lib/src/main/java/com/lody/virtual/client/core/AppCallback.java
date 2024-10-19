package com.lody.virtual.client.core;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

public interface AppCallback {
   AppCallback EMPTY = new AppCallback() {
      public void beforeStartApplication(String packageName, String processName, Context context) {
      }

      public void beforeApplicationCreate(String packageName, String processName, Application application) {
      }

      public void afterApplicationCreate(String packageName, String processName, Application application) {
      }

      public void beforeActivityOnCreate(Activity activity) {
      }

      public void afterActivityOnCreate(Activity activity) {
      }

      public void beforeActivityOnStart(Activity activity) {
      }

      public void afterActivityOnStart(Activity activity) {
      }

      public void beforeActivityOnResume(Activity activity) {
      }

      public void afterActivityOnResume(Activity activity) {
      }

      public void beforeActivityOnStop(Activity activity) {
      }

      public void afterActivityOnStop(Activity activity) {
      }

      public void beforeActivityOnDestroy(Activity activity) {
      }

      public void afterActivityOnDestroy(Activity activity) {
      }
   };

   void beforeStartApplication(String var1, String var2, Context var3);

   void beforeApplicationCreate(String var1, String var2, Application var3);

   void afterApplicationCreate(String var1, String var2, Application var3);

   void beforeActivityOnCreate(Activity var1);

   void afterActivityOnCreate(Activity var1);

   void beforeActivityOnStart(Activity var1);

   void afterActivityOnStart(Activity var1);

   void beforeActivityOnResume(Activity var1);

   void afterActivityOnResume(Activity var1);

   void beforeActivityOnStop(Activity var1);

   void afterActivityOnStop(Activity var1);

   void beforeActivityOnDestroy(Activity var1);

   void afterActivityOnDestroy(Activity var1);
}
