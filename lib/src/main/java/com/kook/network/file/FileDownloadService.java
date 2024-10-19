package com.kook.network.file;

import com.google.gson.GsonBuilder;
import com.kook.common.utils.HVLog;
import com.kook.network.StringFog;
import com.kook.network.creator.IApiService;
import com.kook.network.exception.ErrorAction;
import com.kook.network.interceptors.DownloadInterceptor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class FileDownloadService {
   private static FileDownloadService mInstance = null;
   private static final int DEFAULT_TIMEOUT = 5;
   private final IApiService mApiService;
   private final IDownloadListener mListener;

   private FileDownloadService(IDownloadListener listener) {
      this.mListener = listener;
      OkHttpClient OkhttpClient = (new OkHttpClient.Builder()).addInterceptor(new DownloadInterceptor(listener)).connectTimeout(5L, TimeUnit.SECONDS).readTimeout(5L, TimeUnit.SECONDS).writeTimeout(5L, TimeUnit.SECONDS).build();
      Retrofit retrofitClient = (new Retrofit.Builder()).client(OkhttpClient).addConverterFactory(GsonConverterFactory.create((new GsonBuilder()).create())).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).baseUrl(StringFog.decrypt("AxsbGxdNWhBaEUcTBAAECEIQEEleDx0dUVdfWBVNEQZZBxodGRkKGQI=")).build();
      this.mApiService = (IApiService)retrofitClient.create(IApiService.class);
   }

   public static FileDownloadService getInstance(IDownloadListener listener) {
      if (null == mInstance) {
         Class var1 = FileDownloadService.class;
         synchronized(FileDownloadService.class) {
            if (null == mInstance) {
               mInstance = new FileDownloadService(listener);
            }
         }
      }

      return mInstance;
   }

   public void download(String url, String filePath) {
      this.mApiService.download(url).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).map((responseBody) -> {
         return responseBody.byteStream();
      }).doOnError(new ErrorAction()).doOnNext((inputStream) -> {
         this.saveFile(inputStream, filePath);
      }).observeOn(AndroidSchedulers.mainThread()).subscribe((response) -> {
         if (this.mListener != null) {
            this.mListener.onDownloadSuccess();
            HVLog.d(StringFog.decrypt("jef/jqf9kMOpgfn+"));
         }

      }, (throwable) -> {
         HVLog.d(StringFog.decrypt("S4b78sXN2oKJ4o7o7Q=="));
      });
   }

   public void download(String url, Map<String, String> headers, String filePath) {
      this.mApiService.download(url, headers).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).map((responseBody) -> {
         return responseBody.byteStream();
      }).doOnError(new ErrorAction()).doOnNext((inputStream) -> {
         this.saveFile(inputStream, filePath);
      }).observeOn(AndroidSchedulers.mainThread()).subscribe((response) -> {
         if (this.mListener != null) {
            this.mListener.onDownloadSuccess();
            HVLog.d(StringFog.decrypt("jef/jqf9kMOpgfn+"));
         }

      }, (throwable) -> {
         HVLog.d(StringFog.decrypt("S4b78sXN2oKJ4o7o7Q=="));
      });
   }

   private void saveFile(InputStream inputString, String filePath) {
      File file = new File(filePath);
      if (file.exists()) {
         file.delete();
      }

      FileOutputStream fos = null;

      try {
         file.createNewFile();
         fos = new FileOutputStream(file);
         byte[] b = new byte[10240];

         int len;
         while((len = inputString.read(b)) != -1) {
            fos.write(b, 0, len);
         }

         inputString.close();
         fos.close();
         this.mListener.onDownloadSuccess();
      } catch (Exception var7) {
         Exception exception = var7;
         HVLog.e(StringFog.decrypt("jtPtjpXakdiMgOjXUQ==") + filePath);
         this.mListener.onDownloadFail(exception);
         if (file.exists()) {
            file.deleteOnExit();
         }
      }

   }
}
