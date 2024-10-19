package com.carlos.science.stebcore.step;

import android.app.Activity;
import com.carlos.common.utils.ResponseProgram;
import com.carlos.science.stebcore.StepImpl;
import org.jdeferred.Promise;

public class CloseInputMethod extends StepImpl {
   public static CloseInputMethod getInstance() {
      CloseInputMethod closeInputMethod = new CloseInputMethod();
      return closeInputMethod;
   }

   public void onActivityResumed(Activity activity) {
   }

   public Promise<Void, Throwable, Void> beforeTask() {
      return ResponseProgram.defer().when(() -> {
         this.sleep(400L);
      });
   }

   public boolean task() {
      this.finish();
      return true;
   }

   public void afterTask() {
   }
}
