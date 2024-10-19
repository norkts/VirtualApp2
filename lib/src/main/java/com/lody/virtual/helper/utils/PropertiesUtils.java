package com.lody.virtual.helper.utils;

import com.lody.virtual.StringFog;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtils {
   private static final char[] hexDigit = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

   public static boolean load(Properties properties, File file) {
      if (properties != null && file != null && file.exists()) {
         FileInputStream inputStream = null;

         try {
            inputStream = new FileInputStream(file);
            properties.load(inputStream);
         } catch (Exception var7) {
         } finally {
            FileUtils.closeQuietly(inputStream);
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean save(Map properties, File file, String comments) {
      if (properties != null && file != null) {
         FileOutputStream outputStream = null;

         boolean var5;
         try {
            if (file.exists()) {
               file.delete();
            } else {
               File dir = file.getParentFile();
               if (!dir.exists()) {
                  dir.mkdirs();
               }

               file.createNewFile();
            }

            outputStream = new FileOutputStream(file);
            store(properties, outputStream, comments);
            return true;
         } catch (Exception var9) {
            var5 = false;
         } finally {
            FileUtils.closeQuietly(outputStream);
         }

         return var5;
      } else {
         return false;
      }
   }

   private static void store(Map properties, OutputStream out, String comments) throws IOException {
      store0(properties, new BufferedWriter(new OutputStreamWriter(out, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("OF5eI3kYBQE=")))), comments, true);
   }

   private static void store0(Map properties, BufferedWriter bw, String comments, boolean escUnicode) throws IOException {
      bw.newLine();
      if (comments != null) {
         writeComments(bw, comments);
      }

      synchronized(properties) {
         Iterator var5 = properties.keySet().iterator();

         while(true) {
            if (!var5.hasNext()) {
               break;
            }

            Object k = var5.next();
            String key = String.valueOf(k);
            String val = String.valueOf(properties.get(k));
            key = saveConvert(key, true, escUnicode);
            val = saveConvert(val, false, escUnicode);
            bw.write(key + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PwhSVg==")) + val);
            bw.newLine();
         }
      }

      bw.flush();
   }

   private static char toHex(int nibble) {
      return hexDigit[nibble & 15];
   }

   private static String saveConvert(String theString, boolean escapeSpace, boolean escapeUnicode) {
      int len = theString.length();
      int bufLen = len * 2;
      if (bufLen < 0) {
         bufLen = Integer.MAX_VALUE;
      }

      StringBuffer outBuffer = new StringBuffer(bufLen);

      for(int x = 0; x < len; ++x) {
         char aChar = theString.charAt(x);
         if (aChar > '=' && aChar < 127) {
            if (aChar == '\\') {
               outBuffer.append('\\');
               outBuffer.append('\\');
            } else {
               outBuffer.append(aChar);
            }
         } else {
            switch (aChar) {
               case '\t':
                  outBuffer.append('\\');
                  outBuffer.append('t');
                  continue;
               case '\n':
                  outBuffer.append('\\');
                  outBuffer.append('n');
                  continue;
               case '\f':
                  outBuffer.append('\\');
                  outBuffer.append('f');
                  continue;
               case '\r':
                  outBuffer.append('\\');
                  outBuffer.append('r');
                  continue;
               case ' ':
                  if (x == 0 || escapeSpace) {
                     outBuffer.append('\\');
                  }

                  outBuffer.append(' ');
                  continue;
               case '!':
               case '#':
               case ':':
               case '=':
                  outBuffer.append('\\');
                  outBuffer.append(aChar);
                  continue;
            }

            if ((aChar < ' ' || aChar > '~') & escapeUnicode) {
               outBuffer.append('\\');
               outBuffer.append('u');
               outBuffer.append(toHex(aChar >> 12 & 15));
               outBuffer.append(toHex(aChar >> 8 & 15));
               outBuffer.append(toHex(aChar >> 4 & 15));
               outBuffer.append(toHex(aChar & 15));
            } else {
               outBuffer.append(aChar);
            }
         }
      }

      return outBuffer.toString();
   }

   private static void writeComments(BufferedWriter bw, String comments) throws IOException {
      bw.write(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pi5SVg==")));
      int len = comments.length();
      int current = 0;
      int last = 0;

      for(char[] uu = new char[]{'\\', 'u', '\u0000', '\u0000', '\u0000', '\u0000'}; current < len; ++current) {
         char c = comments.charAt(current);
         if (c > 255 || c == '\n' || c == '\r') {
            if (last != current) {
               bw.write(comments.substring(last, current));
            }

            if (c > 255) {
               uu[2] = toHex(c >> 12 & 15);
               uu[3] = toHex(c >> 8 & 15);
               uu[4] = toHex(c >> 4 & 15);
               uu[5] = toHex(c & 15);
               bw.write(new String(uu));
            } else {
               bw.newLine();
               if (c == '\r' && current != len - 1 && comments.charAt(current + 1) == '\n') {
                  ++current;
               }

               if (current == len - 1 || comments.charAt(current + 1) != '#' && comments.charAt(current + 1) != '!') {
                  bw.write(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Pi5SVg==")));
               }
            }

            last = current + 1;
         }
      }

      if (last != current) {
         bw.write(comments.substring(last, current));
      }

      bw.newLine();
   }
}
