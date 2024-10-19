package com.android.multidex;

import java.io.IOException;
import java.io.InputStream;

interface ClassPathElement {
   char SEPARATOR_CHAR = '/';

   InputStream open(String var1) throws IOException;

   void close() throws IOException;

   Iterable<String> list();
}
