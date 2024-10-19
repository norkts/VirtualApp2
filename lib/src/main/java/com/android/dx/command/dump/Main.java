package com.android.dx.command.dump;

import com.android.dex.util.FileUtils;
import com.android.dx.cf.iface.ParseException;
import com.android.dx.util.HexParser;
import java.io.UnsupportedEncodingException;

public class Main {
   private final Args parsedArgs = new Args();

   private Main() {
   }

   public static void main(String[] args) {
      (new Main()).run(args);
   }

   private void run(String[] args) {
      int at;
      String arg;
      for(at = 0; at < args.length; ++at) {
         arg = args[at];
         if (arg.equals("--") || !arg.startsWith("--")) {
            break;
         }

         if (arg.equals("--bytes")) {
            this.parsedArgs.rawBytes = true;
         } else if (arg.equals("--basic-blocks")) {
            this.parsedArgs.basicBlocks = true;
         } else if (arg.equals("--rop-blocks")) {
            this.parsedArgs.ropBlocks = true;
         } else if (arg.equals("--optimize")) {
            this.parsedArgs.optimize = true;
         } else if (arg.equals("--ssa-blocks")) {
            this.parsedArgs.ssaBlocks = true;
         } else if (arg.startsWith("--ssa-step=")) {
            this.parsedArgs.ssaStep = arg.substring(arg.indexOf(61) + 1);
         } else if (arg.equals("--debug")) {
            this.parsedArgs.debug = true;
         } else if (arg.equals("--dot")) {
            this.parsedArgs.dotDump = true;
         } else if (arg.equals("--strict")) {
            this.parsedArgs.strictParse = true;
         } else if (arg.startsWith("--width=")) {
            arg = arg.substring(arg.indexOf(61) + 1);
            this.parsedArgs.width = Integer.parseInt(arg);
         } else {
            if (!arg.startsWith("--method=")) {
               System.err.println("unknown option: " + arg);
               throw new RuntimeException("usage");
            }

            arg = arg.substring(arg.indexOf(61) + 1);
            this.parsedArgs.method = arg;
         }
      }

      if (at == args.length) {
         System.err.println("no input files specified");
         throw new RuntimeException("usage");
      } else {
         for(; at < args.length; ++at) {
            try {
               arg = args[at];
               System.out.println("reading " + arg + "...");
               byte[] bytes = FileUtils.readFile(arg);
               if (!arg.endsWith(".class")) {
                  String src;
                  try {
                     src = new String(bytes, "utf-8");
                  } catch (UnsupportedEncodingException var7) {
                     UnsupportedEncodingException ex = var7;
                     throw new RuntimeException("shouldn't happen", ex);
                  }

                  bytes = HexParser.parse(src);
               }

               this.processOne(arg, bytes);
            } catch (ParseException var8) {
               System.err.println("\ntrouble parsing:");
               if (this.parsedArgs.debug) {
                  var8.printStackTrace();
               } else {
                  var8.printContext(System.err);
               }
            }
         }

      }
   }

   private void processOne(String name, byte[] bytes) {
      if (this.parsedArgs.dotDump) {
         DotDumper.dump(bytes, name, this.parsedArgs);
      } else if (this.parsedArgs.basicBlocks) {
         BlockDumper.dump(bytes, System.out, name, false, this.parsedArgs);
      } else if (this.parsedArgs.ropBlocks) {
         BlockDumper.dump(bytes, System.out, name, true, this.parsedArgs);
      } else if (this.parsedArgs.ssaBlocks) {
         this.parsedArgs.optimize = false;
         SsaDumper.dump(bytes, System.out, name, this.parsedArgs);
      } else {
         ClassDumper.dump(bytes, System.out, name, this.parsedArgs);
      }

   }
}
