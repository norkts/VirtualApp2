package com.android.dx.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;

public final class TwoColumnOutput {
   private final Writer out;
   private final int leftWidth;
   private final StringBuffer leftBuf;
   private final StringBuffer rightBuf;
   private final IndentingWriter leftColumn;
   private final IndentingWriter rightColumn;

   public static String toString(String s1, int width1, String spacer, String s2, int width2) {
      int len1 = s1.length();
      int len2 = s2.length();
      StringWriter sw = new StringWriter((len1 + len2) * 3);
      TwoColumnOutput twoOut = new TwoColumnOutput(sw, width1, width2, spacer);

      try {
         twoOut.getLeft().write(s1);
         twoOut.getRight().write(s2);
      } catch (IOException var10) {
         IOException ex = var10;
         throw new RuntimeException("shouldn't happen", ex);
      }

      twoOut.flush();
      return sw.toString();
   }

   public TwoColumnOutput(Writer out, int leftWidth, int rightWidth, String spacer) {
      if (out == null) {
         throw new NullPointerException("out == null");
      } else if (leftWidth < 1) {
         throw new IllegalArgumentException("leftWidth < 1");
      } else if (rightWidth < 1) {
         throw new IllegalArgumentException("rightWidth < 1");
      } else if (spacer == null) {
         throw new NullPointerException("spacer == null");
      } else {
         StringWriter leftWriter = new StringWriter(1000);
         StringWriter rightWriter = new StringWriter(1000);
         this.out = out;
         this.leftWidth = leftWidth;
         this.leftBuf = leftWriter.getBuffer();
         this.rightBuf = rightWriter.getBuffer();
         this.leftColumn = new IndentingWriter(leftWriter, leftWidth);
         this.rightColumn = new IndentingWriter(rightWriter, rightWidth, spacer);
      }
   }

   public TwoColumnOutput(OutputStream out, int leftWidth, int rightWidth, String spacer) {
      this((Writer)(new OutputStreamWriter(out)), leftWidth, rightWidth, spacer);
   }

   public Writer getLeft() {
      return this.leftColumn;
   }

   public Writer getRight() {
      return this.rightColumn;
   }

   public void flush() {
      try {
         appendNewlineIfNecessary(this.leftBuf, this.leftColumn);
         appendNewlineIfNecessary(this.rightBuf, this.rightColumn);
         this.outputFullLines();
         this.flushLeft();
         this.flushRight();
      } catch (IOException var2) {
         IOException ex = var2;
         throw new RuntimeException(ex);
      }
   }

   private void outputFullLines() throws IOException {
      while(true) {
         int leftLen = this.leftBuf.indexOf("\n");
         if (leftLen < 0) {
            return;
         }

         int rightLen = this.rightBuf.indexOf("\n");
         if (rightLen < 0) {
            return;
         }

         if (leftLen != 0) {
            this.out.write(this.leftBuf.substring(0, leftLen));
         }

         if (rightLen != 0) {
            writeSpaces(this.out, this.leftWidth - leftLen);
            this.out.write(this.rightBuf.substring(0, rightLen));
         }

         this.out.write(10);
         this.leftBuf.delete(0, leftLen + 1);
         this.rightBuf.delete(0, rightLen + 1);
      }
   }

   private void flushLeft() throws IOException {
      appendNewlineIfNecessary(this.leftBuf, this.leftColumn);

      while(this.leftBuf.length() != 0) {
         this.rightColumn.write(10);
         this.outputFullLines();
      }

   }

   private void flushRight() throws IOException {
      appendNewlineIfNecessary(this.rightBuf, this.rightColumn);

      while(this.rightBuf.length() != 0) {
         this.leftColumn.write(10);
         this.outputFullLines();
      }

   }

   private static void appendNewlineIfNecessary(StringBuffer buf, Writer out) throws IOException {
      int len = buf.length();
      if (len != 0 && buf.charAt(len - 1) != '\n') {
         out.write(10);
      }

   }

   private static void writeSpaces(Writer out, int amt) throws IOException {
      while(amt > 0) {
         out.write(32);
         --amt;
      }

   }
}
