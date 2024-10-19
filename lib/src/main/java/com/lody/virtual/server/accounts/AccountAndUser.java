package com.lody.virtual.server.accounts;

import android.accounts.Account;
import com.lody.virtual.StringFog;

public class AccountAndUser {
   public Account account;
   public int userId;

   public AccountAndUser(Account account, int userId) {
      this.account = account;
      this.userId = userId;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof AccountAndUser)) {
         return false;
      } else {
         AccountAndUser other = (AccountAndUser)o;
         return this.account.equals(other.account) && this.userId == other.userId;
      }
   }

   public int hashCode() {
      return this.account.hashCode() + this.userId;
   }

   public String toString() {
      return this.account.toString() + " u" + this.userId;
   }
}
