package com.android.dx.ssa;

import com.android.dx.util.IntSet;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;

public class DomFront {
   private static final boolean DEBUG = false;
   private final SsaMethod meth;
   private final ArrayList<SsaBasicBlock> nodes;
   private final DomInfo[] domInfos;

   public DomFront(SsaMethod meth) {
      this.meth = meth;
      this.nodes = meth.getBlocks();
      int szNodes = this.nodes.size();
      this.domInfos = new DomInfo[szNodes];

      for(int i = 0; i < szNodes; ++i) {
         this.domInfos[i] = new DomInfo();
      }

   }

   public DomInfo[] run() {
      int szNodes = this.nodes.size();
      Dominators methDom = Dominators.make(this.meth, this.domInfos, false);
      this.buildDomTree();

      for(int i = 0; i < szNodes; ++i) {
         this.domInfos[i].dominanceFrontiers = SetFactory.makeDomFrontSet(szNodes);
      }

      this.calcDomFronts();
      return this.domInfos;
   }

   private void debugPrintDomChildren() {
      int szNodes = this.nodes.size();

      for(int i = 0; i < szNodes; ++i) {
         SsaBasicBlock node = (SsaBasicBlock)this.nodes.get(i);
         StringBuffer sb = new StringBuffer();
         sb.append('{');
         boolean comma = false;

         for(Iterator var6 = node.getDomChildren().iterator(); var6.hasNext(); comma = true) {
            SsaBasicBlock child = (SsaBasicBlock)var6.next();
            if (comma) {
               sb.append(',');
            }

            sb.append(child);
         }

         sb.append('}');
         System.out.println("domChildren[" + node + "]: " + sb);
      }

   }

   private void buildDomTree() {
      int szNodes = this.nodes.size();

      for(int i = 0; i < szNodes; ++i) {
         DomInfo info = this.domInfos[i];
         if (info.idom != -1) {
            SsaBasicBlock domParent = (SsaBasicBlock)this.nodes.get(info.idom);
            domParent.addDomChild((SsaBasicBlock)this.nodes.get(i));
         }
      }

   }

   private void calcDomFronts() {
      int szNodes = this.nodes.size();

      for(int b = 0; b < szNodes; ++b) {
         SsaBasicBlock nb = (SsaBasicBlock)this.nodes.get(b);
         DomInfo nbInfo = this.domInfos[b];
         BitSet pred = nb.getPredecessors();
         if (pred.cardinality() > 1) {
            DomInfo runnerInfo;
            for(int i = pred.nextSetBit(0); i >= 0; i = pred.nextSetBit(i + 1)) {
               for(int runnerIndex = i; runnerIndex != nbInfo.idom && runnerIndex != -1; runnerIndex = runnerInfo.idom) {
                  runnerInfo = this.domInfos[runnerIndex];
                  if (runnerInfo.dominanceFrontiers.has(b)) {
                     break;
                  }

                  runnerInfo.dominanceFrontiers.add(b);
               }
            }
         }
      }

   }

   public static class DomInfo {
      public IntSet dominanceFrontiers;
      public int idom = -1;
   }
}
