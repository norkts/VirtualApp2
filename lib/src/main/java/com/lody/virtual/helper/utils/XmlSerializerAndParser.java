package com.lody.virtual.helper.utils;

import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public interface XmlSerializerAndParser<T> {
   void writeAsXml(T var1, XmlSerializer var2) throws IOException;

   T createFromXml(XmlPullParser var1) throws IOException, XmlPullParserException;
}
