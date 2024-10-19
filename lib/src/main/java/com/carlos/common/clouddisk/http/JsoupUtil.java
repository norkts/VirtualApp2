package com.carlos.common.clouddisk.http;

import com.carlos.libcommon.StringFog;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

public class JsoupUtil {
   public static String LoginInfo(String html) {
      return html == null ? null : Jsoup.clean(html, Whitelist.none());
   }

   public static String getText(String html) {
      return html == null ? null : Jsoup.clean(html, Whitelist.none());
   }

   public static String getSimpleHtml(String html) {
      return html == null ? null : Jsoup.clean(html, Whitelist.simpleText());
   }

   public static String getBasicHtml(String html) {
      return html == null ? null : Jsoup.clean(html, Whitelist.basic());
   }

   public static String getBasicHtmlandimage(String html) {
      return html == null ? null : Jsoup.clean(html, Whitelist.basicWithImages());
   }

   public static String getFullHtml(String html) {
      return html == null ? null : Jsoup.clean(html, Whitelist.relaxed());
   }

   public static String clearTags(String html, String... tags) {
      Whitelist wl = new Whitelist();
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
