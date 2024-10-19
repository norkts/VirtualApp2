package com.android.dx.dex.file;

import com.android.dex.DexIndexOverflowException;
import java.util.Formatter;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class MemberIdsSection extends UniformItemSection {
   public MemberIdsSection(String name, DexFile file) {
      super(name, file, 4);
   }

   protected void orderItems() {
      int idx = 0;
      if (this.items().size() > 65536) {
         throw new DexIndexOverflowException(this.getTooManyMembersMessage());
      } else {
         for(Iterator var2 = this.items().iterator(); var2.hasNext(); ++idx) {
            Object i = var2.next();
            ((MemberIdItem)i).setIndex(idx);
         }

      }
   }

   private String getTooManyMembersMessage() {
      Map<String, AtomicInteger> membersByPackage = new TreeMap();

      String packageName;
      AtomicInteger count;
      for(Iterator var2 = this.items().iterator(); var2.hasNext(); count.incrementAndGet()) {
         Object member = var2.next();
         packageName = ((MemberIdItem)member).getDefiningClass().getPackageName();
         count = (AtomicInteger)membersByPackage.get(packageName);
         if (count == null) {
            count = new AtomicInteger();
            membersByPackage.put(packageName, count);
         }
      }

      Formatter formatter = new Formatter();

      try {
         String memberType = this instanceof MethodIdsSection ? "method" : "field";
         formatter.format("Too many %1$s references to fit in one dex file: %2$d; max is %3$d.%nYou may try using multi-dex. If multi-dex is enabled then the list of classes for the main dex list is too large.%nReferences by package:", memberType, this.items().size(), 65536);
         Iterator var11 = membersByPackage.entrySet().iterator();

         while(var11.hasNext()) {
            Map.Entry<String, AtomicInteger> entry = (Map.Entry)var11.next();
            formatter.format("%n%6d %s", ((AtomicInteger)entry.getValue()).get(), entry.getKey());
         }

         packageName = formatter.toString();
         return packageName;
      } finally {
         formatter.close();
      }
   }
}
