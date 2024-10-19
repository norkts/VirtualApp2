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
   private static final MediaType FROM_DATA = MediaType.parse(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwcuDmwFAgJ9ASwgOi0+DWoVPyNrETg/LRhSVg==")));
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
      OkHttpUtil.RequestData[] rs = new OkHttpUtil.RequestData[]{new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4AKmoVRTdhJBpF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OQgqOXw0ODdPVhpF"))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQgYPA==")), username), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhcmPA==")), password), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRg+KWUzSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Oi5SVg==")))};
      String info = OkHttpUtil.postSyncString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBcqLG8KLzJOIB4sLykYI28FNDJlJwYcPC42KWAOAihsHlkgKi4pKmwVRSQ=")), rs);
      String url = "";
      JSONObject jsonObject = (new JSONArray(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IC5SVg==")) + info + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JwhSVg==")))).getJSONObject(0);
      url = jsonObject.getString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcPmozSFo=")));
      if (url.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BwpcAkZJWh9YKRshAhxABw==")))) {
         return true;
      } else if (url.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Bz8VIUNNDwhYEg8rA1cNPQ==")))) {
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
      OkHttpUtil.RequestData[] rs = new OkHttpUtil.RequestData[]{new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRg+KWUzSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OV4mVg=="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4ADmgFNARsJAYw")), folder_id)};
      String response = OkHttpUtil.postSyncString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBcqLG8KLzJOIB4sLykYI28FNDJlJwYcPC42KWAOAi9sJwo5KT4uO2tSBiRlDjxF")), rs);
      JSONObject jsonObject = (new JSONArray(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IC5SVg==")) + response + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JwhSVg==")))).getJSONObject(0);
      String folderInfo = jsonObject.getString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRguIGwFSFo=")));
      HVLog.i(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4ADmgFNARrDlk+KioIVg==")) + folderInfo);
      JSONArray folderJsonArray = new JSONArray(folderInfo);

      for(int i = 0; i < folderJsonArray.length(); ++i) {
         JSONObject folderObject = folderJsonArray.getJSONObject(i);
         String name = folderObject.getString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4+DWgVSFo=")));
         String fol_id = folderObject.getString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4ADmYzAiw=")));
         list.add(new FileItem(name, 1, fol_id));
      }

      return list;
   }

   public List<FileItem> getFileInfoSync(String folder_id) throws Exception {
      List<FileItem> list = new ArrayList();
      list.clear();

      for(int page = 1; page < 200; ++page) {
         OkHttpUtil.RequestData[] rs = new OkHttpUtil.RequestData[]{new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRg+KWUzSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OQhSVg=="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4ADmgFNARsJAYw")), folder_id), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhgmVg==")), String.valueOf(page))};
         String data2 = OkHttpUtil.postSyncString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBcqLG8KLzJOIB4sLykYI28FNDJlJwYcPC42KWAOAi9sJwo5KT4uO2tSBiRlDjxF")), rs);
         JSONObject jsonObject = (new JSONArray(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IC5SVg==")) + data2 + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JwhSVg==")))).getJSONObject(0);
         String text = jsonObject.getString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRguIGwFSFo=")));
         if (text.length() == 2) {
            break;
         }

         JSONArray jsonArray = new JSONArray(text);

         for(int i = 0; i < jsonArray.length(); ++i) {
            JSONObject fileObject = jsonArray.getJSONObject(i);
            String name = fileObject.getString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4+DWgYGjdgHlFF")));
            String file_id = fileObject.getString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgqVg==")));
            String fileUrl = this.getFileHrefSync(file_id);
            FileItem item = new FileItem(name, 0, file_id);
            item.setFileUrl(fileUrl);
            item.setTime(fileObject.getString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRgYDWgVSFo="))));
            item.setSizes(fileObject.getString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4YImgVSFo="))));
            item.setDowns(fileObject.getString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRgALWogLFo="))));
            list.add(item);
         }
      }

      return list;
   }

   public boolean setNewFolderSync(String uri, String folder_name) throws Exception {
      OkHttpUtil.RequestData[] rs = new OkHttpUtil.RequestData[3];
      String[] folder_ids = uri.split(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PwhSVg==")));
      String parent_id = folder_ids[folder_ids.length - 1];
      rs[0] = new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRg+KWUzSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Oj5SVg==")));
      rs[1] = new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Khg+KmgVBgZsJAYw")), parent_id);
      rs[2] = new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4ADmgFNARsJFk7KgcMVg==")), folder_name);
      String data = OkHttpUtil.postSyncString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBcqLG8KLzJOIB4sLykYI28FNDJlJwYcPC42KWAOAi9sJwo5KT4uO2tSBiRlDjxF")), rs);
      String info = "";
      JSONArray jsonArray = new JSONArray(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IC5SVg==")) + data + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JwhSVg==")));
      JSONObject jsonObject = jsonArray.getJSONObject(0);
      info = jsonObject.getString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcPmozSFo=")));
      return info.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxpcG0ZbQjJYAB8CAhkFHQ==")));
   }

   public boolean uploadFileSync(String file_uri, String folder_id, final UpLoadCallbackListener listener) throws Exception {
      OkHttpClient client = OkHttpUtil.getmOkHttpClient2();
      File file = new File(file_uri);
      String[] str = file_uri.split(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")));
      String name = str[str.length - 1];
      RequestBody fileBody = new FileProgressRequestBody(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgc6KGoFAil9AQozKi0XDWg3PD1vJCMeLi4ACGAOQTBlNF0uKRgYKWsVNDB5NzA/OwgqOm4FLyllJBoxMwNfVg==")), file, new FileProgressRequestBody.ProgressListener() {
         public void transferred(double size) {
            listener.Progress(size);
         }
      });
      MultipartBody mBody = (new MultipartBody.Builder()).setType(MultipartBody.FORM).addFormDataPart(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4YImgVSFo=")), String.valueOf(file.length())).addFormDataPart(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRg+KWUzSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OghSVg=="))).addFormDataPart(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4ADmgFNARsJAYw")), folder_id).addFormDataPart(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4+DWgVSFo=")), name).addFormDataPart(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc6DmozJCxsJDwzKhcMVg==")), name, fileBody).build();
      Request request = (new Request.Builder()).url(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBcqLG8KLzJOIB4sLykYI28FNDJlJwYcPC42KWAOAiFvDl0uIy1WKmwVRSQ="))).post(mBody).build();
      Response response = client.newCall(request).execute();
      String data = response.body().string();
      return data.indexOf(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JxctLGhTIDdsETMgKDotKmMwBT58MDs7IActCU83GiE="))) != -1;
   }

   private boolean deleteFolderSync(String holder_id) throws Exception {
      OkHttpUtil.RequestData[] rs = new OkHttpUtil.RequestData[]{new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRg+KWUzSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Oi5SVg=="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4ADmgFNARsJAYw")), holder_id)};
      String data = OkHttpUtil.postSyncString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBcqLG8KLzJOIB4sLykYI28FNDJlJwYcPC42KWAOAi9sJwo5KT4uO2tSBiRlDjxF")), rs);
      String info = "";
      JSONArray jsonArray = new JSONArray(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IC5SVg==")) + data + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JwhSVg==")));
      JSONObject jsonObject = jsonArray.getJSONObject(0);
      info = jsonObject.getString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcPmozSFo=")));
      return info.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxpcOENNDyxYAB8CAhkFHQ==")));
   }

   private boolean deleteFileSync(String file_id) throws Exception {
      OkHttpUtil.RequestData[] rs = new OkHttpUtil.RequestData[]{new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRg+KWUzSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OT5SVg=="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4YDmgYGi9iEVRF")), file_id)};
      String data = OkHttpUtil.postSyncString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBcqLG8KLzJOIB4sLykYI28FNDJlJwYcPC42KWAOAi9sJwo5KT4uO2tSBiRlDjxF")), rs);
      String info = "";
      JSONArray jsonArray = new JSONArray(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IC5SVg==")) + data + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JwhSVg==")));
      JSONObject jsonObject = jsonArray.getJSONObject(0);
      info = jsonObject.getString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcPmozSFo=")));
      return info.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BxsjKkZJRihZExsw")));
   }

   public long downFileSync(String fileName, String fileId) throws Exception {
      String fileHref = this.getFileHrefSync(fileId);
      HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("B1ZcXkMWHzNYAzETAlcBLH43TChrNx4dLhZfCGIKIiodTQ5F")) + fileHref);
      LanzouHelper.Lanzou lanZouRealLink = LanzouHelper.getLanZouRealLink(fileHref);
      return this.downLoadDatabase(lanZouRealLink.getDlLink(), fileName);
   }

   public static byte[] read(InputStream inStream) throws Exception {
      ByteArrayOutputStream outStream = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      int len = false;

      int len;
      while((len = inStream.read(buffer)) != -1) {
         outStream.write(buffer, 0, len);
      }

      inStream.close();
      return outStream.toByteArray();
   }

   private void testkook(String url) throws IOException {
      OkHttpUtil.RequestData[] rs = new OkHttpUtil.RequestData[]{new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2LGUVGiY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRgALWogIARgJCg/Iy4qVg=="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4uKQ==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OghSVg==")))};
      OkHttpUtil.RequestData[] header = new OkHttpUtil.RequestData[]{new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jgg2OWgaIAY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgc6KGoFAil9AQozKi0XDWwaAiVlMwUrKgguPGZTAi1pATgqLAgYCGoKICB6DT89CCkEVg=="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jgg2OWgaIAZODDA2Ly1fPmwjMC0=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS1XCW8JGShiHjA+KhciLmknOyhoNApF"))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jgg2OWgaIAZODFE7Kj06LW4jEis=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KD5eDWMxASRnNB0hIwRWKn9TLFo="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ji4ACGojNClmHgY1Kj5SVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LC4uM28JEjdgHgYuKAhSVg=="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ji4ACGwFNCZmV10OKAcYM2UzFlo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OgM9Kg=="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ji4ACGwFNCZmV11LLQgmPQ==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgc6KGoFAil9AQozKi0XDWg3PD1vJCMeLi4ACGAOQTBlNF0uKRgYKWsVNDA="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JBgAKWwFSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ixg+CGkjGgVhIFk5Ki1XVg=="))), null, null, new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii4uOXobOCtmHig0OgVXDWkzGlo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4AKm8zSFo="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii4uOXobOCtmHig0OgYqMWUzGlo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4+DWhSEiVhNAY9KQcYVg=="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IQc2M28nEhFiJDA2LBhSVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OwgAImUVHiR9ChEvOjolOnw2Ei9lNywcKj01JGgxESN1DSMdPDk9JGcjAgR8IC8uDRgbPXpTATZmJFEdIxguB2gVFgtjAQ01PAQpI39TDT54VllNOwUqAWhTTCNsHhoaLypXG2sFLD1qMxE3Iy0cOWwgTTF/CgEhMzk5CH80RAJ3ClgrMyotOmIFQS5oDgoaPDktD0wkRDZ6N1RF"))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IF8IDGgaJAViASggKAc1D30FLAZqEVRF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IBYIQH0KMAZhHyw/IwgMPWoKBlo=")))};
      String data = OkHttpUtil.postSyncString(url, rs, header);
      FileTools.saveAsFileWriter(this.getSavePath() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBcqDWoJBgZnEQpF")), data);
      HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRg+LGtTTVo=")) + data);
   }

   private String GetDownUri(String fileSecondHref) throws Exception {
      OkHttpUtil.RequestData[] rs = new OkHttpUtil.RequestData[]{new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2LGUVGiY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRgALWogIARgJCg/Iy4qVg=="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4uKQ==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OghSVg==")))};
      OkHttpUtil.RequestData[] header = new OkHttpUtil.RequestData[]{new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jgg2OWgaIAY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgc6KGoFAil9AQozKi0XDWwaAiVlMwUrKgguPGZTAi1pATgqLAgYCGoKICB6DT89CCkEVg=="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jgg2OWgaIAZODDA2Ly1fPmwjMC0=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS1XCW8JGShiHjA+KhciLmknOyhoNApF"))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jgg2OWgaIAZODFE7Kj06LW4jEis=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KD5eDWMxASRnNB0hIwRWKn9TLFo="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ji4ACGojNClmHgY1Kj5SVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LC4uM28JEjdgHgYuKAhSVg=="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ji4ACGwFNCZmV10OKAcYM2UzFlo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OgM9Kg=="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ji4ACGwFNCZmV11LLQgmPQ==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgc6KGoFAil9AQozKi0XDWg3PD1vJCMeLi4ACGAOQTBlNF0uKRgYKWsVNDA="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JBgAKWwFSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ixg+CGkjGgVhIFk5Ki1XVg=="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Oy0MCWgzAiY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBcqLG8KLzJOIB4tLC4pDm8zQSZuNwYwIyocJWAgQVo="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ij4uPmgaFithN1RF")), fileSecondHref), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii4uOXobOCtmHig0OgVXDWkzGlo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4AKm8zSFo="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii4uOXobOCtmHig0OgYqMWUzGlo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4+DWhSEiVhNAY9KQcYVg=="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IQc2M28nEhFiJDA2LBhSVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OwgAImUVHiR9ChEvOjolOnw2Ei9lNywcKj01JGgxESN1DSMdPDk9JGcjAgR8IC8uDRgbPXpTATZmJFEdIxguB2gVFgtjAQ01PAQpI39TDT54VllNOwUqAWhTTCNsHhoaLypXG2sFLD1qMxE3Iy0cOWwgTTF/CgEhMzk5CH80RAJ3ClgrMyotOmIFQS5oDgoaPDktD0wkRDZ6N1RF"))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IF8IDGgaJAViASggKAc1D30FLAZqEVRF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IBYIQH0KMAZhHyw/IwgMPWoKBlo=")))};
      String data = OkHttpUtil.postSyncString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBcqLG8KLzJOIB4tLC4pDm8zQSZuNwYwIyocJWAgQCo=")), rs, header);
      FileTools.saveAsFileWriter(this.getSavePath() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBcqDWoJBgZnEQpF")), data);
      JSONObject jsonObject = (new JSONArray(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IC5SVg==")) + data + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JwhSVg==")))).getJSONObject(0);
      return jsonObject.getString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQcMDg==")));
   }

   public String getSavePath() {
      String path;
      if (VERSION.SDK_INT > 29) {
         path = App.getApp().getExternalFilesDir((String)null).getAbsolutePath() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg=="));
      } else {
         path = Environment.getExternalStorageDirectory().getPath() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg=="));
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
      String HONEYCOMB_USERAGENT = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OwgAImUVHiR9ChEvOjolOnwxOC9lNCgzPzo6XHckOA5sNDA7KQg2IHhTLwR/V1w3JAdeJGoFMyt+Mgo6Iy4HOGMgNC9gHg01IRVXXXpTBS94Hzg7KQgEJ24gLCVnJBo9OQMfD39SASN/Mz8/LSscWGQIQAR+NyQwLC4tOGAzNCljJBEzPxY+PWoaAi9lJx0cOgQbDksbNCRuNCQ7KiotCXwkMwR/VzBF"));
      builder.addHeader(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ji4ACGwFNCZmV11LLQgmPQ==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgc6KGoFAil9AQozKi0XDWg3PD1vJCMeLi4ACGAOQTBlNF0uKRgYKWsVNDA=")));
      builder.addHeader(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IQc2M28nEhFiJDA2LBhSVg==")), HONEYCOMB_USERAGENT);
      Request request = builder.get().build();
      Call call = this.mOkHttpClient.newCall(request);
      Response response = call.execute();
      return response;
   }

   private static SSLSocketFactory createSSLSocketFactory() {
      SSLSocketFactory ssfFactory = null;

      try {
         SSLContext sc = SSLContext.getInstance(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IRYEAw==")));
         sc.init((KeyManager[])null, new TrustManager[]{new OkHttpUtil.TrustAllCerts()}, new SecureRandom());
         ssfFactory = sc.getSocketFactory();
      } catch (Exception var2) {
      }

      return ssfFactory;
   }

   public String getFileHrefSync(String file_id) throws Exception {
      OkHttpUtil.RequestData[] rs = new OkHttpUtil.RequestData[]{new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRg+KWUzSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OjkMVg=="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4YDmgYGi9iEVRF")), file_id)};
      String uri = "";
      String response = OkHttpUtil.postSyncString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBcqLG8KLzJOIB4sLykYI28FNDJlJwYcPC42KWAOAi9sJwo5KT4uO2tSBiRlDjxF")), rs);
      JSONObject jsonObject = (new JSONArray(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IC5SVg==")) + response + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JwhSVg==")))).getJSONObject(0);
      String info = jsonObject.getString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcPmozSFo=")));
      JSONObject jsonObject2 = (new JSONArray(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IC5SVg==")) + info + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JwhSVg==")))).getJSONObject(0);
      String f_id = jsonObject2.getString(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LTsACWgFSFo=")));
      uri = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBcqLG8KLzJOIB4tLC4pDm8zQSZuNwYwIyocJWAgQCo=")) + f_id;
      return uri;
   }

   private String getFileSecondHref(String file_href) throws Exception {
      String data = OkHttpUtil.getSyncString(file_href);
      if (data.contains(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BwkFM0YyD1dYEws+OjkXDkdNGxdBX149AEQdX1gBLRVEEBtKAQs3LUFbWlc=")))) {
         throw new IOException();
      } else {
         Document document = Jsoup.parse(data);
         Elements element = document.getElementsByClass(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgiKn8jSFo=")));
         return element.attr(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0MOQ==")));
      }
   }

   private String GetDownKey(String fileSecondHref) throws Exception {
      String uri = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBcqLG8KLzJOIB4tLC4pDm8zQSZuNwYwIyocJWAgQVo=")) + fileSecondHref;
      String data = OkHttpUtil.getSyncString(uri);
      Document document = Jsoup.parse(data);
      String str = document.getElementsByTag(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki42KmUaIAY="))).toString().trim();
      int a = str.indexOf(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LghXP2kFMDdmHiM8PgMlMw==")));
      int b = str.indexOf(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LisAOQ==")));
      return str.substring(a + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LghXP2kFMDdmHiM8PgMlMw==")).length(), b + 3);
   }

   private String GetDownUri(String fileSecondHref, String sign) throws Exception {
      OkHttpUtil.RequestData[] rs = new OkHttpUtil.RequestData[]{new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2LGUVGiY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRgALWogIARgJCg/Iy4qVg=="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4YPWojSFo=")), sign), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4uKQ==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OghSVg==")))};
      OkHttpUtil.RequestData[] header = new OkHttpUtil.RequestData[]{new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jgg2OWgaIAY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgc6KGoFAil9AQozKi0XDWwaAiVlMwUrKgguPGZTAi1pATgqLAgYCGoKICB6DT89CCkEVg=="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jgg2OWgaIAZODDA2Ly1fPmwjMC0=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS1XCW8JGShiHjA+KhciLmknOyhoNApF"))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jgg2OWgaIAZODFE7Kj06LW4jEis=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KD5eDWMxASRnNB0hIwRWKn9TLFo="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ji4ACGojNClmHgY1Kj5SVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LC4uM28JEjdgHgYuKAhSVg=="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ji4ACGwFNCZmV10OKAcYM2UzFlo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OgM9Kg=="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ji4ACGwFNCZmV11LLQgmPQ==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgc6KGoFAil9AQozKi0XDWg3PD1vJCMeLi4ACGAOQTBlNF0uKRgYKWsVNDA="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JBgAKWwFSFo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ixg+CGkjGgVhIFk5Ki1XVg=="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Oy0MCWgzAiY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBcqLG8KLzJOIB4tLC45Dm8zQSZuNwYwKTocJWAgQVo="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ij4uPmgaFithN1RF")), fileSecondHref), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii4uOXobOCtmHig0OgVXDWkzGlo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4AKm8zSFo="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii4uOXobOCtmHig0OgYqMWUzGlo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4+DWhSEiVhNAY9KQcYVg=="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IQc2M28nEhFiJDA2LBhSVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OwgAImUVHiR9ChEvOjolOnw2Ei9lNywcKj01JGgxESN1DSMdPDk9JGcjAgR8IC8uDRgbPXpTATZmJFEdIxguB2gVFgtjAQ01PAQpI39TDT54VllNOwUqAWhTTCNsHhoaLypXG2sFLD1qMxE3Iy0cOWwgTTF/CgEhMzk5CH80RAJ3ClgrMyotOmIFQS5oDgoaPDktD0wkRDZ6N1RF"))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IF8IDGgaJAViASggKAc1D30FLAZqEVRF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IBYIQH0KMAZhHyw/IwgMPWoKBlo=")))};
      return "";
   }

   public String GetDownSecondUri(String downUri) throws Exception {
      String address = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBcqLG8KLzJOIB4uKQglDmk0TCZoNzgaLgcuDn0KRClpJFkcOQgEI2UVNwM=")) + downUri;
      String path = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My4iCWoFNyU=")) + downUri;
      OkHttpUtil.RequestData[] header = new OkHttpUtil.RequestData[]{new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LgcuLGUFGgRjAQoZ")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4YKHojMwJONCw7KQc2LWozQSZ1NzAcLBhSVg=="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwguLGUFGiw=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JSwuBg=="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Khg+LGUFSFo=")), path), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki42CmgVEis=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBcqLG8KLFo="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWgaIAY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRguIGwJGiBmHl0oOhciKmozOC9oJzg/IxgAKk4jBitqHlEbOgcML2VSHjNvDjw7JQcuKGoaBgFvVigvIwgDIW9THQJOMxkoKQdXOWkFBSVvJygpKQQEI2AKPCJuClkqLD4qIXVSTAN1IF0eMgQhJXtTQTVqNFE7LAg2P2wFAiVgMB4pKQc6DmkjASNrDlkqIwg+KmIgLz5qM1ErPAM+DXkOIwR5EVRF"))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWgaIAZODjA2Ly1fPmwjMC0=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS1XCW8JGShiHjA+KhciLmknOyhoNApF"))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWgaIAZODlE7Kj06LW4jEis=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KD5eDWMxASRnNB0hIwRWKn9TLFo="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uOXoVOCtmHig0OgdXDWkzGlo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4+LmUVPDdmHjBF"))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uOXoVOCtmHig0OggqMWUzGlo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4ACGgVSFo="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc6PW8jJCxiCl0zKj4qPW4KGgRrDQ45Lhc+CWIFND9lJ1RF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OghSVg=="))), new OkHttpUtil.RequestData(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQc2M28nEjdiJDA2LBhSVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OwgAImUVHiR9ChEvOjolOnw2Ei9lNywcKj01JGgxESN1DSMdPDk9JGcjAgR8IC8uDRgbPXpTATZmJFEdIxguB2gVFgtjAQ01PAQpI39TDT54VllNOwUqAWhTTCNsHhoaLypXG2sFLD1qMxE3Iy0cOWwgTTF/CgEgMzk5CH80DQZMClgrMwQ5OmIFQS5oDgoaPDktD0wkRDZ6N1RF")))};
      return OkHttpUtil.getSync(address, header).request().url().toString();
   }

   private long downLoadDatabase(String downSecondUri, String fileName) {
      String[] filename = downSecondUri.split(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PwhSVg==")));
      DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downSecondUri));
      DownloadManager downloadManager = (DownloadManager)App.getApp().getSystemService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRgALWojHiV9DgpF")));
      HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("BwwFOUZbGw9YXh8XA1dAJQ==")));
      return downloadManager.enqueue(request);
   }

   public static Promise<Boolean, Throwable, Void> Login(String username, String password, loginCallbackListener listener) {
      return ResponseProgram.defer().when(() -> {
         try {
            boolean cookiesActivation = MyCookieJar.isCookiesActivation();
            HVLog.i(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4AD2UzAithIiA5LBccLG4gBi9lJx0x")) + cookiesActivation);
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
                  finalI1 = i;
                  (new Handler(Looper.getMainLooper())).post(new Runnable() {
                     public void run() {
                        listener.onError(finalI1);
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
