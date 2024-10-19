package com.kook.network.creator;

import android.content.Context;
import com.google.gson.GsonBuilder;
import com.kook.common.utils.HVLog;
import com.kook.network.StringFog;
import com.kook.network.https.HttpsUtils;
import com.kook.network.interceptors.DecryptionInterceptor;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public final class RequestCreator {
   private static final int DEFAULT_TIMEOUT = 10;
   private static OkHttpClient OK_HTTP_CLIENT = null;
   private static Retrofit RETROFIT_CLIENT;

   public static HttpLoggingInterceptor getLoggingInterceptor() {
      HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
      loggingInterceptor.setLevel(Level.BASIC);
      return loggingInterceptor;
   }

   public static Retrofit getRetrofitClient(Context context, String baseUrl) {
      if (baseUrl != null && !baseUrl.endsWith("/")) {
         baseUrl = baseUrl + "/";
      }

      if (RETROFIT_CLIENT != null) {
         HVLog.d("getRetrofitClient baseUrl:" + baseUrl + "   RETROFIT_CLIENT.baseUrl:" + RETROFIT_CLIENT.baseUrl());
      } else {
         HVLog.d("getRetrofitClient baseUrl:" + baseUrl);
      }

      if (OK_HTTP_CLIENT == null) {
         OK_HTTP_CLIENT = (new OkHttpClient.Builder()).sslSocketFactory(HttpsUtils.initSSLSocketFactory(), HttpsUtils.initTrustManager()).addInterceptor(new DecryptionInterceptor(context, baseUrl)).connectTimeout(10L, TimeUnit.SECONDS).readTimeout(10L, TimeUnit.SECONDS).writeTimeout(10L, TimeUnit.SECONDS).build();
      }

      if (RETROFIT_CLIENT == null || !RETROFIT_CLIENT.baseUrl().toString().equals(baseUrl)) {
         RETROFIT_CLIENT = (new Retrofit.Builder()).client(OK_HTTP_CLIENT).addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create((new GsonBuilder()).create())).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).baseUrl(baseUrl).build();
      }

      return RETROFIT_CLIENT;
   }
}
