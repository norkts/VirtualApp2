package com.lody.virtual.client.badger;

import android.content.Intent;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.remote.BadgerInfo;
import java.util.HashMap;
import java.util.Map;

public class BadgerManager {
   private static final Map<String, IBadger> BADGERS = new HashMap(10);

   private static void addBadger(IBadger badger) {
      BADGERS.put(badger.getAction(), badger);
   }

   public static boolean handleBadger(Intent intent) {
      IBadger badger = (IBadger)BADGERS.get(intent.getAction());
      if (badger != null) {
         BadgerInfo info = badger.handleBadger(intent);
         VActivityManager.get().notifyBadgerChange(info);
         return true;
      } else {
         return false;
      }
   }

   static {
      addBadger(new BroadcastBadger1.AdwHomeBadger());
      addBadger(new BroadcastBadger1.AospHomeBadger());
      addBadger(new BroadcastBadger1.LGHomeBadger());
      addBadger(new BroadcastBadger1.NewHtcHomeBadger2());
      addBadger(new BroadcastBadger1.OPPOHomeBader());
      addBadger(new BroadcastBadger2.NewHtcHomeBadger1());
   }
}
