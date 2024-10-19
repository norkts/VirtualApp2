package com.android.dx.dex.file;

import com.android.dex.util.ExceptionWithContext;
import com.android.dx.dex.code.LocalList;
import com.android.dx.dex.code.PositionList;
import com.android.dx.rop.code.SourcePosition;
import com.android.dx.rop.cst.CstMethodRef;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.Prototype;
import com.android.dx.rop.type.StdTypeList;
import com.android.dx.rop.type.Type;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.ByteArrayAnnotatedOutput;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public final class DebugInfoEncoder {
   private static final boolean DEBUG = false;
   private final PositionList positions;
   private final LocalList locals;
   private final ByteArrayAnnotatedOutput output;
   private final DexFile file;
   private final int codeSize;
   private final int regSize;
   private final Prototype desc;
   private final boolean isStatic;
   private int address = 0;
   private int line = 1;
   private AnnotatedOutput annotateTo;
   private PrintWriter debugPrint;
   private String prefix;
   private boolean shouldConsume;
   private final LocalList.Entry[] lastEntryForReg;

   public DebugInfoEncoder(PositionList positions, LocalList locals, DexFile file, int codeSize, int regSize, boolean isStatic, CstMethodRef ref) {
      this.positions = positions;
      this.locals = locals;
      this.file = file;
      this.desc = ref.getPrototype();
      this.isStatic = isStatic;
      this.codeSize = codeSize;
      this.regSize = regSize;
      this.output = new ByteArrayAnnotatedOutput();
      this.lastEntryForReg = new LocalList.Entry[regSize];
   }

   private void annotate(int length, String message) {
      if (this.prefix != null) {
         message = this.prefix + message;
      }

      if (this.annotateTo != null) {
         this.annotateTo.annotate(this.shouldConsume ? length : 0, message);
      }

      if (this.debugPrint != null) {
         this.debugPrint.println(message);
      }

   }

   public byte[] convert() {
      try {
         byte[] ret = this.convert0();
         return ret;
      } catch (IOException var2) {
         IOException ex = var2;
         throw ExceptionWithContext.withContext(ex, "...while encoding debug info");
      }
   }

   public byte[] convertAndAnnotate(String prefix, PrintWriter debugPrint, AnnotatedOutput out, boolean consume) {
      this.prefix = prefix;
      this.debugPrint = debugPrint;
      this.annotateTo = out;
      this.shouldConsume = consume;
      byte[] result = this.convert();
      return result;
   }

   private byte[] convert0() throws IOException {
      ArrayList<PositionList.Entry> sortedPositions = this.buildSortedPositions();
      ArrayList<LocalList.Entry> methodArgs = this.extractMethodArguments();
      this.emitHeader(sortedPositions, methodArgs);
      this.output.writeByte(7);
      if (this.annotateTo != null || this.debugPrint != null) {
         this.annotate(1, String.format("%04x: prologue end", this.address));
      }

      int positionsSz = sortedPositions.size();
      int localsSz = this.locals.size();
      int curPositionIdx = 0;
      int curLocalIdx = 0;

      while(true) {
         curLocalIdx = this.emitLocalsAtAddress(curLocalIdx);
         curPositionIdx = this.emitPositionsAtAddress(curPositionIdx, sortedPositions);
         int nextAddrL = Integer.MAX_VALUE;
         int nextAddrP = Integer.MAX_VALUE;
         if (curLocalIdx < localsSz) {
            nextAddrL = this.locals.get(curLocalIdx).getAddress();
         }

         if (curPositionIdx < positionsSz) {
            nextAddrP = ((PositionList.Entry)sortedPositions.get(curPositionIdx)).getAddress();
         }

         int next = Math.min(nextAddrP, nextAddrL);
         if (next == Integer.MAX_VALUE || next == this.codeSize && nextAddrL == Integer.MAX_VALUE && nextAddrP == Integer.MAX_VALUE) {
            this.emitEndSequence();
            return this.output.toByteArray();
         }

         if (next == nextAddrP) {
            this.emitPosition((PositionList.Entry)sortedPositions.get(curPositionIdx++));
         } else {
            this.emitAdvancePc(next - this.address);
         }
      }
   }

   private int emitLocalsAtAddress(int curLocalIdx) throws IOException {
      int sz = this.locals.size();

      while(curLocalIdx < sz && this.locals.get(curLocalIdx).getAddress() == this.address) {
         LocalList.Entry entry = this.locals.get(curLocalIdx++);
         int reg = entry.getRegister();
         LocalList.Entry prevEntry = this.lastEntryForReg[reg];
         if (entry != prevEntry) {
            this.lastEntryForReg[reg] = entry;
            if (entry.isStart()) {
               if (prevEntry != null && entry.matches(prevEntry)) {
                  if (prevEntry.isStart()) {
                     throw new RuntimeException("shouldn't happen");
                  }

                  this.emitLocalRestart(entry);
               } else {
                  this.emitLocalStart(entry);
               }
            } else if (entry.getDisposition() != LocalList.Disposition.END_REPLACED) {
               this.emitLocalEnd(entry);
            }
         }
      }

      return curLocalIdx;
   }

   private int emitPositionsAtAddress(int curPositionIdx, ArrayList<PositionList.Entry> sortedPositions) throws IOException {
      int positionsSz = sortedPositions.size();

      while(curPositionIdx < positionsSz && ((PositionList.Entry)sortedPositions.get(curPositionIdx)).getAddress() == this.address) {
         this.emitPosition((PositionList.Entry)sortedPositions.get(curPositionIdx++));
      }

      return curPositionIdx;
   }

   private void emitHeader(ArrayList<PositionList.Entry> sortedPositions, ArrayList<LocalList.Entry> methodArgs) throws IOException {
      boolean annotate = this.annotateTo != null || this.debugPrint != null;
      int mark = this.output.getCursor();
      if (sortedPositions.size() > 0) {
         PositionList.Entry entry = (PositionList.Entry)sortedPositions.get(0);
         this.line = entry.getPosition().getLine();
      }

      this.output.writeUleb128(this.line);
      if (annotate) {
         this.annotate(this.output.getCursor() - mark, "line_start: " + this.line);
      }

      int curParam = this.getParamBase();
      StdTypeList paramTypes = this.desc.getParameterTypes();
      int szParamTypes = paramTypes.size();
      if (!this.isStatic) {
         Iterator var8 = methodArgs.iterator();

         while(var8.hasNext()) {
            LocalList.Entry arg = (LocalList.Entry)var8.next();
            if (curParam == arg.getRegister()) {
               this.lastEntryForReg[curParam] = arg;
               break;
            }
         }

         ++curParam;
      }

      mark = this.output.getCursor();
      this.output.writeUleb128(szParamTypes);
      if (annotate) {
         this.annotate(this.output.getCursor() - mark, String.format("parameters_size: %04x", szParamTypes));
      }

      for(int i = 0; i < szParamTypes; ++i) {
         Type pt = paramTypes.get(i);
         LocalList.Entry found = null;
         mark = this.output.getCursor();
         Iterator var11 = methodArgs.iterator();

         while(var11.hasNext()) {
            LocalList.Entry arg = (LocalList.Entry)var11.next();
            if (curParam == arg.getRegister()) {
               found = arg;
               if (arg.getSignature() != null) {
                  this.emitStringIndex((CstString)null);
               } else {
                  this.emitStringIndex(arg.getName());
               }

               this.lastEntryForReg[curParam] = arg;
               break;
            }
         }

         if (found == null) {
            this.emitStringIndex((CstString)null);
         }

         if (annotate) {
            String parameterName = found != null && found.getSignature() == null ? found.getName().toHuman() : "<unnamed>";
            this.annotate(this.output.getCursor() - mark, "parameter " + parameterName + " " + "v" + curParam);
         }

         curParam += pt.getCategory();
      }

      LocalList.Entry[] var15 = this.lastEntryForReg;
      int var17 = var15.length;

      for(int var18 = 0; var18 < var17; ++var18) {
         LocalList.Entry arg = var15[var18];
         if (arg != null) {
            CstString signature = arg.getSignature();
            if (signature != null) {
               this.emitLocalStartExtended(arg);
            }
         }
      }

   }

   private ArrayList<PositionList.Entry> buildSortedPositions() {
      int sz = this.positions == null ? 0 : this.positions.size();
      ArrayList<PositionList.Entry> result = new ArrayList(sz);

      for(int i = 0; i < sz; ++i) {
         result.add(this.positions.get(i));
      }

      Collections.sort(result, new Comparator<PositionList.Entry>() {
         public int compare(PositionList.Entry a, PositionList.Entry b) {
            return a.getAddress() - b.getAddress();
         }

         public boolean equals(Object obj) {
            return obj == this;
         }
      });
      return result;
   }

   private int getParamBase() {
      return this.regSize - this.desc.getParameterTypes().getWordCount() - (this.isStatic ? 0 : 1);
   }

   private ArrayList<LocalList.Entry> extractMethodArguments() {
      ArrayList<LocalList.Entry> result = new ArrayList(this.desc.getParameterTypes().size());
      int argBase = this.getParamBase();
      BitSet seen = new BitSet(this.regSize - argBase);
      int sz = this.locals.size();

      for(int i = 0; i < sz; ++i) {
         LocalList.Entry e = this.locals.get(i);
         int reg = e.getRegister();
         if (reg >= argBase && !seen.get(reg - argBase)) {
            seen.set(reg - argBase);
            result.add(e);
         }
      }

      Collections.sort(result, new Comparator<LocalList.Entry>() {
         public int compare(LocalList.Entry a, LocalList.Entry b) {
            return a.getRegister() - b.getRegister();
         }

         public boolean equals(Object obj) {
            return obj == this;
         }
      });
      return result;
   }

   private String entryAnnotationString(LocalList.Entry e) {
      StringBuilder sb = new StringBuilder();
      sb.append("v");
      sb.append(e.getRegister());
      sb.append(' ');
      CstString name = e.getName();
      if (name == null) {
         sb.append("null");
      } else {
         sb.append(name.toHuman());
      }

      sb.append(' ');
      CstType type = e.getType();
      if (type == null) {
         sb.append("null");
      } else {
         sb.append(type.toHuman());
      }

      CstString signature = e.getSignature();
      if (signature != null) {
         sb.append(' ');
         sb.append(signature.toHuman());
      }

      return sb.toString();
   }

   private void emitLocalRestart(LocalList.Entry entry) throws IOException {
      int mark = this.output.getCursor();
      this.output.writeByte(6);
      this.emitUnsignedLeb128(entry.getRegister());
      if (this.annotateTo != null || this.debugPrint != null) {
         this.annotate(this.output.getCursor() - mark, String.format("%04x: +local restart %s", this.address, this.entryAnnotationString(entry)));
      }

   }

   private void emitStringIndex(CstString string) throws IOException {
      if (string != null && this.file != null) {
         this.output.writeUleb128(1 + this.file.getStringIds().indexOf(string));
      } else {
         this.output.writeUleb128(0);
      }

   }

   private void emitTypeIndex(CstType type) throws IOException {
      if (type != null && this.file != null) {
         this.output.writeUleb128(1 + this.file.getTypeIds().indexOf(type));
      } else {
         this.output.writeUleb128(0);
      }

   }

   private void emitLocalStart(LocalList.Entry entry) throws IOException {
      if (entry.getSignature() != null) {
         this.emitLocalStartExtended(entry);
      } else {
         int mark = this.output.getCursor();
         this.output.writeByte(3);
         this.emitUnsignedLeb128(entry.getRegister());
         this.emitStringIndex(entry.getName());
         this.emitTypeIndex(entry.getType());
         if (this.annotateTo != null || this.debugPrint != null) {
            this.annotate(this.output.getCursor() - mark, String.format("%04x: +local %s", this.address, this.entryAnnotationString(entry)));
         }

      }
   }

   private void emitLocalStartExtended(LocalList.Entry entry) throws IOException {
      int mark = this.output.getCursor();
      this.output.writeByte(4);
      this.emitUnsignedLeb128(entry.getRegister());
      this.emitStringIndex(entry.getName());
      this.emitTypeIndex(entry.getType());
      this.emitStringIndex(entry.getSignature());
      if (this.annotateTo != null || this.debugPrint != null) {
         this.annotate(this.output.getCursor() - mark, String.format("%04x: +localx %s", this.address, this.entryAnnotationString(entry)));
      }

   }

   private void emitLocalEnd(LocalList.Entry entry) throws IOException {
      int mark = this.output.getCursor();
      this.output.writeByte(5);
      this.output.writeUleb128(entry.getRegister());
      if (this.annotateTo != null || this.debugPrint != null) {
         this.annotate(this.output.getCursor() - mark, String.format("%04x: -local %s", this.address, this.entryAnnotationString(entry)));
      }

   }

   private void emitPosition(PositionList.Entry entry) throws IOException {
      SourcePosition pos = entry.getPosition();
      int newLine = pos.getLine();
      int newAddress = entry.getAddress();
      int deltaLines = newLine - this.line;
      int deltaAddress = newAddress - this.address;
      if (deltaAddress < 0) {
         throw new RuntimeException("Position entries must be in ascending address order");
      } else {
         if (deltaLines < -4 || deltaLines > 10) {
            this.emitAdvanceLine(deltaLines);
            deltaLines = 0;
         }

         int opcode = computeOpcode(deltaLines, deltaAddress);
         if ((opcode & -256) > 0) {
            this.emitAdvancePc(deltaAddress);
            deltaAddress = 0;
            opcode = computeOpcode(deltaLines, deltaAddress);
            if ((opcode & -256) > 0) {
               this.emitAdvanceLine(deltaLines);
               deltaLines = 0;
               opcode = computeOpcode(deltaLines, deltaAddress);
            }
         }

         this.output.writeByte(opcode);
         this.line += deltaLines;
         this.address += deltaAddress;
         if (this.annotateTo != null || this.debugPrint != null) {
            this.annotate(1, String.format("%04x: line %d", this.address, this.line));
         }

      }
   }

   private static int computeOpcode(int deltaLines, int deltaAddress) {
      if (deltaLines >= -4 && deltaLines <= 10) {
         return deltaLines - -4 + 15 * deltaAddress + 10;
      } else {
         throw new RuntimeException("Parameter out of range");
      }
   }

   private void emitAdvanceLine(int deltaLines) throws IOException {
      int mark = this.output.getCursor();
      this.output.writeByte(2);
      this.output.writeSleb128(deltaLines);
      this.line += deltaLines;
      if (this.annotateTo != null || this.debugPrint != null) {
         this.annotate(this.output.getCursor() - mark, String.format("line = %d", this.line));
      }

   }

   private void emitAdvancePc(int deltaAddress) throws IOException {
      int mark = this.output.getCursor();
      this.output.writeByte(1);
      this.output.writeUleb128(deltaAddress);
      this.address += deltaAddress;
      if (this.annotateTo != null || this.debugPrint != null) {
         this.annotate(this.output.getCursor() - mark, String.format("%04x: advance pc", this.address));
      }

   }

   private void emitUnsignedLeb128(int n) throws IOException {
      if (n < 0) {
         throw new RuntimeException("Signed value where unsigned required: " + n);
      } else {
         this.output.writeUleb128(n);
      }
   }

   private void emitEndSequence() {
      this.output.writeByte(0);
      if (this.annotateTo != null || this.debugPrint != null) {
         this.annotate(1, "end sequence");
      }

   }
}
