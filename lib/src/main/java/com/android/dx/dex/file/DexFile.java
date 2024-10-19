package com.android.dx.dex.file;

import com.android.dex.util.ExceptionWithContext;
import com.android.dx.dex.DexOptions;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstBaseMethodRef;
import com.android.dx.rop.cst.CstCallSiteRef;
import com.android.dx.rop.cst.CstEnumRef;
import com.android.dx.rop.cst.CstFieldRef;
import com.android.dx.rop.cst.CstMethodHandle;
import com.android.dx.rop.cst.CstProtoRef;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.Type;
import com.android.dx.util.ByteArrayAnnotatedOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.Adler32;

public final class DexFile {
   private final DexOptions dexOptions;
   private final MixedItemSection wordData;
   private final MixedItemSection typeLists;
   private final MixedItemSection map;
   private final MixedItemSection stringData;
   private final StringIdsSection stringIds;
   private final TypeIdsSection typeIds;
   private final ProtoIdsSection protoIds;
   private final FieldIdsSection fieldIds;
   private final MethodIdsSection methodIds;
   private final ClassDefsSection classDefs;
   private final MixedItemSection classData;
   private final CallSiteIdsSection callSiteIds;
   private final MethodHandlesSection methodHandles;
   private final MixedItemSection byteData;
   private final HeaderSection header;
   private final Section[] sections;
   private int fileSize;
   private int dumpWidth;

   public DexFile(DexOptions dexOptions) {
      this.dexOptions = dexOptions;
      this.header = new HeaderSection(this);
      this.typeLists = new MixedItemSection((String)null, this, 4, MixedItemSection.SortType.NONE);
      this.wordData = new MixedItemSection("word_data", this, 4, MixedItemSection.SortType.TYPE);
      this.stringData = new MixedItemSection("string_data", this, 1, MixedItemSection.SortType.INSTANCE);
      this.classData = new MixedItemSection((String)null, this, 1, MixedItemSection.SortType.NONE);
      this.byteData = new MixedItemSection("byte_data", this, 1, MixedItemSection.SortType.TYPE);
      this.stringIds = new StringIdsSection(this);
      this.typeIds = new TypeIdsSection(this);
      this.protoIds = new ProtoIdsSection(this);
      this.fieldIds = new FieldIdsSection(this);
      this.methodIds = new MethodIdsSection(this);
      this.classDefs = new ClassDefsSection(this);
      this.map = new MixedItemSection("map", this, 4, MixedItemSection.SortType.NONE);
      if (dexOptions.apiIsSupported(26)) {
         this.callSiteIds = new CallSiteIdsSection(this);
         this.methodHandles = new MethodHandlesSection(this);
         this.sections = new Section[]{this.header, this.stringIds, this.typeIds, this.protoIds, this.fieldIds, this.methodIds, this.classDefs, this.callSiteIds, this.methodHandles, this.wordData, this.typeLists, this.stringData, this.byteData, this.classData, this.map};
      } else {
         this.callSiteIds = null;
         this.methodHandles = null;
         this.sections = new Section[]{this.header, this.stringIds, this.typeIds, this.protoIds, this.fieldIds, this.methodIds, this.classDefs, this.wordData, this.typeLists, this.stringData, this.byteData, this.classData, this.map};
      }

      this.fileSize = -1;
      this.dumpWidth = 79;
   }

   public boolean isEmpty() {
      return this.classDefs.items().isEmpty();
   }

   public DexOptions getDexOptions() {
      return this.dexOptions;
   }

   public void add(ClassDefItem clazz) {
      this.classDefs.add(clazz);
   }

   public ClassDefItem getClassOrNull(String name) {
      try {
         Type type = Type.internClassName(name);
         return (ClassDefItem)this.classDefs.get(new CstType(type));
      } catch (IllegalArgumentException var3) {
         return null;
      }
   }

   public void writeTo(OutputStream out, Writer humanOut, boolean verbose) throws IOException {
      this.writeTo(out, (Storage)null, humanOut, verbose);
   }

   public void writeTo(OutputStream out, Storage storage, Writer humanOut, boolean verbose) throws IOException {
      boolean annotate = humanOut != null;
      ByteArrayAnnotatedOutput result = this.toDex0(annotate, verbose, storage);
      if (out != null) {
         out.write(result.getArray());
      }

      if (annotate) {
         result.writeAnnotationsTo(humanOut);
      }

   }

   public ByteArrayAnnotatedOutput writeTo(Storage storage) {
      return this.toDex0(false, false, storage);
   }

   public byte[] toDex(Writer humanOut, boolean verbose) throws IOException {
      boolean annotate = humanOut != null;
      ByteArrayAnnotatedOutput result = this.toDex0(annotate, verbose, (Storage)null);
      if (annotate) {
         result.writeAnnotationsTo(humanOut);
      }

      return result.getArray();
   }

   public void setDumpWidth(int dumpWidth) {
      if (dumpWidth < 40) {
         throw new IllegalArgumentException("dumpWidth < 40");
      } else {
         this.dumpWidth = dumpWidth;
      }
   }

   public int getFileSize() {
      if (this.fileSize < 0) {
         throw new RuntimeException("file size not yet known");
      } else {
         return this.fileSize;
      }
   }

   MixedItemSection getStringData() {
      return this.stringData;
   }

   MixedItemSection getWordData() {
      return this.wordData;
   }

   MixedItemSection getTypeLists() {
      return this.typeLists;
   }

   MixedItemSection getMap() {
      return this.map;
   }

   StringIdsSection getStringIds() {
      return this.stringIds;
   }

   public ClassDefsSection getClassDefs() {
      return this.classDefs;
   }

   MixedItemSection getClassData() {
      return this.classData;
   }

   public TypeIdsSection getTypeIds() {
      return this.typeIds;
   }

   ProtoIdsSection getProtoIds() {
      return this.protoIds;
   }

   public FieldIdsSection getFieldIds() {
      return this.fieldIds;
   }

   public MethodIdsSection getMethodIds() {
      return this.methodIds;
   }

   public MethodHandlesSection getMethodHandles() {
      return this.methodHandles;
   }

   public CallSiteIdsSection getCallSiteIds() {
      return this.callSiteIds;
   }

   MixedItemSection getByteData() {
      return this.byteData;
   }

   Section getFirstDataSection() {
      return this.wordData;
   }

   Section getLastDataSection() {
      return this.map;
   }

   void internIfAppropriate(Constant cst) {
      if (cst == null) {
         throw new NullPointerException("cst == null");
      } else {
         if (cst instanceof CstString) {
            this.stringIds.intern((CstString)cst);
         } else if (cst instanceof CstType) {
            this.typeIds.intern((CstType)cst);
         } else if (cst instanceof CstBaseMethodRef) {
            this.methodIds.intern((CstBaseMethodRef)cst);
         } else if (cst instanceof CstFieldRef) {
            this.fieldIds.intern((CstFieldRef)cst);
         } else if (cst instanceof CstEnumRef) {
            this.fieldIds.intern(((CstEnumRef)cst).getFieldRef());
         } else if (cst instanceof CstProtoRef) {
            this.protoIds.intern(((CstProtoRef)cst).getPrototype());
         } else if (cst instanceof CstMethodHandle) {
            this.methodHandles.intern((CstMethodHandle)cst);
         }

      }
   }

   IndexedItem findItemOrNull(Constant cst) {
      if (cst instanceof CstString) {
         return this.stringIds.get(cst);
      } else if (cst instanceof CstType) {
         return this.typeIds.get(cst);
      } else if (cst instanceof CstBaseMethodRef) {
         return this.methodIds.get(cst);
      } else if (cst instanceof CstFieldRef) {
         return this.fieldIds.get(cst);
      } else if (cst instanceof CstEnumRef) {
         return this.fieldIds.intern(((CstEnumRef)cst).getFieldRef());
      } else if (cst instanceof CstProtoRef) {
         return this.protoIds.get(cst);
      } else if (cst instanceof CstMethodHandle) {
         return this.methodHandles.get(cst);
      } else {
         return cst instanceof CstCallSiteRef ? this.callSiteIds.get(cst) : null;
      }
   }

   private ByteArrayAnnotatedOutput toDex0(boolean annotate, boolean verbose, Storage storage) {
      this.classDefs.prepare();
      this.classData.prepare();
      this.wordData.prepare();
      if (this.dexOptions.apiIsSupported(26)) {
         this.callSiteIds.prepare();
      }

      this.byteData.prepare();
      if (this.dexOptions.apiIsSupported(26)) {
         this.methodHandles.prepare();
      }

      this.methodIds.prepare();
      this.fieldIds.prepare();
      this.protoIds.prepare();
      this.typeLists.prepare();
      this.typeIds.prepare();
      this.stringIds.prepare();
      this.stringData.prepare();
      this.header.prepare();
      int count = this.sections.length;
      int offset = 0;

      int placedAt;
      RuntimeException ex;
      for(int i = 0; i < count; ++i) {
         Section one = this.sections[i];
         if (one != this.callSiteIds && one != this.methodHandles || !one.items().isEmpty()) {
            placedAt = one.setFileOffset(offset);
            if (placedAt < offset) {
               throw new RuntimeException("bogus placement for section " + i);
            }

            try {
               if (one == this.map) {
                  MapItem.addMap(this.sections, this.map);
                  this.map.prepare();
               }

               if (one instanceof MixedItemSection) {
                  ((MixedItemSection)one).placeItems();
               }

               offset = placedAt + one.writeSize();
            } catch (RuntimeException var11) {
               ex = var11;
               throw ExceptionWithContext.withContext(ex, "...while writing section " + i);
            }
         }
      }

      this.fileSize = offset;
      byte[] barr = storage == null ? new byte[this.fileSize] : storage.getStorage(this.fileSize);
      ByteArrayAnnotatedOutput out = new ByteArrayAnnotatedOutput(barr);
      if (annotate) {
         out.enableAnnotations(this.dumpWidth, verbose);
      }

      for(placedAt = 0; placedAt < count; ++placedAt) {
         try {
            Section one = this.sections[placedAt];
            if (one != this.callSiteIds && one != this.methodHandles || !one.items().isEmpty()) {
               int zeroCount = one.getFileOffset() - out.getCursor();
               if (zeroCount < 0) {
                  throw new ExceptionWithContext("excess write of " + -zeroCount);
               }

               out.writeZeroes(zeroCount);
               one.writeTo(out);
            }
         } catch (RuntimeException var12) {
            ex = var12;
            ExceptionWithContext ec;
            if (ex instanceof ExceptionWithContext) {
               ec = (ExceptionWithContext)ex;
            } else {
               ec = new ExceptionWithContext(ex);
            }

            ec.addContext("...while writing section " + placedAt);
            throw ec;
         }
      }

      if (out.getCursor() != this.fileSize) {
         throw new RuntimeException("foreshortened write");
      } else {
         calcSignature(barr, out.getCursor());
         calcChecksum(barr, out.getCursor());
         if (annotate) {
            this.wordData.writeIndexAnnotation(out, ItemType.TYPE_CODE_ITEM, "\nmethod code index:\n\n");
            this.getStatistics().writeAnnotation(out);
            out.finishAnnotating();
         }

         return out;
      }
   }

   public Statistics getStatistics() {
      Statistics stats = new Statistics();
      Section[] var2 = this.sections;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Section s = var2[var4];
         stats.addAll(s);
      }

      return stats;
   }

   private static void calcSignature(byte[] bytes, int len) {
      MessageDigest md;
      try {
         md = MessageDigest.getInstance("SHA-1");
      } catch (NoSuchAlgorithmException var5) {
         NoSuchAlgorithmException ex = var5;
         throw new RuntimeException(ex);
      }

      md.update(bytes, 32, len - 32);

      try {
         int amt = md.digest(bytes, 12, 20);
         if (amt != 20) {
            throw new RuntimeException("unexpected digest write: " + amt + " bytes");
         }
      } catch (DigestException var4) {
         DigestException ex = var4;
         throw new RuntimeException(ex);
      }
   }

   private static void calcChecksum(byte[] bytes, int len) {
      Adler32 a32 = new Adler32();
      a32.update(bytes, 12, len - 12);
      int sum = (int)a32.getValue();
      bytes[8] = (byte)sum;
      bytes[9] = (byte)(sum >> 8);
      bytes[10] = (byte)(sum >> 16);
      bytes[11] = (byte)(sum >> 24);
   }

   public static final class Storage {
      byte[] storage;

      public Storage(byte[] storage) {
         this.storage = storage;
      }

      public byte[] getStorage(int requestedLength) {
         if (this.storage.length < requestedLength) {
            Logger.getAnonymousLogger().log(Level.FINER, "DexFile storage too small  " + this.storage.length + " vs " + requestedLength);
            this.storage = new byte[requestedLength];
         }

         return this.storage;
      }
   }
}
