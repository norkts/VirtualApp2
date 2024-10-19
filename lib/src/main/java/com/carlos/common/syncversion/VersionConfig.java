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
         HVLog.w("Couldn\'t find or open Version Config file " + permFile);
         return;
      }

      HVLog.i("Reading Version Config from " + permFile);
      XmlPullParser parser = Xml.newPullParser();

      try {
         parser.setInput(permReader);

         int type;
         while((type = parser.next()) != 2 && type != 1) {
         }

         if (type != 2) {
            throw new XmlPullParserException("No start tag found");
         }

         if (!parser.getName().equals("permissions") && !parser.getName().equals("config")) {
            throw new XmlPullParserException("Unexpected start tag in " + permFile + ": found " + parser.getName() + ", expected \'permissions\' or \'config\'");
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
                     if (name.equals("library")) {
                        var7 = 0;
                     }
                  default:
                     switch (var7) {
                        case 0:
                           String lname = parser.getAttributeValue((String)null, "name");
                           String lfile = parser.getAttributeValue((String)null, "file");
                           String ldependency = parser.getAttributeValue((String)null, "dependency");
                           if (lname == null) {
                              HVLog.w("<" + name + "> without name in " + permFile + " at " + parser.getPositionDescription());
                           } else if (lfile == null) {
                              HVLog.w("<" + name + "> without file in " + permFile + " at " + parser.getPositionDescription());
                           } else {
                              new SystemConfig.SharedLibraryEntry(lname, lfile, ldependency == null ? new String[0] : ldependency.split(":"));
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
