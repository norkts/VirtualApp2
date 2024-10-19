package com.android.dx.io.instructions;

import com.android.dex.DexException;
import com.android.dx.io.IndexType;
import com.android.dx.io.OpcodeInfo;
import com.android.dx.util.Hex;
import java.io.EOFException;
import java.util.Arrays;

public enum InstructionCodec {
   FORMAT_00X {
      public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
         return new ZeroRegisterDecodedInstruction(this, opcodeUnit, 0, (IndexType)null, 0, 0L);
      }

      public void encode(DecodedInstruction insn, CodeOutput out) {
         out.write(insn.getOpcodeUnit());
      }
   },
   FORMAT_10X {
      public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
         int opcode = InstructionCodec.byte0(opcodeUnit);
         int literal = InstructionCodec.byte1(opcodeUnit);
         return new ZeroRegisterDecodedInstruction(this, opcode, 0, (IndexType)null, 0, (long)literal);
      }

      public void encode(DecodedInstruction insn, CodeOutput out) {
         out.write(insn.getOpcodeUnit());
      }
   },
   FORMAT_12X {
      public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
         int opcode = InstructionCodec.byte0(opcodeUnit);
         int a = InstructionCodec.nibble2(opcodeUnit);
         int b = InstructionCodec.nibble3(opcodeUnit);
         return new TwoRegisterDecodedInstruction(this, opcode, 0, (IndexType)null, 0, 0L, a, b);
      }

      public void encode(DecodedInstruction insn, CodeOutput out) {
         out.write(InstructionCodec.codeUnit(insn.getOpcodeUnit(), InstructionCodec.makeByte(insn.getA(), insn.getB())));
      }
   },
   FORMAT_11N {
      public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
         int opcode = InstructionCodec.byte0(opcodeUnit);
         int a = InstructionCodec.nibble2(opcodeUnit);
         int literal = InstructionCodec.nibble3(opcodeUnit) << 28 >> 28;
         return new OneRegisterDecodedInstruction(this, opcode, 0, (IndexType)null, 0, (long)literal, a);
      }

      public void encode(DecodedInstruction insn, CodeOutput out) {
         out.write(InstructionCodec.codeUnit(insn.getOpcodeUnit(), InstructionCodec.makeByte(insn.getA(), insn.getLiteralNibble())));
      }
   },
   FORMAT_11X {
      public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
         int opcode = InstructionCodec.byte0(opcodeUnit);
         int a = InstructionCodec.byte1(opcodeUnit);
         return new OneRegisterDecodedInstruction(this, opcode, 0, (IndexType)null, 0, 0L, a);
      }

      public void encode(DecodedInstruction insn, CodeOutput out) {
         out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getA()));
      }
   },
   FORMAT_10T {
      public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
         int baseAddress = in.cursor() - 1;
         int opcode = InstructionCodec.byte0(opcodeUnit);
         int target = (byte)InstructionCodec.byte1(opcodeUnit);
         return new ZeroRegisterDecodedInstruction(this, opcode, 0, (IndexType)null, baseAddress + target, 0L);
      }

      public void encode(DecodedInstruction insn, CodeOutput out) {
         int relativeTarget = insn.getTargetByte(out.cursor());
         out.write(InstructionCodec.codeUnit(insn.getOpcode(), relativeTarget));
      }
   },
   FORMAT_20T {
      public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
         int baseAddress = in.cursor() - 1;
         int opcode = InstructionCodec.byte0(opcodeUnit);
         int literal = InstructionCodec.byte1(opcodeUnit);
         int target = (short)in.read();
         return new ZeroRegisterDecodedInstruction(this, opcode, 0, (IndexType)null, baseAddress + target, (long)literal);
      }

      public void encode(DecodedInstruction insn, CodeOutput out) {
         short relativeTarget = insn.getTargetUnit(out.cursor());
         out.write(insn.getOpcodeUnit(), relativeTarget);
      }
   },
   FORMAT_20BC {
      public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
         int opcode = InstructionCodec.byte0(opcodeUnit);
         int literal = InstructionCodec.byte1(opcodeUnit);
         int index = in.read();
         return new ZeroRegisterDecodedInstruction(this, opcode, index, IndexType.VARIES, 0, (long)literal);
      }

      public void encode(DecodedInstruction insn, CodeOutput out) {
         out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getLiteralByte()), insn.getIndexUnit());
      }
   },
   FORMAT_22X {
      public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
         int opcode = InstructionCodec.byte0(opcodeUnit);
         int a = InstructionCodec.byte1(opcodeUnit);
         int b = in.read();
         return new TwoRegisterDecodedInstruction(this, opcode, 0, (IndexType)null, 0, 0L, a, b);
      }

      public void encode(DecodedInstruction insn, CodeOutput out) {
         out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getA()), insn.getBUnit());
      }
   },
   FORMAT_21T {
      public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
         int baseAddress = in.cursor() - 1;
         int opcode = InstructionCodec.byte0(opcodeUnit);
         int a = InstructionCodec.byte1(opcodeUnit);
         int target = (short)in.read();
         return new OneRegisterDecodedInstruction(this, opcode, 0, (IndexType)null, baseAddress + target, 0L, a);
      }

      public void encode(DecodedInstruction insn, CodeOutput out) {
         short relativeTarget = insn.getTargetUnit(out.cursor());
         out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getA()), relativeTarget);
      }
   },
   FORMAT_21S {
      public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
         int opcode = InstructionCodec.byte0(opcodeUnit);
         int a = InstructionCodec.byte1(opcodeUnit);
         int literal = (short)in.read();
         return new OneRegisterDecodedInstruction(this, opcode, 0, (IndexType)null, 0, (long)literal, a);
      }

      public void encode(DecodedInstruction insn, CodeOutput out) {
         out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getA()), insn.getLiteralUnit());
      }
   },
   FORMAT_21H {
      public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
         int opcode = InstructionCodec.byte0(opcodeUnit);
         int a = InstructionCodec.byte1(opcodeUnit);
         long literal = (long)((short)in.read());
         literal <<= opcode == 21 ? 16 : 48;
         return new OneRegisterDecodedInstruction(this, opcode, 0, (IndexType)null, 0, literal, a);
      }

      public void encode(DecodedInstruction insn, CodeOutput out) {
         int opcode = insn.getOpcode();
         int shift = opcode == 21 ? 16 : 48;
         short literal = (short)((int)(insn.getLiteral() >> shift));
         out.write(InstructionCodec.codeUnit(opcode, insn.getA()), literal);
      }
   },
   FORMAT_21C {
      public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
         int opcode = InstructionCodec.byte0(opcodeUnit);
         int a = InstructionCodec.byte1(opcodeUnit);
         int index = in.read();
         IndexType indexType = OpcodeInfo.getIndexType(opcode);
         return new OneRegisterDecodedInstruction(this, opcode, index, indexType, 0, 0L, a);
      }

      public void encode(DecodedInstruction insn, CodeOutput out) {
         out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getA()), insn.getIndexUnit());
      }
   },
   FORMAT_23X {
      public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
         int opcode = InstructionCodec.byte0(opcodeUnit);
         int a = InstructionCodec.byte1(opcodeUnit);
         int bc = in.read();
         int b = InstructionCodec.byte0(bc);
         int c = InstructionCodec.byte1(bc);
         return new ThreeRegisterDecodedInstruction(this, opcode, 0, (IndexType)null, 0, 0L, a, b, c);
      }

      public void encode(DecodedInstruction insn, CodeOutput out) {
         out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getA()), InstructionCodec.codeUnit(insn.getB(), insn.getC()));
      }
   },
   FORMAT_22B {
      public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
         int opcode = InstructionCodec.byte0(opcodeUnit);
         int a = InstructionCodec.byte1(opcodeUnit);
         int bc = in.read();
         int b = InstructionCodec.byte0(bc);
         int literal = (byte)InstructionCodec.byte1(bc);
         return new TwoRegisterDecodedInstruction(this, opcode, 0, (IndexType)null, 0, (long)literal, a, b);
      }

      public void encode(DecodedInstruction insn, CodeOutput out) {
         out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getA()), InstructionCodec.codeUnit(insn.getB(), insn.getLiteralByte()));
      }
   },
   FORMAT_22T {
      public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
         int baseAddress = in.cursor() - 1;
         int opcode = InstructionCodec.byte0(opcodeUnit);
         int a = InstructionCodec.nibble2(opcodeUnit);
         int b = InstructionCodec.nibble3(opcodeUnit);
         int target = (short)in.read();
         return new TwoRegisterDecodedInstruction(this, opcode, 0, (IndexType)null, baseAddress + target, 0L, a, b);
      }

      public void encode(DecodedInstruction insn, CodeOutput out) {
         short relativeTarget = insn.getTargetUnit(out.cursor());
         out.write(InstructionCodec.codeUnit(insn.getOpcode(), InstructionCodec.makeByte(insn.getA(), insn.getB())), relativeTarget);
      }
   },
   FORMAT_22S {
      public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
         int opcode = InstructionCodec.byte0(opcodeUnit);
         int a = InstructionCodec.nibble2(opcodeUnit);
         int b = InstructionCodec.nibble3(opcodeUnit);
         int literal = (short)in.read();
         return new TwoRegisterDecodedInstruction(this, opcode, 0, (IndexType)null, 0, (long)literal, a, b);
      }

      public void encode(DecodedInstruction insn, CodeOutput out) {
         out.write(InstructionCodec.codeUnit(insn.getOpcode(), InstructionCodec.makeByte(insn.getA(), insn.getB())), insn.getLiteralUnit());
      }
   },
   FORMAT_22C {
      public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
         int opcode = InstructionCodec.byte0(opcodeUnit);
         int a = InstructionCodec.nibble2(opcodeUnit);
         int b = InstructionCodec.nibble3(opcodeUnit);
         int index = in.read();
         IndexType indexType = OpcodeInfo.getIndexType(opcode);
         return new TwoRegisterDecodedInstruction(this, opcode, index, indexType, 0, 0L, a, b);
      }

      public void encode(DecodedInstruction insn, CodeOutput out) {
         out.write(InstructionCodec.codeUnit(insn.getOpcode(), InstructionCodec.makeByte(insn.getA(), insn.getB())), insn.getIndexUnit());
      }
   },
   FORMAT_22CS {
      public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
         int opcode = InstructionCodec.byte0(opcodeUnit);
         int a = InstructionCodec.nibble2(opcodeUnit);
         int b = InstructionCodec.nibble3(opcodeUnit);
         int index = in.read();
         return new TwoRegisterDecodedInstruction(this, opcode, index, IndexType.FIELD_OFFSET, 0, 0L, a, b);
      }

      public void encode(DecodedInstruction insn, CodeOutput out) {
         out.write(InstructionCodec.codeUnit(insn.getOpcode(), InstructionCodec.makeByte(insn.getA(), insn.getB())), insn.getIndexUnit());
      }
   },
   FORMAT_30T {
      public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
         int baseAddress = in.cursor() - 1;
         int opcode = InstructionCodec.byte0(opcodeUnit);
         int literal = InstructionCodec.byte1(opcodeUnit);
         int target = in.readInt();
         return new ZeroRegisterDecodedInstruction(this, opcode, 0, (IndexType)null, baseAddress + target, (long)literal);
      }

      public void encode(DecodedInstruction insn, CodeOutput out) {
         int relativeTarget = insn.getTarget(out.cursor());
         out.write(insn.getOpcodeUnit(), InstructionCodec.unit0(relativeTarget), InstructionCodec.unit1(relativeTarget));
      }
   },
   FORMAT_32X {
      public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
         int opcode = InstructionCodec.byte0(opcodeUnit);
         int literal = InstructionCodec.byte1(opcodeUnit);
         int a = in.read();
         int b = in.read();
         return new TwoRegisterDecodedInstruction(this, opcode, 0, (IndexType)null, 0, (long)literal, a, b);
      }

      public void encode(DecodedInstruction insn, CodeOutput out) {
         out.write(insn.getOpcodeUnit(), insn.getAUnit(), insn.getBUnit());
      }
   },
   FORMAT_31I {
      public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
         int opcode = InstructionCodec.byte0(opcodeUnit);
         int a = InstructionCodec.byte1(opcodeUnit);
         int literal = in.readInt();
         return new OneRegisterDecodedInstruction(this, opcode, 0, (IndexType)null, 0, (long)literal, a);
      }

      public void encode(DecodedInstruction insn, CodeOutput out) {
         int literal = insn.getLiteralInt();
         out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getA()), InstructionCodec.unit0(literal), InstructionCodec.unit1(literal));
      }
   },
   FORMAT_31T {
      public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
         int baseAddress = in.cursor() - 1;
         int opcode = InstructionCodec.byte0(opcodeUnit);
         int a = InstructionCodec.byte1(opcodeUnit);
         int target = baseAddress + in.readInt();
         switch (opcode) {
            case 43:
            case 44:
               in.setBaseAddress(target, baseAddress);
            default:
               return new OneRegisterDecodedInstruction(this, opcode, 0, (IndexType)null, target, 0L, a);
         }
      }

      public void encode(DecodedInstruction insn, CodeOutput out) {
         int relativeTarget = insn.getTarget(out.cursor());
         out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getA()), InstructionCodec.unit0(relativeTarget), InstructionCodec.unit1(relativeTarget));
      }
   },
   FORMAT_31C {
      public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
         int opcode = InstructionCodec.byte0(opcodeUnit);
         int a = InstructionCodec.byte1(opcodeUnit);
         int index = in.readInt();
         IndexType indexType = OpcodeInfo.getIndexType(opcode);
         return new OneRegisterDecodedInstruction(this, opcode, index, indexType, 0, 0L, a);
      }

      public void encode(DecodedInstruction insn, CodeOutput out) {
         int index = insn.getIndex();
         out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getA()), InstructionCodec.unit0(index), InstructionCodec.unit1(index));
      }
   },
   FORMAT_35C {
      public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
         return InstructionCodec.decodeRegisterList(this, opcodeUnit, in);
      }

      public void encode(DecodedInstruction insn, CodeOutput out) {
         InstructionCodec.encodeRegisterList(insn, out);
      }
   },
   FORMAT_35MS {
      public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
         return InstructionCodec.decodeRegisterList(this, opcodeUnit, in);
      }

      public void encode(DecodedInstruction insn, CodeOutput out) {
         InstructionCodec.encodeRegisterList(insn, out);
      }
   },
   FORMAT_35MI {
      public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
         return InstructionCodec.decodeRegisterList(this, opcodeUnit, in);
      }

      public void encode(DecodedInstruction insn, CodeOutput out) {
         InstructionCodec.encodeRegisterList(insn, out);
      }
   },
   FORMAT_3RC {
      public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
         return InstructionCodec.decodeRegisterRange(this, opcodeUnit, in);
      }

      public void encode(DecodedInstruction insn, CodeOutput out) {
         InstructionCodec.encodeRegisterRange(insn, out);
      }
   },
   FORMAT_3RMS {
      public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
         return InstructionCodec.decodeRegisterRange(this, opcodeUnit, in);
      }

      public void encode(DecodedInstruction insn, CodeOutput out) {
         InstructionCodec.encodeRegisterRange(insn, out);
      }
   },
   FORMAT_3RMI {
      public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
         return InstructionCodec.decodeRegisterRange(this, opcodeUnit, in);
      }

      public void encode(DecodedInstruction insn, CodeOutput out) {
         InstructionCodec.encodeRegisterRange(insn, out);
      }
   },
   FORMAT_51L {
      public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
         int opcode = InstructionCodec.byte0(opcodeUnit);
         int a = InstructionCodec.byte1(opcodeUnit);
         long literal = in.readLong();
         return new OneRegisterDecodedInstruction(this, opcode, 0, (IndexType)null, 0, literal, a);
      }

      public void encode(DecodedInstruction insn, CodeOutput out) {
         long literal = insn.getLiteral();
         out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getA()), InstructionCodec.unit0(literal), InstructionCodec.unit1(literal), InstructionCodec.unit2(literal), InstructionCodec.unit3(literal));
      }
   },
   FORMAT_45CC {
      public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
         int opcode = InstructionCodec.byte0(opcodeUnit);
         if (opcode != 250) {
            throw new UnsupportedOperationException(String.valueOf(opcode));
         } else {
            int g = InstructionCodec.nibble2(opcodeUnit);
            int registerCount = InstructionCodec.nibble3(opcodeUnit);
            int methodIndex = in.read();
            int cdef = in.read();
            int c = InstructionCodec.nibble0(cdef);
            int d = InstructionCodec.nibble1(cdef);
            int e = InstructionCodec.nibble2(cdef);
            int f = InstructionCodec.nibble3(cdef);
            int protoIndex = in.read();
            IndexType indexType = OpcodeInfo.getIndexType(opcode);
            if (registerCount >= 1 && registerCount <= 5) {
               int[] registers = new int[]{c, d, e, f, g};
               registers = Arrays.copyOfRange(registers, 0, registerCount);
               return new InvokePolymorphicDecodedInstruction(this, opcode, methodIndex, indexType, protoIndex, registers);
            } else {
               throw new DexException("bogus registerCount: " + Hex.uNibble(registerCount));
            }
         }
      }

      public void encode(DecodedInstruction insn, CodeOutput out) {
         InvokePolymorphicDecodedInstruction polyInsn = (InvokePolymorphicDecodedInstruction)insn;
         out.write(InstructionCodec.codeUnit(polyInsn.getOpcode(), InstructionCodec.makeByte(polyInsn.getG(), polyInsn.getRegisterCount())), polyInsn.getIndexUnit(), InstructionCodec.codeUnit(polyInsn.getC(), polyInsn.getD(), polyInsn.getE(), polyInsn.getF()), polyInsn.getProtoIndex());
      }
   },
   FORMAT_4RCC {
      public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
         int opcode = InstructionCodec.byte0(opcodeUnit);
         if (opcode != 251) {
            throw new UnsupportedOperationException(String.valueOf(opcode));
         } else {
            int registerCount = InstructionCodec.byte1(opcodeUnit);
            int methodIndex = in.read();
            int c = in.read();
            int protoIndex = in.read();
            IndexType indexType = OpcodeInfo.getIndexType(opcode);
            return new InvokePolymorphicRangeDecodedInstruction(this, opcode, methodIndex, indexType, c, registerCount, protoIndex);
         }
      }

      public void encode(DecodedInstruction insn, CodeOutput out) {
         out.write(InstructionCodec.codeUnit(insn.getOpcode(), insn.getRegisterCount()), insn.getIndexUnit(), insn.getCUnit(), insn.getProtoIndex());
      }
   },
   FORMAT_PACKED_SWITCH_PAYLOAD {
      public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
         int baseAddress = in.baseAddressForCursor() - 1;
         int size = in.read();
         int firstKey = in.readInt();
         int[] targets = new int[size];

         for(int i = 0; i < size; ++i) {
            targets[i] = baseAddress + in.readInt();
         }

         return new PackedSwitchPayloadDecodedInstruction(this, opcodeUnit, firstKey, targets);
      }

      public void encode(DecodedInstruction insn, CodeOutput out) {
         PackedSwitchPayloadDecodedInstruction payload = (PackedSwitchPayloadDecodedInstruction)insn;
         int[] targets = payload.getTargets();
         int baseAddress = out.baseAddressForCursor();
         out.write(payload.getOpcodeUnit());
         out.write(InstructionCodec.asUnsignedUnit(targets.length));
         out.writeInt(payload.getFirstKey());
         int[] var6 = targets;
         int var7 = targets.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            int target = var6[var8];
            out.writeInt(target - baseAddress);
         }

      }
   },
   FORMAT_SPARSE_SWITCH_PAYLOAD {
      public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
         int baseAddress = in.baseAddressForCursor() - 1;
         int size = in.read();
         int[] keys = new int[size];
         int[] targets = new int[size];

         int i;
         for(i = 0; i < size; ++i) {
            keys[i] = in.readInt();
         }

         for(i = 0; i < size; ++i) {
            targets[i] = baseAddress + in.readInt();
         }

         return new SparseSwitchPayloadDecodedInstruction(this, opcodeUnit, keys, targets);
      }

      public void encode(DecodedInstruction insn, CodeOutput out) {
         SparseSwitchPayloadDecodedInstruction payload = (SparseSwitchPayloadDecodedInstruction)insn;
         int[] keys = payload.getKeys();
         int[] targets = payload.getTargets();
         int baseAddress = out.baseAddressForCursor();
         out.write(payload.getOpcodeUnit());
         out.write(InstructionCodec.asUnsignedUnit(targets.length));
         int[] var7 = keys;
         int var8 = keys.length;

         int var9;
         int target;
         for(var9 = 0; var9 < var8; ++var9) {
            target = var7[var9];
            out.writeInt(target);
         }

         var7 = targets;
         var8 = targets.length;

         for(var9 = 0; var9 < var8; ++var9) {
            target = var7[var9];
            out.writeInt(target - baseAddress);
         }

      }
   },
   FORMAT_FILL_ARRAY_DATA_PAYLOAD {
      public DecodedInstruction decode(int opcodeUnit, CodeInput in) throws EOFException {
         int elementWidth = in.read();
         int size = in.readInt();
         int i;
         switch (elementWidth) {
            case 1:
               byte[] array = new byte[size];
               boolean even = true;
               int ix = 0;

               for(int value = 0; ix < size; even = !even) {
                  if (even) {
                     value = in.read();
                  }

                  array[ix] = (byte)(value & 255);
                  value >>= 8;
                  ++ix;
               }

               return new FillArrayDataPayloadDecodedInstruction(this, opcodeUnit, array);
            case 2:
               short[] arrayxxx = new short[size];

               for(i = 0; i < size; ++i) {
                  arrayxxx[i] = (short)in.read();
               }

               return new FillArrayDataPayloadDecodedInstruction(this, opcodeUnit, arrayxxx);
            case 3:
            case 5:
            case 6:
            case 7:
            default:
               throw new DexException("bogus element_width: " + Hex.u2(elementWidth));
            case 4:
               int[] arrayxx = new int[size];

               for(i = 0; i < size; ++i) {
                  arrayxx[i] = in.readInt();
               }

               return new FillArrayDataPayloadDecodedInstruction(this, opcodeUnit, arrayxx);
            case 8:
               long[] arrayx = new long[size];

               for(i = 0; i < size; ++i) {
                  arrayx[i] = in.readLong();
               }

               return new FillArrayDataPayloadDecodedInstruction(this, opcodeUnit, arrayx);
         }
      }

      public void encode(DecodedInstruction insn, CodeOutput out) {
         FillArrayDataPayloadDecodedInstruction payload = (FillArrayDataPayloadDecodedInstruction)insn;
         short elementWidth = payload.getElementWidthUnit();
         Object data = payload.getData();
         out.write(payload.getOpcodeUnit());
         out.write(elementWidth);
         out.writeInt(payload.getSize());
         switch (elementWidth) {
            case 1:
               out.write((byte[])data);
               break;
            case 2:
               out.write((short[])data);
               break;
            case 3:
            case 5:
            case 6:
            case 7:
            default:
               throw new DexException("bogus element_width: " + Hex.u2(elementWidth));
            case 4:
               out.write((int[])data);
               break;
            case 8:
               out.write((long[])data);
         }

      }
   };

   private InstructionCodec() {
   }

   public abstract DecodedInstruction decode(int var1, CodeInput var2) throws EOFException;

   public abstract void encode(DecodedInstruction var1, CodeOutput var2);

   private static DecodedInstruction decodeRegisterList(InstructionCodec format, int opcodeUnit, CodeInput in) throws EOFException {
      int opcode = byte0(opcodeUnit);
      int e = nibble2(opcodeUnit);
      int registerCount = nibble3(opcodeUnit);
      int index = in.read();
      int abcd = in.read();
      int a = nibble0(abcd);
      int b = nibble1(abcd);
      int c = nibble2(abcd);
      int d = nibble3(abcd);
      IndexType indexType = OpcodeInfo.getIndexType(opcode);
      switch (registerCount) {
         case 0:
            return new ZeroRegisterDecodedInstruction(format, opcode, index, indexType, 0, 0L);
         case 1:
            return new OneRegisterDecodedInstruction(format, opcode, index, indexType, 0, 0L, a);
         case 2:
            return new TwoRegisterDecodedInstruction(format, opcode, index, indexType, 0, 0L, a, b);
         case 3:
            return new ThreeRegisterDecodedInstruction(format, opcode, index, indexType, 0, 0L, a, b, c);
         case 4:
            return new FourRegisterDecodedInstruction(format, opcode, index, indexType, 0, 0L, a, b, c, d);
         case 5:
            return new FiveRegisterDecodedInstruction(format, opcode, index, indexType, 0, 0L, a, b, c, d, e);
         default:
            throw new DexException("bogus registerCount: " + Hex.uNibble(registerCount));
      }
   }

   private static void encodeRegisterList(DecodedInstruction insn, CodeOutput out) {
      out.write(codeUnit(insn.getOpcode(), makeByte(insn.getE(), insn.getRegisterCount())), insn.getIndexUnit(), codeUnit(insn.getA(), insn.getB(), insn.getC(), insn.getD()));
   }

   private static DecodedInstruction decodeRegisterRange(InstructionCodec format, int opcodeUnit, CodeInput in) throws EOFException {
      int opcode = byte0(opcodeUnit);
      int registerCount = byte1(opcodeUnit);
      int index = in.read();
      int a = in.read();
      IndexType indexType = OpcodeInfo.getIndexType(opcode);
      return new RegisterRangeDecodedInstruction(format, opcode, index, indexType, 0, 0L, a, registerCount);
   }

   private static void encodeRegisterRange(DecodedInstruction insn, CodeOutput out) {
      out.write(codeUnit(insn.getOpcode(), insn.getRegisterCount()), insn.getIndexUnit(), insn.getAUnit());
   }

   private static short codeUnit(int lowByte, int highByte) {
      if ((lowByte & -256) != 0) {
         throw new IllegalArgumentException("bogus lowByte");
      } else if ((highByte & -256) != 0) {
         throw new IllegalArgumentException("bogus highByte");
      } else {
         return (short)(lowByte | highByte << 8);
      }
   }

   private static short codeUnit(int nibble0, int nibble1, int nibble2, int nibble3) {
      if ((nibble0 & -16) != 0) {
         throw new IllegalArgumentException("bogus nibble0");
      } else if ((nibble1 & -16) != 0) {
         throw new IllegalArgumentException("bogus nibble1");
      } else if ((nibble2 & -16) != 0) {
         throw new IllegalArgumentException("bogus nibble2");
      } else if ((nibble3 & -16) != 0) {
         throw new IllegalArgumentException("bogus nibble3");
      } else {
         return (short)(nibble0 | nibble1 << 4 | nibble2 << 8 | nibble3 << 12);
      }
   }

   private static int makeByte(int lowNibble, int highNibble) {
      if ((lowNibble & -16) != 0) {
         throw new IllegalArgumentException("bogus lowNibble");
      } else if ((highNibble & -16) != 0) {
         throw new IllegalArgumentException("bogus highNibble");
      } else {
         return lowNibble | highNibble << 4;
      }
   }

   private static short asUnsignedUnit(int value) {
      if ((value & -65536) != 0) {
         throw new IllegalArgumentException("bogus unsigned code unit");
      } else {
         return (short)value;
      }
   }

   private static short unit0(int value) {
      return (short)value;
   }

   private static short unit1(int value) {
      return (short)(value >> 16);
   }

   private static short unit0(long value) {
      return (short)((int)value);
   }

   private static short unit1(long value) {
      return (short)((int)(value >> 16));
   }

   private static short unit2(long value) {
      return (short)((int)(value >> 32));
   }

   private static short unit3(long value) {
      return (short)((int)(value >> 48));
   }

   private static int byte0(int value) {
      return value & 255;
   }

   private static int byte1(int value) {
      return value >> 8 & 255;
   }

   private static int byte2(int value) {
      return value >> 16 & 255;
   }

   private static int byte3(int value) {
      return value >>> 24;
   }

   private static int nibble0(int value) {
      return value & 15;
   }

   private static int nibble1(int value) {
      return value >> 4 & 15;
   }

   private static int nibble2(int value) {
      return value >> 8 & 15;
   }

   private static int nibble3(int value) {
      return value >> 12 & 15;
   }

   // $FF: synthetic method
   InstructionCodec(Object x2) {
      this();
   }
}
