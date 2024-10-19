package com.android.multidex;

import com.android.dx.cf.attrib.AttRuntimeVisibleAnnotations;
import com.android.dx.cf.direct.DirectClassFile;
import com.android.dx.cf.iface.Attribute;
import com.android.dx.cf.iface.FieldList;
import com.android.dx.cf.iface.HasAttribute;
import com.android.dx.cf.iface.MethodList;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.zip.ZipFile;

public class MainDexListBuilder {
   private static final String CLASS_EXTENSION = ".class";
   private static final int STATUS_ERROR = 1;
   private static final String EOL = System.getProperty("line.separator");
   private static final String USAGE_MESSAGE;
   private static final String DISABLE_ANNOTATION_RESOLUTION_WORKAROUND = "--disable-annotation-resolution-workaround";
   private Set<String> filesToKeep = new HashSet();

   public static void main(String[] args) {
      int argIndex = 0;

      boolean keepAnnotated;
      for(keepAnnotated = true; argIndex < args.length - 2; ++argIndex) {
         if (args[argIndex].equals("--disable-annotation-resolution-workaround")) {
            keepAnnotated = false;
         } else {
            System.err.println("Invalid option " + args[argIndex]);
            printUsage();
            System.exit(1);
         }
      }

      if (args.length - argIndex != 2) {
         printUsage();
         System.exit(1);
      }

      try {
         MainDexListBuilder builder = new MainDexListBuilder(keepAnnotated, args[argIndex], args[argIndex + 1]);
         Set<String> toKeep = builder.getMainDexList();
         printList(toKeep);
      } catch (IOException var5) {
         IOException e = var5;
         System.err.println("A fatal error occured: " + e.getMessage());
         System.exit(1);
      }
   }

   public MainDexListBuilder(boolean keepAnnotated, String rootJar, String pathString) throws IOException {
      ZipFile jarOfRoots = null;
      Path path = null;
      boolean var19 = false;

      try {
         try {
            var19 = true;
            jarOfRoots = new ZipFile(rootJar);
         } catch (IOException var24) {
            IOException e = var24;
            throw new IOException("\"" + rootJar + "\" can not be read as a zip archive. (" + e.getMessage() + ")", e);
         }

         path = new Path(pathString);
         ClassReferenceListBuilder mainListBuilder = new ClassReferenceListBuilder(path);
         mainListBuilder.addRoots(jarOfRoots);
         Iterator var7 = mainListBuilder.getClassNames().iterator();

         while(true) {
            if (!var7.hasNext()) {
               if (keepAnnotated) {
                  this.keepAnnotated(path);
                  var19 = false;
               } else {
                  var19 = false;
               }
               break;
            }

            String className = (String)var7.next();
            this.filesToKeep.add(className + ".class");
         }
      } finally {
         if (var19) {
            try {
               jarOfRoots.close();
            } catch (IOException var21) {
            }

            if (path != null) {
               Iterator var10 = path.elements.iterator();

               while(var10.hasNext()) {
                  ClassPathElement element = (ClassPathElement)var10.next();

                  try {
                     element.close();
                  } catch (IOException var20) {
                  }
               }
            }

         }
      }

      try {
         jarOfRoots.close();
      } catch (IOException var23) {
      }

      if (path != null) {
         Iterator var27 = path.elements.iterator();

         while(var27.hasNext()) {
            ClassPathElement element = (ClassPathElement)var27.next();

            try {
               element.close();
            } catch (IOException var22) {
            }
         }
      }

   }

   public Set<String> getMainDexList() {
      return this.filesToKeep;
   }

   private static void printUsage() {
      System.err.print(USAGE_MESSAGE);
   }

   private static void printList(Set<String> fileNames) {
      Iterator var1 = fileNames.iterator();

      while(var1.hasNext()) {
         String fileName = (String)var1.next();
         System.out.println(fileName);
      }

   }

   private void keepAnnotated(Path path) throws FileNotFoundException {
      Iterator var2 = path.getElements().iterator();

      label52:
      while(var2.hasNext()) {
         ClassPathElement element = (ClassPathElement)var2.next();
         Iterator var4 = element.list().iterator();

         while(true) {
            label48:
            while(true) {
               String name;
               do {
                  if (!var4.hasNext()) {
                     continue label52;
                  }

                  name = (String)var4.next();
               } while(!name.endsWith(".class"));

               DirectClassFile clazz = path.getClass(name);
               if (this.hasRuntimeVisibleAnnotation(clazz)) {
                  this.filesToKeep.add(name);
               } else {
                  MethodList methods = clazz.getMethods();

                  for(int i = 0; i < methods.size(); ++i) {
                     if (this.hasRuntimeVisibleAnnotation(methods.get(i))) {
                        this.filesToKeep.add(name);
                        continue label48;
                     }
                  }

                  FieldList fields = clazz.getFields();

                  for(int i = 0; i < fields.size(); ++i) {
                     if (this.hasRuntimeVisibleAnnotation(fields.get(i))) {
                        this.filesToKeep.add(name);
                        break;
                     }
                  }
               }
            }
         }
      }

   }

   private boolean hasRuntimeVisibleAnnotation(HasAttribute element) {
      Attribute att = element.getAttributes().findFirst("RuntimeVisibleAnnotations");
      return att != null && ((AttRuntimeVisibleAnnotations)att).getAnnotations().size() > 0;
   }

   static {
      USAGE_MESSAGE = "Usage:" + EOL + EOL + "Short version: Don't use this." + EOL + EOL + "Slightly longer version: This tool is used by mainDexClasses script to build" + EOL + "the main dex list." + EOL;
   }
}
