package com.lody.virtual.client.stub;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Build.VERSION;
import com.lody.virtual.StringFog;

public class RequestExternalStorageManagerActivity extends Activity {
   public static void request(Context context, boolean isExt) {
      Intent intent = new Intent();
      if (isExt) {
         intent.setClassName(StubManifest.EXT_PACKAGE_NAME, RequestExternalStorageManagerActivity.class.getName());
      } else {
         intent.setClassName(StubManifest.PACKAGE_NAME, RequestExternalStorageManagerActivity.class.getName());
      }

      intent.setFlags(268435456);
      context.startActivity(intent);
   }

   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      if (VERSION.SDK_INT >= 30 && !Environment.isExternalStorageManager()) {
         Intent intent = new Intent(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1kpKAg2LmwjMC1sIxpXJRYcHWomLF9hDF1AJQYEBX0bNA9mNTgMIys2HH0hRQBhIlkSJAU2A30bGgA=")));
         intent.addFlags(268435456);
         this.startActivity(intent);
      }

      this.finish();
   }
}
