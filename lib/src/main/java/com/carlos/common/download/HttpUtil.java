package com.carlos.common.download;

import com.carlos.libcommon.StringFog;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {
   private OkHttpClient mOkHttpClient;
   private static HttpUtil mInstance;
   private static final long CONNECT_TIMEOUT = 60L;
   private static final long READ_TIMEOUT = 60L;
   private static final long WRITE_TIMEOUT = 60L;
   static Headers.Builder builder = new Headers.Builder();

   public void downloadFileByRange(String url, long startIndex, long endIndex, Callback callback) throws IOException {
      Request request = (new Request.Builder()).header("RANGE", "bytes=" + startIndex + "-" + endIndex).addHeader("Accept-Encoding", "identity").url(url).build();
      this.doAsync(request, callback);
   }

   public void getContentLength(String url, Callback callback) throws IOException {
      Headers headers = builder.set("referer", url).build();
      RequestBody formBody = (new FormBody.Builder()).build();
      Request request = (new Request.Builder()).url(url).headers(headers).build();
      this.doAsync(request, callback);
   }

   private void doAsync(Request request, Callback callback) throws IOException {
      Call call = this.mOkHttpClient.newCall(request);
      call.enqueue(callback);
   }

   private Response doSync(Request request) throws IOException {
      Call call = this.mOkHttpClient.newCall(request);
      return call.execute();
   }

   public static HttpUtil getInstance() {
      if (null == mInstance) {
         Class var0 = HttpUtil.class;
         synchronized(HttpUtil.class) {
            if (null == mInstance) {
               mInstance = new HttpUtil();
            }
         }
      }

      return mInstance;
   }

   public HttpUtil() {
      OkHttpClient.Builder builder = (new OkHttpClient.Builder()).connectTimeout(60L, TimeUnit.SECONDS).writeTimeout(60L, TimeUnit.SECONDS).readTimeout(60L, TimeUnit.SECONDS);
      this.mOkHttpClient = builder.build();
   }

   static {
      builder.add("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
      builder.add("Accept-Encoding", "gzip, deflate");
      builder.add("Upgrade-Insecure-Requests", "1");
      builder.add("accept-language", "zh-CN,zh;q=0.9,zh-TW;q=0.8,en-US;q=0.7,en;q=0.6,ja;q=0.5");
      builder.add("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36");
   }
}
