package com.lody.virtual.helper.utils;

import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class XmlUtils {
   public static void skipCurrentTag(XmlPullParser parser) throws XmlPullParserException, IOException {
      int outerDepth = parser.getDepth();

      int type;
      while((type = parser.next()) != 1) {
         if (type == 3 && parser.getDepth() <= outerDepth) {
            break;
         }
      }

   }

   public static void nextElement(XmlPullParser parser) throws XmlPullParserException, IOException {
      int type;
      while((type = parser.next()) != 2 && type != 1) {
      }

   }
}
