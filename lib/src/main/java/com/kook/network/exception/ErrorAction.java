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
      HVLog.e("异常日志" + throwable);
      HVLog.printThrowable(throwable);
      if (!(throwable instanceof ConnectException) && !(throwable instanceof UnknownHostException)) {
         if (throwable instanceof SocketTimeoutException) {
            HVLog.e("连接超时，请重试");
         } else if (throwable instanceof HttpException) {
            HVLog.e("服务器错误(" + ((HttpException)throwable).code());
         } else if (throwable instanceof ApiException) {
            this.onApiError((ApiException)throwable);
         } else if (!TextUtils.isEmpty(throwable.getMessage())) {
            HVLog.e("未知错误，最好将其上报给服务端，供异常排查" + throwable.getMessage());
         }
      } else {
         HVLog.e("网络错误");
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
