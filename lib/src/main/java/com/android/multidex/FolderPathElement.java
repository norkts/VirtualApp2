package com.android.multidex;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

class FolderPathElement implements ClassPathElement {
   private final File baseFolder;

   public FolderPathElement(File baseFolder) {
      this.baseFolder = baseFolder;
   }

   public InputStream open(String path) throws FileNotFoundException {
      return new FileInputStream(new File(this.baseFolder, path.replace('/', File.separatorChar)));
   }

   public void close() {
   }

   public Iterable<String> list() {
      ArrayList<String> result = new ArrayList();
      this.collect(this.baseFolder, "", result);
      return result;
   }

   private void collect(File folder, String prefix, ArrayList<String> result) {
      File[] var4 = folder.listFiles();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         File file = var4[var6];
         if (file.isDirectory()) {
            this.collect(file, prefix + '/' + file.getName(), result);
         } else {
            result.add(prefix + '/' + file.getName());
         }
      }

   }
}
