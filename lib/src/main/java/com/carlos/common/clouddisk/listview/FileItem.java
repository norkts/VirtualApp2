package com.carlos.common.clouddisk.listview;

import com.carlos.libcommon.StringFog;

public class FileItem {
   public static final int ISFILE = 0;
   public static final int ISHOLDER = 1;
   private String filename;
   private int fileORHolder;
   private String id;
   private boolean isCheck = false;
   private String downs;
   private String time;
   private String sizes;
   private String fileUrl;

   public FileItem(String filename, int fileORHolder, String id) {
      this.filename = filename;
      this.fileORHolder = fileORHolder;
      this.id = id;
      this.isCheck = false;
   }

   public String getDowns() {
      return this.downs;
   }

   public void setDowns(String downs) {
      this.downs = downs;
   }

   public String getTime() {
      return this.time;
   }

   public void setTime(String time) {
      this.time = time;
   }

   public String getSizes() {
      return this.sizes;
   }

   public void setSizes(String sizes) {
      this.sizes = sizes;
   }

   public String getFileUrl() {
      return this.fileUrl;
   }

   public void setFileUrl(String fileUrl) {
      this.fileUrl = fileUrl;
   }

   public String getFilename() {
      return this.filename;
   }

   public void setFilename(String filename) {
      this.filename = filename;
   }

   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public int getFileORHolder() {
      return this.fileORHolder;
   }

   public void setFileORHolder(int fileORHolder) {
      this.fileORHolder = fileORHolder;
   }

   public boolean getIsCheck() {
      return this.isCheck;
   }

   public void setIsCheck(boolean isCheck) {
      this.isCheck = isCheck;
   }

   public String toString() {
      return StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JT4YDmgbAgZiDl0hKD0cCGkjMDdlASsoPj5SVg==")) + this.filename + '\'' + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186PmUVHitoJSwKKi1bPmkgRDM=")) + this.fileORHolder + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186CWgOHS0=")) + this.id + '\'' + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186CW8xLCBiDigxPghSVg==")) + this.isCheck + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186PGowPCZhI1w9")) + this.downs + '\'' + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186LGUVEit0CjhF")) + this.time + '\'' + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186KWUaTSthI1w9")) + this.sizes + '\'' + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186PmUVHituASwoPgM6Vg==")) + this.fileUrl + '\'' + '}';
   }
}
