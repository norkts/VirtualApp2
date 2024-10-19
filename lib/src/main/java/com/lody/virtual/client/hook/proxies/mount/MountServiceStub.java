package com.lody.virtual.client.hook.proxies.mount;

import android.app.usage.StorageStats;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.IInterface;
import android.os.ParcelableException;
import android.os.storage.StorageManager;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.hook.annotations.Inject;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.BinderInvocationStub;
import com.lody.virtual.client.hook.base.ReplaceLastPkgMethodProxy;
import com.lody.virtual.client.hook.base.StaticMethodProxy;
import com.lody.virtual.client.ipc.VPackageManager;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.ArrayUtils;
import com.lody.virtual.helper.utils.Reflect;
import java.lang.reflect.Method;
import mirror.RefStaticMethod;
import mirror.android.os.mount.IMountService;
import mirror.android.os.storage.IStorageManager;

@Inject(MethodProxies.class)
public class MountServiceStub extends BinderInvocationProxy {
   public MountServiceStub() {
      super(getInterfaceMethod(), "mount");
      IInterface hookedStorageManager = (IInterface)((BinderInvocationStub)this.getInvocationStub()).getProxyInterface();

      try {
         Reflect.on(StorageManager.class).set("sStorageManager", hookedStorageManager);
      } catch (Throwable var3) {
         Throwable e = var3;
         e.printStackTrace();
      }

   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceLastPkgMethodProxy("getTotalBytes"));
      this.addMethodProxy(new ReplaceLastPkgMethodProxy("getCacheBytes"));
      this.addMethodProxy(new StaticMethodProxy("getCacheQuotaBytes") {
         public Object call(Object who, Method method, Object... args) throws Throwable {
            if (args[args.length - 1] instanceof Integer) {
               args[args.length - 1] = getRealUid();
            }

            return method.invoke(who, args);
         }
      });
      this.addMethodProxy(new ReplaceLastPkgMethodProxy("queryStatsForUser") {
         public Object call(Object who, Method method, Object... args) throws Throwable {
            replaceLastUserId(args);
            return super.call(who, method, args);
         }
      });
      this.addMethodProxy(new ReplaceLastPkgMethodProxy("queryExternalStatsForUser") {
         public Object call(Object who, Method method, Object... args) throws Throwable {
            replaceLastUserId(args);
            return super.call(who, method, args);
         }
      });
      this.addMethodProxy(new ReplaceLastPkgMethodProxy("queryStatsForUid"));
      this.addMethodProxy(new StaticMethodProxy("queryStatsForPackage") {
         public Object call(Object who, Method method, Object... args) throws Throwable {
            int packageNameIndex = ArrayUtils.indexOfFirst(args, String.class);
            int userIdIndex = ArrayUtils.indexOfLast(args, Integer.class);
            if (packageNameIndex != -1 && userIdIndex != -1) {
               String packageName = (String)args[packageNameIndex];
               int userId = (Integer)args[userIdIndex];
               return MountServiceStub.this.queryStatsForPackage(packageName, userId);
            } else {
               replaceLastUserId(args);
               return super.call(who, method, args);
            }
         }
      });
   }

   private StorageStats queryStatsForPackage(String packageName, int userId) {
      ApplicationInfo appInfo = VPackageManager.get().getApplicationInfo(packageName, 0, userId);
      if (appInfo == null) {
         throw new ParcelableException(new PackageManager.NameNotFoundException(packageName));
      } else {
         StorageStats stats = (StorageStats)mirror.android.app.usage.StorageStats.ctor.newInstance();
         mirror.android.app.usage.StorageStats.cacheBytes.set(stats, 0L);
         mirror.android.app.usage.StorageStats.codeBytes.set(stats, 0L);
         mirror.android.app.usage.StorageStats.dataBytes.set(stats, 0L);
         return stats;
      }
   }

   private static RefStaticMethod<IInterface> getInterfaceMethod() {
      return BuildCompat.isOreo() ? IStorageManager.Stub.asInterface : IMountService.Stub.asInterface;
   }
}
