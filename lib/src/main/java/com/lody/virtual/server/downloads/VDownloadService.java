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
      Uri uri = Uri.parse(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ACGwFNCZmVgU1Oi02DWUFMCRlJzgvKToAL2cLAi9sJzwdKT4uO2saLFo=")));
      Cursor cursor = this.mResolver.query(uri, new String[]{StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jy4YPA=="))}, (String)null, (String[])null, (String)null);
      if (cursor != null) {
         while(true) {
            if (!cursor.moveToNext()) {
               cursor.close();
               break;
            }

            VLog.e(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JRgALWojHiV9DgoNLwcYOWkFGgQ=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRgALWojHiV9Dg08KQc1IH4zSFo=")) + cursor.getLong(0));
         }
      }

   }
}
