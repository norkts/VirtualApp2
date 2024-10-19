package com.carlos.common.syncversion;

import android.util.Xml;
import com.carlos.libcommon.StringFog;
import com.kook.common.utils.HVLog;
import com.lody.virtual.helper.utils.FileUtils;
import com.lody.virtual.helper.utils.XmlUtils;
import com.lody.virtual.server.pm.SystemConfig;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class VersionConfig {
   private void readPermissionsFromXml(File permFile) {
      FileReader permReader;
      try {
         permReader = new FileReader(permFile);
      } catch (FileNotFoundException var16) {
         HVLog.w(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ji4AI2oFMCZIJw08KD0cDmk3TSVsMzwcKQguKksbICBlNywiKQgpJGAjGgRoJx4yDRc6IGwaETY=")) + permFile);
         return;
      }

      HVLog.i(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ij4uP2gFAiZiICRJKAguL2wjNCZ4HzAcLC4iI2IkOCFlNFkcOD5SVg==")) + permFile);
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
                              HVLog.w(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PxhSVg==")) + name + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pzo6LWUaMCBgJzAgPxcYOW8jBShqAR0r")) + permFile + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Phg+LHsFSFo=")) + parser.getPositionDescription());
                           } else if (lfile == null) {
                              HVLog.w(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PxhSVg==")) + name + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pzo6LWUaMCBgJzAgPxc+MW8zBShqAR0r")) + permFile + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Phg+LHsFSFo=")) + parser.getPositionDescription());
                           } else {
                              new SystemConfig.SharedLibraryEntry(lname, lfile, ldependency == null ? new String[0] : ldependency.split(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OD5SVg=="))));
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
}
