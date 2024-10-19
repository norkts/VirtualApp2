package com.carlos.common.imagepicker.entity;

public class ExifInfo {
   private int mExifOrientation;
   private int mExifDegrees;
   private int mExifTranslation;

   public ExifInfo(int exifOrientation, int exifDegrees, int exifTranslation) {
      this.mExifOrientation = exifOrientation;
      this.mExifDegrees = exifDegrees;
      this.mExifTranslation = exifTranslation;
   }

   public int getExifOrientation() {
      return this.mExifOrientation;
   }

   public int getExifDegrees() {
      return this.mExifDegrees;
   }

   public int getExifTranslation() {
      return this.mExifTranslation;
   }

   public void setExifOrientation(int exifOrientation) {
      this.mExifOrientation = exifOrientation;
   }

   public void setExifDegrees(int exifDegrees) {
      this.mExifDegrees = exifDegrees;
   }

   public void setExifTranslation(int exifTranslation) {
      this.mExifTranslation = exifTranslation;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ExifInfo exifInfo = (ExifInfo)o;
         if (this.mExifOrientation != exifInfo.mExifOrientation) {
            return false;
         } else if (this.mExifDegrees != exifInfo.mExifDegrees) {
            return false;
         } else {
            return this.mExifTranslation == exifInfo.mExifTranslation;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.mExifOrientation;
      result = 31 * result + this.mExifDegrees;
      result = 31 * result + this.mExifTranslation;
      return result;
   }
}
