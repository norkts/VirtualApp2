package com.lody.virtual.helper.compat;

import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageParser;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.os.Process;
import android.os.Build.VERSION;
import android.util.DisplayMetrics;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.os.VUserHandle;
import java.io.File;
import mirror.android.content.pm.PackageParserJellyBean;
import mirror.android.content.pm.PackageParserJellyBean17;
import mirror.android.content.pm.PackageParserLollipop;
import mirror.android.content.pm.PackageParserLollipop22;
import mirror.android.content.pm.PackageParserMarshmallow;
import mirror.android.content.pm.PackageParserNougat;
import mirror.android.content.pm.PackageParserPie;
import mirror.android.content.pm.PackageParserTiramisu;
import mirror.android.content.pm.PackageUserState;
import mirror.android.content.pm.pkg.FrameworkPackageUserState;

public class PackageParserCompat {
   public static final int[] GIDS = VirtualCore.get().getGids();
   private static final int API_LEVEL;
   private static final int myUserId;
   private static final Object sUserState;

   public static Object getUserState() {
      if (BuildCompat.isTiramisu()) {
         return FrameworkPackageUserState.DEFAULT;
      } else {
         return API_LEVEL >= 17 ? PackageUserState.ctor.newInstance() : null;
      }
   }

   public static PackageParser createParser(File packageFile) {
      if (API_LEVEL >= 33) {
         return (PackageParser)PackageParserTiramisu.ctor.newInstance();
      } else if (API_LEVEL >= 23) {
         return (PackageParser)PackageParserMarshmallow.ctor.newInstance();
      } else if (API_LEVEL >= 22) {
         return (PackageParser)PackageParserLollipop22.ctor.newInstance();
      } else if (API_LEVEL >= 21) {
         return (PackageParser)PackageParserLollipop.ctor.newInstance();
      } else if (API_LEVEL >= 17) {
         return (PackageParser)PackageParserJellyBean17.ctor.newInstance(packageFile.getAbsolutePath());
      } else {
         return API_LEVEL >= 16 ? (PackageParser)PackageParserJellyBean.ctor.newInstance(packageFile.getAbsolutePath()) : (PackageParser)mirror.android.content.pm.PackageParser.ctor.newInstance(packageFile.getAbsolutePath());
      }
   }

   public static PackageParser.Package parsePackage(PackageParser parser, File packageFile, int flags) throws Throwable {
      if (API_LEVEL >= 33) {
         return (PackageParser.Package)PackageParserTiramisu.parsePackage.callWithException(parser, packageFile, flags);
      } else if (API_LEVEL >= 23) {
         return (PackageParser.Package)PackageParserMarshmallow.parsePackage.callWithException(parser, packageFile, flags);
      } else if (API_LEVEL >= 22) {
         return (PackageParser.Package)PackageParserLollipop22.parsePackage.callWithException(parser, packageFile, flags);
      } else if (API_LEVEL >= 21) {
         return (PackageParser.Package)PackageParserLollipop.parsePackage.callWithException(parser, packageFile, flags);
      } else if (API_LEVEL >= 17) {
         return (PackageParser.Package)PackageParserJellyBean17.parsePackage.callWithException(parser, packageFile, null, new DisplayMetrics(), flags);
      } else {
         return API_LEVEL >= 16 ? (PackageParser.Package)PackageParserJellyBean.parsePackage.callWithException(parser, packageFile, null, new DisplayMetrics(), flags) : (PackageParser.Package)mirror.android.content.pm.PackageParser.parsePackage.callWithException(parser, packageFile, null, new DisplayMetrics(), flags);
      }
   }

   public static ServiceInfo generateServiceInfo(PackageParser.Service service, int flags) {
      if (API_LEVEL >= 33) {
         return (ServiceInfo)PackageParserTiramisu.generateServiceInfo.call(service, flags, sUserState, myUserId);
      } else if (API_LEVEL >= 23) {
         return (ServiceInfo)PackageParserMarshmallow.generateServiceInfo.call(service, flags, sUserState, myUserId);
      } else if (API_LEVEL >= 22) {
         return (ServiceInfo)PackageParserLollipop22.generateServiceInfo.call(service, flags, sUserState, myUserId);
      } else if (API_LEVEL >= 21) {
         return (ServiceInfo)PackageParserLollipop.generateServiceInfo.call(service, flags, sUserState, myUserId);
      } else if (API_LEVEL >= 17) {
         return (ServiceInfo)PackageParserJellyBean17.generateServiceInfo.call(service, flags, sUserState, myUserId);
      } else {
         return API_LEVEL >= 16 ? (ServiceInfo)PackageParserJellyBean.generateServiceInfo.call(service, flags, false, 1, myUserId) : (ServiceInfo)mirror.android.content.pm.PackageParser.generateServiceInfo.call(service, flags);
      }
   }

   public static ApplicationInfo generateApplicationInfo(PackageParser.Package p, int flags) {
      if (API_LEVEL >= 33) {
         return (ApplicationInfo)PackageParserTiramisu.generateApplicationInfo.call(p, flags, sUserState);
      } else if (API_LEVEL >= 23) {
         return (ApplicationInfo)PackageParserMarshmallow.generateApplicationInfo.call(p, flags, sUserState);
      } else if (API_LEVEL >= 22) {
         return (ApplicationInfo)PackageParserLollipop22.generateApplicationInfo.call(p, flags, sUserState);
      } else if (API_LEVEL >= 21) {
         return (ApplicationInfo)PackageParserLollipop.generateApplicationInfo.call(p, flags, sUserState);
      } else if (API_LEVEL >= 17) {
         return (ApplicationInfo)PackageParserJellyBean17.generateApplicationInfo.call(p, flags, sUserState);
      } else {
         return API_LEVEL >= 16 ? (ApplicationInfo)PackageParserJellyBean.generateApplicationInfo.call(p, flags, false, 1) : (ApplicationInfo)mirror.android.content.pm.PackageParser.generateApplicationInfo.call(p, flags);
      }
   }

   public static ActivityInfo generateActivityInfo(PackageParser.Activity activity, int flags) {
      if (API_LEVEL >= 33) {
         return (ActivityInfo)PackageParserTiramisu.generateActivityInfo.call(activity, flags, sUserState, myUserId);
      } else if (API_LEVEL >= 23) {
         return (ActivityInfo)PackageParserMarshmallow.generateActivityInfo.call(activity, flags, sUserState, myUserId);
      } else if (API_LEVEL >= 22) {
         return (ActivityInfo)PackageParserLollipop22.generateActivityInfo.call(activity, flags, sUserState, myUserId);
      } else if (API_LEVEL >= 21) {
         return (ActivityInfo)PackageParserLollipop.generateActivityInfo.call(activity, flags, sUserState, myUserId);
      } else if (API_LEVEL >= 17) {
         return (ActivityInfo)PackageParserJellyBean17.generateActivityInfo.call(activity, flags, sUserState, myUserId);
      } else {
         return API_LEVEL >= 16 ? (ActivityInfo)PackageParserJellyBean.generateActivityInfo.call(activity, flags, false, 1, myUserId) : (ActivityInfo)mirror.android.content.pm.PackageParser.generateActivityInfo.call(activity, flags);
      }
   }

   public static ProviderInfo generateProviderInfo(PackageParser.Provider provider, int flags) {
      if (API_LEVEL >= 33) {
         return (ProviderInfo)PackageParserTiramisu.generateProviderInfo.call(provider, flags, sUserState, myUserId);
      } else if (API_LEVEL >= 23) {
         return (ProviderInfo)PackageParserMarshmallow.generateProviderInfo.call(provider, flags, sUserState, myUserId);
      } else if (API_LEVEL >= 22) {
         return (ProviderInfo)PackageParserLollipop22.generateProviderInfo.call(provider, flags, sUserState, myUserId);
      } else if (API_LEVEL >= 21) {
         return (ProviderInfo)PackageParserLollipop.generateProviderInfo.call(provider, flags, sUserState, myUserId);
      } else if (API_LEVEL >= 17) {
         return (ProviderInfo)PackageParserJellyBean17.generateProviderInfo.call(provider, flags, sUserState, myUserId);
      } else {
         return API_LEVEL >= 16 ? (ProviderInfo)PackageParserJellyBean.generateProviderInfo.call(provider, flags, false, 1, myUserId) : (ProviderInfo)mirror.android.content.pm.PackageParser.generateProviderInfo.call(provider, flags);
      }
   }

   public static PackageInfo generatePackageInfo(PackageParser.Package p, int flags, long firstInstallTime, long lastUpdateTime) {
      if (API_LEVEL >= 33) {
         return (PackageInfo)PackageParserTiramisu.generatePackageInfo.call(p, GIDS, flags, firstInstallTime, lastUpdateTime, null, sUserState);
      } else if (API_LEVEL >= 23) {
         return (PackageInfo)PackageParserMarshmallow.generatePackageInfo.call(p, GIDS, flags, firstInstallTime, lastUpdateTime, null, sUserState);
      } else if (API_LEVEL >= 21) {
         return PackageParserLollipop22.generatePackageInfo != null ? (PackageInfo)PackageParserLollipop22.generatePackageInfo.call(p, GIDS, flags, firstInstallTime, lastUpdateTime, null, sUserState) : (PackageInfo)PackageParserLollipop.generatePackageInfo.call(p, GIDS, flags, firstInstallTime, lastUpdateTime, null, sUserState);
      } else if (API_LEVEL >= 17) {
         return (PackageInfo)PackageParserJellyBean17.generatePackageInfo.call(p, GIDS, flags, firstInstallTime, lastUpdateTime, null, sUserState);
      } else {
         return API_LEVEL >= 16 ? (PackageInfo)PackageParserJellyBean.generatePackageInfo.call(p, GIDS, flags, firstInstallTime, lastUpdateTime, null) : (PackageInfo)mirror.android.content.pm.PackageParser.generatePackageInfo.call(p, GIDS, flags, firstInstallTime, lastUpdateTime);
      }
   }

   public static void collectCertificates(PackageParser parser, PackageParser.Package p, int flags) throws Throwable {
      if (BuildCompat.isPie()) {
         PackageParserPie.collectCertificates.callWithException(p, true);
      } else if (API_LEVEL >= 24) {
         PackageParserNougat.collectCertificates.callWithException(p, flags);
      } else if (API_LEVEL >= 23) {
         PackageParserMarshmallow.collectCertificates.callWithException(parser, p, flags);
      } else if (API_LEVEL >= 22) {
         PackageParserLollipop22.collectCertificates.callWithException(parser, p, flags);
      } else if (API_LEVEL >= 21) {
         PackageParserLollipop.collectCertificates.callWithException(parser, p, flags);
      } else if (API_LEVEL >= 17) {
         PackageParserJellyBean17.collectCertificates.callWithException(parser, p, flags);
      } else if (API_LEVEL >= 16) {
         PackageParserJellyBean.collectCertificates.callWithException(parser, p, flags);
      } else {
         mirror.android.content.pm.PackageParser.collectCertificates.call(parser, p, flags);
      }

   }

   static {
      API_LEVEL = VERSION.SDK_INT;
      myUserId = VUserHandle.getUserId(Process.myUid());
      sUserState = getUserState();
   }
}
