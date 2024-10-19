package com.carlos.science.stebcore.step;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import com.carlos.common.utils.ResponseProgram;
import com.carlos.libcommon.StringFog;
import com.carlos.science.stebcore.StepImpl;
import com.kook.controller.server.IServerController;
import org.jdeferred.Promise;

public class KeyBackActivity extends StepImpl {
   protected String className;

   public static KeyBackActivity getInstance(String className) {
      KeyBackActivity stepTrendActivity = new KeyBackActivity();
      stepTrendActivity.className = className;
      return stepTrendActivity;
   }

   public void onActivityResumed(Activity activity) {
      Log.d(StringFog.decrypt("IBEXBiwDLx8="), StringFog.decrypt("ltjhk+zjuevMiu7Y") + activity.getLocalClassName() + StringFog.decrypt("lPD+n/jMc5bk6ZfU7ofR6ID300UKMCcCHBlQ"));
      this.task();
   }

   public Promise<Void, Throwable, Void> beforeTask() {
      return null;
   }

   public boolean task() {
      Activity activity = this.getActivity();
      String localClassName = activity.getLocalClassName();
      Log.d(StringFog.decrypt("IBEXBiwDLx8="), StringFog.decrypt("ltjhk+zjuevMiu7Y") + localClassName + StringFog.decrypt("lPD+n/jM"));
      if (localClassName.equals(this.className)) {
         ResponseProgram.defer().when(() -> {
            boolean attachedToWindow;
            do {
               if (this.finishStep) {
                  return;
               }

               View decorView = activity.getWindow().getDecorView();
               attachedToWindow = decorView.isAttachedToWindow();
               this.sleep(400L);
            } while(!attachedToWindow);

         }).done((VOID) -> {
            this.finish();
         });
      } else {
         this.sendBackKey();
      }

      return true;
   }

   protected void sendBackKey() {
      try {
         IServerController iServerControler = this.getIServerControler();
         iServerControler.sendKeyEvent(4);
      } catch (Exception var2) {
         Exception e = var2;
         Log.e(StringFog.decrypt("IBEXBiwDLx8="), StringFog.decrypt("IBEXBjEcOh0HTxYfPQ4dGEU=") + e.toString());
      }

   }

   public void afterTask() {
   }
}
