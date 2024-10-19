package com.android.dx.rop.code;

import com.android.dx.util.MutabilityControl;

public final class RegisterSpecSet extends MutabilityControl {
   public static final RegisterSpecSet EMPTY = new RegisterSpecSet(0);
   private final RegisterSpec[] specs;
   private int size;

   public RegisterSpecSet(int maxSize) {
      super(maxSize != 0);
      this.specs = new RegisterSpec[maxSize];
      this.size = 0;
   }

   public boolean equals(Object other) {
      if (!(other instanceof RegisterSpecSet)) {
         return false;
      } else {
         RegisterSpecSet otherSet = (RegisterSpecSet)other;
         RegisterSpec[] otherSpecs = otherSet.specs;
         int len = this.specs.length;
         if (len == otherSpecs.length && this.size() == otherSet.size()) {
            for(int i = 0; i < len; ++i) {
               RegisterSpec s1 = this.specs[i];
               RegisterSpec s2 = otherSpecs[i];
               if (s1 != s2 && (s1 == null || !s1.equals(s2))) {
                  return false;
               }
            }

            return true;
         } else {
            return false;
         }
      }
   }

   public int hashCode() {
      int len = this.specs.length;
      int hash = 0;

      for(int i = 0; i < len; ++i) {
         RegisterSpec spec = this.specs[i];
         int oneHash = spec == null ? 0 : spec.hashCode();
         hash = hash * 31 + oneHash;
      }

      return hash;
   }

   public String toString() {
      int len = this.specs.length;
      StringBuilder sb = new StringBuilder(len * 25);
      sb.append('{');
      boolean any = false;

      for(int i = 0; i < len; ++i) {
         RegisterSpec spec = this.specs[i];
         if (spec != null) {
            if (any) {
               sb.append(", ");
            } else {
               any = true;
            }

            sb.append(spec);
         }
      }

      sb.append('}');
      return sb.toString();
   }

   public int getMaxSize() {
      return this.specs.length;
   }

   public int size() {
      int result = this.size;
      if (result < 0) {
         int len = this.specs.length;
         result = 0;

         for(int i = 0; i < len; ++i) {
            if (this.specs[i] != null) {
               ++result;
            }
         }

         this.size = result;
      }

      return result;
   }

   public RegisterSpec get(int reg) {
      try {
         return this.specs[reg];
      } catch (ArrayIndexOutOfBoundsException var3) {
         throw new IllegalArgumentException("bogus reg");
      }
   }

   public RegisterSpec get(RegisterSpec spec) {
      return this.get(spec.getReg());
   }

   public RegisterSpec findMatchingLocal(RegisterSpec spec) {
      int length = this.specs.length;

      for(int reg = 0; reg < length; ++reg) {
         RegisterSpec s = this.specs[reg];
         if (s != null && spec.matchesVariable(s)) {
            return s;
         }
      }

      return null;
   }

   public RegisterSpec localItemToSpec(LocalItem local) {
      int length = this.specs.length;

      for(int reg = 0; reg < length; ++reg) {
         RegisterSpec spec = this.specs[reg];
         if (spec != null && local.equals(spec.getLocalItem())) {
            return spec;
         }
      }

      return null;
   }

   public void remove(RegisterSpec toRemove) {
      try {
         this.specs[toRemove.getReg()] = null;
         this.size = -1;
      } catch (ArrayIndexOutOfBoundsException var3) {
         throw new IllegalArgumentException("bogus reg");
      }
   }

   public void put(RegisterSpec spec) {
      this.throwIfImmutable();
      if (spec == null) {
         throw new NullPointerException("spec == null");
      } else {
         this.size = -1;

         try {
            int reg = spec.getReg();
            this.specs[reg] = spec;
            if (reg > 0) {
               int prevReg = reg - 1;
               RegisterSpec prevSpec = this.specs[prevReg];
               if (prevSpec != null && prevSpec.getCategory() == 2) {
                  this.specs[prevReg] = null;
               }
            }

            if (spec.getCategory() == 2) {
               this.specs[reg + 1] = null;
            }

         } catch (ArrayIndexOutOfBoundsException var5) {
            throw new IllegalArgumentException("spec.getReg() out of range");
         }
      }
   }

   public void putAll(RegisterSpecSet set) {
      int max = set.getMaxSize();

      for(int i = 0; i < max; ++i) {
         RegisterSpec spec = set.get(i);
         if (spec != null) {
            this.put(spec);
         }
      }

   }

   public void intersect(RegisterSpecSet other, boolean localPrimary) {
      this.throwIfImmutable();
      RegisterSpec[] otherSpecs = other.specs;
      int thisLen = this.specs.length;
      int len = Math.min(thisLen, otherSpecs.length);
      this.size = -1;

      int i;
      for(i = 0; i < len; ++i) {
         RegisterSpec spec = this.specs[i];
         if (spec != null) {
            RegisterSpec intersection = spec.intersect(otherSpecs[i], localPrimary);
            if (intersection != spec) {
               this.specs[i] = intersection;
            }
         }
      }

      for(i = len; i < thisLen; ++i) {
         this.specs[i] = null;
      }

   }

   public RegisterSpecSet withOffset(int delta) {
      int len = this.specs.length;
      RegisterSpecSet result = new RegisterSpecSet(len + delta);

      for(int i = 0; i < len; ++i) {
         RegisterSpec spec = this.specs[i];
         if (spec != null) {
            result.put(spec.withOffset(delta));
         }
      }

      result.size = this.size;
      if (this.isImmutable()) {
         result.setImmutable();
      }

      return result;
   }

   public RegisterSpecSet mutableCopy() {
      int len = this.specs.length;
      RegisterSpecSet copy = new RegisterSpecSet(len);

      for(int i = 0; i < len; ++i) {
         RegisterSpec spec = this.specs[i];
         if (spec != null) {
            copy.put(spec);
         }
      }

      copy.size = this.size;
      return copy;
   }
}
