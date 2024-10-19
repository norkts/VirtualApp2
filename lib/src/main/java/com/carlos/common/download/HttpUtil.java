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
      Request request = (new Request.Builder()).header(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ijw+U2AxNFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj0YLGgaLzM=")) + startIndex + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("MwhSVg==")) + endIndex).addHeader(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jgg2OWgaIAZODDA2Ly1fPmwjMC0=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgqM2ogMC9mEQZF"))).url(url).build();
      this.doAsync(request, callback);
   }

   public void getContentLength(String url, Callback callback) throws IOException {
      Headers headers = builder.set(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uPmgaFithN1RF")), url).build();
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
      builder.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jgg2OWgaIAY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRguIGwJGiBmHl0oOhciKmozOC9oJzg/IxgAKk4jBitqHlEbOgcML2VSHjNvDjw7JQcuKGoaBgFvVigvIwgDIW9THQJOMxkoKQdXOWkFBSVvJygpKQQEI2AKPCJuClkqLD4qIXVSTAN1IF0eMgQhJXsVSFo=")));
      builder.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jgg2OWgaIAZODDA2Ly1fPmwjMC0=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS1XCW8JGShiHjA+KhciLmkjSFo=")));
      builder.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IQc6PW8jJCxiCl0JKj4qPW4KGgRrDQ4fLhc+CWIFND9lJ1RF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OghSVg==")));
      builder.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWgaIAZODlE7Kj06LW4jEis=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KD5eDWMxASRnNB0hIwRWKn9TLyRuN1geIgUlPWEJQDN8MxkbLy4pL2cILy1vVwEdCDo9O24KDStqICMdMzkhDmUjJzFhDVwsOjoMVg==")));
      builder.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IQc2M28nEhFiJDA2LBhSVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OwgAImUVHiR9ChEvOjolOnwxPDdoJx4bKggAD2NSHSNnDh49Ly4hJH0FJDV7DwZBDRYbL3UJPBF6IAYAOSoXOGMaIAJgHjBIKAcuXGwgASV/CjM+PCk1MktTBghnHzBBITohJGUVAj1oVjwIJAcuImwkATZmAQobIy4IM3o0DTBOMyc2PF85LXg3MwF8Vw0rIT4+In0FMCx8Iw08MwQpD38zSFo=")));
   }
}
