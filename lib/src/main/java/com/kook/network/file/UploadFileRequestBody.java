package com.kook.network.file;

import com.kook.network.StringFog;
import java.io.File;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.MultipartBody.Part;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

public class UploadFileRequestBody extends RequestBody {
   private final RequestBody mRequestBody;
   private FileUploadObserver<ResponseBody> mFileUploadObserver;

   public UploadFileRequestBody(File file, FileUploadObserver<ResponseBody> fileUploadObserver) {
      this.mRequestBody = RequestBody.create(MediaType.parse(StringFog.decrypt("BhoDH0QSFBVZSQ8XGQJCD0wWFA==")), file);
      MultipartBody.Part body = Part.createFormData(StringFog.decrypt("DQYDDg=="), file.getName(), this.mRequestBody);
      this.mFileUploadObserver = fileUploadObserver;
   }

   public MediaType contentType() {
      return this.mRequestBody.contentType();
   }

   public long contentLength() throws IOException {
      return this.mRequestBody.contentLength();
   }

   public void writeTo(BufferedSink sink) throws IOException {
      CountingSink countingSink = new CountingSink(sink);
      BufferedSink bufferedSink = Okio.buffer(countingSink);
      this.mRequestBody.writeTo(bufferedSink);
      bufferedSink.flush();
   }

   protected final class CountingSink extends ForwardingSink {
      private long bytesWritten = 0L;

      public CountingSink(Sink delegate) {
         super(delegate);
      }

      public void write(Buffer source, long byteCount) throws IOException {
         super.write(source, byteCount);
         this.bytesWritten += byteCount;
         if (UploadFileRequestBody.this.mFileUploadObserver != null) {
            UploadFileRequestBody.this.mFileUploadObserver.onProgressChange(this.bytesWritten, UploadFileRequestBody.this.contentLength());
         }

      }
   }
}
