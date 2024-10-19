package com.android.dx.cf.iface;

import com.android.dx.util.FixedSizeList;

public final class StdAttributeList extends FixedSizeList implements AttributeList {
   public StdAttributeList(int size) {
      super(size);
   }

   public Attribute get(int n) {
      return (Attribute)this.get0(n);
   }

   public int byteLength() {
      int sz = this.size();
      int result = 2;

      for(int i = 0; i < sz; ++i) {
         result += this.get(i).byteLength();
      }

      return result;
   }

   public Attribute findFirst(String name) {
      int sz = this.size();

      for(int i = 0; i < sz; ++i) {
         Attribute att = this.get(i);
         if (att.getName().equals(name)) {
            return att;
         }
      }

      return null;
   }

   public Attribute findNext(Attribute attrib) {
      int sz = this.size();

      for(int at = 0; at < sz; ++at) {
         Attribute att = this.get(at);
         if (att == attrib) {
            String name = attrib.getName();
            ++at;

            while(at < sz) {
               Attribute attw = this.get(at);
               if (attw.getName().equals(name)) {
                  return att;
               }

               ++at;
            }

            return null;
         }
      }

      return null;
   }

   public void set(int n, Attribute attribute) {
      this.set0(n, attribute);
   }
}
