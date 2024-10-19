package com.android.dx.command.annotool;

import java.lang.annotation.ElementType;
import java.util.EnumSet;
import java.util.Locale;

public class Main {
   private Main() {
   }

   public static void main(String[] argArray) {
      Arguments args = new Arguments();

      try {
         args.parse(argArray);
      } catch (InvalidArgumentException var3) {
         InvalidArgumentException ex = var3;
         System.err.println(ex.getMessage());
         throw new RuntimeException("usage");
      }

      (new AnnotationLister(args)).process();
   }

   static class Arguments {
      String aclass;
      EnumSet<ElementType> eTypes = EnumSet.noneOf(ElementType.class);
      EnumSet<PrintType> printTypes = EnumSet.noneOf(PrintType.class);
      String[] files;

      void parse(String[] argArray) throws InvalidArgumentException {
         for(int i = 0; i < argArray.length; ++i) {
            String arg = argArray[i];
            String argParam;
            if (arg.startsWith("--annotation=")) {
               argParam = arg.substring(arg.indexOf(61) + 1);
               if (this.aclass != null) {
                  throw new InvalidArgumentException("--annotation can only be specified once.");
               }

               this.aclass = argParam.replace('.', '/');
            } else {
               String[] var5;
               int var6;
               int var7;
               String p;
               if (arg.startsWith("--element=")) {
                  argParam = arg.substring(arg.indexOf(61) + 1);

                  try {
                     var5 = argParam.split(",");
                     var6 = var5.length;

                     for(var7 = 0; var7 < var6; ++var7) {
                        p = var5[var7];
                        this.eTypes.add(ElementType.valueOf(p.toUpperCase(Locale.ROOT)));
                     }
                  } catch (IllegalArgumentException var10) {
                     throw new InvalidArgumentException("invalid --element");
                  }
               } else {
                  if (!arg.startsWith("--print=")) {
                     this.files = new String[argArray.length - i];
                     System.arraycopy(argArray, i, this.files, 0, this.files.length);
                     break;
                  }

                  argParam = arg.substring(arg.indexOf(61) + 1);

                  try {
                     var5 = argParam.split(",");
                     var6 = var5.length;

                     for(var7 = 0; var7 < var6; ++var7) {
                        p = var5[var7];
                        this.printTypes.add(Main.PrintType.valueOf(p.toUpperCase(Locale.ROOT)));
                     }
                  } catch (IllegalArgumentException var9) {
                     throw new InvalidArgumentException("invalid --print");
                  }
               }
            }
         }

         if (this.aclass == null) {
            throw new InvalidArgumentException("--annotation must be specified");
         } else {
            if (this.printTypes.isEmpty()) {
               this.printTypes.add(Main.PrintType.CLASS);
            }

            if (this.eTypes.isEmpty()) {
               this.eTypes.add(ElementType.TYPE);
            }

            EnumSet<ElementType> set = this.eTypes.clone();
            set.remove(ElementType.TYPE);
            set.remove(ElementType.PACKAGE);
            if (!set.isEmpty()) {
               throw new InvalidArgumentException("only --element parameters 'type' and 'package' supported");
            }
         }
      }
   }

   static enum PrintType {
      CLASS,
      INNERCLASS,
      METHOD,
      PACKAGE;
   }

   private static class InvalidArgumentException extends Exception {
      InvalidArgumentException() {
      }

      InvalidArgumentException(String s) {
         super(s);
      }
   }
}
