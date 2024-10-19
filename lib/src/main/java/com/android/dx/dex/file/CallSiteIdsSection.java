package com.android.dx.dex.file;

import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstCallSite;
import com.android.dx.rop.cst.CstCallSiteRef;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;

public final class CallSiteIdsSection extends UniformItemSection {
   private final TreeMap<CstCallSiteRef, CallSiteIdItem> callSiteIds = new TreeMap();
   private final TreeMap<CstCallSite, CallSiteItem> callSites = new TreeMap();

   public CallSiteIdsSection(DexFile dexFile) {
      super("call_site_ids", dexFile, 4);
   }

   public IndexedItem get(Constant cst) {
      if (cst == null) {
         throw new NullPointerException("cst == null");
      } else {
         this.throwIfNotPrepared();
         IndexedItem result = (IndexedItem)this.callSiteIds.get((CstCallSiteRef)cst);
         if (result == null) {
            throw new IllegalArgumentException("not found");
         } else {
            return result;
         }
      }
   }

   protected void orderItems() {
      int index = 0;
      Iterator var2 = this.callSiteIds.values().iterator();

      while(var2.hasNext()) {
         CallSiteIdItem callSiteId = (CallSiteIdItem)var2.next();
         callSiteId.setIndex(index++);
      }

   }

   public Collection<? extends Item> items() {
      return this.callSiteIds.values();
   }

   public synchronized void intern(CstCallSiteRef cstRef) {
      if (cstRef == null) {
         throw new NullPointerException("cstRef");
      } else {
         this.throwIfPrepared();
         CallSiteIdItem result = (CallSiteIdItem)this.callSiteIds.get(cstRef);
         if (result == null) {
            result = new CallSiteIdItem(cstRef);
            this.callSiteIds.put(cstRef, result);
         }

      }
   }

   void addCallSiteItem(CstCallSite callSite, CallSiteItem callSiteItem) {
      if (callSite == null) {
         throw new NullPointerException("callSite == null");
      } else if (callSiteItem == null) {
         throw new NullPointerException("callSiteItem == null");
      } else {
         this.callSites.put(callSite, callSiteItem);
      }
   }

   CallSiteItem getCallSiteItem(CstCallSite callSite) {
      if (callSite == null) {
         throw new NullPointerException("callSite == null");
      } else {
         return (CallSiteItem)this.callSites.get(callSite);
      }
   }
}
