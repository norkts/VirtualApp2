package com.android.dx;

import com.android.dx.rop.code.BasicBlock;
import com.android.dx.rop.code.Insn;
import com.android.dx.rop.code.InsnList;
import com.android.dx.util.IntList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class Label {
   final List<Insn> instructions = new ArrayList();
   Code code;
   boolean marked = false;
   List<Label> catchLabels = Collections.emptyList();
   Label primarySuccessor;
   Label alternateSuccessor;
   int id = -1;

   boolean isEmpty() {
      return this.instructions.isEmpty();
   }

   void compact() {
      for(int i = 0; i < this.catchLabels.size(); ++i) {
         while(((Label)this.catchLabels.get(i)).isEmpty()) {
            this.catchLabels.set(i, ((Label)this.catchLabels.get(i)).primarySuccessor);
         }
      }

      while(this.primarySuccessor != null && this.primarySuccessor.isEmpty()) {
         this.primarySuccessor = this.primarySuccessor.primarySuccessor;
      }

      while(this.alternateSuccessor != null && this.alternateSuccessor.isEmpty()) {
         this.alternateSuccessor = this.alternateSuccessor.primarySuccessor;
      }

   }

   BasicBlock toBasicBlock() {
      InsnList result = new InsnList(this.instructions.size());

      int primarySuccessorIndex;
      for(primarySuccessorIndex = 0; primarySuccessorIndex < this.instructions.size(); ++primarySuccessorIndex) {
         result.set(primarySuccessorIndex, (Insn)this.instructions.get(primarySuccessorIndex));
      }

      result.setImmutable();
      primarySuccessorIndex = -1;
      IntList successors = new IntList();
      Iterator var4 = this.catchLabels.iterator();

      while(var4.hasNext()) {
         Label catchLabel = (Label)var4.next();
         successors.add(catchLabel.id);
      }

      if (this.primarySuccessor != null) {
         primarySuccessorIndex = this.primarySuccessor.id;
         successors.add(primarySuccessorIndex);
      }

      if (this.alternateSuccessor != null) {
         successors.add(this.alternateSuccessor.id);
      }

      successors.setImmutable();
      return new BasicBlock(this.id, result, successors, primarySuccessorIndex);
   }
}
