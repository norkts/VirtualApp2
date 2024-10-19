package com.lody.virtual.server.fs;

import android.os.ParcelFileDescriptor;
import com.lody.virtual.remote.FileInfo;
import java.io.File;

public class FileTransfer extends IFileTransfer.Stub {
   private static final FileTransfer sInstance = new FileTransfer();

   public static FileTransfer get() {
      return sInstance;
   }

   public FileInfo[] listFiles(String path) {
      File[] files = (new File(path)).listFiles();
      if (files == null) {
         return null;
      } else {
         FileInfo[] fileInfos = new FileInfo[files.length];

         for(int i = 0; i < files.length; ++i) {
            File file = files[i];
            fileInfos[i] = new FileInfo(file);
         }

         return fileInfos;
      }
   }

   public ParcelFileDescriptor openFile(String path) {
      try {
         return ParcelFileDescriptor.open(new File(path), 268435456);
      } catch (Exception var3) {
         Exception e = var3;
         e.printStackTrace();
         return null;
      }
   }
}
