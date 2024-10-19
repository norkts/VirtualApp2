package com.android.multidex;

import com.android.dx.cf.direct.DirectClassFile;
import com.android.dx.cf.direct.StdAttributeFactory;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

class Path {
   List<ClassPathElement> elements = new ArrayList();
   private final String definition;
   private final ByteArrayOutputStream baos = new ByteArrayOutputStream(40960);
   private final byte[] readBuffer = new byte[20480];

   static ClassPathElement getClassPathElement(File file) throws ZipException, IOException {
      if (file.isDirectory()) {
         return new FolderPathElement(file);
      } else if (file.isFile()) {
         return new ArchivePathElement(new ZipFile(file));
      } else if (file.exists()) {
         throw new IOException("\"" + file.getPath() + "\" is not a directory neither a zip file");
      } else {
         throw new FileNotFoundException("File \"" + file.getPath() + "\" not found");
      }
   }

   Path(String definition) throws IOException {
      this.definition = definition;
      String[] var2 = definition.split(Pattern.quote(File.pathSeparator));
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String filePath = var2[var4];

         try {
            this.addElement(getClassPathElement(new File(filePath)));
         } catch (IOException var7) {
            IOException e = var7;
            throw new IOException("Wrong classpath: " + e.getMessage(), e);
         }
      }

   }

   private static byte[] readStream(InputStream in, ByteArrayOutputStream baos, byte[] readBuffer) throws IOException {
      try {
         while(true) {
            int amt = in.read(readBuffer);
            if (amt < 0) {
               return baos.toByteArray();
            }

            baos.write(readBuffer, 0, amt);
         }
      } finally {
         in.close();
      }
   }

   public String toString() {
      return this.definition;
   }

   Iterable<ClassPathElement> getElements() {
      return this.elements;
   }

   private void addElement(ClassPathElement element) {
      assert element != null;

      this.elements.add(element);
   }

   synchronized DirectClassFile getClass(String path) throws FileNotFoundException {
      DirectClassFile classFile = null;
      Iterator var3 = this.elements.iterator();

      while(var3.hasNext()) {
         ClassPathElement element = (ClassPathElement)var3.next();

         try {
            InputStream in = element.open(path);

            try {
               byte[] bytes = readStream(in, this.baos, this.readBuffer);
               this.baos.reset();
               classFile = new DirectClassFile(bytes, path, false);
               classFile.setAttributeFactory(StdAttributeFactory.THE_ONE);
               break;
            } finally {
               in.close();
            }
         } catch (IOException var11) {
         }
      }

      if (classFile == null) {
         throw new FileNotFoundException("File \"" + path + "\" not found");
      } else {
         return classFile;
      }
   }
}
