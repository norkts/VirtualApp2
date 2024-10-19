package com.kook.network.file;

import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public class DownloadFileResponseBody extends ResponseBody {
   private ResponseBody mResponseBody;
   private IDownloadListener mListener;
   private BufferedSource mBufferedSource;

   public DownloadFileResponseBody(ResponseBody responseBody, IDownloadListener listener) {
      this.mResponseBody = responseBody;
      this.mListener = listener;
   }

   public MediaType contentType() {
      return this.mResponseBody == null ? null : this.mResponseBody.contentType();
   }

   public long contentLength() {
      return this.mResponseBody == null ? 0L : this.mResponseBody.contentLength();
   }

   public BufferedSource source() {
      if (this.mBufferedSource == null && this.mResponseBody != null) {
         this.mBufferedSource = Okio.buffer(this.source(this.mResponseBody.source()));
      }

      return this.mBufferedSource;
   }

   private Source source(Source source) {
      return new ForwardingSource(source) {
         long totalBytesRead = 0L;

         public long read(Buffer sink, long byteCount) throws IOException {
            long bytesRead = super.read(sink, byteCount);
            this.totalBytesRead += bytesRead != -1L ? bytesRead : 0L;
            if (DownloadFileResponseBody.this.mListener != null && bytesRead != -1L) {
               DownloadFileResponseBody.this.mListener.onProgress((int)(this.totalBytesRead * 100L / DownloadFileResponseBody.this.contentLength()));
            }

            return bytesRead;
         }
      };
   }
}
