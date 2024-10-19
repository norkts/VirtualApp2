package com.carlos.science.stebcore;

public class IndicatorItem {
   private String mTitle;

   public void doing() {
      this.setStatus(IndicatorItem.Status.DOING);
   }

   public void skip() {
      this.setStatus(IndicatorItem.Status.SKIP);
   }

   public void complete() {
      this.setStatus(IndicatorItem.Status.COMPLETE);
   }

   private void setStatus(Status status) {
   }

   private static enum Status {
      NORMAL(1, 2),
      DOING(3, 4),
      SKIP(5, 6),
      COMPLETE(7, 8);

      private int mmTextColor;
      private int mmIcon;

      private Status(int textColorId, int icon) {
         this.mmTextColor = textColorId;
         this.mmIcon = icon;
      }

      public int getTextColorId() {
         return this.mmTextColor;
      }

      public int getIconResId() {
         return this.mmIcon;
      }
   }
}
