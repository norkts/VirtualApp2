package com.lody.virtual.remote;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingRuleInfo implements Parcelable {
   public int rule;
   public String word;
   public boolean regex;
   private transient Pattern pattern;
   public static final Parcelable.Creator<SettingRuleInfo> CREATOR = new Parcelable.Creator<SettingRuleInfo>() {
      public SettingRuleInfo createFromParcel(Parcel source) {
         return new SettingRuleInfo(source);
      }

      public SettingRuleInfo[] newArray(int size) {
         return new SettingRuleInfo[size];
      }
   };

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         SettingRuleInfo that = (SettingRuleInfo)o;
         return this.rule == that.rule && this.regex == that.regex && TextUtils.equals(this.word, that.word);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Arrays.hashCode(new Object[]{this.rule, this.word, this.regex});
   }

   public boolean matches(String packageName) {
      if (!this.regex) {
         return TextUtils.equals(packageName, this.word);
      } else {
         try {
            if (this.pattern == null) {
               this.pattern = Pattern.compile(this.word);
            }

            Matcher m = this.pattern.matcher(packageName);
            return m.matches();
         } catch (Exception var3) {
            return false;
         }
      }
   }

   public int describeContents() {
      return 0;
   }

   public void writeToParcel(Parcel dest, int flags) {
      dest.writeInt(this.rule);
      dest.writeString(this.word);
      dest.writeByte((byte)(this.regex ? 1 : 0));
   }

   public SettingRuleInfo() {
   }

   public SettingRuleInfo(int rule, String word, boolean regex) {
      this.rule = rule;
      this.word = word;
      this.regex = regex;
   }

   protected SettingRuleInfo(Parcel in) {
      this.rule = in.readInt();
      this.word = in.readString();
      this.regex = in.readByte() != 0;
   }
}
