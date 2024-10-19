package com.carlos.common.clouddisk.http;

import android.util.Log;
import com.carlos.libcommon.StringFog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class MyCookieJar implements CookieJar {
   static List<Cookie> cache = new ArrayList();

   public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
      cache.addAll(cookies);
   }

   public List<Cookie> loadForRequest(HttpUrl url) {
      List<Cookie> invalidCookies = new ArrayList();
      List<Cookie> validCookies = new ArrayList();
      Iterator var4 = cache.iterator();

      while(var4.hasNext()) {
         Cookie cookie = (Cookie)var4.next();
         if (cookie.expiresAt() < System.currentTimeMillis()) {
            invalidCookies.add(cookie);
         } else if (cookie.matches(url)) {
            validCookies.add(cookie);
         }
      }

      cache.removeAll(invalidCookies);
      return validCookies;
   }

   public static void resetCookies() {
      cache.clear();
   }

   public static boolean isCookiesActivation() {
      return !cache.isEmpty();
   }

   public static void print() {
      Log.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4AD2UzAis=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz0uDmoFSFo=")));
      Iterator<Cookie> it = cache.iterator();

      while(it.hasNext()) {
         Cookie s = (Cookie)it.next();
         Log.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4AD2UzAis=")), s.toString());
      }

   }
}
