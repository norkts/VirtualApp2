package com.carlos.common.clouddisk.http;

import android.util.Log;
import com.carlos.libcommon.StringFog;
import java.io.File;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class FileProgressRequestBody extends RequestBody {
   private int segmentSize = 128;
   protected File file;
   protected ProgressListener listener;
   protected String contentType;
   private long fileLength;

   public FileProgressRequestBody(String contentType, File file, ProgressListener listener) {
      this.file = file;
      this.contentType = contentType;
      this.listener = listener;
      this.fileLength = this.contentLength();
      this.segmentSize = 2048;
      Log.d("789", this.fileLength + ":" + this.segmentSize);
   }

   protected FileProgressRequestBody() {
   }

   public long contentLength() {
      return this.file.length();
   }

   public MediaType contentType() {
      return MediaType.parse(this.contentType);
   }

   public void writeTo(BufferedSink sink) throws IOException {
      Source source = null;

      try {
         source = Okio.source(this.file);
         long total = 0L;

         long read;
         while((read = source.read(sink.buffer(), (long)this.segmentSize)) != -1L) {
            total += read;
            sink.flush();
            this.listener.transferred((double)total / (double)this.fileLength * 100.0);
         }
      } finally {
         Util.closeQuietly(source);
      }

   }

   public interface ProgressListener {
      void transferred(double var1);
   }
}
