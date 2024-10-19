package com.android.multidex;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

class ArchivePathElement implements ClassPathElement {
   private final ZipFile archive;

   public ArchivePathElement(ZipFile archive) {
      this.archive = archive;
   }

   public InputStream open(String path) throws IOException {
      ZipEntry entry = this.archive.getEntry(path);
      if (entry == null) {
         throw new FileNotFoundException("File \"" + path + "\" not found");
      } else if (entry.isDirectory()) {
         throw new DirectoryEntryException();
      } else {
         return this.archive.getInputStream(entry);
      }
   }

   public void close() throws IOException {
      this.archive.close();
   }

   public Iterable<String> list() {
      return new Iterable<String>() {
         public Iterator<String> iterator() {
            return new Iterator<String>() {
               Enumeration<? extends ZipEntry> delegate;
               ZipEntry next;

               {
                  this.delegate = ArchivePathElement.this.archive.entries();
                  this.next = null;
               }

               public boolean hasNext() {
                  while(this.next == null && this.delegate.hasMoreElements()) {
                     this.next = (ZipEntry)this.delegate.nextElement();
                     if (this.next.isDirectory()) {
                        this.next = null;
                     }
                  }

                  return this.next != null;
               }

               public String next() {
                  if (this.hasNext()) {
                     String name = this.next.getName();
                     this.next = null;
                     return name;
                  } else {
                     throw new NoSuchElementException();
                  }
               }

               public void remove() {
                  throw new UnsupportedOperationException();
               }
            };
         }
      };
   }

   static class DirectoryEntryException extends IOException {
   }
}
