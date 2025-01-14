package com.lody.virtual.server.accounts;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.ServiceInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import com.lody.virtual.server.pm.PackageCacheManager;
import com.lody.virtual.server.pm.PackageSetting;

public class RegisteredServicesParser {
   public XmlResourceParser getParser(Context context, ServiceInfo serviceInfo, String name) {
      Bundle meta = serviceInfo.metaData;
      if (meta != null) {
         int xmlId = meta.getInt(name);
         if (xmlId != 0) {
            try {
               return this.getResources(context, serviceInfo.applicationInfo).getXml(xmlId);
            } catch (Exception var7) {
               Exception e = var7;
               e.printStackTrace();
            }
         }
      }

      return null;
   }

   public Resources getResources(Context context, ApplicationInfo appInfo) {
      PackageSetting ps = PackageCacheManager.getSetting(appInfo.packageName);
      if (ps != null) {
         AssetManager assets = (AssetManager)mirror.android.content.res.AssetManager.ctor.newInstance();
         mirror.android.content.res.AssetManager.addAssetPath.call(assets, ps.getPackagePath());
         Resources hostRes = context.getResources();
         return new Resources(assets, hostRes.getDisplayMetrics(), hostRes.getConfiguration());
      } else {
         return null;
      }
   }
}
