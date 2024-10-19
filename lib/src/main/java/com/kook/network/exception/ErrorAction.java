package com.kook.network.exception;

import android.text.TextUtils;
import com.kook.common.utils.HVLog;
import com.kook.network.StringFog;
import io.reactivex.functions.Consumer;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import retrofit2.HttpException;

public class ErrorAction implements Consumer<Throwable> {
   public void accept(Throwable throwable) throws Exception {
      HVLog.e(StringFog.decrypt("jtPtjpXak/CIg9bv") + throwable);
      HVLog.printThrowable(throwable);
      if (!(throwable instanceof ConnectException) && !(throwable instanceof UnknownHostException)) {
         if (throwable instanceof SocketTimeoutException) {
            HVLog.e(StringFog.decrypt("g9DxjaPHndGogP7OhNPjg4LVnOCgjsbt"));
         } else if (throwable instanceof HttpException) {
            HVLog.e(StringFog.decrypt("jfPijqfDkP6Fj/3hg8DAQw==") + ((HttpException)throwable).code());
         } else if (throwable instanceof ApiException) {
            this.onApiError((ApiException)throwable);
         } else if (!TextUtils.isEmpty(throwable.getMessage())) {
            HVLog.e(StringFog.decrypt("jfPFjLLHnPO0jsbXhNPjjbHikMKQg9n+jurZj5Xok+2IgdLhjfPijqfDksyCidX0j9H0jpHgkN+VgOfqjfDK") + throwable.getMessage());
         }
      } else {
         HVLog.e(StringFog.decrypt("jNL+jJb+nPO0jsbX"));
      }

   }

   public void onApiError(ApiException throwable) {
      if (throwable.getMessage() != null) {
         HVLog.e(throwable.getMessage());
      } else if (throwable.getErrorCode() != null) {
         HVLog.e(throwable.getErrorCode());
      }

   }
}
