package com.lody.virtual.server.downloads;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.utils.VLog;

public class VDownloadService {
   private ContentResolver mResolver = VirtualCore.get().getContext().getContentResolver();

   private void trimDownloadRequests() {
      Uri uri = Uri.parse("content://downloads/my_downloads");
      Cursor cursor = this.mResolver.query(uri, new String[]{"_id"}, (String)null, (String[])null, (String)null);
      if (cursor != null) {
         while(true) {
            if (!cursor.moveToNext()) {
               cursor.close();
               break;
            }

            VLog.e("DownloadManager", "download id: " + cursor.getLong(0));
         }
      }

   }
}
