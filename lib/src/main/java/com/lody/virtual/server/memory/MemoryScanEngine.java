package com.lody.virtual.server.memory;

import com.lody.virtual.StringFog;
import com.lody.virtual.helper.utils.VLog;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MemoryScanEngine {
   private List<MappedMemoryRegion> regions;
   private int pid;
   private ProcessMemory memory;
   private static final int PAGE = 4096;
   private List<Match> matches;

   public MemoryScanEngine(int pid) throws IOException {
      this.pid = pid;
      this.memory = new ProcessMemory(pid);
      this.updateMemoryLayout();
   }

   public void updateMemoryLayout() {
      try {
         this.regions = MemoryRegionParser.getMemoryRegions(this.pid);
      } catch (IOException var2) {
         IOException e = var2;
         throw new IllegalStateException(e);
      }
   }

   public List<Match> getMatches() {
      return this.matches;
   }

   public void search(MemoryValue value) throws IOException {
      this.matches = new LinkedList();
      byte[] bytes = new byte[4096];
      byte[] valueBytes = value.toBytes();
      Iterator var4 = this.regions.iterator();

      while(var4.hasNext()) {
         MappedMemoryRegion region = (MappedMemoryRegion)var4.next();
         long start = region.startAddress;
         long end = region.endAddress;

         try {
            while(start < end) {
               int read = Math.min(bytes.length, (int)(end - start));
               read = this.memory.read(start, bytes, read);
               this.matches.addAll(this.matchBytes(region, start, bytes, read, valueBytes));
               start += 4096L;
            }
         } catch (IOException var11) {
            VLog.e(this.getClass().getSimpleName(), "Unable to read region : " + region.description);
         }
      }

   }

   public void modify(Match match, MemoryValue value) throws IOException {
      this.memory.write(match.address, value.toBytes());
   }

   public void modifyAll(MemoryValue value) throws IOException {
      Iterator var2 = this.matches.iterator();

      while(var2.hasNext()) {
         Match match = (Match)var2.next();
         this.modify(match, value);
      }

   }

   private List<Match> matchBytes(MappedMemoryRegion region, long startAddress, byte[] page, int read, byte[] value) {
      List<Match> matches = new LinkedList();
      int start = 0;
      int len = value.length;

      for(int step = 2; start < read; start += step) {
         boolean match = true;

         for(int i = 0; i < len && i + start < read; ++i) {
            if (page[start + i] != value[i]) {
               match = false;
               break;
            }
         }

         if (match) {
            matches.add(new Match(region, startAddress + (long)start, len));
         }
      }

      return matches;
   }

   public void close() {
      try {
         this.memory.close();
      } catch (IOException var2) {
         IOException e = var2;
         e.printStackTrace();
      }

   }

   public class Match {
      MappedMemoryRegion region;
      long address;
      int len;

      public Match(MappedMemoryRegion region, long address, int len) {
         this.region = region;
         this.address = address;
         this.len = len;
      }
   }
}
