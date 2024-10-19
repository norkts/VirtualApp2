package com.carlos.common.clouddisk.http;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Build.VERSION;
import com.carlos.common.App;
import com.carlos.common.clouddisk.listview.FileItem;
import com.carlos.common.ui.utils.LanzouHelper;
import com.carlos.common.utils.FileTools;
import com.carlos.common.utils.ResponseProgram;
import com.carlos.libcommon.StringFog;
import com.kook.common.utils.HVLog;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jdeferred.Promise;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class HttpWorker {
   private static HttpWorker mHttpWorker;
   private static final MediaType FROM_DATA = MediaType.parse("multipart/form-data");
   private OkHttpClient.Builder mOkHttpClientBuilder;
   private OkHttpClient mOkHttpClient;
   MyCookieJar cookieJar;

   private HttpWorker() {
   }

   public static HttpWorker getInstance() {
      if (mHttpWorker == null) {
         Class var0 = OkHttpUtil.class;
         synchronized(OkHttpUtil.class) {
            if (mHttpWorker == null) {
               mHttpWorker = new HttpWorker();
            }
         }
      }

      return mHttpWorker;
   }

   private boolean LoginSync(String username, String password) throws Exception {
      OkHttpUtil.RequestData[] rs = new OkHttpUtil.RequestData[]{new OkHttpUtil.RequestData("formhash", "5dc76a08"), new OkHttpUtil.RequestData("uid", username), new OkHttpUtil.RequestData("pwd", password), new OkHttpUtil.RequestData("task", "3")};
      String info = OkHttpUtil.postSyncString("https://pc.woozooo.com/mlogin.php", rs);
      String url = "";
      JSONObject jsonObject = (new JSONArray("[" + info + "]")).getJSONObject(0);
      url = jsonObject.getString("info");
      if (url.equals("成功登录")) {
         return true;
      } else if (url.equals("登陆失败")) {
         MyCookieJar.resetCookies();
         return false;
      } else {
         MyCookieJar.resetCookies();
         return false;
      }
   }

   public List<FileItem> getFolderInfoSync(String folder_id) throws Exception {
      List<FileItem> list = new ArrayList();
      list.clear();
      OkHttpUtil.RequestData[] rs = new OkHttpUtil.RequestData[]{new OkHttpUtil.RequestData("task", "47"), new OkHttpUtil.RequestData("folder_id", folder_id)};
      String response = OkHttpUtil.postSyncString("https://pc.woozooo.com/doupload.php", rs);
      JSONObject jsonObject = (new JSONArray("[" + response + "]")).getJSONObject(0);
      String folderInfo = jsonObject.getString("text");
      HVLog.i("folderInfo:" + folderInfo);
      JSONArray folderJsonArray = new JSONArray(folderInfo);

      for(int i = 0; i < folderJsonArray.length(); ++i) {
         JSONObject folderObject = folderJsonArray.getJSONObject(i);
         String name = folderObject.getString("name");
         String fol_id = folderObject.getString("fol_id");
         list.add(new FileItem(name, 1, fol_id));
      }

      return list;
   }

   public List<FileItem> getFileInfoSync(String folder_id) throws Exception {
      List<FileItem> list = new ArrayList();
      list.clear();

      for(int page = 1; page < 200; ++page) {
         OkHttpUtil.RequestData[] rs = new OkHttpUtil.RequestData[]{new OkHttpUtil.RequestData("task", "5"), new OkHttpUtil.RequestData("folder_id", folder_id), new OkHttpUtil.RequestData("pg", String.valueOf(page))};
         String data2 = OkHttpUtil.postSyncString("https://pc.woozooo.com/doupload.php", rs);
         JSONObject jsonObject = (new JSONArray("[" + data2 + "]")).getJSONObject(0);
         String text = jsonObject.getString("text");
         if (text.length() == 2) {
            break;
         }

         JSONArray jsonArray = new JSONArray(text);

         for(int i = 0; i < jsonArray.length(); ++i) {
            JSONObject fileObject = jsonArray.getJSONObject(i);
            String name = fileObject.getString("name_all");
            String file_id = fileObject.getString("id");
            String fileUrl = this.getFileHrefSync(file_id);
            FileItem item = new FileItem(name, 0, file_id);
            item.setFileUrl(fileUrl);
            item.setTime(fileObject.getString("time"));
            item.setSizes(fileObject.getString("size"));
            item.setDowns(fileObject.getString("downs"));
            list.add(item);
         }
      }

      return list;
   }

   public boolean setNewFolderSync(String uri, String folder_name) throws Exception {
      OkHttpUtil.RequestData[] rs = new OkHttpUtil.RequestData[3];
      String[] folder_ids = uri.split("=");
      String parent_id = folder_ids[folder_ids.length - 1];
      rs[0] = new OkHttpUtil.RequestData("task", "2");
      rs[1] = new OkHttpUtil.RequestData("parent_id", parent_id);
      rs[2] = new OkHttpUtil.RequestData("folder_name", folder_name);
      String data = OkHttpUtil.postSyncString("https://pc.woozooo.com/doupload.php", rs);
      String info = "";
      JSONArray jsonArray = new JSONArray("[" + data + "]");
      JSONObject jsonObject = jsonArray.getJSONObject(0);
      info = jsonObject.getString("info");
      return info.equals("创建成功");
   }

   public boolean uploadFileSync(String file_uri, String folder_id, final UpLoadCallbackListener listener) throws Exception {
      OkHttpClient client = OkHttpUtil.getmOkHttpClient2();
      File file = new File(file_uri);
      String[] str = file_uri.split("/");
      String name = str[str.length - 1];
      RequestBody fileBody = new FileProgressRequestBody("application/x-www-form-urlencoded;charset=utf-8", file, new FileProgressRequestBody.ProgressListener() {
         public void transferred(double size) {
            listener.Progress(size);
         }
      });
      MultipartBody mBody = (new MultipartBody.Builder()).setType(MultipartBody.FORM).addFormDataPart("size", String.valueOf(file.length())).addFormDataPart("task", "1").addFormDataPart("folder_id", folder_id).addFormDataPart("name", name).addFormDataPart("upload_file", name, fileBody).build();
      Request request = (new Request.Builder()).url("https://pc.woozooo.com/fileup.php").post(mBody).build();
      Response response = client.newCall(request).execute();
      String data = response.body().string();
      return data.indexOf("\\u4e0a\\u4f20\\u6210\\u529f") != -1;
   }

   private boolean deleteFolderSync(String holder_id) throws Exception {
      OkHttpUtil.RequestData[] rs = new OkHttpUtil.RequestData[]{new OkHttpUtil.RequestData("task", "3"), new OkHttpUtil.RequestData("folder_id", holder_id)};
      String data = OkHttpUtil.postSyncString("https://pc.woozooo.com/doupload.php", rs);
      String info = "";
      JSONArray jsonArray = new JSONArray("[" + data + "]");
      JSONObject jsonObject = jsonArray.getJSONObject(0);
      info = jsonObject.getString("info");
      return info.equals("删除成功");
   }

   private boolean deleteFileSync(String file_id) throws Exception {
      OkHttpUtil.RequestData[] rs = new OkHttpUtil.RequestData[]{new OkHttpUtil.RequestData("task", "6"), new OkHttpUtil.RequestData("file_id", file_id)};
      String data = OkHttpUtil.postSyncString("https://pc.woozooo.com/doupload.php", rs);
      String info = "";
      JSONArray jsonArray = new JSONArray("[" + data + "]");
      JSONObject jsonObject = jsonArray.getJSONObject(0);
      info = jsonObject.getString("info");
      return info.equals("已删除");
   }

   public long downFileSync(String fileName, String fileId) throws Exception {
      String fileHref = this.getFileHrefSync(fileId);
      HVLog.d("下载文件   fileHref：" + fileHref);
      LanzouHelper.Lanzou lanZouRealLink = LanzouHelper.getLanZouRealLink(fileHref);
      return this.downLoadDatabase(lanZouRealLink.getDlLink(), fileName);
   }

   public static byte[] read(InputStream inStream) throws Exception {
      ByteArrayOutputStream outStream = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      int len;
      while((len = inStream.read(buffer)) != -1) {
         outStream.write(buffer, 0, len);
      }

      inStream.close();
      return outStream.toByteArray();
   }

   private void testkook(String url) throws IOException {
      OkHttpUtil.RequestData[] rs = new OkHttpUtil.RequestData[]{new OkHttpUtil.RequestData("action", "downprocess"), new OkHttpUtil.RequestData("ves", "1")};
      OkHttpUtil.RequestData[] header = new OkHttpUtil.RequestData[]{new OkHttpUtil.RequestData("Accept", "application/json, text/javascript, */*"), new OkHttpUtil.RequestData("Accept-Encoding", "gzip, deflate, br"), new OkHttpUtil.RequestData("Accept-Language", "zh-CN,zh;q=0.9"), new OkHttpUtil.RequestData("Connection", "keep-alive"), new OkHttpUtil.RequestData("Content-Length", "112"), new OkHttpUtil.RequestData("Content-Type", "application/x-www-form-urlencoded"), new OkHttpUtil.RequestData("Host", "lanzous.com"), null, null, new OkHttpUtil.RequestData("Sec-Fetch-Mode", "cors"), new OkHttpUtil.RequestData("Sec-Fetch-Site", "same-origin"), new OkHttpUtil.RequestData("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36"), new OkHttpUtil.RequestData("X-Requested-With", "XMLHttpRequest")};
      String data = OkHttpUtil.postSyncString(url, rs, header);
      FileTools.saveAsFileWriter(this.getSavePath() + "html.txt", data);
      HVLog.d("data:" + data);
   }

   private String GetDownUri(String fileSecondHref) throws Exception {
      OkHttpUtil.RequestData[] rs = new OkHttpUtil.RequestData[]{new OkHttpUtil.RequestData("action", "downprocess"), new OkHttpUtil.RequestData("ves", "1")};
      OkHttpUtil.RequestData[] header = new OkHttpUtil.RequestData[]{new OkHttpUtil.RequestData("Accept", "application/json, text/javascript, */*"), new OkHttpUtil.RequestData("Accept-Encoding", "gzip, deflate, br"), new OkHttpUtil.RequestData("Accept-Language", "zh-CN,zh;q=0.9"), new OkHttpUtil.RequestData("Connection", "keep-alive"), new OkHttpUtil.RequestData("Content-Length", "112"), new OkHttpUtil.RequestData("Content-Type", "application/x-www-form-urlencoded"), new OkHttpUtil.RequestData("Host", "lanzous.com"), new OkHttpUtil.RequestData("Origin", "https://wws.lanzouj.com"), new OkHttpUtil.RequestData("Referer", fileSecondHref), new OkHttpUtil.RequestData("Sec-Fetch-Mode", "cors"), new OkHttpUtil.RequestData("Sec-Fetch-Site", "same-origin"), new OkHttpUtil.RequestData("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36"), new OkHttpUtil.RequestData("X-Requested-With", "XMLHttpRequest")};
      String data = OkHttpUtil.postSyncString("https://wws.lanzouj.com/", rs, header);
      FileTools.saveAsFileWriter(this.getSavePath() + "html.txt", data);
      JSONObject jsonObject = (new JSONArray("[" + data + "]")).getJSONObject(0);
      return jsonObject.getString("url");
   }

   public String getSavePath() {
      String path;
      if (VERSION.SDK_INT > 29) {
         path = App.getApp().getExternalFilesDir((String)null).getAbsolutePath() + "/";
      } else {
         path = Environment.getExternalStorageDirectory().getPath() + "/";
      }

      return path;
   }

   private Response getSync(String url) throws IOException {
      if (this.mOkHttpClient == null) {
         this.cookieJar = new MyCookieJar();
         this.mOkHttpClientBuilder = new OkHttpClient.Builder();
         this.mOkHttpClientBuilder.cookieJar(this.cookieJar);
         this.mOkHttpClientBuilder.sslSocketFactory(createSSLSocketFactory(), new OkHttpUtil.TrustAllCerts());
         this.mOkHttpClientBuilder.hostnameVerifier(new OkHttpUtil.TrustAllHostnameVerifier());
         this.mOkHttpClient = this.mOkHttpClientBuilder.build();
      }

      Request.Builder builder = (new Request.Builder()).url(url);
      String HONEYCOMB_USERAGENT = "Mozilla/5.0 (Linux; U; Android 3.1; en-us; Xoom Build/HMJ25) AppleWebKit/534.13 (KHTML, like Gecko) Version/4.0 Safari/534.13";
      builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
      builder.addHeader("User-Agent", HONEYCOMB_USERAGENT);
      Request request = builder.get().build();
      Call call = this.mOkHttpClient.newCall(request);
      Response response = call.execute();
      return response;
   }

   private static SSLSocketFactory createSSLSocketFactory() {
      SSLSocketFactory ssfFactory = null;

      try {
         SSLContext sc = SSLContext.getInstance("TLS");
         sc.init((KeyManager[])null, new TrustManager[]{new OkHttpUtil.TrustAllCerts()}, new SecureRandom());
         ssfFactory = sc.getSocketFactory();
      } catch (Exception var2) {
      }

      return ssfFactory;
   }

   public String getFileHrefSync(String file_id) throws Exception {
      OkHttpUtil.RequestData[] rs = new OkHttpUtil.RequestData[]{new OkHttpUtil.RequestData("task", "22"), new OkHttpUtil.RequestData("file_id", file_id)};
      String uri = "";
      String response = OkHttpUtil.postSyncString("https://pc.woozooo.com/doupload.php", rs);
      JSONObject jsonObject = (new JSONArray("[" + response + "]")).getJSONObject(0);
      String info = jsonObject.getString("info");
      JSONObject jsonObject2 = (new JSONArray("[" + info + "]")).getJSONObject(0);
      String f_id = jsonObject2.getString("f_id");
      uri = "https://wws.lanzouj.com/" + f_id;
      return uri;
   }

   private String getFileSecondHref(String file_href) throws Exception {
      String data = OkHttpUtil.getSyncString(file_href);
      if (data.contains("来晚啦...文件取消分享了")) {
         throw new IOException();
      } else {
         Document document = Jsoup.parse(data);
         Elements element = document.getElementsByClass("ifr2");
         return element.attr("src");
      }
   }

   private String GetDownKey(String fileSecondHref) throws Exception {
      String uri = "https://wws.lanzouj.com" + fileSecondHref;
      String data = OkHttpUtil.getSyncString(uri);
      Document document = Jsoup.parse(data);
      String str = document.getElementsByTag("script").toString().trim();
      int a = str.indexOf("ajaxdata = \'");
      int b = str.indexOf("c_c");
      return str.substring(a + "ajaxdata = \'".length(), b + 3);
   }

   private String GetDownUri(String fileSecondHref, String sign) throws Exception {
      OkHttpUtil.RequestData[] rs = new OkHttpUtil.RequestData[]{new OkHttpUtil.RequestData("action", "downprocess"), new OkHttpUtil.RequestData("sign", sign), new OkHttpUtil.RequestData("ves", "1")};
      OkHttpUtil.RequestData[] header = new OkHttpUtil.RequestData[]{new OkHttpUtil.RequestData("Accept", "application/json, text/javascript, */*"), new OkHttpUtil.RequestData("Accept-Encoding", "gzip, deflate, br"), new OkHttpUtil.RequestData("Accept-Language", "zh-CN,zh;q=0.9"), new OkHttpUtil.RequestData("Connection", "keep-alive"), new OkHttpUtil.RequestData("Content-Length", "112"), new OkHttpUtil.RequestData("Content-Type", "application/x-www-form-urlencoded"), new OkHttpUtil.RequestData("Host", "lanzous.com"), new OkHttpUtil.RequestData("Origin", "https://www.lanzous.com"), new OkHttpUtil.RequestData("Referer", fileSecondHref), new OkHttpUtil.RequestData("Sec-Fetch-Mode", "cors"), new OkHttpUtil.RequestData("Sec-Fetch-Site", "same-origin"), new OkHttpUtil.RequestData("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36"), new OkHttpUtil.RequestData("X-Requested-With", "XMLHttpRequest")};
      return "";
   }

   public String GetDownSecondUri(String downUri) throws Exception {
      String address = "https://vip.d0.baidupan.com/file/" + downUri;
      String path = "/file/" + downUri;
      OkHttpUtil.RequestData[] header = new OkHttpUtil.RequestData[]{new OkHttpUtil.RequestData("authority", "vip.d0.baidupan.com"), new OkHttpUtil.RequestData("method", "GET"), new OkHttpUtil.RequestData("path", path), new OkHttpUtil.RequestData("scheme", "https"), new OkHttpUtil.RequestData("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9"), new OkHttpUtil.RequestData("accept-encoding", "gzip, deflate, br"), new OkHttpUtil.RequestData("accept-language", "zh-CN,zh;q=0.9"), new OkHttpUtil.RequestData("sec-fetch-mode", "navigate"), new OkHttpUtil.RequestData("sec-fetch-site", "none"), new OkHttpUtil.RequestData("upgrade-insecure-requests", "1"), new OkHttpUtil.RequestData("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36")};
      return OkHttpUtil.getSync(address, header).request().url().toString();
   }

   private long downLoadDatabase(String downSecondUri, String fileName) {
      String[] filename = downSecondUri.split("=");
      DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downSecondUri));
      DownloadManager downloadManager = (DownloadManager)App.getApp().getSystemService("download");
      HVLog.d("正式下载");
      return downloadManager.enqueue(request);
   }

   public static Promise<Boolean, Throwable, Void> Login(String username, String password, loginCallbackListener listener) {
      return ResponseProgram.defer().when(() -> {
         try {
            boolean cookiesActivation = MyCookieJar.isCookiesActivation();
            HVLog.i("cookiesActivation:" + cookiesActivation);
            return cookiesActivation ? true : getInstance().LoginSync(username, password);
         } catch (Exception var3) {
            Exception e = var3;
            HVLog.printException(e);
            return false;
         }
      }).done((success) -> {
         if (success) {
            listener.onFinish();
         } else {
            listener.onError(new Exception());
         }

      }).fail((throwable) -> {
         listener.onError(throwable);
      });
   }

   public static Promise<List<FileItem>, Throwable, Void> UpdatePage(String folder_id, PageUpdatePageCallbackListener listener) {
      return ResponseProgram.defer().when(() -> {
         try {
            return getInstance().getFolderInfoSync(folder_id);
         } catch (Exception var2) {
            Exception e = var2;
            HVLog.printException(e);
            return null;
         }
      }).done((fileItemList) -> {
         if (fileItemList != null) {
            listener.onFinish(fileItemList);
         } else {
            listener.onError(new Exception());
         }

      }).fail((throwable) -> {
         listener.onError(throwable);
      });
   }

   public static void AddFolder(final String uri, final String folder_name, final CRUDCallbackListener listener) {
      (new Thread(new Runnable() {
         public void run() {
            try {
               final Boolean aBoolean = HttpWorker.getInstance().setNewFolderSync(uri, folder_name);
               (new Handler(Looper.getMainLooper())).post(new Runnable() {
                  public void run() {
                     if (aBoolean) {
                        listener.onFinish();
                     } else {
                        listener.onError(new Exception());
                     }

                  }
               });
            } catch (Exception var2) {
               final Exception e = var2;
               (new Handler(Looper.getMainLooper())).post(new Runnable() {
                  public void run() {
                     listener.onError(e);
                  }
               });
            }

         }
      })).start();
   }

   public static void FileDown(final String fileName, final String fileId) {
      (new Thread(new Runnable() {
         public void run() {
            try {
               HttpWorker.getInstance().downFileSync(fileName, fileId);
            } catch (Exception var2) {
               Exception e = var2;
               e.printStackTrace();
            }

         }
      })).start();
   }

   public static void DeleteFile(final String file_id, final CRUDCallbackListener listener) {
      (new Thread(new Runnable() {
         public void run() {
            try {
               final Boolean aBoolean = HttpWorker.getInstance().deleteFileSync(file_id);
               (new Handler(Looper.getMainLooper())).post(new Runnable() {
                  public void run() {
                     if (aBoolean) {
                        listener.onFinish();
                     } else {
                        listener.onError(new Exception());
                     }

                  }
               });
            } catch (Exception var2) {
               final Exception e = var2;
               (new Handler(Looper.getMainLooper())).post(new Runnable() {
                  public void run() {
                     listener.onError(e);
                  }
               });
            }

         }
      })).start();
   }

   public static void DeleteFolder(final String folder_id, final CRUDCallbackListener listener) {
      (new Thread(new Runnable() {
         public void run() {
            try {
               final Boolean aBoolean = HttpWorker.getInstance().deleteFolderSync(folder_id);
               (new Handler(Looper.getMainLooper())).post(new Runnable() {
                  public void run() {
                     if (aBoolean) {
                        listener.onFinish();
                     } else {
                        listener.onError(new Exception());
                     }

                  }
               });
            } catch (Exception var2) {
               final Exception e = var2;
               (new Handler(Looper.getMainLooper())).post(new Runnable() {
                  public void run() {
                     listener.onError(e);
                  }
               });
            }

         }
      })).start();
   }

   public static void sendFile(final String file_uri, final String folder_id, final UpLoadCallbackListener listener) {
      (new Thread(new Runnable() {
         public void run() {
            try {
               final boolean aBoolean = HttpWorker.getInstance().uploadFileSync(file_uri, folder_id, listener);
               (new Handler(Looper.getMainLooper())).post(new Runnable() {
                  public void run() {
                     if (aBoolean) {
                        listener.onFinish(0);
                     } else {
                        listener.onError(0);
                     }

                  }
               });
            } catch (Exception var2) {
               final Exception e = var2;
               (new Handler(Looper.getMainLooper())).post(new Runnable() {
                  public void run() {
                     listener.onError(0);
                     e.printStackTrace();
                  }
               });
            }

         }
      })).start();
   }

   public static void sendFiles(final List<String> file_uris, final String folder_id, final UpLoadCallbackListener listener) {
      (new Thread(new Runnable() {
         public void run() {
            for(int i = 0; i < file_uris.size(); ++i) {
               final int finalI1;
               try {
                  final boolean aBoolean = HttpWorker.getInstance().uploadFileSync((String)file_uris.get(i), folder_id, listener);
                  finalI1 = i;
                  (new Handler(Looper.getMainLooper())).post(new Runnable() {
                     public void run() {
                        if (aBoolean) {
                           listener.onFinish(finalI1);
                        } else {
                           listener.onError(finalI1);
                        }

                     }
                  });
               } catch (Exception var4) {
                  final Exception e = var4;
                  final int finalI2 = i;
                  (new Handler(Looper.getMainLooper())).post(new Runnable() {
                     public void run() {
                        listener.onError(finalI2);
                        e.printStackTrace();
                     }
                  });
               }
            }

         }
      })).start();
   }

   public interface UpLoadCallbackListener {
      void onError(int var1);

      void Progress(double var1);

      void onFinish(int var1);
   }

   public interface CRUDCallbackListener {
      void onError(Throwable var1);

      void onFinish();
   }

   public interface PageUpdatePageCallbackListener {
      void onError(Throwable var1);

      void onFinish(List<FileItem> var1);
   }

   public interface loginCallbackListener {
      void onError(Throwable var1);

      void onFinish();
   }
}
