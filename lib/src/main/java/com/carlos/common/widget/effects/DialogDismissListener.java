package com.carlos.common.widget.effects;

import androidx.fragment.app.DialogFragment;
import java.util.List;

public interface DialogDismissListener {
   void dismiss(DialogFragment var1);

   public interface BaseListView<M> {
      void onRefreshSuccess(List<M> var1);

      void onLoadMoreSuccess(List<M> var1);

      void onLoadMoreFailed();

      void showMoreMore();

      void onComplete(boolean var1);

      void noNetConnect();
   }
}
