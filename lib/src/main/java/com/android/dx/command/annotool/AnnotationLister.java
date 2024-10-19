package com.android.dx.command.annotool;

import com.android.dx.cf.attrib.BaseAnnotations;
import com.android.dx.cf.direct.ClassPathOpener;
import com.android.dx.cf.direct.DirectClassFile;
import com.android.dx.cf.direct.StdAttributeFactory;
import com.android.dx.cf.iface.Attribute;
import com.android.dx.cf.iface.AttributeList;
import com.android.dx.rop.annotation.Annotation;
import com.android.dx.util.ByteArray;
import java.io.File;
import java.lang.annotation.ElementType;
import java.util.HashSet;
import java.util.Iterator;

class AnnotationLister {
   private static final String PACKAGE_INFO = "package-info";
   private final Main.Arguments args;
   HashSet<String> matchInnerClassesOf = new HashSet();
   HashSet<String> matchPackages = new HashSet();

   AnnotationLister(Main.Arguments args) {
      this.args = args;
   }

   void process() {
      String[] var1 = this.args.files;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String path = var1[var3];
         ClassPathOpener opener = new ClassPathOpener(path, true, new ClassPathOpener.Consumer() {
            public boolean processFileBytes(String name, long lastModified, byte[] bytes) {
               if (!name.endsWith(".class")) {
                  return true;
               } else {
                  ByteArray ba = new ByteArray(bytes);
                  DirectClassFile cf = new DirectClassFile(ba, name, true);
                  cf.setAttributeFactory(StdAttributeFactory.THE_ONE);
                  AttributeList attributes = cf.getAttributes();
                  String cfClassName = cf.getThisClass().getClassType().getClassName();
                  Attribute att;
                  BaseAnnotations ann;
                  if (cfClassName.endsWith("package-info")) {
                     for(att = attributes.findFirst("RuntimeInvisibleAnnotations"); att != null; att = attributes.findNext(att)) {
                        ann = (BaseAnnotations)att;
                        AnnotationLister.this.visitPackageAnnotation(cf, ann);
                     }

                     for(att = attributes.findFirst("RuntimeVisibleAnnotations"); att != null; att = attributes.findNext(att)) {
                        ann = (BaseAnnotations)att;
                        AnnotationLister.this.visitPackageAnnotation(cf, ann);
                     }
                  } else if (!AnnotationLister.this.isMatchingInnerClass(cfClassName) && !AnnotationLister.this.isMatchingPackage(cfClassName)) {
                     for(att = attributes.findFirst("RuntimeInvisibleAnnotations"); att != null; att = attributes.findNext(att)) {
                        ann = (BaseAnnotations)att;
                        AnnotationLister.this.visitClassAnnotation(cf, ann);
                     }

                     for(att = attributes.findFirst("RuntimeVisibleAnnotations"); att != null; att = attributes.findNext(att)) {
                        ann = (BaseAnnotations)att;
                        AnnotationLister.this.visitClassAnnotation(cf, ann);
                     }
                  } else {
                     AnnotationLister.this.printMatch(cf);
                  }

                  return true;
               }
            }

            public void onException(Exception ex) {
               throw new RuntimeException(ex);
            }

            public void onProcessArchiveStart(File file) {
            }
         });
         opener.process();
      }

   }

   private void visitClassAnnotation(DirectClassFile cf, BaseAnnotations ann) {
      if (this.args.eTypes.contains(ElementType.TYPE)) {
         Iterator var3 = ann.getAnnotations().getAnnotations().iterator();

         while(var3.hasNext()) {
            Annotation anAnn = (Annotation)var3.next();
            String annClassName = anAnn.getType().getClassType().getClassName();
            if (this.args.aclass.equals(annClassName)) {
               this.printMatch(cf);
            }
         }

      }
   }

   private void visitPackageAnnotation(DirectClassFile cf, BaseAnnotations ann) {
      if (this.args.eTypes.contains(ElementType.PACKAGE)) {
         String packageName = cf.getThisClass().getClassType().getClassName();
         int slashIndex = packageName.lastIndexOf(47);
         if (slashIndex == -1) {
            packageName = "";
         } else {
            packageName = packageName.substring(0, slashIndex);
         }

         Iterator var5 = ann.getAnnotations().getAnnotations().iterator();

         while(var5.hasNext()) {
            Annotation anAnn = (Annotation)var5.next();
            String annClassName = anAnn.getType().getClassType().getClassName();
            if (this.args.aclass.equals(annClassName)) {
               this.printMatchPackage(packageName);
            }
         }

      }
   }

   private void printMatchPackage(String packageName) {
      Iterator var2 = this.args.printTypes.iterator();

      while(var2.hasNext()) {
         Main.PrintType pt = (Main.PrintType)var2.next();
         switch (pt) {
            case CLASS:
            case INNERCLASS:
            case METHOD:
               this.matchPackages.add(packageName);
               break;
            case PACKAGE:
               System.out.println(packageName.replace('/', '.'));
         }
      }

   }

   private void printMatch(DirectClassFile cf) {
      Iterator var2 = this.args.printTypes.iterator();

      while(var2.hasNext()) {
         Main.PrintType pt = (Main.PrintType)var2.next();
         switch (pt) {
            case CLASS:
               String classname = cf.getThisClass().getClassType().getClassName();
               classname = classname.replace('/', '.');
               System.out.println(classname);
               break;
            case INNERCLASS:
               this.matchInnerClassesOf.add(cf.getThisClass().getClassType().getClassName());
            case METHOD:
            case PACKAGE:
         }
      }

   }

   private boolean isMatchingInnerClass(String s) {
      while(true) {
         int i;
         if (0 < (i = s.lastIndexOf(36))) {
            s = s.substring(0, i);
            if (!this.matchInnerClassesOf.contains(s)) {
               continue;
            }

            return true;
         }

         return false;
      }
   }

   private boolean isMatchingPackage(String s) {
      int slashIndex = s.lastIndexOf(47);
      String packageName;
      if (slashIndex == -1) {
         packageName = "";
      } else {
         packageName = s.substring(0, slashIndex);
      }

      return this.matchPackages.contains(packageName);
   }
}
