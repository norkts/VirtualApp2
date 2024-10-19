package de.robv.android.xposed;

import android.util.Log;
import dalvik.system.DexClassLoader;
import dalvik.system.DexFile;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class XposedInit {
   private static final String TAG = "SandXposed";
   private static final String INSTANT_RUN_CLASS = "com.android.tools.fd.runtime.BootstrapApplication";
   private static boolean disableResources = true;
   private static volatile AtomicBoolean bootstrapHooked = new AtomicBoolean(false);
   private static volatile AtomicBoolean modulesLoaded = new AtomicBoolean(false);

   private XposedInit() {
   }

   static void hookResources() throws Throwable {
   }

   private static boolean needsToCloseFilesForFork() {
      return false;
   }

   public static void loadModule(String modulePath, String moduleOdexDir, String moduleSoPath, ClassLoader topClassLoader) {
      if (!(new File(modulePath)).exists()) {
         Log.e("SandXposed", "  File does not exist");
      } else {
         DexFile dexFile;
         IOException exception;
         try {
            dexFile = new DexFile(modulePath);
         } catch (IOException var19) {
            exception = var19;
            Log.e("SandXposed", "  Cannot load module", exception);
            return;
         }

         if (dexFile.loadClass("com.android.tools.fd.runtime.BootstrapApplication", topClassLoader) != null) {
            Log.e("SandXposed", "  Cannot load module, please disable \"Instant Run\" in Android Studio.");
            XposedHelpers.closeSilently(dexFile);
         } else if (dexFile.loadClass(XposedBridge.class.getName(), topClassLoader) != null) {
            Log.e("SandXposed", "  Cannot load module:");
            Log.e("SandXposed", "  The Xposed API classes are compiled into the module's APK.");
            Log.e("SandXposed", "  This may cause strange issues and must be fixed by the module developer.");
            Log.e("SandXposed", "  For details, see: http://api.xposed.info/using.html");
            XposedHelpers.closeSilently(dexFile);
         } else {
            XposedHelpers.closeSilently(dexFile);
            exception = null;

            InputStream is;
            ZipFile zipFile = null;
            try {
               zipFile = new ZipFile(modulePath);
               ZipEntry zipEntry = zipFile.getEntry("assets/xposed_init");
               if (zipEntry == null) {
                  Log.e("SandXposed", "  assets/xposed_init not found in the APK");
                  XposedHelpers.closeSilently(zipFile);
                  return;
               }

               is = zipFile.getInputStream(zipEntry);
            } catch (IOException var23) {
               IOException e = var23;
               Log.e("SandXposed", "  Cannot read assets/xposed_init in the APK", e);
               XposedHelpers.closeSilently((ZipFile)zipFile);
               return;
            }

            ClassLoader mcl = new DexClassLoader(modulePath, moduleOdexDir, moduleSoPath, topClassLoader);
            BufferedReader moduleClassesReader = new BufferedReader(new InputStreamReader(is));

            try {
               String moduleClassName;
               try {
                  while((moduleClassName = moduleClassesReader.readLine()) != null) {
                     moduleClassName = moduleClassName.trim();
                     if (!moduleClassName.isEmpty() && !moduleClassName.startsWith("#")) {
                        try {
                           Log.i("SandXposed", "  Loading class " + moduleClassName);
                           Class<?> moduleClass = ((ClassLoader)mcl).loadClass(moduleClassName);
                           if (!IXposedMod.class.isAssignableFrom(moduleClass)) {
                              Log.e("SandXposed", "    This class doesn't implement any sub-interface of IXposedMod, skipping it");
                           } else if (disableResources && IXposedHookInitPackageResources.class.isAssignableFrom(moduleClass)) {
                              Log.e("SandXposed", "    This class requires resource-related hooks (which are disabled), skipping it.");
                           } else {
                              Object moduleInstance = moduleClass.newInstance();
                              if (moduleInstance instanceof IXposedHookZygoteInit) {
                                 IXposedHookZygoteInit.StartupParam param = new IXposedHookZygoteInit.StartupParam();
                                 param.modulePath = modulePath;
                                 param.startsSystemServer = false;
                                 ((IXposedHookZygoteInit)moduleInstance).initZygote(param);
                              }

                              if (moduleInstance instanceof IXposedHookLoadPackage) {
                                 XposedBridge.hookLoadPackage(new IXposedHookLoadPackage.Wrapper((IXposedHookLoadPackage)moduleInstance));
                              }

                              if (moduleInstance instanceof IXposedHookInitPackageResources) {
                                 throw new UnsupportedOperationException("can not hook resource!");
                              }
                           }
                        } catch (Throwable var20) {
                           Throwable t = var20;
                           Log.e("SandXposed", "    Failed to load class " + moduleClassName, t);
                        }
                     }
                  }
               } catch (IOException var21) {
                  IOException e = var21;
                  Log.e("SandXposed", "  Failed to load module from " + modulePath, e);
               }
            } finally {
               XposedHelpers.closeSilently((Closeable)is);
               XposedHelpers.closeSilently(zipFile);
            }

         }
      }
   }
}
