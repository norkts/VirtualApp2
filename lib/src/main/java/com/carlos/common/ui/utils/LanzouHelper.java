package com.carlos.common.ui.utils;

import android.text.TextUtils;
import com.carlos.libcommon.StringFog;
import com.kook.common.utils.HVLog;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LanzouHelper {
   static Headers.Builder builder = new Headers.Builder();

   public static String getLanZouDownLink(final String filePath, String url) {
      Headers headers = builder.set("referer", url).build();
      OkHttpClient client = new OkHttpClient();
      RequestBody formBody = (new FormBody.Builder()).build();
      Request request = (new Request.Builder()).url(url).post(formBody).headers(headers).build();

      try {
         client.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
            }

            public void onResponse(Call call, Response response) throws IOException {
               if (response.isSuccessful()) {
                  InputStream is = null;
                  FileOutputStream fos = null;
                  is = response.body().byteStream();
                  String path = filePath;
                  File file = new File(path, "test.zip");

                  try {
                     fos = new FileOutputStream(file);
                     byte[] bytes = new byte[1024];
                     long fileSize = response.body().contentLength();
                     HVLog.d("fileSize:" + fileSize);
                     long sum = 0L;

                     int len;
                     while((len = is.read(bytes)) != -1) {
                        fos.write(bytes);
                        sum += (long)len;
                        int porSizex = (int)((float)sum * 1.0F / (float)fileSize * 100.0F);
                        HVLog.d("====================");
                     }
                  } catch (Exception var22) {
                     Exception ex = var22;
                     ex.printStackTrace();
                  } finally {
                     try {
                        if (is != null) {
                           is.close();
                        }

                        if (fos != null) {
                           fos.close();
                        }
                     } catch (IOException var21) {
                        IOException e = var21;
                        e.printStackTrace();
                     }

                  }

                  HVLog.i("下载成功");
               }

            }
         });
         return null;
      } catch (Exception var7) {
         Exception e = var7;
         HVLog.printException(e);
         return null;
      }
   }

   public static Lanzou getLanZouRealLink(String url) {
      String fullHost = getFullHost(url);
      HVLog.d("fullHost:" + fullHost);
      Headers headers = builder.set("referer", url).build();
      String name = null;
      String size = null;

      try {
         Document doc = Jsoup.connect(url).get();
         Elements title1 = doc.select("div[style=font-size: 30px;text-align: center;padding: 56px 0px 20px 0px;]");
         if (title1.size() != 0) {
            name = ((Element)title1.get(0)).html();
         } else {
            name = doc.select("#filenajax").html();
         }

         HVLog.d("name：" + name);
         Elements size1 = doc.select("meta[name=description]");
         if (size1.size() != 0) {
            size = regex(size1.attr("content"), "\\d.\\d(?: )*[A-Za-z]+");
         }

         Elements realPage = doc.select("iframe");
         HVLog.d("realPage：" + realPage);
         String realUrl = "https://" + fullHost + realPage.attr("src");
         doc = Jsoup.connect(realUrl).get();
         HVLog.d("realUrl：" + realUrl);
         Elements select = doc.select("script[type=text/javascript]");
         Element element = (Element)select.get(1);
         String jshtml = element.html();
         String js = ((Element)select.get(1)).html();
         String jsonData = matcherJSON(jshtml);
         String sign = getJsonValue("sign", jsonData);
         HVLog.d("sign：" + sign);
         String ves = getJsonValue("ves", jsonData);
         if (TextUtils.isEmpty(ves)) {
            ves = "1";
         }

         HVLog.d("ves：" + ves);
         String action = getJsonValue("action", jsonData);
         HVLog.d("action：" + action);
         String signs = getJsonValue("ajaxdata", jsonData);
         HVLog.d("signs：" + signs);
         if (TextUtils.isEmpty(signs)) {
            signs = "ajaxdata";
         }

         String webSignKey = getJsonValue("websignkey", jsonData);
         HVLog.d("webSignKey：" + webSignKey);
         if (TextUtils.isEmpty(webSignKey)) {
            webSignKey = "webSignKey";
         }

         String webSign = "";
         String apiUrl = "https://" + fullHost + getJsonValue("url", js);
         HVLog.d("apiUrl：" + apiUrl);
         OkHttpClient client = new OkHttpClient();
         RequestBody formBody = (new FormBody.Builder()).add("sign", sign).add("action", action).add("signs", signs).add("websign", webSign).add("websignkey", webSignKey).add("ves", "1").build();
         Request request = (new Request.Builder()).url(apiUrl).post(formBody).headers(headers).build();
         String dl = null;

         try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
               throw new IOException("Unexpected code " + response);
            } else {
               String body = response.body().string();
               HVLog.d("body:" + body);
               String dom = getJsonValue("dom", body).replace("\\/", "/");
               HVLog.d("body dom:" + dom);
               String file = "/file/";
               String domurl = getJsonValue("url", body).replace("\\/", "/");
               HVLog.d("body domurl:" + domurl);
               dl = getJsonValue("dom", body).replace("\\/", "/") + "/file/" + getJsonValue("url", body).replace("\\/", "/");
               return new Lanzou(name, size, dl);
            }
         } catch (Exception var31) {
            Exception e = var31;
            HVLog.printException(e);
            return new Lanzou(name, size, dl);
         }
      } catch (IOException var32) {
         IOException e = var32;
         HVLog.printException(e);
         return null;
      }
   }

   public static String matcherJSON(String content) {
      try {
         Pattern pattern = Pattern.compile("(data)? : (\\{[\\s\\S]*\\},)\\n");
         Matcher matcher = pattern.matcher(content);
         HVLog.d("matcherJSON:" + matcher + "    matcher:" + matcher.find() + "    matcher.groupCount():" + matcher.groupCount());
         String group = matcher.group(0);
         String[] strings = group.split("\n");
         String requestData = strings[1];
         int startIndexOf = requestData.indexOf("{");
         int endIndexOf = requestData.indexOf("}");
         String substring = requestData.substring(startIndexOf, endIndexOf + 1);
         HVLog.d(" matcher data substring:" + substring);
         return substring;
      } catch (Exception var9) {
         Exception e = var9;
         HVLog.printException(e);
         return "";
      }
   }

   private static String getJsonValue(String key, String json) {
      return regex(json, "(?<=" + key + "[\'\"]?( )?[:=]( )?[\'\"]).+?(?=[\'\"])");
   }

   private static String getMainHost(String url) {
      Pattern p = Pattern.compile("(?<=http://|\\.)[^.]*?\\.(?:com\\.cn|net\\.cn|org\\.cn|com|net|org|cn|biz|info|cc|tv)", 2);
      Matcher matcher = p.matcher(url);
      return matcher.find() ? matcher.group() : null;
   }

   private static String regex(String words, String regex) {
      Pattern p = Pattern.compile(regex, 2);
      Matcher matcher = p.matcher(words);
      return matcher.find() ? matcher.group() : null;
   }

   private static String getFullHost(String url) {
      Pattern p = Pattern.compile("[^//]*?\\.(com|cn|net|org|biz|info|cc|tv)", 2);
      Matcher matcher = p.matcher(url);
      return matcher.find() ? matcher.group() : null;
   }

   public static void main(String[] args) {
      ArrayList<String> urls = new ArrayList();
      urls.add("https://macwk.lanzouo.com/iRH0wwq0m8d");
      urls.add("https://vincentapps.lanzouo.com/i4uS3lmp56j");
      Iterator var2 = urls.iterator();

      while(var2.hasNext()) {
         String url = (String)var2.next();
         System.out.println(getLanZouRealLink(url));
      }

   }

   static {
      builder.add("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
      builder.add("Accept-Encoding", "gzip, deflate");
      builder.add("Upgrade-Insecure-Requests", "1");
      builder.add("accept-language", "zh-CN,zh;q=0.9,zh-TW;q=0.8,en-US;q=0.7,en;q=0.6,ja;q=0.5");
      builder.add("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36");
   }

   public static class Lanzou {
      String name;
      String size;
      String dlLink;

      public Lanzou(String name, String size, String dlLink) {
         this.name = name;
         this.size = size;
         this.dlLink = dlLink;
      }

      public String toString() {
         return "name=\'" + this.name + '\'' + '\n' + ", size=\'" + this.size + '\'' + '\n' + ", dlLink=\'" + this.dlLink + '\'' + '\n';
      }

      public String getName() {
         return this.name;
      }

      public String getSize() {
         return this.size;
      }

      public String getDlLink() {
         return this.dlLink;
      }
   }
}
