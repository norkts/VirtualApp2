package com.kook.network.interceptors;

import com.kook.network.file.DownloadFileResponseBody;
import com.kook.network.file.IDownloadListener;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadInterceptor extends BaseInterceptor {
   private IDownloadListener mDownloadListener;

   public DownloadInterceptor(IDownloadListener downloadListener) {
      this.mDownloadListener = downloadListener;
   }

   public Response intercept(Interceptor.Chain chain) throws IOException {
      Request request = chain.request();
      Response response = null;

      try {
         response = chain.proceed(request);
      } catch (SocketTimeoutException var5) {
         SocketTimeoutException e = var5;
         this.handleException(e);
         throw e;
      } catch (UnknownHostException var6) {
         UnknownHostException e = var6;
         this.handleException(e);
         throw e;
      } catch (IOException var7) {
         IOException e = var7;
         this.handleException(e);
         throw e;
      }

      return response.newBuilder().body(new DownloadFileResponseBody(response.body(), this.mDownloadListener)).build();
   }

   private void handleException(Exception e) {
      if (this.mDownloadListener != null) {
         this.mDownloadListener.onDownloadFail(e);
      }

   }
}
