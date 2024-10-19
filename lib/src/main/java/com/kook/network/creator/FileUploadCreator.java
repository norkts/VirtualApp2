package com.kook.network.creator;

import com.google.gson.GsonBuilder;
import com.kook.network.https.HttpsUtils;
import com.kook.network.interceptors.AuthIntercepter;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public final class FileUploadCreator {
   private static final int DEFAULT_TIMEOUT = 10;
   private static final OkHttpClient OK_HTTP_CLIENT;

   public static Retrofit getRetrofitClient(String url) {
      Retrofit retrofit = (new Retrofit.Builder()).client(OK_HTTP_CLIENT).addConverterFactory(GsonConverterFactory.create((new GsonBuilder()).create())).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).baseUrl(url).build();
      return retrofit;
   }

   static {
      OK_HTTP_CLIENT = (new OkHttpClient.Builder()).addInterceptor((new AuthIntercepter.Builder()).getTag().build()).sslSocketFactory(HttpsUtils.initSSLSocketFactory(), HttpsUtils.initTrustManager()).connectTimeout(10L, TimeUnit.SECONDS).readTimeout(10L, TimeUnit.SECONDS).writeTimeout(10L, TimeUnit.SECONDS).build();
   }
}
