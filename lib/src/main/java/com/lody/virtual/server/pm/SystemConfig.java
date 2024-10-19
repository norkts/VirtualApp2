package com.lody.virtual.server.pm;

import android.util.Xml;
import com.lody.virtual.StringFog;
import com.lody.virtual.helper.utils.FileUtils;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.helper.utils.XmlUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class SystemConfig {
   private static final String TAG = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii0YKWwFNCNlJB42KD0cMw=="));
   private final Map<String, SharedLibraryEntry> mSharedLibraries = new HashMap();

   public void load() {
      long beforeTime = System.currentTimeMillis();
      File permissionsDir = new File(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My4uLGs3GgJiASw3KQgqL2wjNCZsIwZF")));
      this.readSharedLibraries(permissionsDir);
      long costTime = System.currentTimeMillis() - beforeTime;
      VLog.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IxgAP2gJIClgJyggPxg2MW8jBTJ4EVRF")) + costTime + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iwc1CA==")));
   }

   public SharedLibraryEntry getSharedLibrary(String name) {
      return (SharedLibraryEntry)this.mSharedLibraries.get(name);
   }

   private void readPermissionsFromXml(File permFile) {
      FileReader permReader;
      try {
         permReader = new FileReader(permFile);
      } catch (FileNotFoundException var16) {
         VLog.w(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ji4AI2oFMCZIJw08KD0cDmk3TSVsMzwcKQguKksVOCBlNFEiLAcYI2UjBiV7ASQwJhc1Lw==")) + permFile);
         return;
      }

      VLog.i(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ij4uP2gFAiZiICQsKAguD2wgAgNqAQYbKTo6ImEwAih5EVRF")) + permFile);
      XmlPullParser parser = Xml.newPullParser();

      try {
         parser.setInput(permReader);

         int type;
         while((type = parser.next()) != 2 && type != 1) {
         }

         if (type != 2) {
            throw new XmlPullParserException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Oz4fOG8wMDdhNw08LBciM34zHiVvARov")));
         }

         if (!parser.getName().equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhguKmoVAgNhJAY1Kj4qVg=="))) && !parser.getName().equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ACGgjAi0=")))) {
            throw new XmlPullParserException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IQgcM2kKICt9Jwo/KF4mL2UzQQRvVjw/LRglJGMKRCM=")) + permFile + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("ODo6PmowNCZiVyRF")) + parser.getName() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186M2kKICt9Jwo/KF4lM2ozGgRlAR46KT4YKWAzNyJ5Hlk7ODoIJWUjBjJlESMy")));
         }

         while(true) {
            XmlUtils.nextElement(parser);
            if (parser.getEventType() == 1) {
               break;
            }

            String name = parser.getName();
            if (name == null) {
               XmlUtils.skipCurrentTag(parser);
            } else {
               byte var7 = -1;
               switch (name.hashCode()) {
                  case 166208699:
                     if (name.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IxgYOm8jJARnAVRF")))) {
                        var7 = 0;
                     }
                  default:
                     switch (var7) {
                        case 0:
                           String lname = parser.getAttributeValue((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4+DWgVSFo=")));
                           String lfile = parser.getAttributeValue((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4YDmgVSFo=")));
                           String ldependency = parser.getAttributeValue((String)null, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRguKGgVBixiDlk5LQhSVg==")));
                           if (lname == null) {
                              VLog.w(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PxhSVg==")) + name + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pzo6LWUaMCBgJzAgPxcYOW8jBShqAR0r")) + permFile + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Phg+LHsFSFo=")) + parser.getPositionDescription());
                           } else if (lfile == null) {
                              VLog.w(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PxhSVg==")) + name + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pzo6LWUaMCBgJzAgPxc+MW8zBShqAR0r")) + permFile + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Phg+LHsFSFo=")) + parser.getPositionDescription());
                           } else {
                              SharedLibraryEntry entry = new SharedLibraryEntry(lname, lfile, ldependency == null ? new String[0] : ldependency.split(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OD5SVg=="))));
                              this.mSharedLibraries.put(lname, entry);
                           }

                           XmlUtils.skipCurrentTag(parser);
                           break;
                        default:
                           XmlUtils.skipCurrentTag(parser);
                     }
               }
            }
         }
      } catch (IOException | XmlPullParserException var17) {
      } finally {
         FileUtils.closeQuietly(permReader);
      }

   }

   public void readSharedLibraries(File permissionsDir) {
      File[] permissionFiles = permissionsDir.listFiles();
      if (permissionFiles != null) {
         File[] var3 = permissionFiles;
         int var4 = permissionFiles.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            File permissionFile = var3[var5];
            if (permissionFile.isFile() && permissionFile.getName().endsWith(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz1fDWoFSFo=")))) {
               this.readPermissionsFromXml(permissionFile);
            }
         }

      }
   }

   public static class SharedLibraryEntry {
      public String name;
      public String path;
      public String[] dependencies;

      public SharedLibraryEntry(String name, String path, String[] dependencies) {
         this.name = name;
         this.path = path;
         this.dependencies = dependencies;
      }
   }
}
