package com.android.dx.cf.direct;

import com.android.dex.util.FileUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ClassPathOpener {
   private final String pathname;
   private final Consumer consumer;
   private final boolean sort;
   private FileNameFilter filter;
   public static final FileNameFilter acceptAll = new FileNameFilter() {
      public boolean accept(String path) {
         return true;
      }
   };

   public ClassPathOpener(String pathname, boolean sort, Consumer consumer) {
      this(pathname, sort, acceptAll, consumer);
   }

   public ClassPathOpener(String pathname, boolean sort, FileNameFilter filter, Consumer consumer) {
      this.pathname = pathname;
      this.sort = sort;
      this.consumer = consumer;
      this.filter = filter;
   }

   public boolean process() {
      File file = new File(this.pathname);
      return this.processOne(file, true);
   }

   private boolean processOne(File file, boolean topLevel) {
      try {
         if (file.isDirectory()) {
            return this.processDirectory(file, topLevel);
         } else {
            String path = file.getPath();
            if (!path.endsWith(".zip") && !path.endsWith(".jar") && !path.endsWith(".apk")) {
               if (this.filter.accept(path)) {
                  byte[] bytes = FileUtils.readFile(file);
                  return this.consumer.processFileBytes(path, file.lastModified(), bytes);
               } else {
                  return false;
               }
            } else {
               return this.processArchive(file);
            }
         }
      } catch (Exception var5) {
         Exception ex = var5;
         this.consumer.onException(ex);
         return false;
      }
   }

   private static int compareClassNames(String a, String b) {
      a = a.replace('$', '0');
      b = b.replace('$', '0');
      a = a.replace("package-info", "");
      b = b.replace("package-info", "");
      return a.compareTo(b);
   }

   private boolean processDirectory(File dir, boolean topLevel) {
      if (topLevel) {
         dir = new File(dir, ".");
      }

      File[] files = dir.listFiles();
      int len = files.length;
      boolean any = false;
      if (this.sort) {
         Arrays.sort(files, new Comparator<File>() {
            public int compare(File a, File b) {
               return ClassPathOpener.compareClassNames(a.getName(), b.getName());
            }
         });
      }

      for(int i = 0; i < len; ++i) {
         any |= this.processOne(files[i], false);
      }

      return any;
   }

   private boolean processArchive(File file) throws IOException {
      ZipFile zip = new ZipFile(file);
      ArrayList<? extends ZipEntry> entriesList = Collections.list(zip.entries());
      if (this.sort) {
         Collections.sort(entriesList, new Comparator<ZipEntry>() {
            public int compare(ZipEntry a, ZipEntry b) {
               return ClassPathOpener.compareClassNames(a.getName(), b.getName());
            }
         });
      }

      this.consumer.onProcessArchiveStart(file);
      ByteArrayOutputStream baos = new ByteArrayOutputStream(40000);
      byte[] buf = new byte[20000];
      boolean any = false;
      Iterator var7 = entriesList.iterator();

      while(true) {
         ZipEntry one;
         boolean isDirectory;
         String path;
         do {
            if (!var7.hasNext()) {
               zip.close();
               return any;
            }

            one = (ZipEntry)var7.next();
            isDirectory = one.isDirectory();
            path = one.getName();
         } while(!this.filter.accept(path));

         byte[] bytes;
         if (isDirectory) {
            bytes = new byte[0];
         } else {
            InputStream in = zip.getInputStream(one);
            baos.reset();

            int read;
            while((read = in.read(buf)) != -1) {
               baos.write(buf, 0, read);
            }

            in.close();
            bytes = baos.toByteArray();
         }

         any |= this.consumer.processFileBytes(path, one.getTime(), bytes);
      }
   }

   public interface FileNameFilter {
      boolean accept(String var1);
   }

   public interface Consumer {
      boolean processFileBytes(String var1, long var2, byte[] var4);

      void onException(Exception var1);

      void onProcessArchiveStart(File var1);
   }
}
