package com.kook.network.interceptors;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import com.kook.common.utils.HVLog;
import com.kook.network.StringFog;
import com.kook.network.secret.CipherUtil;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class DecryptionInterceptor implements Interceptor {
   private static String INTERCEPTOR = StringFog.decrypt("AgEbDl8BEBdZCRtVGB8=");
   public Context mContext;
   public String mBaseUrl;

   public DecryptionInterceptor(Context context, String baseUrl) {
      this.mContext = context;
      this.mBaseUrl = baseUrl;
   }

   public static int countPrefix(String str1, String str2) {
      int minLength = Math.min(str1.length(), str2.length());
      int count = 0;

      for(int i = 0; i < minLength && str1.charAt(i) == str2.charAt(i); ++i) {
         ++count;
      }

      return count;
   }

   public String removeQuotes(String str) {
      return str != null && str.length() > 1 && str.charAt(0) == '"' && str.charAt(str.length() - 1) == '"' ? str.substring(1, str.length() - 1) : str;
   }

   public void setPersistent(String requestUrl, String encryptContent) {
      SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(INTERCEPTOR, 0);
      SharedPreferences.Editor editor = sharedPreferences.edit();
      editor.putString(requestUrl, encryptContent);
      HVLog.d(StringFog.decrypt("GQoeHkgRATJfClM=") + requestUrl + StringFog.decrypt("S09PS0gMFhVUFh07BAEbDkMWTw==") + encryptContent);
      if (VERSION.SDK_INT >= 9) {
         editor.apply();
      }

   }

   public static String getPersistent(Context context, String requestUrl, boolean encrypt) {
      SharedPreferences sharedPreferences = context.getSharedPreferences(INTERCEPTOR, 4);
      String encryptContent = sharedPreferences.getString(requestUrl, (String)null);
      return encrypt ? CipherUtil.decrypt(StringFog.decrypt("IwcFWR1QQUkdXkdIXUJfXA=="), encryptContent) : encryptContent;
   }

   public Response intercept(Interceptor.Chain chain) throws IOException {
      Request request = chain.request();
      String requestUrl = request.url().toString();
      Response response = chain.proceed(request);
      int code = response.code();
      HVLog.d(StringFog.decrypt("g8DYjZzgkPSgg9PsUQ==") + code);
      ResponseBody responseBody;
      if (code == 200) {
         responseBody = response.body();
         BufferedSource source = responseBody.source();
         source.request(Long.MAX_VALUE);
         Buffer buffer = source.getBuffer();
         String encryptedString = buffer.clone().readString(StandardCharsets.UTF_8);
         String encryptContent = this.removeQuotes(encryptedString);
         this.setPersistent(requestUrl, encryptContent);
         String desDecrypt = CipherUtil.decrypt(StringFog.decrypt("IwcFWR1QQUkdXkdIXUJfXA=="), encryptContent);
         ResponseBody newResponseBody = ResponseBody.create(responseBody.contentType(), desDecrypt);
         return response.newBuilder().body(newResponseBody).build();
      } else {
         responseBody = response.body();
         return response.newBuilder().body(responseBody).build();
      }
   }
}
