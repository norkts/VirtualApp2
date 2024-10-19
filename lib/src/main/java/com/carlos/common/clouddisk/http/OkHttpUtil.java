package com.carlos.common.clouddisk.http;

import android.os.Handler;
import android.os.Looper;
import com.carlos.libcommon.StringFog;
import com.kook.common.utils.HVLog;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtil {
   private static OkHttpUtil mHttpUtil;
   private OkHttpClient.Builder mOkHttpClientBuilder = new OkHttpClient.Builder();
   private OkHttpClient mOkHttpClient;
   private Handler mDelivery;
   private MyCookieJar cookieJar = new MyCookieJar();

   private OkHttpUtil() {
      this.mOkHttpClientBuilder.cookieJar(this.cookieJar);
      this.mOkHttpClientBuilder.sslSocketFactory(createSSLSocketFactory(), new TrustAllCerts());
      this.mOkHttpClientBuilder.hostnameVerifier(new TrustAllHostnameVerifier());
      this.mOkHttpClient = this.mOkHttpClientBuilder.build();
      this.mDelivery = new Handler(Looper.getMainLooper());
   }

   private static OkHttpUtil getInstance() {
      if (mHttpUtil == null) {
         Class var0 = OkHttpUtil.class;
         synchronized(OkHttpUtil.class) {
            if (mHttpUtil == null) {
               mHttpUtil = new OkHttpUtil();
            }
         }
      }

      return mHttpUtil;
   }

   private OkHttpClient getmOkHttpClient() {
      return this.mOkHttpClient;
   }

   private static SSLSocketFactory createSSLSocketFactory() {
      SSLSocketFactory ssfFactory = null;

      try {
         SSLContext sc = SSLContext.getInstance(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IRYEAw==")));
         sc.init((KeyManager[])null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());
         ssfFactory = sc.getSocketFactory();
      } catch (Exception var2) {
      }

      return ssfFactory;
   }

   private Response _getSync(String url) throws IOException {
      Request request = (new Request.Builder()).url(url).build();
      HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQcMDnkjSFo=")) + url);
      Call call = this.mOkHttpClient.newCall(request);
      Response response = call.execute();
      return response;
   }

   private Response _getSync(String url, RequestData... headers) throws IOException {
      if (headers == null) {
         headers = new RequestData[0];
      }

      Request.Builder builder = (new Request.Builder()).url(url);
      RequestData[] var4 = headers;
      int var5 = headers.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         RequestData header = var4[var6];
         if (header != null) {
            builder.addHeader(header.key, header.value);
         }
      }

      Request request = builder.build();
      Call call = this.mOkHttpClient.newCall(request);
      Response response = call.execute();
      return response;
   }

   private String _getSyncString(String url) throws IOException {
      Response response = this._getSync(url);
      return response.body().string();
   }

   private void _getAsync(String url, ResultCallback callback) {
      Request request = (new Request.Builder()).url(url).build();
      this.deliveryResult(callback, request);
   }

   private Response _postSync(String url, RequestData[] params, RequestData... headers) throws IOException {
      Request request = this.buildPostRequest(url, params, headers);
      Response response = this.mOkHttpClient.newCall(request).execute();
      return response;
   }

   private String _postSyncString(String url, RequestData[] params, RequestData... headers) throws IOException {
      Response response = this._postSync(url, params, headers);
      return response.body().string();
   }

   private void _postAsync(String url, ResultCallback callback, RequestData[] params, RequestData... headers) {
      Request request = this.buildPostRequest(url, params, headers);
      this.deliveryResult(callback, request);
   }

   private void _postAsync(String url, ResultCallback callback, Map<String, String> params, Map<String, String> headers) {
      RequestData[] paramsArr = this.mapToRequestDatas(params);
      RequestData[] headersArr = this.mapToRequestDatas(headers);
      Request request = this.buildPostRequest(url, paramsArr, headersArr);
      this.deliveryResult(callback, request);
   }

   private RequestData[] mapToRequestDatas(Map<String, String> params) {
      int index = 0;
      if (params == null) {
         return new RequestData[0];
      } else {
         int size = params.size();
         RequestData[] res = new RequestData[size];
         Set<Map.Entry<String, String>> entries = params.entrySet();

         Map.Entry entry;
         for(Iterator var6 = entries.iterator(); var6.hasNext(); res[index++] = new RequestData((String)entry.getKey(), (String)entry.getValue())) {
            entry = (Map.Entry)var6.next();
         }

         return res;
      }
   }

   private Request buildPostRequest(String url, RequestData[] params, RequestData... headers) {
      if (headers == null) {
         headers = new RequestData[0];
      }

      Headers.Builder headersBuilder = new Headers.Builder();
      RequestData[] var5 = headers;
      int var6 = headers.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         RequestData header = var5[var7];
         if (header != null) {
            headersBuilder.add(header.key, header.value);
         }
      }

      Headers requestHeaders = headersBuilder.build();
      if (params == null) {
         params = new RequestData[0];
      }

      FormBody.Builder formBodyBuilder = new FormBody.Builder();
      RequestData[] var13 = params;
      int var15 = params.length;

      for(int var9 = 0; var9 < var15; ++var9) {
         RequestData param = var13[var9];
         formBodyBuilder.add(param.key, param.value);
      }

      RequestBody requestBody = formBodyBuilder.build();
      return (new Request.Builder()).url(url).headers(requestHeaders).post(requestBody).build();
   }

   private void deliveryResult(final ResultCallback callback, Request request) {
      this.mOkHttpClient.newCall(request).enqueue(new Callback() {
         public void onFailure(Call call, IOException e) {
            OkHttpUtil.this.sendFailedStringCallback(call, e, callback);
         }

         public void onResponse(Call call, Response response) throws IOException {
            try {
               switch (response.code()) {
                  case 200:
                     byte[] bytes = response.body().bytes();
                     OkHttpUtil.this.sendSuccessResultCallback(bytes, callback);
                     break;
                  case 500:
                     OkHttpUtil.this.sendSuccessResultCallback((byte[])null, callback);
                     break;
                  default:
                     throw new IOException();
               }
            } catch (IOException var4) {
               IOException e = var4;
               OkHttpUtil.this.sendFailedStringCallback(call, e, callback);
            }

         }
      });
   }

   private void sendFailedStringCallback(final Call call, final Exception e, final ResultCallback callback) {
      this.mDelivery.post(new Runnable() {
         public void run() {
            if (callback != null) {
               callback.onError(call, e);
            }

         }
      });
   }

   private void sendSuccessResultCallback(final byte[] bytes, final ResultCallback callback) {
      this.mDelivery.post(new Runnable() {
         public void run() {
            if (callback != null) {
               callback.onResponse(bytes);
            }

         }
      });
   }

   public static OkHttpClient getmOkHttpClient2() {
      return getInstance().getmOkHttpClient();
   }

   public static Response getSync(String url) throws IOException {
      return getInstance()._getSync(url);
   }

   public static Response getSync(String url, RequestData... headers) throws IOException {
      return getInstance()._getSync(url, headers);
   }

   public static String getSyncString(String url) throws IOException {
      return getInstance()._getSyncString(url);
   }

   public static void getAsync(String url, ResultCallback callback) {
      getInstance()._getAsync(url, callback);
   }

   public static Response postSync(String url, RequestData[] params, RequestData... headers) throws IOException {
      return getInstance()._postSync(url, params, headers);
   }

   public static String postSyncString(String url, RequestData[] params, RequestData... headers) throws IOException {
      return getInstance()._postSyncString(url, params, headers);
   }

   public static void postAsync(String url, ResultCallback callback, RequestData[] params, RequestData... headers) {
      getInstance()._postAsync(url, callback, params, headers);
   }

   public static void postAsync(String url, ResultCallback callback, Map<String, String> params, Map<String, String> headers) {
      getInstance()._postAsync(url, callback, params, headers);
   }

   public static class RequestData {
      public String key;
      public String value;

      public RequestData() {
      }

      public RequestData(String key, String value) {
         this.key = key;
         this.value = value;
      }
   }

   public abstract static class ResultCallback {
      public abstract void onError(Call var1, Exception var2);

      public abstract void onResponse(byte[] var1);
   }

   public static class TrustAllHostnameVerifier implements HostnameVerifier {
      public boolean verify(String hostname, SSLSession session) {
         return true;
      }
   }

   public static class TrustAllCerts implements X509TrustManager {
      public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
      }

      public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
      }

      public X509Certificate[] getAcceptedIssuers() {
         return new X509Certificate[0];
      }
   }
}
