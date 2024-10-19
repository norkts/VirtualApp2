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
      Headers headers = builder.set(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uPmgaFithN1RF")), url).build();
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
                  File file = new File(path, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRguKWwJBjJjASRF")));

                  try {
                     fos = new FileOutputStream(file);
                     byte[] bytes = new byte[1024];
                     long fileSize = response.body().contentLength();
                     HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4YDmgYLC9nNDMi")) + fileSize);
                     long sum = 0L;

                     int len;
                     while((len = is.read(bytes)) != -1) {
                        fos.write(bytes);
                        sum += (long)len;
                        int porSizex = (int)((float)sum * 1.0F / (float)fileSize * 100.0F);
                        HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PwMHO35THTN0DVwdPgRWJXskPzN5CgEoOF4IVg==")));
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

                  HVLog.i(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("B1ZcXkMWHzNYAB8CAhkFHQ==")));
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
      HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT0uDmoLRSVhJw0i")) + fullHost);
      Headers headers = builder.set(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uPmgaFithN1RF")), url).build();
      String name = null;
      String size = null;

      try {
         Document doc = Jsoup.connect(url).get();
         Elements title1 = doc.select(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRgYLmEwLAZnDlE/Pgc+DW8aASNsJx4xLl5WJE8nODNrVgI9Ly0MCnUFJAJlESA5MTkiKm4KAiJpJFguKhg+PGgFAiZiIwU8PAQ+Kmg3TAJsHlgrOSk6DmdTOzNlERk0JS5SVg==")));
         if (title1.size() != 0) {
            name = ((Element)title1.get(0)).html();
         } else {
            name = doc.select(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pi4iCWoFNCZ9DgI7LRhSVg=="))).html();
         }

         HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4+DWhWBzRVN1RF")) + name);
         Elements size1 = doc.select(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwguLGsYQSZ9Dl0/Pgc2PWoFAgRqDjw/IxgAKmwFSFo=")));
         if (size1.size() != 0) {
            size = regex(size1.attr(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ACGwFNCZmEVRF"))), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JxgpCGYFMyB0IwU8OQMIGWYnPFdoDQ4xIF9bVg==")));
         }

         Elements realPage = doc.select(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgiKmsVEis=")));
         HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uP2oIIDdiJAg1W0QIVg==")) + realPage);
         String realUrl = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBcqLG8KLzJOIB5F")) + fullHost + realPage.attr(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0MOQ==")));
         doc = Jsoup.connect(realUrl).get();
         HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj4uP2oINARgUxMeUj5SVg==")) + realUrl);
         Elements select = doc.select(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki42KmUaIAZvJwoZIxcLJWUzGjBvVgYhLRciO2EgNDVvASA9JS5SVg==")));
         Element element = (Element)select.get(1);
         String jshtml = element.html();
         String js = ((Element)select.get(1)).html();
         String jsonData = matcherJSON(jshtml);
         String sign = getJsonValue(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4YPWojSFo=")), jsonData);
         HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4YPWorBzRVN1RF")) + sign);
         String ves = getJsonValue(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4uKQ==")), jsonData);
         if (TextUtils.isEmpty(ves)) {
            ves = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OghSVg=="));
         }

         HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4uKUAGG1c=")) + ves);
         String action = getJsonValue(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2LGUVGiY=")), jsonData);
         HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2LGUVGiZeK0ZN")) + action);
         String signs = getJsonValue(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LghXP2kFMDdmHiBF")), jsonData);
         HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4YPWogFCUZTQJF")) + signs);
         if (TextUtils.isEmpty(signs)) {
            signs = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LghXP2kFMDdmHiBF"));
         }

         String webSignKey = getJsonValue(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KS4uOm8zAi1gNA4/LQhSVg==")), jsonData);
         HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KS4uOmczAi1gMg4/LR9cJhUVSFo=")) + webSignKey);
         if (TextUtils.isEmpty(webSignKey)) {
            webSignKey = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KS4uOmczAi1gMg4/LQhSVg=="));
         }

         String webSign = "";
         String apiUrl = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBcqLG8KLzJOIB5F")) + fullHost + getJsonValue(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQcMDg==")), js);
         HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgc6CWQaFiReK0ZN")) + apiUrl);
         OkHttpClient client = new OkHttpClient();
         RequestBody formBody = (new FormBody.Builder()).add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4YPWojSFo=")), sign).add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2LGUVGiY=")), action).add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4YPWogLFo=")), signs).add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KS4uOm8zAi1gN1RF")), webSign).add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KS4uOm8zAi1gNA4/LQhSVg==")), webSignKey).add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4uKQ==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OghSVg=="))).build();
         Request request = (new Request.Builder()).url(apiUrl).post(formBody).headers(headers).build();
         String dl = null;

         try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
               throw new IOException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IQgcM2kKICt9Jwo/KF4mP28FBit4EVRF")) + response);
            } else {
               String body = response.body().string();
               HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4APGlTTVo=")) + body);
               String dom = getJsonValue(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRgADQ==")), body).replace(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("J18AVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")));
               HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4APGlSICxgJFwi")) + dom);
               String file = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My4iCWoFNyU="));
               String domurl = getJsonValue(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQcMDg==")), body).replace(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("J18AVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")));
               HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lj4APGlSICxgJF0vIz1aIA==")) + domurl);
               dl = getJsonValue(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRgADQ==")), body).replace(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("J18AVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg=="))) + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My4iCWoFNyU=")) + getJsonValue(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KQcMDg==")), body).replace(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("J18AVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")));
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
         Pattern pattern = Pattern.compile(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PBgqP2wFJy90ICciP14AGGgIJB5sIgIQIF9XEmQOTCxgHh5F")));
         Matcher matcher = pattern.matcher(content);
         HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iwg+LGszRSthMgIPIisXIA==")) + matcher + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsFEjdmHig0KAgtIA==")) + matcher.find() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pl85OHsFEjdmHig0KAgtDmkKRSVvDjwALD0uKmZTASx7N1RF")) + matcher.groupCount());
         String group = matcher.group(0);
         String[] strings = group.split("\n");
         String requestData = strings[1];
         int startIndexOf = requestData.indexOf(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KC5SVg==")));
         int endIndexOf = requestData.indexOf(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LwhSVg==")));
         String substring = requestData.substring(startIndexOf, endIndexOf + 1);
         HVLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhgIP2wFLCBiAS88KBciLm4nTQNvAQo6KgcMI2AwJz0=")) + substring);
         return substring;
      } catch (Exception var9) {
         Exception e = var9;
         HVLog.printException(e);
         return "";
      }
   }

   private static String getJsonValue(String key, String json) {
      return regex(json, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PF4fJH4VSFo=")) + key + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IColOmZTBSBLVxkbJSoHJWMnESh6CgYIPjoMEUkORC54IBkoPSs9IXg2HQU=")));
   }

   private static String getMainHost(String url) {
      Pattern p = Pattern.compile(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PF4fJH4VRQZmESciOilfJmM3My9mIh0bIF9WOWxTRCt4Iw4sKQgmEnUzLARuARo0IBZWJWkgAipvDlkyJ18cOWogHilgJF0eKj0MLmszNARrJAIqLC0EJmMFHgFvDh4vKQciJWggHiBsIx5F")), 2);
      Matcher matcher = p.matcher(url);
      return matcher.find() ? matcher.group() : null;
   }

   private static String regex(String words, String regex) {
      Pattern p = Pattern.compile(regex, 2);
      Matcher matcher = p.matcher(words);
      return matcher.find() ? matcher.group() : null;
   }

   private static String getFullHost(String url) {
      Pattern p = Pattern.compile(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("ICsbD3o2HSJ0JVA2ORcqDW8gOCllNAIbLhcqOGAjMCJoHigiLhciI2UzOANuATA2IhgMPX8FSFo=")), 2);
      Matcher matcher = p.matcher(url);
      return matcher.find() ? matcher.group() : null;
   }

   public static void main(String[] args) {
      ArrayList<String> urls = new ArrayList();
      urls.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBcqLG8KLzJOIB43LwcqI2wJMCRoARoxLD0uKU4wNCpsClkiJBYLDm8gPCN/AQEvJBhSVg==")));
      urls.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBcqLG8KLzJOIB4uKQcYP2kjMAZoDjw7KTocKH0KRT1sJwoeORgYKWUJGgV8DihBCS1XJGVSESBsEVRF")));
      Iterator var2 = urls.iterator();

      while(var2.hasNext()) {
         String url = (String)var2.next();
         System.out.println(getLanZouRealLink(url));
      }

   }

   static {
      builder.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jgg2OWgaIAY=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRguIGwJGiBmHl0oOhciKmozOC9oJzg/IxgAKk4jBitqHlEbOgcML2VSHjNvDjw7JQcuKGoaBgFvVigvIwgDIW9THQJOMxkoKQdXOWkFBSVvJygpKQQEI2AKPCJuClkqLD4qIXVSTAN1IF0eMgQhJXsVSFo=")));
      builder.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jgg2OWgaIAZODDA2Ly1fPmwjMC0=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LS1XCW8JGShiHjA+KhciLmkjSFo=")));
      builder.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IQc6PW8jJCxiCl0JKj4qPW4KGgRrDQ4fLhc+CWIFND9lJ1RF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OghSVg==")));
      builder.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgg2OWgaIAZODlE7Kj06LW4jEis=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KD5eDWMxASRnNB0hIwRWKn9TLyRuN1geIgUlPWEJQDN8MxkbLy4pL2cILy1vVwEdCDo9O24KDStqICMdMzkhDmUjJzFhDVwsOjoMVg==")));
      builder.add(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IQc2M28nEhFiJDA2LBhSVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OwgAImUVHiR9ChEvOjolOnwxPDdoJx4bKggAD2NSHSNnDh49Ly4hJH0FJDV7DwZBDRYbL3UJPBF6IAYAOSoXOGMaIAJgHjBIKAcuXGwgASV/CjM+PCk1MktTBghnHzBBITohJGUVAj1oVjwIJAcuImwkATZmAQobIy4IM3o0DTBOMyc2PF85LXg3MwF8Vw0rIT4+In0FMCx8Iw08MwQpD38zSFo=")));
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
         return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4+DWhTHS0=")) + this.name + '\'' + '\n' + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186KWUaTSt0CjhF")) + this.size + '\'' + '\n' + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186PGoLHi9gNAEdOC5SVg==")) + this.dlLink + '\'' + '\n';
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
