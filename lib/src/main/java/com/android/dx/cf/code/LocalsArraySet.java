package com.android.dx.cf.code;

import com.android.dex.util.ExceptionWithContext;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.rop.type.Type;
import com.android.dx.rop.type.TypeBearer;
import com.android.dx.util.Hex;
import java.util.ArrayList;
import java.util.Iterator;

public class LocalsArraySet extends LocalsArray {
   private final OneLocalsArray primary;
   private final ArrayList<LocalsArray> secondaries;

   public LocalsArraySet(int maxLocals) {
      super(maxLocals != 0);
      this.primary = new OneLocalsArray(maxLocals);
      this.secondaries = new ArrayList();
   }

   public LocalsArraySet(OneLocalsArray primary, ArrayList<LocalsArray> secondaries) {
      super(primary.getMaxLocals() > 0);
      this.primary = primary;
      this.secondaries = secondaries;
   }

   private LocalsArraySet(LocalsArraySet toCopy) {
      super(toCopy.getMaxLocals() > 0);
      this.primary = toCopy.primary.copy();
      this.secondaries = new ArrayList(toCopy.secondaries.size());
      int sz = toCopy.secondaries.size();

      for(int i = 0; i < sz; ++i) {
         LocalsArray la = (LocalsArray)toCopy.secondaries.get(i);
         if (la == null) {
            this.secondaries.add((Object)null);
         } else {
            this.secondaries.add(la.copy());
         }
      }

   }

   public void setImmutable() {
      this.primary.setImmutable();
      Iterator var1 = this.secondaries.iterator();

      while(var1.hasNext()) {
         LocalsArray la = (LocalsArray)var1.next();
         if (la != null) {
            la.setImmutable();
         }
      }

      super.setImmutable();
   }

   public LocalsArray copy() {
      return new LocalsArraySet(this);
   }

   public void annotate(ExceptionWithContext ex) {
      ex.addContext("(locals array set; primary)");
      this.primary.annotate(ex);
      int sz = this.secondaries.size();

      for(int label = 0; label < sz; ++label) {
         LocalsArray la = (LocalsArray)this.secondaries.get(label);
         if (la != null) {
            ex.addContext("(locals array set: primary for caller " + Hex.u2(label) + ')');
            la.getPrimary().annotate(ex);
         }
      }

   }

   public String toHuman() {
      StringBuilder sb = new StringBuilder();
      sb.append("(locals array set; primary)\n");
      sb.append(this.getPrimary().toHuman());
      sb.append('\n');
      int sz = this.secondaries.size();

      for(int label = 0; label < sz; ++label) {
         LocalsArray la = (LocalsArray)this.secondaries.get(label);
         if (la != null) {
            sb.append("(locals array set: primary for caller " + Hex.u2(label) + ")\n");
            sb.append(la.getPrimary().toHuman());
            sb.append('\n');
         }
      }

      return sb.toString();
   }

   public void makeInitialized(Type type) {
      int len = this.primary.getMaxLocals();
      if (len != 0) {
         this.throwIfImmutable();
         this.primary.makeInitialized(type);
         Iterator var3 = this.secondaries.iterator();

         while(var3.hasNext()) {
            LocalsArray la = (LocalsArray)var3.next();
            if (la != null) {
               la.makeInitialized(type);
            }
         }

      }
   }

   public int getMaxLocals() {
      return this.primary.getMaxLocals();
   }

   public void set(int idx, TypeBearer type) {
      this.throwIfImmutable();
      this.primary.set(idx, type);
      Iterator var3 = this.secondaries.iterator();

      while(var3.hasNext()) {
         LocalsArray la = (LocalsArray)var3.next();
         if (la != null) {
            la.set(idx, type);
         }
      }

   }

   public void set(RegisterSpec spec) {
      this.set(spec.getReg(), spec);
   }

   public void invalidate(int idx) {
      this.throwIfImmutable();
      this.primary.invalidate(idx);
      Iterator var2 = this.secondaries.iterator();

      while(var2.hasNext()) {
         LocalsArray la = (LocalsArray)var2.next();
         if (la != null) {
            la.invalidate(idx);
         }
      }

   }

   public TypeBearer getOrNull(int idx) {
      return this.primary.getOrNull(idx);
   }

   public TypeBearer get(int idx) {
      return this.primary.get(idx);
   }

   public TypeBearer getCategory1(int idx) {
      return this.primary.getCategory1(idx);
   }

   public TypeBearer getCategory2(int idx) {
      return this.primary.getCategory2(idx);
   }

   private LocalsArraySet mergeWithSet(LocalsArraySet other) {
      boolean secondariesChanged = false;
      OneLocalsArray newPrimary = this.primary.merge(other.getPrimary());
      int sz1 = this.secondaries.size();
      int sz2 = other.secondaries.size();
      int sz = Math.max(sz1, sz2);
      ArrayList<LocalsArray> newSecondaries = new ArrayList(sz);

      for(int i = 0; i < sz; ++i) {
         LocalsArray la1 = i < sz1 ? (LocalsArray)this.secondaries.get(i) : null;
         LocalsArray la2 = i < sz2 ? (LocalsArray)other.secondaries.get(i) : null;
         LocalsArray resultla = null;
         if (la1 == la2) {
            resultla = la1;
         } else if (la1 == null) {
            resultla = la2;
         } else if (la2 == null) {
            resultla = la1;
         } else {
            try {
               resultla = la1.merge(la2);
            } catch (SimException var13) {
               SimException ex = var13;
               ex.addContext("Merging locals set for caller block " + Hex.u2(i));
            }
         }

         secondariesChanged = secondariesChanged || la1 != resultla;
         newSecondaries.add(resultla);
      }

      if (this.primary == newPrimary && !secondariesChanged) {
         return this;
      } else {
         return new LocalsArraySet(newPrimary, newSecondaries);
      }
   }

   private LocalsArraySet mergeWithOne(OneLocalsArray other) {
      boolean secondariesChanged = false;
      OneLocalsArray newPrimary = this.primary.merge(other.getPrimary());
      ArrayList<LocalsArray> newSecondaries = new ArrayList(this.secondaries.size());
      int sz = this.secondaries.size();

      for(int i = 0; i < sz; ++i) {
         LocalsArray la = (LocalsArray)this.secondaries.get(i);
         LocalsArray resultla = null;
         if (la != null) {
            try {
               resultla = la.merge(other);
            } catch (SimException var10) {
               SimException ex = var10;
               ex.addContext("Merging one locals against caller block " + Hex.u2(i));
            }
         }

         secondariesChanged = secondariesChanged || la != resultla;
         newSecondaries.add(resultla);
      }

      if (this.primary == newPrimary && !secondariesChanged) {
         return this;
      } else {
         return new LocalsArraySet(newPrimary, newSecondaries);
      }
   }

   public LocalsArraySet merge(LocalsArray other) {
      LocalsArraySet result;
      try {
         if (other instanceof LocalsArraySet) {
            result = this.mergeWithSet((LocalsArraySet)other);
         } else {
            result = this.mergeWithOne((OneLocalsArray)other);
         }
      } catch (SimException var4) {
         SimException ex = var4;
         ex.addContext("underlay locals:");
         this.annotate(ex);
         ex.addContext("overlay locals:");
         other.annotate(ex);
         throw ex;
      }

      result.setImmutable();
      return result;
   }

   private LocalsArray getSecondaryForLabel(int label) {
      return label >= this.secondaries.size() ? null : (LocalsArray)this.secondaries.get(label);
   }

   public LocalsArraySet mergeWithSubroutineCaller(LocalsArray other, int predLabel) {
      LocalsArray mine = this.getSecondaryForLabel(predLabel);
      OneLocalsArray newPrimary = this.primary.merge(other.getPrimary());
      LocalsArray newSecondary;
      if (mine == other) {
         newSecondary = mine;
      } else if (mine == null) {
         newSecondary = other;
      } else {
         newSecondary = mine.merge(other);
      }

      if (newSecondary == mine && newPrimary == this.primary) {
         return this;
      } else {
         newPrimary = null;
         int szSecondaries = this.secondaries.size();
         int sz = Math.max(predLabel + 1, szSecondaries);
         ArrayList<LocalsArray> newSecondaries = new ArrayList(sz);

         for(int i = 0; i < sz; ++i) {
            LocalsArray la = null;
            if (i == predLabel) {
               la = newSecondary;
            } else if (i < szSecondaries) {
               la = (LocalsArray)this.secondaries.get(i);
            }

            if (la != null) {
               if (newPrimary == null) {
                  newPrimary = la.getPrimary();
               } else {
                  newPrimary = newPrimary.merge(la.getPrimary());
               }
            }

            newSecondaries.add(la);
         }

         LocalsArraySet result = new LocalsArraySet(newPrimary, newSecondaries);
         result.setImmutable();
         return result;
      }
   }

   public LocalsArray subArrayForLabel(int subLabel) {
      LocalsArray result = this.getSecondaryForLabel(subLabel);
      return result;
   }

   protected OneLocalsArray getPrimary() {
      return this.primary;
   }
}
