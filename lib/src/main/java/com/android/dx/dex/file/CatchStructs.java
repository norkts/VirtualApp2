package com.android.dx.dex.file;

import com.android.dx.dex.code.CatchHandlerList;
import com.android.dx.dex.code.CatchTable;
import com.android.dx.dex.code.DalvCode;
import com.android.dx.util.AnnotatedOutput;
import com.android.dx.util.ByteArrayAnnotatedOutput;
import com.android.dx.util.Hex;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public final class CatchStructs {
   private static final int TRY_ITEM_WRITE_SIZE = 8;
   private final DalvCode code;
   private CatchTable table;
   private byte[] encodedHandlers;
   private int encodedHandlerHeaderSize;
   private TreeMap<CatchHandlerList, Integer> handlerOffsets;

   public CatchStructs(DalvCode code) {
      this.code = code;
      this.table = null;
      this.encodedHandlers = null;
      this.encodedHandlerHeaderSize = 0;
      this.handlerOffsets = null;
   }

   private void finishProcessingIfNecessary() {
      if (this.table == null) {
         this.table = this.code.getCatches();
      }

   }

   public int triesSize() {
      this.finishProcessingIfNecessary();
      return this.table.size();
   }

   public void debugPrint(PrintWriter out, String prefix) {
      this.annotateEntries(prefix, out, (AnnotatedOutput)null);
   }

   public void encode(DexFile file) {
      this.finishProcessingIfNecessary();
      TypeIdsSection typeIds = file.getTypeIds();
      int size = this.table.size();
      this.handlerOffsets = new TreeMap();

      for(int i = 0; i < size; ++i) {
         this.handlerOffsets.put(this.table.get(i).getHandlers(), (Object)null);
      }

      if (this.handlerOffsets.size() > 65535) {
         throw new UnsupportedOperationException("too many catch handlers");
      } else {
         ByteArrayAnnotatedOutput out = new ByteArrayAnnotatedOutput();
         this.encodedHandlerHeaderSize = out.writeUleb128(this.handlerOffsets.size());
         Iterator var5 = this.handlerOffsets.entrySet().iterator();

         while(var5.hasNext()) {
            Map.Entry<CatchHandlerList, Integer> mapping = (Map.Entry)var5.next();
            CatchHandlerList list = (CatchHandlerList)mapping.getKey();
            int listSize = list.size();
            boolean catchesAll = list.catchesAll();
            mapping.setValue(out.getCursor());
            if (catchesAll) {
               out.writeSleb128(-(listSize - 1));
               --listSize;
            } else {
               out.writeSleb128(listSize);
            }

            for(int i = 0; i < listSize; ++i) {
               CatchHandlerList.Entry entry = list.get(i);
               out.writeUleb128(typeIds.indexOf(entry.getExceptionType()));
               out.writeUleb128(entry.getHandler());
            }

            if (catchesAll) {
               out.writeUleb128(list.get(listSize).getHandler());
            }
         }

         this.encodedHandlers = out.toByteArray();
      }
   }

   public int writeSize() {
      return this.triesSize() * 8 + this.encodedHandlers.length;
   }

   public void writeTo(DexFile file, AnnotatedOutput out) {
      this.finishProcessingIfNecessary();
      if (out.annotates()) {
         this.annotateEntries("  ", (PrintWriter)null, out);
      }

      int tableSize = this.table.size();

      for(int i = 0; i < tableSize; ++i) {
         CatchTable.Entry one = this.table.get(i);
         int start = one.getStart();
         int end = one.getEnd();
         int insnCount = end - start;
         if (insnCount >= 65536) {
            throw new UnsupportedOperationException("bogus exception range: " + Hex.u4(start) + ".." + Hex.u4(end));
         }

         out.writeInt(start);
         out.writeShort(insnCount);
         out.writeShort((Integer)this.handlerOffsets.get(one.getHandlers()));
      }

      out.write(this.encodedHandlers);
   }

   private void annotateEntries(String prefix, PrintWriter printTo, AnnotatedOutput annotateTo) {
      this.finishProcessingIfNecessary();
      boolean consume = annotateTo != null;
      int amt1 = consume ? 6 : 0;
      int amt2 = consume ? 2 : 0;
      int size = this.table.size();
      String subPrefix = prefix + "  ";
      if (consume) {
         annotateTo.annotate(0, prefix + "tries:");
      } else {
         printTo.println(prefix + "tries:");
      }

      int lastOffset;
      for(lastOffset = 0; lastOffset < size; ++lastOffset) {
         CatchTable.Entry entry = this.table.get(lastOffset);
         CatchHandlerList handlers = entry.getHandlers();
         String s1 = subPrefix + "try " + Hex.u2or4(entry.getStart()) + ".." + Hex.u2or4(entry.getEnd());
         String s2 = handlers.toHuman(subPrefix, "");
         if (consume) {
            annotateTo.annotate(amt1, s1);
            annotateTo.annotate(amt2, s2);
         } else {
            printTo.println(s1);
            printTo.println(s2);
         }
      }

      if (consume) {
         annotateTo.annotate(0, prefix + "handlers:");
         annotateTo.annotate(this.encodedHandlerHeaderSize, subPrefix + "size: " + Hex.u2(this.handlerOffsets.size()));
         lastOffset = 0;
         CatchHandlerList lastList = null;

         int offset;
         for(Iterator var16 = this.handlerOffsets.entrySet().iterator(); var16.hasNext(); lastOffset = offset) {
            Map.Entry<CatchHandlerList, Integer> mapping = (Map.Entry)var16.next();
            CatchHandlerList list = (CatchHandlerList)mapping.getKey();
            offset = (Integer)mapping.getValue();
            if (lastList != null) {
               annotateAndConsumeHandlers(lastList, lastOffset, offset - lastOffset, subPrefix, printTo, annotateTo);
            }

            lastList = list;
         }

         annotateAndConsumeHandlers(lastList, lastOffset, this.encodedHandlers.length - lastOffset, subPrefix, printTo, annotateTo);
      }
   }

   private static void annotateAndConsumeHandlers(CatchHandlerList handlers, int offset, int size, String prefix, PrintWriter printTo, AnnotatedOutput annotateTo) {
      String s = handlers.toHuman(prefix, Hex.u2(offset) + ": ");
      if (printTo != null) {
         printTo.println(s);
      }

      annotateTo.annotate(size, s);
   }
}
