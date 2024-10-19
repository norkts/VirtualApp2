package com.lody.virtual.server.accounts;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class VAccount implements Parcelable {
   public static final Parcelable.Creator<VAccount> CREATOR = new Parcelable.Creator<VAccount>() {
      public VAccount createFromParcel(Parcel source) {
         return new VAccount(source);
      }

      public VAccount[] newArray(int size) {
         return new VAccount[size];
      }
   };
   public int userId;
   public String name;
   public String previousName;
   public String type;
   public String password;
   public long lastAuthenticatedTime;
   public Map<String, String> authTokens;
   public Map<String, String> userDatas;

   public VAccount(int userId, Account account) {
      this.userId = userId;
      this.name = account.name;
      this.type = account.type;
      this.authTokens = new HashMap();
      this.userDatas = new HashMap();
   }

   public VAccount(Parcel in) {
      this.userId = in.readInt();
      this.name = in.readString();
      this.previousName = in.readString();
      this.type = in.readString();
      this.password = in.readString();
      this.lastAuthenticatedTime = in.readLong();
      int authTokensSize = in.readInt();
      this.authTokens = new HashMap(authTokensSize);

      int userDatasSize;
      String key;
      for(userDatasSize = 0; userDatasSize < authTokensSize; ++userDatasSize) {
         String key = in.readString();
         key = in.readString();
         this.authTokens.put(key, key);
      }

      userDatasSize = in.readInt();
      this.userDatas = new HashMap(userDatasSize);

      for(int i = 0; i < userDatasSize; ++i) {
         key = in.readString();
         String value = in.readString();
         this.userDatas.put(key, value);
      }

   }

   public int describeContents() {
      return 0;
   }

   public void writeToParcel(Parcel dest, int flags) {
      dest.writeInt(this.userId);
      dest.writeString(this.name);
      dest.writeString(this.previousName);
      dest.writeString(this.type);
      dest.writeString(this.password);
      dest.writeLong(this.lastAuthenticatedTime);
      dest.writeInt(this.authTokens.size());
      Iterator var3 = this.authTokens.entrySet().iterator();

      Map.Entry entry;
      while(var3.hasNext()) {
         entry = (Map.Entry)var3.next();
         dest.writeString((String)entry.getKey());
         dest.writeString((String)entry.getValue());
      }

      dest.writeInt(this.userDatas.size());
      var3 = this.userDatas.entrySet().iterator();

      while(var3.hasNext()) {
         entry = (Map.Entry)var3.next();
         dest.writeString((String)entry.getKey());
         dest.writeString((String)entry.getValue());
      }

   }
}
