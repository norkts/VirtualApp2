package com.carlos.common.clouddisk.http;

import com.carlos.libcommon.StringFog;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;

public class JsoupUtil {
   public static String LoginInfo(String html) {
      return html == null ? null : Jsoup.clean(html, Safelist.none());
   }

   public static String getText(String html) {
      return html == null ? null : Jsoup.clean(html, Safelist.none());
   }

   public static String getSimpleHtml(String html) {
      return html == null ? null : Jsoup.clean(html, Safelist.simpleText());
   }

   public static String getBasicHtml(String html) {
      return html == null ? null : Jsoup.clean(html, Safelist.basic());
   }

   public static String getBasicHtmlandimage(String html) {
      return html == null ? null : Jsoup.clean(html, Safelist.basicWithImages());
   }

   public static String getFullHtml(String html) {
      return html == null ? null : Jsoup.clean(html, Safelist.relaxed());
   }

   public static String clearTags(String html, String... tags) {
      Safelist wl = new Safelist();
      return Jsoup.clean(html, wl.addTags(tags));
   }

   public static String getImgSrc(String html) {
      if (html == null) {
         return null;
      } else {
         Document doc = Jsoup.parseBodyFragment(html);
         Element image = doc.select(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgIPQ=="))).first();
         return image == null ? null : image.attr(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0MOQ==")));
      }
   }
}
