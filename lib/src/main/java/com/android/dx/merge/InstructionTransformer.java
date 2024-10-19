package com.android.dx.merge;

import com.android.dex.DexException;
import com.android.dex.DexIndexOverflowException;
import com.android.dx.io.CodeReader;
import com.android.dx.io.instructions.DecodedInstruction;
import com.android.dx.io.instructions.ShortArrayCodeOutput;

final class InstructionTransformer {
   private final CodeReader reader = new CodeReader();
   private DecodedInstruction[] mappedInstructions;
   private int mappedAt;
   private IndexMap indexMap;

   public InstructionTransformer() {
      this.reader.setAllVisitors(new GenericVisitor());
      this.reader.setStringVisitor(new StringVisitor());
      this.reader.setTypeVisitor(new TypeVisitor());
      this.reader.setFieldVisitor(new FieldVisitor());
      this.reader.setMethodVisitor(new MethodVisitor());
      this.reader.setMethodAndProtoVisitor(new MethodAndProtoVisitor());
      this.reader.setCallSiteVisitor(new CallSiteVisitor());
   }

   public short[] transform(IndexMap indexMap, short[] encodedInstructions) throws DexException {
      DecodedInstruction[] decodedInstructions = DecodedInstruction.decodeAll(encodedInstructions);
      int size = decodedInstructions.length;
      this.indexMap = indexMap;
      this.mappedInstructions = new DecodedInstruction[size];
      this.mappedAt = 0;
      this.reader.visitAll(decodedInstructions);
      ShortArrayCodeOutput out = new ShortArrayCodeOutput(size);
      DecodedInstruction[] var6 = this.mappedInstructions;
      int var7 = var6.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         DecodedInstruction instruction = var6[var8];
         if (instruction != null) {
            instruction.encode(out);
         }
      }

      this.indexMap = null;
      return out.getArray();
   }

   private static void jumboCheck(boolean isJumbo, int newIndex) {
      if (!isJumbo && newIndex > 65535) {
         throw new DexIndexOverflowException("Cannot merge new index " + newIndex + " into a non-jumbo instruction!");
      }
   }

   private class CallSiteVisitor implements CodeReader.Visitor {
      private CallSiteVisitor() {
      }

      public void visit(DecodedInstruction[] all, DecodedInstruction one) {
         int callSiteId = one.getIndex();
         int mappedCallSiteId = InstructionTransformer.this.indexMap.adjustCallSite(callSiteId);
         InstructionTransformer.this.mappedInstructions[InstructionTransformer.this.mappedAt++] = one.withIndex(mappedCallSiteId);
      }

      // $FF: synthetic method
      CallSiteVisitor(Object x1) {
         this();
      }
   }

   private class MethodAndProtoVisitor implements CodeReader.Visitor {
      private MethodAndProtoVisitor() {
      }

      public void visit(DecodedInstruction[] all, DecodedInstruction one) {
         int methodId = one.getIndex();
         int protoId = one.getProtoIndex();
         InstructionTransformer.this.mappedInstructions[InstructionTransformer.this.mappedAt++] = one.withProtoIndex(InstructionTransformer.this.indexMap.adjustMethod(methodId), InstructionTransformer.this.indexMap.adjustProto(protoId));
      }

      // $FF: synthetic method
      MethodAndProtoVisitor(Object x1) {
         this();
      }
   }

   private class MethodVisitor implements CodeReader.Visitor {
      private MethodVisitor() {
      }

      public void visit(DecodedInstruction[] all, DecodedInstruction one) {
         int methodId = one.getIndex();
         int mappedId = InstructionTransformer.this.indexMap.adjustMethod(methodId);
         boolean isJumbo = one.getOpcode() == 27;
         InstructionTransformer.jumboCheck(isJumbo, mappedId);
         InstructionTransformer.this.mappedInstructions[InstructionTransformer.this.mappedAt++] = one.withIndex(mappedId);
      }

      // $FF: synthetic method
      MethodVisitor(Object x1) {
         this();
      }
   }

   private class TypeVisitor implements CodeReader.Visitor {
      private TypeVisitor() {
      }

      public void visit(DecodedInstruction[] all, DecodedInstruction one) {
         int typeId = one.getIndex();
         int mappedId = InstructionTransformer.this.indexMap.adjustType(typeId);
         boolean isJumbo = one.getOpcode() == 27;
         InstructionTransformer.jumboCheck(isJumbo, mappedId);
         InstructionTransformer.this.mappedInstructions[InstructionTransformer.this.mappedAt++] = one.withIndex(mappedId);
      }

      // $FF: synthetic method
      TypeVisitor(Object x1) {
         this();
      }
   }

   private class FieldVisitor implements CodeReader.Visitor {
      private FieldVisitor() {
      }

      public void visit(DecodedInstruction[] all, DecodedInstruction one) {
         int fieldId = one.getIndex();
         int mappedId = InstructionTransformer.this.indexMap.adjustField(fieldId);
         boolean isJumbo = one.getOpcode() == 27;
         InstructionTransformer.jumboCheck(isJumbo, mappedId);
         InstructionTransformer.this.mappedInstructions[InstructionTransformer.this.mappedAt++] = one.withIndex(mappedId);
      }

      // $FF: synthetic method
      FieldVisitor(Object x1) {
         this();
      }
   }

   private class StringVisitor implements CodeReader.Visitor {
      private StringVisitor() {
      }

      public void visit(DecodedInstruction[] all, DecodedInstruction one) {
         int stringId = one.getIndex();
         int mappedId = InstructionTransformer.this.indexMap.adjustString(stringId);
         boolean isJumbo = one.getOpcode() == 27;
         InstructionTransformer.jumboCheck(isJumbo, mappedId);
         InstructionTransformer.this.mappedInstructions[InstructionTransformer.this.mappedAt++] = one.withIndex(mappedId);
      }

      // $FF: synthetic method
      StringVisitor(Object x1) {
         this();
      }
   }

   private class GenericVisitor implements CodeReader.Visitor {
      private GenericVisitor() {
      }

      public void visit(DecodedInstruction[] all, DecodedInstruction one) {
         InstructionTransformer.this.mappedInstructions[InstructionTransformer.this.mappedAt++] = one;
      }

      // $FF: synthetic method
      GenericVisitor(Object x1) {
         this();
      }
   }
}
