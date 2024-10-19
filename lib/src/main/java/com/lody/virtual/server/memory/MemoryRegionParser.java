package com.lody.virtual.server.memory;

import com.lody.virtual.StringFog;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MemoryRegionParser {
   public static final String PATTERN = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PBVaKHpTAjdODjxTOSkbD3w2JwJ1Ch4sPBgiEUkkGkhlIAYKLF8mEXoJRRdsMw4CDwMcX2tTTRN8IwpJKi06HXUYHgNJHwEsOgQcOX8jHh16Ix4NKTpfWk9TQAZpClEvJSo9I343RRd/DQEgOwNbLWAOGT1jNBE/JxgpMXUYHgN0IB02OTkcVg=="));
   public static final Pattern MAPS_LINE_PATTERN = Pattern.compile(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PBVaKHpTAjdODjxTOSkbD3w2JwJ1Ch4sPBgiEUkkGkhlIAYKLF8mEXoJRRdsMw4CDwMcX2tTTRN8IwpJKi06HXUYHgNJHwEsOgQcOX8jHh16Ix4NKTpfWk9TQAZpClEvJSo9I343RRd/DQEgOwNbLWAOGT1jNBE/JxgpMXUYHgN0IB02OTkcVg==")), 2);

   private static long parseHex(String s) {
      return Long.parseLong(s, 16);
   }

   private static MappedMemoryRegion parseMapLine(String line) {
      line = line.trim();
      Matcher m = MAPS_LINE_PATTERN.matcher(line);
      if (!m.matches()) {
         throw new IllegalArgumentException(String.format(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IRhfM3sKIARgJzwzKBcMPn4zOC9lNysrLggAJ2EkOClsJzMpKS5bCmgjRDRsAVk0DRgiKGoVLDFqES83LT4AKnsJGgJhNB45Oik2KmwjASVlATg7KTo6KGMKRSBlIBEpJwg2MmsFAS57DSgc")), line));
      } else if (m.groupCount() != 11) {
         throw new InternalError(String.format(Locale.ENGLISH, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JAgcLmsVHi9iVyQ9Iz1fLWo3TSllJCgbKgNWJGowAjBsNDMpPy4bKHgVFi9sDTw0IRgiLGkjLDFpM1A0LRhSVg==")), m.groupCount(), 12));
      } else {
         long start = parseHex(m.group(1));
         long end = parseHex(m.group(2));
         boolean read = m.group(3).equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj5SVg==")));
         boolean write = m.group(4).equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KS5SVg==")));
         boolean exec = m.group(5).equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KBhSVg==")));
         boolean shared = m.group(6).equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki5SVg==")));
         long fileOffset = parseHex(m.group(7));
         long majorDevNum = parseHex(m.group(8));
         long minorDevNum = parseHex(m.group(9));
         long inode = parseHex(m.group(10));
         String desc = m.group(11);
         return new MappedMemoryRegion(start, end, read, write, exec, shared, fileOffset, majorDevNum, minorDevNum, inode, desc);
      }
   }

   public static List<MappedMemoryRegion> getMemoryRegions(int pid) throws IOException {
      List<MappedMemoryRegion> list = new LinkedList();
      BufferedReader reader = new BufferedReader(new FileReader(String.format(Locale.ENGLISH, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("My06KmozLyVIDg01KgciKmoFSFo=")), pid)));

      String line;
      while((line = reader.readLine()) != null) {
         MappedMemoryRegion region = parseMapLine(line);
         if (region.isReadable && region.isWritable && !region.description.endsWith(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PBgqM2oFNAZiDg0z")))) {
            list.add(region);
         }
      }

      return list;
   }
}
