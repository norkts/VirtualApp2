package com.lody.virtual.client.hook.proxies.shortcut;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.text.TextUtils;
import android.util.ArraySet;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.VClient;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.hook.base.BinderInvocationProxy;
import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.client.hook.base.ReplaceFirstUserIdMethodProxy;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.compat.ParceledListSliceCompat;
import com.lody.virtual.helper.utils.BitmapUtils;
import com.lody.virtual.helper.utils.Reflect;
import com.lody.virtual.helper.utils.VLog;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import mirror.android.content.pm.IShortcutService;
import mirror.android.content.pm.ParceledListSlice;
import mirror.com.android.internal.infra.AndroidFuture;

@TargetApi(25)
public class ShortcutServiceStub extends BinderInvocationProxy {
   public ShortcutServiceStub() {
      super(IShortcutService.Stub.TYPE, "shortcut");
   }

   protected void onBindMethods() {
      super.onBindMethods();
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("disableShortcuts"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("enableShortcuts"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getRemainingCallCount"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getRateLimitResetTime"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getIconMaxDimensions"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getMaxShortcutCountPerActivity"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("reportShortcutUsed"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("onApplicationActive"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("hasShortcutHostPermission"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("removeAllDynamicShortcuts"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("removeDynamicShortcuts"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getShortcuts"));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("removeLongLivedShortcuts"));
      this.addMethodProxy(new WrapperShortcutInfo("pushDynamicShortcut", 1, (Object)null));
      this.addMethodProxy(new WrapperShortcutInfo("requestPinShortcut", 1, false));
      this.addMethodProxy(new UnWrapperShortcutInfo("getPinnedShortcuts"));
      this.addMethodProxy(new WrapperShortcutInfo("addDynamicShortcuts", 1, false));
      this.addMethodProxy(new WrapperShortcutInfo("setDynamicShortcuts", 1, false));
      this.addMethodProxy(new UnWrapperShortcutInfo("getDynamicShortcuts"));
      this.addMethodProxy(new WrapperShortcutInfo("createShortcutResultIntent", 1, (Object)null));
      this.addMethodProxy(new WrapperShortcutInfo("updateShortcuts", 1, false));
      this.addMethodProxy(new ReplaceCallingPkgMethodProxy("getManifestShortcuts") {
         public Object call(Object who, Method method, Object... args) {
            return ParceledListSliceCompat.create(new ArrayList());
         }
      });
      this.addMethodProxy(new ReplaceFirstUserIdMethodProxy("isRequestPinItemSupported"));
   }

   static ShortcutInfo wrapper(Context appContext, ShortcutInfo shortcutInfo, String pkg, int userId) {
      Icon icon = (Icon)Reflect.on((Object)shortcutInfo).opt("mIcon");
      Bitmap bmp;
      if (icon != null) {
         bmp = BitmapUtils.drawableToBitmap(icon.loadDrawable(appContext));
      } else {
         PackageManager pm = VirtualCore.get().getPackageManager();
         bmp = BitmapUtils.drawableToBitmap(appContext.getApplicationInfo().loadIcon(pm));
      }

      Intent proxyIntent = VirtualCore.get().wrapperShortcutIntent(shortcutInfo.getIntent(), (Intent)null, pkg, userId);
      proxyIntent.putExtra("_VA_|categories", setToString(shortcutInfo.getCategories()));
      proxyIntent.putExtra("_VA_|activity", shortcutInfo.getActivity());
      ShortcutInfo.Builder builder = new ShortcutInfo.Builder(VirtualCore.get().getContext(), pkg + "@" + userId + "/" + shortcutInfo.getId());
      if (shortcutInfo.getLongLabel() != null) {
         builder.setLongLabel(shortcutInfo.getLongLabel());
      }

      if (shortcutInfo.getShortLabel() != null) {
         builder.setShortLabel(shortcutInfo.getShortLabel());
      }

      builder.setIcon(Icon.createWithBitmap(bmp));
      builder.setIntent(proxyIntent);
      return builder.build();
   }

   static ShortcutInfo unWrapper(Context appContext, ShortcutInfo shortcutInfo, String _pkg, int _userId) throws URISyntaxException {
      Intent intent = shortcutInfo.getIntent();
      if (intent == null) {
         return null;
      } else {
         String pkg = intent.getStringExtra("_VA_|_pkg_");
         int userId = intent.getIntExtra("_VA_|_user_id_", 0);
         if (TextUtils.equals(pkg, _pkg) && userId == _userId) {
            String _id = shortcutInfo.getId();
            String id = _id.substring(_id.indexOf("/") + 1);
            Icon icon = (Icon)Reflect.on((Object)shortcutInfo).opt("mIcon");
            String uri = intent.getStringExtra("_VA_|_uri_");
            Intent targetIntent = null;
            if (!TextUtils.isEmpty(uri)) {
               targetIntent = Intent.parseUri(uri, 0);
            }

            ComponentName componentName = (ComponentName)intent.getParcelableExtra("_VA_|activity");
            String categories = intent.getStringExtra("_VA_|categories");
            ShortcutInfo.Builder builder = new ShortcutInfo.Builder(appContext, id);
            if (icon != null) {
               builder.setIcon(icon);
            }

            if (shortcutInfo.getLongLabel() != null) {
               builder.setLongLabel(shortcutInfo.getLongLabel());
            }

            if (shortcutInfo.getShortLabel() != null) {
               builder.setShortLabel(shortcutInfo.getShortLabel());
            }

            if (componentName != null) {
               builder.setActivity(componentName);
            }

            if (targetIntent != null) {
               builder.setIntent(targetIntent);
            }

            Set<String> cs = toSet(categories);
            if (cs != null) {
               builder.setCategories(cs);
            }

            return builder.build();
         } else {
            return null;
         }
      }
   }

   private static <T> String setToString(Set<T> sets) {
      if (sets == null) {
         return null;
      } else {
         StringBuilder stringBuilder = new StringBuilder();
         Iterator<T> iterator = sets.iterator();

         for(boolean first = true; iterator.hasNext(); stringBuilder.append(iterator.next())) {
            if (first) {
               first = false;
            } else {
               stringBuilder.append(",");
            }
         }

         return stringBuilder.toString();
      }
   }

   @TargetApi(23)
   private static Set<String> toSet(String allStr) {
      if (allStr == null) {
         return null;
      } else {
         String[] strs = allStr.split(",");
         Set<String> sets = new ArraySet();
         String[] var3 = strs;
         int var4 = strs.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String str = var3[var5];
            sets.add(str);
         }

         return sets;
      }
   }

   static class UnWrapperShortcutInfo extends ReplaceCallingPkgMethodProxy {
      public UnWrapperShortcutInfo(String name) {
         super(name);
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         Object parceledListSlice = super.call(who, method, args);
         if (parceledListSlice != null) {
            List<ShortcutInfo> result = new ArrayList();
            if (!getConfig().isAllowCreateShortcut()) {
               return ParceledListSliceCompat.create(result);
            } else {
               List list = (List)ParceledListSlice.getList.call(parceledListSlice);
               if (list != null) {
                  for(int i = list.size() - 1; i >= 0; --i) {
                     Object obj = list.get(i);
                     if (obj instanceof ShortcutInfo) {
                        ShortcutInfo info = (ShortcutInfo)obj;
                        ShortcutInfo target = ShortcutServiceStub.unWrapper(VClient.get().getCurrentApplication(), info, getAppPkg(), getAppUserId());
                        if (target != null) {
                           result.add(target);
                        }
                     }
                  }
               }

               return ParceledListSliceCompat.create(result);
            }
         } else {
            return null;
         }
      }
   }

   static class WrapperShortcutInfo extends ReplaceCallingPkgMethodProxy {
      private int infoIndex;
      private Object defValue;

      public WrapperShortcutInfo(String name, int index, Object defValue) {
         super(name);
         this.infoIndex = index;
         this.defValue = defValue;
      }

      private Object wrapperResult_(Object result) {
         if (!BuildCompat.isS()) {
            return result;
         } else {
            Object ret = AndroidFuture.ctor.newInstance();
            AndroidFuture.complete.call(ret, result);
            return ret;
         }
      }

      private Object wrapperResult(Method method, Object result) {
         VLog.i("wrapperResult", "method---" + method.toString());
         if (!method.toString().contains("AndroidFuture")) {
            return result;
         } else {
            Object ret = AndroidFuture.ctor.newInstance();
            AndroidFuture.complete.call(ret, result);
            return ret;
         }
      }

      public Object call(Object who, Method method, Object... args) throws Throwable {
         if (!getConfig().isAllowCreateShortcut()) {
            VLog.i("ShortcutServiceStub", "无创建快捷方式权限");
            return this.wrapperResult(method, this.defValue);
         } else {
            Object paramValue = args[this.infoIndex];
            if (paramValue == null) {
               return this.wrapperResult(method, this.defValue);
            } else {
               if (paramValue instanceof ShortcutInfo) {
                  ShortcutInfo shortcutInfo = (ShortcutInfo)paramValue;
                  args[this.infoIndex] = ShortcutServiceStub.wrapper(VClient.get().getCurrentApplication(), shortcutInfo, getAppPkg(), getAppUserId());
               } else {
                  List<ShortcutInfo> result = new ArrayList();

                  List list;
                  try {
                     list = (List)ParceledListSlice.getList.call(paramValue);
                  } catch (Throwable var11) {
                     return this.wrapperResult(method, this.defValue);
                  }

                  if (list != null) {
                     for(int i = list.size() - 1; i >= 0; --i) {
                        Object obj = list.get(i);
                        if (obj instanceof ShortcutInfo) {
                           ShortcutInfo info = (ShortcutInfo)obj;
                           ShortcutInfo target = ShortcutServiceStub.unWrapper(VClient.get().getCurrentApplication(), info, getAppPkg(), getAppUserId());
                           if (target != null) {
                              result.add(target);
                           }
                        }
                     }
                  }

                  args[this.infoIndex] = ParceledListSliceCompat.create(result);
               }

               return method.invoke(who, args);
            }
         }
      }
   }
}
