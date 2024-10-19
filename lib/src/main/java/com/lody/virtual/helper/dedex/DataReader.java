package com.lody.virtual.helper.dedex;

import com.lody.virtual.StringFog;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.Iterator;

public class DataReader implements Closeable {
   private final RandomAccessFile mRaf;
   private final File mFile;
   private final MappedByteBuffer mMappedBuffer;
   private ArrayList<DataReader> mAssociatedReaders;

   public DataReader(String file) throws Exception {
      this(new File(file));
   }

   public DataReader(File file) throws Exception {
      this.mFile = file;
      this.mRaf = new RandomAccessFile(this.mFile, "r");
      this.mMappedBuffer = this.mRaf.getChannel().map(MapMode.READ_ONLY, 0L, file.length());
      this.mMappedBuffer.rewind();
      this.setLittleEndian(true);
   }

   public void setLittleEndian(boolean isLittleEndian) {
      this.mMappedBuffer.order(isLittleEndian ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN);
   }

   public void seek(long offset) {
      this.position((int)offset);
   }

   public void position(int newPosition) {
      this.mMappedBuffer.position(newPosition);
   }

   public int position() {
      return this.mMappedBuffer.position();
   }

   public int readByte() {
      return this.mMappedBuffer.get() & 255;
   }

   public void readBytes(byte[] b) {
      this.mMappedBuffer.get(b, 0, b.length);
   }

   public void readBytes(char[] b) {
      byte[] bs = new byte[b.length];
      this.readBytes(bs);

      for(int i = 0; i < b.length; ++i) {
         b[i] = (char)bs[i];
      }

   }

   public short readShort() {
      return this.mMappedBuffer.getShort();
   }

   public int readInt() {
      return this.mMappedBuffer.getInt();
   }

   public int previewInt() {
      this.mMappedBuffer.mark();
      int value = this.readInt();
      this.mMappedBuffer.reset();
      return value;
   }

   public final long readLong() {
      return this.mMappedBuffer.getLong();
   }

   public int readUleb128() {
      int result = this.readByte();
      if (result > 127) {
         int curVal = this.readByte();
         result = result & 127 | (curVal & 127) << 7;
         if (curVal > 127) {
            curVal = this.readByte();
            result |= (curVal & 127) << 14;
            if (curVal > 127) {
               curVal = this.readByte();
               result |= (curVal & 127) << 21;
               if (curVal > 127) {
                  curVal = this.readByte();
                  result |= curVal << 28;
               }
            }
         }
      }

      return result;
   }

   public File getFile() {
      return this.mFile;
   }

   public FileChannel getChannel() {
      return this.mRaf.getChannel();
   }

   public void addAssociatedReader(DataReader reader) {
      if (this.mAssociatedReaders == null) {
         this.mAssociatedReaders = new ArrayList();
      }

      this.mAssociatedReaders.add(reader);
   }

   public void close() {
      try {
         this.mRaf.close();
      } catch (IOException var3) {
         IOException e = var3;
         e.printStackTrace();
      }

      if (this.mAssociatedReaders != null) {
         Iterator var4 = this.mAssociatedReaders.iterator();

         while(var4.hasNext()) {
            DataReader r = (DataReader)var4.next();
            r.close();
         }
      }

   }

   public static int toInt(String str) {
      int len = str.length();
      int p = 0;
      char[] sb = new char[len];

      for(int i = 0; i < len; ++i) {
         char c = str.charAt(i);
         if (c >= '0' && c <= '9' || c == '-') {
            sb[p++] = c;
         }
      }

      return p == 0 ? 0 : Integer.parseInt(new String(sb, 0, p));
   }
}
