package com.carlos.science;

import android.app.Activity;
import android.app.Application;

public interface IVirtualController {
   IVirtualController EMPTY = new IVirtualController() {
      public IController getController() {
         return null;
      }

      public void onCreateController(Application application, String hostPkg) {
      }

      public void controllerActivityCreate(Activity activity) {
      }

      public void controllerActivityResume(Activity activity) {
      }

      public void controllerActivityDestroy(Activity activity) {
      }

      public void controllerActivityPause(Activity activity) {
      }
   };

   IController getController();

   void onCreateController(Application var1, String var2);

   void controllerActivityCreate(Activity var1);

   void controllerActivityResume(Activity var1);

   void controllerActivityDestroy(Activity var1);

   void controllerActivityPause(Activity var1);

   public interface IController {
      boolean needShow();
   }
}
