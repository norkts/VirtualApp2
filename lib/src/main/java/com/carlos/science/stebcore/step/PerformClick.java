package com.carlos.science.stebcore.step;

import android.app.Activity;
import android.view.View;
import com.carlos.common.utils.ResponseProgram;
import com.carlos.science.client.core.FindView;
import com.carlos.science.stebcore.StepImpl;
import org.jdeferred.Promise;

public abstract class PerformClick extends StepImpl {
   protected RES_TYPE res_type;
   protected Object res;

   public void onActivityResumed(Activity activity) {
   }

   public abstract String getRes();

   public Promise<Void, Throwable, Void> beforeTask() {
      return this.getRes().equals(this.res) ? ResponseProgram.defer().when(() -> {
         View dk = FindView.findView(this.getActivity(), this.getRes());
         boolean attachedToWindow = dk.isAttachedToWindow();

         while(!attachedToWindow) {
            if (this.finishStep) {
               return;
            }

            dk = FindView.findView(this.getActivity(), this.getRes());
            attachedToWindow = dk.isAttachedToWindow();
            this.sleep(400L);
         }

      }) : null;
   }

   public boolean task() {
      View dk = FindView.findView(this.getActivity(), this.getRes());
      dk.performClick();
      return true;
   }

   public void afterTask() {
      this.finish();
   }

   public static enum RES_TYPE {
      RES_INT,
      RES_NAME,
      RES_CLASS;
   }
}
