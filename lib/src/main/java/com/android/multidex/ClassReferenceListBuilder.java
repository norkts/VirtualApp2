package com.android.multidex;

import com.android.dx.cf.direct.DirectClassFile;
import com.android.dx.cf.iface.FieldList;
import com.android.dx.cf.iface.MethodList;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstBaseMethodRef;
import com.android.dx.rop.cst.CstFieldRef;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.Prototype;
import com.android.dx.rop.type.StdTypeList;
import com.android.dx.rop.type.TypeList;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ClassReferenceListBuilder {
   private static final String CLASS_EXTENSION = ".class";
   private final Path path;
   private final Set<String> classNames = new HashSet();

   public ClassReferenceListBuilder(Path path) {
      this.path = path;
   }

   /** @deprecated */
   @Deprecated
   public static void main(String[] args) {
      MainDexListBuilder.main(args);
   }

   public void addRoots(ZipFile jarOfRoots) throws IOException {
      Enumeration<? extends ZipEntry> entries = jarOfRoots.entries();

      ZipEntry entry;
      String name;
      while(entries.hasMoreElements()) {
         entry = (ZipEntry)entries.nextElement();
         name = entry.getName();
         if (name.endsWith(".class")) {
            this.classNames.add(name.substring(0, name.length() - ".class".length()));
         }
      }

      entries = jarOfRoots.entries();

      while(entries.hasMoreElements()) {
         entry = (ZipEntry)entries.nextElement();
         name = entry.getName();
         if (name.endsWith(".class")) {
            DirectClassFile classFile;
            try {
               classFile = this.path.getClass(name);
            } catch (FileNotFoundException var7) {
               FileNotFoundException e = var7;
               throw new IOException("Class " + name + " is missing form original class path " + this.path, e);
            }

            this.addDependencies(classFile);
         }
      }

   }

   Set<String> getClassNames() {
      return this.classNames;
   }

   private void addDependencies(DirectClassFile classFile) {
      Constant[] var2 = classFile.getConstantPool().getEntries();
      int nbField = var2.length;

      int i;
      for(i = 0; i < nbField; ++i) {
         Constant constant = var2[i];
         if (constant instanceof CstType) {
            this.checkDescriptor(((CstType)constant).getClassType().getDescriptor());
         } else if (constant instanceof CstFieldRef) {
            this.checkDescriptor(((CstFieldRef)constant).getType().getDescriptor());
         } else if (constant instanceof CstBaseMethodRef) {
            this.checkPrototype(((CstBaseMethodRef)constant).getPrototype());
         }
      }

      FieldList fields = classFile.getFields();
      nbField = fields.size();

      for(i = 0; i < nbField; ++i) {
         this.checkDescriptor(fields.get(i).getDescriptor().getString());
      }

      MethodList methods = classFile.getMethods();
      int nbMethods = methods.size();

      for(int i = 0; i < nbMethods; ++i) {
         this.checkPrototype(Prototype.intern(methods.get(i).getDescriptor().getString()));
      }

   }

   private void checkPrototype(Prototype proto) {
      this.checkDescriptor(proto.getReturnType().getDescriptor());
      StdTypeList args = proto.getParameterTypes();

      for(int i = 0; i < args.size(); ++i) {
         this.checkDescriptor(args.get(i).getDescriptor());
      }

   }

   private void checkDescriptor(String typeDescriptor) {
      if (typeDescriptor.endsWith(";")) {
         int lastBrace = typeDescriptor.lastIndexOf(91);
         if (lastBrace < 0) {
            this.addClassWithHierachy(typeDescriptor.substring(1, typeDescriptor.length() - 1));
         } else {
            assert typeDescriptor.length() > lastBrace + 3 && typeDescriptor.charAt(lastBrace + 1) == 'L';

            this.addClassWithHierachy(typeDescriptor.substring(lastBrace + 2, typeDescriptor.length() - 1));
         }
      }

   }

   private void addClassWithHierachy(String classBinaryName) {
      if (!this.classNames.contains(classBinaryName)) {
         try {
            DirectClassFile classFile = this.path.getClass(classBinaryName + ".class");
            this.classNames.add(classBinaryName);
            CstType superClass = classFile.getSuperclass();
            if (superClass != null) {
               this.addClassWithHierachy(superClass.getClassType().getClassName());
            }

            TypeList interfaceList = classFile.getInterfaces();
            int interfaceNumber = interfaceList.size();

            for(int i = 0; i < interfaceNumber; ++i) {
               this.addClassWithHierachy(interfaceList.getType(i).getClassName());
            }
         } catch (FileNotFoundException var7) {
         }

      }
   }
}
