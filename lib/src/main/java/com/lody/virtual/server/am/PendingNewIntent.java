package com.lody.virtual.server.am;

import android.content.Intent;

public class PendingNewIntent {
   public String creator;
   public Intent intent;

   public PendingNewIntent(String creator, Intent intent) {
      this.creator = creator;
      this.intent = intent;
   }
}
