package com.android.dx;

import com.android.dx.dex.DexOptions;
import com.android.dx.dex.code.DalvCode;
import com.android.dx.dex.code.RopTranslator;
import com.android.dx.dex.file.ClassDefItem;
import com.android.dx.dex.file.DexFile;
import com.android.dx.dex.file.EncodedField;
import com.android.dx.dex.file.EncodedMethod;
import com.android.dx.rop.code.LocalVariableInfo;
import com.android.dx.rop.code.RopMethod;
import com.android.dx.rop.cst.CstString;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.StdTypeList;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

public final class DexMaker {
   private final Map<TypeId<?>, TypeDeclaration> types = new LinkedHashMap();
   private static boolean didWarnBlacklistedMethods;
   private static boolean didWarnNonBaseDexClassLoader;
   private ClassLoader sharedClassLoader;
   private DexFile outputDex;
   private boolean markAsTrusted;

   TypeDeclaration getTypeDeclaration(TypeId<?> type) {
      TypeDeclaration result = (TypeDeclaration)this.types.get(type);
      if (result == null) {
         result = new TypeDeclaration(type);
         this.types.put(type, result);
      }

      return result;
   }

   public void declare(TypeId<?> type, String sourceFile, int flags, TypeId<?> supertype, TypeId<?>... interfaces) {
      TypeDeclaration declaration = this.getTypeDeclaration(type);
      int supportedFlags = 5137;
      if ((flags & ~supportedFlags) != 0) {
         throw new IllegalArgumentException("Unexpected flag: " + Integer.toHexString(flags));
      } else if (declaration.declared) {
         throw new IllegalStateException("already declared: " + type);
      } else {
         declaration.declared = true;
         declaration.flags = flags;
         declaration.supertype = supertype;
         declaration.sourceFile = sourceFile;
         declaration.interfaces = new TypeList(interfaces);
      }
   }

   public Code declare(MethodId<?, ?> method, int flags) {
      TypeDeclaration typeDeclaration = this.getTypeDeclaration(method.declaringType);
      if (typeDeclaration.methods.containsKey(method)) {
         throw new IllegalStateException("already declared: " + method);
      } else {
         int supportedFlags = 4223;
         if ((flags & ~supportedFlags) != 0) {
            throw new IllegalArgumentException("Unexpected flag: " + Integer.toHexString(flags));
         } else {
            if ((flags & 32) != 0) {
               flags = flags & -33 | 131072;
            }

            if (method.isConstructor() || method.isStaticInitializer()) {
               flags |= 65536;
            }

            MethodDeclaration methodDeclaration = new MethodDeclaration(method, flags);
            typeDeclaration.methods.put(method, methodDeclaration);
            return methodDeclaration.code;
         }
      }
   }

   public void declare(FieldId<?, ?> fieldId, int flags, Object staticValue) {
      TypeDeclaration typeDeclaration = this.getTypeDeclaration(fieldId.declaringType);
      if (typeDeclaration.fields.containsKey(fieldId)) {
         throw new IllegalStateException("already declared: " + fieldId);
      } else {
         int supportedFlags = 4319;
         if ((flags & ~supportedFlags) != 0) {
            throw new IllegalArgumentException("Unexpected flag: " + Integer.toHexString(flags));
         } else if ((flags & 8) == 0 && staticValue != null) {
            throw new IllegalArgumentException("staticValue is non-null, but field is not static");
         } else {
            FieldDeclaration fieldDeclaration = new FieldDeclaration(fieldId, flags, staticValue);
            typeDeclaration.fields.put(fieldId, fieldDeclaration);
         }
      }
   }

   public byte[] generate() {
      if (this.outputDex == null) {
         DexOptions options = new DexOptions();
         options.minSdkVersion = 13;
         this.outputDex = new DexFile(options);
      }

      Iterator var4 = this.types.values().iterator();

      while(var4.hasNext()) {
         TypeDeclaration typeDeclaration = (TypeDeclaration)var4.next();
         this.outputDex.add(typeDeclaration.toClassDefItem());
      }

      try {
         return this.outputDex.toDex((Writer)null, false);
      } catch (IOException var3) {
         IOException e = var3;
         throw new RuntimeException(e);
      }
   }

   private String generateFileName() {
      int checksum = 1;
      Set<TypeId<?>> typesKeySet = this.types.keySet();
      Iterator<TypeId<?>> it = typesKeySet.iterator();
      int[] checksums = new int[typesKeySet.size()];
      int i = 0;

      int sum;
      while(it.hasNext()) {
         TypeId<?> typeId = (TypeId)it.next();
         TypeDeclaration decl = this.getTypeDeclaration(typeId);
         Set<MethodId> methodSet = decl.methods.keySet();
         if (decl.supertype != null) {
            sum = 31 * decl.supertype.hashCode() + decl.interfaces.hashCode();
            checksums[i++] = 31 * sum + methodSet.hashCode();
         }
      }

      Arrays.sort(checksums);
      int[] var10 = checksums;
      int var11 = checksums.length;

      for(int var12 = 0; var12 < var11; ++var12) {
         sum = var10[var12];
         checksum *= 31;
         checksum += sum;
      }

      return "Generated_" + checksum + ".jar";
   }

   public void setSharedClassLoader(ClassLoader classLoader) {
      this.sharedClassLoader = classLoader;
   }

   public void markAsTrusted() {
      this.markAsTrusted = true;
   }

   private ClassLoader generateClassLoader(File result, File dexCache, ClassLoader parent) {
      try {
         boolean shareClassLoader = this.sharedClassLoader != null;
         ClassLoader preferredClassLoader = null;
         if (parent != null) {
            preferredClassLoader = parent;
         } else if (this.sharedClassLoader != null) {
            preferredClassLoader = this.sharedClassLoader;
         }

         Class baseDexClassLoaderClass = Class.forName("dalvik.system.BaseDexClassLoader");
         if (shareClassLoader && !baseDexClassLoaderClass.isAssignableFrom(preferredClassLoader.getClass())) {
            if (!preferredClassLoader.getClass().getName().equals("java.lang.BootClassLoader") && !didWarnNonBaseDexClassLoader) {
               System.err.println("Cannot share classloader as shared classloader '" + preferredClassLoader + "' is not a subclass of '" + baseDexClassLoaderClass + "'");
               didWarnNonBaseDexClassLoader = true;
            }

            shareClassLoader = false;
         }

         if (this.markAsTrusted) {
            try {
               if (shareClassLoader) {
                  preferredClassLoader.getClass().getMethod("addDexPath", String.class, Boolean.TYPE).invoke(preferredClassLoader, result.getPath(), true);
                  return preferredClassLoader;
               }

               return (ClassLoader)baseDexClassLoaderClass.getConstructor(String.class, File.class, String.class, ClassLoader.class, Boolean.TYPE).newInstance(result.getPath(), dexCache.getAbsoluteFile(), null, preferredClassLoader, true);
            } catch (InvocationTargetException var8) {
               InvocationTargetException e = var8;
               if (!(e.getCause() instanceof SecurityException)) {
                  throw e;
               }

               if (!didWarnBlacklistedMethods) {
                  System.err.println("Cannot allow to call blacklisted super methods. This might break spying on system classes." + e.getCause());
                  didWarnBlacklistedMethods = true;
               }
            }
         }

         if (shareClassLoader) {
            preferredClassLoader.getClass().getMethod("addDexPath", String.class).invoke(preferredClassLoader, result.getPath());
            return preferredClassLoader;
         } else {
            return (ClassLoader)Class.forName("dalvik.system.DexClassLoader").getConstructor(String.class, String.class, String.class, ClassLoader.class).newInstance(result.getPath(), dexCache.getAbsolutePath(), null, preferredClassLoader);
         }
      } catch (ClassNotFoundException var9) {
         ClassNotFoundException e = var9;
         throw new UnsupportedOperationException("load() requires a Dalvik VM", e);
      } catch (InvocationTargetException var10) {
         InvocationTargetException e = var10;
         throw new RuntimeException(e.getCause());
      } catch (InstantiationException var11) {
         throw new AssertionError();
      } catch (NoSuchMethodException var12) {
         throw new AssertionError();
      } catch (IllegalAccessException var13) {
         throw new AssertionError();
      }
   }

   public ClassLoader loadClassDirect(ClassLoader parent, File dexCache, String dexFileName) {
      File result = new File(dexCache, dexFileName);
      return result.exists() ? this.generateClassLoader(result, dexCache, parent) : null;
   }

   public ClassLoader generateAndLoad(ClassLoader parent, File dexCache) throws IOException {
      return this.generateAndLoad(parent, dexCache, this.generateFileName());
   }

   public ClassLoader generateAndLoad(ClassLoader parent, File dexCache, String dexFileName) throws IOException {
      if (dexCache == null) {
         String property = System.getProperty("dexmaker.dexcache");
         if (property != null) {
            dexCache = new File(property);
         } else {
            dexCache = (new AppDataDirGuesser()).guess();
            if (dexCache == null) {
               throw new IllegalArgumentException("dexcache == null (and no default could be found; consider setting the 'dexmaker.dexcache' system property)");
            }
         }
      }

      File result = new File(dexCache, dexFileName);
      if (result.exists()) {
         try {
            this.deleteOldDex(result);
         } catch (Throwable var9) {
         }
      }

      File parentDir = result.getParentFile();
      if (!parentDir.exists()) {
         parentDir.mkdirs();
      }

      result.createNewFile();
      JarOutputStream jarOut = new JarOutputStream(new FileOutputStream(result));
      JarEntry entry = new JarEntry("classes.dex");
      byte[] dex = this.generate();
      entry.setSize((long)dex.length);
      jarOut.putNextEntry(entry);
      jarOut.write(dex);
      jarOut.closeEntry();
      jarOut.close();
      return this.generateClassLoader(result, dexCache, parent);
   }

   public void deleteOldDex(File dexFile) {
      dexFile.delete();
      String dexDir = dexFile.getParent();
      File oatDir = new File(dexDir, "/oat/");
      File oatDirArm = new File(oatDir, "/arm/");
      File oatDirArm64 = new File(oatDir, "/arm64/");
      if (oatDir.exists()) {
         String nameStart = dexFile.getName().replaceAll(".jar", "");
         this.doDeleteOatFiles(oatDir, nameStart);
         this.doDeleteOatFiles(oatDirArm, nameStart);
         this.doDeleteOatFiles(oatDirArm64, nameStart);
      }
   }

   private void doDeleteOatFiles(File dir, String nameStart) {
      if (dir.exists()) {
         File[] oats = dir.listFiles();
         if (oats != null) {
            File[] var4 = oats;
            int var5 = oats.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               File oatFile = var4[var6];
               if (oatFile.isFile() && oatFile.getName().startsWith(nameStart)) {
                  oatFile.delete();
               }
            }

         }
      }
   }

   DexFile getDexFile() {
      if (this.outputDex == null) {
         DexOptions options = new DexOptions();
         options.minSdkVersion = 13;
         this.outputDex = new DexFile(options);
      }

      return this.outputDex;
   }

   static class MethodDeclaration {
      final MethodId<?, ?> method;
      private final int flags;
      private final Code code;

      public MethodDeclaration(MethodId<?, ?> method, int flags) {
         this.method = method;
         this.flags = flags;
         this.code = new Code(this);
      }

      boolean isStatic() {
         return (this.flags & 8) != 0;
      }

      boolean isDirect() {
         return (this.flags & 65546) != 0;
      }

      EncodedMethod toEncodedMethod(DexOptions dexOptions) {
         RopMethod ropMethod = new RopMethod(this.code.toBasicBlocks(), 0);
         LocalVariableInfo locals = null;
         DalvCode dalvCode = RopTranslator.translate(ropMethod, 1, (LocalVariableInfo)locals, this.code.paramSize(), dexOptions);
         return new EncodedMethod(this.method.constant, this.flags, dalvCode, StdTypeList.EMPTY);
      }
   }

   static class FieldDeclaration {
      final FieldId<?, ?> fieldId;
      private final int accessFlags;
      private final Object staticValue;

      FieldDeclaration(FieldId<?, ?> fieldId, int accessFlags, Object staticValue) {
         if ((accessFlags & 8) == 0 && staticValue != null) {
            throw new IllegalArgumentException("instance fields may not have a value");
         } else {
            this.fieldId = fieldId;
            this.accessFlags = accessFlags;
            this.staticValue = staticValue;
         }
      }

      EncodedField toEncodedField() {
         return new EncodedField(this.fieldId.constant, this.accessFlags);
      }

      public boolean isStatic() {
         return (this.accessFlags & 8) != 0;
      }
   }

   static class TypeDeclaration {
      private final TypeId<?> type;
      private boolean declared;
      private int flags;
      private TypeId<?> supertype;
      private String sourceFile;
      private TypeList interfaces;
      private ClassDefItem classDefItem;
      private final Map<FieldId, FieldDeclaration> fields = new LinkedHashMap();
      private final Map<MethodId, MethodDeclaration> methods = new LinkedHashMap();

      TypeDeclaration(TypeId<?> type) {
         this.type = type;
      }

      ClassDefItem toClassDefItem() {
         if (!this.declared) {
            throw new IllegalStateException("Undeclared type " + this.type + " declares members: " + this.fields.keySet() + " " + this.methods.keySet());
         } else {
            DexOptions dexOptions = new DexOptions();
            dexOptions.minSdkVersion = 13;
            CstType thisType = this.type.constant;
            if (this.classDefItem == null) {
               this.classDefItem = new ClassDefItem(thisType, this.flags, this.supertype.constant, this.interfaces.ropTypes, new CstString(this.sourceFile));
               Iterator var3 = this.methods.values().iterator();

               while(var3.hasNext()) {
                  MethodDeclaration method = (MethodDeclaration)var3.next();
                  EncodedMethod encoded = method.toEncodedMethod(dexOptions);
                  if (method.isDirect()) {
                     this.classDefItem.addDirectMethod(encoded);
                  } else {
                     this.classDefItem.addVirtualMethod(encoded);
                  }
               }

               var3 = this.fields.values().iterator();

               while(var3.hasNext()) {
                  FieldDeclaration field = (FieldDeclaration)var3.next();
                  EncodedField encoded = field.toEncodedField();
                  if (field.isStatic()) {
                     this.classDefItem.addStaticField(encoded, Constants.getConstant(field.staticValue));
                  } else {
                     this.classDefItem.addInstanceField(encoded);
                  }
               }
            }

            return this.classDefItem;
         }
      }
   }
}
