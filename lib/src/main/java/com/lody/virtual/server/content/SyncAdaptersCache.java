package com.lody.virtual.server.content;

import android.accounts.Account;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SyncAdapterType;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Xml;
import com.lody.virtual.StringFog;
import com.lody.virtual.helper.utils.ComponentUtils;
import com.lody.virtual.server.accounts.RegisteredServicesParser;
import com.lody.virtual.server.pm.VPackageManagerService;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import mirror.android.content.SyncAdapterTypeN;
import mirror.com.android.internal.R_Hide;

public class SyncAdaptersCache {
   private Context mContext;
   private final Map<String, SyncAdapterInfo> mSyncAdapterInfos = new HashMap();

   public SyncAdaptersCache(Context context) {
      this.mContext = context;
   }

   public void refreshServiceCache(String packageName) {
      Intent intent = new Intent(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k5Ki0YLmkjMAZ1MjA0LC42HWIaPDNqHgo7")));
      if (packageName != null) {
         intent.setPackage(packageName);
      }

      this.generateServicesMap(VPackageManagerService.get().queryIntentServices(intent, (String)null, 128, 0), this.mSyncAdapterInfos, new RegisteredServicesParser());
   }

   public SyncAdapterInfo getServiceInfo(Account account, String providerName) {
      synchronized(this.mSyncAdapterInfos) {
         return (SyncAdapterInfo)this.mSyncAdapterInfos.get(account.type + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")) + providerName);
      }
   }

   public Collection<SyncAdapterInfo> getAllServices() {
      return this.mSyncAdapterInfos.values();
   }

   private void generateServicesMap(List<ResolveInfo> services, Map<String, SyncAdapterInfo> map, RegisteredServicesParser accountParser) {
      Iterator var4 = services.iterator();

      while(true) {
         ResolveInfo info;
         XmlResourceParser parser;
         do {
            if (!var4.hasNext()) {
               return;
            }

            info = (ResolveInfo)var4.next();
            parser = accountParser.getParser(this.mContext, info.serviceInfo, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1k5Ki0YLmkjMAZ1MjA0LC42HWIaPDNqHgo7")));
         } while(parser == null);

         try {
            AttributeSet attributeSet = Xml.asAttributeSet(parser);

            int type;
            while((type = parser.next()) != 1 && type != 2) {
            }

            if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0YCGs3EjdiHiAsLBcMKA==")).equals(parser.getName())) {
               SyncAdapterType adapterType = this.parseSyncAdapterType(accountParser.getResources(this.mContext, info.serviceInfo.applicationInfo), attributeSet);
               if (adapterType != null) {
                  String key = adapterType.accountType + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My5SVg==")) + adapterType.authority;
                  map.put(key, new SyncAdapterInfo(adapterType, info.serviceInfo));
               }
            }
         } catch (Exception var11) {
            Exception e = var11;
            e.printStackTrace();
         }
      }
   }

   private SyncAdapterType parseSyncAdapterType(Resources res, AttributeSet set) {
      TypedArray obtainAttributes = res.obtainAttributes(set, (int[])R_Hide.styleable.SyncAdapter.get());

      try {
         String contentAuthority = obtainAttributes.getString(R_Hide.styleable.SyncAdapter_contentAuthority.get());
         String accountType = obtainAttributes.getString(R_Hide.styleable.SyncAdapter_accountType.get());
         if (contentAuthority != null && accountType != null) {
            boolean userVisible = obtainAttributes.getBoolean(R_Hide.styleable.SyncAdapter_userVisible.get(), true);
            boolean supportsUploading = obtainAttributes.getBoolean(R_Hide.styleable.SyncAdapter_supportsUploading.get(), true);
            boolean isAlwaysSyncable = obtainAttributes.getBoolean(R_Hide.styleable.SyncAdapter_isAlwaysSyncable.get(), true);
            boolean allowParallelSyncs = obtainAttributes.getBoolean(R_Hide.styleable.SyncAdapter_allowParallelSyncs.get(), true);
            String settingsActivity = obtainAttributes.getString(R_Hide.styleable.SyncAdapter_settingsActivity.get());
            SyncAdapterType type;
            if (SyncAdapterTypeN.ctor != null) {
               type = (SyncAdapterType)SyncAdapterTypeN.ctor.newInstance(contentAuthority, accountType, userVisible, supportsUploading, isAlwaysSyncable, allowParallelSyncs, settingsActivity, null);
               obtainAttributes.recycle();
               return type;
            } else {
               type = (SyncAdapterType)mirror.android.content.SyncAdapterType.ctor.newInstance(contentAuthority, accountType, userVisible, supportsUploading, isAlwaysSyncable, allowParallelSyncs, settingsActivity);
               obtainAttributes.recycle();
               return type;
            }
         } else {
            obtainAttributes.recycle();
            return null;
         }
      } catch (Throwable var12) {
         Throwable e = var12;
         e.printStackTrace();
         return null;
      }
   }

   public static class SyncAdapterInfo {
      public SyncAdapterType type;
      public ServiceInfo serviceInfo;
      public ComponentName componentName;

      SyncAdapterInfo(SyncAdapterType adapterType, ServiceInfo serviceInfo) {
         this.type = adapterType;
         this.serviceInfo = serviceInfo;
         this.componentName = ComponentUtils.toComponentName(serviceInfo);
      }
   }
}
