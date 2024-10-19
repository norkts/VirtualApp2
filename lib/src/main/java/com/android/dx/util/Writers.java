package com.android.dx.util;

import java.io.PrintWriter;
import java.io.Writer;

public final class Writers {
   private Writers() {
   }

   public static PrintWriter printWriterFor(Writer writer) {
      return writer instanceof PrintWriter ? (PrintWriter)writer : new PrintWriter(writer);
   }
}
