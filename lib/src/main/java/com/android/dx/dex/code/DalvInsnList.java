package com.android.dx.dex.code;

import com.android.dex.util.ExceptionWithContext;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstBaseMethodRef;
import com.android.dx.rop.cst.CstCallSiteRef;
import com.android.dx.rop.cst.CstProtoRef;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.FixedSizeList;
import com.android.dx.util.IndentingWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

public final class DalvInsnList extends FixedSizeList {
   private final int regCount;

   public static DalvInsnList makeImmutable(ArrayList<DalvInsn> list, int regCount) {
      int size = list.size();
      DalvInsnList result = new DalvInsnList(size, regCount);

      for(int i = 0; i < size; ++i) {
         result.set(i, (DalvInsn)list.get(i));
      }

      result.setImmutable();
      return result;
   }

   public DalvInsnList(int size, int regCount) {
      super(size);
      this.regCount = regCount;
   }

   public DalvInsn get(int n) {
      return (DalvInsn)this.get0(n);
   }

   public void set(int n, DalvInsn insn) {
      this.set0(n, insn);
   }

   public int codeSize() {
      int sz = this.size();
      if (sz == 0) {
         return 0;
      } else {
         DalvInsn last = this.get(sz - 1);
         return last.getNextAddress();
      }
   }

   public void writeTo(AnnotatedOutput out) {
      int startCursor = out.getCursor();
      int sz = this.size();
      if (out.annotates()) {
         boolean verbose = out.isVerbose();

         for(int i = 0; i < sz; ++i) {
            DalvInsn insn = (DalvInsn)this.get0(i);
            int codeBytes = insn.codeSize() * 2;
            String s;
            if (codeBytes == 0 && !verbose) {
               s = null;
            } else {
               s = insn.listingString("  ", out.getAnnotationWidth(), true);
            }

            if (s != null) {
               out.annotate(codeBytes, s);
            } else if (codeBytes != 0) {
               out.annotate(codeBytes, "");
            }
         }
      }

      int written;
      for(written = 0; written < sz; ++written) {
         DalvInsn insn = (DalvInsn)this.get0(written);

         try {
            insn.writeTo(out);
         } catch (RuntimeException var9) {
            throw ExceptionWithContext.withContext(var9, "...while writing " + insn);
         }
      }

      written = (out.getCursor() - startCursor) / 2;
      if (written != this.codeSize()) {
         throw new RuntimeException("write length mismatch; expected " + this.codeSize() + " but actually wrote " + written);
      }
   }

   public int getRegistersSize() {
      return this.regCount;
   }

   public int getOutsSize() {
      int sz = this.size();
      int result = 0;

      for(int i = 0; i < sz; ++i) {
         DalvInsn insn = (DalvInsn)this.get0(i);
         int count = 0;
         if (insn instanceof CstInsn) {
            Constant cst = ((CstInsn)insn).getConstant();
            if (cst instanceof CstBaseMethodRef) {
               CstBaseMethodRef methodRef = (CstBaseMethodRef)cst;
               boolean isStatic = insn.getOpcode().getFamily() == 113;
               count = methodRef.getParameterWordCount(isStatic);
            } else if (cst instanceof CstCallSiteRef) {
               CstCallSiteRef invokeDynamicRef = (CstCallSiteRef)cst;
               count = invokeDynamicRef.getPrototype().getParameterTypes().getWordCount();
            }
         } else {
            if (!(insn instanceof MultiCstInsn)) {
               continue;
            }

            if (insn.getOpcode().getFamily() != 250) {
               throw new RuntimeException("Expecting invoke-polymorphic");
            }

            MultiCstInsn mci = (MultiCstInsn)insn;
            CstProtoRef proto = (CstProtoRef)mci.getConstant(1);
            count = proto.getPrototype().getParameterTypes().getWordCount();
            ++count;
         }

         if (count > result) {
            result = count;
         }
      }

      return result;
   }

   public void debugPrint(Writer out, String prefix, boolean verbose) {
      IndentingWriter iw = new IndentingWriter(out, 0, prefix);
      int sz = this.size();

      try {
         for(int i = 0; i < sz; ++i) {
            DalvInsn insn = (DalvInsn)this.get0(i);
            String s;
            if (insn.codeSize() == 0 && !verbose) {
               s = null;
            } else {
               s = insn.listingString("", 0, verbose);
            }

            if (s != null) {
               iw.write(s);
            }
         }

         iw.flush();
      } catch (IOException var9) {
         IOException ex = var9;
         throw new RuntimeException(ex);
      }
   }

   public void debugPrint(OutputStream out, String prefix, boolean verbose) {
      Writer w = new OutputStreamWriter(out);
      this.debugPrint((Writer)w, prefix, verbose);

      try {
         ((Writer)w).flush();
      } catch (IOException var6) {
         IOException ex = var6;
         throw new RuntimeException(ex);
      }
   }
}
