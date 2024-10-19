package com.android.dx.dex.file;

import com.android.dex.Leb128;
import com.android.dex.util.ByteArrayByteInput;
import com.android.dex.util.ByteInput;
import com.android.dex.util.ExceptionWithContext;
import com.android.dx.dex.code.DalvCode;
import com.android.dx.dex.code.DalvInsnList;
import com.android.dx.dex.code.LocalList;
import com.android.dx.dex.code.PositionList;
import com.android.dx.rop.cst.CstMethodRef;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.type.Prototype;
import com.android.dx.rop.type.StdTypeList;
import com.android.dx.rop.type.Type;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DebugInfoDecoder {
   private final byte[] encoded;
   private final ArrayList<PositionEntry> positions;
   private final ArrayList<LocalEntry> locals;
   private final int codesize;
   private final LocalEntry[] lastEntryForReg;
   private final Prototype desc;
   private final boolean isStatic;
   private final DexFile file;
   private final int regSize;
   private int line = 1;
   private int address = 0;
   private final int thisStringIdx;

   DebugInfoDecoder(byte[] encoded, int codesize, int regSize, boolean isStatic, CstMethodRef ref, DexFile file) {
      if (encoded == null) {
         throw new NullPointerException("encoded == null");
      } else {
         this.encoded = encoded;
         this.isStatic = isStatic;
         this.desc = ref.getPrototype();
         this.file = file;
         this.regSize = regSize;
         this.positions = new ArrayList();
         this.locals = new ArrayList();
         this.codesize = codesize;
         this.lastEntryForReg = new LocalEntry[regSize];
         int idx = -1;

         try {
            idx = file.getStringIds().indexOf(new CstString("this"));
         } catch (IllegalArgumentException var9) {
         }

         this.thisStringIdx = idx;
      }
   }

   public List<PositionEntry> getPositionList() {
      return this.positions;
   }

   public List<LocalEntry> getLocals() {
      return this.locals;
   }

   public void decode() {
      try {
         this.decode0();
      } catch (Exception var2) {
         Exception ex = var2;
         throw ExceptionWithContext.withContext(ex, "...while decoding debug info");
      }
   }

   private int readStringIndex(ByteInput bs) throws IOException {
      int offsetIndex = Leb128.readUnsignedLeb128(bs);
      return offsetIndex - 1;
   }

   private int getParamBase() {
      return this.regSize - this.desc.getParameterTypes().getWordCount() - (this.isStatic ? 0 : 1);
   }

   private void decode0() throws IOException {
      ByteInput bs = new ByteArrayByteInput(this.encoded);
      this.line = Leb128.readUnsignedLeb128(bs);
      int szParams = Leb128.readUnsignedLeb128(bs);
      StdTypeList params = this.desc.getParameterTypes();
      int curReg = this.getParamBase();
      if (szParams != params.size()) {
         throw new RuntimeException("Mismatch between parameters_size and prototype");
      } else {
         if (!this.isStatic) {
            LocalEntry thisEntry = new LocalEntry(0, true, curReg, this.thisStringIdx, 0, 0);
            this.locals.add(thisEntry);
            this.lastEntryForReg[curReg] = thisEntry;
            ++curReg;
         }

         LocalEntry prevle;
         int typeIdx;
         int opcode;
         for(opcode = 0; opcode < szParams; ++opcode) {
            Type paramType = params.getType(opcode);
            typeIdx = this.readStringIndex(bs);
            if (typeIdx == -1) {
               prevle = new LocalEntry(0, true, curReg, -1, 0, 0);
            } else {
               prevle = new LocalEntry(0, true, curReg, typeIdx, 0, 0);
            }

            this.locals.add(prevle);
            this.lastEntryForReg[curReg] = prevle;
            curReg += paramType.getCategory();
         }

         while(true) {
            opcode = bs.readByte() & 255;
            int reg;
            LocalEntry le;
            int nameIdx;
            switch (opcode) {
               case 0:
                  return;
               case 1:
                  this.address += Leb128.readUnsignedLeb128(bs);
                  break;
               case 2:
                  this.line += Leb128.readSignedLeb128(bs);
                  break;
               case 3:
                  reg = Leb128.readUnsignedLeb128(bs);
                  nameIdx = this.readStringIndex(bs);
                  typeIdx = this.readStringIndex(bs);
                  LocalEntry le = new LocalEntry(this.address, true, reg, nameIdx, typeIdx, 0);
                  this.locals.add(le);
                  this.lastEntryForReg[reg] = le;
                  break;
               case 4:
                  reg = Leb128.readUnsignedLeb128(bs);
                  nameIdx = this.readStringIndex(bs);
                  typeIdx = this.readStringIndex(bs);
                  int sigIdx = this.readStringIndex(bs);
                  LocalEntry le = new LocalEntry(this.address, true, reg, nameIdx, typeIdx, sigIdx);
                  this.locals.add(le);
                  this.lastEntryForReg[reg] = le;
                  break;
               case 5:
                  reg = Leb128.readUnsignedLeb128(bs);

                  try {
                     prevle = this.lastEntryForReg[reg];
                     if (!prevle.isStart) {
                        throw new RuntimeException("nonsensical END_LOCAL on dead register v" + reg);
                     }

                     le = new LocalEntry(this.address, false, reg, prevle.nameIndex, prevle.typeIndex, prevle.signatureIndex);
                  } catch (NullPointerException var12) {
                     throw new RuntimeException("Encountered END_LOCAL on new v" + reg);
                  }

                  this.locals.add(le);
                  this.lastEntryForReg[reg] = le;
                  break;
               case 6:
                  reg = Leb128.readUnsignedLeb128(bs);

                  try {
                     prevle = this.lastEntryForReg[reg];
                     if (prevle.isStart) {
                        throw new RuntimeException("nonsensical RESTART_LOCAL on live register v" + reg);
                     }

                     le = new LocalEntry(this.address, true, reg, prevle.nameIndex, prevle.typeIndex, 0);
                  } catch (NullPointerException var11) {
                     throw new RuntimeException("Encountered RESTART_LOCAL on new v" + reg);
                  }

                  this.locals.add(le);
                  this.lastEntryForReg[reg] = le;
               case 7:
               case 8:
               case 9:
                  break;
               default:
                  if (opcode < 10) {
                     throw new RuntimeException("Invalid extended opcode encountered " + opcode);
                  }

                  reg = opcode - 10;
                  this.address += reg / 15;
                  this.line += -4 + reg % 15;
                  this.positions.add(new PositionEntry(this.address, this.line));
            }
         }
      }
   }

   public static void validateEncode(byte[] info, DexFile file, CstMethodRef ref, DalvCode code, boolean isStatic) {
      PositionList pl = code.getPositions();
      LocalList ll = code.getLocals();
      DalvInsnList insns = code.getInsns();
      int codeSize = insns.codeSize();
      int countRegisters = insns.getRegistersSize();

      try {
         validateEncode0(info, codeSize, countRegisters, isStatic, ref, file, pl, ll);
      } catch (RuntimeException var11) {
         RuntimeException ex = var11;
         System.err.println("instructions:");
         insns.debugPrint((OutputStream)System.err, "  ", true);
         System.err.println("local list:");
         ll.debugPrint(System.err, "  ");
         throw ExceptionWithContext.withContext(ex, "while processing " + ref.toHuman());
      }
   }

   private static void validateEncode0(byte[] info, int codeSize, int countRegisters, boolean isStatic, CstMethodRef ref, DexFile file, PositionList pl, LocalList ll) {
      DebugInfoDecoder decoder = new DebugInfoDecoder(info, codeSize, countRegisters, isStatic, ref, file);
      decoder.decode();
      List<PositionEntry> decodedEntries = decoder.getPositionList();
      if (decodedEntries.size() != pl.size()) {
         throw new RuntimeException("Decoded positions table not same size was " + decodedEntries.size() + " expected " + pl.size());
      } else {
         Iterator var10 = decodedEntries.iterator();

         PositionEntry entry;
         boolean found;
         do {
            int paramBase;
            if (!var10.hasNext()) {
               List<LocalEntry> decodedLocals = decoder.getLocals();
               int thisStringIdx = decoder.thisStringIdx;
               int decodedSz = decodedLocals.size();
               paramBase = decoder.getParamBase();

               int i;
               LocalEntry e2;
               int i;
               for(i = 0; i < decodedSz; ++i) {
                  LocalEntry entry = (LocalEntry)decodedLocals.get(i);
                  int idx = entry.nameIndex;
                  if (idx < 0 || idx == thisStringIdx) {
                     for(i = i + 1; i < decodedSz; ++i) {
                        e2 = (LocalEntry)decodedLocals.get(i);
                        if (e2.address != 0) {
                           break;
                        }

                        if (entry.reg == e2.reg && e2.isStart) {
                           decodedLocals.set(i, e2);
                           decodedLocals.remove(i);
                           --decodedSz;
                           break;
                        }
                     }
                  }
               }

               i = ll.size();
               int decodeAt = 0;
               boolean problem = false;

               label90:
               for(i = 0; i < i; ++i) {
                  LocalList.Entry origEntry = ll.get(i);
                  if (origEntry.getDisposition() != LocalList.Disposition.END_REPLACED) {
                     while(true) {
                        LocalEntry decodedEntry = (LocalEntry)decodedLocals.get(decodeAt);
                        if (decodedEntry.nameIndex < 0) {
                           ++decodeAt;
                           if (decodeAt < decodedSz) {
                              continue;
                           }
                        }

                        int decodedAddress = decodedEntry.address;
                        if (decodedEntry.reg != origEntry.getRegister()) {
                           System.err.println("local register mismatch at orig " + i + " / decoded " + decodeAt);
                           problem = true;
                           break label90;
                        }

                        if (decodedEntry.isStart != origEntry.isStart()) {
                           System.err.println("local start/end mismatch at orig " + i + " / decoded " + decodeAt);
                           problem = true;
                           break label90;
                        }

                        if (decodedAddress != origEntry.getAddress() && (decodedAddress != 0 || decodedEntry.reg < paramBase)) {
                           System.err.println("local address mismatch at orig " + i + " / decoded " + decodeAt);
                           problem = true;
                           break label90;
                        }

                        ++decodeAt;
                        break;
                     }
                  }
               }

               if (!problem) {
                  return;
               }

               System.err.println("decoded locals:");
               Iterator var28 = decodedLocals.iterator();

               while(var28.hasNext()) {
                  e2 = (LocalEntry)var28.next();
                  System.err.println("  " + e2);
               }

               throw new RuntimeException("local table problem");
            }

            entry = (PositionEntry)var10.next();
            found = false;

            for(paramBase = pl.size() - 1; paramBase >= 0; --paramBase) {
               PositionList.Entry ple = pl.get(paramBase);
               if (entry.line == ple.getPosition().getLine() && entry.address == ple.getAddress()) {
                  found = true;
                  break;
               }
            }
         } while(found);

         throw new RuntimeException("Could not match position entry: " + entry.address + ", " + entry.line);
      }
   }

   private static class LocalEntry {
      public int address;
      public boolean isStart;
      public int reg;
      public int nameIndex;
      public int typeIndex;
      public int signatureIndex;

      public LocalEntry(int address, boolean isStart, int reg, int nameIndex, int typeIndex, int signatureIndex) {
         this.address = address;
         this.isStart = isStart;
         this.reg = reg;
         this.nameIndex = nameIndex;
         this.typeIndex = typeIndex;
         this.signatureIndex = signatureIndex;
      }

      public String toString() {
         return String.format("[%x %s v%d %04x %04x %04x]", this.address, this.isStart ? "start" : "end", this.reg, this.nameIndex, this.typeIndex, this.signatureIndex);
      }
   }

   private static class PositionEntry {
      public int address;
      public int line;

      public PositionEntry(int address, int line) {
         this.address = address;
         this.line = line;
      }
   }
}
