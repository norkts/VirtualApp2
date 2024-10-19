package com.lody.virtual.server.pm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AppChangedCallbackList {
   private static final AppChangedCallbackList sInstance = new AppChangedCallbackList();
   private List<IAppChangedCallback> mList = new ArrayList(2);

   public static AppChangedCallbackList get() {
      return sInstance;
   }

   public void register(IAppChangedCallback callback) {
      this.mList.add(callback);
   }

   public void unregister(IAppChangedCallback callback) {
      this.mList.remove(callback);
   }

   void notifyCallbacks(boolean removed) {
      Iterator var2 = this.mList.iterator();

      while(var2.hasNext()) {
         IAppChangedCallback callback = (IAppChangedCallback)var2.next();
         callback.onCallback(removed);
      }

   }
}
