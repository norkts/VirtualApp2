package de.robv.android.xposed.services;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class DirectAccessService extends BaseService {
   public boolean hasDirectFileAccess() {
      return true;
   }

   public boolean checkFileAccess(String filename, int mode) {
      File file = new File(filename);
      if (mode == 0 && !file.exists()) {
         return false;
      } else if ((mode & 4) != 0 && !file.canRead()) {
         return false;
      } else if ((mode & 2) != 0 && !file.canWrite()) {
         return false;
      } else {
         return (mode & 1) == 0 || file.canExecute();
      }
   }

   public boolean checkFileExists(String filename) {
      return (new File(filename)).exists();
   }

   public FileResult statFile(String filename) throws IOException {
      File file = new File(filename);
      return new FileResult(file.length(), file.lastModified());
   }

   public byte[] readFile(String filename) throws IOException {
      File file = new File(filename);
      byte[] content = new byte[(int)file.length()];
      FileInputStream fis = new FileInputStream(file);
      fis.read(content);
      fis.close();
      return content;
   }

   public FileResult readFile(String filename, long previousSize, long previousTime) throws IOException {
      File file = new File(filename);
      long size = file.length();
      long time = file.lastModified();
      return previousSize == size && previousTime == time ? new FileResult(size, time) : new FileResult(this.readFile(filename), size, time);
   }

   public FileResult readFile(String filename, int offset, int length, long previousSize, long previousTime) throws IOException {
      File file = new File(filename);
      long size = file.length();
      long time = file.lastModified();
      if (previousSize == size && previousTime == time) {
         return new FileResult(size, time);
      } else if (offset <= 0 && length <= 0) {
         return new FileResult(this.readFile(filename), size, time);
      } else if (offset > 0 && (long)offset >= size) {
         throw new IllegalArgumentException("Offset " + offset + " is out of range for " + filename);
      } else {
         if (offset < 0) {
            offset = 0;
         }

         if (length > 0 && (long)(offset + length) > size) {
            throw new IllegalArgumentException("Length " + length + " is out of range for " + filename);
         } else {
            if (length <= 0) {
               length = (int)(size - (long)offset);
            }

            byte[] content = new byte[length];
            FileInputStream fis = new FileInputStream(file);
            fis.skip((long)offset);
            fis.read(content);
            fis.close();
            return new FileResult(content, size, time);
         }
      }
   }

   public InputStream getFileInputStream(String filename) throws IOException {
      return new BufferedInputStream(new FileInputStream(filename), 16384);
   }

   public FileResult getFileInputStream(String filename, long previousSize, long previousTime) throws IOException {
      File file = new File(filename);
      long size = file.length();
      long time = file.lastModified();
      return previousSize == size && previousTime == time ? new FileResult(size, time) : new FileResult(new BufferedInputStream(new FileInputStream(filename), 16384), size, time);
   }
}
