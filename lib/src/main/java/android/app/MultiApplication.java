package android.app;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import com.carlos.libcommon.StringFog;
import com.lody.virtual.client.core.AppCallback;
import dalvik.system.DexClassLoader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public abstract class MultiApplication extends Application {
   private DexClassLoader pluginDexClassLoader;
   private Resources pluginResources;
   private PackageInfo pluginPackageArchiveInfo;
   private AssetManager assetManager;
   private Resources newResource;
   private Resources.Theme mTheme;
   private static MultiApplication multiApplication;

   public DexClassLoader getPluginDexClassLoader() {
      return this.pluginDexClassLoader;
   }

   protected void attachBaseContext(Context base) {
      super.attachBaseContext(base);
      multiApplication = this;
   }

   public void onCreate() {
      super.onCreate();
      String sha1 = getSHA1(this);
   }

   public static String getSHA1(Context context) {
      try {
         PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 64);
         byte[] cert = info.signatures[0].toByteArray();
         MessageDigest md = MessageDigest.getInstance(StringFog.decrypt("IC0zRw=="));
         byte[] publicKey = md.digest(cert);
         StringBuffer hexString = new StringBuffer();

         for(int i = 0; i < publicKey.length; ++i) {
            String appendString = Integer.toHexString(255 & publicKey[i]).toUpperCase(Locale.US);
            if (appendString.length() == 1) {
               hexString.append(StringFog.decrypt("Qw=="));
            }

            hexString.append(appendString);
            hexString.append(StringFog.decrypt("SQ=="));
         }

         String result = hexString.toString();
         return result.substring(0, result.length() - 1);
      } catch (PackageManager.NameNotFoundException var8) {
         var8.printStackTrace();
      } catch (NoSuchAlgorithmException var9) {
         NoSuchAlgorithmException e = var9;
         e.printStackTrace();
      }

      return null;
   }

   public abstract AppCallback getAppCallback();

   public void loadApk(Context context, String dexPath) {
      if (dexPath == null) {
         dexPath = StringFog.decrypt("XBYGGRcPOBZMCh8FBQ4aFgFdRkoNMB4TABwVBxtDFwAQAwJAPgMI");
      }

      this.pluginDexClassLoader = new DexClassLoader(dexPath, context.getDir(StringFog.decrypt("FwAK"), 0).getAbsolutePath(), (String)null, context.getClassLoader());
      this.pluginPackageArchiveInfo = context.getPackageManager().getPackageArchiveInfo(dexPath, 1);

      try {
         this.assetManager = (AssetManager)AssetManager.class.newInstance();
         Method addAssetPath = AssetManager.class.getMethod(StringFog.decrypt("EgEWNxYdOgczDgYY"), String.class);
         addAssetPath.invoke(this.assetManager, dexPath);
      } catch (InstantiationException var4) {
         InstantiationException e = var4;
         e.printStackTrace();
      } catch (IllegalAccessException var5) {
         IllegalAccessException e = var5;
         e.printStackTrace();
      } catch (NoSuchMethodException var6) {
         NoSuchMethodException e = var6;
         e.printStackTrace();
      } catch (InvocationTargetException var7) {
         InvocationTargetException e = var7;
         e.printStackTrace();
      }

      this.newResource = new Resources(this.assetManager, context.getResources().getDisplayMetrics(), context.getResources().getConfiguration());
      this.mTheme = this.newResource.newTheme();
      this.mTheme.setTo(context.getTheme());
   }

   public AssetManager getAssets() {
      return this.assetManager == null ? super.getAssets() : this.assetManager;
   }

   public Resources getResources() {
      if (this.newResource != null) {
      }

      return this.newResource == null ? super.getResources() : this.newResource;
   }

   public Resources.Theme getTheme() {
      return this.mTheme == null ? super.getTheme() : this.mTheme;
   }

   public static MultiApplication getMultiApplication() {
      return multiApplication;
   }
}
