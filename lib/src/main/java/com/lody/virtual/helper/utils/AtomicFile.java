package com.lody.virtual.helper.utils;

import android.util.Log;
import com.lody.virtual.StringFog;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AtomicFile {
   private final File mBaseName;
   private final File mBackupName;

   public AtomicFile(File baseName) {
      this.mBaseName = baseName;
      this.mBackupName = new File(baseName.getPath() + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Mz4MP2UzSFo=")));
   }

   public File getBaseFile() {
      return this.mBaseName;
   }

   public void delete() {
      this.mBaseName.delete();
      this.mBackupName.delete();
   }

   public FileOutputStream startWrite() throws IOException {
      if (this.mBaseName.exists()) {
         if (!this.mBackupName.exists()) {
            if (!this.mBaseName.renameTo(this.mBackupName)) {
               Log.w(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JgcqD2oVAilqNAYoKAhSVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ji4AI2oFMCZIJw08Iz0MDm4jPCt4ESQaLAgtJA==")) + this.mBaseName + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhcqD3sFFjd9JA4vI14mPGwjOCt4EVRF")) + this.mBackupName);
            }
         } else {
            this.mBaseName.delete();
         }
      }

      FileOutputStream str = null;

      try {
         str = new FileOutputStream(this.mBaseName);
      } catch (FileNotFoundException var6) {
         File parent = this.mBaseName.getParentFile();
         if (!parent.mkdir()) {
            throw new IOException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ji4AI2oFMCZIJw08Ly4uPW4gBit4ESwaKS4uJWYaAjVrCiBF")) + this.mBaseName);
         }

         try {
            str = new FileOutputStream(this.mBaseName);
         } catch (FileNotFoundException var5) {
            throw new IOException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ji4AI2oFMCZIJw08Ly4uPW4gBit4EVRF")) + this.mBaseName);
         }
      }

      return str;
   }

   public void finishWrite(FileOutputStream str) {
      if (str != null) {
         sync(str);

         try {
            str.close();
            this.mBackupName.delete();
         } catch (IOException var3) {
            IOException e = var3;
            Log.w(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JgcqD2oVAilqNAYoKAhSVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4YCGUaLCBuJywzLBcLIH4xEiVvVjwgLwg2J2EVFixsJBEz")), e);
         }
      }

   }

   public void failWrite(FileOutputStream str) {
      if (str != null) {
         sync(str);

         try {
            str.close();
            this.mBaseName.delete();
            this.mBackupName.renameTo(this.mBaseName);
         } catch (IOException var3) {
            IOException e = var3;
            Log.w(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JgcqD2oVAilqNAYoKAhSVg==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4+CWoIPARjAQo/PTkmWG8KAShrDlkqLhc6CmMKAil7N1RF")), e);
         }
      }

   }

   public FileInputStream openRead() throws FileNotFoundException {
      if (this.mBackupName.exists()) {
         this.mBaseName.delete();
         this.mBackupName.renameTo(this.mBaseName);
      }

      return new FileInputStream(this.mBaseName);
   }

   public byte[] readFully() throws IOException {
      FileInputStream stream = this.openRead();

      try {
         int pos = 0;
         int avail = stream.available();
         byte[] data = new byte[avail];

         while(true) {
            int amt = stream.read(data, pos, data.length - pos);
            byte[] newData;
            if (amt <= 0) {
               newData = data;
               return newData;
            }

            pos += amt;
            avail = stream.available();
            if (avail > data.length - pos) {
               newData = new byte[pos + avail];
               System.arraycopy(data, 0, newData, 0, pos);
               data = newData;
            }
         }
      } finally {
         stream.close();
      }
   }

   /** @deprecated */
   public void truncate() throws IOException {
      try {
         FileOutputStream fos = new FileOutputStream(this.mBaseName);
         fos.getFD().sync();
         fos.close();
      } catch (FileNotFoundException var2) {
         throw new IOException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ji4AI2oFMCZIJw08LwgmKmkjMCx4EVRF")) + this.mBaseName);
      } catch (IOException var3) {
      }

   }

   /** @deprecated */
   @Deprecated
   public FileOutputStream openAppend() throws IOException {
      try {
         return new FileOutputStream(this.mBaseName, true);
      } catch (FileNotFoundException var2) {
         throw new IOException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ji4AI2oFMCZIJw08LwgmKmkjMCx4EVRF")) + this.mBaseName);
      }
   }

   static boolean sync(FileOutputStream stream) {
      try {
         if (stream != null) {
            stream.getFD().sync();
         }

         return true;
      } catch (IOException var2) {
         return false;
      }
   }
}
